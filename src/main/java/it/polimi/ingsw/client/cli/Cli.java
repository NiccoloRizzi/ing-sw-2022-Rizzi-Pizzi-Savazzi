package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.CharactersEnum;
import it.polimi.ingsw.model.Colour;

import java.util.Scanner;


public class Cli extends View {

    private final static Scanner scanner = new Scanner(System.in);
    private CliBuilder cliBuilder;
    private boolean printing = false;



    public void start(){
        int playersNumber=0;
        String ip;
        int port;
        String players;
        String nickname;
        boolean expertMode=false;
        boolean check = true;
        System.out.println("A quale ip vuoi connetterti?");
        ip = scanner.nextLine();
        System.out.println("A quale porta?");
        port = Integer.parseInt(scanner.nextLine());
        notifyConnection(ip,port);
        do {
            System.out.println("Ciao! Come ti vuoi chiamare?");
            nickname = scanner.nextLine();
            if(nickname.length()<=3){
                System.out.println("Il nickname deve contenere almeno 4 caratteri!");
            }
            else{
                check=false;
            }
        }while(check);
        check=true;
        do {
            System.out.println("Quanti giocatori deve contenere la partita?");
            players = scanner.nextLine();
            try{
                playersNumber = Integer.parseInt(players);
                if(playersNumber<2 || playersNumber >4){
                    System.out.println("Il numero dev'essere compreso fra 2 e 4.");
                }else{
                    check = false;
                }
            }catch(NumberFormatException e){
                System.out.println("Devi inserire un numero compreso fra 2 e 4.");
            }
        }while(check);
        check=true;
        do {
            System.out.println("Vuoi giocare in modalità esperto? y/n");
            char choice = scanner.nextLine().charAt(0);
            if(choice == 'y' || choice == 'n'){
                expertMode = (choice=='y');
                check=false;
            }else{
                System.out.println("Rispondi con y(es) o n(o).");
            }
        }while(check);
        sendPlayerInfo(nickname,playersNumber,expertMode);
        cliBuilder = (getModelView().isExpert())?new CliSetter(getModelView()):new CliNotExpertSetter(getModelView());
        handleInput();
    }
    @Override
    public void startGame(){
        refresh();
    }

    public void startPrint(){
        printing = true;
    }

    public void handleInput(){
            String command;
            while(true){
                command = scanner.nextLine();
                if(command.matches("help")) {
                    System.out.println("Comandi:\n" +
                            "moveStudent colour index: sposta lo studente del colore \"colour\" all'isola 'index'.");
                }
                else if(command.matches("assistant "+"(1?0|[1-9])"))
                    super.ChooseAssistant(Integer.parseInt(command.split(" ")[1]));
                else if(command.matches("mvtotable "+"[0-4]"))
                    MoveToTable(Colour.values()[Integer.parseInt(command.split(" ")[1])]);
                else if(command.matches("mvtoisle "+"[0-4] "+"(1?[0-2]|[0-9])")) {
                    Colour colour = Colour.values()[Integer.parseInt(command.split(" ")[1])];
                    int isleid = Integer.parseInt(command.split(" ")[2]);
                    MoveToIsle(colour,isleid);
                }
                else if(command.matches("cloud "+"[0-4]")) {
                    ChooseCloud(Integer.parseInt(command.split(" ")[1]) - 1);
                }
                else if(command.matches("movemn "+"[0-9]"))
                    MoveMotherNature(Integer.parseInt(command.split(" ")[1]));
                else if(command.matches("colours"))
                    System.out.println("0: Red (Dragons)\n1: Violet (Fairies)\n2: Green (Frogs)\n3: Yellow (Gnomes)\n4: Blue (Unicorns)");
                else if(command.matches("usechracter "+"[1-3]"))
                    handleCharacter(Integer.parseInt(command.split(" ")[1]));
                else
                    System.out.println("Comando errato");
            }

    }




    public void handleCharacter(int charpos){
        String []params;
        String read;
        int number;
        Colour []fromBoard;
            CharactersEnum character = getModelView().getCharacters()[charpos].getCard();
            try {
                switch (character) {
                    case SIMIL_MN -> {
                        System.out.println("Inserisci su che isola vuoi effettuare il calcolo dell'influenza:");
                        read = scanner.nextLine();
                        if (read.matches("(1?[0-2]|[1-9])"))
                            similMn(charpos, Integer.parseInt(read));
                        else
                            System.out.print("Valore non corretto.");
                        }
                    case ONE_STUD_TO_ISLE -> {
                        System.out.println("Inserisci studente da spostare e isola su cui metterlo:");
                        read = scanner.nextLine();
                        if (read.matches("[0-4] " + "(1?[0-2]|[1-9])")) {
                            params = read.split(" ");
                            charStudToIsle(charpos, Colour.values()[Integer.parseInt(params[0])], Integer.parseInt(params[1]));
                        }
                        else
                            System.out.print("Valori non corretti.");
                    }
                    case NO_TOWER_INFLUENCE, PLUS_2_INFLUENCE -> useInfluenceCharacter(charpos);
                    case NO_COLOUR_INFLUENCE -> {
                        System.out.println("Scegli il colore da ignorare per il calcolo dell'influenza:");
                        read = scanner.nextLine();
                        if (read.matches("[0-4]"))
                            noColourInfluence(charpos, Colour.values()[Integer.parseInt(read)]);
                        else
                            System.out.print("Valore non corretto.");
                    }
                    case PLUS_2_MN -> motherNBoost(charpos);
                    case EXCHANGE_2_STUD -> {
                        System.out.println("Quanti studenti  vuoi spostare? (Da 1 a 2)");
                        if((number=Integer.parseInt(scanner.nextLine()))<=2 && number>0) {
                            fromBoard = new Colour[number];
                            Colour[] fromTables = new Colour[number];
                            for (int i = 0; i < number; i++) {
                                System.out.println("Scegli gli studenti da scambiare fra sala e ingresso: (\"coloreDaSala coloreDaIngresso)");
                                if((read=scanner.nextLine()).matches("[0-4] "+"[0-4]")){
                                    fromBoard[i] = Colour.values()[Integer.parseInt(read.split(" ")[0])];
                                    fromTables[i] = Colour.values()[Integer.parseInt(read.split(" ")[1])];
                                }
                                else{
                                    System.out.print("Studenti scelti non validi.");
                                    break;
                                }
                            }
                            exchange2Students(charpos, fromBoard, fromTables);
                        }
                        System.out.print("Numero non valido!");
                    }
                    case EXCHANGE_3_STUD -> {
                        System.out.println("Quanti studenti  vuoi spostare? (Da 1 a 3)");
                        if((number = Integer.parseInt(scanner.nextLine()))>0 && number<=3) {
                            fromBoard = new Colour[number];
                            Colour[] fromChar = new Colour[number];
                            for (int i = 0; i < number; i++) {
                                System.out.println("Scegli gli studenti da scambiare fra sala e personaggio: (\"coloreDaSala coloreDaPersonaggio)");
                                if((read=scanner.nextLine()).matches("[0-4] "+"[0-4]")){
                                    fromBoard[i] = Colour.values()[Integer.parseInt(read.split(" ")[0])];
                                    fromChar[i] = Colour.values()[Integer.parseInt(read.split(" ")[1])];
                                }
                                else{
                                    System.out.print("Studenti scelti non validi.");
                                    break;
                                }
                            }
                            exchange3Students(charpos, fromBoard, fromChar);
                        }
                    }
                    case ONE_STUD_TO_TABLES -> {
                        System.out.println("Scegli il colore dello studente da spostare da questa carta alla tua sala:");
                        read = scanner.nextLine();
                        if(read.matches("[0-4]"))
                            charStudToTable(charpos, Colour.values()[Integer.parseInt(read)]);
                    }
                    case PROFESSOR_CONTROL -> professorControl(charpos);
                    case PROHIBITED -> {
                        System.out.println("Scegli l'isola su cui vuoi aggiungere un divieto:");
                        read = scanner.nextLine();
                        if(read.matches("(1?[0-2]|[1-9])"))
                            prohibit(charpos, Integer.parseInt(read));
                    }
                    case REMOVE_3_STUD -> {
                        System.out.println("Scegli il colore dei 3 studenti da far rimuovere:");
                        read = scanner.nextLine();
                        if (read.matches("[0-4]"))
                            remove3Stud(charpos, Colour.values()[Integer.parseInt(read)]);
                    }
                }
            }catch(NumberFormatException e){
                System.out.print("Errore nel formato dei parametri.");
            }
        }

    public void refresh(){
        if(printing){
            cliBuilder.setAllCli();
        }else{
            cliBuilder.setErrors();
        }
        cliBuilder.composeCLi();
        cliBuilder.getCli().print();
    }
}

