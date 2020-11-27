package com.google.engedu.anagrams

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.MockitoAnnotations
import java.io.FileInputStream
import java.io.InputStreamReader

@RunWith(JUnit4::class)
class AnagramDictionaryTest {

    private lateinit var anagramDictionary: AnagramDictionary

    @Before
    fun start() {
        MockitoAnnotations.initMocks(this)
        val inputStream = InputStreamReader(FileInputStream("../app/src/main/assets/words.txt"))
        anagramDictionary = AnagramDictionary(inputStream)
    }

    @Test
    fun `get Anagram`() {
        val stopAnagram = anagramDictionary.getAnagrams("stop")
        val realStopAnagram = listOf("opts", "post", "pots", "spot", "stop", "tops")
        Assert.assertEquals(stopAnagram.size, realStopAnagram.size)
    }
}