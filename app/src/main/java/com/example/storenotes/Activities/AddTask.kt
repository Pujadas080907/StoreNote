package com.example.storenotes.Activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.storenotes.Models.Note
import com.example.storenotes.Models.Task
import com.example.storenotes.R
import com.example.storenotes.databinding.ActivityAddTaskBinding
import com.example.storenotes.databinding.ActivityToDoListBinding
import android.content.Intent
import android.app.Activity

class AddTask : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    private lateinit var task: Task
    private lateinit var old_task: Task
    var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        try {
            old_task = intent.getSerializableExtra("current_task") as Task
            binding.etTitle.setText(old_task.title)
            binding.etTask.setText(old_task.task)
            isUpdate = true;
        }catch (e : Exception){
            e.printStackTrace()
        }
        binding.saveTask.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val taskDes = binding.etTask.text.toString()


            if (title.isNotEmpty() || taskDes.isNotEmpty()) {
                if (isUpdate) {
                    task = Task(old_task.id, title, taskDes)
                } else {
                    task = Task(null, title, taskDes)
                }

                val intent = Intent()
                intent.putExtra("task", task)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this@AddTask, "Please enter task details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }

        binding.backArrow.setOnClickListener {
            onBackPressed()

        }

    }
}