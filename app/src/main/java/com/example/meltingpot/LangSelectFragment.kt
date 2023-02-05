package com.example.meltingpot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLangSelectBinding.inflate(inflater, container, false)
        binding.spanish.setOnClickListener{
            findNavController().navigate(R.id.action_langSelectFragment_to_primaryFeed, Bundle().apply {
                putString("LANG", "Spanish")
            })
        }
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}