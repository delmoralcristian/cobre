package com.delmoralcristian.notifier.infraestructure.adapter.out.persistence.adapter;

import com.delmoralcristian.notifier.application.port.out.ClientPersistencePort;
import com.delmoralcristian.notifier.infraestructure.adapter.out.persistence.entity.ClientEntity;
import com.delmoralcristian.notifier.infraestructure.adapter.out.persistence.repository.JpaClientRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientPersistenceAdapter implements ClientPersistencePort {

    private final JpaClientRepository repository;


    @Override
    public Optional<ClientEntity> findById(Long id) {
        return this.repository.findById(id);
    }
}
