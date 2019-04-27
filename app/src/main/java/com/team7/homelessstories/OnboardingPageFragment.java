package com.team7.homelessstories;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnboardingPageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OnboardingPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnboardingPageFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TEXT = "text";
    private static final String ARG_HEADER_TEXT = "headerText";
    private static final String ARG_IMAGE_ID = "image_id";
    private static final String ARG_LAST_PAGE = "last_page";

    private String text, headerText;
    private int imageId;
    private boolean lastPage;

    private OnFragmentInteractionListener mListener;

    public OnboardingPageFragment() {
        // Required empty public constructor
    }

    public static OnboardingPageFragment newInstance(int imageId, String headerText, String text, boolean lastPage) {
        OnboardingPageFragment fragment = new OnboardingPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        args.putString(ARG_HEADER_TEXT, headerText);
        args.putInt(ARG_IMAGE_ID, imageId);
        args.putBoolean(ARG_LAST_PAGE, lastPage);
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
        if (getArguments() != null) {
            text = getArguments().getString(ARG_TEXT);
            headerText = getArguments().getString(ARG_HEADER_TEXT);
            imageId = getArguments().getInt(ARG_IMAGE_ID);
            lastPage = getArguments().getBoolean(ARG_LAST_PAGE);
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_page_onboarding, container, false);
        TextView tv = (TextView) view.findViewById(R.id.text_view);
        tv.setText(text);

        TextView headerTextView = view.findViewById(R.id.header_text);
        headerTextView.setText(headerText);

        ImageView imageView = view.findViewById(R.id.image_view);
        imageView.setImageResource(imageId);

        MaterialButton startButton = view.findViewById(R.id.start_button);
        if (!lastPage) {
            ((ViewGroup) view).removeView(startButton);
        } else {
            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.getPrefs(getContext()).edit().putBoolean("onboarding_complete", true).commit();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
        }

        return view;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
