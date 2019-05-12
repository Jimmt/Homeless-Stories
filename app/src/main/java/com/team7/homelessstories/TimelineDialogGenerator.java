package com.team7.homelessstories;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class TimelineDialogGenerator {
    public static void showDialog(FragmentActivity activity, LayoutInflater inflater, final View container, final Story story) {
        AlertDialog dialog = new AlertDialog.Builder(container.getContext())
                .setTitle(story.getName() + "'s Key Decisions")
                .setView(R.layout.timeline)
                .setNegativeButton("Close", null)
                .create();

        dialog.show();

        populateTimeline(activity, inflater, dialog.findViewById(R.id.timeline_container), story);

        Button closeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        Typeface face = ResourcesCompat.getFont(activity, R.font.lato_regular);
        closeButton.setTypeface(face);

//        closeButton.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
    }

    private static void populateTimeline(FragmentActivity activity, LayoutInflater inflater, LinearLayout layout, Story story) {
        int childIndex = 0;
        for (int i = 0; i < story.getDecisions().size(); i++) {
            Decision decision = story.getDecisions().get(i);

            if (decision.getKeyDecisionText().isEmpty()) {
                continue;
            }

            inflater.inflate(R.layout.timeline_entry, layout);

            // Multiply by 2 to skip the spacers
            ConstraintLayout entry = (ConstraintLayout) layout.getChildAt(childIndex * 2);
            childIndex++;

            // Decision
            int imageId = activity.getResources().getIdentifier("ic_story_" + decision.getIcon(), "drawable", activity.getPackageName());
            ((ImageView) entry.findViewById(R.id.entry_image)).setImageResource(imageId);
            ((TextView) entry.findViewById(R.id.entry_text)).setText(decision.getKeyDecisionText());

            // Spacer
            if (i != story.getDecisions().size() - 1) {
                inflater.inflate(R.layout.timeline_spacer, layout);
            }
        }
    }
}