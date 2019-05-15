package com.team7.homelessstories;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONException;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {
    private static final int UNSELECTED = -1;
    private FragmentActivity activity;
    private ArrayList<Story> stories;
    private RecyclerView recyclerView;
    private int selectedItem = UNSELECTED;
    private int index = 0;

    public SimpleAdapter(RecyclerView recyclerView, FragmentActivity activity) {
        this.recyclerView = recyclerView;
        this.activity = activity;

        stories = new ArrayList<>();
        try {
            stories = BuildStories.getStories(recyclerView.getContext());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Story story = stories.get(index);
        final MaterialCardView mcv = (MaterialCardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.story_card, parent, false);
        final View divider = mcv.findViewById(R.id.divider);
        final ExpandableLayout el = mcv.findViewById(R.id.expandable_layout);
        final ImageView arrowImage = mcv.findViewById(R.id.arrow_image);

        ImageView image = mcv.findViewById(R.id.person_image);
        image.setImageResource(
                parent.getResources().getIdentifier(story.getImage(), "drawable", parent.getContext().getPackageName()));

        ((TextView) el.findViewById(R.id.preview_text)).setText(story.getPreview());
        ((TextView) mcv.findViewById(R.id.story_name)).setText(story.getName());
        ((TextView) mcv.findViewById(R.id.story_type)).setText(story.getType());

        ((TextView) mcv.findViewById(R.id.read_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                StoryFragment frag = StoryFragment.newInstance(story);

                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_up, R.anim.fade_out, R.anim.fade_in, R.anim.slide_down)
                        .replace(R.id.frame_layout, frag)
                        .addToBackStack(null)
                        .commit();
            }
        });

        mcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                el.toggle();
                if (el.isExpanded()) {
                    arrowImage.animate().rotation(180).start();
                    divider.setVisibility(View.VISIBLE);
                } else {
                    arrowImage.animate().rotation(0).start();
                    divider.setVisibility(View.GONE);
                }
            }
        });

        index++;
        return new ViewHolder(mcv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements ExpandableLayout.OnExpansionUpdateListener {
        private ExpandableLayout expandableLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            expandableLayout.setOnExpansionUpdateListener(this);
        }


        @Override
        public void onExpansionUpdate(float expansionFraction, int state) {
            if (state == ExpandableLayout.State.EXPANDING) {
                recyclerView.scrollToPosition(getAdapterPosition());
            }
        }
    }
}