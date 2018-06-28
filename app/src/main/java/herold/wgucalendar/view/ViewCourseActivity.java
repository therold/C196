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
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import herold.wgucalendar.R;
import herold.wgucalendar.data.CourseData;
import herold.wgucalendar.model.Assessment;
import herold.wgucalendar.model.Course;

public class ViewCourseActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Context context = this;
    private String oTerm;
    private String oTitle;
    private String oStart;
    private String oEnd;
    private String oStatus;
    private String oMentorName;
    private String oMentorPhone;
    private String oMentorEmail;
    private String oNotes;
    private Calendar startDate = Calendar.getInstance();
    private Calendar endDate = Calendar.getInstance();
    private EditText txtTerm;
    private EditText txtTitle;
    private EditText txtStartDate;
    private EditText txtEndDate;
    private EditText txtMentorName;
    private EditText txtMentorPhone;
    private EditText txtMentorEmail;
    private EditText txtNotes;
    private Spinner cboStatus;
    private LinearLayout cntLayout;
    private LinearLayout buttonBar;
    private NavigationView navigationView;
    private TextView lblAssessment;
    private ListView lvAssessments;
    private List<Assessment> assessments;
    private ImageView imgMenu;
    private List<View> inputs;
    private Toolbar toolbar;
    private Course course;
    private CourseData courseData;
    private CourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course);

        lvAssessments = findViewById(R.id.lvAssessments);
        lblAssessment = findViewById(R.id.lblAssessment);
        cboStatus = findViewById(R.id.cboStatus);
        txtTerm = findViewById(R.id.txtTerm);
        txtTitle = findViewById(R.id.txtTitle);
        txtStartDate = findViewById(R.id.txtTermStartDate);
        txtEndDate = findViewById(R.id.txtTermEndDate);
        txtMentorName = findViewById(R.id.txtMentorName);
        txtMentorPhone = findViewById(R.id.txtMentorPhone);
        txtMentorEmail = findViewById(R.id.txtMentorEmail);
        txtNotes = findViewById(R.id.txtNotes);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        imgMenu = findViewById(R.id.imgMenu);
        cntLayout = findViewById(R.id.cntLayout);
        toolbar = findViewById(R.id.toolbar);

        course = getIntent().getParcelableExtra("Course");
        courseData = new CourseData(this);
        courseData.open();


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.view_course);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener( new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.nav_add_assessment:
                        tryAddAssessment();
                        break;
                    case R.id.nav_edit_course:
                        tryEditCourse();
                        break;
                    case R.id.nav_delete_course:
                        tryDeleteCourse(course);
                        break;
                }
                return true;
            }
        });

        View.OnClickListener menuClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) { drawerLayout.openDrawer(GravityCompat.END); }
        };
        imgMenu.setOnClickListener(menuClickListener);

        inputs = new ArrayList<>();
        inputs.add(cboStatus);
        inputs.add(txtTerm);
        inputs.add(txtTitle);
        inputs.add(txtStartDate);
        inputs.add(txtEndDate);
        inputs.add(txtMentorName);
        inputs.add(txtMentorPhone);
        inputs.add(txtMentorEmail);
        inputs.add(txtNotes);
        for(View input : inputs) { input.setEnabled(false); }

        cboStatus.setSelection(getIndex(cboStatus, course.getStatus()));
        txtTerm.setText(Long.toString(course.getTermId()));
        txtTitle.setText(course.getTitle());
        txtStartDate.setText(course.getStart());
        txtEndDate.setText(course.getEnd());
        txtMentorName.setText(course.getMentorName());
        txtMentorPhone.setText(course.getMentorPhone());
        txtMentorEmail.setText(course.getMentorEmail());
        txtNotes.setText(course.getNotes());

        // TODO add support for assessments
//        courseData = new CourseData(this);
//        courseData.open();
//        lvCourses = findViewById(R.id.lvCourses);
//        courses = courseData.findByTerm(term.getId());
//        adapter = new CourseAdapter(this, courses);
//        lvCourses.setAdapter(adapter);
//        lvCourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView lv, View v, int position, long id) {
//                Intent intent = new Intent(context, ViewCourseActivity.class);
//                intent.putExtra("Course", (Course) lv.getItemAtPosition(position));
//                startActivityForResult(intent, 0);
//            }
//        });
    }

    private void updateLabel(EditText e, Calendar c) {
        String myFormat = getResources().getString(R.string.date_format);
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        e.setText(sdf.format(c.getTime()));
    }

    private void tryAddAssessment() {
//        Intent intent = new Intent(context, AddAssessmentActivity.class);
//        intent.putExtra("Term", term);
//        startActivityForResult(intent, 0);
    }

    private void tryDeleteCourse(Course course) {
        // TODO check if Course contains any Assessment
        if (false) {
            // has terms, show alert
        } else {
            courseData.deleteCourse(course);
            setResult(ViewHelper.DATA_SET_CHANGED);
            finish();
        }
    }

    private void tryEditCourse() {
        oTerm = txtTerm.getText().toString();
        oTitle = txtTitle.getText().toString();
        oStart = txtStartDate.getText().toString();
        oEnd = txtEndDate.getText().toString();
        oStatus = cboStatus.getSelectedItem().toString();
        oMentorName = txtMentorName.getText().toString();
        oMentorPhone = txtMentorPhone.getText().toString();
        oMentorEmail = txtMentorPhone.getText().toString();
        oNotes = txtNotes.getText().toString();

        for(View input : inputs) { input.setEnabled(true); }
        for(View input : inputs) { input.setFocusable(true); }
        for(View input : inputs) { input.setFocusableInTouchMode(true); }
        txtStartDate.setFocusableInTouchMode(false);
        txtEndDate.setFocusableInTouchMode(false);
        txtStartDate.setFocusable(false);
        txtEndDate.setFocusable(false);
        lblAssessment.setVisibility(View.GONE);
        lvAssessments.setVisibility(View.GONE);
        imgMenu.setEnabled(false);
        imgMenu.setVisibility(View.GONE);

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
        buttonBar = new LinearLayout(context);
        Button btnSave = new Button(context);
        btnSave.setText(R.string.save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { saveUpdate(); }
        });
        Button btnCancel = new Button(context);
        btnCancel.setText(R.string.cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { cancelUpdate(); }
        });
        buttonBar.setOrientation(LinearLayout.HORIZONTAL);
        buttonBar.addView(btnSave);
        buttonBar.addView(btnCancel);
        cntLayout.addView(buttonBar);
    }

    private void saveUpdate() {
        course.setTitle(txtTitle.getText().toString());
        course.setStart(txtStartDate.getText().toString());
        course.setEnd(txtEndDate.getText().toString());
        course.setStatus(cboStatus.getSelectedItem().toString());
        course.setMentorName(txtMentorName.getText().toString());
        course.setMentorPhone(txtMentorPhone.getText().toString());
        course.setMentorEmail(txtMentorEmail.getText().toString());
        course.setNotes(txtNotes.getText().toString());
        courseData.updateCourse(course);
        setResult(ViewHelper.DATA_SET_CHANGED);
        finish();
    }

    private void cancelUpdate() {
        for(View input : inputs) { input.setEnabled(false); }
        for(View input : inputs) { input.setFocusable(false); }
        for(View input : inputs) { input.setFocusableInTouchMode(false); }
        lblAssessment.setVisibility(View.VISIBLE);
        lvAssessments.setVisibility(View.VISIBLE);
        imgMenu.setEnabled(true);
        imgMenu.setVisibility(View.VISIBLE);
        txtStartDate.setOnClickListener(null);
        txtEndDate.setOnClickListener(null);
        cntLayout.removeView(buttonBar);

        txtTerm.setText(oTerm);
        txtTitle.setText(oTitle);
        txtStartDate.setText(oStart);
        txtEndDate.setText(oEnd);
        cboStatus.setSelection(getIndex(cboStatus, oStatus));
        txtMentorName.setText(oMentorName);
        txtMentorPhone.setText(oMentorPhone);
        txtMentorEmail.setText(oMentorEmail);
        txtNotes.setText(oNotes);

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }
}
