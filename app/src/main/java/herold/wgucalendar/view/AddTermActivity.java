package herold.wgucalendar.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import herold.wgucalendar.R;
import herold.wgucalendar.data.DBHelper;
import herold.wgucalendar.data.TermData;
import herold.wgucalendar.model.Term;

public class AddTermActivity extends AppCompatActivity {
    private Context context = this;
    private DrawerLayout drawerLayout;
    private EditText txtTitle;
    private EditText txtStartDate;
    private EditText txtEndDate;
    private NavigationView navigationView;
    private Switch swStart;
    private Switch swEnd;
    private TermData termData;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        swStart = findViewById(R.id.swStart);
        swEnd = findViewById(R.id.swEnd);
        txtTitle = findViewById(R.id.txtTermTitle);
        txtStartDate =  findViewById(R.id.txtTermStartDate);
        txtEndDate =  findViewById(R.id.txtTermEndDate);
        navigationView = findViewById(R.id.nav_view);

        ViewHelper.setupDateInput(this, txtStartDate);
        ViewHelper.setupDateInput(this, txtEndDate);
        termData = new TermData(this);
        termData.open();

        navigationView.setNavigationItemSelectedListener(ViewHelper.getNavigationListener(context, drawerLayout));

        Button btnSave = new Button(this);
        btnSave.setText(R.string.save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { save(); }
        });
        btnSave.setLayoutParams(ViewHelper.rightLayout());
        toolbar.addView(btnSave);

        Button btnCancel = new Button(this);
        btnCancel.setText(R.string.cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { cancel(); }
        });
        btnCancel.setLayoutParams(ViewHelper.leftLayout());
        toolbar.addView(btnCancel);

        TextView txtTitle = ViewHelper.getTitleText(this, R.string.add_term);
        toolbar.addView(txtTitle);
    }

    private void save() {
        String title = txtTitle.getText().toString();
        long startDate = DBHelper.stringToTimestamp(txtStartDate.getText().toString());
        long endDate = DBHelper.stringToTimestamp(txtEndDate.getText().toString());
        int startId = 17 * (title.hashCode() + Long.hashCode(startDate));
        int endId = 17 * (title.hashCode() + Long.hashCode(endDate));
        boolean startEnabled = swStart.isChecked();
        boolean endEnabled = swEnd.isChecked();
        Term term = termData.createTerm(title, startDate, endDate, startId, endId);
        ViewHelper.setAlarm(this, startDate, term.getStartMessage(), startId, startEnabled);
        ViewHelper.setAlarm(this, endDate, term.getEndMessage(), endId, endEnabled);
        finish();
    }

    private void cancel() {
        finish();
    }

    @Override
    protected void onResume() {
        termData.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        termData.close();
        super.onPause();
    }
}
