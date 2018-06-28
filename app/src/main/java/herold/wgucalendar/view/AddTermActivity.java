package herold.wgucalendar.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import herold.wgucalendar.R;
import herold.wgucalendar.data.TermData;

public class AddTermActivity extends AppCompatActivity {
    private Button btnSave;
    private Context context = this;
    private DrawerLayout drawerLayout;
    private EditText txtTitle;
    private EditText txtStartDate;
    private EditText txtEndDate;
    private ImageView imgMenu;
    private NavigationView navigationView;
    private TermData termData;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        txtTitle = findViewById(R.id.txtTermTitle);
        txtStartDate =  findViewById(R.id.txtTermStartDate);
        txtEndDate =  findViewById(R.id.txtTermEndDate);
        navigationView = findViewById(R.id.nav_view);
        imgMenu = findViewById(R.id.imgMenu);
        btnSave = findViewById(R.id.btnTermSave);

        ViewHelper.setupToolbar(this, toolbar, R.string.add_term);
        ViewHelper.setupDateInput(this, txtStartDate);
        ViewHelper.setupDateInput(this, txtEndDate);
        termData = new TermData(this);
        termData.open();

        navigationView.setNavigationItemSelectedListener(ViewHelper.getNavigationListener(context, drawerLayout));
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { drawerLayout.openDrawer(GravityCompat.START); }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { save(); }
        });
    }

    private void save() {
        String title = txtTitle.getText().toString();
        String startDate = txtStartDate.getText().toString();
        String endDate = txtEndDate.getText().toString();
        termData.createTerm(title, startDate, endDate);
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
