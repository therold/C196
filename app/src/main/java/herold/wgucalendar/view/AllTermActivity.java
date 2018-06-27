package herold.wgucalendar.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
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
    private List<Term> terms;
    private TermAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_term);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.all_term);
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
        terms = datasource.all();
        adapter = new TermAdapter(this, terms);
        lvTerms.setAdapter(adapter);
        lvTerms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView lv, View v, int position, long id) {
               Intent intent = new Intent(context, ViewTermActivity.class);
               intent.putExtra("Term", (Term) lv.getItemAtPosition(position));
               startActivityForResult(intent, 0);
           }
        });
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ViewHelper.DATA_SET_CHANGED) {
            datasource.open();
            terms = datasource.all();
            adapter.clear();
            adapter.addAll(terms);
        }
    }

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
