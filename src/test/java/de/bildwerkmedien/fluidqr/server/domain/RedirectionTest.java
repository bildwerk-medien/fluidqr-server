package de.bildwerkmedien.fluidqr.server.domain;

import static de.bildwerkmedien.fluidqr.server.domain.QrCodeTestSamples.*;
import static de.bildwerkmedien.fluidqr.server.domain.RedirectionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.bildwerkmedien.fluidqr.server.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RedirectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Redirection.class);
        Redirection redirection1 = getRedirectionSample1();
        Redirection redirection2 = new Redirection();
        assertThat(redirection1).isNotEqualTo(redirection2);

        redirection2.setId(redirection1.getId());
        assertThat(redirection1).isEqualTo(redirection2);

        redirection2 = getRedirectionSample2();
        assertThat(redirection1).isNotEqualTo(redirection2);
    }

    @Test
    void qrCodeTest() throws Exception {
        Redirection redirection = getRedirectionRandomSampleGenerator();
        QrCode qrCodeBack = getQrCodeRandomSampleGenerator();

        redirection.setQrCode(qrCodeBack);
        assertThat(redirection.getQrCode()).isEqualTo(qrCodeBack);

        redirection.qrCode(null);
        assertThat(redirection.getQrCode()).isNull();
    }
}
