package herold.wgucalendar.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import herold.wgucalendar.R;

public class ViewHelper {
    public static int DATA_SET_CHANGED = 10;

    public static void switchToActivity(Context context, Class c) {
        Intent intent = new Intent(context, c);
        context.startActivity(intent);
    }

    public static NavigationView.OnNavigationItemSelectedListener getNavigationListener(Context context, DrawerLayout drawerLayout) {
        final Context c = context;
        final DrawerLayout d = drawerLayout;
        return new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                itemSelected(c, d, menuItem);
                return true;
            }
        };
    }

    private static void itemSelected(Context context, DrawerLayout drawerLayout, MenuItem menuItem) {
        drawerLayout.closeDrawers();
        switch (menuItem.getItemId()) {
            case R.id.nav_add_term:
                ViewHelper.switchToActivity(context, AddTermActivity.class);
                break;
            case R.id.nav_all_term:
                ViewHelper.switchToActivity(context, AllTermActivity.class);
                break;
        }
    }

    public static void setupToolbar(AppCompatActivity activity, Toolbar toolbar, int title) {
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(title);
        ActionBar actionbar = activity.getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
    }

    public static void closeKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void setupDateInput(final Activity activity, final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = activity.getResources().getString(R.string.date_format);
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                editText.setText(sdf.format(calendar.getTime()));
            }
        };
        final DatePickerDialog startDatePickerDialog = new DatePickerDialog(
                activity, dateListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDatePickerDialog.show();
            }
        });
    }

    public static AlertDialog.Builder getDialog(Context context, String title, String message) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        builder.setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert);
        return builder;
    }

    public static AlertDialog.Builder getDialog(Context context, int title, int message) {
        return getDialog(context, context.getResources().getString(title),
                context.getResources().getString(message));
    }

    public static View.OnTouchListener scrollInsideScrollview(final ScrollView scrollView) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }

        };
    }

    public static void enableInput(List<View> inputs) {
        for(View input : inputs) {
            input.setFocusable(true);
            input.setClickable(true);
            input.setFocusableInTouchMode(true);
            if (input instanceof EditText) {
                ((EditText) input).setCursorVisible(true);
            } else if (input instanceof Spinner) {
                input.setEnabled(true);
            }
        }
    }

    public static void disableInput(List<View> inputs) {
        for(View input : inputs) {
            input.setFocusable(false);
            input.setClickable(false);
            if (input instanceof EditText) {
                ((EditText) input).setCursorVisible(false);
            } else if (input instanceof Spinner) {
                input.setEnabled(false);
            }
        }
    }
}