package com.bear.englishlearning.domain.speech

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import com.bear.englishlearning.domain.model.WordStatus

class WordDiffEngineTest {

    @Test
    fun `perfect match gives 100 percent accuracy`() {
        val result = WordDiffEngine.compare(
            targetSentence = "Hello how are you",
            spokenText = "Hello how are you"
        )
        assertEquals(1.0f, result.accuracy)
        assertEquals(4, result.matchedCount)
        assertTrue(result.diffWords.all { it.status == WordStatus.MATCH })
    }

    @Test
    fun `case insensitive comparison`() {
        val result = WordDiffEngine.compare("Hello World", "hello world")
        assertEquals(1.0f, result.accuracy)
    }

    @Test
    fun `punctuation is ignored`() {
        val result = WordDiffEngine.compare(
            "Hello, how are you?",
            "hello how are you"
        )
        assertEquals(1.0f, result.accuracy)
    }

    @Test
    fun `contractions are expanded`() {
        val result = WordDiffEngine.compare(
            "I'd like to order",
            "I would like to order"
        )
        assertEquals(1.0f, result.accuracy)
        assertEquals(5, result.matchedCount)
    }

    @Test
    fun `missing words are detected`() {
        val result = WordDiffEngine.compare(
            "I would like a cheeseburger please",
            "I would like please"
        )
        val missing = result.diffWords.filter { it.status == WordStatus.MISSING }
        assertEquals(2, missing.size)
        assertTrue(missing.any { it.word == "a" })
        assertTrue(missing.any { it.word == "cheeseburger" })
    }

    @Test
    fun `extra words are detected`() {
        val result = WordDiffEngine.compare(
            "I want water",
            "I want some cold water"
        )
        val extra = result.diffWords.filter { it.status == WordStatus.EXTRA }
        assertEquals(2, extra.size)
    }

    @Test
    fun `empty spoken gives 0 accuracy`() {
        val result = WordDiffEngine.compare("Hello world", "")
        assertEquals(0.0f, result.accuracy)
        assertEquals(2, result.diffWords.size)
        assertTrue(result.diffWords.all { it.status == WordStatus.MISSING })
    }

    @Test
    fun `both empty gives 100 accuracy`() {
        val result = WordDiffEngine.compare("", "")
        assertEquals(1.0f, result.accuracy)
    }

    @Test
    fun `accuracy calculation is correct`() {
        val result = WordDiffEngine.compare(
            "one two three four five",
            "one two four"
        )
        assertEquals(3, result.matchedCount)
        assertEquals(5, result.targetWordCount)
        assertEquals(0.6f, result.accuracy)
    }

    @Test
    fun `complex real-world scenario`() {
        val result = WordDiffEngine.compare(
            "Could I have the check please?",
            "Can I have check please"
        )
        assertTrue(result.accuracy > 0.5f)
        assertTrue(result.diffWords.any { it.word == "could" && it.status == WordStatus.MISSING })
        assertTrue(result.diffWords.any { it.word == "can" && it.status == WordStatus.EXTRA })
    }

    @Test
    fun `wont expands to will not`() {
        val result = WordDiffEngine.compare(
            "I won't go",
            "I will not go"
        )
        assertEquals(1.0f, result.accuracy)
    }

    @Test
    fun `accuracy percent formatting`() {
        val result = WordDiffEngine.compare("one two three four five", "one two four")
        assertEquals("60%", result.accuracyPercent)
    }
}
