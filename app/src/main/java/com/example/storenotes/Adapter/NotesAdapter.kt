package com.example.storenotes.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.storenotes.Models.Note
import com.example.storenotes.R
import kotlin.random.Random

class NotesAdapter(private val context: Context, val listener:NotesClickListener): RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val NoteList = ArrayList<Note>()
    private val  fullList = ArrayList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return NoteList.size
    }
    fun updateList(newList : List<Note>){

        fullList.clear()
        fullList.addAll(newList)

        NoteList.clear()
        NoteList.addAll(newList)
        notifyDataSetChanged()


    }
    fun filterList(search : String){

        NoteList.clear()

        for(item in fullList){
            if(item.title?.lowercase()?.contains(search.lowercase()) == true ||
                item.note?.lowercase()?.contains(search.lowercase()) ==true){

                NoteList.add(item)
            }
        }
        notifyDataSetChanged()

    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = NoteList[position]
        holder.title.text = currentNote.title
        holder.title.isSelected = true

        holder.Note_tv.text = currentNote.note
        holder.date.text = currentNote.date
        holder.date.isSelected = true

        holder.notes_layout.setCardBackgroundColor(holder.itemView.resources.getColor(randomColor(), null))

        holder.notes_layout.setOnClickListener{
            listener.onItemClicked(NoteList[holder.adapterPosition])
        }

        holder.notes_layout.setOnLongClickListener{
            listener.onLongItemClicked( NoteList[holder.adapterPosition],holder.notes_layout)
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

    inner class NoteViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val notes_layout = itemView.findViewById<CardView>(R.id.card_layout)
        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val Note_tv = itemView.findViewById<TextView>(R.id.tv_note)
        val date = itemView.findViewById<TextView>(R.id.tv_date)


    }

    interface NotesClickListener{

        fun onItemClicked(note:Note)
        fun onLongItemClicked(note:Note, cardView: CardView)
    }
}