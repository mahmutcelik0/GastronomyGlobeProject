package com.globe.gastronomy.backend.facade;

import com.globe.gastronomy.backend.command.PasswordEmailCommand;
import com.globe.gastronomy.backend.command.PasswordEmailResponse;
import com.globe.gastronomy.backend.model.User;
import com.globe.gastronomy.backend.service.UserService;
import com.globe.gastronomy.backend.utils.LogUtil;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
}
