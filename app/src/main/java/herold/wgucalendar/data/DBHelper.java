package herold.wgucalendar.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE = "wgu.db";
    private static final int DATABASE_VERSION = 6;

    private static final String DATABASE_CREATE_TERM = "CREATE TABLE "
            + TermData.TABLE + "( "
            + TermData.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TermData.COLUMN_TITLE + " TEXT NOT NULL, "
            + TermData.COLUMN_START_DATE + " INTEGER NOT NULL, "
            + TermData.COLUMN_END_DATE + " INTEGER NOT NULL);";
    private static final String DATABASE_CREATE_COURSE = "CREATE TABLE "
            + CourseData.TABLE + "( "
            + CourseData.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CourseData.COLUMN_TITLE + " TEXT NOT NULL, "
            + CourseData.COLUMN_START_DATE + " TEXT NOT NULL, "
            + CourseData.COLUMN_END_DATE + " TEXT NOT NULL, "
            + CourseData.COLUMN_STATUS + " TEXT NOT NULL, "
            + CourseData.COLUMN_MENTOR_NAME + " TEXT NOT NULL, "
            + CourseData.COLUMN_MENTOR_PHONE + " TEXT NOT NULL, "
            + CourseData.COLUMN_MENTOR_EMAIL + " TEXT NOT NULL, "
            + CourseData.COLUMN_NOTES  + " TEXT, "
            + CourseData.COLUMN_TERM_ID + " INTEGER NOT NULL, "
            + " FOREIGN KEY (" + CourseData.COLUMN_TERM_ID + ")  REFERENCES "
                + TermData.TABLE + " (" + TermData.COLUMN_ID + "));";
    private static final String DATABASE_CREATE_ASSESSMENT = "CREATE TABLE "
            + AssessmentData.TABLE + "( "
            + AssessmentData.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + AssessmentData.COLUMN_TITLE + " TEXT NOT NULL, "
            + AssessmentData.COLUMN_TYPE + " TEXT NOT NULL, "
            + AssessmentData.COLUMN_DUE_DATE + " TEXT NOT NULL, "
            + AssessmentData.COLUMN_COURSE_ID + " INTEGER NOT NULL, "
            + " FOREIGN KEY (" + AssessmentData.COLUMN_COURSE_ID + ")  REFERENCES "
            + CourseData.TABLE + " (" + CourseData.COLUMN_ID + "));";

    public DBHelper(Context context) { super(context, DATABASE, null, DATABASE_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_TERM);
        database.execSQL(DATABASE_CREATE_COURSE);
        database.execSQL(DATABASE_CREATE_ASSESSMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TermData.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CourseData.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + AssessmentData.TABLE);
        onCreate(db);
    }

    public static long stringToTimestamp(String date) {
        String isoDate = "20" + date.substring(6, 8) + "-" + date.substring(0, 2)
                + "-" + date.substring(3, 5) + " 00:00:00";
        Log.v("test", isoDate);
        Timestamp timestamp = Timestamp.valueOf(isoDate);
        return timestamp.getTime();
    }

    public static String timestampToString(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        calendar.setTimeInMillis(timestamp);
        String date = sdf.format(calendar.getTime());
        return date;
    }
}
