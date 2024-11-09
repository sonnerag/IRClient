package vn.edu.usth.chatbox;

public class ChatMessage {
    private String senderName;
    private String message;

    public ChatMessage(String senderName, String message) {
        this.senderName = senderName;
        this.message = message;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMessage() {
        return message;
    }
}
