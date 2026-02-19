package com.bear.englishlearning.domain.speech

import org.junit.Assert.assertEquals
import org.junit.Test

class TextNormalizerTest {

    @Test
    fun `normalizes to lowercase`() {
        assertEquals(listOf("hello", "world"), TextNormalizer.normalize("Hello World"))
    }

    @Test
    fun `removes punctuation`() {
        assertEquals(listOf("hello", "world"), TextNormalizer.normalize("Hello, World!"))
    }

    @Test
    fun `expands contractions`() {
        assertEquals(listOf("i", "would", "like"), TextNormalizer.normalize("I'd like"))
    }

    @Test
    fun `handles multiple spaces`() {
        assertEquals(listOf("a", "b", "c"), TextNormalizer.normalize("  a   b  c  "))
    }

    @Test
    fun `empty string returns empty list`() {
        assertEquals(emptyList<String>(), TextNormalizer.normalize(""))
    }

    @Test
    fun `expands wont to will not`() {
        assertEquals(listOf("i", "will", "not", "go"), TextNormalizer.normalize("I won't go"))
    }

    @Test
    fun `expands cant to cannot`() {
        assertEquals(listOf("i", "cannot", "do", "it"), TextNormalizer.normalize("I can't do it"))
    }

    @Test
    fun `expands its to it is`() {
        assertEquals(listOf("it", "is", "cold"), TextNormalizer.normalize("It's cold"))
    }

    @Test
    fun `handles question marks and periods`() {
        assertEquals(listOf("how", "are", "you"), TextNormalizer.normalize("How are you?"))
    }

    @Test
    fun `handles complex sentence with contractions`() {
        assertEquals(
            listOf("i", "would", "like", "a", "table", "for", "two", "please"),
            TextNormalizer.normalize("I'd like a table for two, please.")
        )
    }
}
