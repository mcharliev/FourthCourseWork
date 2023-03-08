package pro.sky.telegrambot.model;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_task")
public class NotificationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "notification")
    private String notification;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    public Integer getId() {
        return id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public LocalDateTime getDateAndTime() {
        return dateTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateTime = dateAndTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotificationTask)) return false;

        NotificationTask that = (NotificationTask) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getChatId() != null ? !getChatId().equals(that.getChatId()) : that.getChatId() != null) return false;
        if (getNotification() != null ? !getNotification().equals(that.getNotification()) : that.getNotification() != null)
            return false;
        return dateTime != null ? dateTime.equals(that.dateTime) : that.dateTime == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getChatId() != null ? getChatId().hashCode() : 0);
        result = 31 * result + (getNotification() != null ? getNotification().hashCode() : 0);
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", notification='" + notification + '\'' +
                ", dateAndTime=" + dateTime +
                '}';
    }
}
