package com.example.wever.myfirebaserealtimedatabaseapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
//import android.widget.Toolbar;
import android.app.FragmentTransaction;
import android.app.FragmentManager;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {


        private FragmentManager fm;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.classifieds_layout);

            Toolbar tb = findViewById(R.id.toolbar);
            setSupportActionBar(tb);
            tb.setSubtitle("Realtime Database");

            fm = getFragmentManager();
            addClassifiedAdFrgmt();
        }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.ads_menu, menu);
            MenuInflater inflater1 = getMenuInflater();
            inflater1.inflate(R.menu.add_type_menu,menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.add_cinema_m:
                    addClassifiedAdFrgmt();
                    return true;
                case R.id.add_movie_m:
                    addMovieFrgmt();
                    return true;
                case R.id.view_cinema_m:
                    viewClassifiedAdFrgmt();
                    return true;
                case R.id.view_movies_m:
                    viewMoviesAdFrgmt();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        public void addClassifiedAdFrgmt(){
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.adds_frame, new AddClassifiedFragment());

            ft.commit();
        }
    public void addMovieFrgmt(){
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.adds_frame, new AddMovieFragment());

        ft.commit();
    }
        public void viewClassifiedAdFrgmt(){
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.adds_frame, new ViewClassifiedFragment());
            ft.commit();
        }
    public void viewMoviesAdFrgmt(){
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.adds_frame, new ViewMoviesFragment());
        ft.commit();
    }
}