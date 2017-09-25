package com.aronskiy_anton.p2p.controllers.employer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.aronskiy_anton.p2p.R;
import com.aronskiy_anton.p2p.controllers.freelancer.FreelancerActivity;
import com.aronskiy_anton.p2p.data.Repository;
import com.aronskiy_anton.p2p.utils.ActivityUtils;

/**
 * Created by Aronskiy Anton on 13.09.2017.
 */

public class EmployerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employer_activity);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        if(ab == null) {
            String errorMsg = "This activity requires an AppCompat theme with an action bar, finishing activity...";
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            this.finish();
        } else {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // Set up the navigation drawer.
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        EmployerFragment employerFragment =
                (EmployerFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (employerFragment == null) {
            // Create the fragment
            employerFragment = EmployerFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), employerFragment, R.id.contentFrame);
        }

        new EmployerPresenter(employerFragment, Repository.getInstance());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.freelancer_mode:
                                Intent intent =
                                        new Intent(EmployerActivity.this, FreelancerActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.employer_mode:
                                // Do nothing, we're already on that screen
                                break;
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
}
