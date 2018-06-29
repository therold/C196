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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import herold.wgucalendar.R;
import herold.wgucalendar.data.TermData;
import herold.wgucalendar.model.Term;

public class MainActivity extends AppCompatActivity {
    private Context context = this;
    private DrawerLayout drawerLayout;
    private ImageView imgMenu;
    LinearLayout mainContent;
    private NavigationView navigationView;
    TermData termData;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        imgMenu = findViewById(R.id.imgMenu);
        navigationView = findViewById(R.id.nav_view);
        mainContent = findViewById(R.id.main_content);
        toolbar = findViewById(R.id.toolbar);
        ViewHelper.setupToolbar(this, toolbar, R.string.main_title);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);

        navigationView.setNavigationItemSelectedListener(ViewHelper.getNavigationListener(context, drawerLayout));
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { drawerLayout.openDrawer(GravityCompat.START); }
        });

        termData = new TermData(this);
    }

    private void viewTerm(Term term) {
        Intent intent = new Intent(context, ViewTermActivity.class);
        intent.putExtra("Term", term);
        startActivity(intent);
    }

    private void clearAll() {
        mainContent.removeAllViews();
    }

    private void updateTerm() {
        TextView txtTermTitle = new TextView(context);
        txtTermTitle.setText("Current Term:");
        mainContent.addView(txtTermTitle);
        Term term = termData.getCurrent();
        if(term != null){
            List<Term> terms = new ArrayList<>();
            terms.add(term);
            ListView lvTerm = new ListView(context);
            TermAdapter adpTerm = new TermAdapter(context, terms);
            lvTerm.setAdapter(adpTerm);
            lvTerm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView lv, View v, int position, long id) {
                    viewTerm((Term) lv.getItemAtPosition(position));
                }
            });
            mainContent.addView(lvTerm);
        } else {
            TextView txtNoTerm = new TextView(context);
            txtNoTerm.setText("No Current Term!");
            txtNoTerm.setTextSize(19);
            mainContent.addView(txtNoTerm);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        termData.open();
        clearAll();
        updateTerm();
    }

    public void onPause() {
        super.onPause();
        termData.close();
    }
}
