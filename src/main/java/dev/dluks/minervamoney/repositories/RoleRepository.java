package dev.dluks.minervamoney.repositories;

import dev.dluks.minervamoney.entities.Role;
import dev.dluks.minervamoney.entities.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleEnum name);

}
