package deco.combatevolved.networking;

public enum DisconnectReason {
    GENERIC ("Generic error"),
    CLIENTDISCONNECT ("Client disconnected"),
    SERVERCLOSING ("Server has closed"),
    SERVERCRASHED ("Server has crashed"),
    SERVERKICKED ("You have been kicked from the game"),
    ILLEGALREQUEST ("Server received an illegal request"),
    INCORRECTPASSWORD ("Incorrect password"),
    TIMEOUT ("Connection timed out"),
    CONNECTIONFAILED ("Unable to connect");

    private final String reasonMessage;

    DisconnectReason(String reasonMessage) {
        this.reasonMessage = reasonMessage;
    }

    public String getMessage() {
        return this.reasonMessage;
    }
}