package com.example.android.unscramble.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.unscramble.R
import com.example.android.unscramble.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Fragment where the game is played, contains the game logic.
 */

class GameFragment : Fragment() {

    private val viewModel :GameViewModel by viewModels()
    private lateinit var binding: GameFragmentBinding


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.game_fragment,container,false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.gameViewModel=viewModel
        binding.maxNoOfWords= MAX_NO_OF_WORDS
        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner=viewLifecycleOwner

        // Setup a click listener for the Submit and Skip buttons.
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }

    }

    private fun onSubmitWord() {
        val playerWord=binding.textInputEditText.text.toString()
        if(viewModel.isUserWordCorrect(playerWord)){
            setErrorTextField(false)
            if(!viewModel.nextWord()){
                showFinalScoreDialog()
            }
        }else{
            setErrorTextField(true)
        }
    }

    private fun onSkipWord() {
        if(viewModel.nextWord()){
            setErrorTextField(false)
        } else{
            showFinalScoreDialog()
        }
    }


    private fun showFinalScoreDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.you_scored,viewModel.score.value))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)){_, _->
                exitGame()
            }
            .setPositiveButton(getString(R.string.play_again)){_, _->
                restartGame()
            }
            .show()


    }

    private fun restartGame() {
        viewModel.reinitializeData()
        setErrorTextField(false)
    }

    private fun exitGame() {
        activity?.finish()
    }

    /*
    * Sets and resets the text field error status.
    */
    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }
}
