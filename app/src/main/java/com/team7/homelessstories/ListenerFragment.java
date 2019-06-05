package com.team7.homelessstories;

import android.content.Context;

import androidx.fragment.app.Fragment;

/**
 * Parent fragment class. Main purpose is to attach and detach a {@link com.team7.homelessstories.FragmentInteractionListener}.
 */
public class ListenerFragment extends Fragment {
    protected FragmentInteractionListener listener;


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
