package herold.wgucalendar.model;

import java.util.List;

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

    private long getId() { return id; }
    private String getTitle() { return title; }
    private String getStart() { return startDate; }
    private String getEnd() { return endDate; }
    private String getStatus() { return status; }
    private String getMentorName() { return mentorName; }
    private String getMentorPhone() { return mentorPhone; }
    private String getMentorEmail() { return mentorEmail; }
    private String getNotes() { return notes; }
    // private List<String> getAssessments() { return assessments; }

    private void setId(long id) { this.id = id; }
    private void setTitle(String title) { this.title = title; }
    private void setStart(String startDate) { this.startDate = startDate; }
    private void setEnd(String endDate) { this.endDate = endDate; }
    private void setStatus(String status) { this.status = status; }
    private void setMentorName(String mentorName) { this.mentorName = mentorName; }
    private void setMentorPhone(String mentorPhone) { this.mentorPhone = mentorPhone; }
    private void setMentorEmail(String mentorEmail) { this.mentorEmail = mentorEmail; }
    private void setNotes(String notes) { this.notes = notes; }
    // private void addAssessment(String string)
    // private void deleteAssessment(String string)
}
