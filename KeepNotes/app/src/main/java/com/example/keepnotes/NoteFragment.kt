package com.example.keepnotes

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.keepnotes.models.Note
import com.example.keepnotes.viewmodel.NoteViewModel
import java.util.*

class NoteFragment : Fragment() {

    private val args:NoteFragmentArgs by navArgs()

    private var title:String = ""
    private var body: String = ""

    private val viewModel : NoteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_note, container, false)
        setHasOptionsMenu(true)
        val titleText = view.findViewById<EditText>(R.id.titleEditView)
        val bodyText = view.findViewById<EditText>(R.id.bodyEditText)

        titleText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus)
            {
                title = titleText.text.toString()
            }
        }

        bodyText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus)
            {
                body = bodyText.text.toString()
            }
        }


        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_app_bar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                val date = Date().toString()
                if (title.isEmpty()) {
                    title = date

                }


                val note = Note(null, title, body, "", date, args.index)

                viewModel.insert(note)

            }

            R.id.action_image -> {

            }

        }

        return super.onOptionsItemSelected(item)
    }
}