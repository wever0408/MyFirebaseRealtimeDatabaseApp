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

//import android.support.v4.app.Fragment;

public class ViewClassifiedFragment extends Fragment {
    private static final String TAG = "ViewAdsFragment";
    private DatabaseReference databaseReference;
    private RecyclerView adsRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ad_view_layout,
                container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        Button button = (Button) view.findViewById(R.id.view_adds_b);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClassifiedAds();
            }
        });

        adsRecyclerView = (RecyclerView) view.findViewById(R.id.ads_lst);

        LinearLayoutManager recyclerLayoutManager =
                new LinearLayoutManager(getActivity().getApplicationContext());
        adsRecyclerView.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(adsRecyclerView.getContext(),
                        recyclerLayoutManager.getOrientation());
        adsRecyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void getClassifiedAds() {
        String category = ((TextView) getActivity()
                .findViewById(R.id.category_v)).getText().toString();
        getClassifiedsFromDb(category);
    }

    private void getClassifiedsFromDb(final String category) {
        databaseReference.child("classified").orderByChild("category")
                .equalTo(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ClassifiedAd> adsList = new ArrayList<ClassifiedAd>();
                for (DataSnapshot adSnapshot: dataSnapshot.getChildren()) {
                    adsList.add(adSnapshot.getValue(ClassifiedAd.class));
                }
                Log.d(TAG, "no of ads for search is "+adsList.size());
                AdsRecyclerView recyclerViewAdapter = new
                        AdsRecyclerView(adsList, getActivity());
                adsRecyclerView.setAdapter(recyclerViewAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Error trying to get classified ads for " +category+
                        " "+databaseError);
                Toast.makeText(getActivity(),
                        "Error trying to get classified ads for " +category,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
