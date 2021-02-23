package com.helloworld.goodpoint.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.helloworld.goodpoint.R;

import java.util.ArrayList;
import java.util.List;

import fragments.PageFragment1;
import fragments.PageFragment2;
import fragments.PageFragment3;

public class WelcomeActivity extends AppCompatActivity {
    private ViewPager pager;
    private SlideAdapter slideAdapter;
    private LinearLayout Dots_layout;
    private ImageView[] dots;
    private int[] layouts = {R.layout.slider_page1, R.layout.slider_page2, R.layout.slider_page3};
    private TextView Next, Skip, Start;
    private ImageView arrow;
    private PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
        setContentView(R.layout.activity_welcome);
        List<Fragment> list = new ArrayList<>();
        list.add(new PageFragment1());
        list.add(new PageFragment2());
        list.add(new PageFragment3());

        pager = findViewById(R.id.viewpager);
        slideAdapter = new SlideAdapter(getSupportFragmentManager(), list);
        pager.setAdapter(slideAdapter);

        Dots_layout = (LinearLayout) findViewById(R.id.dotsLayout);
        Skip = (TextView) findViewById(R.id.Skip);
        Next = (TextView) findViewById(R.id.Nextt);
        createDots(0);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                createDots(position);
                    // changing the next button text 'NEXT' / 'START '
                    if (position == layouts.length - 1) {
                        // last page. make button text to START
                        Next.setText(getString(R.string.start));
                        Skip.setVisibility(View.GONE);
                    } else {
                        // still pages are left
                        Next.setText(getString(R.string.next));
                        Skip.setVisibility(View.VISIBLE);
                    }
                }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    pager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });
        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });
    }
    private void launchHomeScreen (){
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }

    private int getItem(int i) {
        return pager.getCurrentItem() + i;
    }
    private void createDots(int current_position) {
        if (Dots_layout != null)
            Dots_layout.removeAllViews();
        dots = new ImageView[layouts.length];
        for (int i = 0; i < layouts.length; i++) {
            dots[i] = new ImageView(this);
            if (i == current_position) {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dot));
            } else {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.default_dot));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 0);
            Dots_layout.addView(dots[i], params);

        }
    }
}