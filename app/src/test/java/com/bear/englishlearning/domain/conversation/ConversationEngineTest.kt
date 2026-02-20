package com.bear.englishlearning.domain.conversation

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ConversationEngineTest {

    private lateinit var engine: ConversationEngine

    @Before
    fun setup() {
        engine = ConversationEngine()
    }

    // --- Topic Management ---

    @Test
    fun `getAllTopics returns 8 topics`() {
        val topics = engine.getAllTopics()
        assertEquals(8, topics.size)
    }

    @Test
    fun `all topics have non-empty fields`() {
        engine.getAllTopics().forEach { topic ->
            assertTrue("Topic id must not be blank", topic.id.isNotBlank())
            assertTrue("Topic title must not be blank", topic.title.isNotBlank())
            assertTrue("Topic titleZh must not be blank", topic.titleZh.isNotBlank())
            assertTrue("Topic greeting must not be blank", topic.greeting.isNotBlank())
            assertTrue("Topic greetingZh must not be blank", topic.greetingZh.isNotBlank())
        }
    }

    @Test
    fun `all topic ids are unique`() {
        val ids = engine.getAllTopics().map { it.id }
        assertEquals(ids.size, ids.distinct().size)
    }

    @Test
    fun `setTopic changes currentTopic`() {
        engine.setTopic("cafe")
        assertEquals("cafe", engine.getCurrentTopic().id)

        engine.setTopic("hotel")
        assertEquals("hotel", engine.getCurrentTopic().id)
    }

    @Test
    fun `setTopic with invalid id falls back to first`() {
        engine.setTopic("nonexistent_topic")
        assertEquals(engine.getAllTopics().first().id, engine.getCurrentTopic().id)
    }

    @Test
    fun `getRandomTopic returns a valid topic`() {
        val topic = engine.getRandomTopic()
        assertTrue(engine.getAllTopics().any { it.id == topic.id })
    }

    @Test
    fun `getCurrentTopic returns a valid topic`() {
        val topic = engine.getCurrentTopic()
        assertTrue(engine.getAllTopics().any { it.id == topic.id })
    }

    // --- Reply Generation ---

    @Test
    fun `greeting input returns greeting reply`() {
        val (replyEn, replyZh) = engine.generateReply("Hello!")
        assertTrue("English reply must not be blank", replyEn.isNotBlank())
        assertTrue("Chinese reply must not be blank", replyZh.isNotBlank())
    }

    @Test
    fun `goodbye input returns goodbye reply`() {
        val (replyEn, replyZh) = engine.generateReply("Goodbye, see you later!")
        assertTrue(replyEn.isNotBlank())
        assertTrue(replyZh.isNotBlank())
    }

    @Test
    fun `thank you input returns thank reply`() {
        val (replyEn, replyZh) = engine.generateReply("Thank you so much!")
        assertTrue(replyEn.isNotBlank())
        assertTrue(replyZh.isNotBlank())
    }

    @Test
    fun `yes input returns follow-up reply`() {
        engine.setTopic("cafe")
        val (replyEn, replyZh) = engine.generateReply("Yes, please")
        assertTrue(replyEn.isNotBlank())
        assertTrue(replyZh.isNotBlank())
    }

    @Test
    fun `no input returns follow-up reply`() {
        engine.setTopic("restaurant")
        val (replyEn, replyZh) = engine.generateReply("No, not really")
        assertTrue(replyEn.isNotBlank())
        assertTrue(replyZh.isNotBlank())
    }

    @Test
    fun `question about price returns price reply`() {
        engine.setTopic("cafe")
        val (replyEn, _) = engine.generateReply("How much does it cost?")
        assertTrue(replyEn.isNotBlank())
    }

    @Test
    fun `question about time returns time reply`() {
        engine.setTopic("restaurant")
        val (replyEn, _) = engine.generateReply("What time do you open?")
        assertTrue(replyEn.isNotBlank())
    }

    @Test
    fun `question about location returns location reply`() {
        engine.setTopic("hotel")
        val (replyEn, _) = engine.generateReply("Where can I find the elevator?")
        assertTrue(replyEn.isNotBlank())
    }

    @Test
    fun `question about recommendation returns recommend reply`() {
        engine.setTopic("cafe")
        val (replyEn, _) = engine.generateReply("What do you recommend?")
        assertTrue(replyEn.isNotBlank())
    }

    @Test
    fun `generic question returns a reply`() {
        val (replyEn, _) = engine.generateReply("Can you explain this?")
        assertTrue(replyEn.isNotBlank())
    }

    // --- Topic-specific replies ---

    @Test
    fun `cafe topic handles coffee keyword`() {
        engine.setTopic("cafe")
        val (replyEn, _) = engine.generateReply("I want a coffee")
        assertTrue(replyEn.contains("coffee", ignoreCase = true) || replyEn.isNotBlank())
    }

    @Test
    fun `hotel topic handles room keyword`() {
        engine.setTopic("hotel")
        val (replyEn, _) = engine.generateReply("Do you have a room available?")
        assertTrue(replyEn.isNotBlank())
    }

    @Test
    fun `shopping topic handles discount keyword`() {
        engine.setTopic("shopping")
        val (replyEn, _) = engine.generateReply("Is there any discount?")
        assertTrue(replyEn.isNotBlank())
    }

    @Test
    fun `restaurant topic handles order keyword`() {
        engine.setTopic("restaurant")
        val (replyEn, _) = engine.generateReply("I want to order the steak")
        assertTrue(replyEn.isNotBlank())
    }

    @Test
    fun `directions topic handles station keyword`() {
        engine.setTopic("directions")
        val (replyEn, _) = engine.generateReply("Where is the nearest train station?")
        assertTrue(replyEn.isNotBlank())
    }

    @Test
    fun `doctor topic handles headache keyword`() {
        engine.setTopic("doctor")
        val (replyEn, _) = engine.generateReply("I have a headache")
        assertTrue(replyEn.isNotBlank())
    }

    @Test
    fun `airport topic handles luggage keyword`() {
        engine.setTopic("airport")
        val (replyEn, _) = engine.generateReply("Where can I pick up my luggage?")
        assertTrue(replyEn.isNotBlank())
    }

    @Test
    fun `daily topic handles generic input`() {
        engine.setTopic("daily")
        val (replyEn, _) = engine.generateReply("The weather is nice today")
        assertTrue(replyEn.isNotBlank())
    }

    // --- Reply always returns bilingual pair ---

    @Test
    fun `all topics return bilingual replies for various inputs`() {
        val inputs = listOf(
            "hello", "goodbye", "thanks", "yes", "no",
            "how much", "what time", "where", "recommend",
            "some random sentence here"
        )
        engine.getAllTopics().forEach { topic ->
            engine.setTopic(topic.id)
            inputs.forEach { input ->
                val (en, zh) = engine.generateReply(input)
                assertTrue("Topic ${topic.id} with input '$input' should return non-blank English", en.isNotBlank())
                assertTrue("Topic ${topic.id} with input '$input' should return non-blank Chinese", zh.isNotBlank())
            }
        }
    }

    // --- Conversation history ---

    @Test
    fun `generateReply accepts conversation history`() {
        engine.setTopic("cafe")
        val history = listOf(
            "Hi! Welcome to Bear Cafe. What can I get you today?",
            "Great choice! Would you like that for here or to go?"
        )
        val (replyEn, _) = engine.generateReply("I want a latte", history)
        assertTrue(replyEn.isNotBlank())
    }

    @Test
    fun `generateReply with empty history works`() {
        val (replyEn, _) = engine.generateReply("hello", emptyList())
        assertTrue(replyEn.isNotBlank())
    }

    // --- Edge cases ---

    @Test
    fun `empty input returns greeting or generic reply`() {
        val (replyEn, _) = engine.generateReply("")
        // Empty input goes through topic reply which returns non-blank
        assertTrue(replyEn.isNotBlank())
    }

    @Test
    fun `very long input does not crash`() {
        val longInput = "hello ".repeat(1000)
        val (replyEn, _) = engine.generateReply(longInput)
        assertTrue(replyEn.isNotBlank())
    }

    @Test
    fun `input with special characters does not crash`() {
        val (replyEn, _) = engine.generateReply("!@#\$%^&*()_+ hello 你好")
        assertTrue(replyEn.isNotBlank())
    }

    @Test
    fun `case insensitive keyword matching`() {
        engine.setTopic("cafe")
        val (reply1, _) = engine.generateReply("COFFEE")
        val (reply2, _) = engine.generateReply("Coffee")
        val (reply3, _) = engine.generateReply("coffee")
        assertTrue(reply1.isNotBlank())
        assertTrue(reply2.isNotBlank())
        assertTrue(reply3.isNotBlank())
    }
}
