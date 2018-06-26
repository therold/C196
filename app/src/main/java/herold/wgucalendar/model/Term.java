package herold.wgucalendar.model;

public class Term {
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
}
