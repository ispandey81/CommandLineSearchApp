package pojo;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Ticket {
    private String _id;
    private Date created_at;
    private String type;
    private String subject;
    private Integer assignee_id;
    private List<String> tags;

    public Ticket() {
    }

    public Ticket(String _id, Date created_at, String type, String subject, Integer assignee_id, List<String> tags) {
        this._id = _id;
        this.created_at = created_at;
        this.type = type;
        this.subject = subject;
        this.assignee_id = assignee_id;
        this.tags = tags;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getAssignee_id() {
        return assignee_id;
    }

    public void setAssignee_id(Integer assignee_id) {
        this.assignee_id = assignee_id;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return _id.equals(ticket._id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id);
    }

    @Override
    public String toString() {
        return  "===============================================%n_id = " + _id +
                "%ncreated_at = " + created_at +
                "%ntype = " + type +
                "%nsubject = " + subject +
                "%nassignee_id = " + assignee_id +
                "%ntags = " + tags + "%n";
    }
}
