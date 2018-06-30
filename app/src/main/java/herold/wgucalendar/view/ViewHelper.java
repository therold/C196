package herold.wgucalendar.view;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

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
            case R.id.nav_add_course:
                ViewHelper.switchToActivity(context, AddCourseActivity.class);
                break;
            case R.id.nav_all_course:
                ViewHelper.switchToActivity(context, AllCourseActivity.class);
                break;
            case R.id.nav_course_by_term:
                ViewHelper.switchToActivity(context, ViewCourseByTermActivity.class);
                break;
            case R.id.nav_add_assessment:
                ViewHelper.switchToActivity(context, AddAssessmentActivity.class);
                break;
            case R.id.nav_all_assessment:
                ViewHelper.switchToActivity(context, AllAssessmentActivity.class);
                break;
            case R.id.nav_assessment_by_course:
                ViewHelper.switchToActivity(context, ViewAssessmentByCourseActivity.class);
                break;
        }
    }

    public static void setupToolbar(AppCompatActivity activity, Toolbar toolbar, int title) {
        activity.setSupportActionBar(toolbar);
        TextView txtTitle = getTitleText(activity, title);
        toolbar.addView(txtTitle);
        ActionBar actionbar = activity.getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayHomeAsUpEnabled(true);
    }

    public static Toolbar.LayoutParams centerLayout() {
        Toolbar.LayoutParams layout = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        layout.gravity = Gravity.CENTER;
        return layout;
    }

    public static Toolbar.LayoutParams leftLayout() {
        Toolbar.LayoutParams layout = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        layout.gravity = Gravity.START;
        return layout;
    }

    public static Toolbar.LayoutParams rightLayout() {
        Toolbar.LayoutParams layout = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        layout.gravity = Gravity.END;
        return layout;
    }

    public static TextView getTitleText(Activity activity, int title) {
        TextView txtTitle = new TextView(activity);
        txtTitle.setTextSize(18);
        txtTitle.setTextColor(Color.BLACK);
        txtTitle.setLayoutParams(centerLayout());
        txtTitle.setText(title);
        return txtTitle;
    }

    public static void scrollToTop(final ScrollView scrollView) {
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });
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

    public static void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void setAlarm(Activity activity, long date, String message, int key, boolean isEnabled) {
        SharedPreferences sharedPref = activity.getSharedPreferences(activity.getString(R.string.preference_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(Integer.toString(key), isEnabled);
        editor.commit();

        Intent intent = new Intent(activity, AlarmReceiver.class);
        intent.putExtra("Title", message);
        boolean pendingAlarm = (PendingIntent.getBroadcast(activity, key, intent, PendingIntent.FLAG_NO_CREATE) != null);

        AlarmManager alarmMgr = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        PendingIntent sender;
        if (pendingAlarm && isEnabled) {
            sender = PendingIntent.getBroadcast(activity, key, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmMgr.set(AlarmManager.RTC_WAKEUP, date, sender);
        } else if (pendingAlarm && !isEnabled) {
            sender = PendingIntent.getBroadcast(activity, key, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmMgr.cancel(sender);
        } else if (!pendingAlarm && isEnabled) {
            sender = PendingIntent.getBroadcast(activity, key, intent, 0);
            alarmMgr.set(AlarmManager.RTC_WAKEUP, date, sender);
        }
    }
}