package com.sipios.model.mail;

public enum EmailType {
    RESET_PASSWORD,
    VALIDATION_EMAIL;

    public String getTemplatePath() {
        return "mails/" + this.toString().toLowerCase();
    }
}
