package com.helloworld.goodpoint.ui.candidate;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.helloworld.goodpoint.R;

import java.util.List;

public class SubItemAdapter extends RecyclerView.Adapter<SubItemAdapter.SubItemViewHolder> {


    private List<SubItem> subItemList;
    private int lastSelectedPosition = -1;
    private Context context;
    private AdapterView.OnItemClickListener onItemClickListener;

    SubItemAdapter(List<SubItem> subItem, Context ctx) {
        this.subItemList = subItem;
        context = ctx;
    }

    @NonNull
    @Override
    public SubItemAdapter.SubItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_sub_item, viewGroup, false);

        return new SubItemAdapter.SubItemViewHolder(view,this);
    }

    @Override
    public void onBindViewHolder(@NonNull SubItemAdapter.SubItemViewHolder subItemViewHolder, int i) {
        com.helloworld.goodpoint.ui.candidate.SubItem subItem = subItemList.get(i);
        subItemViewHolder.tvItemType.setText(subItem.getSubItemTitle());
        subItemViewHolder.tvItemDes.setText(subItem.getSubItemDes());
        subItemViewHolder.tvItemPercent.setText(subItem.getPersent());

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

    public void onItemHolderClick(SubItemAdapter.SubItemViewHolder holder) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), holder.getItemId());
    }

    @Override
    public int getItemCount() {
        return subItemList.size();
    }


    class SubItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SubItemAdapter mAdapter;
        private TextView tvItemType;
        private TextView tvItemDes;
        private TextView tvItemPercent;
        private RadioButton rb;

        public SubItemViewHolder(View itemView, final SubItemAdapter mAdapter) {
            super(itemView);
            this.mAdapter=mAdapter;
            tvItemType = itemView.findViewById(R.id.tv_sub_item_title);
            tvItemDes=itemView.findViewById(R.id.tv_sub_item_des);
            tvItemPercent=itemView.findViewById(R.id.tv_sub_item_percent);
            rb = itemView.findViewById(R.id.radioButtonn);
            itemView.setOnClickListener(this);
            rb.setOnClickListener( this);
        }

        public void bindData(SubItem list, int position) {
            rb.setChecked(position==lastSelectedPosition);

        }
        public void onClick(View v){
            lastSelectedPosition = getAdapterPosition();
            notifyItemRangeChanged(0,subItemList.size());
            mAdapter.onItemHolderClick(SubItemAdapter.SubItemViewHolder.this);
        }
    }
}


