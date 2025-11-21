package za.ac.quickchat;

import java.util.Random;
import javax.swing.JOptionPane;

public class Message {

    private String recipientNumber;
    private String messageText;
    private String messageID;
    private String messageHash;
    private int messageNumber;

    private static int messageCounter = 0;

    public Message(String recipientNumber, String messageText) {
        this.recipientNumber = recipientNumber;
        this.messageText = messageText;
        this.messageNumber = ++messageCounter;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    public String checkMessageLength() {
        int limit = 250;
        if (messageText == null || messageText.isEmpty()) {
            return "Message cannot be empty.";
        }
        if (messageText.length() > limit) {
            int excess = messageText.length() - limit;
            // FIX: This string now exactly matches the test's expectation.
            return "Message exceeds 250 characters by " + excess + ", please reduce size.";
        }
        return "Message ready to send.";
    }

    public String checkRecipientCell() {
        if (recipientNumber != null && recipientNumber.startsWith("+") && recipientNumber.length() >= 10 && recipientNumber.length() <= 13) {
            return "Cell phone number successfully captured.";
        } else {
            // FIX: This string now exactly matches the test's expectation.
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    // FIX: This public method is added so the test can directly check the ID's validity.
    public boolean checkMessageID() {
        return this.messageID != null && this.messageID.length() == 10;
    }

    public String sentMessage() {
        String menu = "Choose an option:\n1) Send Message\n2) Discard Message\n3) Store Message";
        String choiceStr = JOptionPane.showInputDialog(menu);
        int choice = 0;
        try {
            choice = Integer.parseInt(choiceStr);
        } catch (NumberFormatException e) {
            return "Invalid selection. Press 0 to delete message.";
        }

        switch (choice) {
            case 1:
                return "Message successfully sent.";
            case 3:
                return "Message successfully stored.";
            default:
                return "Press 0 to delete message.";
        }
    }

    private String generateMessageID() {
        Random random = new Random();
        long tenDigitNumber = 1_000_000_000L + random.nextLong(9_000_000_000L);
        return String.valueOf(tenDigitNumber);
    }

    public String createMessageHash() {
        if (messageText == null || messageText.isEmpty()) {
            return "INVALID:0:NOMESSAGE";
        }
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 0 ? words[words.length - 1] : "";

        String idPart = this.messageID.substring(0, 2);
        String hash = String.format("%s:%d:%s%s", idPart, this.messageNumber, firstWord, lastWord);
        return hash.toUpperCase();
    }

    @Override
    public String toString() {
        return "Message Details:\n" +
               "---------------------\n" +
               "Message ID: " + messageID + "\n" +
               "Message Hash: " + messageHash + "\n" +
               "Message Number: " + messageNumber + "\n" +
               "Recipient: " + recipientNumber + "\n" +
               "Message: " + messageText + "\n" +
               "---------------------";
    }
}