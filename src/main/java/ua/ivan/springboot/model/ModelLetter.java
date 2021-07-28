package ua.ivan.springboot.model;

import ua.ivan.springboot.entity.Admin;
import ua.ivan.springboot.entity.Subscriber;
import ua.ivan.springboot.entity.SubscriberRepository;

import java.io.IOException;
import java.util.Properties;

public class ModelLetter {
    private final Admin admin = new Admin();

    public void addEmail(String email, SubscriberRepository subscriberRepository) {
        Subscriber subscriber = new Subscriber();
        subscriber.setEmail(email);
        subscriberRepository.save(subscriber);
    }

    public void deleteEmail(int id, SubscriberRepository subscriberRepository) {
        subscriberRepository.deleteById(id);
    }

    public Iterable<Subscriber> getAllSubs(SubscriberRepository subscriberRepository) {
        return subscriberRepository.findAll();
    }


    public String checkPerson(String login, String password) {
        if (admin.getLogin().equals(login) && admin.getPassword() == password.hashCode()) {
            return "admin";
        }
        return "login";
    }

}
