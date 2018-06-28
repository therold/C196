package herold.wgucalendar.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import herold.wgucalendar.R;
import herold.wgucalendar.data.CourseData;
import herold.wgucalendar.model.Assessment;
import herold.wgucalendar.model.Course;

public class ViewCourseActivity extends AppCompatActivity {
    private Context context = this;
    private Course course;
    private CourseAdapter adapter;
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
    private LinearLayout cntLayout;
    private LinearLayout buttonBar;
    private List<Assessment> assessments;
    private List<View> inputs;
    private ListView lvAssessments;
    private NavigationView navigationView;
    private Spinner cboStatus;
    private String oTerm;
    private String oTitle;
    private String oStart;
    private String oEnd;
    private String oStatus;
    private String oMentorName;
    private String oMentorPhone;
    private String oMentorEmail;
    private String oNotes;
    private TextView lblAssessment;
    private Toolbar toolbar;

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
        ViewHelper.setupToolbar(this, toolbar, R.string.view_course);

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

        course = getIntent().getParcelableExtra("Course");
        courseData = new CourseData(this);
        courseData.open();

        cboStatus.setSelection(getIndex(cboStatus, course.getStatus()));
        txtTerm.setText(Long.toString(course.getTermId()));
        txtTitle.setText(course.getTitle());
        txtStartDate.setText(course.getStart());
        txtEndDate.setText(course.getEnd());
        txtMentorName.setText(course.getMentorName());
        txtMentorPhone.setText(course.getMentorPhone());
        txtMentorEmail.setText(course.getMentorEmail());
        txtNotes.setText(course.getNotes());

        NavigationView.OnNavigationItemSelectedListener navListener = new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.nav_add_assessment:
                        addAssessment();
                        break;
                    case R.id.nav_edit_course:
                        editCourse();
                        break;
                    case R.id.nav_delete_course:
                        deleteCourse(course);
                        break;
                }
                return true;
            }
        };
        navigationView.setNavigationItemSelectedListener(navListener);
        View.OnClickListener menuClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) { drawerLayout.openDrawer(GravityCompat.END); }
        };
        imgMenu.setOnClickListener(menuClickListener);

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

    private void addAssessment() {
        Intent intent = new Intent(context, AddAssessmentActivity.class);
        intent.putExtra("Course", course);
        startActivity(intent);
    }

    private void deleteCourse(Course course) {
        // TODO check if Course contains any Assessment
        if (false) {
            // has terms, show alert
        } else {
            courseData.deleteCourse(course);
            setResult(ViewHelper.DATA_SET_CHANGED);
            finish();
        }
    }

    private void editCourse() {
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
        ViewHelper.setupDateInput(this, txtStartDate);
        ViewHelper.setupDateInput(this, txtEndDate);

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
        ViewHelper.closeKeyboard(this);
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
