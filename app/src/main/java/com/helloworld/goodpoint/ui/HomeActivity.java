package com.helloworld.goodpoint.ui;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.helloworld.goodpoint.R;
import com.helloworld.goodpoint.ui.lostFoundObject.FoundObjectActivity;
import com.helloworld.goodpoint.ui.lostFoundObject.LostObjectDetailsActivity;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    AlertDialog.Builder dialog;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;
    Fragment fhome, fmap, fmatch, fprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        setToolBarAndDrawer();
        setBottomNavigator();

        if(savedInstanceState == null) {
            //To make first fragment is home when opening the app
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fhome).commit();
        }
    }

    private void setBottomNavigator() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        //To Disable item under Fab
        Menu menuNav=bottomNavigationView.getMenu();
        MenuItem nav_item2 = menuNav.findItem(R.id.placeholder);
        nav_item2.setEnabled(false);

        bottomNavigationView.setBackgroundColor(0); //To hide the color of nav view
        bottomNavigationView.setOnNavigationItemSelectedListener(navListner);
    }

    private void setToolBarAndDrawer() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_drawer_open,R.string.nav_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(android.R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void init() {
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nv);
        navigationView.bringToFront();
        fhome = new HomeFragment();
        fmap = new FoundMapFragment();
        fmatch = new MatchFragment();
        fprofile = new ProfileFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        menu.getItem(0).getIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.notification)
            startActivity(new Intent(this,NotificationActivity.class));
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.drawer_share:

                break;
            case R.id.drawer_rate:

                break;
            case R.id.drawer_feedback:

                break;
            case R.id.drawer_about_us:

                break;
            case R.id.drawer_setting:

                break;
            case R.id.drawer_logout:
                dialog = createDialog("Logout",R.drawable.ic_baseline_exit_to_app_24);
                dialog.create().show();
                break;
            default:
                return false;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private AlertDialog.Builder createDialog(String title, int icon) {
        return new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage("Are you sure?")
                .setIcon(icon)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PrefManager prefManager = new PrefManager(getApplicationContext());
                        prefManager.setLogout();
                        startActivity(new Intent(HomeActivity.this,SigninActivity.class));
                        finish();
                    }
                }).setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setCancelable(false);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    public void showPopup(View v) { //Fab Action

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(HomeActivity.this, R.style.BottomSheetTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.bottom_sheet_dialog, (LinearLayout)findViewById(R.id.bottom_sheet));

        bottomSheetView.findViewById(R.id.hide_sheet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetView.findViewById(R.id.ilost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, LostObjectDetailsActivity.class));
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetView.findViewById(R.id.ifound).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, FoundObjectActivity.class));
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListner =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = getSupportFragmentManager().getFragments().get(0);
                    switch (item.getItemId()) {
                        case R.id.miHome:
                            if(!(selectedFragment instanceof HomeFragment))
                                selectedFragment = fhome;
                            break;
                        case R.id.miMatch:
                            if(!(selectedFragment instanceof MatchFragment))
                                selectedFragment = fmatch;
                            break;
                        case R.id.miProfile:
                            if(!(selectedFragment instanceof ProfileFragment))
                                selectedFragment = fprofile;
                            break;
                        case R.id.miLocation:
                            if(!(selectedFragment instanceof FoundMapFragment))
                                selectedFragment = fmap;
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };


}