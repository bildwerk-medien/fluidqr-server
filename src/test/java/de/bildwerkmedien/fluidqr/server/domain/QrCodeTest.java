package de.bildwerkmedien.fluidqr.server.domain;

import static de.bildwerkmedien.fluidqr.server.domain.QrCodeTestSamples.*;
import static de.bildwerkmedien.fluidqr.server.domain.RedirectionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.bildwerkmedien.fluidqr.server.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class QrCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QrCode.class);
        QrCode qrCode1 = getQrCodeSample1();
        QrCode qrCode2 = new QrCode();
        assertThat(qrCode1).isNotEqualTo(qrCode2);

        qrCode2.setId(qrCode1.getId());
        assertThat(qrCode1).isEqualTo(qrCode2);

        qrCode2 = getQrCodeSample2();
        assertThat(qrCode1).isNotEqualTo(qrCode2);
    }

    @Test
    void redirectionTest() throws Exception {
        QrCode qrCode = getQrCodeRandomSampleGenerator();
        Redirection redirectionBack = getRedirectionRandomSampleGenerator();

        qrCode.addRedirection(redirectionBack);
        assertThat(qrCode.getRedirections()).containsOnly(redirectionBack);
        assertThat(redirectionBack.getQrCode()).isEqualTo(qrCode);

        qrCode.removeRedirection(redirectionBack);
        assertThat(qrCode.getRedirections()).doesNotContain(redirectionBack);
        assertThat(redirectionBack.getQrCode()).isNull();

        qrCode.redirections(new HashSet<>(Set.of(redirectionBack)));
        assertThat(qrCode.getRedirections()).containsOnly(redirectionBack);
        assertThat(redirectionBack.getQrCode()).isEqualTo(qrCode);

        qrCode.setRedirections(new HashSet<>());
        assertThat(qrCode.getRedirections()).doesNotContain(redirectionBack);
        assertThat(redirectionBack.getQrCode()).isNull();
    }
}
