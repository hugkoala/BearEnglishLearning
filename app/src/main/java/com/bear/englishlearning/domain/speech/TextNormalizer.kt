package com.bear.englishlearning.domain.speech

object TextNormalizer {

    private val contractions = mapOf(
        "i'm" to "i am", "i'll" to "i will", "i'd" to "i would", "i've" to "i have",
        "you're" to "you are", "you'll" to "you will", "you'd" to "you would", "you've" to "you have",
        "he's" to "he is", "he'll" to "he will", "he'd" to "he would",
        "she's" to "she is", "she'll" to "she will", "she'd" to "she would",
        "it's" to "it is", "it'll" to "it will", "it'd" to "it would",
        "we're" to "we are", "we'll" to "we will", "we'd" to "we would", "we've" to "we have",
        "they're" to "they are", "they'll" to "they will", "they'd" to "they would", "they've" to "they have",
        "that's" to "that is", "that'll" to "that will", "that'd" to "that would",
        "who's" to "who is", "who'll" to "who will", "who'd" to "who would",
        "what's" to "what is", "what'll" to "what will", "what'd" to "what would",
        "where's" to "where is", "where'll" to "where will", "where'd" to "where would",
        "when's" to "when is", "when'll" to "when will", "when'd" to "when would",
        "why's" to "why is", "why'll" to "why will", "why'd" to "why would",
        "how's" to "how is", "how'll" to "how will", "how'd" to "how would",
        "isn't" to "is not", "aren't" to "are not", "wasn't" to "was not", "weren't" to "were not",
        "hasn't" to "has not", "haven't" to "have not", "hadn't" to "had not",
        "doesn't" to "does not", "don't" to "do not", "didn't" to "did not",
        "won't" to "will not", "wouldn't" to "would not",
        "shan't" to "shall not", "shouldn't" to "should not",
        "mustn't" to "must not", "can't" to "cannot", "couldn't" to "could not",
        "let's" to "let us", "there's" to "there is", "here's" to "here is"
    )

    fun normalize(text: String): List<String> {
        var normalized = text.lowercase().trim()

        contractions.forEach { (contraction, expansion) ->
            normalized = normalized.replace(
                Regex("\\b${Regex.escape(contraction)}\\b"),
                expansion
            )
        }

        normalized = normalized.replace(Regex("[^a-z0-9\\s]"), "")
        normalized = normalized.replace(Regex("\\s+"), " ").trim()

        return if (normalized.isEmpty()) emptyList() else normalized.split(" ")
    }
}
