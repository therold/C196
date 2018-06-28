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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import herold.wgucalendar.R;
import herold.wgucalendar.data.CourseData;
import herold.wgucalendar.data.TermData;
import herold.wgucalendar.model.Course;
import herold.wgucalendar.model.Term;

public class ViewTermActivity extends AppCompatActivity {
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
        ViewHelper.setupToolbar(this, toolbar, R.string.view_term);

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

        courseData = new CourseData(this);
        courseData.open();
        courses = courseData.findByTerm(term.getId());
        adapter = new CourseAdapter(this, courses);
        lvCourses.setAdapter(adapter);
        AdapterView.OnItemClickListener lvListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView lv, View v, int position, long id) {
                Intent intent = new Intent(context, ViewCourseActivity.class);
                intent.putExtra("Course", (Course) lv.getItemAtPosition(position));
                startActivityForResult(intent, 0);
            }
        };
        lvCourses.setOnItemClickListener(lvListener);
        NavigationView.OnNavigationItemSelectedListener navListener = new NavigationView.OnNavigationItemSelectedListener() {
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
        };
        navigationView.setNavigationItemSelectedListener(navListener);
        View.OnClickListener imgMenuListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) { drawerLayout.openDrawer(GravityCompat.END); }
        };
        imgMenu.setOnClickListener(imgMenuListener);
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

        ViewHelper.closeKeyboard(this);
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ViewHelper.DATA_SET_CHANGED) {
            courseData.open();
            courses = courseData.findByTerm(term.getId());
            adapter.clear();
            adapter.addAll(courses);
        }
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
