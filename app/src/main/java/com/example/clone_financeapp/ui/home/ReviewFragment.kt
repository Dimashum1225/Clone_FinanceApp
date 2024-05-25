package com.example.clone_financeapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.clone_financeapp.databinding.FragmentReviewBinding

class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var mainButton: Button
    private lateinit var plusButton: Button
    private lateinit var minusButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(ReviewViewModel::class.java)

        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.
        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
            mainButton = binding.mainButton
            plusButton = binding.plusButton
            minusButton = binding.minusButton

            mainButton.setOnClickListener{
                toggleButtons()
            }
        }
        return root
    }

    fun toggleButtons(){
        if(plusButton.visibility == View.GONE && minusButton.visibility == View.GONE){
            plusButton.visibility = View.VISIBLE
            minusButton.visibility = View.VISIBLE
        }else{
            plusButton.visibility = View.GONE
            minusButton.visibility = View.GONE
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}