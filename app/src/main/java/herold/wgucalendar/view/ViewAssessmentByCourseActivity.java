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
import android.widget.Spinner;

import java.util.List;

import herold.wgucalendar.R;
import herold.wgucalendar.data.AssessmentData;
import herold.wgucalendar.data.CourseData;
import herold.wgucalendar.model.Assessment;
import herold.wgucalendar.model.Course;

public class ViewAssessmentByCourseActivity extends AppCompatActivity {
    private AssessmentAdapter adapter;
    private AssessmentData assessmentData;
    private Context context = this;
    private CourseSpinnerAdapter adpCourse;
    private CourseData courseData;
    private DrawerLayout drawerLayout;
    private ImageView imgMenu;
    private List<Assessment> assessments;
    private List<Course> courses;
    private ListView lvAssessments;
    private NavigationView navigationView;
    private Spinner cboCourse;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assessment_by_course);

        toolbar = findViewById(R.id.toolbar);
        cboCourse = findViewById(R.id.cboCourse);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        imgMenu = findViewById(R.id.imgMenu);
        lvAssessments = findViewById(R.id.lvAssessments);

        ViewHelper.setupToolbar(this, toolbar, R.string.view_assessment_by_course);
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

        courseData = new CourseData(this);
        courseData.open();
        courses = courseData.all();
        adpCourse = new CourseSpinnerAdapter(this, android.R.layout.simple_spinner_item, courses);
        cboCourse.setAdapter(adpCourse);
        cboCourse.setSelection(0);
        cboCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                assessments = assessmentData.findByCourse(((Course) cboCourse.getSelectedItem()).getId());
                adapter.clear();
                adapter.addAll(assessments);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) { }
        });

        navigationView.setNavigationItemSelectedListener(ViewHelper.getNavigationListener(context, drawerLayout));
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { drawerLayout.openDrawer(GravityCompat.START); }
        });
    }

    private void viewAssessment(Assessment assessment) {
        Intent intent = new Intent(context, ViewAssessmentActivity.class);
        intent.putExtra("Assessment", assessment);
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
