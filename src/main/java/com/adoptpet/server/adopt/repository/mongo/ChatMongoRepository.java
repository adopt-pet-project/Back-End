package com.adoptpet.server.adopt.repository.mongo;

import com.adoptpet.server.adopt.domain.mongo.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMongoRepository extends MongoRepository<Customer, String> {

    Customer findByFirstName(String firstName);
}
