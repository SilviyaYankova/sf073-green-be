package org.example.exeption;

public class WrongToken extends BusinessException {

    public WrongToken() {
        super("Wrong token!");
    }
}
