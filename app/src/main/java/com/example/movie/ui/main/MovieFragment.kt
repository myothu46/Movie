package com.example.movie.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v4.app.Fragment
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import com.example.movie.DetailActivity
import com.example.movie.adapter.MovieAdapter
import com.example.movie.databinding.FragmentMovieBinding
import com.example.movie.models.ItemsViewModel
import com.example.movie.store.DatabaseHandler
import com.google.gson.Gson


class MovieFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!
    val data = ArrayList<ItemsViewModel>()
    lateinit var databaseHandler: DatabaseHandler
    lateinit var recyclerview: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
        databaseHandler = context?.let { DatabaseHandler(it) }!!
    }

    override fun onResume() {
        super.onResume()
        reloadData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        val root = binding.root
        recyclerview = binding.recyclerview
        recyclerview.layoutManager = LinearLayoutManager(activity)

        val refresh: Button = binding.refresh
        refresh.setOnClickListener {
            reloadData()
        }

        return root
    }

    private fun onListItemClick(position: Int) {
        val intent = Intent(activity, DetailActivity::class.java)
        val gson = Gson()
        val myData = gson.toJson(data[position])
        intent.putExtra("myData", myData)
        startActivity(intent)
    }

    fun reloadData() {
        databaseHandler = DatabaseHandler(context!!)
        var empl = databaseHandler.viewEmployee()
        data.clear()
        for (emp in empl) {
            data.add(ItemsViewModel(emp.id, emp.title, emp.subtitle, emp.description))
        }
        val adapter = MovieAdapter(data) { position -> onListItemClick(position) }
        recyclerview.adapter = adapter
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): MovieFragment {
            return MovieFragment().apply {
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