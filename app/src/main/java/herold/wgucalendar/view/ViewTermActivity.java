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
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import herold.wgucalendar.R;
import herold.wgucalendar.data.CourseData;
import herold.wgucalendar.data.DBHelper;
import herold.wgucalendar.data.TermData;
import herold.wgucalendar.model.Course;
import herold.wgucalendar.model.Term;

public class ViewTermActivity extends AppCompatActivity {
    private final Activity activity = this;
    private Button btnSave;
    private Context context = this;
    private CourseAdapter adapter;
    private CourseData courseData;
    private DrawerLayout drawerLayout;
    private EditText txtTermTitle;
    private EditText txtStartDate;
    private EditText txtEndDate;
    private ImageView imgMenu;
    private List<Course> courses;
    private List<View> inputs;
    private ListView lvCourses;
    private NavigationView navigationView;
    private ScrollView scrollView;
    private SharedPreferences sharedPref;
    private String oTitle;
    private String oStart;
    private String oEnd;
    private Switch swStart;
    private Switch swEnd;
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
        lblCourses = findViewById(R.id.lblCourses);
        swStart = findViewById(R.id.swStart);
        swEnd = findViewById(R.id.swEnd);
        toolbar = findViewById(R.id.toolbar);
        scrollView = findViewById(R.id.scrollView);
        ViewHelper.setupToolbar(this, toolbar, R.string.view_term);

        inputs = new ArrayList<>();
        inputs.add(txtTermTitle);
        inputs.add(txtStartDate);
        inputs.add(txtEndDate);
        ViewHelper.disableInput(inputs);

        termData = new TermData(this);
        termData.open();
        term = getIntent().getParcelableExtra("Term");
        txtTermTitle.setText(term.getTitle());
        txtStartDate.setText(term.getStartDisplay());
        txtEndDate.setText(term.getEndDisplay());

        sharedPref = getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);
        swStart.setChecked(sharedPref.getBoolean(Integer.toString(term.getStartId()), false));
        swEnd.setChecked(sharedPref.getBoolean(Integer.toString(term.getEndId()), false));
        swStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ViewHelper.setAlarm(activity, term.getStart(), term.getStartMessage(), term.getStartId(), b);
            }
        });
        swEnd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ViewHelper.setAlarm(activity, term.getEnd(), term.getEndMessage(), term.getEndId(), b);
            }
        });

        courseData = new CourseData(this);
        courseData.open();
        courses = courseData.findByTerm(term.getId());
        adapter = new CourseAdapter(this, courses);
        lvCourses.setAdapter(adapter);
        AdapterView.OnItemClickListener lvListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView lv, View v, int position, long id) {
                viewCourse((Course) lv.getItemAtPosition(position));
            }
        };
        lvCourses.setOnItemClickListener(lvListener);
        ViewHelper.setListViewHeight(lvCourses);
        NavigationView.OnNavigationItemSelectedListener navListener = new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.nav_add_course:
                        addCourse();
                        break;
                    case R.id.nav_edit_term:
                        editTerm();
                        break;
                    case R.id.nav_delete_term:
                        deleteTerm();
                        break;
                }
                return true;
            }
        };
        navigationView.setNavigationItemSelectedListener(navListener);
        View.OnClickListener imgMenuListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) { drawerLayout.openDrawer(GravityCompat.END); }
        };
        imgMenu.setOnClickListener(imgMenuListener);
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

        txtTermTitle.addTextChangedListener(txtChanged);
        txtStartDate.addTextChangedListener(txtChanged);
        txtEndDate.addTextChangedListener(txtChanged);
    }

    private void canSave() {
        boolean title = (!txtTermTitle.getText().toString().isEmpty());
        boolean startDate = (!txtStartDate.getText().toString().isEmpty());
        boolean endDate = (!txtEndDate.getText().toString().isEmpty());
        if (title && startDate && endDate) {
            btnSave.setEnabled(true);
        } else {
            btnSave.setEnabled(false);
        }
    }

    private void addCourse() {
        Intent intent = new Intent(context, AddCourseActivity.class);
        intent.putExtra("Term", term);
        startActivity(intent);
    }

    private void viewCourse(Course course) {
        Intent intent = new Intent(context, ViewCourseActivity.class);
        intent.putExtra("CourseId", course.getId());
        startActivity(intent);
    }

    private void deleteTerm() {
        if (courses.size() > 0) {
            AlertDialog.Builder builder = ViewHelper.getDialog(context, R.string.error_has_children_term,
                    R.string.error_has_children_term_message);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            });
            builder.show();
        } else {
            AlertDialog.Builder builder = ViewHelper.getDialog(context, R.string.confirm_delete,
                    R.string.confirm_delete_message);
            final Term termToDelete = term;
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ViewHelper.setAlarm(activity, term.getStart(), term.getStartMessage(), term.getStartId(), false);
                    ViewHelper.setAlarm(activity, term.getEnd(), term.getEndMessage(), term.getEndId(), false);
                    termData.deleteTerm(termToDelete);
                    finish();
                }
            });
            builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {}
            });
            builder.show();
        }
    }

    private void editTerm() {
        oTitle = txtTermTitle.getText().toString();
        oStart = txtStartDate.getText().toString();
        oEnd = txtEndDate.getText().toString();
        ViewHelper.enableInput(inputs);
        txtStartDate.setFocusableInTouchMode(false);
        txtEndDate.setFocusableInTouchMode(false);
        txtStartDate.setFocusable(false);
        txtEndDate.setFocusable(false);
        lblCourses.setVisibility(View.GONE);
        lvCourses.setVisibility(View.GONE);
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

        TextView txtTitle = ViewHelper.getTitleText(this, R.string.edit_term);
        toolbar.addView(txtTitle);
    }

    private void saveUpdate() {
        term.setTitle(txtTermTitle.getText().toString());
        term.setStart(DBHelper.stringToTimestamp(txtStartDate.getText().toString()));
        term.setEnd(DBHelper.stringToTimestamp(txtEndDate.getText().toString()));
        termData.updateTerm(term);
        ViewHelper.setAlarm(activity, term.getStart(), term.getStartMessage(), term.getStartId(), swStart.isChecked());
        ViewHelper.setAlarm(activity, term.getEnd(), term.getEndMessage(), term.getEndId(), swEnd.isChecked());
        finish();
    }

    private void cancelUpdate() {
        toolbar.removeAllViews();
        ViewHelper.setupToolbar(this, toolbar, R.string.view_term);
        toolbar.addView(imgMenu);

        ViewHelper.disableInput(inputs);
        lblCourses.setVisibility(View.VISIBLE);
        lvCourses.setVisibility(View.VISIBLE);
        imgMenu.setEnabled(true);
        imgMenu.setVisibility(View.VISIBLE);
        txtStartDate.setOnClickListener(null);
        txtEndDate.setOnClickListener(null);

        txtTermTitle.setText(oTitle);
        txtStartDate.setText(oStart);
        txtEndDate.setText(oEnd);

        ViewHelper.closeKeyboard(this);
        ViewHelper.scrollToTop(scrollView);
    }

    @Override
    protected void onResume() {
        courseData.open();
        termData.open();
        courses = courseData.findByTerm(term.getId());
        adapter.clear();
        adapter.addAll(courses);
        adapter.notifyDataSetChanged();
        ViewHelper.setListViewHeight(lvCourses);
        ViewHelper.scrollToTop(scrollView);
        super.onResume();
    }

    @Override
    protected void onPause() {
        courseData.close();
        termData.close();
        super.onPause();
    }
}
