package com.example.android.unscramble.ui.game

import android.os.Build.VERSION_CODES.M
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {
    private val _score=MutableLiveData(0)
    private val _currentWordCount= MutableLiveData(0)
    private val _currentScrambledWord = MutableLiveData<String>()
    private var wordsList : MutableList<String> = mutableListOf()
    private lateinit var currentWord : String

    val currentScrambledWord: LiveData<String>
        get() =_currentScrambledWord
    val score: LiveData<Int>
        get() = _score
    val currentWordCount : LiveData<Int>
        get() = _currentWordCount

    init{
        getNextWord()
    }
    fun reinitializeData(){
        _score.value=0
        _currentWordCount.value=0
        wordsList.clear()
        getNextWord()
    }

    private fun getNextWord() {
        currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()

        while (String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }
        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord.value = String(tempWord)
            _currentWordCount.value=(_currentWordCount.value)?.inc()
            wordsList.add(currentWord)
        }
    }

    private fun increaseScore(){
        _score.value=(_score.value)?.plus(SCORE_INCREASE)
    }
    fun isUserWordCorrect(playerWord : String) : Boolean{
        if (playerWord.equals(currentWord,true)){
            increaseScore()
            return true
        } else{
            return false
        }
    }
    fun nextWord():Boolean{
        return if (_currentWordCount.value!! < MAX_NO_OF_WORDS){
            getNextWord()
            true
        }else false
    }

}