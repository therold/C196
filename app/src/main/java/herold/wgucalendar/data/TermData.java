package herold.wgucalendar.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import herold.wgucalendar.model.Term;

public class TermData {
    public static final String TABLE = "terms";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] allColumns = { COLUMN_ID, COLUMN_TITLE,
            COLUMN_START_DATE, COLUMN_END_DATE };

    public TermData(Context context) { dbHelper = new DBHelper(context); }

    public void open() throws SQLException { database = dbHelper.getWritableDatabase(); }

    public void close() { dbHelper.close(); }

    public Term createTerm(String title, long startDate, long endDate) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_START_DATE, startDate);
        values.put(COLUMN_END_DATE, endDate);
        long insertId = database.insert(TABLE, null, values);
        Cursor cursor = database.query(TABLE, allColumns,
                COLUMN_ID + " = " + insertId, null,
                null, null,null);
        cursor.moveToFirst();
        Term newTerm = cursorToTerm(cursor);
        cursor.close();
        return newTerm;
    }

    public void updateTerm(Term term) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, term.getTitle());
        values.put(COLUMN_START_DATE, term.getStart());
        values.put(COLUMN_END_DATE, term.getEnd());
        database.update(TABLE, values, COLUMN_ID + " = " + term.getId(), null);
    }

    public void deleteTerm(Term term) {
        long id = term.getId();
        database.delete(TABLE, COLUMN_ID + " = " + id,null);
    }

    public Term get(long id) {
        Cursor cursor = database.query(TABLE, allColumns,
                COLUMN_ID + " = " + id, null, null, null, null);
        cursor.moveToFirst();
        Term term = cursorToTerm(cursor);
        cursor.close();
        return term;
    }

    public Term getCurrent() {
        long now = Calendar.getInstance().getTimeInMillis();

        Cursor cursor = database.query(TABLE, allColumns,
                COLUMN_START_DATE + " <= " + now + " AND " + COLUMN_END_DATE + " >= " + now, null, null, null, null);
        cursor.moveToFirst();
        Term term = null;
        if (cursor!=null && cursor.getCount()>0) {
            term = cursorToTerm(cursor);
        }
        cursor.close();
        return term;
    }

    public List<Term> all() {
        List<Term> terms = new ArrayList<>();
        Cursor cursor = database.query(TABLE, allColumns,
                null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Term term = cursorToTerm(cursor);
            terms.add(term);
            cursor.moveToNext();
        }
        cursor.close();
        return terms;
    }

    private Term cursorToTerm(Cursor cursor) {
        Term term = new Term();
        term.setId(cursor.getLong(0));
        term.setTitle(cursor.getString(1));
        term.setStart(cursor.getLong(2));
        term.setEnd(cursor.getLong(3));
        return term;
    }
}
