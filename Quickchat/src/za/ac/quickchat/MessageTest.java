package za.ac.quickchat;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.List;

public class MessageTest {

    MessageProcessor processor;

    @Before
    public void setUp() {
        processor = new MessageProcessor();
        processor.populateTestData();
    }

    @Test
    public void testMessageLength_Success() {
        Message msg = new Message("", "This is a valid message.");
        assertEquals("Message ready to send.", msg.checkMessageLength());
    }

    @Test
    public void testMessageLength_Failure() {
        String longMessage = "a".repeat(251);
        Message msg = new Message("", longMessage);
        assertEquals("Message exceeds 250 characters by 1, please reduce size.", msg.checkMessageLength());
    }

    @Test
    public void testSentMessagesPopulated() {
        assertTrue(processor.sentMessages.contains("Did you get the cake?"));
        assertTrue(processor.sentMessages.contains("It is dinner time !"));
    }

    @Test
    public void testLongestMessage() {
        String actual = processor.findLongestMessage();
        assertEquals("Did you get the cake?", actual);
    }

    @Test
    public void testSearchByMessageID() {
        String result = processor.searchByMessageID("0838884567");
        assertTrue(result.contains("It is dinner time !"));
    }

    @Test
    public void testSearchByRecipient() {
        List<String> results = processor.searchMessagesByRecipient("+27838884567");
        assertEquals(2, results.size());
        assertTrue(results.contains("Ok, I am leaving without you."));
    }

    @Test
    public void testDeleteByHash() {
        boolean deleted = processor.deleteMessageByHash("67890");
        assertTrue(deleted);
        assertFalse(processor.storedMessages.contains("Where are you? You are late! I have asked you to be on time."));
    }
}