package pojo;

import java.util.Date;
import java.util.Objects;

public class User {
    private String _id;
    private String name;
    private Date created_at;
    private Boolean verified;

    public User() {
    }

    public User(String _id, String name, Date created_at, Boolean verified) {
        this._id = _id;
        this.name = name;
        this.created_at = created_at;
        this.verified = verified;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return _id.equals(user._id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id);
    }

    @Override
    public String toString() {
        return  "===============================================%n_id = " + _id +
                "%nname = " + name +
                "%ncreated_at = " + created_at +
                "%nverified = " + verified + "%n";
    }
}
