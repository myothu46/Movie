package com.example.movie.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v4.app.Fragment
import android.arch.lifecycle.ViewModelProvider
import android.support.v4.app.FragmentTransaction
import android.widget.Button
import android.widget.Toast
import com.example.movie.R
import com.example.movie.databinding.FragmentNewMovieBinding
import com.example.movie.models.ItemsViewModel
import com.example.movie.store.DatabaseHandler

class NewMovieFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentNewMovieBinding? = null

    private val binding get() = _binding!!
    lateinit var databaseHandler: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNewMovieBinding.inflate(inflater, container, false)
        val root = binding.root
        var btnSave: Button = binding.save

        databaseHandler = context?.let { DatabaseHandler(it) }!!
        btnSave.setOnClickListener {
            saveData()
        }

        return root
    }

    fun saveData() {
        val id = "1"
        val title = binding.title.text.toString()
        val subtitle = binding.subtitle.text.toString()
        val description = binding.description.text.toString()

        if (title.trim() != "" && subtitle.trim() != "") {
            val status =
                databaseHandler.addEmployee(
                    ItemsViewModel(
                        Integer.parseInt(id),
                        title,
                        subtitle,
                        description
                    )
                )
            if (status > -1) {
                binding.title.text = null
                binding.subtitle.text = null
                binding.description.text = null
                Toast.makeText(context, "record save", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(
                context,
                "id or name or email cannot be blank",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): NewMovieFragment {
            return NewMovieFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}