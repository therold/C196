package herold.wgucalendar.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import herold.wgucalendar.R;
import herold.wgucalendar.data.CourseData;
import herold.wgucalendar.data.TermData;
import herold.wgucalendar.model.Course;
import herold.wgucalendar.model.Term;

public class ViewTermActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Context context = this;
    private String origTitle;
    private String origStart;
    private String origEnd;
    private Calendar startDate = Calendar.getInstance();
    private Calendar endDate = Calendar.getInstance();
    private EditText txtTermTitle;
    private EditText txtStartDate;
    private EditText txtEndDate;
    private LinearLayout cntLayout;
    private LinearLayout buttonBar;
    private ListView lvCourses;
    private List<Course> courses;
    private Term term;
    private TermData termData;
    private CourseData courseData;
    private CourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_term);
        term = getIntent().getParcelableExtra("Term");
        termData = new TermData(this);
        termData.open();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.view_term);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener( new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.nav_add_course:
                        tryAddCourse();
                        break;
                    case R.id.nav_edit_term:
                        tryEditTerm();
                        break;
                    case R.id.nav_delete_term:
                        tryDeleteTerm(term);
                        break;
                }
                return true;
            }
        });
        ImageView imgMenu = findViewById(R.id.imgMenu);
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        txtTermTitle = findViewById(R.id.txtTermTitle);
        txtTermTitle.setBackgroundResource(android.R.drawable.edit_text);
        txtStartDate = findViewById(R.id.txtTermStartDate);
        txtEndDate = findViewById(R.id.txtTermEndDate);
        txtTermTitle.setText(term.getTitle());
        txtStartDate.setText(term.getStart());
        txtEndDate.setText(term.getEnd());
        cntLayout = findViewById(R.id.cntLayout);

        courseData = new CourseData(this);
        courseData.open();
        lvCourses = findViewById(R.id.lvCourses);
        courses = courseData.findByTerm(term.getId());
        adapter = new CourseAdapter(this, courses);
        lvCourses.setAdapter(adapter);
        lvCourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView lv, View v, int position, long id) {
                Intent intent = new Intent(context, ViewCourseActivity.class);
                intent.putExtra("Course", (Course) lv.getItemAtPosition(position));
                startActivityForResult(intent, 0);
            }
        });
    }

    private void updateLabel(EditText e, Calendar c) {
        String myFormat = getResources().getString(R.string.date_format);
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        e.setText(sdf.format(c.getTime()));
    }

    private void tryAddCourse() {
        Intent intent = new Intent(context, AddCourseActivity.class);
        intent.putExtra("Term", term);
        startActivityForResult(intent, 0);
    }

    private void tryDeleteTerm(Term term) {
        // TODO check if Term contains any Course
        if (false) {
            // has terms, show alert
        } else {
            termData.deleteTerm(term);
            setResult(ViewHelper.DATA_SET_CHANGED);
            finish();
        }
    }

    private void tryEditTerm() {
        origTitle = txtTermTitle.getText().toString();
        origStart = txtStartDate.getText().toString();
        origEnd = txtEndDate.getText().toString();
        txtTermTitle.setFocusableInTouchMode(true);
        txtTermTitle.setFocusable(true);
        txtTermTitle.setClickable(true);
        txtTermTitle.setBackgroundResource(android.R.drawable.edit_text);
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
            public void onClick(View view) {
                saveUpdate();
            }
        });
        Button btnCancel = new Button(context);
        btnCancel.setText(R.string.cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               cancelUpdate();
           }
        });
        buttonBar.setOrientation(LinearLayout.HORIZONTAL);
        buttonBar.addView(btnSave);
        buttonBar.addView(btnCancel);

        cntLayout.addView(buttonBar);
    }

    private void saveUpdate() {
        term.setTitle(txtTermTitle.getText().toString());
        term.setStart(txtStartDate.getText().toString());
        term.setEnd(txtEndDate.getText().toString());
        termData.updateTerm(term);
        setResult(ViewHelper.DATA_SET_CHANGED);
        finish();
    }

    private void cancelUpdate() {
        txtTermTitle.setText(origTitle);
        txtStartDate.setText(origStart);
        txtEndDate.setText(origEnd);
        txtTermTitle.setFocusableInTouchMode(false);
        txtTermTitle.setFocusable(false);
        txtTermTitle.setClickable(false);
        txtStartDate.setOnClickListener(null);
        txtEndDate.setOnClickListener(null);
        cntLayout.removeView(buttonBar);
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
