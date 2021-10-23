package com.helloworld.goodpoint.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.helloworld.goodpoint.R;
import com.helloworld.goodpoint.pojo.FoundItem;
import com.helloworld.goodpoint.pojo.FoundPerson;
import com.helloworld.goodpoint.pojo.LostItem;
import com.helloworld.goodpoint.pojo.LostObject;
import com.helloworld.goodpoint.pojo.LostPerson;
import com.helloworld.goodpoint.pojo.User;
import com.helloworld.goodpoint.retrofit.ApiClient;
import com.helloworld.goodpoint.retrofit.ApiInterface;
import com.helloworld.goodpoint.ui.lostFoundObject.FoundObjectActivity;
import com.helloworld.goodpoint.ui.lostFoundObject.LostObjectDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    AlertDialog.Builder dialog;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;
    TextView namenavigator;
    TextView mailnavigator;
    CircleImageView imgnavigator;
    Fragment selectedFragment;
    List<LostObject> listObj;
    List<LostItem> list1;
    FoundPerson list3;
    LostPerson list2;
    List<FoundItem> list;
    boolean isGetLostItems=false, isGetFoundItems=false, isGetLostPersons=false, isGetFoundPersons=false;

    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getHomeLosts();
        getHomeFounds();
        setContentView(R.layout.activity_home);
        /*refreshLayout = findViewById(R.id.swipe);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                selectedFragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                refreshLayout.setRefreshing(false);
            }
        });*/
        init();
        setToolBarAndDrawer();
        setBottomNavigator();

        /*if (savedInstanceState == null) {
            //To make first fragment is home when opening the app
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fhome).commit();

        }*/
        selectedFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

    }

    private void setBottomNavigator() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        //To Disable item under Fab
        Menu menuNav = bottomNavigationView.getMenu();
        MenuItem nav_item2 = menuNav.findItem(R.id.placeholder);
        nav_item2.setEnabled(false);

        bottomNavigationView.setBackgroundColor(0); //To hide the color of nav view
        bottomNavigationView.setOnNavigationItemSelectedListener(navListner);
    }

    private void setToolBarAndDrawer() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
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
        View view = navigationView.getHeaderView(0);
        namenavigator = (TextView) view.findViewById(R.id.namenav);
        mailnavigator = (TextView) view.findViewById(R.id.mailnav);
        imgnavigator = view.findViewById(R.id.circuler_profile_img);
        namenavigator.setText(User.getUser().getUsername());
        mailnavigator.setText(User.getUser().getEmail());
        if (User.getUser().getProfile_bitmap() != null)
            imgnavigator.setImageBitmap(User.getUser().getProfile_bitmap());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getHomeLosts();
        getHomeFounds();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        menu.getItem(0).getIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.notification){
            Intent intent = new Intent(this, NotificationActivity.class);
            intent.putExtra("ID",User.getUser().getId());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
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
                dialog = createDialog("Logout", R.drawable.ic_baseline_exit_to_app_24);
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
                        User.userLogout();
                        startActivity(new Intent(HomeActivity.this, SigninActivity.class));
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
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    public void showPopup(View v) { //Fab Action

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(HomeActivity.this, R.style.BottomSheetTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.bottom_sheet_dialog, (LinearLayout) findViewById(R.id.bottom_sheet));

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
                    selectedFragment = getSupportFragmentManager().getFragments().get(0);
                    switch (item.getItemId()) {
                        case R.id.miHome:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.miMatch:
                            selectedFragment = new MatchFragment();
                            break;
                        case R.id.miProfile:
                            selectedFragment = new ProfileFragment();
                            break;
                        case R.id.miLocation:
                            selectedFragment = new FoundMapFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };

    public void getHomeLosts() {
        List<Integer> losts = new ArrayList<>();
        losts = User.getUser().getLosts();
        ApiInterface apiInterface = ApiClient.getApiClient(new PrefManager(getApplicationContext()).getNGROKLink()).create(ApiInterface.class);

        GlobalVar.losts = new ArrayList<String>();
        if (losts != null) {
            for (int i = 0; i < losts.size(); i++) {
                Call<List<LostItem>> call2 = apiInterface.getLostItem(losts.get(i));
                call2.enqueue(new Callback<List<LostItem>>() {
                    @Override
                    public void onResponse(Call<List<LostItem>> call, Response<List<LostItem>> response) {
                        list1 = response.body();
                          if (response.body()!=null&&list1.size()!=0) {
                            String t = list1.get(0).getType() + " " + list1.get(0).getBrand() + "";
                            GlobalVar.losts.add(t);
                        }
                        isGetLostItems=true;
                          dataIsReady();
                    }

                    @Override
                    public void onFailure(Call<List<LostItem>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        isGetLostItems=true;
                        dataIsReady();
                    }
                });

                Call<LostPerson> call3 = apiInterface.getLostPerson(losts.get(i));
                call3.enqueue(new Callback<LostPerson>() {
                    @Override
                    public void onResponse(Call<LostPerson> call, Response<LostPerson> response) {
                        list2 = response.body();
                        Log.e("TAG", "onResponse: "+response.body());
                        if (response.body()!=null) {
                            String t = list2.getName() + " is Missing";
                            GlobalVar.losts.add(t);
                        }
                        isGetLostPersons=true;
                        dataIsReady();
                    }

                    @Override
                    public void onFailure(Call<LostPerson> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        isGetLostPersons=true;
                        dataIsReady();
                    }
                });
            }

        } else
            Log.e("lost", "There is no objects of Losses.");
            //Toast.makeText(getApplicationContext(), "There is no objects of Losses", Toast.LENGTH_LONG).show();

    }

    private void dataIsReady() {
        if(isGetFoundItems && isGetFoundPersons && isGetLostItems && isGetLostPersons)
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
    }

    public void getHomeFounds() {
        List<Integer> founds = new ArrayList<>();
        founds = User.getUser().getFounds();
        GlobalVar.founds = new ArrayList<String>();
        if (!founds.isEmpty()) {
            for (int i = 0; i < founds.size(); i++) {
                ApiInterface apiInterface = ApiClient.getApiClient(new PrefManager(getApplicationContext()).getNGROKLink()).create(ApiInterface.class);
                    Call<List<FoundItem>> call2 = apiInterface.getFoundItem(founds.get(i));
                    call2.enqueue(new Callback<List<FoundItem>>() {
                        @Override
                        public void onResponse(Call<List<FoundItem>> call, Response<List<FoundItem>> response) {
                            list = response.body();
                            if (response.body()!=null&& list.size() !=0) {
                                String t = list.get(0).getType() + " " + list.get(0).getBrand() + "";
                                GlobalVar.founds.add(t);
                            }
                            isGetFoundItems=true;
                            dataIsReady();
                        }
                        @Override
                        public void onFailure(Call<List<FoundItem>> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                            isGetFoundItems=true;
                            dataIsReady();
                        }
                    });
                    Call<FoundPerson> call3 = apiInterface.getFoundPerson(founds.get(i));
                    call3.enqueue(new Callback<FoundPerson>() {
                        @Override
                        public void onResponse(Call<FoundPerson> call, Response<FoundPerson> response) {
                            list3 = response.body();
                            Log.e("TAG", "onResponse: "+response.body());
                            if (response.body() != null) {
                               String t = list3.getName() + " is Founds";
                                GlobalVar.founds.add(t);
                            }
                            isGetFoundPersons=true;
                            dataIsReady();
                        }

                        @Override
                        public void onFailure(Call<FoundPerson> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                            isGetFoundPersons=true;
                            dataIsReady();
                        }
                    });

            }
        } else
            Log.e("found", "There is no objects of Founds.");
            //Toast.makeText(getApplicationContext(), "There is no objects of Founds.", Toast.LENGTH_LONG).show();
    }

}