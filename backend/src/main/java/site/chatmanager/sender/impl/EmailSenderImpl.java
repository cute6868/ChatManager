package site.chatmanager.sender.impl;

import site.chatmanager.sender.EmailSender;

public class EmailSenderImpl implements EmailSender {

    @Override
    public boolean sendEmailVerificationCode(String email, String code) {
        return false;
    }

}
