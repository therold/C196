package herold.wgucalendar.model;

import android.os.Parcel;
import android.os.Parcelable;

import herold.wgucalendar.data.DBHelper;

public class Assessment implements Parcelable {
    private long id;
    private String title;
    private String type;
    private long dueDate;
    private long courseId;
    private int dueDateId;

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getType() { return type; }
    public long getDueDate() { return dueDate; }
    public String getDueDateDisplay() { return DBHelper.timestampToString(dueDate); }
    public long getCourseId() { return courseId; }
    public int getDueDateId() { return dueDateId; }
    public String getDueMessage() { return "Assessment " + title + " due on " + getDueDateDisplay(); }

    public void setId(long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setType(String type) { this.type = type; }
    public void setDueDate(long dueDate) { this.dueDate = dueDate; }
    public void setCourseId(long courseId) { this.courseId = courseId; }
    public void setDueDateId(int dueDateId) { this.dueDateId = dueDateId; }

    public Assessment() {}

    public Assessment(long id, String title, String type, long dueDate, long courseId, int dueDateId) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.dueDate = dueDate;
        this.courseId = courseId;
        this.dueDateId = dueDateId;
    }

    public Assessment(Parcel source) {
        id = source.readLong();
        title = source.readString();
        type = source.readString();
        dueDate = source.readLong();
        courseId = source.readLong();
        dueDateId = source.readInt();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(type);
        dest.writeLong(dueDate);
        dest.writeLong(courseId);
        dest.writeInt(dueDateId);
    }

    public static final Creator<Assessment> CREATOR = new Creator<Assessment>() {
        @Override
        public Assessment[] newArray(int size) { return new Assessment[size]; }

        @Override
        public Assessment createFromParcel(Parcel source) { return new Assessment(source); }
    };

    @Override
    public String toString() { return title; }
}
