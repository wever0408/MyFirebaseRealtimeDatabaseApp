package com.example.wever.myfirebaserealtimedatabaseapp;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewMoviesFragment extends Fragment {
    private static final String TAG = "ViewMoviesFragment";
    private DatabaseReference databaseReference;
    private RecyclerView movieRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.movie_view_layout,
                container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        Button button = (Button) view.findViewById(R.id.view_movies_b);
        getDefaultClassifiedsFromDb();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClassifiedAds();
            }
        });

        movieRecyclerView = (RecyclerView) view.findViewById(R.id.movies_lst);

        LinearLayoutManager recyclerLayoutManager =
                new LinearLayoutManager(getActivity().getApplicationContext());
        movieRecyclerView.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(movieRecyclerView.getContext(),
                        recyclerLayoutManager.getOrientation());
        movieRecyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void getClassifiedAds() {
        String title = ((TextView) getActivity()
                .findViewById(R.id.title_v)).getText().toString();
        getClassifiedsFromDb(title);
    }

    private void getClassifiedsFromDb(final String title) {
        databaseReference.child("Movies").orderByChild("title")
                .equalTo(title).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Movie> movieList = new ArrayList<Movie>();
                for (DataSnapshot adSnapshot: dataSnapshot.getChildren()) {
                    movieList.add(adSnapshot.getValue(Movie.class));
                }
                Log.d(TAG, "no of movies for search is "+movieList.size());
                MoviesRecyclerView recyclerViewAdapter = new
                        MoviesRecyclerView(movieList, getActivity());
                movieRecyclerView.setAdapter(recyclerViewAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Error trying to get classified movies for " +title+
                        " "+databaseError);
                Toast.makeText(getActivity(),
                        "Error trying to get classified movies for " +title,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDefaultClassifiedsFromDb() {
        databaseReference.child("Movies").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Movie> movieList = new ArrayList<Movie>();
                for (DataSnapshot adSnapshot: dataSnapshot.getChildren()) {
                    movieList.add(adSnapshot.getValue(Movie.class));
                }
                Log.d(TAG, "no of movies for search is "+movieList.size());
                MoviesRecyclerView recyclerViewAdapter = new
                        MoviesRecyclerView(movieList, getActivity());
                movieRecyclerView.setAdapter(recyclerViewAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Error trying to get classified movies for " +
                        " "+databaseError);
                Toast.makeText(getActivity(),
                        "Error trying to get classified movies for " ,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
