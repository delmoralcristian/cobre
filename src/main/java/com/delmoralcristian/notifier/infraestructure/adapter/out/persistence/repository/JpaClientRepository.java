package com.delmoralcristian.notifier.infraestructure.adapter.out.persistence.repository;

import com.delmoralcristian.notifier.infraestructure.adapter.out.persistence.entity.ClientEntity;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaClientRepository extends CrudRepository<ClientEntity, Long> {

    Optional<ClientEntity> findById(Long id);

}
