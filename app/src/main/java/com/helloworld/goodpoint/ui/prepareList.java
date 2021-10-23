package com.helloworld.goodpoint.ui;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.helloworld.goodpoint.R;

import java.util.ArrayList;
import java.util.List;

public class prepareList {
    public List<String> prepareList(Context con) {
        List<String> list = new ArrayList<>();
        list.add(con.getString(R.string.Cairo));
        list.add(con.getString(R.string.SharmElSheikh));
        list.add(con.getString(R.string.Alexandria));
        list.add(con.getString(R.string.ShubraElKheima));
        list.add(con.getString(R.string.ElBeheira));
        list.add(con.getString(R.string.Giza));
        list.add(con.getString(R.string.PortSaid));
        list.add(con.getString(R.string.Suez));
        list.add(con.getString(R.string.ElMahallaElKubra));
        list.add(con.getString(R.string.Luxor));
        list.add(con.getString(R.string.Mansoura));
        list.add(con.getString(R.string.Tanta));
        list.add(con.getString(R.string.Asyut));
        list.add(con.getString(R.string.Ismailia));
        list.add(con.getString(R.string.Faiyum));
        list.add(con.getString(R.string.Zagazig));
        list.add(con.getString(R.string.Damietta));
        list.add(con.getString(R.string.Aswan));
        list.add(con.getString(R.string.Minya));
        list.add(con.getString(R.string.BeniSuef));
        list.add(con.getString(R.string.Hurghada));
        list.add(con.getString(R.string.Qena));
        list.add(con.getString(R.string.Sohag));
        list.add(con.getString(R.string.ShibinElKom));
        list.add(con.getString(R.string.Banha));
        list.add(con.getString(R.string.Arish));
        return  list;
    }
    public List<String> prepareListColor(Context con) {
        List<String>list = new ArrayList<>();
        list.add(con.getString(R.string.Blue));
        list.add(con.getString(R.string.Red));
        list.add(con.getString(R.string.Yellow));
        list.add(con.getString(R.string.Orange));
        list.add(con.getString(R.string.Green));
        list.add(con.getString(R.string.Violet));
        list.add(con.getString(R.string.Brown));
        list.add(con.getString(R.string.Magenta));
        list.add(con.getString(R.string.Tan));
        list.add(con.getString(R.string.Cyan));
        list.add(con.getString(R.string.Olive));
        list.add(con.getString(R.string.Pink));
        list.add(con.getString(R.string.Black));
        list.add(con.getString(R.string.White));
        list.add(con.getString(R.string.Gray));
        list.add(con.getString(R.string.Purple));
        return list;
    }
}
