package it.polimi.ingsw;

import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ArrayList<Integer> test = new ArrayList<>();
        test.add(0);
        test.add(1);
        test.add(2);
//        for(Integer i : test){
//            System.out.println(i);
//        }
//        System.out.println(test.size());
        System.out.println(test.get(1));
        test.remove(0);
        System.out.println(test.get(1));
//        for(Integer i : test){
//            System.out.println(i);
//        }
//        System.out.println(test.size());
        System.out.println( "Hello World!" );
    }
}
