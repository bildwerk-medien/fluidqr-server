package de.bildwerkmedien.fluidqr.server.repository;

import de.bildwerkmedien.fluidqr.server.domain.Redirection;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Redirection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RedirectionRepository extends JpaRepository<Redirection, Long>, JpaSpecificationExecutor<Redirection> {
    @Query("select redirection from Redirection redirection where redirection.user.login = ?#{principal.username}")
    List<Redirection> findByUserIsCurrentUser();
}
