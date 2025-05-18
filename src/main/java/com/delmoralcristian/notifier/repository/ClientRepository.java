package com.delmoralcristian.notifier.repository;

import com.delmoralcristian.notifier.model.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {

}
