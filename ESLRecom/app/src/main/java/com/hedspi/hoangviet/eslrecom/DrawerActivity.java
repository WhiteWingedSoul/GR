package com.hedspi.hoangviet.eslrecom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.hedspi.hoangviet.eslrecom.helpers.SettingHelper;
import com.hedspi.hoangviet.eslrecom.managers.DatabaseManager;
import com.hedspi.hoangviet.eslrecom.models.UserProfile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Linh on 9/10/2016.
 */
public class DrawerActivity extends AppCompatActivity {
    protected Toolbar toolbar;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected CircleImageView avatar;
    protected TextView userName;
    protected ActionBarDrawerToggle toggle;
    protected int activityMenuId;
    protected TextView finishButton;
    protected String currentLanguage;
    private boolean currentvalue = true;
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
        currentLanguage = SettingHelper.getInstance(this).getLanguage();
    }

    public void switchToggle(boolean value){
        if (toggle!=null) {
            currentvalue = value;
            toggle.setDrawerIndicatorEnabled(value);

        }
    }

    protected void setUpNavigationDrawer() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View headerLayout = navigationView.getHeaderView(0);

        avatar = (CircleImageView) headerLayout.findViewById(R.id.avatar);
        userName = (TextView) headerLayout.findViewById(R.id.userName);

        UserProfile profile = DatabaseManager.getUserProfile();
        if(profile!=null){
            Picasso.with(this).load(profile.getAvatarLink())
                    .fit()
                    .into(avatar);
            userName.setText(profile.getName());
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu();
                //drawerOpened = false;
            }

            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();
                //drawerOpened = true;
            }
        };

        drawerLayout.setDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.drawer_item_home:
                        if (!(DrawerActivity.this instanceof MainActivity)) {
                            intent = new Intent(DrawerActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        return true;
                    case R.id.drawer_item_test:
                        if (!(DrawerActivity.this instanceof TestActivity)) {
                            intent = new Intent(DrawerActivity.this, TestActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }

                        return true;
                    case R.id.drawer_item_history:
                        if (!(DrawerActivity.this instanceof HistoryActivity)) {
                            intent = new Intent(DrawerActivity.this, HistoryActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        return true;
                    case R.id.drawer_item_language:
                        String currentLang = SettingHelper.getInstance(getApplicationContext()).getLanguage();
                        if (currentLang.equals("en")){
                            SettingHelper.getInstance(getApplicationContext()).setLanguage("vi");
                            setLocale("vi");
                        }else{
                            SettingHelper.getInstance(getApplicationContext()).setLanguage("en");
                            setLocale("en");
                        }

                        refresh();
                        return true;

                    case R.id.drawer_item_logout:
                        SettingHelper.getInstance(getApplicationContext()).removeUid();
                        LoginManager.getInstance().logOut();
                        intent = new Intent(DrawerActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        return true;

                }
                return false;
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!currentLanguage.equals(SettingHelper.getInstance(this).getLanguage())) {
            refresh();
        }

        ArrayList<MenuItemImpl> menuItems = ((NavigationMenu) navigationView.getMenu()).getVisibleItems();
        for (MenuItem item : menuItems) {
            if (item.getItemId() == activityMenuId)
                item.setChecked(true);
            else item.setCheckable(false);
        }
//        ArrayList<MenuItemImpl> menuItems = ((NavigationMenu) navigationView.getMenu()).getNonActionItems();
//        boolean login = AccountHelper.getInstance().getUserInfo() != null;
//        for (MenuItem item : menuItems) {
//            if (item.getItemId() == R.id.drawer_item_update_profile) {
//                if (login)
//                    item.setVisible(true);
//                else item.setVisible(false);
//            } else if (item.getItemId() == R.id.drawer_item_logout) {
//                if (login)
//                    item.setVisible(true);
//                else item.setVisible(false);
//            }
//        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home && !currentvalue){
            try{
            getSupportFragmentManager().popBackStack();
            }catch (Exception e){
                e.printStackTrace();
            }

            return true;
        }else if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return false;
    }

    protected void refresh() {
        finish();
        Intent intent = new Intent(DrawerActivity.this, DrawerActivity.this.getClass());
        startActivity(intent);
    }

    private void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        /*Intent refresh = new Intent(this, AndroidLocalize.class);
        startActivity(refresh);
        finish();*/
    }
}
