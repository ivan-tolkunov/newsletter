package ua.ivan.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import ua.ivan.springboot.entity.HTMLMail;
import ua.ivan.springboot.entity.Subscriber;
import ua.ivan.springboot.entity.SubscriberRepository;
import ua.ivan.springboot.model.ModelLetter;

import java.util.ArrayList;
import java.util.List;

@Controller
@Async("sendEmail")
public class MainController {
    private ModelLetter modelLetter = new ModelLetter();
    private ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("Spring-Mail.xml");
    private HTMLMail mm = (HTMLMail) context.getBean("htmlMail");

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @GetMapping("/")
    public String registration() {
        return "registration";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/email")
    public String setEmail(@RequestParam String email) {
        modelLetter.addEmail(email, subscriberRepository);
        return "added";
    }

    @RequestMapping(value = "/log_as_admin")
    public String login(@RequestParam("login") String login, @RequestParam("password") String password, Model model) {
        return modelLetter.checkPerson(login, password);
    }

    @RequestMapping(value = "/check")
    public String check(@RequestParam("subject") String subject, @RequestParam("text_of_letter") String text, Model model) {
        model.addAttribute("subject", subject);
        model.addAttribute("html", markdownToHTML(text));
        model.addAttribute("subsAmount", parseSubscribers().size());
        return "view";
    }

    @RequestMapping(value = "/send")
    public String send(@RequestParam("subject") String subject, @RequestParam("text") String text) {
        sendEmail(subject, text);
        return "admin";
    }

    public void sendEmail(String subject, String text) {
        List<LetterThread> letterThreads = new ArrayList<>();
        int i = 0;
        for (Subscriber s : modelLetter.getAllSubs(subscriberRepository)) {
            letterThreads.add(new LetterThread());
        }
        for (LetterThread thread : letterThreads) {
            thread.setSubject(subject);
            thread.setText(text);
            thread.setMm(mm);
            thread.setSubscriber(parseSubscribers().get(i));
            thread.start();
            i++;
        }
    }

    @GetMapping("/unsubscribe")
    public String deleteEmail(@RequestParam String id) {
        modelLetter.deleteEmail(Integer.parseInt(id), subscriberRepository);
        return "unsubscribe";
    }

    public void sendSimpleEmail(String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setBcc(parseEmail());
        msg.setSubject(subject);
        msg.setText(text);

        javaMailSender.send(msg);

    }

    private String markdownToHTML(String markdown) {
        Parser parser = Parser.builder()
                .build();

        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .build();

        return renderer.render(document);
    }

    private String[] parseEmail() {
        List<String> emails = new ArrayList<>();
        for (Subscriber s : modelLetter.getAllSubs(subscriberRepository)) {
            emails.add(s.getEmail());
        }
        return emails.toArray(new String[0]);
    }

    private List<Subscriber> parseSubscribers() {
        List<Subscriber> subscribers = new ArrayList<>();
        for (Subscriber s : modelLetter.getAllSubs(subscriberRepository)) {
            subscribers.add(s);
        }
        return subscribers;
    }

}
