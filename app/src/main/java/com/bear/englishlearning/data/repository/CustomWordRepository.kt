package com.bear.englishlearning.data.repository

import com.bear.englishlearning.data.local.dao.CustomWordDao
import com.bear.englishlearning.data.local.entity.CustomWord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomWordRepository @Inject constructor(
    private val customWordDao: CustomWordDao
) {
    fun getAllWords(): Flow<List<CustomWord>> = customWordDao.getAllWords()

    fun getWordCount(): Flow<Int> = customWordDao.getWordCount()

    suspend fun addWord(word: CustomWord): Long = customWordDao.insert(word)

    suspend fun deleteWord(word: CustomWord) = customWordDao.delete(word)

    suspend fun deleteById(id: Long) = customWordDao.deleteById(id)
}
