package de.bildwerkmedien.fluidqr.server.domain;

import static org.assertj.core.api.Assertions.assertThat;

import de.bildwerkmedien.fluidqr.server.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QrCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QrCode.class);
        QrCode qrCode1 = new QrCode();
        qrCode1.setId(1L);
        QrCode qrCode2 = new QrCode();
        qrCode2.setId(qrCode1.getId());
        assertThat(qrCode1).isEqualTo(qrCode2);
        qrCode2.setId(2L);
        assertThat(qrCode1).isNotEqualTo(qrCode2);
        qrCode1.setId(null);
        assertThat(qrCode1).isNotEqualTo(qrCode2);
    }
}
