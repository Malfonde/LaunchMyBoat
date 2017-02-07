package lmb.lmbv4.ClientSide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import lmb.lmbv4.DAL.Model;
import lmb.lmbv4.Entities.LazyAdapter;
import lmb.lmbv4.Entities.Marine;
import lmb.lmbv4.R;


public class MarinesSearch extends AppCompatActivity {


    private GridView marines;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marines_search);

        marines = (GridView)findViewById(R.id.marines_gridview);
        String[] MarinesPicturesPaths = Model.instance().getMarinesPicturesPaths();

        LazyAdapter la = new LazyAdapter(this,MarinesPicturesPaths);
        marines.setAdapter(la);
        marines.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                ShowMarineDetails(Model.instance().getMarineIds().get(position), false);
            }
        });
    }

    private void ShowMarineDetails(String s, boolean b)
    {
        Marine currMarine = Model.instance().getMarineById(s);
      //  Intent marineDetails = new Intent(MarinesSearch.this, MarineDetails.class);
     //   startActivity(marineDetails);
    }

    @Override
    public void onDestroy()
    {
        marines.setAdapter(null);
        super.onDestroy();
    }
}
