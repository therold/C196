package herold.wgucalendar.model;

import android.os.Parcel;
import android.os.Parcelable;

import herold.wgucalendar.data.DBHelper;

public class Term implements Parcelable {
    private long id;
    private String title;
    private long start;
    private long end;

    public long getId() { return id; }
    public String getTitle() { return title; }
    public long getStart() { return start; }
    public long getEnd() { return end; }
    public String getStartDisplay() { return DBHelper.timestampToString(start); }
    public String getEndDisplay() { return DBHelper.timestampToString(end); }

    public void setId(long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setStart(long start) { this.start = start; }
    public void setEnd(long end) { this.end = end; }

    public Term() {}

    public Term(long id, String title, long start, long end) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
    }

    public Term(Parcel source) {
        id = source.readLong();
        title = source.readString();
        start = source.readLong();
        end = source.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeLong(start);
        dest.writeLong(end);
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Term)) {
            return false;
        }

        Term term = (Term) o;
        return term.id == id;
    }
}
