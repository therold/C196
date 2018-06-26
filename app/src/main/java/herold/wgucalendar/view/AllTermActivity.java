package herold.wgucalendar.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

import herold.wgucalendar.R;
import herold.wgucalendar.data.TermData;
import herold.wgucalendar.model.Term;

public class AllTermActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Context context = this;
    private TermData datasource;
    private ListView lvTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_term);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Terms");
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(ViewHelper.getNavigationListener(context, drawerLayout));
        ImageView imgMenu = findViewById(R.id.imgMenu);
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { drawerLayout.openDrawer(GravityCompat.START); }
        });

        datasource = new TermData(this);
        datasource.open();
        lvTerms = findViewById(R.id.lvTerms);
        List<Term> terms = datasource.all();
        TermAdapter adapter = new TermAdapter(this, terms);
        lvTerms.setAdapter(adapter);
    }


//    public void onClick(View view) {
////        ArrayAdapter<Term> adapter = (ArrayAdapter<Term>) getListAdapter();
//        switch (view.getId()) {
//            case R.id.btnAddTerm:
//                Intent intent = new Intent(this, AddTermActivity.class);
//                startActivity(intent);
//                //Term term = datasource.createTerm("New Term", "s", "e");
//                //adapter.add(term);
//                break;
//            case R.id.delete:
////                if (getListAdapter().getCount() > 0) {
//                    //term = (Term) getListAdapter().getItem(0);
//                    //datasource.deleteTerm(term);
//                    //adapter.remove(term);
////                }
//                break;
//        }
////        adapter.notifyDataSetChanged();
//    }


    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }
}
