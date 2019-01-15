package dv.project.movienight.repositories;

import dv.project.movienight.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
