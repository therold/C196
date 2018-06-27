package herold.wgucalendar.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE = "wgu.db";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE_TERM = "CREATE TABLE "
            + TermData.TABLE + "( "
            + TermData.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TermData.COLUMN_TITLE + " TEXT NOT NULL, "
            + TermData.COLUMN_START_DATE + " TEXT NOT NULL, "
            + TermData.COLUMN_END_DATE + " TEXT NOT NULL);";
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
            + CourseData.COLUMN_NOTES  + " TEXT NOT NULL, "
            + CourseData.COLUMN_TERM_ID + " INTEGER NOT NULL, "
            + " FOREIGN KEY (" + CourseData.COLUMN_TERM_ID + ")  REFERENCES "
                + TermData.TABLE + " (" + TermData.TABLE + "));";

    public DBHelper(Context context) { super(context, DATABASE, null, DATABASE_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_TERM);
        database.execSQL(DATABASE_CREATE_COURSE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TermData.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CourseData.TABLE);
        onCreate(db);
    }
}
