package com.helloworld.goodpoint.ui.select_multiple_faces;


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

public class SubItemListAdapter extends RecyclerView.Adapter<SubItemListAdapter.SubItemViewHolder> {

    private List<SubItemList> subItemList;
    private int lastSelectedPosition = -1;
    private Context context;
    private AdapterView.OnItemClickListener onItemClickListener;

    SubItemListAdapter(List<SubItemList> subItemList, Context ctx) {
        this.subItemList = subItemList;
        context = ctx;
    }

    @NonNull
    @Override
    public SubItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_sub_list_items, viewGroup, false);

        return new SubItemViewHolder(view,this);
    }

    @Override
    public void onBindViewHolder(@NonNull SubItemViewHolder subItemViewHolder, int i) {
        com.helloworld.goodpoint.ui.select_multiple_faces.SubItemList subItem = subItemList.get(i);
        subItemViewHolder.tvItemImage.setImageBitmap(subItem.getSubItemImage());
        try {
            subItemViewHolder.bindData(subItem, i);
            subItem.setPos(i);
        } catch (Exception e) {
            e.printStackTrace();

        }
        //Toast.makeText(cxt, "size="+item.getSubItemList().size(),Toast.LENGTH_LONG).show();

    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void onItemHolderClick(SubItemViewHolder holder) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), holder.getItemId());
    }

    @Override
    public int getItemCount() {
        return subItemList.size();
    }


    class SubItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SubItemListAdapter mAdapter;
        private ImageView tvItemImage;
        private RadioButton rb;

        public SubItemViewHolder(View itemView, final SubItemListAdapter mAdapter) {
            super(itemView);
            this.mAdapter=mAdapter;
            tvItemImage = itemView.findViewById(R.id.img_view);
            rb = itemView.findViewById(R.id.radioButton);
            itemView.setOnClickListener(this);
            rb.setOnClickListener( this);
        }

        public void bindData(SubItemList list, int position) {
            rb.setChecked(position==lastSelectedPosition);

        }
        public void onClick(View v){
            lastSelectedPosition = getAdapterPosition();
            notifyItemRangeChanged(0,subItemList.size());
            mAdapter.onItemHolderClick(SubItemViewHolder.this);
        }
    }
}


