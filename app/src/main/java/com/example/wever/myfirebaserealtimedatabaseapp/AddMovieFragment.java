package com.example.wever.myfirebaserealtimedatabaseapp;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class AddMovieFragment extends Fragment {
    private static final String TAG = "AddMovieFragment";

    private DatabaseReference dbRef;
    private int nextClassifiedID;
    private boolean isEdit;
    private String adId;
    private Button button;
    private TextView headTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.movie_add_layout,
                container, false);

        button = (Button) view.findViewById(R.id.movie_add);
        headTxt = view.findViewById(R.id.add_head_tv);

        dbRef = FirebaseDatabase.getInstance().getReference();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEdit) {
                    addEvent();
                } else {
                    updateEvent();
                }

            }
        });

        //add or update depending on existence of adId in arguments
        if (getArguments() != null) {
            adId = getArguments().getString("adId");
        }
        if (adId != null) {
            populateUpdateAd(); //kiem tra neu adId != null thi se la layout edit ad
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void addEvent() {
        Movie movie = createClassifiedAdObj();
        addClassifiedToDB(movie);
    }

    public void updateEvent() {
        Movie movie = createClassifiedAdObj();
        updateClassifiedToDB(movie);
    }

    private void addClassifiedToDB(final Movie movie) {
        final DatabaseReference idDatabaseRef = FirebaseDatabase.getInstance()
                .getReference("ClassifiedIDs").child("movieID");

        idDatabaseRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                //create id node if it doesn't eixst
                //this code runs only once
                if (mutableData.getValue(int.class) == null) {
                    idDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //set initial value
                            if(dataSnapshot != null && dataSnapshot.getValue() == null){
                                idDatabaseRef.setValue(1);
                                Log.d(TAG, "Initial id is set");
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    Log.d(TAG, "Classified id null so " +
                            " transaction aborted, " );

                    return Transaction.abort();
                }

                nextClassifiedID = mutableData.getValue(int.class);
                mutableData.setValue(nextClassifiedID + 1);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean state,
                                   DataSnapshot dataSnapshot) {
                if (state) {
                    Log.d(TAG, "Classified id retrieved ");
                    addClassified(movie, ""+nextClassifiedID);
                } else {
                    Log.d(TAG, "Classified id retrieval unsuccessful " + databaseError);
                    Toast.makeText(getActivity(),
                            "There is a problem, please submit movie post again",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //Screen after adding or editing
    private void addClassified(Movie movie, String cAdId) {
        movie.setAdId(cAdId);
        dbRef.child("Movies").child(cAdId)
                .setValue(movie)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if(isEdit){
                                addClassifieds();
                            }else{
                                restUi();
                            }
                            Log.d(TAG, "Classified has been added to db");
                            Toast.makeText(getActivity(),
                                    "Classified has been posted",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "Classified couldn't be added to db");
                            Toast.makeText(getActivity(),
                                    "Classified could not be added",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void populateUpdateAd() {
        headTxt.setText("Edit Movie");
        button.setText("Edit Movie");
        isEdit = true;

        dbRef.child("Movies").child(adId).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Movie movie = dataSnapshot.getValue(Movie.class);
                        displayAdForUpdate(movie);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "Error trying to get classified movie for update " +
                                ""+databaseError);
                        Toast.makeText(getActivity(),
                                "Please try classified edit action again",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private void displayAdForUpdate(Movie movie){// getActivity().findViewById
        ((EditText) getActivity()
                .findViewById(R.id.title_a)).setText(movie.getTitle());
        ((EditText) getActivity()
                .findViewById(R.id.genre_a)).setText(movie.getGenre());
        ((EditText) getActivity()
                .findViewById(R.id.desc_a)).setText(movie.getDescription());
        ((EditText) getActivity()
                .findViewById(R.id.director_a)).setText(movie.getDirector());
        ((EditText) getActivity()
                .findViewById(R.id.cast_a)).setText(movie.getCast());
        ((EditText) getActivity()
                .findViewById(R.id.openingDay_a)).setText(movie.getOpeningDay());
    }
    private void updateClassifiedToDB(Movie movie) {
        addClassified(movie, adId);
    }

    private Movie createClassifiedAdObj() {
        final Movie movie = new Movie();
        movie.setTitle(((EditText) getActivity()
                .findViewById(R.id.title_a)).getText().toString());
        movie.setGenre(((EditText) getActivity()
                .findViewById(R.id.genre_a)).getText().toString());
        movie.setDescription(((EditText) getActivity()
                .findViewById(R.id.desc_a)).getText().toString());
        movie.setDirector(((EditText) getActivity()
                .findViewById(R.id.director_a)).getText().toString());
        movie.setCast(((EditText) getActivity()
                .findViewById(R.id.cast_a)).getText().toString());
        movie.setOpeningDay(((EditText) getActivity()
                .findViewById(R.id.openingDay_a)).getText().toString());
        return movie;
    }

    private void restUi() {
        ((EditText) getActivity()
                .findViewById(R.id.title_a)).setText("");
        ((EditText) getActivity()
                .findViewById(R.id.genre_a)).setText("");
        ((EditText) getActivity()
                .findViewById(R.id.desc_a)).setText("");
        ((EditText) getActivity()
                .findViewById(R.id.director_a)).setText("");
        ((EditText) getActivity()
                .findViewById(R.id.cast_a)).setText("");
        ((EditText) getActivity()
                .findViewById(R.id.openingDay_a)).setText("");
    }

    private void addClassifieds() {
        Intent i = new Intent();
        i.setClass(getActivity(), MainActivity.class);
        startActivity(i);
    }

}
