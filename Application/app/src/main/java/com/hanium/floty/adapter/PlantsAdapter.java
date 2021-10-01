package com.example.srh6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PlantsAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<FriendsItem> data; //Item 목록을 담을 배열
    private int layout;

    public PlantsAdapter(Context context, int layout, ArrayList<FriendsItem> data) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout = layout;
    }

    @Override
    public int getCount() { //리스트 안 Item의 개수를 센다.
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position).getPlantkor();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }
        FriendsItem friendsItem = data.get(position);


        ImageView profile = (ImageView) convertView.findViewById(R.id.profile);
        profile.setImageResource(friendsItem.getImgurl());


        TextView plantkor = (TextView) convertView.findViewById(R.id.plantkor);
        plantkor.setText(friendsItem.getPlantkor());


        TextView planteng = (TextView) convertView.findViewById(R.id.planteng);
        planteng.setText(friendsItem.getPlanteng());


        return convertView;
    }
}