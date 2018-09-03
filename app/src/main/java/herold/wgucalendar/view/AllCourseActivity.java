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
import herold.wgucalendar.data.CourseData;
import herold.wgucalendar.model.Course;

public class AllCourseActivity extends AppCompatActivity {
    private Context context = this;
    private CourseAdapter adapter;
    private CourseData courseData;
    private DrawerLayout drawerLayout;
    private ImageView imgMenu;
    private List<Course> courses;
    private ListView lvCourses;
    private NavigationView navigationView;


    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_course);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        imgMenu = findViewById(R.id.imgMenu);
        lvCourses = findViewById(R.id.lvCourses);

        ViewHelper.setupToolbar(this, toolbar, R.string.all_course);
        courseData = new CourseData(this);
        courseData.open();
        courses = courseData.all();
        adapter = new CourseAdapter(this, courses);
        lvCourses.setAdapter(adapter);
        lvCourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView lv, View v, int position, long id) {
                viewCourse((Course) lv.getItemAtPosition(position));
            }
        });
        navigationView.setNavigationItemSelectedListener(ViewHelper.getNavigationListener(context, drawerLayout));
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { drawerLayout.openDrawer(GravityCompat.START); }
        });
    }

    private void viewCourse(Course course) {
        Intent intent = new Intent(context, ViewCourseActivity.class);
        intent.putExtra("CourseId", course.getId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        courseData.open();
        courses = courseData.all();
        adapter.clear();
        adapter.addAll(courses);
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onPause() {
        courseData.close();
        super.onPause();
    }
}
