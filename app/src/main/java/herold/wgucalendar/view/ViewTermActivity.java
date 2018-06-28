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
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import herold.wgucalendar.R;
import herold.wgucalendar.data.CourseData;
import herold.wgucalendar.data.TermData;
import herold.wgucalendar.model.Course;
import herold.wgucalendar.model.Term;

public class ViewTermActivity extends AppCompatActivity {
    private Calendar startDate;
    private Calendar endDate;
    private Context context = this;
    private CourseAdapter adapter;
    private CourseData courseData;
    private DrawerLayout drawerLayout;
    private EditText txtTermTitle;
    private EditText txtStartDate;
    private EditText txtEndDate;
    private ImageView imgMenu;
    private LinearLayout cntLayout;
    private LinearLayout buttonBar;
    private List<Course> courses;
    private List<View> inputs;
    private ListView lvCourses;
    private NavigationView navigationView;
    private String oTitle;
    private String oStart;
    private String oEnd;
    private Term term;
    private TermData termData;
    private TextView lblCourses;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_term);

        txtTermTitle = findViewById(R.id.txtTermTitle);
        txtStartDate = findViewById(R.id.txtTermStartDate);
        txtEndDate = findViewById(R.id.txtTermEndDate);
        lvCourses = findViewById(R.id.lvCourses);
        imgMenu = findViewById(R.id.imgMenu);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        cntLayout = findViewById(R.id.cntLayout);
        lblCourses = findViewById(R.id.lblCourses);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.view_term);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

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
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { drawerLayout.openDrawer(GravityCompat.END); }
        });

        inputs = new ArrayList<>();
        inputs.add(txtTermTitle);
        inputs.add(txtStartDate);
        inputs.add(txtEndDate);
        for(View input : inputs) { input.setEnabled(false); }

        termData = new TermData(this);
        termData.open();
        term = getIntent().getParcelableExtra("Term");
        txtTermTitle.setText(term.getTitle());
        txtStartDate.setText(term.getStart());
        txtEndDate.setText(term.getEnd());
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();

        courseData = new CourseData(this);
        courseData.open();
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
        oTitle = txtTermTitle.getText().toString();
        oStart = txtStartDate.getText().toString();
        oEnd = txtEndDate.getText().toString();
        for(View input : inputs) { input.setEnabled(true); }
        for(View input : inputs) { input.setFocusable(true); }
        for(View input : inputs) { input.setFocusableInTouchMode(true); }
        txtStartDate.setFocusableInTouchMode(false);
        txtEndDate.setFocusableInTouchMode(false);
        txtStartDate.setFocusable(false);
        txtEndDate.setFocusable(false);
        lblCourses.setVisibility(View.GONE);
        lvCourses.setVisibility(View.GONE);
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
        for(View input : inputs) { input.setEnabled(false); }
        for(View input : inputs) { input.setFocusable(false); }
        for(View input : inputs) { input.setFocusableInTouchMode(false); }
        lblCourses.setVisibility(View.VISIBLE);
        lvCourses.setVisibility(View.VISIBLE);
        imgMenu.setEnabled(true);
        imgMenu.setVisibility(View.VISIBLE);
        txtStartDate.setOnClickListener(null);
        txtEndDate.setOnClickListener(null);
        cntLayout.removeView(buttonBar);

        txtTermTitle.setText(oTitle);
        txtStartDate.setText(oStart);
        txtEndDate.setText(oEnd);

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
