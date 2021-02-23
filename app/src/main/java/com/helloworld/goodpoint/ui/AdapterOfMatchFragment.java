package com.helloworld.goodpoint.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.helloworld.goodpoint.R;

public class AdapterOfMatchFragment  extends ArrayAdapter<String> {

    private Context context;
    private String Rtitle[];
    private String Rstatus[];
    private int Rimg[];

    //String ObjStat[] ={},ObjDetails;
    String status[]={"Has found" ,"Its owner has been found","Has found" ,"Its owner has been found",
            "Has found" ,"Its owner has been found","Has found" ,"Its owner has been found" }  ;

    public AdapterOfMatchFragment(@NonNull Context context, String[] Rstatus) {
        super(context, R.layout.row,Rstatus);
        this.context = context;
        //this.Rtitle = Rtitle;
        this.Rstatus = Rstatus;
        // this.Rimg = Rimg;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        viewHolder viewholder =null;

        if(r == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            r = layoutInflater.inflate(R.layout.row,null,true);
            viewholder = new viewHolder(r);
            r.setTag(viewholder);
        }
        else{

            viewholder = (viewHolder) r.getTag();
        }
        viewholder.imageView.setImageResource(R.drawable.ic_baseline_gallery_24);
        viewholder.stat.setText(status[position]);
        viewholder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, DetailsActivity.class));
            }
        });

        return r;
    }
    class viewHolder{
        private ImageView imageView;
        private TextView stat, details;
        viewHolder(View v){
            imageView =  v.findViewById(R.id.obj);
            stat =  v.findViewById(R.id.status);
            details = v.findViewById(R.id.details);
        }
    }
}
