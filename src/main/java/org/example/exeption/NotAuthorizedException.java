package org.example.exeption;

public class NotAuthorizedException extends BusinessException {

    public NotAuthorizedException() {
        super("The password is wrong!");
    }
}
