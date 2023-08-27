package com.globe.gastronomy.backend.facade;

import com.globe.gastronomy.backend.command.PasswordEmailCommand;
import com.globe.gastronomy.backend.command.PasswordEmailResponse;
import com.globe.gastronomy.backend.command.WeeklyEmailResponse;
import com.globe.gastronomy.backend.dto.UserDtoPopulator;
import com.globe.gastronomy.backend.exception.UserNotFoundException;
import com.globe.gastronomy.backend.model.User;
import com.globe.gastronomy.backend.service.UserService;
import com.globe.gastronomy.backend.utils.LogUtil;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class EmailFacade {
    private final PasswordEmailCommand emailCommand;
    private final UserService userService;

    public EmailFacade(PasswordEmailCommand emailCommand, UserService userService) {
        this.emailCommand = emailCommand;
        this.userService = userService;
    }

    public PasswordEmailResponse sendForgotPasswordEmail(String language,String email){
        try {
            User user = userService.getUserByEmail(email);
            return new PasswordEmailResponse(emailCommand.sendEmail(user,language).getResponseMessage()) ;
        }catch (UsernameNotFoundException e){
            LogUtil.printLog("USER NOT FOUND", EmailFacade.class);
            return new PasswordEmailResponse("E-MAIL CREDENTIAL IS NOT CORRECT. TRY AGAIN!");
        }
    }

    private static Integer number = 1;
    public WeeklyEmailResponse sendWeeklyEmail(String language, User user){
        LogUtil.printLog(String.format("WORKED for %s: %s",user.getEmail(),number++),EmailFacade.class);
        return new WeeklyEmailResponse(String.format("WORKED for %s",user.getEmail()));
        // TODO: 8/27/2023 fix smtp credential issue
        // TODO: 8/27/2023  jakarta.mail.AuthenticationFailedException: 535-5.7.8 Username and Password not accepted. Learn more at
    }

    public List<User> getUsers(){
        try {
            return userService.getUsers();
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
