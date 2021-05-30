package com.project_ikni.leprecone.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project_ikni.leprecone.DB.DBHelp;
import com.project_ikni.leprecone.R;
import com.project_ikni.leprecone.adapters.MyAdapter;
import com.project_ikni.leprecone.model.Information;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    Cursor cursor1, cursor;
    ImageView img;
    DBHelp dbHelp;
    ImageView imageView;
    TextView name, about;
    Bitmap bitmap;
    RecyclerView rv;
    MyAdapter adapter;
    ArrayList<Information> players = new ArrayList<>();

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        name = v.findViewById(R.id.name);
        about = v.findViewById(R.id.about);
        imageView = v.findViewById(R.id.img);
        dbHelp = new DBHelp(getActivity());
        rv = v.findViewById(R.id.mRecycler);

        //SET PROPS
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        rv.setItemAnimator(new DefaultItemAnimator());

        //ADAPTER
        adapter = new MyAdapter(getContext(), players);

        //RETRIEVE
        retrieve();
        refresh();
        return v;
    }

    private void retrieve() {
        players.clear();

        DBHelp db = new DBHelp(getActivity());


        //RETRIEVE
        Cursor c = db.getLastMarkers();

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

    public void refresh() {
        cursor1 = dbHelp.queueAll();
        cursor1.requery();

        if (cursor1.getCount() == 0) {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] imageInByte = baos.toByteArray();
            dbHelp.insert(1, "Имя", "О себе", imageInByte);
        } else {
            while (cursor1.moveToNext()) {
                name.setText(cursor1.getString(0));
                about.setText(cursor1.getString(1));
                byte[] image = cursor1.getBlob(2);
                bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                imageView.setImageBitmap(bitmap);
            }
//            stopManagingCursor(cursor1);

        }
//        mySQLiteAdapter.openToRead();
//        cursor = mySQLiteAdapter.queueAll();
//        startManagingCursor(cursor);
//        String[] from = new String[]{SQLiteAdapter.KEY_CONTENT};
//        int[] to = new int[]{R.id.text};
//        SimpleCursorAdapter cursorAdapter =
//                new SimpleCursorAdapter(this, R.layout.row_, cursor, from, to);
//
//        main_lv.setAdapter(cursorAdapter);
//        stopManagingCursor(cursor);
//        mySQLiteAdapter.close();
    }


    // TODO: Rename method, update argument and hook method into UI event
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 888 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {

                InputStream inputStream = getActivity().getApplicationContext().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
