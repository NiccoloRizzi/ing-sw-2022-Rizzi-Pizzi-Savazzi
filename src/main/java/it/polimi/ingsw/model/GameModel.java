package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameModel {
    private int unusedCoins;
    private int motherNature;
    private ArrayList<Student> bag;
    private ArrayList<Player> players;
    protected HashMap<Colour,Player> professors;
    private ArrayList<Isle> isles;
    private ArrayList<Cloud> clouds;
    private ArrayList<Character> characters;
    private ArrayList<Team> teams;
    private ArrayList<Character> activeCharacters;

    public GameModel(){
        unusedCoins = 20;
        motherNature = (new Random()).nextInt(12);
        bag = new ArrayList<Student>();
        generateBag();
        players = new ArrayList<Player>();
        isles = new ArrayList<Isle>();
        generateIsle();
        clouds = new ArrayList<Cloud>();
        characters = new ArrayList<Character>();
        activeCharacters= new ArrayList<Character>();
        professors = new HashMap<Colour,Player>();
    }

    private void generateBag()
    {
        for(Colour c: Colour.values())
        {
            for(int i = 0; i<26; i++)
                bag.add(new Student(c));
        }
    }

    private void generateIsle()
    {
        for(int i = 0; i<12; i++)
            isles.add(new Isle());
    }

    public int getMotherNature() {
        return motherNature;
    }

    public ArrayList<Student> extractStudents(int num){
        ArrayList<Student> extractedStud = new ArrayList<>();
        Random rand = new Random();
        // EXCEPTION
        if(num >= 0 && num < bag.size()){
            for(int i = 0; i < num; i++){
                int randomPos = rand.nextInt(bag.size());
                extractedStud.add(bag.remove(randomPos));
            }
            return extractedStud;
        }
        return null;
    }
    public void moveMN(int moves)
    {
        motherNature = (motherNature+moves)%12;
    }
    public Player getPlayer(int playerID) {
        for(Player p : players){
            if(p.getID() == playerID){
                return p;
            }
        }
        return null;
    }

    public Isle getIsle(int isleID){
        // FOR EXCEPTION
        if(isleID >= 0 && isleID < isles.size()){
            return isles.get(isleID);
        }
        return null;
    }

    public Cloud getCloud(int cloudID){
        // FOR EXCEPTION
        if(cloudID >= 0 && cloudID < clouds.size()){
            return clouds.get(cloudID);
        }
        return null;
    }

    public Team getTeam(int teamID){
        // FOR EXCEPTION
        if(teamID >= 0 && teamID < teams.size()){
            return teams.get(teamID);
        }
        return null;
    }

    public int getCoins(){
        return unusedCoins;
    }

    public void addCoin(int numCoins){
        this.unusedCoins += numCoins;
    }

    public void setMotherNPos(int isleID){
        this.motherNature = isleID;
    }

    public Student getRandomStudent(){
        Random rand = new Random();
        return bag.get(rand.nextInt(bag.size()));
    }

    public void addStudent(ArrayList<Student> students){
        bag.addAll(students);
    }

    public Player getProfessor (Colour c)
    {
        return professors.get(c);
    }
    public void setProfessor(Colour c, Player p)
    {
        professors.replace(c,p);
    }
}
