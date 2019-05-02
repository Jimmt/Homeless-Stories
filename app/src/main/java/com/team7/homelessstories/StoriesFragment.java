package com.team7.homelessstories;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONException;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.transition.Scene;
import androidx.transition.TransitionManager;


public class StoriesFragment extends Fragment {
    private FragmentInteractionListener listener;

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
        listener.updateToolbarTitle("Stories");
        listener.setUpButton(true);
        return view;
    }

    private void addStoryCards(View view, final LayoutInflater inflater, final ViewGroup container) {
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
            final MaterialCardView mcv = (MaterialCardView) storiesContainer.getChildAt(i);
            final View divider = mcv.findViewById(R.id.divider);
            final ExpandableLayout el = mcv.findViewById(R.id.expandable_layout);

            ((TextView) el.findViewById(R.id.preview_text)).setText(story.getPreview());
            ((TextView) mcv.findViewById(R.id.story_name)).setText(story.getName());
            ((TextView) mcv.findViewById(R.id.story_type)).setText(story.getType());

            mcv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    el.toggle();
                    if(el.isExpanded()){
                        divider.setVisibility(View.VISIBLE);
                    } else {
                        divider.setVisibility(View.GONE);
                    }
//                    TransitionManager.beginDelayedTransition(mcv);
//                    StoryDialogGenerator.showDialog(getActivity(), container, story);
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteractionListener) {
            listener = (FragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement StoryFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}
