package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ModelView;
import it.polimi.ingsw.clientModels.*;
import it.polimi.ingsw.clientModels.Answers.ErrorMessage;
import it.polimi.ingsw.clientModels.Answers.TurnMessage;
import it.polimi.ingsw.clientModels.Answers.WinMessage;
import it.polimi.ingsw.model.CharactersEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class CliSetter implements CliBuilder{

    CliModel cli;
    private final ModelView modelView;

    /**
     * Instantiates a new CliSetter.
     *
     * @param modelView the ModelView
     */
    public CliSetter(ModelView modelView){
        this.modelView = modelView;
        this.reset();
    }

    @Override
    public void reset() {
        cli = new CliModel();
    }



    @Override
    public void setBoards(boolean isExpert) {
        CliEntity boards = new CliEntity();
        for(ClientBoard board : modelView.getBoards()){
            boards = boards.rightStick(new CliEntities.CliBoard(modelView, board.getPlayerID(), isExpert));
        }
        cli.setBoards(boards);
    }

    @Override
    public void setIsles(boolean isProhibitedPresent) {
        ArrayList<ClientIsle> isles = modelView.getGameModel().getIsles();
        CliEntity tempCliEntity = new CliEntity();
        CliEntity cliIsles = new CliEntity();
        for(int i = 0; i < isles.size(); i++){
            tempCliEntity = tempCliEntity.rightStick(new CliEntities.CliIsle(modelView, i, isProhibitedPresent));
            if((i + 1) % 4 == 0 || i == isles.size() - 1){
                cliIsles = cliIsles.bottomStick(tempCliEntity);
                tempCliEntity = new CliEntity();
            }
        }
        cli.setIsles(cliIsles);
    }

    @Override
    public void setCharacter(Optional<Integer> usedCharacter) {
        CliEntity characters = new CliEntity();
        for(ClientCharacter character : modelView.getCharacters()){
            characters = characters.rightStick(new CliEntities.CliCharacter(modelView, character.getID(), usedCharacter.isPresent() && usedCharacter.get() == character.getID()));
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
        for(int i = 0; i < modelView.getPlayers()[modelView.getMyId()].getDeck().length; i++){
            deck = deck.rightStick(new CliEntities.CliAssistant(modelView, i, true, modelView.getMyId()));
        }
        CliEntity usedString = new CliEntities.CliString("Assistenti usati");
        CliEntity used = new CliEntity();
        for(int i = modelView.getPlayers()[modelView.getMyId()].getUsedAssistants().length - 1; i >= 0 ; i--){
            used = used.rightStick(new CliEntities.CliAssistant(modelView, i, false, modelView.getMyId()));
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
        for(ClientPlayer player : modelView.getOtherPlayerAssistant().keySet()){
                    others = others.rightStick(
                            new CliEntities.CliString(player.getNickname())
                                    .bottomStick(new CliEntities.CliAssistant(modelView, modelView.getOtherPlayerAssistant().get(player), false, player.getId()))
                    );
        }
        cli.setOtherPlayerAss(otherString.bottomStick(others));
    }

    @Override
    public void setTurn() {
        TurnMessage turn = modelView.getTurn();
        CliEntity eTurn;
        eTurn = new CliEntities.CliString("Fase: " + turn.getTurn().getTurnMsg() + ". ");
        if(turn.getPlayerId() == modelView.getMyId()){
            eTurn = eTurn.rightStick(new CliEntities.CliString("E' il tuo turno!"));
        }else{
            eTurn = eTurn.rightStick(new CliEntities.CliString("E' il turno di " + modelView.getPlayers()[turn.getPlayerId()].getNickname()));
        }
        cli.setTurn(eTurn);
    }

    @Override
    public void setErrors() {
        ErrorMessage.ErrorType error = modelView.getError();
        if(error != null){
            CliEntity eError = new CliEntities.CliString("Attenzione! " + error.getErrorMsg());
            cli.setErrors(eError);
        }
    }

    @Override
    public void setWin() {
        WinMessage winMessage = modelView.getWin();
        CliEntity win;
        if(winMessage != null){
            if(winMessage.isDraw()){
                win = new CliEntities.CliString("E' un pareggio!!!");
            }else{
                if(winMessage.getId() == modelView.getMyId()){
                    win = new CliEntities.CliString("Hai vinto!!!");
                }else{
                    win = new CliEntities.CliString(modelView.getPlayers()[winMessage.getId()].getNickname() + " ha vinto!!!");
                }
            }
            cli.setWin(win);
        }
    }

    public void setAllCli(){
        setBoards(true);
        setAssistants();
        setIsles(
                Arrays.stream(modelView.getCharacters())
                .anyMatch(c -> c.getCard() == CharactersEnum.PROHIBITED)
        );
        setCharacter(modelView.getCurrentCharacter());
        setClouds();
        setTurn();
        setErrors();
        setOtherPlayersAss();
        setWin();
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
                .bottomStick(cli.getErrors())
                .bottomStick(cli.getWin()));
    }

    public CliModel getCli(){
        CliModel cli = this.cli;
        this.reset();
        return cli;
    }
}
