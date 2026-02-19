package com.bear.englishlearning.data.repository

import com.bear.englishlearning.data.local.dao.ConversationDao
import com.bear.englishlearning.data.local.entity.Conversation
import com.bear.englishlearning.data.local.entity.ConversationLine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConversationRepository @Inject constructor(
    private val conversationDao: ConversationDao
) {
    suspend fun getRandomConversation(): Conversation? =
        conversationDao.getRandomConversation()

    suspend fun getConversationById(id: Long): Conversation? =
        conversationDao.getConversationById(id)

    suspend fun getLinesForConversation(conversationId: Long): List<ConversationLine> =
        conversationDao.getLinesForConversation(conversationId)

    suspend fun getAllConversations(): List<Conversation> =
        conversationDao.getAllConversations()
}
