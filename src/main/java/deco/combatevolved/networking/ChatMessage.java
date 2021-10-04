package deco.combatevolved.networking;

public class ChatMessage {
    String message;
    String username;
    int id;
    long time;

    public ChatMessage() {

    }

    public ChatMessage(String username, String message, int id) {
        this.message = message;
        this.username = username;
        this.id = id;
        this.time = System.currentTimeMillis();
    }

    /**
     * Returns the message stored in the ChatMessage.
     * @return (String) - The message in ChatMessage.
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Replaces the chat message "message"
     * @param newMessage the new message.
     */
    public void setMessage(String newMessage) {
        this.message = newMessage;
    }

    /**
     * Returns the id of the sender.
     * @return (int) - The id of the sender.
     */
    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", username, message);
    }
}