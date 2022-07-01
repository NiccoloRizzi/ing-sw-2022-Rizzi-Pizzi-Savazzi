package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.clientModels.*;
import it.polimi.ingsw.clientModels.Answers.ErrorMessage;
import it.polimi.ingsw.clientModels.Answers.StartMessage;
import it.polimi.ingsw.clientModels.Answers.TurnMessage;
import it.polimi.ingsw.clientModels.Answers.WinMessage;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.server.Observable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class View extends Observable<JsonObject> {

    /**
     * ModelView containg all the needed information on the game
     */
    private ModelView modelView;
    /**
     * Gson needed to serialize the type of message
     */
    static final Gson gson = new Gson();

    /**
     * Getter of the ModelView
     * @return modelView
     */
    public ModelView getModelView() {
        return modelView;
    }

    /**
     * Visit of the ClientBoard message
     * @param clientBoard ClientBoard that need to be saved in the modelView
     */
    public synchronized void visit(ClientBoard clientBoard){
        modelView.getBoards()[clientBoard.getPlayerID()] = clientBoard;
        refresh(2);
    }

    /**
     * Visit of the ClientIsle message
     * @param clientIsle ClientIsle that need to be saved in the ModelView
     */
    public synchronized void visit(ClientIsle clientIsle){
        ClientGameModel clientGameModel = modelView.getGameModel();
        for(int i = 0; i < modelView.getGameModel().getIsles().size(); i++){
            if(clientGameModel.getIsles().get(i).getId() == clientIsle.getId()){
                clientGameModel.getIsles().set(i, clientIsle);
                break;
            }
        }
        refresh(3);
    }
    /**
     * Visit of the ClientPlayer message
     * @param clientPlayer ClientPlayer that need to be saved in the ModelView
     */
    public synchronized void visit(ClientPlayer clientPlayer){
        modelView.getPlayers()[clientPlayer.getId()]=clientPlayer;
        if(clientPlayer.getUsedAssistants().length > 0){
            Map<ClientPlayer, Integer> otherPLayerAss = modelView.getOtherPlayerAssistant();
            int usedAssLength = clientPlayer.getUsedAssistants().length;
            if(clientPlayer.getId() != getModelView().getMyId()){
                otherPLayerAss.put(clientPlayer, usedAssLength - 1);
            }
            modelView.setOtherPlayerAssistant(otherPLayerAss);
        }
        refresh(1);
    }

    /**
     * Visit of the ClientGameModel message
     * @param clientGameModel ClientGameModel that need to be saved in modelView
     */
    public synchronized void visit(ClientGameModel clientGameModel){
        modelView.setGameModel(clientGameModel);
        refresh(0);
    }

    /**
     * Visit of the ClientCloud message
     * @param clientCloud ClientCloud that need to be saved in modelView
     */
    public synchronized void visit(ClientCloud clientCloud){
        modelView.getClouds()[clientCloud.getId()] = clientCloud;
        refresh(3);
    }

    /**
     * Visit of the ClientCharacter message
     * @param character ClientCharacter that need to be saved in the modelView
     */
    public synchronized void visit(ClientCharacter character){
        modelView.getCharacters()[character.getID()] = character;
        getModelView().setCurrentCharacter(Optional.of(character.getID()));
        refresh(4);
    }

    /**
     * Visit of the ErrorMessage
     * @param errorMessage ErrorMessage containing the ErrorType that need to be saved in the modelView
     */
    public synchronized void visit(ErrorMessage errorMessage){
        if(errorMessage.getError()== ErrorMessage.ErrorType.PlayerDisconnected){
            modelView.setError(errorMessage.getError());
            refresh(6);
        }
        else if(errorMessage.getId()==modelView.getMyId()) {
            modelView.setError(errorMessage.getError());
            refresh(6);
        }
    }

    /**
     * Visit of the TurnMessage
     * @param turnMessage TurnMessage that need to be saved in the modelView
     */
    public synchronized void visit(TurnMessage turnMessage){
        if(turnMessage.getTurn() == TurnMessage.Turn.ACTION_STUDENTS){
            modelView.setCurrentCharacter(Optional.empty());
        }
        if(modelView.getTurn() != null && turnMessage.getTurn() == TurnMessage.Turn.PLANNING && modelView.getTurn().getTurn() == TurnMessage.Turn.ACTION_CLOUDS){
            modelView.setOtherPlayerAssistant(new HashMap<>());
        }
        modelView.setTurn(turnMessage);
        refresh(5);
    }

    /**
     * Visit of the WinMessage
     * @param winMessage WinMessage that need to be saved in the modelView
     */
    public synchronized void visit(WinMessage winMessage) {
        modelView.setWin(winMessage);
        refresh(6);
    }

    /**
     * visit of the StartMessage, set up of the modelView and start of the CLI or GUI
     * @param startMessage StartMessage containing the nickname assigned to the player
     */
    public synchronized void visit(StartMessage startMessage){
        modelView.setMyId(startMessage.getId(modelView.getNickname()));
        startPrint();
        getModelView().setCurrentCharacter(Optional.empty());
        startGame();
    }

    /**
     * Method to create with the message to choose an assistant
     * @param assistant ID of the chosen assistant
     */
    public void ChooseAssistant(int assistant){
        if(assistant>0 && assistant<11) {
            AssistantChoiceMessage acm = new AssistantChoiceMessage(assistant - 1, modelView.getMyId());
            notifyClient(acm.serialize());
        }
    }

    /**
     * Method to create with the message to move a student on an isle
     * @param c Colour of the student to move
     * @param isleid ID of the chosen isle
     */
    public void MoveToIsle(Colour c, int isleid){
        MoveStudentMessage msm = new MoveStudentMessage(modelView.getMyId(), c, isleid, false);
        notifyClient(msm.serialize());
    }

    /**
     * Method to create with the message to move a student to the tables
     * @param c Colour of the student to move
     */
    public void MoveToTable(Colour c){
        MoveStudentMessage msm = new MoveStudentMessage(modelView.getMyId(),c,0,true);
        notifyClient(msm.serialize());
    }

    /**
     * Method to create with the message to choose a cloud
     * @param cloudid ID of the chosen cloud
     */
    public void ChooseCloud(int cloudid){
        CloudChoiceMessage ccm = new CloudChoiceMessage(modelView.getMyId(), cloudid);
        notifyClient(ccm.serialize());
    }

    /**
     * Method to notify the observer with the message to move mothernature
     * @param spaces number steps to move
     */
    public void MoveMotherNature(int spaces){
        MoveMotherNatureMessage movemnm = new MoveMotherNatureMessage(modelView.getMyId(), spaces);
        notifyClient(movemnm.serialize());
    }

    /**
     * Method to create with the message to remove three student of the same colour
     * @param charpos ID of the character
     * @param c Colour of the student to remove
     */
    public void remove3Stud(int charpos, Colour c){
        Remove3StudCharacterMessage r3s = new Remove3StudCharacterMessage(charpos, modelView.getMyId(),c);
        notifyClient(r3s.serialize());
    }

    /**
     * Method to notify the observer with the message to move a student from the character to an isle
     * @param charpos ID of the character
     * @param student Colour of the student to move
     * @param isle ID of the chose isle
     */
    public void charStudToIsle(int charpos, Colour student, int isle){
        MoveStudentCharacterMessage mscm = new MoveStudentCharacterMessage(modelView.getMyId(), charpos, student,isle );
        notifyClient(mscm.serialize());
    }

    /**
     * Method to create to move a student from the characters to the tabel
     * @param charpos   ID of the character
     * @param student   Colour of the student to move
     */
    public void charStudToTable(int charpos, Colour student){
        MoveStudentCharacterMessage mscm = new MoveStudentCharacterMessage(modelView.getMyId(), charpos, student,0);
        notifyClient(mscm.serialize());
    }

    /**
     * Message to notify the observer with the message to use the character to calculate the influence on an isle
     * @param charpos ID of the character
     * @param isle ID of the chose isle
     */
    public void similMn(int charpos, int isle){
        SimilMotherNatureMesage smm = new SimilMotherNatureMesage(charpos, modelView.getMyId(), isle);
        notifyClient(smm.serialize());
    }

    /**
     * Method to notify the observer with the message to use one of the characters to change the strategy used to calculate the influence
     * @param charpos ID of the character
     */
    public void useInfluenceCharacter(int charpos){
        IsleInfluenceCharacterMessage iicm = new IsleInfluenceCharacterMessage(charpos, modelView.getMyId());
        notifyClient(iicm.serialize());
    }

    /**
     * Method to create with the message to use the character to place a prohibited tile on an isle
     * @param charpos ID of the character
     * @param isle ID of the chose isle
     */
    public void prohibit(int charpos, int isle){
        ProhibitedIsleCharacterMessage pm = new ProhibitedIsleCharacterMessage(charpos, modelView.getMyId(), isle);
        notifyClient(pm.serialize());
    }

    /**
     * Mehtod to create with the message to use the character to change the strategy to take control of the professors
     * @param charpos ID of the character
     */
    public void professorControl(int charpos){
        StrategyProfessorMessage strategyProfessorMessage = new StrategyProfessorMessage(charpos, modelView.getMyId());
        notifyClient(strategyProfessorMessage.serialize());
    }

    /**
     * Mehtod to create with the message to use the character to add 2 to the number of possible step
     * @param charpos ID of the character
     */
    public void motherNBoost(int charpos){
        Plus2MoveMnMessage plus = new Plus2MoveMnMessage(charpos, modelView.getMyId());
        notifyClient(plus.serialize());
    }

    /**
     * Method to create with the message to use the character to change the strategy to calculate the influence to the one that ignores a chosen colour
     * @param charpos ID of the character
     * @param ignored Colour that need to be ignored
     */
    public void noColourInfluence(int charpos, Colour ignored){
        IsleInfluenceCharacterMessage icm = new IsleInfluenceCharacterMessage(charpos, modelView.getMyId(),ignored);
        notifyClient(icm.serialize());
    }

    /**
     * Method to create to use the character to exchange up to 2 student between the entrance and the tables
     * @param charpos ID of the characters
     * @param fromBoard Colours of the student to exchange from the entrance
     * @param fromtables Colours of the student to exchange from the tables
     */
    public void exchange2Students(int charpos, Colour[] fromBoard, Colour[] fromtables){
        Move2StudCharacterMessage m2m = new Move2StudCharacterMessage(charpos,modelView.getMyId(),fromBoard,fromtables);
        notifyClient(m2m.serialize());
    }

    /**
     * Method to create the message to sue the character to exchange three student between the ones on the character and the entrance
     * @param charpos ID of the character
     * @param fromBoard Colours of the student to exchange from the board
     * @param fromChar Colours of the student to exchange from the character
     */
    public void exchange3Students(int charpos, Colour[] fromBoard, Colour[] fromChar ){
        Move6StudCharacterMessage m3m = new Move6StudCharacterMessage(charpos, modelView.getMyId(), fromBoard,fromChar);
        notifyClient(m3m.serialize());
    }

    /**
     * Method to create the message containing the player information
     * @param nickname nickname of the player
     * @param nplayers preferred number of playe for the game
     * @param expertMode preferred type of game
     */
    public void sendPlayerInfo(String nickname, int nplayers, boolean expertMode){
        modelView = new ModelView(nickname,nplayers,expertMode);
        PlayerMessage pm = new PlayerMessage(nickname,nplayers,expertMode);
        notifyClient(MessageSerializer.serialize(pm));
    }

    /**
     * Method to notify the observers with the message
     * @param message message to send
     */
    public void notifyClient(String message){
        modelView.setError(null);
        JsonObject jo = gson.fromJson(message,JsonObject.class);
        jo.addProperty("command","message");
        notify(jo);
    }

    /**
     * Method to notify the observers with the informetion on the server to connect
     * @param ip ip of the server
     * @param port port of the server
     */
    public void notifyConnection(String ip, int port){
        JsonObject jo = new JsonObject();
        jo.addProperty("ip",ip);
        jo.addProperty("port",port);
        jo.addProperty("command","connect");
        notify(jo);
    }

    /**
     * Method to set up the view if needed
     */
    public abstract void start();

    /**
     * Method to start the print on command line if needed
     */
    public abstract void startPrint();

    /**
     * Method to start the game fo the view
     */
    public abstract void startGame();

    /**
     * Method to update the information visualized by the view
     * @param index indicate which part of the view need to be updated
     */
    public abstract void refresh(int index);
}
