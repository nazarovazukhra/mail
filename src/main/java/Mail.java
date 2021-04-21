import java.sql.Date;
import java.sql.Timestamp;

public class Mail {
    private Integer id;
    private String title;
    private String body;
    private User sender;
    private User receiver;
    private Timestamp createdAt;
    private Boolean isRead;
    private Boolean isDeleted;

    public Mail(Integer id, String title, String body, User sender, User receiver, Timestamp createdAt, Boolean isRead, Boolean isDeleted) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.sender = sender;
        this.receiver = receiver;
        this.createdAt = createdAt;
        this.isRead = isRead;
        this.isDeleted = isDeleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User senderId) {
        this.sender = senderId;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiverId) {
        this.receiver = receiverId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "Mail{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", createdAt=" + createdAt +
                ", isRead=" + isRead +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
