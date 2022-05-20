package it.polimi.ingsw.client;

import it.polimi.ingsw.clientModels.*;
import it.polimi.ingsw.clientModels.Answers.ErrorMessage;
import it.polimi.ingsw.clientModels.Answers.StartMessage;
import it.polimi.ingsw.clientModels.Answers.TurnMessage;
import it.polimi.ingsw.clientModels.Answers.WinMessage;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.server.Observable;

public class View extends Observable<Message> {

    private ModelView modelView;
    private boolean started = false;
    private Client client;


    public void setupModel(Client client){
        this.client = client;
        modelView = new ModelView(client.getPlayersNumber(), client.isExpert());
    }

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
        modelView.setError(errorMessage.getError());
        refresh();
    }
    public synchronized void visit(TurnMessage turnMessage){
        modelView.setTurn(turnMessage);
        refresh();
    }
    public synchronized void visit(WinMessage winMessage) {

    }

    public synchronized void visit(StartMessage startMessage){
       client.setId(startMessage.getPlayer(client.getNickname()).getId());
       startPrint();
        refresh();
    }
    public void startPrint(){

    }
    public void ChooseAssistant(int assistant){
        if(assistant>0 && assistant<11) {
            AssistantChoiceMessage acm = new AssistantChoiceMessage(assistant - 1, client.getId());
            notify(acm);
        }
    }

    public void MoveToIsle(Colour c, int isleid){
        MoveStudentMessage msm = new MoveStudentMessage(client.getId(), c, isleid, false);
        notify(msm);
    }

    public void MoveToTable(Colour c){
        MoveStudentMessage msm = new MoveStudentMessage(client.getId(),c,0,true);
        notify(msm);
    }

    public void ChooseCloud(int cloudid){
        CloudChoiceMessage ccm = new CloudChoiceMessage(client.getId(), cloudid);
        notify(ccm);
    }

    public void MoveMotherNature(int spaces){
        MoveMotherNatureMessage movemnm = new MoveMotherNatureMessage(client.getId(), spaces);
        notify(movemnm);
    }

    public void refresh(){

    }
}
