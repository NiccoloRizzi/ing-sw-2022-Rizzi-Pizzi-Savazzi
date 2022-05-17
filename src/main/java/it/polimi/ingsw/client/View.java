package it.polimi.ingsw.client;

import it.polimi.ingsw.clientModels.*;
import it.polimi.ingsw.clientModels.Answers.ErrorMessage;
import it.polimi.ingsw.clientModels.Answers.StartMessage;
import it.polimi.ingsw.clientModels.Answers.TurnMessage;
import it.polimi.ingsw.clientModels.Answers.WinMessage;
import it.polimi.ingsw.messages.AssistantChoiceMessage;
import it.polimi.ingsw.messages.CloudChoiceMessage;
import it.polimi.ingsw.messages.MoveMotherNatureMessage;
import it.polimi.ingsw.messages.MoveStudentMessage;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.server.ModelSerializer;

public class View {

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

    public synchronized void sendMessage(String messageToSend) {
        client.writeToSocket(messageToSend);
    }


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
        AssistantChoiceMessage acm = new AssistantChoiceMessage(assistant-1, client.getId());
        client.writeToSocket(MessageSerializer.serialize(acm));
    }

    public void MoveToIsle(Colour c, int isleid){
        MoveStudentMessage msm = new MoveStudentMessage(client.getId(), c, isleid, false);
        client.writeToSocket(MessageSerializer.serialize(msm));
    }

    public void MoveToTable(Colour c){
        MoveStudentMessage msm = new MoveStudentMessage(client.getId(),c,0,true);
        client.writeToSocket(MessageSerializer.serialize(msm));
    }

    public void ChooseCloud(int cloudid){
        CloudChoiceMessage ccm = new CloudChoiceMessage(client.getId(), cloudid);
        client.writeToSocket(MessageSerializer.serialize(ccm));
    }

    public void MoveMotherNature(int spaces){
        MoveMotherNatureMessage movemnm = new MoveMotherNatureMessage(client.getId(), spaces);
        client.writeToSocket(MessageSerializer.serialize(movemnm));
    }

    public void refresh(){

    }
}
