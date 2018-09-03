package herold.wgucalendar.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
    private Context context = this;
    private DrawerLayout drawerLayout;
    private ImageView imgMenu;
    private List<Term> terms;
    private ListView lvTerms;
    private NavigationView navigationView;
    private TermAdapter adapter;
    private TermData termData;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_term);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        imgMenu = findViewById(R.id.imgMenu);
        lvTerms = findViewById(R.id.lvTerms);

        ViewHelper.setupToolbar(this, toolbar, R.string.all_term);
        termData = new TermData(this);
        termData.open();
        terms = termData.all();
        adapter = new TermAdapter(this, terms);
        lvTerms.setAdapter(adapter);
        lvTerms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView lv, View v, int position, long id) {
                viewTerm((Term) lv.getItemAtPosition(position));
            }
        });
        navigationView.setNavigationItemSelectedListener(ViewHelper.getNavigationListener(context, drawerLayout));
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { drawerLayout.openDrawer(GravityCompat.START); }
        });
    }

    private void viewTerm(Term term) {
        Intent intent = new Intent(context, ViewTermActivity.class);
        intent.putExtra("Term", term);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        termData.open();
        terms = termData.all();
        adapter.clear();
        adapter.addAll(terms);
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onPause() {
        termData.close();
        super.onPause();
    }
}
