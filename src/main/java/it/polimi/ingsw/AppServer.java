package it.polimi.ingsw;

import it.polimi.ingsw.server.Server;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class AppServer
{
    public static void main( String[] args )
    {
        int port = 12345;
        Scanner scanner = new Scanner(System.in);
        System.out.println("On what port do you want to run the server?");
        try {
            port = Integer.parseInt(scanner.nextLine());
        }catch(NumberFormatException e){
            System.out.println("Unable to get port, running on default.");
        }
        Server server = new Server(port);
        server.run();
    }
}
