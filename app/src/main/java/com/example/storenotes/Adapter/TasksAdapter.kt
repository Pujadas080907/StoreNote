package com.example.storenotes.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.storenotes.Models.Task
import com.example.storenotes.R
import kotlin.random.Random

class TasksAdapter(private val context: Context, val listener:TasksClickListener): RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {


    private val TasksList = ArrayList<Task>()
    private val fullTaskList = ArrayList<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item_to_do,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return TasksList.size
    }
    fun updateList(newList : List<Task>){

        fullTaskList.clear()
        fullTaskList.addAll(newList)

        TasksList.clear()
        TasksList.addAll(newList)
        notifyDataSetChanged()


    }
    fun filterList(search : String){

        TasksList.clear()

        for(item in fullTaskList){
            if(item.title?.lowercase()?.contains(search.lowercase()) == true ||
                item.task?.lowercase()?.contains(search.lowercase()) ==true){

                TasksList.add(item)
            }
        }
        notifyDataSetChanged()

    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = TasksList[position]
        holder.title2.text = currentTask.title
        holder.title2.isSelected = true

        holder.tasks.text = currentTask.task
        holder.tasks.isSelected = true

        holder.tasks_layout.setCardBackgroundColor(holder.itemView.resources.getColor(randomColor(), null))
        holder.tasks_layout.setOnClickListener{
            listener.onItemClicked(TasksList[holder.adapterPosition])
        }

        holder.tasks_layout.setOnLongClickListener{
            listener.onLongItemClicked( TasksList[holder.adapterPosition],holder.tasks_layout)
            true
        }

    }

    fun randomColor() : Int{


        val list = ArrayList<Int>()
        list.add(R.color.NoteColor1)
        list.add(R.color.NoteColor2)
        list.add(R.color.NoteColor3)
        list.add(R.color.NoteColor4)
        list.add(R.color.NoteColor5)
        list.add(R.color.NoteColor6)
        list.add(R.color.NoteColor7)
        list.add(R.color.NoteColor0)
        list.add(R.color.NoteColor9)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)
        return list[randomIndex]
    }

    inner class TaskViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val tasks_layout = itemView.findViewById<CardView>(R.id.taskCard_layout)
        val title2 = itemView.findViewById<TextView>(R.id.task_title)
        val tasks = itemView.findViewById<TextView>(R.id.et_task)
    }

    interface TasksClickListener{

        fun onItemClicked(task: Task)
        fun onLongItemClicked(task:Task, cardView: CardView)
    }
}