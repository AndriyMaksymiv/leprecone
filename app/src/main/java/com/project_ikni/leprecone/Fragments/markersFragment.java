package com.project_ikni.leprecone.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project_ikni.leprecone.DB.DBHelp;
import com.project_ikni.leprecone.R;
import com.project_ikni.leprecone.adapters.MyAdapter;
import com.project_ikni.leprecone.addMarkerActivity;
import com.project_ikni.leprecone.model.Information;

import java.util.ArrayList;

public class markersFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    Cursor c;
    RecyclerView rv;
    MyAdapter adapter;
    ArrayList<Information> players = new ArrayList<>();
    FloatingActionButton myFab;

    public markersFragment() {
    }

    public static markersFragment newInstance(String param1, String param2) {
        markersFragment fragment = new markersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_markers, container, false);
        rv = v.findViewById(R.id.mRecycler);

        //SET PROPS
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setItemAnimator(new DefaultItemAnimator());
        //ADAPTER
        adapter = new MyAdapter(getContext(), players);
        myFab = v.findViewById(R.id.floatingActionButton);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), addMarkerActivity.class);

                getActivity().startActivity(i);
            }
        });
        //RETRIEVE
        retrieve();

        return v;
    }
    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        retrieve();
    }

    private void retrieve() {
        players.clear();

        DBHelp db = new DBHelp(getActivity());


        //RETRIEVE
        c = db.getAllMarkers();

        //LOOP AND ADD TO ARRAYLIST
        while (c.moveToNext()) {
            int id = c.getInt(0);
            String name = c.getString(1);
            String pos = c.getString(2);
            byte[] image = c.getBlob(4);

            Information p = new Information(id, name, pos, image);

            //ADD TO ARRAYLIS
            players.add(p);
        }

        //CHECK IF ARRAYLIST ISNT EMPTY
        if (!(players.size() < 1)) {
            rv.setAdapter(adapter);
        }

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
