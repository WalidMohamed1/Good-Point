package com.helloworld.goodpoint.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.helloworld.goodpoint.R;
import com.helloworld.goodpoint.pojo.NotificationItem;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationListAdapter extends ArrayAdapter<NotificationItem> {

    Context context;
    List<NotificationItem> list;
    Locale locale = new Locale("en");

    public NotificationListAdapter(@NonNull Context context, int resource, @NonNull List<NotificationItem> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View notificationItem = convertView;
        ViewHolder viewHolder;
        final int revposition = list.size()-position-1;

        if(notificationItem == null){
            notificationItem = createItem(parent);
            viewHolder = new ViewHolder(notificationItem);
            notificationItem.setTag(viewHolder);
        }else
            viewHolder = (ViewHolder) notificationItem.getTag();

        if(list.get(revposition).isRead())
            viewHolder.getLayout().setBackgroundColor(Color.WHITE);
        else
            viewHolder.getLayout().setBackgroundColor(context.getResources().getColor(R.color.table_detail));

        setItemDetails(viewHolder,revposition);

        return notificationItem;
    }

    private void setItemDetails(@NotNull ViewHolder viewHolder, int revposition) {
        viewHolder.getTitle().setText(list.get(revposition).getTitle());
        Date date = list.get(revposition).getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm",locale);
        String date_time = dateFormat.format(date);
        viewHolder.getDate().setText(date_time);
        viewHolder.getDescription().setText(list.get(revposition).getDescription());
        if(list.get(revposition).getImage() != null)
            viewHolder.getImageView().setImageBitmap(list.get(revposition).getImage());
    }

    private View createItem(ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.notification_item, parent, false);
    }

    private class ViewHolder{
        private View convertView;
        private TextView title, date, description;
        private CircleImageView imageView;
        private LinearLayout layout;

        public ViewHolder(View view) {
            this.convertView = view;
        }

        public TextView getTitle() {
            if(title == null)
                title = convertView.findViewById(R.id.notification_title);
            return title;
        }

        public TextView getDate() {
            if(date == null)
                date = convertView.findViewById(R.id.notification_date);
            return date;
        }

        public TextView getDescription() {
            if(description == null)
                description = convertView.findViewById(R.id.notification_description);
            return description;
        }

        public CircleImageView getImageView() {
            if(imageView == null)
                imageView = convertView.findViewById(R.id.notification_image);
            return imageView;
        }

        public LinearLayout getLayout() {
            if(layout == null)
                layout = convertView.findViewById(R.id.notification_layout);
            return layout;
        }
    }
}
