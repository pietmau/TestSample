package com.pietrantuono.pietrantuonoevaluationtask.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.pietrantuono.pietrantuonoevaluationtask.R;
import com.pietrantuono.pietrantuonoevaluationtask.fragments.ContentListFragment;
import com.pietrantuono.pietrantuonoevaluationtask.fragments.DetailFragment;
import com.pietrantuono.pietrantuonoevaluationtask.service.FetchService;

public class MainActivity extends AppCompatActivity implements ContentListFragment.Callback {
    private static final String DETAIL_FRAGMENT_TAG="detail_tag";
    private static final String LIST_FRAGMENT_TAG="liste_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        if(savedInstanceState==null){
            showListFragment();
            startService(new Intent(MainActivity.this,FetchService.class));
        }
    }

    private void showListFragment() {
        getSupportFragmentManager().beginTransaction().add(R.id.container,ContentListFragment.newInstance(),LIST_FRAGMENT_TAG).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showDetailFragment(long itemId) {//TODO ANIMATE
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        DetailFragment detailFragment= DetailFragment.newInstance(itemId);
        transaction.replace(R.id.container, detailFragment,DETAIL_FRAGMENT_TAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
