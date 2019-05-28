package com.team7.homelessstories;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class CorrectFragment extends OptionsFragment {
    private static final String ARG_STORY = "story";
    private static final String ARG_DECISION_INDEX = "decision_index";

    private Story story;
    private int decisionIndex;

    public CorrectFragment() {
        // Required empty public constructor
    }


    public static CorrectFragment newInstance(Story story, int decisionIndex) {
        CorrectFragment fragment = new CorrectFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_STORY, story);
        args.putInt(ARG_DECISION_INDEX, decisionIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (getArguments() != null) {
            story = (Story) getArguments().get(ARG_STORY);
            decisionIndex = getArguments().getInt(ARG_DECISION_INDEX);
        }

        View view = inflater.inflate(R.layout.fragment_correct, container, false);

        listener.updateToolbarTitle(story.getName() + "'s Story", Gravity.LEFT);
        listener.setToolbarStyle(this);


        TextView bodyText = view.findViewById(R.id.correct_body_text);
        bodyText.setText(story.getDecisions().get(decisionIndex).getCorrectText());

        MaterialProgressBar progressBar = view.findViewById(R.id.progress_bar);

        // Google Forms does it this way, where at step one you are at 1 / n progress, not 0 / n.
        progressBar.setProgress((int) ((decisionIndex + 1.0) / story.getDecisions().size() * 100));

        MaterialButton button = view.findViewById(R.id.next_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag = null;
                if (decisionIndex < story.getDecisions().size() - 1) {
                    decisionIndex++;
                    frag = StoryFragment.newInstance(story, decisionIndex);
                } else {
                    frag = StoryEndFragment.newInstance(story);
                }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(container.getId(), frag)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}
