package za.ac.quickchat;

import java.util.List;
import javax.swing.JOptionPane;

public class MessageSender {

    private MessageProcessor processor = new MessageProcessor();

    public MessageSender() {
        processor.populateTestData();
    }

    public void runApplication() {
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat.");

        boolean isRunning = true;
        while (isRunning) {
            String menu = "Please choose one of the following features:\n"
                        + "1) Send Messages\n"
                        + "2) Show recently sent messages (Old)\n"
                        + "3) Quit\n"
                        + "4) Data & Reports (Part 3 Features)";

            String choiceStr = JOptionPane.showInputDialog(menu);

            try {
                int choice = Integer.parseInt(choiceStr);
                switch (choice) {
                    case 1:
                        sendMessages();
                        break;
                    case 2:
                        JOptionPane.showMessageDialog(null, "Refer to Option 4 for full reports.");
                        break;
                    case 3:
                        isRunning = false;
                        break;
                    case 4:
                        handleReports();
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid choice.");
                        break;
                }
            } catch (NumberFormatException e) {
                if (choiceStr == null) isRunning = false;
            }
        }
    }

    private void handleReports() {
        String subMenu = "1) Display Sent Messages Report\n"
                       + "2) Find Longest Sent Message\n"
                       + "3) Search by Message ID\n"
                       + "4) Search by Recipient\n"
                       + "5) Delete Message by Hash\n"
                       + "6) Display Full Report";
        
        String subChoice = JOptionPane.showInputDialog(subMenu);
        if(subChoice == null) return;

        switch(subChoice) {
            case "1":
                JOptionPane.showMessageDialog(null, processor.getSenderRecipientForAllSent());
                break;
            case "2":
                JOptionPane.showMessageDialog(null, "Longest Sent Msg: " + processor.findLongestMessage());
                break;
            case "3":
                String searchID = JOptionPane.showInputDialog("Enter Message ID:");
                JOptionPane.showMessageDialog(null, processor.searchByMessageID(searchID));
                break;
            case "4":
                String searchRec = JOptionPane.showInputDialog("Enter Recipient Number:");
                List<String> results = processor.searchMessagesByRecipient(searchRec);
                JOptionPane.showMessageDialog(null, "Found " + results.size() + " messages:\n" + results);
                break;
            case "5":
                String searchHash = JOptionPane.showInputDialog("Enter Hash to delete:");
                boolean deleted = processor.deleteMessageByHash(searchHash);
                JOptionPane.showMessageDialog(null, deleted ? "Deleted successfully" : "Hash not found");
                break;
            case "6":
                System.out.println(processor.getAllMessagesReport()); 
                JOptionPane.showMessageDialog(null, "Full report printed to Console (for readability).");
                break;
        }
    }

    private void sendMessages() {
        try {
            String numMessagesStr = JOptionPane.showInputDialog("How many messages do you want to send?");
            if (numMessagesStr == null) return;
            int numMessages = Integer.parseInt(numMessagesStr);

            for (int i = 0; i < numMessages; i++) {
                String recipient;
                String recipientValidation;

                do {
                    recipient = JOptionPane.showInputDialog("Enter recipient number for message " + (i + 1) + ":");
                    if (recipient == null) return;
                    recipientValidation = new Message(recipient, " ").checkRecipientCell();
                    if (!recipientValidation.equals("Cell phone number successfully captured.")) {
                        JOptionPane.showMessageDialog(null, "Error: " + recipientValidation);
                    }
                } while (!recipientValidation.equals("Cell phone number successfully captured."));

                String text = JOptionPane.showInputDialog("Enter message text for message " + (i + 1) + ":");
                if (text == null) return;

                Message newMessage = new Message(recipient, text);
                if (!newMessage.checkMessageLength().equals("Message ready to send.")) {
                    JOptionPane.showMessageDialog(null, "Message too long.");
                    continue;
                }

                String sentStatus = newMessage.sentMessage();
                JOptionPane.showMessageDialog(null, sentStatus);

                String status = "Disregarded";
                if (sentStatus.contains("sent")) status = "Sent";
                else if (sentStatus.contains("stored")) status = "Stored";

                processor.addRecord(text, status, recipient, newMessage.toString().split("Message ID: ")[1].split("\n")[0], newMessage.createMessageHash());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}