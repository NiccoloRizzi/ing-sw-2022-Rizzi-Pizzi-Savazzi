package it.polimi.ingsw.controller;

import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.GameModel;

public class MessageVisitor {

    final Game game;

    public MessageVisitor(Game game){
        this.game = game;
    }

    public void visit(AssistantChoiceMessage assistantChoiceMessage){
    }
    public void visit(MoveStudentMessage moveStudentMessage){
    }
    public void visit(MoveMotherNatureMessage moveMotherNatureMessage){
    }
    public void visit(CloudChoiceMessage cloudChoiceMessage){
    }
    public void visit(IsleInfluenceCharacterMessage isleInfluenceCharacterMessage){
    }
    public void visit(MoveStudentCharacterMessage moveStudentCharacterMessage){
    }
    public void visit(StrategyProfessorMessage strategyProfessorMessage){
    }
    public void visit(SimilMotherNatureMesage similMotherNatureMesage){
    }
    public void visit(Plus2MoveMnMessage plus2MoveMnMessage){
    }
    public void visit(ProhibitedIsleCharacterMessage prohibitedIsleCharacterMessage){
    }
    public void visit(Move6StudCharacterMessage move6StudCharacterMessage){
    }
    public void visit(Move2StudCharacterMessage move2StudCharacterMessage){
    }
    public void visit(Remove3StudCharacterMessage remove3StudCharacterMessage){
    }
}
