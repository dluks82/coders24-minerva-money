package dev.dluks.minervamoney.repositories;

import dev.dluks.minervamoney.entities.Category;
import dev.dluks.minervamoney.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByOwnerIsNull();

    Set<Category> findByOwner(User user);
}
