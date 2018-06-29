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
import herold.wgucalendar.data.AssessmentData;
import herold.wgucalendar.model.Assessment;

public class AllAssessmentActivity extends AppCompatActivity {
    private AssessmentAdapter adapter;
    private AssessmentData assessmentData;
    private Context context = this;
    private DrawerLayout drawerLayout;
    private ImageView imgMenu;
    private List<Assessment> assessments;
    private ListView lvAssessments;
    private NavigationView navigationView;


    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_assessment);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        imgMenu = findViewById(R.id.imgMenu);
        lvAssessments = findViewById(R.id.lvAssessments);

        ViewHelper.setupToolbar(this, toolbar, R.string.all_course);
        assessmentData = new AssessmentData(this);
        assessmentData.open();
        assessments = assessmentData.all();
        adapter = new AssessmentAdapter(this, assessments);
        lvAssessments.setAdapter(adapter);
        lvAssessments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView lv, View v, int position, long id) {
                viewAssessment((Assessment) lv.getItemAtPosition(position));
            }
        });
        navigationView.setNavigationItemSelectedListener(ViewHelper.getNavigationListener(context, drawerLayout));
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { drawerLayout.openDrawer(GravityCompat.START); }
        });
    }

    private void viewAssessment(Assessment assessment) {
        Intent intent = new Intent(context, ViewCourseActivity.class);
        intent.putExtra("Assessment", assessment.getId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        assessmentData.open();
        assessments = assessmentData.all();
        adapter.clear();
        adapter.addAll(assessments);
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onPause() {
        assessmentData.close();
        super.onPause();
    }
}
