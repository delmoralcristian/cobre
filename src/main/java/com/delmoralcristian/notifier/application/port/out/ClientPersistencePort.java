package com.delmoralcristian.notifier.application.port.out;

import com.delmoralcristian.notifier.infraestructure.adapter.out.persistence.entity.ClientEntity;
import java.util.Optional;

public interface ClientPersistencePort {

    Optional<ClientEntity> findById(Long id);

}
