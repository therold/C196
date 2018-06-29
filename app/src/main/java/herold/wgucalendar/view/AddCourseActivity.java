package herold.wgucalendar.view;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import herold.wgucalendar.R;
import herold.wgucalendar.data.CourseData;
import herold.wgucalendar.data.TermData;
import herold.wgucalendar.model.Term;

public class AddCourseActivity extends AppCompatActivity {
    private Button btnSave;
    private CourseData courseData;
    private DrawerLayout drawerLayout;
    private EditText txtTitle;
    private EditText txtStartDate;
    private EditText txtEndDate;
    private EditText txtMentorName;
    private EditText txtMentorPhone;
    private EditText txtMentorEmail;
    private EditText txtNotes;
    private List<Term> terms;
    private NavigationView navigationView;
    private ScrollView scrollView;
    private Spinner cboStatus;
    private Spinner cboTerm;
    private Term term;
    private TermData termData;
    private TermSpinnerAdapter adpTerm;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        cboTerm = findViewById(R.id.cboTerm);
        txtTitle = findViewById(R.id.txtTitle);
        txtStartDate = findViewById(R.id.txtStartDate);
        txtEndDate = findViewById(R.id.txtEndDate);
        cboStatus = findViewById(R.id.cboStatus);
        txtMentorName = findViewById(R.id.txtMentorName);
        txtMentorPhone = findViewById(R.id.txtMentorPhone);
        txtMentorEmail = findViewById(R.id.txtMentorEmail);
        txtNotes = findViewById(R.id.txtNotes);
        btnSave = findViewById(R.id.btnSave);
        scrollView = findViewById(R.id.scrollView);

        ViewHelper.setupDateInput(this, txtStartDate);
        ViewHelper.setupDateInput(this, txtEndDate);
        courseData = new CourseData(this);
        courseData.open();


        term = getIntent().getParcelableExtra("Term");
        termData = new TermData(this);
        termData.open();
        terms = termData.all();
        adpTerm = new TermSpinnerAdapter(this, android.R.layout.simple_spinner_item, terms);
        cboTerm.setAdapter(adpTerm);
        if (term != null) {
            cboTerm.setSelection(terms.indexOf(term));
        }

        txtNotes.setOnTouchListener(ViewHelper.scrollInsideScrollview(scrollView));
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

        TextView txtTitle = ViewHelper.getTitleText(this, R.string.add_course);
        toolbar.addView(txtTitle);
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
        long termId = ((Term) cboTerm.getSelectedItem()).getId();
        courseData.createCourse(title, startDate, endDate, status,
                mentorName, mentorPhone, mentorEmail, notes, termId);
        setResult(ViewHelper.DATA_SET_CHANGED);
        finish();
    }

    private void cancel() {
        finish();
    }

    @Override
    protected void onResume() {
        courseData.open();
        termData.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        courseData.close();
        termData.close();
        super.onPause();
    }
}
