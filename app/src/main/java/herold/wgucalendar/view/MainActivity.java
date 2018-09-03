package herold.wgucalendar.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import herold.wgucalendar.R;
import herold.wgucalendar.data.CourseData;
import herold.wgucalendar.data.TermData;
import herold.wgucalendar.model.Course;
import herold.wgucalendar.model.Term;

public class MainActivity extends AppCompatActivity {
    private Context context = this;
    private CourseData courseData;
    private DrawerLayout drawerLayout;
    private ImageView imgMenu;
    private LinearLayout mainContent;
    private NavigationView navigationView;
    private TermData termData;
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

        termData = new TermData(context);
        courseData = new CourseData(context);
    }

    private void viewTerm(Term term) {
        Intent intent = new Intent(context, ViewTermActivity.class);
        intent.putExtra("Term", term);
        startActivity(intent);
    }

    private void viewCourse(Course course) {
        Intent intent = new Intent(context, ViewCourseActivity.class);
        intent.putExtra("CourseId", course.getId());
        startActivity(intent);
    }

    private void clearAll() {
        mainContent.removeAllViews();
    }

    private void updateTerm() {
        TextView txtTermTitle = new TextView(context);
        txtTermTitle.setText(R.string.current_term);
        txtTermTitle.setTextColor(Color.BLACK);
        txtTermTitle.setTypeface(null, Typeface.BOLD);
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
            txtNoTerm.setText(R.string.no_current_term);
            txtNoTerm.setTextSize(19);
            mainContent.addView(txtNoTerm);
        }
    }

    private void updateCourses() {
        TextView txtCourseTitle = new TextView(context);
        txtCourseTitle.setText(R.string.current_course);
        final ViewGroup.MarginLayoutParams lpt = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lpt.setMargins(0,100,0,0);
        txtCourseTitle.setLayoutParams(lpt);
        txtCourseTitle.setTextColor(Color.BLACK);
        txtCourseTitle.setTypeface(null, Typeface.BOLD);
        mainContent.addView(txtCourseTitle);

        List<Course> courses = courseData.getCurrent();
        if(courses != null && courses.size() > 0){
            ListView lvCourse = new ListView(context);
            CourseAdapter adpTerm = new CourseAdapter(context, courses);
            lvCourse.setAdapter(adpTerm);
            lvCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView lv, View v, int position, long id) {
                    viewCourse((Course) lv.getItemAtPosition(position));
                }
            });
            mainContent.addView(lvCourse);
            ViewHelper.setListViewHeight(lvCourse);
        } else {
            TextView txtNoCourse = new TextView(context);
            txtNoCourse.setText(R.string.no_current_course);
            txtNoCourse.setTextSize(19);
            mainContent.addView(txtNoCourse);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        termData.open();
        courseData.open();
        clearAll();
        updateTerm();
        updateCourses();
    }

    public void onPause() {
        super.onPause();
        termData.close();
        courseData.close();
    }
}
