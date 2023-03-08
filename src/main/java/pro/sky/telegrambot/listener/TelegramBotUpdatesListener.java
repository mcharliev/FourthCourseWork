package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final NotificationTaskRepository notificationTaskRepository;

    public TelegramBotUpdatesListener(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    private Logger log = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            log.info("Processing update: {}", update);
            Long chatId = update.message().chat().id();
            String messageText = update.message().text();
            Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
            Matcher matcher = pattern.matcher(messageText);
            if (messageText.equals("/start")) {
                sendTextMessage(chatId, "Hi, " +
                        update.message().chat().firstName() +
                        " nice to meet you!");
            } else if (matcher.matches()) {
                parseAndSaveInDb(matcher.group(1),
                        matcher.group(3),
                        chatId);
            } else {
                sendTextMessage(chatId, "Sorry, the command was not recognized");
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void getNotificationByDateTime() {
        log.info("Scheduled method getLocalDateTime was called");
        LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<NotificationTask> notificationTaskList =
                notificationTaskRepository.findALlByDateTime(dateTime);
        sendNotifications(notificationTaskList);
    }

    private void sendNotifications(List<NotificationTask> listOfTask) {
        log.info("sendNotifications method was called");
        listOfTask.stream().forEach(element ->
               telegramBot.execute(new SendMessage(element.getChatId(),
                       element.getNotification())));
    }

    private void sendTextMessage(Long chatId, String message) {
        log.info("sendTextMessage method was called");
        SendMessage sendMessage = new SendMessage(chatId, message);
        telegramBot.execute(sendMessage);
    }

    private void parseAndSaveInDb(String DateAndTime, String notification, Long chatId) {
        log.info("parseAndSaveInDb method was called");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(DateAndTime, formatter);
        NotificationTask notificationTask = new NotificationTask();
        notificationTask.setNotification(notification);
        notificationTask.setChatId(chatId);
        notificationTask.setDateAndTime(dateTime);
        notificationTaskRepository.save(notificationTask);
    }
}
