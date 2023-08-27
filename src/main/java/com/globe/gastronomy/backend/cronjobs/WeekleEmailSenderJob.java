package com.globe.gastronomy.backend.cronjobs;

import com.globe.gastronomy.backend.facade.EmailFacade;
import com.globe.gastronomy.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executors;

// TODO: 8/27/2023 improve the job architecture to provide inheritance hierarchy
@Component
public class WeekleEmailSenderJob {
    private final EmailFacade emailFacade;

    public WeekleEmailSenderJob(EmailFacade emailFacade) {
        this.emailFacade = emailFacade;
    }

    @Scheduled(cron = "0/15 * * * * *")
    public void weeklyEmailSender() {
        var executor = Executors.newFixedThreadPool(5);

        List<User> users = emailFacade.getUsers();

        users.stream().map(e -> new MidLayer(emailFacade, e)).forEach(executor::execute);

    }
}

// TODO: 8/27/2023 make it seperate class
class MidLayer implements Runnable {
    private final EmailFacade emailFacade;
    private final User user;

    public MidLayer(EmailFacade emailFacade, User user) {
        this.emailFacade = emailFacade;
        this.user = user;
    }

    @Override
    public void run() {
        emailFacade.sendWeeklyEmail("tr", user);
    }

}
