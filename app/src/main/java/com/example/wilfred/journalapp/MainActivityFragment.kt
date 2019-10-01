package com.example.wilfred.journalapp


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar

import com.example.wilfred.journalapp.Adapters.JournalRecyclerAdapter
import com.example.wilfred.journalapp.database.EntryViewModel
import com.example.wilfred.journalapp.database.JournalDatabase
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL


/**
 * A simple [Fragment] subclass.
 * Use the [MainActivityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainActivityFragment : Fragment(), JournalRecyclerAdapter.ItemClickListener {

    private val mProgressBar: ProgressBar? = null
    private val mGoogleSignInClient: GoogleSignInClient? = null
    //Firebase variables
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null


    lateinit var mEntryViewModel: EntryViewModel

    private var mUsername: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //PreferenceManager for settings
        PreferenceManager.setDefaultValues(context!!, R.xml.preferences, false)

        //Initialise DB
        JournalDatabase.getsInstance(context!!)



    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_main_fragment, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.entries_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this.context) as RecyclerView.LayoutManager?

        //Creat instance of JournalEntryAdapter
        val adapter = JournalRecyclerAdapter(this.context!!, this)


        val decoration = DividerItemDecoration(this.context!!, VERTICAL)
        recyclerView.addItemDecoration(decoration)
        recyclerView.adapter = adapter
        subscribeUiData(adapter)


        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            // Called when a user swipes left or right on a ViewHolder
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                val entries = adapter.entries
                mEntryViewModel.delete(entries!![position])

            }
        }).attachToRecyclerView(recyclerView)


        //Launch AddEntry Activity when Floating action button is clicked
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            // Create a new intent to start an AddEntryActivity
            val addEntryIntent = Intent(activity, AddEntry::class.java)
            startActivity(addEntryIntent)
        }

        mUsername = UNKNOWN_USER
        mFirebaseAuth = FirebaseAuth.getInstance()

        // Choose authentication providers
        val providers = listOf<AuthUI.IdpConfig>(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build())

        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                //If User is already loggedIn
            } else
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .setIsSmartLockEnabled(false)
                                .build(),
                        RC_SIGN_IN)
        }
        return view
    }

    ///Get data from DB and update UI
    private fun subscribeUiData(adapter: JournalRecyclerAdapter) {
        mEntryViewModel = ViewModelProviders.of(this).get(EntryViewModel::class.java)
        mEntryViewModel.allEntries!!.observe(this, Observer { journalEntries ->
            Log.i(TAG, "onChanged: $journalEntries")
            // Update the cached copy of the words in the adapter.
            adapter.entries = journalEntries
        })
    }


    override fun onPause() {
        super.onPause()
        mFirebaseAuth!!.removeAuthStateListener(mAuthListener!!)
    }

    override fun onResume() {
        super.onResume()
        mFirebaseAuth!!.addAuthStateListener(mAuthListener!!)
    }

    override fun onItemClickListener(itemId: Int) {
        val intent = Intent(this.context, EntryDetailActivity::class.java)
        intent.putExtra("journalId", itemId)
        startActivity(intent)
    }

    companion object {

        private val ARG_PARAM1 = "param1"
        val NEW_ENTRY_ACTIVITY_REQUEST_CODE = 1
        private val RC_SIGN_IN = 1236
        private val TAG = "MainActivity"
        private val UNKNOWN_USER = "anonymous"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment MainActivityFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String): MainActivityFragment {
            val fragment = MainActivityFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
