package herold.wgucalendar.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import herold.wgucalendar.model.Course;

public class CourseData {
    public static final String TABLE = "courses";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_MENTOR_NAME = "mentor_name";
    public static final String COLUMN_MENTOR_PHONE = "mentor_phone";
    public static final String COLUMN_MENTOR_EMAIL = "mentor_email";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_TERM_ID = "term_id";
    public static final String COLUMN_START_ID = "start_id";
    public static final String COLUMN_END_ID = "end_id";
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] allColumns = { COLUMN_ID, COLUMN_TITLE,
            COLUMN_START_DATE, COLUMN_END_DATE, COLUMN_STATUS,
            COLUMN_MENTOR_NAME, COLUMN_MENTOR_PHONE,
            COLUMN_MENTOR_EMAIL, COLUMN_NOTES, COLUMN_TERM_ID,
            COLUMN_START_ID, COLUMN_END_ID };

    public CourseData(Context context) { dbHelper = new DBHelper(context); }

    public void open() throws SQLException { database = dbHelper.getWritableDatabase(); }

    public void close() { dbHelper.close(); }

    public Course createCourse(String title, long startDate, long endDate, String status,
                               String mentorName, String mentorPhone, String mentorEmail,
                               String notes, long termId, int startId, int endId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_START_DATE, startDate);
        values.put(COLUMN_END_DATE, endDate);
        values.put(COLUMN_STATUS, status);
        values.put(COLUMN_MENTOR_NAME, mentorName);
        values.put(COLUMN_MENTOR_PHONE, mentorPhone);
        values.put(COLUMN_MENTOR_EMAIL, mentorEmail);
        values.put(COLUMN_NOTES, notes);
        values.put(COLUMN_TERM_ID, termId);
        values.put(COLUMN_START_ID, startId);
        values.put(COLUMN_END_ID, endId);
        long insertId = database.insert(TABLE, null, values);
        Cursor cursor = database.query(TABLE, allColumns,
                COLUMN_ID + " = " + insertId, null,
                null, null,null);
        cursor.moveToFirst();
        Course newCourse = cursorToCourse(cursor);
        cursor.close();
        return newCourse;
    }

    public void updateCourse(Course course) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, course.getTitle());
        values.put(COLUMN_START_DATE, course.getStart());
        values.put(COLUMN_END_DATE, course.getEnd());
        values.put(COLUMN_STATUS, course.getStatus());
        values.put(COLUMN_MENTOR_NAME, course.getMentorName());
        values.put(COLUMN_MENTOR_PHONE, course.getMentorPhone());
        values.put(COLUMN_MENTOR_EMAIL, course.getMentorEmail());
        values.put(COLUMN_NOTES, course.getNotes());
        values.put(COLUMN_TERM_ID, course.getTermId());
        values.put(COLUMN_START_ID, course.getStartId());
        values.put(COLUMN_END_ID, course.getEndId());
        database.update(TABLE, values, COLUMN_ID + " = " + course.getId(), null);
    }

    public void deleteCourse(Course course) {
        long id = course.getId();
        database.delete(TABLE, COLUMN_ID + " = " + id,null);
    }

    public Course get(long id) {
        Cursor cursor = database.query(TABLE, allColumns,
                COLUMN_ID + " = " + id, null, null, null, null);
        cursor.moveToFirst();
        Course course = cursorToCourse(cursor);
        cursor.close();
        return course;
    }

    public List<Course> getCurrent() {
        long now = Calendar.getInstance().getTimeInMillis();

        Cursor cursor = database.query(TABLE, allColumns,
                COLUMN_START_DATE + " <= " + now + " AND " + COLUMN_END_DATE + " >= " + now, null, null, null, null);
        cursor.moveToFirst();
        List<Course> courses = new ArrayList<>();
        while(!cursor.isAfterLast()) {
            Course course = cursorToCourse(cursor);
            courses.add(course);
            cursor.moveToNext();
        }
        cursor.close();
        return courses;
    }

    public List<Course> all() {
        List<Course> courses = new ArrayList<>();
        Cursor cursor = database.query(TABLE, allColumns,
                null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Course course = cursorToCourse(cursor);
            courses.add(course);
            cursor.moveToNext();
        }
        cursor.close();
        return courses;
    }

    public List<Course> findByTerm(long termId) {
        List<Course> courses = new ArrayList<>();
        Cursor cursor = database.query(TABLE, allColumns,
                COLUMN_TERM_ID + " = " + termId, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Course course = cursorToCourse(cursor);
            courses.add(course);
            cursor.moveToNext();
        }
        cursor.close();
        return courses;
    }

    private Course cursorToCourse(Cursor cursor) {
        Course course = new Course();
        course.setId(cursor.getLong(0));
        course.setTitle(cursor.getString(1));
        course.setStart(cursor.getLong(2));
        course.setEnd(cursor.getLong(3));
        course.setStatus(cursor.getString(4));
        course.setMentorName(cursor.getString(5));
        course.setMentorPhone(cursor.getString(6));
        course.setMentorEmail(cursor.getString(7));
        course.setNotes(cursor.getString(8));
        course.setTermId(cursor.getLong(9));
        course.setStartId(cursor.getInt(10));
        course.setEndId(cursor.getInt(11));
        return course;
    }
}
