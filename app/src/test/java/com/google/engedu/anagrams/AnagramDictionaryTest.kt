package com.google.engedu.anagrams

import com.google.engedu.anagrams.AnagramDictionary.Companion.MIN_NUM_ANAGRAMS
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
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
        val anagram = anagramDictionary.getAnagrams("stop")
        val realStopAnagram = listOf("opts", "post", "pots", "spot", "stop", "tops")
        println(anagram)
        assertEquals(anagram.size, realStopAnagram.size)
    }

    @Test
    fun `get Anagram One More Letter`() {
        val anagram = anagramDictionary.getAnagramsWithOneMoreLetter("stop")
        val realStopAnagram = listOf(
            "poets",
            "posit",
            "plots",
            "stomp",
            "stoop",
            "ports",
            "sport",
            "strop",
            "posts",
            "spots",
            "pouts",
            "spout",
            "typos",
            "opts",
            "post",
            "pots",
            "spot",
            "tops"
        )

        println(anagram)
        assertEquals(anagram.size, realStopAnagram.size)
    }

    @Test
    fun `list and set at same size?`() {
        assertEquals(anagramDictionary.wordSet.size, anagramDictionary.wordList.size)
    }

    @Test
    fun `pickGoodWord get good word?`() {
        val candidate3 = anagramDictionary.pickGoodStarterWord()
        println(candidate3)
        assertTrue(anagramDictionary.getAnagramsWithOneMoreLetter(candidate3).size >= MIN_NUM_ANAGRAMS)

        val candidate4 = anagramDictionary.pickGoodStarterWord()
        println(candidate4)
        assertTrue(anagramDictionary.getAnagramsWithOneMoreLetter(candidate4).size >= MIN_NUM_ANAGRAMS)

        val candidate5 = anagramDictionary.pickGoodStarterWord()
        println(candidate5)
        assertTrue(anagramDictionary.getAnagramsWithOneMoreLetter(candidate5).size >= MIN_NUM_ANAGRAMS)

        val candidate6 = anagramDictionary.pickGoodStarterWord()
        println(candidate6)
        assertTrue(anagramDictionary.getAnagramsWithOneMoreLetter(candidate6).size >= MIN_NUM_ANAGRAMS)

        val candidate7 = anagramDictionary.pickGoodStarterWord()
        println(candidate7)
        assertTrue(anagramDictionary.getAnagramsWithOneMoreLetter(candidate7).size >= MIN_NUM_ANAGRAMS)

        val candidate8 = anagramDictionary.pickGoodStarterWord()
        println(candidate8)
        assertTrue(anagramDictionary.getAnagramsWithOneMoreLetter(candidate8).size >= MIN_NUM_ANAGRAMS)
    }

    @Test
    fun `is good word`() {
        val word = "post"
        assertEquals(anagramDictionary.isGoodWord("nonstop", word), true)
        assertEquals(anagramDictionary.isGoodWord("poster", word), false)
        assertEquals(anagramDictionary.isGoodWord("lamp post", word), false)
        assertEquals(anagramDictionary.isGoodWord("spots", word), true)
        assertEquals(anagramDictionary.isGoodWord("apostrophe", word), false)
    }
}