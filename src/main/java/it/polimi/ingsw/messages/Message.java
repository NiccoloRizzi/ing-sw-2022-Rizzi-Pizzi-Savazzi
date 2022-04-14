package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.MessageVisitor;

public interface Message {
    void accept(MessageVisitor visitor);
}
