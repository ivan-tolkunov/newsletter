package ua.ivan.springboot.controller;

import ua.ivan.springboot.entity.HTMLMail;
import ua.ivan.springboot.entity.Subscriber;

public class LetterThread extends Thread {
    private String subject;
    private String text;
    private Subscriber subscriber;
    private HTMLMail mm;

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public HTMLMail getMm() {
        return mm;
    }

    public void setMm(HTMLMail mm) {
        this.mm = mm;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void run() {
        sendLetter();
    }

    public void sendLetter() {
        System.out.println(subject);
        System.out.println(subscriber.getEmail());
        System.out.println(text);
        mm.sendMail("example@gmail.com",
                subscriber.getEmail(),
                subject,
                text + "<br>" + "<a href= \"/unsubscribe?id=" + subscriber.getId() + "\">Unsubscribe</a>");

    }

}