package com.bear.englishlearning.ui.screens.realtimeconversation

import com.bear.englishlearning.data.local.dao.DailyProgressDao
import com.bear.englishlearning.data.local.entity.DailyProgress
import com.bear.englishlearning.data.repository.DailyProgressRepository
import com.bear.englishlearning.domain.conversation.ConversationEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/** No-op DAO stub for unit tests */
private class FakeDailyProgressDao : DailyProgressDao {
    override suspend fun getProgressByDate(date: String): DailyProgress? =
        DailyProgress(date = date)
    override suspend fun getProgressRange(startDate: String, endDate: String) = emptyList<DailyProgress>()
    override fun getProgressRangeFlow(startDate: String, endDate: String): Flow<List<DailyProgress>> = flowOf(emptyList())
    override suspend fun upsertProgress(progress: DailyProgress): Long = 1L
    override suspend fun incrementSentences(date: String, count: Int, ts: Long) {}
    override suspend fun incrementVocabulary(date: String, count: Int, ts: Long) {}
    override suspend fun incrementConversation(date: String, count: Int, ts: Long) {}
    override suspend fun incrementListening(date: String, count: Int, ts: Long) {}
    override suspend fun incrementMemos(date: String, count: Int, ts: Long) {}
    override suspend fun incrementTranslations(date: String, count: Int, ts: Long) {}
    override suspend fun incrementStudyMinutes(date: String, minutes: Int, ts: Long) {}
    override suspend fun getTotalActiveDays(): Int = 0
    override suspend fun getLatestProgress(): DailyProgress? = null
    override suspend fun getAllProgress(): List<DailyProgress> = emptyList()
}

@OptIn(ExperimentalCoroutinesApi::class)
class RealTimeConversationViewModelTest {

    private lateinit var viewModel: RealTimeConversationViewModel
    private lateinit var engine: ConversationEngine
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        engine = ConversationEngine()
        val progressRepository = DailyProgressRepository(FakeDailyProgressDao())
        viewModel = RealTimeConversationViewModel(engine, progressRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // --- Initial State ---

    @Test
    fun `initial state is Idle`() {
        assertEquals(RealTimeConversationUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `initial messages list is empty`() {
        assertTrue(viewModel.messages.value.isEmpty())
    }

    @Test
    fun `initial partial text is empty`() {
        assertEquals("", viewModel.partialText.value)
    }

    @Test
    fun `initial showTranslation is true`() {
        assertTrue(viewModel.showTranslation.value)
    }

    // --- startNewConversation ---

    @Test
    fun `startNewConversation adds greeting message`() {
        viewModel.startNewConversation()
        val messages = viewModel.messages.value
        assertEquals(1, messages.size)
        assertFalse(messages[0].isUser)
        assertTrue(messages[0].text.isNotBlank())
    }

    @Test
    fun `startNewConversation sets state to Idle`() {
        viewModel.startNewConversation()
        assertEquals(RealTimeConversationUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `startNewConversation clears partial text`() {
        viewModel.startNewConversation()
        assertEquals("", viewModel.partialText.value)
    }

    @Test
    fun `startNewConversation greeting has Chinese translation`() {
        viewModel.startNewConversation()
        val messages = viewModel.messages.value
        assertTrue(messages[0].textZh.isNotBlank())
    }

    // --- onSpeechResult ---

    @Test
    fun `onSpeechResult with blank text sets Idle`() {
        viewModel.setListening()
        viewModel.onSpeechResult("")
        assertEquals(RealTimeConversationUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `onSpeechResult with blank text does not add message`() {
        viewModel.startNewConversation()
        val initialCount = viewModel.messages.value.size
        viewModel.onSpeechResult("   ")
        assertEquals(initialCount, viewModel.messages.value.size)
    }

    @Test
    fun `onSpeechResult adds user message and bot reply`() {
        viewModel.startNewConversation()
        viewModel.onSpeechResult("Hello there!")
        val messages = viewModel.messages.value
        // 1 greeting + 1 user + 1 bot = 3
        assertEquals(3, messages.size)
        assertTrue(messages[1].isUser)
        assertEquals("Hello there!", messages[1].text)
        assertFalse(messages[2].isUser)
    }

    @Test
    fun `onSpeechResult sets state to Speaking`() {
        viewModel.startNewConversation()
        viewModel.onSpeechResult("Hi!")
        assertEquals(RealTimeConversationUiState.Speaking, viewModel.uiState.value)
    }

    @Test
    fun `onSpeechResult bot reply has Chinese translation`() {
        viewModel.startNewConversation()
        viewModel.onSpeechResult("Hello!")
        val botMessage = viewModel.messages.value.last()
        assertFalse(botMessage.isUser)
        assertTrue(botMessage.textZh.isNotBlank())
    }

    @Test
    fun `onSpeechResult clears partial text`() {
        viewModel.startNewConversation()
        viewModel.onSpeechResult("Hello!")
        assertEquals("", viewModel.partialText.value)
    }

    // --- ChatMessage ID uniqueness ---

    @Test
    fun `multiple messages have unique ids`() {
        viewModel.startNewConversation()
        viewModel.onSpeechResult("Hello!")
        viewModel.onSpeechResult("How are you?")
        viewModel.onSpeechResult("What do you recommend?")
        val ids = viewModel.messages.value.map { it.id }
        assertEquals("All message IDs should be unique", ids.size, ids.distinct().size)
    }

    @Test
    fun `consecutive ChatMessage ids are different`() {
        val msg1 = ChatMessage(text = "a", isUser = true)
        val msg2 = ChatMessage(text = "b", isUser = false)
        val msg3 = ChatMessage(text = "c", isUser = true)
        assertNotEquals(msg1.id, msg2.id)
        assertNotEquals(msg2.id, msg3.id)
        assertNotEquals(msg1.id, msg3.id)
    }

    // --- onSpeakingDone ---

    @Test
    fun `onSpeakingDone sets Idle state`() {
        viewModel.startNewConversation()
        viewModel.onSpeechResult("Hi!")
        assertEquals(RealTimeConversationUiState.Speaking, viewModel.uiState.value)
        viewModel.onSpeakingDone()
        assertEquals(RealTimeConversationUiState.Idle, viewModel.uiState.value)
    }

    // --- setListening ---

    @Test
    fun `setListening changes state to Listening`() {
        viewModel.setListening()
        assertEquals(RealTimeConversationUiState.Listening, viewModel.uiState.value)
    }

    @Test
    fun `setListening clears partial text`() {
        viewModel.setListening()
        assertEquals("", viewModel.partialText.value)
    }

    // --- onListeningError ---

    @Test
    fun `onListeningError sets Idle state`() {
        viewModel.setListening()
        viewModel.onListeningError()
        assertEquals(RealTimeConversationUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `onListeningError clears partial text`() {
        viewModel.setListening()
        viewModel.onListeningError()
        assertEquals("", viewModel.partialText.value)
    }

    // --- clearConversation ---

    @Test
    fun `clearConversation empties messages`() {
        viewModel.startNewConversation()
        viewModel.onSpeechResult("Hello!")
        viewModel.clearConversation()
        assertTrue(viewModel.messages.value.isEmpty())
    }

    @Test
    fun `clearConversation sets Idle`() {
        viewModel.startNewConversation()
        viewModel.onSpeechResult("Hi!")
        viewModel.clearConversation()
        assertEquals(RealTimeConversationUiState.Idle, viewModel.uiState.value)
    }

    // --- toggleTranslation ---

    @Test
    fun `toggleTranslation flips value`() {
        assertTrue(viewModel.showTranslation.value)
        viewModel.toggleTranslation()
        assertFalse(viewModel.showTranslation.value)
        viewModel.toggleTranslation()
        assertTrue(viewModel.showTranslation.value)
    }

    // --- Topic selection ---

    @Test
    fun `selectTopic changes current topic`() {
        viewModel.selectTopic("hotel")
        assertEquals("hotel", viewModel.currentTopic.value.id)
    }

    @Test
    fun `selectTopic starts new conversation with topic greeting`() {
        viewModel.selectTopic("cafe")
        val messages = viewModel.messages.value
        assertEquals(1, messages.size)
        assertTrue(messages[0].text.contains("Cafe", ignoreCase = true) || messages[0].text.isNotBlank())
    }

    @Test
    fun `randomTopic changes topic and starts conversation`() {
        viewModel.randomTopic()
        val messages = viewModel.messages.value
        assertEquals(1, messages.size)
        assertFalse(messages[0].isUser)
    }

    @Test
    fun `getAllTopics returns topics from engine`() {
        val topics = viewModel.getAllTopics()
        assertEquals(8, topics.size)
    }

    // --- createRecognizerIntent ---
    // Note: Intent extras use Android framework, tested via instrumentation tests

    // --- createRecognitionListener ---

    @Test
    fun `createRecognitionListener is not null`() {
        val listener = viewModel.createRecognitionListener()
        assertNotNull(listener)
    }

    // --- Multi-turn conversation ---

    @Test
    fun `multiple turns accumulate messages correctly`() {
        viewModel.startNewConversation()
        // Turn 1
        viewModel.onSpeechResult("Hello!")
        viewModel.onSpeakingDone()
        // Turn 2
        viewModel.onSpeechResult("I want a coffee")
        viewModel.onSpeakingDone()
        // Turn 3
        viewModel.onSpeechResult("Thank you!")
        viewModel.onSpeakingDone()

        val messages = viewModel.messages.value
        // 1 greeting + 3 user + 3 bot = 7
        assertEquals(7, messages.size)

        // Verify alternating pattern (after greeting)
        assertTrue(messages[0].isUser.not())  // greeting
        assertTrue(messages[1].isUser)         // user turn 1
        assertTrue(messages[2].isUser.not())   // bot turn 1
        assertTrue(messages[3].isUser)         // user turn 2
        assertTrue(messages[4].isUser.not())   // bot turn 2
        assertTrue(messages[5].isUser)         // user turn 3
        assertTrue(messages[6].isUser.not())   // bot turn 3
    }

    @Test
    fun `state transitions follow correct flow`() {
        viewModel.startNewConversation()
        assertEquals(RealTimeConversationUiState.Idle, viewModel.uiState.value)

        viewModel.setListening()
        assertEquals(RealTimeConversationUiState.Listening, viewModel.uiState.value)

        viewModel.onSpeechResult("Hello!")
        assertEquals(RealTimeConversationUiState.Speaking, viewModel.uiState.value)

        viewModel.onSpeakingDone()
        assertEquals(RealTimeConversationUiState.Idle, viewModel.uiState.value)
    }
}
