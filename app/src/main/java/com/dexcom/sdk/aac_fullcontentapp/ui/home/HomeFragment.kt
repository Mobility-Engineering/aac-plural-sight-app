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
import androidx.recyclerview.widget.LinearLayoutManager
import com.dexcom.sdk.aac_fullcontentapp.DataManager
import com.dexcom.sdk.aac_fullcontentapp.NoteActivity
import com.dexcom.sdk.aac_fullcontentapp.NoteInfo
import com.dexcom.sdk.aac_fullcontentapp.NoteRecyclerAdapter
import com.dexcom.sdk.aac_fullcontentapp.databinding.ActivityNoteList2Binding
import com.dexcom.sdk.aac_fullcontentapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var noteRecyclerAdapter: NoteRecyclerAdapter? = null
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
        noteRecyclerAdapter?.notifyDataSetChanged()
        super.onResume()
    }

    private fun initializeDisplayContent() {
        val context = activity as Context
        val recyclerNotes = binding.listNotes
        val notesLayoutManager = LinearLayoutManager(context)
        recyclerNotes.layoutManager = notesLayoutManager
        val notes = DataManager.instance?.notes
        noteRecyclerAdapter = notes?.let{NoteRecyclerAdapter(context, it) }
        recyclerNotes.adapter = noteRecyclerAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}