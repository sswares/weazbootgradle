package net.weaz.data.repositories;

import net.weaz.data.models.AuthCustomUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomUserRepository extends CrudRepository<AuthCustomUser, Long> {

    AuthCustomUser findByUsername(String username);
}