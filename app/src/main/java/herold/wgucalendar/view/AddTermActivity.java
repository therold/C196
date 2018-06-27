package herold.wgucalendar.view;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import herold.wgucalendar.R;
import herold.wgucalendar.data.TermData;

public class AddTermActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Context context = this;
    private TermData datasource;
    private Calendar startDate = Calendar.getInstance();
    private Calendar endDate = Calendar.getInstance();
    private EditText txtTitle;
    private EditText txtStartDate;
    private EditText txtEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.add_term);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(ViewHelper.getNavigationListener(context, drawerLayout));
        ImageView imgMenu = findViewById(R.id.imgMenu);
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { drawerLayout.openDrawer(GravityCompat.START); }
        });

        txtTitle = findViewById(R.id.txtTermTitle);
        txtStartDate =  findViewById(R.id.txtTermStartDate);
        txtEndDate =  findViewById(R.id.txtTermEndDate);
        Button btnSave = findViewById(R.id.btnTermSave);
        datasource = new TermData(this);
        datasource.open();

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
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = txtTitle.getText().toString();
                String startDate = txtStartDate.getText().toString();
                String endDate = txtEndDate.getText().toString();
                datasource.createTerm(title, startDate, endDate);
                goToMainActivity();
            }
        });
    }

    private void updateLabel(EditText e, Calendar c) {
        String myFormat = getResources().getString(R.string.date_format);
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        e.setText(sdf.format(c.getTime()));
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }
}
