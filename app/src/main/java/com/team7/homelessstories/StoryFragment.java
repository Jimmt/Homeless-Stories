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


public class StoryFragment extends Fragment {
    private static final String ARG_STORY = "story";
    private static final String ARG_DECISION_INDEX = "decision_index";

    private Story story;
    private int decisionIndex;

    private ViewGroup container;
    private ImageView storyImage;
    private TextView headerText, text;
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
                // TODO should standardize this
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.slide_down, R.anim.slide_up, R.anim.fade_out)
                    .replace(container.getId(), StoriesFragment.newInstance())
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
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            story = (Story) getArguments().get(ARG_STORY);
            decisionIndex = getArguments().getInt(ARG_DECISION_INDEX);
        }

        this.container = container;
        View view = inflater.inflate(R.layout.fragment_story, container, false);

//        storyImage = view.findViewById(R.id.story_image);
        headerText = view.findViewById(R.id.decision_header_text);
        text = view.findViewById(R.id.decision_text);
        buttonContainer = view.findViewById(R.id.button_container);
        // Remove buttons put there for UI testing in-IDE.
        buttonContainer.removeAllViews();
//
        displayDecision(inflater);

        listener.updateToolbarTitle(story.getName() + "'s Story", Gravity.LEFT);
        listener.setToolbarStyle(false);

        MaterialProgressBar progressBar = view.findViewById(R.id.progress_bar);

        // Google Forms does it this way, where at step one you are at 1 / n progress, not 0 / n.
        progressBar.setProgress((int) ((decisionIndex + 1.0) / story.getDecisions().size() * 100));

        TextView progressIndicatorText = view.findViewById(R.id.progress_indicator);
        progressIndicatorText.setText((decisionIndex + 1) + " of " + story.getDecisions().size());
        return view;
    }

    private void displayDecision(LayoutInflater inflater) {
        Decision decision = story.getDecisions().get(decisionIndex);

//        int imageId = getResources().getIdentifier(decision.getImageName(), "drawable", getContext().getPackageName());
//        storyImage.setImageResource(imageId);
        headerText.setText("Step " + (decisionIndex + 1));
        text.setText(decision.getDecisionText());

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
                        decisionIndex++;
                        frag = StoryFragment.newInstance(story, decisionIndex);
                    } else {
                        frag = StoryEndFragment.newInstance(story);
                    }

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                            .replace(container.getId(), frag)
                            .addToBackStack(null)
                            .commit();

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
                    + " must implement FragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}
