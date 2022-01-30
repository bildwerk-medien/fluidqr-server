package de.bildwerkmedien.fluidqr.server.repository;

import de.bildwerkmedien.fluidqr.server.domain.QrCode;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the QrCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, Long>, JpaSpecificationExecutor<QrCode> {
    @Query("select qrCode from QrCode qrCode where qrCode.user.login = ?#{principal.username}")
    List<QrCode> findByUserIsCurrentUser();
}
