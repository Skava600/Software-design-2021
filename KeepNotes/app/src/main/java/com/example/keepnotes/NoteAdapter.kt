package com.example.keepnotes

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.keepnotes.databinding.FragmentNoteItemBinding

import com.example.keepnotes.models.Note
import com.example.keepnotes.touchcallback.NoteTouchCallBack
import com.example.keepnotes.viewmodel.NoteViewModel
import java.util.*


class NoteAdapter(private val onItemClickListener: AdapterView.OnItemClickListener,
                  private val onItemLongClickListener: AdapterView.OnItemLongClickListener
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>(), NoteTouchCallBack.ItemTouchHelperContract {

    private var  notes: MutableList<Note>? = mutableListOf()

    fun setData(notes: List<Note>)
    {
        this.notes = notes.sortedBy {
            it.index
        }.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {

        return NoteViewHolder(
            FragmentNoteItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = notes!![position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = notes!!.size

    inner class NoteViewHolder(binding: FragmentNoteItemBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener, View.OnLongClickListener{
        val idView: TextView = binding.noteTitle
        val contentView: TextView = binding.noteBody
        val itemView = binding.noteItem

        fun bind(item: Note)
        {
            idView.text = item.title
            contentView.text = item.body
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(view: View?) {
            onItemClickListener.onItemClick(null, view, bindingAdapterPosition, view!!.id.toLong())
        }

        override fun onLongClick(view: View?): Boolean {
            onItemLongClickListener.onItemLongClick(null, view, bindingAdapterPosition, view!!.id.toLong())
            return true
        }

    }

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(notes, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(notes, i, i - 1)
            }
        }

        notifyItemMoved(fromPosition, toPosition)
    }

    fun getNotes(): List<Note> =  this.notes!!

}