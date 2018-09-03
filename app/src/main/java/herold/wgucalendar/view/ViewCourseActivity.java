package herold.wgucalendar.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import herold.wgucalendar.R;
import herold.wgucalendar.data.AssessmentData;
import herold.wgucalendar.data.CourseData;
import herold.wgucalendar.data.DBHelper;
import herold.wgucalendar.data.TermData;
import herold.wgucalendar.model.Assessment;
import herold.wgucalendar.model.Course;
import herold.wgucalendar.model.Term;

public class ViewCourseActivity extends AppCompatActivity {
    private Activity activity = this;
    private AssessmentAdapter adapter;
    private AssessmentData assessmentData;
    private Button btnSave;
    private Context context = this;
    private Course course;
    private CourseData courseData;
    private long courseId;
    private DrawerLayout drawerLayout;
    private EditText txtTitle;
    private EditText txtStartDate;
    private EditText txtEndDate;
    private EditText txtMentorName;
    private EditText txtMentorPhone;
    private EditText txtMentorEmail;
    private EditText txtNotes;
    private ImageView imgMenu;
    private List<Assessment> assessments;
    private List<Term> terms;
    private List<View> inputs;
    private ListView lvAssessments;
    private NavigationView navigationView;
    private ScrollView scrollView;
    private SharedPreferences sharedPref;
    private Spinner cboStatus;
    private Spinner cboTerm;
    private String oTitle;
    private String oStart;
    private String oEnd;
    private String oStatus;
    private String oMentorName;
    private String oMentorPhone;
    private String oMentorEmail;
    private String oNotes;
    private Switch swStart;
    private Switch swEnd;
    private Term term;
    private Term oTerm;
    private TermData termData;
    private TermSpinnerAdapter adpTerm;
    private TextView lblAssessment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course);

        lvAssessments = findViewById(R.id.lvAssessments);
        lblAssessment = findViewById(R.id.lblAssessment);
        cboStatus = findViewById(R.id.cboStatus);
        cboTerm = findViewById(R.id.cboTerm);
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
        toolbar = findViewById(R.id.toolbar);
        swStart = findViewById(R.id.swStart);
        swEnd = findViewById(R.id.swEnd);
        scrollView = findViewById(R.id.scrollView);
        ViewHelper.setupToolbar(this, toolbar, R.string.view_course);
        txtNotes.setOnTouchListener(ViewHelper.scrollInsideScrollview(scrollView));

        inputs = new ArrayList<>();
        inputs.add(cboStatus);
        inputs.add(cboTerm);
        inputs.add(txtTitle);
        inputs.add(txtStartDate);
        inputs.add(txtEndDate);
        inputs.add(txtMentorName);
        inputs.add(txtMentorPhone);
        inputs.add(txtMentorEmail);
        inputs.add(txtNotes);
        ViewHelper.disableInput(inputs);

        termData = new TermData(this);
        courseData = new CourseData(this);
        assessmentData = new AssessmentData(this);
        courseId = getIntent().getLongExtra("CourseId", 0);
        assessments = new ArrayList<>();
        adapter = new AssessmentAdapter(this, assessments);
        btnSave = new Button(context);
        loadData();

        sharedPref = getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);
        swStart.setChecked(sharedPref.getBoolean(Integer.toString(course.getStartId()), false));
        swEnd.setChecked(sharedPref.getBoolean(Integer.toString(course.getEndId()), false));
        swStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ViewHelper.setAlarm(activity, course.getStart(), course.getStartMessage(), course.getStartId(), b);
            }
        });
        swEnd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ViewHelper.setAlarm(activity, course.getEnd(), course.getEndMessage(), course.getEndId(), b);
            }
        });

        lvAssessments.setAdapter(adapter);
        AdapterView.OnItemClickListener lvListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView lv, View v, int position, long id) {
                viewAssessment((Assessment) lv.getItemAtPosition(position));
            }
        };
        lvAssessments.setOnItemClickListener(lvListener);
        ViewHelper.setListViewHeight(lvAssessments);
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
                        deleteCourse();
                        break;
                    case R.id.nav_share_notes:
                        shareNotes();
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
        ViewHelper.scrollToTop(scrollView);

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


    private void addAssessment() {
        Intent intent = new Intent(context, AddAssessmentActivity.class);
        intent.putExtra("Course", course);
        startActivity(intent);
    }

    private void viewAssessment(Assessment assessment) {
        Intent intent = new Intent(context, ViewAssessmentActivity.class);
        intent.putExtra("Assessment", assessment);
        startActivity(intent);
    }

    private void deleteCourse() {
        if (assessments.size() > 0) {
            AlertDialog.Builder builder = ViewHelper.getDialog(context, R.string.error_has_children_course,
                    R.string.error_has_children_course_message);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            });
            builder.show();
        } else {
            AlertDialog.Builder builder = ViewHelper.getDialog(context, R.string.confirm_delete,
                    R.string.confirm_delete_message);
            final Course courseToDelete = course;
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ViewHelper.setAlarm(activity, course.getStart(), course.getStartMessage(), course.getStartId(), false);
                    ViewHelper.setAlarm(activity, course.getEnd(), course.getEndMessage(), course.getEndId(), false);
                    courseData.deleteCourse(courseToDelete);
                    finish();
                }
            });
            builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {}
            });
            builder.show();
        }
    }

    private void editCourse() {
        oTerm = (Term) cboTerm.getSelectedItem();
        oTitle = txtTitle.getText().toString();
        oStart = txtStartDate.getText().toString();
        oEnd = txtEndDate.getText().toString();
        oStatus = cboStatus.getSelectedItem().toString();
        oMentorName = txtMentorName.getText().toString();
        oMentorPhone = txtMentorPhone.getText().toString();
        oMentorEmail = txtMentorPhone.getText().toString();
        oNotes = txtNotes.getText().toString();

        ViewHelper.enableInput(inputs);
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.removeAllViews();
        btnSave = new Button(context);
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

        TextView txtTitle = ViewHelper.getTitleText(this, R.string.edit_course);
        toolbar.addView(txtTitle);
    }

    private void saveUpdate() {
        course.setTermId(((Term) cboTerm.getSelectedItem()).getId());
        course.setTitle(txtTitle.getText().toString());
        course.setStart(DBHelper.stringToTimestamp(txtStartDate.getText().toString()));
        course.setEnd(DBHelper.stringToTimestamp(txtEndDate.getText().toString()));
        course.setStatus(cboStatus.getSelectedItem().toString());
        course.setMentorName(txtMentorName.getText().toString());
        course.setMentorPhone(txtMentorPhone.getText().toString());
        course.setMentorEmail(txtMentorEmail.getText().toString());
        course.setNotes(txtNotes.getText().toString());
        ViewHelper.setAlarm(activity, course.getStart(), course.getStartMessage(), course.getStartId(), swStart.isChecked());
        ViewHelper.setAlarm(activity, course.getEnd(), course.getEndMessage(), course.getEndId(), swEnd.isChecked());
        courseData.updateCourse(course);
        finish();
    }

    private void cancelUpdate() {
        toolbar.removeAllViews();
        ViewHelper.setupToolbar(this, toolbar, R.string.view_course);
        toolbar.addView(imgMenu);

        ViewHelper.disableInput(inputs);
        lblAssessment.setVisibility(View.VISIBLE);
        lvAssessments.setVisibility(View.VISIBLE);
        imgMenu.setEnabled(true);
        imgMenu.setVisibility(View.VISIBLE);
        txtStartDate.setOnClickListener(null);
        txtEndDate.setOnClickListener(null);

        cboTerm.setSelection(terms.indexOf(oTerm));
        txtTitle.setText(oTitle);
        txtStartDate.setText(oStart);
        txtEndDate.setText(oEnd);
        cboStatus.setSelection(getIndex(cboStatus, oStatus));
        txtMentorName.setText(oMentorName);
        txtMentorPhone.setText(oMentorPhone);
        txtMentorEmail.setText(oMentorEmail);
        txtNotes.setText(oNotes);
        ViewHelper.closeKeyboard(this);
        ViewHelper.scrollToTop(scrollView);
    }

    private void loadData() {
        courseData.open();
        course = courseData.get(courseId);
        cboStatus.setSelection(getIndex(cboStatus, course.getStatus()));
        txtTitle.setText(course.getTitle());
        txtStartDate.setText(course.getStartDisplay());
        txtEndDate.setText(course.getEndDisplay());
        txtMentorName.setText(course.getMentorName());
        txtMentorPhone.setText(course.getMentorPhone());
        txtMentorEmail.setText(course.getMentorEmail());
        txtNotes.setText(course.getNotes());

        termData.open();
        term = termData.get(course.getTermId());
        terms = termData.all();
        adpTerm = new TermSpinnerAdapter(this, android.R.layout.simple_spinner_item, terms);
        cboTerm.setAdapter(adpTerm);
        cboTerm.setSelection(terms.indexOf(term));

        assessmentData.open();
        assessments = assessmentData.findByCourse(course.getId());
        adapter.clear();
        adapter.addAll(assessments);

        adapter.notifyDataSetChanged();
    }

    private void shareNotes() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, course.getTitle() + " Notes:" + "\n" + course.getNotes());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Send " + course.getTitle() + " Notes To"));
    }

    @Override
    protected void onResume() {
        loadData();
        ViewHelper.setListViewHeight(lvAssessments);
        ViewHelper.scrollToTop(scrollView);
        super.onResume();
    }

    @Override
    protected void onPause() {
        termData.close();
        courseData.close();
        assessmentData.close();
        super.onPause();
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
