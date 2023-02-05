package com.example.meltingpot

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.meltingpot.databinding.FragmentLangSelectBinding

/**
 * A simple [Fragment] subclass.
 * Use the [LangSelect.newInstance] factory method to
 * create an instance of this fragment.
 */
class LangSelectFragment : Fragment() {
    companion object {
        val TAG: String = "LangSelectFragment"
    }
    private var _binding: FragmentLangSelectBinding? = null
    private val binding get() = _binding!!
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLangSelectBinding.inflate(inflater, container, false)
        binding.selectBtn.isEnabled = false
        // get reference to the string array that we just created
        val languages = resources.getStringArray(R.array.available_languages)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdownitem, languages)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
        binding.autoCompleteTextView.addTextChangedListener {
            binding.selectBtn.isEnabled = true
        }
        binding.selectBtn.setOnClickListener{
            Log.d(TAG, "lang selected: ${binding.autoCompleteTextView.text.toString()}")
            findNavController().navigate(R.id.action_langSelectFragment_to_primaryFeed, Bundle().apply {
                putString("LANG", binding.autoCompleteTextView.text.toString())
            })
        }
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}