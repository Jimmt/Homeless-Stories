package com.team7.homelessstories;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class StoriesFragment extends Fragment {

    public StoriesFragment() {
        // Required empty public constructor
    }

    public static StoriesFragment newInstance() {
        StoriesFragment fragment = new StoriesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stories, container, false);

        addStoryCards(view, inflater, container);

        return view;
    }

    private void addStoryCards(View view, LayoutInflater inflater, final ViewGroup container) {
        ArrayList<Story> stories = new ArrayList<>();
        try {
            stories = BuildStories.getStories(view.getContext());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LinearLayout storiesContainer = view.findViewById(R.id.stories_container);

        for (int i = 0; i < stories.size(); i++) {
            final Story story = stories.get(i);

            View v = inflater.inflate(R.layout.story_card, storiesContainer);
            MaterialCardView mcv = (MaterialCardView) storiesContainer.getChildAt(i);

            ((TextView) mcv.findViewById(R.id.story_name)).setText(story.getName());
            ((TextView) mcv.findViewById(R.id.story_type)).setText(story.getType());

            mcv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(container.getId(), StoryFragment.newInstance(story)).addToBackStack(null).commit();
                }
            });
        }
    }

}
