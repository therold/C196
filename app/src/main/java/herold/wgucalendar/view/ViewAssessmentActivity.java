package herold.wgucalendar.view;

import android.content.Context;
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
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import herold.wgucalendar.R;
import herold.wgucalendar.data.AssessmentData;
import herold.wgucalendar.model.Assessment;

public class ViewAssessmentActivity extends AppCompatActivity {
    private Assessment assessment;
    private AssessmentData assessmentData;
    private Context context = this;
    private DrawerLayout drawerLayout;
    private EditText txtCourse;
    private EditText txtTitle;
    private EditText txtDueDate;
    private ImageView imgMenu;
    private LinearLayout cntLayout;
    private LinearLayout buttonBar;
    private List<View> inputs;
    private NavigationView navigationView;
    private Spinner cboType;
    private String oCourse;
    private String oTitle;
    private String oDueDate;
    private String oType;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assessment);

        cboType = findViewById(R.id.cboType);
        txtCourse = findViewById(R.id.txtCourse);
        txtTitle = findViewById(R.id.txtTitle);
        txtDueDate = findViewById(R.id.txtDueDate);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        imgMenu = findViewById(R.id.imgMenu);
        cntLayout = findViewById(R.id.cntLayout);
        toolbar = findViewById(R.id.toolbar);
        ViewHelper.setupToolbar(this, toolbar, R.string.view_assessment);

        inputs = new ArrayList<>();
        inputs.add(cboType);
        inputs.add(txtCourse);
        inputs.add(txtTitle);
        inputs.add(txtDueDate);
        for(View input : inputs) { input.setEnabled(false); }

        assessment = getIntent().getParcelableExtra("Assessment");
        assessmentData = new AssessmentData(this);
        assessmentData.open();

        cboType.setSelection(getIndex(cboType, assessment.getType()));
        txtCourse.setText(Long.toString(assessment.getCourseId()));
        txtTitle.setText(assessment.getTitle());
        txtDueDate.setText(assessment.getDueDate());

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
        // TODO check if Course contains any Assessment
        if (false) {
            // has terms, show alert
        } else {
//            courseData.deleteCourse(course);
//            setResult(ViewHelper.DATA_SET_CHANGED);
//            finish();
        }
    }

    private void editAssessment() {
        oCourse = txtCourse.getText().toString();
        oTitle = txtTitle.getText().toString();
        oDueDate = txtDueDate.getText().toString();
        oType = cboType.getSelectedItem().toString();

        for(View input : inputs) { input.setEnabled(true); }
        for(View input : inputs) { input.setFocusable(true); }
        for(View input : inputs) { input.setFocusableInTouchMode(true); }
        txtDueDate.setFocusableInTouchMode(false);
        txtDueDate.setFocusable(false);
        imgMenu.setEnabled(false);
        imgMenu.setVisibility(View.GONE);
        ViewHelper.setupDateInput(this, txtDueDate);

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
        assessment.setTitle(txtTitle.getText().toString());
        assessment.setDueDate(txtDueDate.getText().toString());
        assessment.setType(cboType.getSelectedItem().toString());
        finish();
    }

    private void cancelUpdate() {
        for(View input : inputs) { input.setEnabled(false); }
        for(View input : inputs) { input.setFocusable(false); }
        for(View input : inputs) { input.setFocusableInTouchMode(false); }
        imgMenu.setEnabled(true);
        imgMenu.setVisibility(View.VISIBLE);
        txtDueDate.setOnClickListener(null);
        cntLayout.removeView(buttonBar);

        txtCourse.setText(oCourse);
        txtTitle.setText(oTitle);
        txtDueDate.setText(oDueDate);
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
