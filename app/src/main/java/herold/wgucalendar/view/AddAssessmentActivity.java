package herold.wgucalendar.view;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import herold.wgucalendar.R;
import herold.wgucalendar.data.AssessmentData;
import herold.wgucalendar.data.CourseData;
import herold.wgucalendar.model.Course;

public class AddAssessmentActivity extends AppCompatActivity {
    private AssessmentData assessmentData;
    private Course course;
    private CourseSpinnerAdapter adpCourse;
    private CourseData courseData;
    private DrawerLayout drawerLayout;
    private EditText txtTitle;
    private EditText txtDueDate;
    private List<Course> courses;
    private NavigationView navigationView;
    private Spinner cboCourse;
    private Spinner cboType;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        cboCourse = findViewById(R.id.cboCourse);
        txtTitle = findViewById(R.id.txtTitle);
        txtDueDate = findViewById(R.id.txtDueDate);
        cboType = findViewById(R.id.cboType);

        ViewHelper.setupDateInput(this, txtDueDate);
        assessmentData = new AssessmentData(this);
        assessmentData.open();

        course = getIntent().getParcelableExtra("Course");
        courseData = new CourseData(this);
        courseData.open();
        courses = courseData.all();
        adpCourse = new CourseSpinnerAdapter(this, android.R.layout.simple_spinner_item, courses);
        cboCourse.setAdapter(adpCourse);
        if (course != null) {
            cboCourse.setSelection(courses.indexOf(course));
        }

        navigationView.setNavigationItemSelectedListener(ViewHelper.getNavigationListener(this, drawerLayout));

        Button btnSave = new Button(this);
        btnSave.setText(R.string.save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { save(); }
        });
        btnSave.setLayoutParams(ViewHelper.rightLayout());
        toolbar.addView(btnSave);

        Button btnCancel = new Button(this);
        btnCancel.setText(R.string.cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { cancel(); }
        });
        btnCancel.setLayoutParams(ViewHelper.leftLayout());
        toolbar.addView(btnCancel);

        TextView txtTitle = ViewHelper.getTitleText(this, R.string.add_assessment);
        toolbar.addView(txtTitle);
    }

    private void save() {
        String title = txtTitle.getText().toString();
        String dueDate = txtDueDate.getText().toString();
        String type = cboType.getSelectedItem().toString();
        long courseId = ((Course) cboCourse.getSelectedItem()).getId();
        assessmentData.createAssessment(title, type, dueDate, courseId);
        finish();
    }

    private void cancel() {
        finish();
    }

    @Override
    protected void onResume() {
        assessmentData.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        assessmentData.close();
        super.onPause();
    }
}
