package com.team7.homelessstories;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class MainActivity extends AppCompatActivity implements FragmentInteractionListener {
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        StoriesFragment frag = StoriesFragment.newInstance();
        if (!Utils.getPrefs(this).getBoolean("onboarding_complete", false)) {
            Intent intent = new Intent(MainActivity.this, OnboardingActivity.class);
            startActivity(intent);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, frag).commit();
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
        setToolbarStyle(frag);
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
                .setCustomAnimations(R.anim.fade_in, R.anim.disappear, R.anim.fade_in, R.anim.disappear)
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
            switchFragments(StoriesFragment.newInstance(), TransitionType.FADE_IN_SLIDE_DOWN);
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void switchFragments(Fragment frag, TransitionType type) {
        FragmentManager fm = getSupportFragmentManager();
        if (type == TransitionType.SLIDE_UP_FADE_OUT) {
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_up, R.anim.fade_out, R.anim.fade_in, R.anim.slide_down)
                    .replace(R.id.frame_layout, frag)
                    .addToBackStack(null)
                    .commit();
        } else if (type == TransitionType.FADE_IN_SLIDE_DOWN) {
            fm.beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.slide_down, R.anim.slide_up, R.anim.fade_out)
                    .replace(R.id.frame_layout, StoriesFragment.newInstance())
                    .addToBackStack(null).commit();
        }
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

    private void setStatusBarColor(int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, colorId));
        }
    }

    @Override
    public void setToolbarStyle(Fragment currentFrag) {
        if (currentFrag instanceof StoriesFragment || currentFrag instanceof AboutFragment
                || currentFrag instanceof DonateFragment || currentFrag instanceof SourcesFragment) {
            toolbarTitle.setBackground(null);
            updateToolbarTitle("PATHS", Gravity.CENTER);
            setUpButton(true);
            setStatusBarColor(android.R.color.white);
            toolbar.setBackgroundColor(Color.WHITE);
            toolbar.setElevation(4);
            Typeface face = ResourcesCompat.getFont(this, R.font.lato_regular);
            toolbarTitle.setTypeface(face);

            TypedValue tv = new TypedValue();
            getResources().getValue(R.dimen.logo_letter_spacing, tv, true);
            toolbarTitle.setLetterSpacing(tv.getFloat());

            toolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        } else {
            toolbarTitle.setBackground(getResources().getDrawable(R.drawable.ic_dots));
            setUpButton(false);

            int pageColor = ContextCompat.getColor(this, R.color.colorPage);
            setStatusBarColor(R.color.colorPage);
            toolbar.setBackgroundColor(pageColor);

            toolbar.setElevation(0);
            Typeface face = ResourcesCompat.getFont(this, R.font.noto_serif_jp_bold);
            toolbarTitle.setTypeface(face);
            toolbarTitle.setLetterSpacing(0);

            toolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.lightGray));
        }
    }
}
