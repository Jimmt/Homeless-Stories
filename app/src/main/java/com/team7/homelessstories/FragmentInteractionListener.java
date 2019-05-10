package com.team7.homelessstories;

import androidx.fragment.app.Fragment;

public interface FragmentInteractionListener {
    void updateToolbarTitle(String text, int gravity);
    void setUpButton(boolean showDrawer);
    void setToolbarStyle(Fragment fragment);
    void switchFragments(Fragment fragment, TransitionType type);
}
