package de.bildwerkmedien.fluidqr.server.service;

public class UserNotAuthorizedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserNotAuthorizedException() {
        super("User not authorized");
    }

}
