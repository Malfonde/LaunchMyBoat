package lmb.lmbv4.ClientSide;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import lmb.lmbv4.Entities.Marine;
import lmb.lmbv4.Entities.Singletons.ImageLoader;
import lmb.lmbv4.Entities.Singletons.MyApplication;
import lmb.lmbv4.R;


public class MarineDetailFragment extends Fragment
{
    public static final String ARG_ITEM_ID = "marine";
    private ImageLoader imageLoader;

    private Marine mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MarineDetailFragment()
    {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;

        if (context instanceof Activity)
        {
            a=(Activity) context;
            imageLoader = ((MyApplication)a.getApplication()).getImageLoader();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID))
        {
            mItem = getArguments().getParcelable(ARG_ITEM_ID);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout =
                    (CollapsingToolbarLayout)
                            activity.findViewById(R.id.marine_detail_activity_toolbar_layout);

            if (appBarLayout != null)
            {
                appBarLayout.setTitle(mItem.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.marine_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null)
        {
            ((TextView) rootView.findViewById(R.id.marine_detail_id)).setText(mItem.getObjectID());
            ((TextView) rootView.findViewById(R.id.marine_detail_name)).setText(mItem.getName());
            imageLoader.DisplayImage(mItem.getImagePath() ,(ImageView) rootView.findViewById(R.id.marine_detail_image));
        }

        return rootView;
    }
}
