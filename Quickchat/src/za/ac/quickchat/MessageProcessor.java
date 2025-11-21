package za.ac.quickchat;

import java.util.ArrayList;
import java.util.List;

public class MessageProcessor {
    public List<String> sentMessages = new ArrayList<>();
    public List<String> disregardedMessages = new ArrayList<>();
    public List<String> storedMessages = new ArrayList<>();
    public List<String> messageHashes = new ArrayList<>();
    public List<String> messageIDs = new ArrayList<>();
    public List<String> recipients = new ArrayList<>();

    public void addRecord(String text, String type, String recipient, String id, String hash) {
        recipients.add(recipient);
        messageIDs.add(id);
        messageHashes.add(hash);

        if (type.equalsIgnoreCase("Sent")) {
            sentMessages.add(text);
            storedMessages.add(null);
            disregardedMessages.add(null);
        } else if (type.equalsIgnoreCase("Stored")) {
            sentMessages.add(null);
            storedMessages.add(text);
            disregardedMessages.add(null);
        } else if (type.equalsIgnoreCase("Disregarded")) {
            sentMessages.add(null);
            storedMessages.add(null);
            disregardedMessages.add(text);
        }
    }

    public void populateTestData() {
        addRecord("Did you get the cake?", "Sent", "+27834557896", "001", "12345");
        addRecord("Where are you? You are late! I have asked you to be on time.", "Stored", "+27838884567", "002", "67890");
        addRecord("Yohoooo, I am at your gate.", "Disregarded", "+27834484567", "003", "11223");
        addRecord("It is dinner time !", "Sent", "0838884567", "0838884567", "44556");
        addRecord("Ok, I am leaving without you.", "Stored", "+27838884567", "005", "99887");
    }

    public String getSenderRecipientForAllSent() {
        StringBuilder sb = new StringBuilder("--- Sent Messages ---\n");
        for (int i = 0; i < sentMessages.size(); i++) {
            if (sentMessages.get(i) != null) {
                sb.append("To: ").append(recipients.get(i)).append(" - ").append(sentMessages.get(i)).append("\n");
            }
        }
        return sb.toString();
    }

    public String findLongestMessage() {
        String longest = "";
        for (String msg : sentMessages) {
            if (msg != null && msg.length() > longest.length()) {
                longest = msg;
            }
        }
        return longest;
    }

    public String searchByMessageID(String id) {
        for (int i = 0; i < messageIDs.size(); i++) {
            if (messageIDs.get(i).equals(id)) {
                String text = sentMessages.get(i) != null ? sentMessages.get(i) :
                              storedMessages.get(i) != null ? storedMessages.get(i) :
                              disregardedMessages.get(i);
                return "ID: " + id + " | Recipient: " + recipients.get(i) + " | Message: " + text;
            }
        }
        return "Message not found";
    }

    public List<String> searchMessagesByRecipient(String recipientNumber) {
        List<String> results = new ArrayList<>();
        for (int i = 0; i < recipients.size(); i++) {
            if (recipients.get(i).equals(recipientNumber)) {
                String text = sentMessages.get(i) != null ? sentMessages.get(i) :
                              storedMessages.get(i) != null ? storedMessages.get(i) :
                              disregardedMessages.get(i);
                results.add(text);
            }
        }
        return results;
    }

    public boolean deleteMessageByHash(String hash) {
        for (int i = 0; i < messageHashes.size(); i++) {
            if (messageHashes.get(i).equals(hash)) {
                sentMessages.remove(i);
                storedMessages.remove(i);
                disregardedMessages.remove(i);
                recipients.remove(i);
                messageIDs.remove(i);
                messageHashes.remove(i);
                return true;
            }
        }
        return false;
    }

    public String getAllMessagesReport() {
        StringBuilder sb = new StringBuilder("--- Full Report ---\n");
        sb.append(String.format("%-15s %-15s %-15s %s\n", "ID", "Hash", "Recipient", "Message"));
        
        for (int i = 0; i < messageIDs.size(); i++) {
            String text = sentMessages.get(i) != null ? sentMessages.get(i) :
                          storedMessages.get(i) != null ? storedMessages.get(i) :
                          disregardedMessages.get(i);
            
            sb.append(String.format("%-15s %-15s %-15s %s\n", 
                messageIDs.get(i), 
                messageHashes.get(i), 
                recipients.get(i), 
                text));
        }
        return sb.toString();
    }
}