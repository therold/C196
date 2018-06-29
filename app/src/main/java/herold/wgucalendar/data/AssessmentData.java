package herold.wgucalendar.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import herold.wgucalendar.model.Assessment;

public class AssessmentData {
    public static final String TABLE = "assessments";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_DUE_DATE = "due_date";
    public static final String COLUMN_COURSE_ID = "course_id";
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] allColumns = { COLUMN_ID, COLUMN_TITLE,
            COLUMN_TYPE, COLUMN_DUE_DATE, COLUMN_COURSE_ID };

    public AssessmentData(Context context) { dbHelper = new DBHelper(context); }

    public void open() throws SQLException { database = dbHelper.getWritableDatabase(); }

    public void close() { dbHelper.close(); }

    public Assessment createAssessment(String title, String type, long dueDate, long courseId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_DUE_DATE, dueDate);
        values.put(COLUMN_COURSE_ID, courseId);
        long insertId = database.insert(TABLE, null, values);
        Cursor cursor = database.query(TABLE, allColumns,
                COLUMN_ID + " = " + insertId, null,
                null, null,null);
        cursor.moveToFirst();
        Assessment newAssessment = cursorToAssessment(cursor);
        cursor.close();
        return newAssessment;
    }

    public void updateAssessment(Assessment assessment) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, assessment.getTitle());
        values.put(COLUMN_TYPE, assessment.getType());
        values.put(COLUMN_DUE_DATE, assessment.getDueDate());
        values.put(COLUMN_COURSE_ID, assessment.getCourseId());
        database.update(TABLE, values, COLUMN_ID + " = " + assessment.getId(), null);
    }

    public void deleteAssessment(Assessment assessment) {
        long id = assessment.getId();
        database.delete(TABLE, COLUMN_ID + " = " + id,null);
    }

    public List<Assessment> all() {
        List<Assessment> assessments = new ArrayList<>();
        Cursor cursor = database.query(TABLE, allColumns,
                null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Assessment assessment = cursorToAssessment(cursor);
            assessments.add(assessment);
            cursor.moveToNext();
        }
        cursor.close();
        return assessments;
    }

    public List<Assessment> findByCourse(long termId) {
        List<Assessment> assessments = new ArrayList<>();
        Cursor cursor = database.query(TABLE, allColumns,
                COLUMN_COURSE_ID + " = " + termId, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Assessment assessment = cursorToAssessment(cursor);
            assessments.add(assessment);
            cursor.moveToNext();
        }
        cursor.close();
        return assessments;
    }

    private Assessment cursorToAssessment(Cursor cursor) {
        Assessment assessment = new Assessment();
        assessment.setId(cursor.getLong(0));
        assessment.setTitle(cursor.getString(1));
        assessment.setType(cursor.getString(2));
        assessment.setDueDate(cursor.getLong(3));
        assessment.setCourseId(cursor.getLong(4));
        return assessment;
    }
}
