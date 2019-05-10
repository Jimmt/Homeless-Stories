package com.team7.homelessstories;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.json.JSONException;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


public class StoryEndFragment extends OptionsFragment {
    private static final String ARG_STORY = "story";

    private Story story;

    private int NUM_PAGES = 0;

    private ViewGroup container;

    public StoryEndFragment() {
        // Required empty public constructor
    }

    public static StoryEndFragment newInstance(Story story) {
        StoryEndFragment fragment = new StoryEndFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_STORY, story);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (getArguments() != null) {
            story = (Story) getArguments().get(ARG_STORY);
        }
        NUM_PAGES = story.getDecisions().size();

        this.container = container;

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_story_end, container, false);

        listener.updateToolbarTitle(story.getName() + "'s Story", Gravity.LEFT);
        listener.setToolbarStyle(this);

        TextView finalTextHeader = view.findViewById(R.id.final_text_header);
        finalTextHeader.setText("Final Decision");

        TextView finalTextView = view.findViewById(R.id.final_text);
        finalTextView.setText(story.getFinalText());


        TextView keyDecisionsText = view.findViewById(R.id.key_decisions_text);
        keyDecisionsText.setText("Key Decisions");

        ViewPager pager = view.findViewById(R.id.view_pager);
        pager.setAdapter(new CustomPagerAdapter(getContext()));

        MaterialButton backToStoriesButton = view.findViewById(R.id.back_to_stories_button);
        backToStoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.switchFragments(StoriesFragment.newInstance(), TransitionType.FADE_IN_SLIDE_DOWN);
            }
        });

        MaterialButton readMoreButton = view.findViewById(R.id.read_more_button);
        readMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
            }
        });

        return view;
    }

    private class CustomPagerAdapter extends PagerAdapter {
        private ImageView[] pages;
        private Context context;

        public CustomPagerAdapter(Context context) {
            this.context = context;
            pages = new ImageView[NUM_PAGES];

            for (int i = 0; i < NUM_PAGES; i++) {
                pages[i] = null;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            ImageView iv = new ImageView(context);
//            int imageId = getResources().getIdentifier(story.getDecisions().get(position).getImageName(), "drawable", getContext().getPackageName());
//            iv.setImageResource(imageId);
            iv.setImageResource(R.drawable.placeholder);
            collection.addView(iv);
            return iv;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object view) {
            container.removeView((View) view);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }
}
