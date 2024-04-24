package com.hcmus.manage_employee.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.hcmus.manage_employee.Fragments.BottomSheetTaskFragment;
import com.hcmus.manage_employee.R;
import com.hcmus.manage_employee.others.SharedPref;
import com.hcmus.manage_employee.ui.mytasks.MyTasksFragment;
import com.hcmus.manage_employee.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

public class WorkerDashboardActivity extends AppCompatActivity {
    private static final String TAG = "WORKER_DASHBOARD_ACTIVITY";

    private AppBarConfiguration mAppBarConfiguration;
    private ImageView mProfileImage;
    private NavigationView navigationView;
    private Bundle taskSortBundle;
    private LinearLayout linearLayout;
    private SharedPref sharedPref;
    private TextView nav_name, nav_email;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_dashboard);
        db = FirebaseFirestore.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        taskSortBundle = new Bundle();
        sharedPref = new SharedPref(WorkerDashboardActivity.this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        nav_name = header.findViewById(R.id.nav_name);
        nav_email = header.findViewById(R.id.nav_email);
        nav_name.setText(sharedPref.getNAME());
        nav_email.setText(sharedPref.getEMAIL());
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkerDashboardActivity.this, OwnWorkerProfileActivity.class);
                startActivity(intent);
            }
        });
        mProfileImage = header.findViewById(R.id.profile_image);
        loadNavViewHeaderImage();
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_tasks, R.id.nav_calendar, R.id.nav_search,
                R.id.nav_leave, R.id.nav_allowance, R.id.nav_project_status,
                R.id.nav_scaner_qr, R.id.nav_calendar_attendance)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                invalidateOptionsMenu();
            }
        });
        navigationView.setItemIconTintList(null);
        setDefaultIconTint();
        MenuItem favoriteItem = navigationView.getMenu().findItem(R.id.nav_project_status);
        Drawable favoriteIcon = DrawableCompat.wrap(favoriteItem.getIcon());
        ColorStateList colorSelector = ResourcesCompat.getColorStateList(getResources(), R.color.success, getTheme());
        DrawableCompat.setTintList(favoriteIcon, colorSelector);
        favoriteItem.setIcon(favoriteIcon);
        getFCMToken();
    }
    private void loadNavViewHeaderImage() {
        db.collection("Employees").document(sharedPref.getEMP_ID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if(!documentSnapshot.getData().containsKey("profile_image") || documentSnapshot.getString("profile_image").equals("")) {
                    ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
                    int color = colorGenerator.getRandomColor();
                    TextDrawable textDrawable = TextDrawable.builder().buildRect(String.valueOf(sharedPref.getNAME().charAt(0)),color);
                    mProfileImage.setImageDrawable(textDrawable);
                }
                else {
                    Picasso.get().load(task.getResult().getString("profile_image")).fit().into(mProfileImage);
                }
            }
        });
    }

    private void setDefaultIconTint() {
        ColorStateList colorSelector1 = ResourcesCompat.getColorStateList(getResources(), android.R.color.white, getTheme());

        MenuItem favoriteItem = navigationView.getMenu().findItem(R.id.nav_tasks);
        Drawable favoriteIcon = DrawableCompat.wrap(favoriteItem.getIcon());
        DrawableCompat.setTintList(favoriteIcon, colorSelector1);
        favoriteItem.setIcon(favoriteIcon);

        MenuItem favoriteItem1 = navigationView.getMenu().findItem(R.id.nav_calendar);
        Drawable favoriteIcon1 = DrawableCompat.wrap(favoriteItem1.getIcon());
        DrawableCompat.setTintList(favoriteIcon1, colorSelector1);
        favoriteItem1.setIcon(favoriteIcon1);

        MenuItem favoriteItem2 = navigationView.getMenu().findItem(R.id.nav_search);
        Drawable favoriteIcon2 = DrawableCompat.wrap(favoriteItem2.getIcon());
        DrawableCompat.setTintList(favoriteIcon2, colorSelector1);
        favoriteItem2.setIcon(favoriteIcon2);

        MenuItem favoriteItem3 = navigationView.getMenu().findItem(R.id.nav_allowance);
        Drawable favoriteIcon3 = DrawableCompat.wrap(favoriteItem3.getIcon());
        DrawableCompat.setTintList(favoriteIcon3, colorSelector1);
        favoriteItem3.setIcon(favoriteIcon3);

        MenuItem favoriteItem4 = navigationView.getMenu().findItem(R.id.nav_leave);
        Drawable favoriteIcon4 = DrawableCompat.wrap(favoriteItem4.getIcon());
        DrawableCompat.setTintList(favoriteIcon4, colorSelector1);
        favoriteItem4.setIcon(favoriteIcon4);

        MenuItem favoriteItem5 = navigationView.getMenu().findItem(R.id.nav_worker_laws);
        Drawable favoriteIcon5 = DrawableCompat.wrap(favoriteItem5.getIcon());
        DrawableCompat.setTintList(favoriteIcon5, colorSelector1);
        favoriteItem5.setIcon(favoriteIcon5);
        linearLayout = findViewById(R.id.ll_logout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            SharedPref sharedPref = new SharedPref(getBaseContext());
                            sharedPref.logout();
                            ExitActivity.exitApplicationAndRemoveFromRecent(WorkerDashboardActivity.this);
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.worker_dashboard, menu);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        int destId = navController.getCurrentDestination().getId();
        if (destId == R.id.nav_search) {
            MenuItem item = menu.findItem(R.id.action_settings);
            item.setIcon(R.drawable.ic_search2);
        }

        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                if(navigationView.getMenu().findItem(R.id.nav_tasks).isChecked())
                {
                    BottomSheetTaskFragment bottomSheetTaskFragment = new BottomSheetTaskFragment();
                    taskSortBundle = initTaskSortBundle();
                    bottomSheetTaskFragment.setArguments(taskSortBundle);
                    bottomSheetTaskFragment.show(getSupportFragmentManager(), "bstf");
                } else if (navigationView.getMenu().findItem(R.id.nav_search).isChecked())
                {
                    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                    navController.navigate(R.id.searchFragment);
                    return true;
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onWorkerDetailsSortingChanged(Bundle b) {
        boolean nameChip =  b.getBoolean("nameChip");
        boolean maleChip =  b.getBoolean("maleChip");
        boolean femaleChip =  b.getBoolean("femaleChip");
        taskSortBundle = (Bundle)b.clone();
        MyTasksFragment fragment = (MyTasksFragment) getSupportFragmentManager().findFragmentByTag("fbss");
        if (fragment != null) {
            fragment.sort(getBaseContext(), taskSortBundle, nameChip, maleChip, femaleChip);
        }else{
            Log.e(TAG, "fragment is null");
        }
    }

    private Bundle initTaskSortBundle() {
        if(taskSortBundle.isEmpty()) {
            taskSortBundle.putBoolean("isSupervisor",false);
            taskSortBundle.putBoolean("dateChip",false);
            taskSortBundle.putBoolean("completedChip",false);
            taskSortBundle.putBoolean("incompleteChip",false);
            return taskSortBundle;
        }
        else {
            return taskSortBundle;
        }
    }
    void getFCMToken(){
        Context context = getApplicationContext();
        SharedPref sharedPref = new SharedPref(context);
        String currentUserId = sharedPref.getEMP_ID();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                String token = task.getResult();
                FirebaseUtil.currentUserDetails(currentUserId).update("fcmToken",token);
            }
        });
    }

}