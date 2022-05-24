package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.messages.PlayerMessage;
import it.polimi.ingsw.model.CharactersEnum;
import it.polimi.ingsw.model.Colour;

import java.io.IOException;
import java.util.Scanner;


public class Cli extends View {
    private final static Scanner scanner = new Scanner(System.in);
    private CliBuilder cliSetter;
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
        cliSetter = new CliSetter(getModelView());
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
        String []command;
        while(true){
            int param;
            command = scanner.nextLine().split(" ",5);
            switch(command[0]){
                case "help":
                    System.out.println("Comandi:\n" +
                            "moveStudent colour index: sposta lo studente del colore \"colour\" all'isola 'index'.");
                    break;
                case "assistant":
                    try {
                        super.ChooseAssistant(Integer.parseInt(command[1]));
                    }catch(NumberFormatException e){
                        System.out.println("Devi inserire un numero!");
                    }
                    break;
                case "mvtotable":
                    if(command.length==2)
                        MoveToTable(Colour.values()[Integer.parseInt(command[1])]);
                    break;
                case "mvtoisle":
                    if(command.length==3)
                        MoveToIsle(Colour.values()[Integer.parseInt(command[1])],Integer.parseInt(command[2])-1);
                    break;
                case "cloud":
                    if(command.length==2)
                        ChooseCloud(Integer.parseInt(command[1])-1);
                    break;
                case "movemn":
                    if(command.length==2)
                        MoveMotherNature(Integer.parseInt(command[1]));
                    break;
                case "colours":
                    System.out.println("0: Red (Dragons)\n1: Violet (Fairies)\n2: Green (Frogs)\n3: Yellow (Gnomes)\n4: Blue (Unicorns)");
                    break;
                case "usecharacter":
                    handleCharacter(command);
                    break;
                default:
                    System.out.println("Comando errato");
                    break;

            }
        }
    }

    public void handleCharacter(String []command){
        String []params;
        int param;
        int []intParams = new int[5];
        int number;
        Colour []fromBoard;
        if(command.length>1) {
            int charpos = Integer.parseInt(command[1]);
            CharactersEnum character = getModelView().getCharacters()[charpos].getCard();
            switch(character){
                case SIMIL_MN:
                    System.out.println("Inserisci su che isola vuoi effettuare il calcolo dell'influenza:");
                    param = Integer.parseInt(scanner.nextLine());
                    if(param>=0 && param<=12)
                        similMn(charpos,param);
                    break;
                case ONE_STUD_TO_ISLE:
                    System.out.println("Inserisci studente da spostare e isola su cui metterlo:");
                    params = scanner.nextLine().split(" ",2);
                    charStudToIsle(charpos,Colour.values()[Integer.parseInt(params[0])],Integer.parseInt(params[1]));
                    break;
                case NO_TOWER_INFLUENCE,PLUS_2_INFLUENCE:
                    useInfluenceCharacter(charpos);
                    break;
                case NO_COLOUR_INFLUENCE:
                    System.out.println("Scegli il colore da ignorare per il calcolo dell'influenza:");
                    param = Integer.parseInt(scanner.nextLine());
                    if(param>=0 && param<=4)
                        noColourInfluence(charpos,Colour.values()[param]);
                    break;
                case PLUS_2_MN:
                    motherNBoost(charpos);
                    break;
                case EXCHANGE_2_STUD:
                    System.out.println("Quanti studenti  vuoi spostare? (Da 1 a 2)");
                    number = Integer.parseInt(scanner.nextLine());
                    fromBoard = new Colour[number];
                    Colour[] fromTables = new Colour[number];
                    for(int i=0; i<number;i++) {
                        System.out.println("Scegli gli studenti da scambiare fra sala e ingresso: (\"coloreDaSala coloreDaIngresso)");
                        params = scanner.nextLine().split(" ", 2);
                        fromBoard[i] = Colour.values()[Integer.parseInt(params[0])];
                        fromTables[i] = Colour.values()[Integer.parseInt(params[1])];
                    }
                    exchange3Students(charpos,fromBoard,fromTables);
                    break;
                case EXCHANGE_3_STUD:
                    System.out.println("Quanti studenti  vuoi spostare? (Da 1 a 3)");
                    number = Integer.parseInt(scanner.nextLine());
                    fromBoard = new Colour[number];
                    Colour[] fromChar = new Colour[number];
                    for(int i=0; i<number;i++) {
                        System.out.println("Scegli gli studenti da scambiare fra sala e personaggio: (\"coloreDaSala coloreDaPersonaggio)");
                        params = scanner.nextLine().split(" ", 2);
                        fromBoard[i] = Colour.values()[Integer.parseInt(params[0])];
                        fromChar[i] = Colour.values()[Integer.parseInt(params[1])];
                    }
                    exchange3Students(charpos,fromBoard,fromChar);
                    break;
                case ONE_STUD_TO_TABLES:
                    System.out.println("Scegli il colore dello studente da spostare da questa carta alla tua sala:");
                    param = Integer.parseInt(scanner.nextLine());
                    if(param>=0 && param<=5)
                        charStudToTable(charpos,Colour.values()[param]);
                    break;
                case PROFESSOR_CONTROL:
                    professorControl(charpos);
                    break;
                case PROHIBITED:
                    System.out.println("Scegli l'isola su cui vuoi aggiungere un divieto:");
                    param = Integer.parseInt(scanner.nextLine());
                    if(param>=0 && param<=12)
                        prohibit(charpos,param);
                    break;
                case REMOVE_3_STUD:
                    System.out.println("Scegli il colore dei 3 studenti da far rimuovere:");
                    param = Integer.parseInt(scanner.nextLine());
                    if(param>=0 && param<=5)
                        remove3Stud(charpos,Colour.values()[param]);
                    break;
            }
        }
    }

    public void constructCli(){
        cliSetter.setAllCli();
        cliSetter.composeCLi();
    }

    public void refresh(){
        if(printing){
            constructCli();
            CliModel cli = cliSetter.getCli();
            cli.print();
        }else{
            cliSetter.setErrors();
            cliSetter.composeCLi();
            cliSetter.getCli().print();
        }
    }
}

