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

            String[] headerText = {"Did You Know?", "Walk In Their Shoes", "Call To Action"};
            String[] bodyText = {"Throughout the year hundreds continue to face the prospects of losing their homes due to financial hardships, " +
                    "family breakups, mental illness, drug or alcohol abuse and/or domestic violence.",
                    "This is body text",
                    "This is body text part 2"};

            for (int i = 0; i < NUM_PAGES; i++) {
                pages[i] = OnboardingPageFragment.newInstance(R.drawable.placeholder, headerText[i],bodyText[i],
                        i == NUM_PAGES - 1 ? true : false);
            }
        }

        @Override
        public Fragment getItem(int position) {
            return pages[position];
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
