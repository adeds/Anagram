/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.engedu.anagrams

import java.io.BufferedReader
import java.io.Reader
import java.util.*
import kotlin.random.Random

class AnagramDictionary(reader: Reader?) {
    val wordList: MutableList<String> = mutableListOf()
    val wordSet: HashSet<String> = hashSetOf()
    private val lettersToWord: HashMap<String, List<String>> = hashMapOf()

    fun isGoodWord(word: String, base: String): Boolean {
        return wordSet.contains(word) && word.contains(base).not()
    }

    fun getAnagrams(targetWord: String): List<String> {
        val sorted = sortLetters(targetWord)
        return wordList.filter { s ->
            s.length == sorted.length && sortLetters(s) == sorted
        }
    }

    fun getAnagramsWithOneMoreLetter(word: String): List<String> {
        val alphabet = listOf(
            "a",
            "b",
            "c",
            "d",
            "e",
            "f",
            "g",
            "h",
            "i",
            "j",
            "k",
            "l",
            "m",
            "n",
            "o",
            "p",
            "q",
            "r",
            "s",
            "t",
            "u",
            "v",
            "w",
            "x",
            "y",
            "z"
        )

        val oneMoreAnagram = mutableListOf<String>()
        alphabet.forEach {
            val addedWord = sortLetters(word + it)
            oneMoreAnagram.addAll(lettersToWord.getOrElse(addedWord) { emptyList() })
        }
        oneMoreAnagram.addAll(lettersToWord.getOrElse(sortLetters(word)) { emptyList() })
        return oneMoreAnagram
    }

    fun pickGoodStarterWord(): String {
        val goodWord : String
        while (true){
            val candidate = wordList[Random.nextInt(0, wordList.size)]
            if (getAnagramsWithOneMoreLetter(candidate).size >= MIN_NUM_ANAGRAMS){
                goodWord = candidate
                break
            }
        }
        return goodWord
    }

    private fun sortLetters(word: String) = String(word.toCharArray().apply { Arrays.sort(this) })

    companion object {
        const val MIN_NUM_ANAGRAMS = 5
        private const val DEFAULT_WORD_LENGTH = 3
        private const val MAX_WORD_LENGTH = 7
    }

    init {
        val `in` = BufferedReader(reader)
        var line: String
        while (
            `in`.readLine().also { line = it.orEmpty() } != null) {
            line.trim { it <= ' ' }.toLowerCase(Locale.ROOT).let {
                wordList.add(it)
                wordSet.add(it)
                val sorted = sortLetters(it)

                lettersToWord[sorted] = lettersToWord.getOrElse(sorted) { emptyList() }
                    .toMutableList()
                    .apply { add(it) }
            }
        }
    }
}