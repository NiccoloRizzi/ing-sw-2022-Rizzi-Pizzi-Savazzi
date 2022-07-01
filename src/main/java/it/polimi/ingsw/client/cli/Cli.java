package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.CharactersEnum;
import it.polimi.ingsw.model.Colour;

import java.util.Scanner;


public class Cli extends View {
    /**
     * Scanner for user input
     */
    private final static Scanner scanner = new Scanner(System.in);
    /**
     * Class for creating CliModel that can be printed
     */
    private CliBuilder cliBuilder;
    /**
     * Whether the client can start printing
     */
    private boolean printing = false;

    /**
     * Starting method for Cli which asks users for information such as Ip, Port, Nickname and match settings
     */
    public void start(){
        int playersNumber=0;
        String ip;
        int port=12345;
        String players;
        String nickname;
        boolean expertMode=false;
        boolean check = true;
        System.out.println("A quale ip vuoi connetterti?");
        ip = scanner.nextLine();
        if(!ip.matches("\\b(?:(?:2(?:[0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9])\\.){3}(?:(?:2([0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9]))\\b") && !ip.equals("localhost")){
            System.out.println("Illegal ip.");
            return;
        }
        System.out.println("A quale porta?");
        try {
            port = Integer.parseInt(scanner.nextLine());
        }catch(NumberFormatException e){
            System.out.println("Illegal Port.");
            return;
        }
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

    /**
     * Override method shared between Cli and Gui for starting the game
     */
    @Override
    public void startGame(){
        refresh(0);
    }

    /**
     * Method for enabling Cli printing
     */
    public void startPrint(){
        printing = true;
    }

    /**
     * Method for handling user input during matches
     */
    public void handleInput(){
            String command;
            while(true){
                command = scanner.nextLine();
                if(command.matches("exit")) {
                    System.out.println("Closing game...");
                    notifyClose();
                    return;
                }
                else if(command.matches("assistant "+"(1?0|[1-9])"))
                    super.ChooseAssistant(Integer.parseInt(command.split(" ")[1]));
                else if(command.matches("mvtotable "+"[0-4]"))
                    MoveToTable(Colour.values()[Integer.parseInt(command.split(" ")[1])]);
                else if(command.matches("mvtoisle "+"[0-4] "+"(1?[0-2]|[1-9])")) {
                    Colour colour = Colour.values()[Integer.parseInt(command.split(" ")[1])];
                    int isleid = Integer.parseInt(command.split(" ")[2])-1;
                    MoveToIsle(colour,isleid);
                }
                else if(command.matches("cloud "+"[1-4]")) {
                    ChooseCloud(Integer.parseInt(command.split(" ")[1]) - 1);
                }
                else if(command.matches("movemn "+"[1-9]"))
                    MoveMotherNature(Integer.parseInt(command.split(" ")[1]));
                else if(command.matches("colours"))
                    System.out.println("0: Green (Frogs)\n1: Rosso (Draghi)\n2: Giallo (Gnomi)\n3: Viola (Fate)\n4: Blu (Unicorni)");
                else if(command.matches("usecharacter "+"[1-3]"))
                    handleCharacter(Integer.parseInt(command.split(" ")[1])-1);
                else
                    System.out.println("Comando errato");
            }

    }


    /**
     * Method for handling user input for using characters
     * @param charpos The position of the activated character
     */
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
                            similMn(charpos, Integer.parseInt(read)-1);
                        else
                            System.out.print("Valore non corretto.");
                        }
                    case ONE_STUD_TO_ISLE -> {
                        System.out.println("Inserisci studente da spostare e isola su cui metterlo:");
                        read = scanner.nextLine();
                        if (read.matches("[0-4] " + "(1?[0-2]|[1-9])")) {
                            params = read.split(" ");
                            charStudToIsle(charpos, Colour.values()[Integer.parseInt(params[0])], Integer.parseInt(params[1])-1);
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
                                System.out.println("Scegli gli studenti da scambiare fra sala e ingresso: (\"coloreDaIngresso coloreDaSala)");
                                if((read=scanner.nextLine()).matches("[0-4] "+"[0-4]")){
                                    fromBoard[i] = Colour.values()[Integer.parseInt(read.split(" ")[0])];
                                    fromTables[i] = Colour.values()[Integer.parseInt(read.split(" ")[1])];
                                }
                                else{
                                    System.out.print("Studenti scelti non validi.");
                                    return;
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
                                System.out.println("Scegli gli studenti da scambiare fra sala e personaggio: (\"coloreDaIngresso coloreDaPersonaggio)");
                                if((read=scanner.nextLine()).matches("[0-4] "+"[0-4]")){
                                    fromBoard[i] = Colour.values()[Integer.parseInt(read.split(" ")[0])];
                                    fromChar[i] = Colour.values()[Integer.parseInt(read.split(" ")[1])];
                                }
                                else{
                                    System.out.print("Studenti scelti non validi.");
                                    return;
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
                            prohibit(charpos, Integer.parseInt(read)-1);
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

    /**
     * Method for refreshing Cli
     * @param index indicate which part of the view need to be updated
     */
    @Override
    public void refresh(int index){
        System.out.print("\033[H\033[2J");
        System.out.flush();
        if(printing){
            cliBuilder.setAllCli();
        }else{
            cliBuilder.setErrors();
        }
        cliBuilder.composeCLi();
        cliBuilder.getCli().print();
    }
}

