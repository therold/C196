package herold.wgucalendar.model;

public class Course {
    private long id;
    private String title;
    private String startDate;
    private String endDate;
    private String status;
    private String mentorName;
    private String mentorPhone;
    private String mentorEmail;
    private String notes;
    // private List<String> assessments;

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getStart() { return startDate; }
    public String getEnd() { return endDate; }
    public String getStatus() { return status; }
    public String getMentorName() { return mentorName; }
    public String getMentorPhone() { return mentorPhone; }
    public String getMentorEmail() { return mentorEmail; }
    public String getNotes() { return notes; }
    // private List<String> getAssessments() { return assessments; }

    public void setId(long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setStart(String startDate) { this.startDate = startDate; }
    public void setEnd(String endDate) { this.endDate = endDate; }
    public void setStatus(String status) { this.status = status; }
    public void setMentorName(String mentorName) { this.mentorName = mentorName; }
    public void setMentorPhone(String mentorPhone) { this.mentorPhone = mentorPhone; }
    public void setMentorEmail(String mentorEmail) { this.mentorEmail = mentorEmail; }
    public void setNotes(String notes) { this.notes = notes; }
    // private void addAssessment(String string)
    // private void deleteAssessment(String string)
}
