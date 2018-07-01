package herold.wgucalendar.view;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import herold.wgucalendar.R;
import herold.wgucalendar.data.CourseData;
import herold.wgucalendar.data.DBHelper;
import herold.wgucalendar.data.TermData;
import herold.wgucalendar.model.Course;
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
    private Switch swStart;
    private Switch swEnd;
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
        swStart = findViewById(R.id.swStart);
        swEnd = findViewById(R.id.swEnd);

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

        btnSave = new Button(this);
        btnSave.setText(R.string.save);
        btnSave.setEnabled(false);
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

        TextView txtActivityTitle = ViewHelper.getTitleText(this, R.string.add_course);
        toolbar.addView(txtActivityTitle);

        TextWatcher txtChanged = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) { canSave(); }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {}
        };

        txtTitle.addTextChangedListener(txtChanged);
        txtStartDate.addTextChangedListener(txtChanged);
        txtEndDate.addTextChangedListener(txtChanged);
        txtMentorName.addTextChangedListener(txtChanged);
        txtMentorPhone.addTextChangedListener(txtChanged);
        txtMentorEmail.addTextChangedListener(txtChanged);
    }

    private void canSave() {
        boolean title = (!txtTitle.getText().toString().isEmpty());
        boolean startDate = (!txtStartDate.getText().toString().isEmpty());
        boolean endDate = (!txtEndDate.getText().toString().isEmpty());
        boolean mentorName = (!txtMentorName.getText().toString().isEmpty());
        boolean mentorPhone = (!txtMentorPhone.getText().toString().isEmpty());
        boolean mentorEmail = (!txtMentorEmail.getText().toString().isEmpty());
        if (title && startDate && endDate && mentorName && mentorPhone && mentorEmail) {
            btnSave.setEnabled(true);
        } else {
            btnSave.setEnabled(false);
        }
    }

    private void save() {
        String title = txtTitle.getText().toString();
        long startDate = DBHelper.stringToTimestamp(txtStartDate.getText().toString());
        long endDate = DBHelper.stringToTimestamp(txtEndDate.getText().toString());
        String status = cboStatus.getSelectedItem().toString();
        String mentorName = txtMentorName.getText().toString();
        String mentorPhone = txtMentorPhone.getText().toString();
        String mentorEmail = txtMentorEmail.getText().toString();
        String notes = txtNotes.getText().toString();
        long termId = ((Term) cboTerm.getSelectedItem()).getId();
        int startId = 17 * (title.hashCode() + Long.hashCode(startDate));
        int endId = 17 * (title.hashCode() + Long.hashCode(endDate));
        boolean startEnabled = swStart.isChecked();
        boolean endEnabled = swEnd.isChecked();
        Course course = courseData.createCourse(title, startDate, endDate, status,
                mentorName, mentorPhone, mentorEmail, notes, termId, startId, endId);
        ViewHelper.setAlarm(this, startDate, course.getStartMessage(), startId, startEnabled);
        ViewHelper.setAlarm(this, endDate, course.getEndMessage(), endId, endEnabled);
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
