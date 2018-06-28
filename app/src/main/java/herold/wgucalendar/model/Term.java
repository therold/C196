package herold.wgucalendar.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Term implements Parcelable {
    private long id;
    private String title;
    private String start;
    private String end;

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getStart() { return start; }
    public String getEnd() { return end; }

    public void setId(long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setStart(String start) { this.start = start; }
    public void setEnd(String end) { this.end = end; }

    public Term() {}

    public Term(long id, String title, String start, String end) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
    }

    public Term(Parcel source) {
        id = source.readLong();
        title = source.readString();
        start = source.readString();
        end = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(start);
        dest.writeString(end);
    }

    public static final Creator<Term> CREATOR = new Creator<Term>() {
        @Override
        public Term[] newArray(int size) {
            return new Term[size];
        }

        @Override
        public Term createFromParcel(Parcel source) {
            return new Term(source);
        }
    };

    @Override
    public String toString() {
        return title;
    }
}
