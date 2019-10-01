package com.example.wilfred.journalapp.Adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.wilfred.journalapp.R
import com.example.wilfred.journalapp.database.JournalEntry

import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Created by wilfred on 6/29/18.
 */

class JournalRecyclerAdapter(private val mContext: Context, // Member variable to handle item clicks
                             private val mItemClickListener: ItemClickListener) : RecyclerView.Adapter<JournalRecyclerAdapter.JournalVH>() {

    //final private ItemClickListener itemClickListener;

    /**
     * When data changes, this method updates the list of taskEntries
     * and notifies the adapter to use the new values on it
     */
    var entries: List<JournalEntry>? = null
        set(entries) {
            field = entries
            notifyDataSetChanged()
        }

    //Date formater
    private val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): JournalVH {
        val view = LayoutInflater.from(mContext)
                .inflate(R.layout.entry_item, viewGroup, false)
        return JournalVH(view)
    }

    override fun onBindViewHolder(holder: JournalVH, position: Int) {
        val entry = entries!![position]
        val title = entry.entryTitle
        val entryText = entry.entryText
        val date = dateFormat.format(entry.updatedAt)

        holder.mTitleTextView.text = title
        holder.mEntryTextView.text = entryText
        holder.mDateTextView.text = date

    }

    /**
     * Returns the number of items to display.
     */
    override fun getItemCount(): Int {
        return if (entries == null) {
            0
        } else entries!!.size
    }

    interface ItemClickListener {
        fun onItemClickListener(itemId: Int)
    }

    inner class JournalVH(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {


        var mEntryTextView: TextView = itemView.findViewById(R.id.entry_text_view)
        var mTitleTextView: TextView = itemView.findViewById(R.id.title_text_view)
        var mDateTextView: TextView = itemView.findViewById(R.id.date_text_view)


        init {

            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val elementId = entries!![adapterPosition].id
            mItemClickListener.onItemClickListener(elementId)
        }

    }

    companion object {
        //Date format Constant
        private const val DATE_FORMAT = "dd/MM/yyyy"
    }
}
