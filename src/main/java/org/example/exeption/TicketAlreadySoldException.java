package org.example.exeption;

public class TicketAlreadySoldException extends BusinessException {

    public TicketAlreadySoldException() {
        super("The ticket has been already purchased!");
    }
}
