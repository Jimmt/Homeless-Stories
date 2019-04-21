package com.team7.homelessstories;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;

public class OnboardingActivity extends FragmentActivity implements OnboardingPageFragment.OnFragmentInteractionListener {
    private static final int NUM_PAGES = 3;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        pager = (ViewPager) findViewById(R.id.view_pager);

        PagerAdapter pagerAdapter = new PagerAdapter(this, getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);

        Utils.getPrefs(this).edit().putBoolean("onboarding_complete", false);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class PagerAdapter extends FragmentStatePagerAdapter {
        private Fragment[] pages;
        private Context context;

        public PagerAdapter(Context context, FragmentManager m) {
            super(m);
            this.context = context;
            pages = new Fragment[NUM_PAGES];

            for(int i = 0; i < NUM_PAGES; i++){
                pages[i] = OnboardingPageFragment.newInstance("This is page " + String.valueOf(i));
            }
        }

        @Override
        public Fragment getItem(int position) {
            // Change to button press later
            if(position == pages.length - 1){
                Utils.getPrefs(context).edit().putBoolean("onboarding_complete", true).commit();
                System.out.println(Utils.getPrefs(context).getBoolean("onboarding_complete", false));
            }
            return pages[position];
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
