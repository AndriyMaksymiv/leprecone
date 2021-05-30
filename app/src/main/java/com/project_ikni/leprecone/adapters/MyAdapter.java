package com.project_ikni.leprecone.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project_ikni.leprecone.GalleryActivity;
import com.project_ikni.leprecone.R;
import com.project_ikni.leprecone.model.Information;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {
    Context c;
    ArrayList<Information> players;

    public MyAdapter(Context c, ArrayList<Information> players) {
        this.c = c;
        this.players = players;
    }

    //INITIALIZE VIEWHODER
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //VIEW OBJ
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model, parent, false);
        //HOLDER
        MyHolder holder = new MyHolder(v);

        return holder;
    }

    //BIND VIEW TO DATA
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        Bitmap bitmap;

        bitmap = BitmapFactory.decodeByteArray(players.get(position).getImage(), 0,
                players.get(position).getImage().length);

        holder.img.setImageBitmap(bitmap);
        holder.nametxt.setText(players.get(position).getName());
        holder.posTxt.setText(players.get(position).getPosition());

        //CLICKED
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(c, GalleryActivity.class);
                intent.putExtra("image_url", players.get(pos).getImage());
                intent.putExtra("image_name", players.get(pos).getName());
                intent.putExtra("image_about", players.get(pos).getPosition());
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }
}