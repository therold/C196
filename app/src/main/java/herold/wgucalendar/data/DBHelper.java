package herold.wgucalendar.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE = "wgu.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TermData.TABLE + "( "
            + TermData.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TermData.COLUMN_TITLE + " TEXT NOT NULL, "
            + TermData.COLUMN_START_DATE + " TEXT NOT NULL, "
            + TermData.COLUMN_END_DATE + " TEXT NOT NULL);";

    public DBHelper(Context context) { super(context, DATABASE, null, DATABASE_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase database) { database.execSQL(DATABASE_CREATE); }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TermData.TABLE);
        onCreate(db);
    }
}
