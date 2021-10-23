package com.helloworld.goodpoint.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.helloworld.goodpoint.R;
import com.helloworld.goodpoint.pojo.LostItem;
import com.helloworld.goodpoint.pojo.Token;
import com.helloworld.goodpoint.pojo.User;
import com.helloworld.goodpoint.retrofit.ApiClient;
import com.helloworld.goodpoint.retrofit.ApiInterface;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    TextView name_above,email_above,name,email,phone,city,date,losts,founds;
    CircleImageView pic;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        name_above = v.findViewById(R.id.above_name);
        email_above = v.findViewById(R.id.above_mail);
        name = v.findViewById(R.id.username_p);
        email = v.findViewById(R.id.e_mail);
        phone = v.findViewById(R.id.phone_p);
        city = v.findViewById(R.id.city_p);
        date = v.findViewById(R.id.birth_date);
        pic = v.findViewById(R.id.profile_pic);
        losts = v.findViewById(R.id.lost_no);
        founds = v.findViewById(R.id.found_no);


        name_above.setText(User.getUser().getUsername());
        email_above.setText(User.getUser().getEmail());
        losts.setText(User.getUser().getLosts().size() + "");
        founds.setText(User.getUser().getFounds().size() + "");
        name.setText(User.getUser().getUsername());
        email.setText(User.getUser().getEmail());
        phone.setText(User.getUser().getPhone());
        city.setText(User.getUser().getCity());
        date.setText(User.getUser().getBirthdate());
        if(User.getUser().getProfile_bitmap() != null)
            pic.setImageBitmap(User.getUser().getProfile_bitmap());


        return v;
    }
}