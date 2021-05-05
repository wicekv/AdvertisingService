package net.javaguides.springboot.springsecurity.repository;

import net.javaguides.springboot.springsecurity.model.Photos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotosRepository extends JpaRepository<Photos, Long> {

}
