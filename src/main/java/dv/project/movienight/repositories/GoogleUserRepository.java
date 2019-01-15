package dv.project.movienight.repositories;


import dv.project.movienight.entities.GoogleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoogleUserRepository extends JpaRepository <GoogleUser, Long> {
    GoogleUser findByUserId(String id);
    List<GoogleUser> getAllByUserIdNotNull();
    GoogleUser findByAccessToken(String accessToken);
}
