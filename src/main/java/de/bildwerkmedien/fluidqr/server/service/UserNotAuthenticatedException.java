package de.bildwerkmedien.fluidqr.server.service;

public class UserNotAuthenticatedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserNotAuthenticatedException() {
        super("There was an authentication error");
    }

}
