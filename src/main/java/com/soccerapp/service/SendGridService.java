package com.soccerapp.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.soccerapp.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;

@Service
public class SendGridService {

    @Value("${sendgrid.api-key}")
    private String SENDGRID_API_KEY;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public String buildGameEmail(Game game, String firstName) {
        Context context = new Context();
        context.setVariable("firstName", firstName);
        context.setVariable("location", game.getLocation());
        context.setVariable("date", game.getGameDate().toString());
        context.setVariable("time", game.getGameTime().toString());

        return templateEngine.process("game-notification", context);
    }

    public void sendEmail(String sendTo, String subject, String content) throws IOException {
        Email from = new Email("sahil.budhathoki25@gmail.com");
        Email toEmail = new Email(sendTo);
        Content body = new Content("text/html", content);

        Mail mail = new Mail(from, subject, toEmail, body);
        SendGrid sg = new SendGrid(SENDGRID_API_KEY);
        Request request = new Request();

        try{
            request.setMethod(Method.POST);
            request.setEndpoint("/mail/send");
            request.setBody(mail.build());
            sg.api(request);
        }
        catch (IOException ex){
            throw ex;
        }
    }
}
