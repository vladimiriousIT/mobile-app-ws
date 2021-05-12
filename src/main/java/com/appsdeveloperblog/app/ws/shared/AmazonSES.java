package com.appsdeveloperblog.app.ws.shared;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.appsdeveloperblog.app.ws.shared.dto.UserDTO;

public class AmazonSES {
    final String FROM = "vladimir_stratiev@XXXX.com";
    final String SUBJECT = "One last step to complete your registration with PhotoApp";
    final String PASSWORD_RESET_SUBJECT = "Password reset request";

    public boolean sendPasswordResetRequest(String firstName, String email, String token) {
        return false;
    }

    //HTML body for the email
    final String HTMLBODY = "<h1>Please verify your email address</h1>"
            + "<p>Thank you for registering with our mobile app. To complete registration process and be able to use it"
            + " click on the following link: "
            + "<a href='ec2.url"
            + "Final step to complete your registration " + "<a><br/><br/>"
            + "Thank you! And we are waiting for you inside!";

    //EMAIL body for recipients with non-HTML email clients.
    final String TEXTBODY = "Please verify your email address. "
            + "Thank you for registering with our mobile app. To complete registration process and be able to use it"
            + " opne then the following URL in your browser window: "
            + " ec2.uel"
            + " Thank you! And we are waiting for you inside!";

    //HTML body for the email
    final String PASSWORD_RESET_HTMLBODY = "<h1>A request to reset your password</h1>"
            + "<p>Hi, $firstName!</p> "
            + "<p>Someone has reuested to reset your password with our project. If it were not you please contact us"
            + " otherwise please click on the link below to set a new password: "
            + "<a href='http://localhost:8080/verification-service/password-reset.html?token=$tokenValue'>"
            + " Click this link to Reset Password "
            + "<a><br/><br/>"
            + "Thank you!";

    //HTML body for the email
    final String PASSWORD_RESET_TEXTBODY = "A request to reset your password"
            + " Hi, $firstName!"
            + " Someone has reuested to reset your password with our project. If it were not you please contact us"
            + " otherwise please open the link below in your browser window to set a new password: "
            + " http://localhost:8080/verification-service/password-reset.html?token=$tokenValue"
            + " Click this link to Reset Password "
            + " Thank you!";

    public void verifyEmail(UserDTO userDTO) {

    }

    public boolean sendPasswordResetReuqest(String firstName, String email, String token) {
        boolean returnValue = false;

        AmazonSimpleEmailService client =
                AmazonSimpleEmailServiceClientBuilder.standard()
                        .withRegion(Regions.US_EAST_1).build();

        String htmlBodyWithToken = PASSWORD_RESET_HTMLBODY.replace("$tokenValue", token);
        htmlBodyWithToken = htmlBodyWithToken.replace("$firstName", firstName);

        String textBodyWithToken = PASSWORD_RESET_TEXTBODY.replace("$tokenValue", token);
        textBodyWithToken = textBodyWithToken.replace("$firstName", firstName);

        SendEmailRequest request = new SendEmailRequest()
                .withDestination(
                        new Destination().withToAddresses(email))
                .withMessage(new Message()
                        .withBody(new Body()
                                .withHtml(new Content()
                                        .withCharset("UTF-8").withData(htmlBodyWithToken))
                                .withText(new Content()
                                        .withCharset("UTF-8").withData(textBodyWithToken)))
                        .withSubject(new Content()
                                .withCharset("UTF-8").withData(PASSWORD_RESET_SUBJECT)))
                .withSource(FROM);

        SendEmailResult result = client.sendEmail(request);
        if(result != null && (result.getMessageId()!=null && !result.getMessageId().isEmpty())){
            returnValue = true;
        }
        return returnValue;
    }
}
