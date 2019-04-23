package com.team7.homelessstories;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
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

    private TextView text;
    private LinearLayout buttonContainer;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container, false);

        text = view.findViewById(R.id.decision_text);
        buttonContainer = view.findViewById(R.id.button_container);
        // Remove buttons put there for UI testing in-IDE.
        buttonContainer.removeAllViews();

        displayDecision(inflater, container);
        return view;
    }

    private void displayDecision(LayoutInflater inflater, final ViewGroup container) {
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
                        // Move to final screen
                    }

                    // Might need an intermediary screen instead of jumping between decisions quickly
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(container.getId(), frag).addToBackStack(null).commit();
                }
            });
        }
    }

}
