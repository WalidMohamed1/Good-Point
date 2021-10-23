package com.helloworld.goodpoint.ui.select_multiple_faces;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.helloworld.goodpoint.R;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<com.helloworld.goodpoint.ui.select_multiple_faces.ItemList> itemList;
    private Context cxt;
    ItemListAdapter(List<com.helloworld.goodpoint.ui.select_multiple_faces.ItemList> itemList,Context cxt) {
        this.itemList = itemList;
        this.cxt=cxt;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_of_images, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        com.helloworld.goodpoint.ui.select_multiple_faces.ItemList item = itemList.get(i);
        itemViewHolder.tvItemImage.setImageBitmap(item.getItemImage());

        // Create layout manager with initial prefetch item count
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                itemViewHolder.rvSubItem.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(item.getSubItemList().size());

        // Create sub item view adapter
        com.helloworld.goodpoint.ui.select_multiple_faces.SubItemListAdapter subItemAdapter = new com.helloworld.goodpoint.ui.select_multiple_faces.SubItemListAdapter(item.getSubItemList(),cxt);


        itemViewHolder.rvSubItem.setLayoutManager(layoutManager);
        itemViewHolder.rvSubItem.setAdapter(subItemAdapter);
        itemViewHolder.rvSubItem.setRecycledViewPool(viewPool);


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView tvItemImage;
        private RecyclerView rvSubItem;

        ItemViewHolder(View itemView) {
            super(itemView);
            tvItemImage = itemView.findViewById(R.id.ItemImage);
            rvSubItem = itemView.findViewById(R.id.recycler_view_sub_items);

        }
    }
}
