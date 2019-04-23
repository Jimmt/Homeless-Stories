package com.team7.homelessstories;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class StoryFragment extends Fragment {
    private static final String ARG_STORY = "story";
    private static final String ARG_DECISION_INDEX = "decision_index";

    private Story story;
    private int decisionIndex;

    private ViewGroup container;
    private TextView text;
    private LinearLayout buttonContainer;

    private FragmentInteractionListener listener;

    public StoryFragment() {
        // Required empty public constructor
    }


    public static StoryFragment newInstance(Story story, int decisionIndex) {
        StoryFragment fragment = new StoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_STORY, story);
        args.putInt(ARG_DECISION_INDEX, decisionIndex);
        fragment.setArguments(args);
        return fragment;
    }

    public static StoryFragment newInstance(Story story) {
        return newInstance(story, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            story = (Story) getArguments().get(ARG_STORY);
            decisionIndex = getArguments().getInt(ARG_DECISION_INDEX);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.appbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancel:
                // Go back to stories fragment
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(container.getId(), StoriesFragment.newInstance())
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .addToBackStack(null).commit();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.container = container;
        View view = inflater.inflate(R.layout.fragment_story, container, false);

        text = view.findViewById(R.id.decision_text);
        buttonContainer = view.findViewById(R.id.button_container);
        // Remove buttons put there for UI testing in-IDE.
        buttonContainer.removeAllViews();

        displayDecision(inflater);
        listener.updateToolbarTitle(story.getName() + "'s Story");
        return view;
    }

    private void displayDecision(LayoutInflater inflater) {
        text.setText(story.getDecisions().get(decisionIndex).getDecisionText());

        ArrayList<Answer> answers = story.getDecisions().get(decisionIndex).getAnswers();
        for (int i = 0; i < answers.size(); i++) {
            final Answer answer = answers.get(i);
            View v = inflater.inflate(R.layout.answer_button, buttonContainer);
            MaterialButton button = (MaterialButton) buttonContainer.getChildAt(i);

            button.setText(answer.getAnswer());

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!answer.isReal()) {
                        new AlertDialog.Builder(container.getContext())
                                .setMessage(answer.getAnswerText())
                                .setPositiveButton("Ok", null)
                                .create().show();
                        return;
                    }

                    Fragment frag = null;
                    if (decisionIndex < story.getDecisions().size() - 1) {
                        frag = StoryFragment.newInstance(story, ++decisionIndex);
                    } else {
                        frag = StoryEndFragment.newInstance(story);
                    }

                    // Might need an intermediary screen instead of jumping between decisions quickly
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(container.getId(), frag)
                            .addToBackStack(null).commit();
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
