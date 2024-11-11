package dev.dluks.minervamoney.repositories;

import dev.dluks.minervamoney.entities.Category;
import dev.dluks.minervamoney.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByOwnerIsNull();

    Set<Category> findByOwner(User user);

    @Query(value = "SELECT * FROM categories WHERE owner_id = :ownerId AND LOWER(name) = LOWER(:name)", nativeQuery = true)
    Optional<Category> findByOwnerIdAndName(@Param("ownerId") UUID ownerId, @Param("name") String name);
}
