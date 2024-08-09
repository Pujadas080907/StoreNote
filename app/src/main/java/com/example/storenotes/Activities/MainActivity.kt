package com.example.storenotes.Activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.storenotes.Models.Note
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.storenotes.Adapter.NotesAdapter
import com.example.storenotes.Adapter.TasksAdapter
import com.example.storenotes.Database.NoteDatabase
import com.example.storenotes.Models.NoteViewModel
import com.example.storenotes.Models.Task
import com.example.storenotes.Models.TaskViewModel
import com.example.storenotes.R
import com.example.storenotes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),NotesAdapter.NotesClickListener, PopupMenu.OnMenuItemClickListener{

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NoteDatabase
    lateinit var viewModel: NoteViewModel
    lateinit var adapter: NotesAdapter
    lateinit var selectedNote: Note


    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

        if(result.resultCode == Activity.RESULT_OK){

            val note = result.data?.getSerializableExtra("note") as? Note

            if(note != null){
                viewModel.updateNote(note)
            }
        }



    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        initNoteUI()

        viewModel = ViewModelProvider(this,
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)
        viewModel.allnotes.observe(this){ list->
            list?.let {
                adapter.updateList(list)
            }
        }
        database = NoteDatabase.getDatabase(this)

    }

    private fun initNoteUI() {
        binding.recyclerViewNote.setHasFixedSize(true)
        binding.recyclerViewNote.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter = NotesAdapter(this,this)
        binding.recyclerViewNote.adapter = adapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if(result.resultCode == Activity.RESULT_OK){
                val note = result.data?.getSerializableExtra("note") as? Note
                if (note != null){
                    viewModel.insertNote(note)
                }
            }
        }
        binding.fbAddNote.setOnClickListener {
            val intent = Intent(this, AddNote::class.java)
            getContent.launch(intent)
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if(newText != null){

                    adapter.filterList(newText)

                }

                return true
            }


        })
    }

    override fun onItemClicked(note: com.example.storenotes.Models.Note) {
        val intent = Intent(this@MainActivity, AddNote::class.java)
        intent.putExtra("current_note",note)
        updateNote.launch(intent)
    }

    override fun onLongItemClicked(note: com.example.storenotes.Models.Note, cardView: CardView) {
        selectedNote = note
        popUpDisplay(this, cardView)
    }

    private fun popUpDisplay(activity: Activity, cardView: CardView){
        val popup = PopupMenu(this,cardView)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.delete_note){

            viewModel.deleteNote(selectedNote)
            return true
        }
        return false
    }
}