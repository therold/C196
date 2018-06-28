package herold.wgucalendar.view;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import herold.wgucalendar.R;
import herold.wgucalendar.data.AssessmentData;
import herold.wgucalendar.model.Course;

public class AddAssessmentActivity extends AppCompatActivity {
    private AssessmentData assessmentData;
    private Button btnSave;
    private Course course;
    private DrawerLayout drawerLayout;
    private EditText txtCourse;
    private EditText txtTitle;
    private EditText txtDueDate;
    private ImageView imgMenu;
    private NavigationView navigationView;
    private Spinner cboType;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        imgMenu = findViewById(R.id.imgMenu);
        txtCourse = findViewById(R.id.txtCourse);
        txtTitle = findViewById(R.id.txtTitle);
        txtDueDate = findViewById(R.id.txtDueDate);
        cboType = findViewById(R.id.cboType);
        btnSave = findViewById(R.id.btnSave);

        ViewHelper.setupToolbar(this, toolbar, R.string.add_assessment);
        ViewHelper.setupDateInput(this, txtDueDate);
        assessmentData = new AssessmentData(this);
        assessmentData.open();
        course = getIntent().getParcelableExtra("Course");
        txtCourse.setText(course.getTitle());

        navigationView.setNavigationItemSelectedListener(ViewHelper.getNavigationListener(this, drawerLayout));
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { drawerLayout.openDrawer(GravityCompat.START); }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { save(); }
        });
    }

    private void save() {
        String title = txtTitle.getText().toString();
        String dueDate = txtDueDate.getText().toString();
        String type = cboType.getSelectedItem().toString();
        assessmentData.createAssessment(title, dueDate, type, course.getId());
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
