package com.example.wever.myfirebaserealtimedatabaseapp;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MoviesRecyclerView extends RecyclerView.Adapter<MoviesRecyclerView.ViewHolder>{
    private List<Movie> movieList;
    private Context context;

    public MoviesRecyclerView(List<Movie> list, Context ctx) {
        movieList = list;
        context = ctx;
    }
    @Override
    public int getItemCount() {
        return movieList.size();
    }

    @Override
    public MoviesRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item_layout, parent, false);

        MoviesRecyclerView.ViewHolder viewHolder =
                new MoviesRecyclerView.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MoviesRecyclerView.ViewHolder holder, int position) {
        final int itemPos = position;
        final Movie movie = movieList.get(position);
        holder.title.setText(movie.getTitle());
        holder.genre.setText(movie.getGenre());
        holder.openingDay.setText(movie.getOpeningDay());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editClassifiedAd(movie.getAdId());
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteClassifiedAd(movie.getAdId(), itemPos);
            }
        });
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView genre;
        public TextView openingDay;
        public Button edit;
        public Button delete;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title_i);
            genre = (TextView) view.findViewById(R.id.name_i);
            openingDay = (TextView) view.findViewById(R.id.phone_i);
            edit = view.findViewById(R.id.edit_ad_b);
            delete = view.findViewById(R.id.delete_ad_b);
        }
    }

    //Press Edit button
    private void editClassifiedAd(String adId){
        FragmentManager fm = ((MainActivity)context).getFragmentManager();

        Bundle bundle=new Bundle();
        bundle.putString("adId", adId);

        AddMovieFragment addFragment = new AddMovieFragment();
        addFragment.setArguments(bundle); // Bundle -> putString("adId") -> Fragment.setArguments(bundle) -> getArguments.getString(key:"adId")

        fm.beginTransaction().replace(R.id.adds_frame, addFragment).commit();
    }

    private void deleteClassifiedAd(String adId, final int position){
        FirebaseDatabase.getInstance().getReference()
                .child("Movies").child(adId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //remove item from list alos and refresh recyclerView
                            movieList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, movieList.size());

                            Log.d("Delete Ad", "Classified has been deleted");
                            Toast.makeText(context,
                                    "Classified has been deleted",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Delete Ad", "Classified couldn't be deleted");
                            Toast.makeText(context,
                                    "Classified could not be deleted",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
