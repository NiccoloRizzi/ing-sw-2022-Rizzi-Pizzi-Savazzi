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

public abstract class View extends Observable<JsonObject> {

    private ModelView modelView;
    static Gson gson = new Gson();

    public ModelView getModelView() {
        return modelView;
    }

   /* public synchronized void sendMessage(String messageToSend) {
        notify(messageToSend);
    }*/


    public synchronized void visit(ClientBoard clientBoard){
        modelView.getBoards()[clientBoard.getPlayerID()] = clientBoard;
        refresh();
    }
    public synchronized void visit(ClientIsle clientIsle){
        ClientGameModel clientGameModel = modelView.getGameModel();
        for(int i = 0; i < modelView.getGameModel().getIsles().size(); i++){
            if(clientGameModel.getIsles().get(i).getId() == clientIsle.getId()){
                clientGameModel.getIsles().set(i, clientIsle);
                break;
            }
        }
        refresh();
    }
    public synchronized void visit(ClientPlayer clientPLayer){
        modelView.getPlayers()[clientPLayer.getId()]=clientPLayer;
    }
    public synchronized void visit(ClientGameModel clientGameModel){
        modelView.setGameModel(clientGameModel);
        refresh();
    }
    public synchronized void visit(ClientCloud clientCloud){
        modelView.getClouds()[clientCloud.getId()] = clientCloud;
        refresh();
    }
    public synchronized void visit(ClientCharacter character){
        modelView.getCharacters()[character.getID()] = character;
        refresh();
    }
    public synchronized void visit(ErrorMessage errorMessage){
        if(errorMessage.getError()== ErrorMessage.ErrorType.PlayerDisconnected){
            modelView.setError(errorMessage.getError());
            refresh();
        }
        else if(errorMessage.getId()==modelView.getMyId()) {
            modelView.setError(errorMessage.getError());
            refresh();
        }
    }
    public synchronized void visit(TurnMessage turnMessage){
        modelView.setTurn(turnMessage);
        refresh();
    }
    public synchronized void visit(WinMessage winMessage) {

    }

    public synchronized void visit(StartMessage startMessage){
        System.out.println(startMessage);

       modelView.setMyId(startMessage.getId(modelView.getNickname()));
       startPrint();
       startGame();
    }
    public void startPrint(){

    }
    public void ChooseAssistant(int assistant){
        if(assistant>0 && assistant<11) {
            AssistantChoiceMessage acm = new AssistantChoiceMessage(assistant - 1, modelView.getMyId());
            notifyClient(acm.serialize());
        }
    }

    public void MoveToIsle(Colour c, int isleid){
        MoveStudentMessage msm = new MoveStudentMessage(modelView.getMyId(), c, isleid, false);
        notifyClient(msm.serialize());
    }

    public void MoveToTable(Colour c){
        MoveStudentMessage msm = new MoveStudentMessage(modelView.getMyId(),c,0,true);
        notifyClient(msm.serialize());
    }

    public void ChooseCloud(int cloudid){
        CloudChoiceMessage ccm = new CloudChoiceMessage(modelView.getMyId(), cloudid);
        notifyClient(ccm.serialize());
    }

    public void MoveMotherNature(int spaces){
        MoveMotherNatureMessage movemnm = new MoveMotherNatureMessage(modelView.getMyId(), spaces);
        notifyClient(movemnm.serialize());
    }

    public void remove3Stud(int charpos, Colour c){
        Remove3StudCharacterMessage r3s = new Remove3StudCharacterMessage(charpos, modelView.getMyId(),c);
        notifyClient(r3s.serialize());
    }

    public void charStudToIsle(int charpos, Colour student, int isle){
        MoveStudentCharacterMessage mscm = new MoveStudentCharacterMessage(modelView.getMyId(), charpos, student,isle );
        notifyClient(mscm.serialize());
    }

    public void charStudToTable(int charpos, Colour student){
        MoveStudentCharacterMessage mscm = new MoveStudentCharacterMessage(modelView.getMyId(), charpos, student,0);
        notifyClient(mscm.serialize());
    }


    public void similMn(int charpos, int isle){
        SimilMotherNatureMesage smm = new SimilMotherNatureMesage(charpos, modelView.getMyId(), isle);
        notifyClient(smm.serialize());
    }

    public void useInfluenceCharacter(int charpos){
        IsleInfluenceCharacterMessage iicm = new IsleInfluenceCharacterMessage(charpos, modelView.getMyId());
        notifyClient(iicm.serialize());
    }

    public void prohibit(int charpos, int isle){
        ProhibitedIsleCharacterMessage pm = new ProhibitedIsleCharacterMessage(charpos, modelView.getMyId(), isle);
        notifyClient(pm.serialize());
    }

    public void professorControl(int charpos){
        StrategyProfessorMessage strategyProfessorMessage = new StrategyProfessorMessage(charpos, modelView.getMyId());
        notifyClient(strategyProfessorMessage.serialize());
    }

    public void motherNBoost(int charpos){
        Plus2MoveMnMessage plus = new Plus2MoveMnMessage(charpos, modelView.getMyId());
        notifyClient(plus.serialize());
    }

    public void noColourInfluence(int charpos, Colour ignored){
        IsleInfluenceCharacterMessage icm = new IsleInfluenceCharacterMessage(charpos, modelView.getMyId(),ignored);
    }
    public void exchange2Students(int charpos, Colour[] fromBoard, Colour[] fromtables){
        Move2StudCharacterMessage m2m = new Move2StudCharacterMessage(charpos,modelView.getMyId(),fromBoard,fromtables);
        notifyClient(m2m.serialize());
    }

    public void exchange3Students(int charpos, Colour[] fromBoard, Colour[] fromChar ){
        Move6StudCharacterMessage m3m = new Move6StudCharacterMessage(charpos, modelView.getMyId(), fromBoard,fromChar);
        notifyClient(m3m.serialize());
    }

    public void sendPlayerInfo(String nickname, int nplayers, boolean expertMode){
        modelView = new ModelView(nickname,nplayers,expertMode);
        PlayerMessage pm = new PlayerMessage(nickname,nplayers,expertMode);
        notifyClient(MessageSerializer.serialize(pm));
        System.out.println("sending...");
        System.out.println(modelView);
        System.out.println(this);
    }

    public void notifyClient(String message){
        modelView.setError(null);
        JsonObject jo = gson.fromJson(message,JsonObject.class);
        jo.addProperty("command","message");
        notify(jo);
    }

    public void notifyConnection(String ip, int port){
        System.out.println("sending...");
        JsonObject jo = new JsonObject();
        jo.addProperty("ip",ip);
        jo.addProperty("port",port);
        jo.addProperty("command","connect");
        notify(jo);
    }



    public abstract void start();
    public abstract void startGame();
    public abstract void refresh();
}
