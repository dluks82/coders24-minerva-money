package dev.dluks.minervamoney.repositories;

import dev.dluks.minervamoney.entities.Role;
import dev.dluks.minervamoney.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);

}
