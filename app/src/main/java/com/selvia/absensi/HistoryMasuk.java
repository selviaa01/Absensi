package com.selvia.absensi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistoryMasuk extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<String> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historymasuk);

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Buat contoh data item
        itemList = new ArrayList<>();
        itemList.add("Item 1");
        itemList.add("Item 2");
        itemList.add("Item 3");

        // Inisialisasi adapter dan menghubungkannya ke RecyclerView
        RecyclerView.Adapter<CustomViewHolder> adapter = new RecyclerView.Adapter<CustomViewHolder>() {

            @Override
            public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Inflate layout for each item view and create a ViewHolder
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.historymasuk, parent, false);
                return new CustomViewHolder(view);
            }

            @Override
            public void onBindViewHolder(CustomViewHolder holder, int position) {
                // Bind data to the item view
                // Misalnya, set teks pada TextView
                holder.bindData(itemList.get(position));
            }

            @Override
            public int getItemCount() {
                return itemList.size();
            }
        };

        recyclerView.setAdapter(adapter);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView itemText;

        public CustomViewHolder(View itemView) {
            super(itemView);
            itemText = itemView.findViewById(R.id.itemText);
        }

        public void bindData(String item) {
            itemText.setText(item);
        }
    }
}
