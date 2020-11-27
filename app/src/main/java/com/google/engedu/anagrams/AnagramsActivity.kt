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

import android.os.Bundle
import android.text.Html
import android.text.InputType
import android.text.TextUtils
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.engedu.anagrams.databinding.ActivityAnagramsBinding
import com.google.engedu.anagrams.databinding.ContentAnagramsBinding
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

class AnagramsActivity : AppCompatActivity() {
    private var currentWord: String? = null
    private lateinit var dictionary: AnagramDictionary
    private lateinit var anagrams: MutableList<String>
    private lateinit var binding: ActivityAnagramsBinding
    private lateinit var contentBinding: ContentAnagramsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnagramsBinding.inflate(layoutInflater)
        contentBinding = ContentAnagramsBinding.bind(binding.content.root)
        setContentView(binding.root)
        anagrams = mutableListOf()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val assetManager = assets
        try {
            val inputStream = assetManager.open("words.txt")
            dictionary = AnagramDictionary(InputStreamReader(inputStream))
        } catch (e: IOException) {
            val toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG)
            toast.show()
        }
        // init click listener
        initClickListener()

    }

    private fun initClickListener() {
        contentBinding.editText.apply {
            setRawInputType(InputType.TYPE_CLASS_TEXT)
            imeOptions = EditorInfo.IME_ACTION_GO
            setOnEditorActionListener { _, actionId, event ->
                var handled = false
                if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_NULL && event != null && event.action == KeyEvent.ACTION_DOWN) {
                    processWord(this)
                    handled = true
                }
                handled
            }
        }
        binding.fab.setOnClickListener { defaultAction() }
    }

    private fun processWord(editText: EditText) {
        var word = editText.text.toString().trim { it <= ' ' }.toLowerCase(Locale.ROOT)
        if (word.isEmpty()) {
            return
        }
        var color = "#cc0029"
        if (dictionary.isGoodWord(word, currentWord.orEmpty()) && anagrams.contains(word)) {
            color = "#00aa29"
        } else {
            word = "X $word"
        }
        anagrams.remove(word)
        contentBinding.resultView.append(Html.fromHtml(String.format("<font color=%s>%s</font><BR>", color, word)))
        editText.setText("")
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_anagrams, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun defaultAction() {
        if (currentWord == null) {
            currentWord = dictionary.pickGoodStarterWord()

            anagrams.clear()
            anagrams.addAll(dictionary.getAnagramsWithOneMoreLetter(currentWord.orEmpty()))

            contentBinding.gameStatusView.text = Html.fromHtml(
                String.format(
                    START_MESSAGE,
                    currentWord.orEmpty().toUpperCase(Locale.ROOT),
                    currentWord
                )
            )
            binding.fab.setImageResource(android.R.drawable.ic_menu_help)
            binding.fab.hide()
            contentBinding.resultView.text = ""
            contentBinding.editText.apply {
                setText("")
                isEnabled = true
                requestFocus()
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            }
        } else {
            contentBinding.editText.setText(currentWord)
            contentBinding.editText.isEnabled = false
            binding.fab.setImageResource(android.R.drawable.ic_media_play)
            currentWord = null
            contentBinding.resultView.append(TextUtils.join("\n", anagrams))
            contentBinding.gameStatusView.append(" Hit 'Play' to start again")
        }
    }

    companion object {
        const val START_MESSAGE =
            "Find as many words as possible that can be formed by adding one letter to <big>%s</big> (but that do not contain the substring %s)."
    }
}