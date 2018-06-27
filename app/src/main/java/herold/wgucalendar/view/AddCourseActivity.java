package herold.wgucalendar.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import herold.wgucalendar.R;
import herold.wgucalendar.data.CourseData;
import herold.wgucalendar.model.Term;

public class AddCourseActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Context context = this;
    private CourseData courseData;
    private Calendar startDate = Calendar.getInstance();
    private Calendar endDate = Calendar.getInstance();
    private Term term;
    private EditText txtTerm;
    private EditText txtTitle;
    private EditText txtStartDate;
    private EditText txtEndDate;
    private Spinner cboStatus;
    private EditText txtMentorName;
    private EditText txtMentorPhone;
    private EditText txtMentorEmail;
    private EditText txtNotes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.add_course);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(ViewHelper.getNavigationListener(context, drawerLayout));
        ImageView imgMenu = findViewById(R.id.imgMenu);
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { drawerLayout.openDrawer(GravityCompat.START); }
        });

        txtTerm = findViewById(R.id.txtTerm);
        txtTitle = findViewById(R.id.txtTitle);
        txtStartDate = findViewById(R.id.txtStartDate);
        txtEndDate = findViewById(R.id.txtEndDate);
        cboStatus = findViewById(R.id.cboStatus);
        txtMentorName = findViewById(R.id.txtMentorName);
        txtMentorPhone = findViewById(R.id.txtMentorPhone);
        txtMentorEmail = findViewById(R.id.txtMentorEmail);
        txtNotes = findViewById(R.id.txtNotes);
        Button btnSave = findViewById(R.id.btnSave);
        courseData = new CourseData(this);
        courseData.open();

        DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                startDate.set(Calendar.YEAR, year);
                startDate.set(Calendar.MONTH, monthOfYear);
                startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(txtStartDate, startDate);
            }
        };

        DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                endDate.set(Calendar.YEAR, year);
                endDate.set(Calendar.MONTH, monthOfYear);
                endDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(txtEndDate, endDate);
            }
        };

        final DatePickerDialog startDatePickerDialog = new DatePickerDialog(
                this, startDateListener, startDate.get(Calendar.YEAR),
                startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));

        final DatePickerDialog endDatePickerDialog = new DatePickerDialog(
                this, endDateListener,  endDate.get(Calendar.YEAR),
                endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH));

        txtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startDatePickerDialog.show(); }
        });
        txtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { endDatePickerDialog.show(); }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
        term = getIntent().getParcelableExtra("Term");
        txtTerm.setText(term.getTitle());
    }

    private void updateLabel(EditText e, Calendar c) {
        String myFormat = getResources().getString(R.string.date_format);
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        e.setText(sdf.format(c.getTime()));
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
