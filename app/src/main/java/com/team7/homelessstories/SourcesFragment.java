package com.team7.homelessstories;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


public class SourcesFragment extends ListenerFragment {
    public SourcesFragment() {
        // Required empty public constructor
    }


    public static SourcesFragment newInstance() {
        SourcesFragment fragment = new SourcesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sources, container, false);

        listener.setToolbarStyle(this);

        LinearLayout storySourcesContainer = view.findViewById(R.id.story_sources_container);
        String[] storySources = getSources(R.raw.story_sources);
        generateEntries(inflater, storySourcesContainer, R.drawable.ic_story, storySources);

        LinearLayout iconSourcesContainer = view.findViewById(R.id.icon_sources_container);
        String[] iconSources = getSources(R.raw.icon_sources);
        generateEntries(inflater, iconSourcesContainer, R.drawable.ic_source_sm, iconSources);

        return view;
    }

    private void generateEntries(LayoutInflater inflater,
                                 ViewGroup container,
                                 int image,
                                 String[] sources) {
        for (int i = 0; i < sources.length; i++) {
            inflater.inflate(R.layout.source_entry, container);
            ConstraintLayout sourceEntry = (ConstraintLayout) container.getChildAt(i);

            int openIndex = sources[i].indexOf("<b>");
            int closeIndex = sources[i].lastIndexOf("<b>") - 3;

            String storySource = sources[i].replace("<b>", "");

            SpannableStringBuilder builder = new SpannableStringBuilder();

            SpannableString str = new SpannableString(storySource);
            str.setSpan(new ForegroundColorSpan(Color.BLACK), openIndex, closeIndex, 0);
            builder.append(str);

            ImageView entryImage = sourceEntry.findViewById(R.id.entry_image);
            entryImage.setImageResource(image);

            TextView entryText = sourceEntry.findViewById(R.id.entry_text);
            entryText.setText(builder, TextView.BufferType.SPANNABLE);
        }
    }

    private String[] getSources(int file) {
        String result = "";
        try {
            Resources res = getResources();
            InputStream inputStream = res.openRawResource(file);

            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            result = new String(b);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.split("\n\\s*\n");
    }
}
