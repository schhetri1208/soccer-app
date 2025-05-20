package com.soccerapp.controller;

import com.soccerapp.service.SendGridService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestEmailController {

    @Autowired
    private SendGridService sendGridService;

    @GetMapping("/send-test-email")
    public ResponseEntity<String> sendTestEmail() {
        try {
            String html = """
                <html>
                <body>
                    <h1>Test Email from SoccerApp</h1>
                    <p>If you received this, SendGrid is working! 🎯</p>
                </body>
                </html>
                """;
            sendGridService.sendEmail("sahil.budhathoki25@gmail.com", "Test Email from SoccerApp", html);
            return ResponseEntity.ok("Test email sent! Check your inbox.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email: " + e.getMessage());
        }
    }
}
