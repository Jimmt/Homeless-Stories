package com.team7.homelessstories;

public interface FragmentInteractionListener {
    void updateToolbarTitle(String text, int gravity);
    void setUpButton(boolean showDrawer);
    void setToolbarStyle(boolean mainScreen);
}
