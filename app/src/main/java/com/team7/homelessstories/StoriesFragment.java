package com.team7.homelessstories;


import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONException;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.transition.Fade;
import androidx.transition.Scene;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionPropagation;
import androidx.transition.TransitionSet;
import androidx.transition.TransitionValues;
import androidx.transition.Visibility;


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

            ((TextView) mcv.findViewById(R.id.read_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    StoryFragment frag = StoryFragment.newInstance(story);

                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_up, R.anim.fade_out, R.anim.fade_in, R.anim.slide_down)
                            .replace(container.getId(), frag)
                            .addToBackStack(null)
                            .commit();
                }
            });

            ((TextView) mcv.findViewById(R.id.cancel_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    el.collapse();
                    divider.setVisibility(View.GONE);
                }
            });

            mcv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggle(el, divider);
//                    TransitionManager.beginDelayedTransition(mcv);
//                    StoryDialogGenerator.showDialog(getActivity(), container, story);
                }
            });
        }
    }

    public void toggle(ExpandableLayout el, View divider){
        el.toggle();
        if (el.isExpanded()) {
            divider.setVisibility(View.VISIBLE);
        } else {
            divider.setVisibility(View.GONE);
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
