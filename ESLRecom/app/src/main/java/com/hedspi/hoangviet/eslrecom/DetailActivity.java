package com.hedspi.hoangviet.eslrecom;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.hedspi.hoangviet.eslrecom.fragments.DetailBookFragment;
import com.hedspi.hoangviet.eslrecom.fragments.SetBookProfileFragment;
import com.hedspi.hoangviet.eslrecom.fragments.UpdateBookProfileFragment;
import com.hedspi.hoangviet.eslrecom.models.Material;
import com.hedspi.hoangviet.eslrecom.models.BookProfile;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        FragmentManager fragmentManager = getSupportFragmentManager();
        int type = getIntent().getExtras().getInt("type");
        switch (type){
            case MainActivity.ADDED:
                BookProfile bookProfile = (BookProfile)getIntent().getExtras().getSerializable("bookProfile");
                fragmentManager.beginTransaction().replace(R.id.fragment, UpdateBookProfileFragment.newInstance(bookProfile)).commit();
                break;
            case MainActivity.TO_BE_ADDED:
                Material book = (Material)getIntent().getExtras().getSerializable("book");
                fragmentManager.beginTransaction().replace(R.id.fragment, SetBookProfileFragment.newInstance(book)).commit();
                break;
            case MainActivity.VIEW:
                book = (Material)getIntent().getExtras().getSerializable("book");
//                fragmentManager.beginTransaction().add(R.id.fragment, DetailBookFragment.newInstance(book)).commit();
                break;
        }


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
