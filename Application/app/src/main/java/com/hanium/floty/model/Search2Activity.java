package com.hanium.floty.model;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.hanium.floty.R;

import java.util.ArrayList;
import java.util.List;

public class Search2Activity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private List<ImagePlant> imageplants = new ArrayList<>();
    private List<String> uidList = new ArrayList<>();
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerciew);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        com.hanium.floty.fragment.SearchAdapter sEarchAdapter = new com.hanium.floty.fragment.searchAdapter();
        recyclerView.setAdapter(sEarchAdapter);

        database.getReference().child("plant").addValueEventListener(new vlueEventListener){
            @Override
            public void
        }
    }

}

    class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board,parent,false);

        return new com.hanium.floty.fragment.SearchAdapter.PlantViewHolder(view);
        }
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        }

        @Override
        public int getItemCount{
            return imageplants.size();

        }
        private class PlantViewHolder extends RecyclerView.ViewHolder{
            private ImageView imageView;
            private TextView plantkor;
            private TextView planteng;

        public PlantViewHolder(View view){
            super(view);
            imageView=(ImageView) view.findViewById(R.id.imgurl);
            plantkor=(TextView) view.findViewById(R.id.plantkor);
            planteng=(TextView) view.findViewById(R.id.planteng);
        }
    }
}
}
