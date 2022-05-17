package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.MessageVisitor;
import it.polimi.ingsw.server.MoveSerializer;

public interface Message {
    void accept(MessageVisitor visitor);
    String serialize();
}
