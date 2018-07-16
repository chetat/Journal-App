package com.example.wilfred.journalapp;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.wilfred.journalapp.Adapters.JournalRecyclerAdapter;
import com.example.wilfred.journalapp.database.EntryViewModel;
import com.example.wilfred.journalapp.database.JournalDatabase;
import com.example.wilfred.journalapp.database.JournalEntry;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainActivityFragment extends Fragment implements JournalRecyclerAdapter.ItemClickListener {

    private static final String ARG_PARAM1 = "param1";
    public static final int NEW_ENTRY_ACTIVITY_REQUEST_CODE = 1;
    private static final int RC_SIGN_IN = 1236;
    private static final String TAG = "MainActivity";
    private static final String UNKNOWN_USER = "anonymous";

    private ProgressBar mProgressBar;
    private GoogleSignInClient mGoogleSignInClient;
    //Firebase variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    public EntryViewModel mEntryViewModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mUsername;


    private JournalDatabase mJDatabase;


    public MainActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MainActivityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainActivityFragment newInstance(String param1) {
        MainActivityFragment fragment = new MainActivityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //PreferenceManager for settings
        PreferenceManager.setDefaultValues(getContext(), R.xml.preferences, false);

        //Initialise DB
        mJDatabase = JournalDatabase.getsInstance(getContext());


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_activity_layout, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.entries_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        //Creat instance of JournalEntryAdapter
        final JournalRecyclerAdapter adapter = new JournalRecyclerAdapter(this.getContext(), this);


        DividerItemDecoration decoration = new DividerItemDecoration(this.getContext(), VERTICAL);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);
        subscribeUiData(adapter);


         /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                List<JournalEntry> entries = adapter.getEntries();
                mEntryViewModel.delete(entries.get(position));

            }
        }).attachToRecyclerView(recyclerView);


        //Launch AddEntry Activity when Floating action button is clicked
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new intent to start an AddEntryActivity
                Intent addEntryIntent = new Intent(getActivity(), AddEntry.class);
                startActivityForResult(addEntryIntent, NEW_ENTRY_ACTIVITY_REQUEST_CODE);
            }
        });

        mUsername = UNKNOWN_USER;
        mFirebaseAuth = FirebaseAuth.getInstance();

// Choose authentication providers
        final List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //If User is already loggedIn
                } else startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .setIsSmartLockEnabled(false)
                                .build(),
                        RC_SIGN_IN);
            }
        };
        return view;
    }

    ///Get data from DB and update UI
    private void subscribeUiData(final JournalRecyclerAdapter adapter) {
        mEntryViewModel = ViewModelProviders.of(this).get(EntryViewModel.class);
        mEntryViewModel.getAllEntries().observe(this, new Observer<List<JournalEntry>>() {
            @Override
            public void onChanged(List<JournalEntry> journalEntries) {
                Log.i(TAG, "onChanged: " + journalEntries.toString());
                // Update the cached copy of the words in the adapter.
                adapter.setEntries(journalEntries);
            }
        });
    }

    //Insert data into Database
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_ENTRY_ACTIVITY_REQUEST_CODE
                && resultCode == RESULT_OK) {
            String title = data.getStringExtra("title");
            String textEntry = data.getStringExtra("entry");
            Date date = new Date(data.getExtras().getLong("date", -1));

            JournalEntry entry = new JournalEntry(title, textEntry, date);
            mEntryViewModel.insert(entry);
        } else {
            Toast.makeText(
                    this.getActivity(),
                    "Error Saving entry",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(this.getContext(), EntryDetailActivity.class);
        intent.putExtra("itemData", itemId);
        startActivity(intent);
    }
}
