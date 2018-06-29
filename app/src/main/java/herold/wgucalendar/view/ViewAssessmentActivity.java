package herold.wgucalendar.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import herold.wgucalendar.R;
import herold.wgucalendar.data.AssessmentData;
import herold.wgucalendar.data.CourseData;
import herold.wgucalendar.model.Assessment;
import herold.wgucalendar.model.Course;

public class ViewAssessmentActivity extends AppCompatActivity {
    private Assessment assessment;
    private AssessmentData assessmentData;
    private Context context = this;
    private Course course;
    private Course oCourse;
    private CourseData courseData;
    private CourseSpinnerAdapter adpCourse;
    private DrawerLayout drawerLayout;
    private EditText txtTitle;
    private EditText txtDueDate;
    private EditText txtNotes;
    private ImageView imgMenu;
    private List<Course> courses;
    private List<View> inputs;
    private long courseId;
    private NavigationView navigationView;
    private ScrollView scrollView;
    private Spinner cboCourse;
    private Spinner cboType;
    private String oTitle;
    private String oDueDate;
    private String oType;
    private String oNotes;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assessment);

        cboType = findViewById(R.id.cboType);
        cboCourse = findViewById(R.id.cboCourse);
        txtTitle = findViewById(R.id.txtTitle);
        txtDueDate = findViewById(R.id.txtDueDate);
        txtNotes = findViewById(R.id.txtNotes);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        scrollView = findViewById(R.id.scrollView);
        imgMenu = findViewById(R.id.imgMenu);
        toolbar = findViewById(R.id.toolbar);
        ViewHelper.setupToolbar(this, toolbar, R.string.view_assessment);

        inputs = new ArrayList<>();
        inputs.add(cboType);
        inputs.add(cboCourse);
        inputs.add(txtTitle);
        inputs.add(txtDueDate);
        inputs.add(txtNotes);
        ViewHelper.disableInput(inputs);

        courseData = new CourseData(this);
        courseData.open();
        courseId = getIntent().getLongExtra("CourseId", 0);
        course = courseData.get(courseId);
        courses = courseData.all();
        adpCourse = new CourseSpinnerAdapter(this, android.R.layout.simple_spinner_item, courses);
        cboCourse.setAdapter(adpCourse);
        cboCourse.setSelection(courses.indexOf(course));
        assessment = getIntent().getParcelableExtra("Assessment");
        assessmentData = new AssessmentData(this);
        assessmentData.open();

        cboType.setSelection(getIndex(cboType, assessment.getType()));
        txtTitle.setText(assessment.getTitle());
        txtDueDate.setText(assessment.getDueDate());
        txtNotes.setText(course.getNotes());

        txtNotes.setOnTouchListener(ViewHelper.scrollInsideScrollview(scrollView));

        NavigationView.OnNavigationItemSelectedListener navListener = new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.nav_edit_assessment:
                        editAssessment();
                        break;
                    case R.id.nav_delete_assessment:
                        deleteAssessment();
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
    }

    private void deleteAssessment() {
        AlertDialog.Builder builder = ViewHelper.getDialog(context, R.string.confirm_delete,
                R.string.confirm_delete_message);
        final Assessment assessmentToDelete = assessment;
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                assessmentData.deleteAssessment(assessmentToDelete);
                finish();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {}
        });
        builder.show();
    }

    private void editAssessment() {
        oCourse = (Course) cboCourse.getSelectedItem();
        oTitle = txtTitle.getText().toString();
        oDueDate = txtDueDate.getText().toString();
        oType = cboType.getSelectedItem().toString();
        oNotes = txtNotes.getText().toString();

        ViewHelper.enableInput(inputs);
        txtDueDate.setFocusableInTouchMode(false);
        txtDueDate.setFocusable(false);
        imgMenu.setEnabled(false);
        imgMenu.setVisibility(View.GONE);
        ViewHelper.setupDateInput(this, txtDueDate);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.removeAllViews();
        Button btnSave = new Button(context);
        btnSave.setText(R.string.save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { saveUpdate(); }
        });
        btnSave.setLayoutParams(ViewHelper.rightLayout());
        toolbar.addView(btnSave);

        Button btnCancel = new Button(context);
        btnCancel.setText(R.string.cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { cancelUpdate(); }
        });
        btnCancel.setLayoutParams(ViewHelper.leftLayout());
        toolbar.addView(btnCancel);

        TextView txtTitle = ViewHelper.getTitleText(this, R.string.edit_assessment);
        toolbar.addView(txtTitle);
    }

    private void saveUpdate() {
        assessment.setCourseId(((Course) cboCourse.getSelectedItem()).getId());
        assessment.setTitle(txtTitle.getText().toString());
        assessment.setDueDate(txtDueDate.getText().toString());
        assessment.setType(cboType.getSelectedItem().toString());
        assessmentData.updateAssessment(assessment);
        course.setNotes(txtNotes.getText().toString());
        courseData.updateCourse(course);
        finish();
    }

    private void cancelUpdate() {
        toolbar.removeAllViews();
        ViewHelper.setupToolbar(this, toolbar, R.string.view_assessment);
        toolbar.addView(imgMenu);

        ViewHelper.disableInput(inputs);
        imgMenu.setEnabled(true);
        imgMenu.setVisibility(View.VISIBLE);
        txtDueDate.setOnClickListener(null);

        cboCourse.setSelection(courses.indexOf(oCourse));
        txtTitle.setText(oTitle);
        txtDueDate.setText(oDueDate);
        txtNotes.setText(oNotes);
        cboType.setSelection(getIndex(cboType, oType));
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
