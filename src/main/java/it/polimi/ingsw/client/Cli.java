package it.polimi.ingsw.client;

import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;

import javax.sound.midi.Soundbank;
import java.util.Scanner;


public class Cli {
    private View view;
    private final static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args){
        int playersNumber;
        System.out.println("Ciao! Come ti vuoi chiamare?");
        String nickname = scanner.nextLine();
        String players;
        boolean expertMode=false;
        boolean check = true;
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
        System.out.println(expertMode);
        handleInput();
    }

    public static void handleInput(){
        String command;
        while(true){
            command = scanner.nextLine();
            switch(command){
                case "help":
                    System.out.println("Comandi:\n" +
                             "moveStudent colour index: sposta lo studente del colore \"colour\" all'isola 'index'.");
            }
        }
    }
}
