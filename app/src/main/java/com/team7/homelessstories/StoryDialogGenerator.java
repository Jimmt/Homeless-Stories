package com.team7.homelessstories;

import android.content.DialogInterface;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class StoryDialogGenerator {
    public static void showDialog(final FragmentActivity activity, final View container, final Story story){
        DialogInterface.OnClickListener buttonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(container.getId(), StoryFragment.newInstance(story)).addToBackStack(null).commit();
            }
        };

        new AlertDialog.Builder(container.getContext())
                .setTitle("Read " + story.getName() + "'s Story")
                .setMessage(story.getPreview())
                .setPositiveButton("Read", buttonListener)
                .setNegativeButton("Cancel", null)
                .create().show();
    }
}
