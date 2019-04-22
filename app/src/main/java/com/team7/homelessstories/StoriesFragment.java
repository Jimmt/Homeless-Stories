package com.team7.homelessstories;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.card.MaterialCardView;


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

        LinearLayout storiesContainer = view.findViewById(R.id.stories_container);
        View v = inflater.inflate(R.layout.story_button, storiesContainer);
        MaterialCardView mcv = v.findViewById(R.id.story_card);
        mcv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Switch to story first decision
                System.out.println("abcd");
            }
        });
        View storyButton2 = inflater.inflate(R.layout.story_button, storiesContainer);

        return view;
    }

}
