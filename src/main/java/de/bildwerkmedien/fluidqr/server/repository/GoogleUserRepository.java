package de.bildwerkmedien.fluidqr.server.repository;

import de.bildwerkmedien.fluidqr.server.domain.GoogleUser;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the GoogleUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GoogleUserRepository extends JpaRepository<GoogleUser, Long> {
    @Query("select googleUser from GoogleUser googleUser where googleUser.user.login = ?#{principal.username}")
    List<GoogleUser> findByUserIsCurrentUser();
}
