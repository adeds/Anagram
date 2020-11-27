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
import kotlin.collections.ArrayList

class AnagramDictionary(reader: Reader?) {
    private val random = Random()
    private val wordList: ArrayList<String> = arrayListOf()
    private lateinit var wordSet: HashSet<String>
    private val lettersToWord: HashMap<String, List<String>> = hashMapOf()

    fun isGoodWord(word: String?, base: String?): Boolean {
        return true
    }

    fun getAnagrams(targetWord: String): List<String> {
        val sorted = sortLetters(targetWord)
        return wordList.filter { s ->
            s.length == sorted.length && sortLetters(s) == sorted
        }.apply { lettersToWord[sorted] = wordList }
    }

    fun getAnagramsWithOneMoreLetter(word: String?): List<String> {
        return ArrayList()
    }

    fun pickGoodStarterWord(): String {
        return "stop"
    }

    private fun sortLetters(word: String) = String(word.toCharArray().apply { Arrays.sort(this) })


    fun lettersToWord(word: String?) {

    }

    companion object {
        private const val MIN_NUM_ANAGRAMS = 5
        private const val DEFAULT_WORD_LENGTH = 3
        private const val MAX_WORD_LENGTH = 7
    }

    init {
        val `in` = BufferedReader(reader)
        var line: String
        while (
            `in`.readLine().also { line = it.orEmpty() } != null) {
            line.trim { it <= ' ' }.let { wordList.add(it) }
        }
    }
}