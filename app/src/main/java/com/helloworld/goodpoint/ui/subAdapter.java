package com.helloworld.goodpoint.ui;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.helloworld.goodpoint.R;

import java.util.List;

public class subAdapter extends RecyclerView.Adapter<subAdapter.subViewHolder> {

    private List<sub> subItemList;
    private int lastSelectedPosition = -1;
    private Context context;
    private AdapterView.OnItemClickListener onItemClickListener;

    subAdapter(List<sub> subItemList, Context ctx) {
        this.subItemList = subItemList;
        context = ctx;
    }

    @NonNull
    @Override
    public subViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rr, viewGroup, false);

        return new subViewHolder(view,this);
    }

    @Override
    public void onBindViewHolder(@NonNull subViewHolder subItemViewHolder, int i) {
     sub subItem = subItemList.get(i);
        subItemViewHolder.tvItemImage.setImageBitmap(subItem.getSubItemImage());

        //Toast.makeText(cxt, "size="+item.getSubItemList().size(),Toast.LENGTH_LONG).show();

    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void onItemHolderClick(subViewHolder holder) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), holder.getItemId());
    }

    @Override
    public int getItemCount() {
        return subItemList.size();
    }


    class subViewHolder extends RecyclerView.ViewHolder {
        private subAdapter mAdapter;
        private ImageView tvItemImage;
        private RadioButton rb;

        public subViewHolder(View itemView, final subAdapter mAdapter) {
            super(itemView);
            this.mAdapter=mAdapter;
            tvItemImage = itemView.findViewById(R.id.img1_view);

        }


    }
}


