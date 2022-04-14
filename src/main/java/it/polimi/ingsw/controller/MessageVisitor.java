package it.polimi.ingsw.controller;

import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.GameModel;

public class MessageVisitor {

    final GameModel gameModel;

    public MessageVisitor(GameModel gameModel){
        this.gameModel = gameModel;
    }

    public void visitAssistantChoiceMessage(AssistantChoiceMessage assistantChoiceMessage){
    }
    public void visitMoveStudentMessage(MoveStudentMessage moveStudentMessage){
    }
    public void visitMoveMotherNatureMessage(MoveMotherNatureMessage moveMotherNatureMessage){
    }
    public void visitCloudChoiceMessage(CloudChoiceMessage cloudChoiceMessage){
    }
    public void visitIsleInfluenceCharacterMessage(IsleInfluenceCharacterMessage isleInfluenceCharacterMessage){
    }
    public void visitMoveStudentCharacterMessage(MoveStudentCharacterMessage moveStudentCharacterMessage){
    }
    public void visitStrategyProfessorMessage(StrategyProfessorMessage strategyProfessorMessage){
    }
    public void visitSimilMotherNatureMessage(SimilMotherNatureMesage similMotherNatureMesage){
    }
    public void visitPlus2MoveMnMessage(Plus2MoveMnMessage plus2MoveMnMessage){
    }
    public void visitProhibitedIsleCharacterMessage(ProhibitedIsleCharacterMessage prohibitedIsleCharacterMessage){
    }
    public void visitMove6StudCharacterMessage(Move6StudCharacterMessage move6StudCharacterMessage){
    }
    public void visitMove2StudCharacterMessage(Move2StudCharacterMessage move2StudCharacterMessage){
    }
    public void visitRemove3StudCharacterMessage(Remove3StudCharacterMessage remove3StudCharacterMessage){
    }
}
