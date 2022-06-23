package com.dexcom.sdk.aac_fullcontentapp.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dexcom.sdk.aac_fullcontentapp.*
import com.dexcom.sdk.aac_fullcontentapp.databinding.ActivityNoteList2Binding
import com.dexcom.sdk.aac_fullcontentapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private var noteListSize = DataManager.instance?.notes?.size!!
    private val binding get() = _binding!!
    private var noteRecyclerAdapter: NoteRecyclerAdapter? = null
    private var courseRecyclerAdapter: CourseRecyclerAdapter? = null
    private lateinit var adapterNotes: ArrayAdapter<NoteInfo>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initializeDisplayContent()
        return root
    }

    override fun onResume() {
        if (DataManager.instance?.notes?.size!! > noteListSize) {
            noteListSize++
            noteRecyclerAdapter?.notifyItemInserted(noteListSize - 1)
        }
        else
            DataManager!!.instance?.let { noteRecyclerAdapter?.notifyItemChanged(it.currentlyDisplayedNote) }
        super.onResume()
    }





    private fun initializeDisplayContent() {
        displayNotes()
    }

    private fun displayNotes() {
        val context = activity as Context
        val recyclerNotes = binding.listItems
        val notesLayoutManager = LinearLayoutManager(context)
        recyclerNotes.layoutManager = notesLayoutManager
        val notes = DataManager.instance?.notes
        noteRecyclerAdapter = notes?.let{NoteRecyclerAdapter(context, it) }
        recyclerNotes.adapter = noteRecyclerAdapter
    }
    private fun displayCourses() {
        val context = activity as Context
            val recyclerCourses = binding.listItems
        val courseLayoutManager = GridLayoutManager(context, 2)
        recyclerCourses.layoutManager = courseLayoutManager
        val courses = DataManager.instance?.courses
        courseRecyclerAdapter = courses?.let{CourseRecyclerAdapter(context, it) }
        recyclerCourses.adapter = courseRecyclerAdapter
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}