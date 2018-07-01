package com.example.wilfred.journalapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Update;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.wilfred.journalapp.Adapters.JournalRecyclerAdapter;
import com.example.wilfred.journalapp.Utils.JournalPreferences;
import com.example.wilfred.journalapp.database.EntryViewModel;
import com.example.wilfred.journalapp.database.JournalDatabase;
import com.example.wilfred.journalapp.database.JournalEntry;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class MainActivity extends AppCompatActivity implements JournalRecyclerAdapter.ItemClickListener {
    public static final int NEW_ENTRY_ACTIVITY_REQUEST_CODE = 1;

    private static final int RC_SIGN_IN = 1236;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "MainActivity";
    private ProgressBar mProgressBar;
    private String mUsername;
    private static final String UNKNOWN_USER = "anonymous";

    //Firebase variables
    private FirebaseAuth mFirebaseAuth;
    private ChildEventListener mChildEventListener;
    private FirebaseDatabase mDatabase;
    private FirebaseDatabase mDatabaseReference;
    private FirebaseAuth.AuthStateListener mAuthListener;

   public EntryViewModel mEntryViewModel;


    private JournalRecyclerAdapter adapter;
    private RecyclerView mRecyclerView;
    private JournalDatabase mJDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //PreferenceManager for settings
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);



        //Initialise DB
        mJDatabase = JournalDatabase.getsInstance(getApplicationContext());

        mRecyclerView = findViewById(R.id.entries_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Initialise adapter
        adapter = new JournalRecyclerAdapter(this,this);
        mRecyclerView.setAdapter(adapter);


        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

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
        }).attachToRecyclerView(mRecyclerView);


        mEntryViewModel = ViewModelProviders.of(this).get(EntryViewModel.class);
        mEntryViewModel.getAllEntries().observe(this, new Observer<List<JournalEntry>>() {
            @Override
            public void onChanged(@Nullable List<JournalEntry> journalEntries) {
                // Update the cached copy of the words in the adapter.
                adapter.setEntries(journalEntries);
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new intent to start an AddTaskActivity
                Intent addEntryIntent = new Intent(MainActivity.this, AddEntry.class);
                startActivityForResult(addEntryIntent, NEW_ENTRY_ACTIVITY_REQUEST_CODE);
            }
        });


        mUsername = UNKNOWN_USER;

        mDatabase = FirebaseDatabase.getInstance();
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
                    return;
                } else startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .setIsSmartLockEnabled(false)
                                .build(),
                        RC_SIGN_IN);
            }
        };


    }

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
                    getApplicationContext(),
                    "Error Saving entry",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.setings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.signOut:
                AuthUI.getInstance()
                        .signOut(this);
                return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    private void setupSharedPreferences() {
        // Get all of the values from shared preferences to set it up

    }
    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(MainActivity.this, UpdateEntry.class);
        intent.putExtra("itemData", itemId);
        startActivity(intent);
    }
}

