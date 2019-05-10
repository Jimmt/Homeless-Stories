package com.team7.homelessstories;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import java.io.InputStream;

import androidx.fragment.app.Fragment;


public class DonateFragment extends ListenerFragment {

    public DonateFragment() {
        // Required empty public constructor
    }


    public static DonateFragment newInstance() {
        DonateFragment fragment = new DonateFragment();
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
        View view = inflater.inflate(R.layout.fragment_donate, container, false);

        String result = "";
        try {
            Resources res = getResources();
            InputStream inputStream = res.openRawResource(R.raw.donate);

            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            result = new String(b);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] places = result.split("\n\\s*\n");

        LinearLayout cardContainer = view.findViewById(R.id.donate_card_container);

        for(int i = 0; i < places.length; i++){
            String[] lines = places[i].split("\\n");
            String image = lines[0].trim();
            int imageId = getResources().getIdentifier(image, "drawable", getContext().getPackageName());
            String name = lines[1];
            String type = lines[2];
            final String link = lines[3];

            System.out.println(imageId + " " + image + R.drawable.avatar);
            inflater.inflate(R.layout.donate_place_card, cardContainer);

            MaterialCardView mcv = (MaterialCardView) cardContainer.getChildAt(i);
            ((ImageView) mcv.findViewById(R.id.place_image)).setImageResource(imageId);
            ((TextView) mcv.findViewById(R.id.place_name)).setText(name);
            ((TextView) mcv.findViewById(R.id.place_type)).setText(type);

            mcv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    startActivity(browserIntent);
                }
            });
        }

        listener.setToolbarStyle(this);
        return view;
    }
}
