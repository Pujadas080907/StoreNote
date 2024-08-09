package com.example.storenotes.Activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.storenotes.Adapter.TasksAdapter
import com.example.storenotes.Database.NoteDatabase
import com.example.storenotes.Models.Task
import com.example.storenotes.Models.TaskViewModel
import com.example.storenotes.R
import com.example.storenotes.databinding.ActivityMainBinding
import com.example.storenotes.databinding.ActivityToDoListBinding

class ToDoList : AppCompatActivity(), TasksAdapter.TasksClickListener, PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivityToDoListBinding
    private lateinit var database: NoteDatabase
    lateinit var viewModelTask: TaskViewModel
    lateinit var taskAdapter : TasksAdapter
    lateinit var selectedTask: Task

    private val updateTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->

        if(result.resultCode == Activity.RESULT_OK){
            val task = result.data?.getSerializableExtra("task") as? Task
            if(task != null){
                viewModelTask.updateTask(task)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityToDoListBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        initTaskUI()

        viewModelTask = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(TaskViewModel::class.java)
        viewModelTask.allTask.observe(this){ list->
            list?.let {
                taskAdapter.updateList(list)
            }
        }
        database = NoteDatabase.getDatabase(this)
    }

    private fun initTaskUI() {
        binding.recyclerViewTask.setHasFixedSize(true)
        binding.recyclerViewTask.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        taskAdapter = TasksAdapter(this,this)
        binding.recyclerViewTask.adapter = taskAdapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if(result.resultCode == Activity.RESULT_OK){
                val task = result.data?.getSerializableExtra("task") as? Task
                if(task != null){
                    viewModelTask.insertTask(task)
                }
            }
        }
        binding.fbAddTask.setOnClickListener {
            val intent = Intent(this, AddTask::class.java)
            getContent.launch(intent)
        }
    }

    override fun onItemClicked(task: Task) {
        val intent = Intent(this@ToDoList, AddTask::class.java)
        intent.putExtra("current_task",task)
        updateTask.launch(intent)
    }

    override fun onLongItemClicked(task: Task, cardView: CardView) {
        selectedTask = task
        popUpDisplay(this, cardView)
    }

    private fun popUpDisplay(activity: Activity, cardView: CardView){
        val popup = PopupMenu(this,cardView)
        popup.setOnMenuItemClickListener(this@ToDoList)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.delete_note){

            viewModelTask.deleteTask(selectedTask)
            return true
        }
        return false
    }
}