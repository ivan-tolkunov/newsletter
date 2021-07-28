package ua.ivan.springboot.entity;

import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface SubscriberRepository extends CrudRepository<Subscriber, Integer> {

}