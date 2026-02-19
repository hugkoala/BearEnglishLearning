package com.bear.englishlearning.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bear.englishlearning.data.local.entity.Conversation
import com.bear.englishlearning.data.local.entity.ConversationLine

@Dao
interface ConversationDao {

    @Query("SELECT * FROM conversations ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomConversation(): Conversation?

    @Query("SELECT * FROM conversations WHERE conversationId = :id")
    suspend fun getConversationById(id: Long): Conversation?

    @Query("SELECT * FROM conversations")
    suspend fun getAllConversations(): List<Conversation>

    @Query("SELECT * FROM conversation_lines WHERE conversationId = :conversationId ORDER BY orderIndex")
    suspend fun getLinesForConversation(conversationId: Long): List<ConversationLine>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllConversations(conversations: List<Conversation>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllLines(lines: List<ConversationLine>)
}
