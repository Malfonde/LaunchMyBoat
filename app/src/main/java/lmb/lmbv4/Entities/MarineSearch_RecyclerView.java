package lmb.lmbv4.Entities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import lmb.lmbv4.ClientSide.ItemListActivity;
import lmb.lmbv4.ClientSide.MarineDetailActivity;
import lmb.lmbv4.ClientSide.MarineDetailFragment;
import lmb.lmbv4.Entities.Singletons.ImageLoader;
import lmb.lmbv4.Entities.Singletons.MyApplication;
import lmb.lmbv4.R;


/**
 * Created by izik on 12/26/2016.
 */

public class MarineSearch_RecyclerView extends RecyclerView.Adapter<MarineSearch_RecyclerView.ViewHolder>
{
    private final ArrayList<Marine> mValues;
    private boolean mTwoPane;

    private Activity activity;
    private String[] data;

    public MarineSearch_RecyclerView(ArrayList<Marine> marines)
    {
        mValues = marines;
    }

    public MarineSearch_RecyclerView(ItemListActivity itemListActivity, String[] marinesPicturesPaths, ArrayList<Marine> marines, boolean mTwoPane)
    {
        activity = itemListActivity;
        data = marinesPicturesPaths;
        mValues = marines;
        this.mTwoPane = mTwoPane;
    }

    @Override
    public MarineSearch_RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new MarineSearch_RecyclerView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MarineSearch_RecyclerView.ViewHolder holder, int position)
    {
        holder.mItem = mValues.get(position);

        //set image
        holder.displayImage(data[position]);

        //this is like onItemClickListener
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if (mTwoPane)
                {
                    //TODO: tablet view
                    /*
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(MarineDetailFragment.ARG_ITEM_ID, holder.mItem);
                    MarineDetailFragment fragment = new MarineDetailFragment();
                    fragment.setArguments(arguments);
                    ((AppCompatActivity)activity).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                    */
                }
                else
                {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MarineDetailActivity.class);
                    intent.putExtra(MarineDetailFragment.ARG_ITEM_ID, holder.mItem);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final View mView;
        public final ImageView mMarineImageView;
        public Marine mItem;
        private ImageLoader imageLoader;

        public ViewHolder(View view)
        {
            super(view);
            mView = view;
            mMarineImageView = (ImageView) view.findViewById(R.id.marineImage);
            imageLoader = ((MyApplication)activity.getApplication()).getImageLoader();//  new ImageLoader(activity.getApplicationContext());
        }

        public void displayImage(String picPath)
        {
            imageLoader.DisplayImage(picPath, mMarineImageView);
        }

        @Override
        public String toString()
        {
            if(mItem != null)
            {
                return super.toString() + " '" + mItem.getName() + "'";
            }

            return "";
        }
    }
}
