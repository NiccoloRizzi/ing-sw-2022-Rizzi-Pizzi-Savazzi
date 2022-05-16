package it.polimi.ingsw.client;

import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;
import it.polimi.ingsw.messages.PlayerMessage;
import it.polimi.ingsw.model.Colour;

import java.io.IOException;
import java.util.Scanner;


public class Cli{
    private static View view;
    private final static Scanner scanner = new Scanner(System.in);
    private static Client client;
    
    public static void main(String[] args){
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
        try {
            client = new Client("127.0.0.1", 12345);
            client.setNickname(nickname);
            client.setPn(playersNumber);
            client.setExpert(expertMode);
            client.startView();
            new Thread(()-> {
                try {
                    client.run();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }catch(IOException e){
            e.printStackTrace();
        }
        handleInput();
    }

    public static void handleInput(){
        String []command;
        String cm;
        while(true){
            int param;
            command = scanner.nextLine().split(" ",5);
            switch(command[0]){
                case "help":
                    System.out.println("Comandi:\n" +
                             "moveStudent colour index: sposta lo studente del colore \"colour\" all'isola 'index'.");
                    break;
                case "chooseassistant":
                    try {
                        client.ChooseAssistant(Integer.parseInt(command[1]));
                    }catch(NumberFormatException e){
                        System.out.println("Devi inserire un numero!");
                        }
                    break;
                case "mvtotable":
                    client.MoveToTable(Colour.values()[Integer.parseInt(command[1])]);
                    break;
                case "mvtoisle":
                    client.MoveToIsle(Colour.values()[Integer.parseInt(command[1])],Integer.parseInt(command[2]));
                    break;
                case "cloud":
                    client.ChooseCloud(Integer.parseInt(command[1]));
                    break;
                case "movemn":
                    client.MoveMotherNature(Integer.parseInt(command[1]));
                    break;

            }
        }
    }
}

