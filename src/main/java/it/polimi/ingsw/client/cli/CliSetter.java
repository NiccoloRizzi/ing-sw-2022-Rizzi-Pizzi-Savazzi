package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ModelView;
import it.polimi.ingsw.clientModels.*;
import it.polimi.ingsw.clientModels.Answers.ErrorMessage;
import it.polimi.ingsw.clientModels.Answers.TurnMessage;

import java.util.ArrayList;

public class CliSetter implements CliBuilder{

    CliModel cli;
    private final ModelView modelView;
    private final int myId;

    /**
     * Instantiates a new CliSetter.
     *
     * @param modelView the ModelView
     * @param myId      the client/player id
     */
    public CliSetter(ModelView modelView, int myId){
        this.modelView = modelView;
        this.myId = myId;
        this.reset();
    }

    @Override
    public void reset() {
        cli = new CliModel();
    }

    @Override
    public void setBoards() {
        CliEntity boards = new CliEntity();
        for(ClientBoard board : modelView.getBoards()){
            boards = boards.rightStick(new CliEntities.CliBoard(modelView, board.getPlayerID()));
        }
        cli.setBoards(boards);
    }

    @Override
    public void setIsles() {
        ArrayList<ClientIsle> isles = modelView.getGameModel().getIsles();
        CliEntity tempCliEntity = new CliEntity();
        CliEntity cliIsles = new CliEntity();
        for(int i = 0; i < isles.size(); i++){
            tempCliEntity = tempCliEntity.rightStick(new CliEntities.CliIsle(modelView, i));
            if((i + 1) % 4 == 0){
                cliIsles = cliIsles.bottomStick(tempCliEntity);
                tempCliEntity = new CliEntity();
            }
        }
        cli.setIsles(cliIsles);
    }

    @Override
    public void setCharacter() {
        CliEntity characters = new CliEntity();
        for(ClientCharacter character : modelView.getCharacters()){
            characters = characters.rightStick(new CliEntities.CliCharacter(modelView, character.getID()));
        }
        cli.setCharacters(characters);
    }

    @Override
    public void setClouds() {
        CliEntity clouds = new CliEntity();
        for(ClientCloud cloud : modelView.getClouds()){
            clouds = clouds.rightStick(new CliEntities.CliCloud(modelView, cloud.getId()));
        }
        cli.setClouds(clouds);
    }

    /**
     * Create a CliEntity that represents all the player's assistants, both the ones in the deck and the used
     */
    @Override
    public void setAssistants() {
        CliEntity deck = new CliEntity();
        CliEntity deckString = new CliEntities.CliString("I tuoi assistenti");
        for(int i = 0; i < modelView.getPlayers()[myId].getDeck().length; i++){
            deck = deck.rightStick(new CliEntities.CliAssistant(modelView, i, true, myId));
        }
        CliEntity usedString = new CliEntities.CliString("Assistenti usati");
        CliEntity used = new CliEntity();
        for(int i = 0; i < modelView.getPlayers()[myId].getUsedAssistants().length; i++){
            used = used.rightStick(new CliEntities.CliAssistant(modelView, i, false, myId));
        }
        cli.setAssistants(deckString
                .bottomStick(deck)
                .bottomStick(usedString)
                .bottomStick(used));

    }

    /**
     * Create a CliEntity that represents all the used assistants (not used in this builder)
     */
    @Override
    public void setUsedAssistants() {
    }

    @Override
    public void setOtherPlayersAss() {
        CliEntity otherString = new CliEntities.CliString("Assistenti degli altri giocatori");
        CliEntity others = new CliEntity();
        for(ClientPlayer p : modelView.getPlayers()){
            if(p.getId() != myId){
                if(p.getUsedAssistants().length > 0){
                    others = others.rightStick(
                            new CliEntities.CliString(p.getNickname())
                                    .bottomStick(new CliEntities.CliAssistant(modelView, p.getUsedAssistants().length - 1, false, p.getId()))
                    );
                }
            }
        }
        cli.setOtherPlayerAss(otherString.bottomStick(others));
    }

//    @Override
//    public void setTurnAndError() {
//        TurnMessage turn = modelView.getTurn();
//        CliEntity eTurn;
//        if(turn.getPlayerId() == myId){
//            eTurn = new CliEntities.CliString("E' il tuo turno!");
//        }else{
//            eTurn = new CliEntities.CliString("E' il turno di " + modelView.getPlayers()[turn.getPlayerId()].getNickname());
//        }
//        ErrorMessage.ErrorType error = modelView.getError();
//        CliEntity eError = new CliEntities.CliString("Attenzione! " + error);
//        if(error != null){
//            cli.setTurnAndErrors(eTurn.bottomStick(eError));
//        }else{
//            cli.setTurnAndErrors(eTurn);
//        }
//    }

    @Override
    public void setTurn() {
        TurnMessage turn = modelView.getTurn();
        CliEntity eTurn;
        if(turn.getPlayerId() == myId){
            eTurn = new CliEntities.CliString("E' il tuo turno!");
        }else{
            eTurn = new CliEntities.CliString("E' il turno di " + modelView.getPlayers()[turn.getPlayerId()].getNickname());
        }
        cli.setTurn(eTurn);
    }

    @Override
    public void setErrors() {
        ErrorMessage.ErrorType error = modelView.getError();
        CliEntity eError = new CliEntities.CliString("Attenzione! " + error);
        if(error != null){
            cli.setErrors(eError);
        }
    }

    public void setAllCli(){
        setBoards();
        setAssistants();
        setIsles();
        setCharacter();
        setClouds();
        setTurn();
        setErrors();
        setOtherPlayersAss();
    }

    @Override
    public void composeCLi(){
        cli.setFullCli(cli.getBoards()
                .bottomStick(cli.getClouds())
                .bottomStick(cli.getAssistants())
                .bottomStick(cli.getOtherPlayerAss())
                .bottomStick(cli.getCharacters())
                .rightStick(cli.getIsles())
                .bottomStick(cli.getTurn())
                .bottomStick(cli.getErrors()));
    }

    public CliModel getCli(){
        CliModel cli = this.cli;
        this.reset();
        return cli;
    }
}
