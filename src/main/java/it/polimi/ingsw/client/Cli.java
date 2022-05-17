package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.CharactersEnum;
import it.polimi.ingsw.model.Colour;

import java.io.IOException;
import java.util.Scanner;


public class Cli extends View{
    private final static Scanner scanner = new Scanner(System.in);
    private static Client client;
    private CliUtil cliUtil;
    private boolean printing = false;


    public void startCli(){
        int playersNumber=0;
        String players;
        String nickname;
        boolean expertMode=false;
        boolean check = true;
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
        client = new Client(this);
        client.setOptions(nickname,playersNumber,expertMode);
        setupModel(client);
        new Thread(()-> {
            try {
                client.startConnection("127.0.0.1",12345);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        handleInput();
    }

    public void startPrint(){
        printing = true;
        cliUtil = new CliUtil(getModelView(), client.getId());
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
                    MoveToTable(Colour.values()[Integer.parseInt(command[1])]);
                    break;
                case "mvtoisle":
                    MoveToIsle(Colour.values()[Integer.parseInt(command[1])],Integer.parseInt(command[2]));
                    break;
                case "cloud":
                    ChooseCloud(Integer.parseInt(command[1]));
                    break;
                case "movemn":
                    MoveMotherNature(Integer.parseInt(command[1]));
                    break;
                default:
                    System.out.println("Comando errato");
                    break;

            }
        }


    }
    public void refresh(){
        if(printing)cliUtil.printCells(cliUtil.generateGame());
    }
}

