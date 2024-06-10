package com.example.clone_financeapp.ui.add_incomes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.clone_financeapp.databinding.FragmentAddIncomesBinding




class Add_IncomesFragment : Fragment() {

    private var _binding: FragmentAddIncomesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val Add_incomesViewModel =

//            ViewModelProvider(this).get(Add_incomesViewModel::class.java)
            ViewModelProvider(this).get(Add_incomesViewModel::class.java)

        _binding = FragmentAddIncomesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.newIncome
        Add_incomesViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



