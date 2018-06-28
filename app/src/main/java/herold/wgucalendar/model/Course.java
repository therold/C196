package herold.wgucalendar.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Course implements Parcelable {
    private long id;
    private String title;
    private String startDate;
    private String endDate;
    private String status;
    private String mentorName;
    private String mentorPhone;
    private String mentorEmail;
    private String notes;
    private long termId;

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getStart() { return startDate; }
    public String getEnd() { return endDate; }
    public String getStatus() { return status; }
    public String getMentorName() { return mentorName; }
    public String getMentorPhone() { return mentorPhone; }
    public String getMentorEmail() { return mentorEmail; }
    public String getNotes() { return notes; }
    public long getTermId() { return termId; }

    public void setId(long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setStart(String startDate) { this.startDate = startDate; }
    public void setEnd(String endDate) { this.endDate = endDate; }
    public void setStatus(String status) { this.status = status; }
    public void setMentorName(String mentorName) { this.mentorName = mentorName; }
    public void setMentorPhone(String mentorPhone) { this.mentorPhone = mentorPhone; }
    public void setMentorEmail(String mentorEmail) { this.mentorEmail = mentorEmail; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setTermId(long termId) { this.termId = termId; }

    public Course() {}

    public Course(long id, String title, String startDate, String endDate, String status,
            String mentorName, String mentorPhone, String mentorEmail, String notes,
            long termId) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.mentorName = mentorName;
        this.mentorPhone = mentorPhone;
        this.mentorEmail = mentorEmail;
        this.notes = notes;
        this.termId = termId;
    }

    public Course(Parcel source) {
        this.id = source.readLong();
        this.title = source.readString();
        this.startDate = source.readString();
        this.endDate = source.readString();
        this.status = source.readString();
        this.mentorName = source.readString();
        this.mentorPhone = source.readString();
        this.mentorEmail = source.readString();
        this.notes = source.readString();
        this.termId = source.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(status);
        dest.writeString(mentorName);
        dest.writeString(mentorPhone);
        dest.writeString(mentorEmail);
        dest.writeString(notes);
        dest.writeLong(termId);
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }

        @Override
        public Course createFromParcel(Parcel source) {
            return new Course(source);
        }
    };

    @Override
    public String toString() {
        return title;
    }
}
