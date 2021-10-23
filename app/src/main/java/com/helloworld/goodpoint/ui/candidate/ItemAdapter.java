package com.helloworld.goodpoint.ui.candidate;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.helloworld.goodpoint.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<Item> itemlist;
    private Context cxt;
    ItemAdapter(List<com.helloworld.goodpoint.ui.candidate.Item> item,Context cxt) {
        this.itemlist= item;
        this.cxt=cxt;
    }

    @NonNull
    @Override
    public ItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_candidate_page, viewGroup, false);
        return new ItemAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ItemViewHolder itemViewHolder, int i) {
        com.helloworld.goodpoint.ui.candidate.Item item = itemlist.get(i);
        itemViewHolder.tvItem.setText(item.getItemTitle());

        // Create layout manager with initial prefetch item count
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                itemViewHolder.rvSubItem.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(item.getSubItem().size());

        // Create sub item view adapter
        com.helloworld.goodpoint.ui.candidate.SubItemAdapter subItemAdapter = new com.helloworld.goodpoint.ui.candidate.SubItemAdapter(item.getSubItem(),cxt);


        itemViewHolder.rvSubItem.setLayoutManager(layoutManager);
        itemViewHolder.rvSubItem.setAdapter(subItemAdapter);
        itemViewHolder.rvSubItem.setRecycledViewPool(viewPool);


    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView tvItem;
        private RecyclerView rvSubItem;

        ItemViewHolder(View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tv_item_title1);
            rvSubItem = itemView.findViewById(R.id.rv_sub_item1);

        }
    }
}

