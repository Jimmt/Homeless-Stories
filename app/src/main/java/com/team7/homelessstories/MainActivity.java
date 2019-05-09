package com.team7.homelessstories;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements FragmentInteractionListener  {
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Menu drawerMenu;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private boolean showDrawer = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Utils.getPrefs(this).edit().putBoolean("onboarding_complete", false).commit();

        if (!Utils.getPrefs(this).getBoolean("onboarding_complete", false)) {
            Intent intent = new Intent(MainActivity.this, OnboardingActivity.class);
            startActivity(intent);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, StoriesFragment.newInstance()).commit();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        drawerMenu = navigationView.getMenu();
        setupDrawerContent(drawer);

        toolbarTitle = drawer.findViewById(R.id.toolbar_title);
    }

    private void setupDrawerContent(DrawerLayout drawer) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    private void selectDrawerItem(MenuItem menuItem) {
        if (drawerMenu.findItem(menuItem.getItemId()).isChecked()) {
            drawer.closeDrawers();
            return;
        }
        Fragment fragment = null;
        Class fragmentClass = null;

        switch (menuItem.getItemId()) {
            case R.id.stories:
                fragmentClass = StoriesFragment.class;
                break;
            case R.id.about:
                fragmentClass = AboutFragment.class;
                break;
            case R.id.donate:
                fragmentClass = DonateFragment.class;
                break;
            case R.id.sources:
                fragmentClass = SourcesFragment.class;
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment).addToBackStack(null).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawer.closeDrawers();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (showDrawer) {
            drawer.openDrawer(GravityCompat.START);
        } else {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.slide_down)
                    .replace(R.id.frame_layout, StoriesFragment.newInstance())
                    .addToBackStack(null).commit();
            setUpButton(true);
        }
        return super.onSupportNavigateUp();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    @Override
    public void updateToolbarTitle(String text, int gravity) {
        toolbarTitle.setText(text);
        Toolbar.LayoutParams lp = (Toolbar.LayoutParams) toolbarTitle.getLayoutParams();
        lp.gravity = gravity;
        toolbarTitle.setLayoutParams(lp);
    }

    @Override
    public void setUpButton(boolean showDrawer) {
        this.showDrawer = showDrawer;

        if (showDrawer) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24px);
        }
    }

    @Override
    public void setToolbarStyle(boolean mainScreen) {
        if(mainScreen){
            setUpButton(true);
            toolbar.setBackgroundColor(Color.WHITE);
            toolbar.setElevation(4);
            Typeface face = ResourcesCompat.getFont(this, R.font.lato_regular);
            toolbarTitle.setTypeface(face);

            TypedValue tv = new TypedValue();
            getResources().getValue(R.dimen.logo_letter_spacing, tv, true);
            toolbarTitle.setLetterSpacing(tv.getFloat());
        } else {
            setUpButton(false);
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPage));
            toolbar.setElevation(0);
            Typeface face = ResourcesCompat.getFont(this, R.font.noto_serif_jp_bold);
            toolbarTitle.setTypeface(face);
            toolbarTitle.setLetterSpacing(0);
        }
    }
}
