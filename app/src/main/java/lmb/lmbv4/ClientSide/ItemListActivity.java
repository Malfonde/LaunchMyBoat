package lmb.lmbv4.ClientSide;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import lmb.lmbv4.DAL.Model;
import lmb.lmbv4.Entities.Marine;
import lmb.lmbv4.Entities.MarineSearch_RecyclerView;
import lmb.lmbv4.R;

public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;


        if (findViewById(R.id.marine_detail_fragment_container) != null)
        {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
             mTwoPane = true;
        }

        setupRecyclerView((RecyclerView) recyclerView, mTwoPane);

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, boolean mTwoPane)
    {
        String[] MarinesPicturesPaths = Model.instance().getMarinesPicturesPaths();

        ArrayList<Marine> marines = new ArrayList<>();

        for (String imagePath : MarinesPicturesPaths)
        {
            String prefix = imagePath.replace(Model.SharedFolder,"");
            String id = prefix.replace(Model.MarineProfileImageName,"");
            marines.add(Model.instance().getMarineById(id));
        }

        MarineSearch_RecyclerView marineSearchAdapter = new MarineSearch_RecyclerView(this,MarinesPicturesPaths,marines, mTwoPane);
        recyclerView.setAdapter(marineSearchAdapter);



        // Create a grid layout with two columns
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
    }
}
