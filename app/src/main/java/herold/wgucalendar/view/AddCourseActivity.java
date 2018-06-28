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
import herold.wgucalendar.data.CourseData;
import herold.wgucalendar.model.Term;

public class AddCourseActivity extends AppCompatActivity {
    private Button btnSave;
    private CourseData courseData;
    private DrawerLayout drawerLayout;
    private EditText txtTerm;
    private EditText txtTitle;
    private EditText txtStartDate;
    private EditText txtEndDate;
    private EditText txtMentorName;
    private EditText txtMentorPhone;
    private EditText txtMentorEmail;
    private EditText txtNotes;
    private ImageView imgMenu;
    private NavigationView navigationView;
    private Spinner cboStatus;
    private Term term;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        imgMenu = findViewById(R.id.imgMenu);
        txtTerm = findViewById(R.id.txtTerm);
        txtTitle = findViewById(R.id.txtTitle);
        txtStartDate = findViewById(R.id.txtStartDate);
        txtEndDate = findViewById(R.id.txtEndDate);
        cboStatus = findViewById(R.id.cboStatus);
        txtMentorName = findViewById(R.id.txtMentorName);
        txtMentorPhone = findViewById(R.id.txtMentorPhone);
        txtMentorEmail = findViewById(R.id.txtMentorEmail);
        txtNotes = findViewById(R.id.txtNotes);
        btnSave = findViewById(R.id.btnSave);

        ViewHelper.setupToolbar(this, toolbar, R.string.add_course);
        ViewHelper.setupDateInput(this, txtStartDate);
        ViewHelper.setupDateInput(this, txtEndDate);
        courseData = new CourseData(this);
        courseData.open();
        term = getIntent().getParcelableExtra("Term");
        txtTerm.setText(term.getTitle());

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
        String startDate = txtStartDate.getText().toString();
        String endDate = txtEndDate.getText().toString();
        String status = cboStatus.getSelectedItem().toString();
        String mentorName = txtMentorName.getText().toString();
        String mentorPhone = txtMentorPhone.getText().toString();
        String mentorEmail = txtMentorEmail.getText().toString();
        String notes = txtNotes.getText().toString();
        courseData.createCourse(title, startDate, endDate, status,
                mentorName, mentorPhone, mentorEmail, notes, term.getId());
        setResult(ViewHelper.DATA_SET_CHANGED);
        finish();
    }

    @Override
    protected void onResume() {
        courseData.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        courseData.close();
        super.onPause();
    }
}
