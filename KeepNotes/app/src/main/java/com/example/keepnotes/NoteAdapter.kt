package com.example.keepnotes

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import com.example.keepnotes.databinding.FragmentNoteBinding
import com.example.keepnotes.models.Note


class NoteAdapter(
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var  notes: MutableList<Note>? = mutableListOf()

    fun setData(notes: List<Note>)
    {
        this.notes = notes.sortedBy {
            it.noteId
        }.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {

        return NoteViewHolder(
            FragmentNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = notes!![position]
        holder.idView.text = item.title
        holder.contentView.text = item.body
        holder.bind(item)
    }

    override fun getItemCount(): Int = notes!!.size

    inner class NoteViewHolder(binding: FragmentNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.noteTitle
        val contentView: TextView = binding.noteBody

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }

        fun bind(item: Note)
        {
            idView.text = item.title
            contentView.text = item.body
        }

    }

}