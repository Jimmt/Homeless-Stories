package com.team7.homelessstories;

import androidx.fragment.app.Fragment;

public interface FragmentInteractionListener {
    void updateToolbarTitle(String text);
    void setUpButton(boolean showDrawer);
}
