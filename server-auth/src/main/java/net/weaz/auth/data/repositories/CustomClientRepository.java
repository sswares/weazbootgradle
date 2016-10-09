package net.weaz.auth.data.repositories;

import net.weaz.auth.data.models.CustomClient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomClientRepository extends CrudRepository<CustomClient, Long> {

    CustomClient findByClientName(String clientName);
}