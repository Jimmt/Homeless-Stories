package com.team7.homelessstories;


import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import pl.allegro.finance.tradukisto.ValueConverters;


public class StoryFragment extends OptionsFragment {
    private static final String ARG_STORY = "story";
    private static final String ARG_DECISION_INDEX = "decision_index";

    private Story story;
    private int decisionIndex;

    private ViewGroup container;
    private ImageView decisionIcon;
    private TextView headerText, text;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (getArguments() != null) {
            story = (Story) getArguments().get(ARG_STORY);
            decisionIndex = getArguments().getInt(ARG_DECISION_INDEX);
        }

        this.container = container;
        View view = inflater.inflate(R.layout.fragment_story, container, false);

        decisionIcon = view.findViewById(R.id.decision_icon);
        headerText = view.findViewById(R.id.decision_header_text);
        text = view.findViewById(R.id.decision_text);
        buttonContainer = view.findViewById(R.id.button_container);
        // Remove buttons put there for UI testing in-IDE.
        buttonContainer.removeAllViews();
//
        displayDecision(inflater);

        listener.updateToolbarTitle(story.getName() + "'s Story", Gravity.LEFT);
        listener.setToolbarStyle(this);

        MaterialProgressBar progressBar = view.findViewById(R.id.progress_bar);

        // Google Forms does it this way, where at step one you are at 1 / n progress, not 0 / n.
        progressBar.setProgress((int) ((decisionIndex + 1.0) / story.getDecisions().size() * 100));

//        TextView progressIndicatorText = view.findViewById(R.id.progress_indicator);
//        progressIndicatorText.setText((decisionIndex + 1) + " of " + story.getDecisions().size());
        return view;
    }

    private void displayDecision(LayoutInflater inflater) {
        Decision decision = story.getDecisions().get(decisionIndex);

        int imageId = getResources().getIdentifier("ic_story_" + decision.getIcon(), "drawable", getContext().getPackageName());
        decisionIcon.setImageResource(imageId);

        String numberWord = ValueConverters.ENGLISH_INTEGER.asWords((decisionIndex + 1));
        numberWord = (String.valueOf(numberWord.charAt(0))).toUpperCase() + numberWord.substring(1);
        headerText.setText("Step " + numberWord);
        text.setText(decision.getDecisionText());

        ArrayList<Answer> answers = story.getDecisions().get(decisionIndex).getAnswers();
        for (int i = 0; i < answers.size(); i++) {
            final Answer answer = answers.get(i);
            View v = inflater.inflate(R.layout.answer_button, buttonContainer);
            MaterialButton button = (MaterialButton) buttonContainer.getChildAt(i);

            button.setText(answer.getAnswer());
            if (i == answers.size() - 1) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) button.getLayoutParams();
                lp.bottomMargin = 0;
                button.setLayoutParams(lp);
            }

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

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                            .replace(container.getId(), CorrectFragment.newInstance(story, decisionIndex))
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }

}
