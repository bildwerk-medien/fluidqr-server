package de.bildwerkmedien.fluidqr.server.domain;

import static org.assertj.core.api.Assertions.assertThat;

import de.bildwerkmedien.fluidqr.server.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GoogleUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GoogleUser.class);
        GoogleUser googleUser1 = new GoogleUser();
        googleUser1.setId(1L);
        GoogleUser googleUser2 = new GoogleUser();
        googleUser2.setId(googleUser1.getId());
        assertThat(googleUser1).isEqualTo(googleUser2);
        googleUser2.setId(2L);
        assertThat(googleUser1).isNotEqualTo(googleUser2);
        googleUser1.setId(null);
        assertThat(googleUser1).isNotEqualTo(googleUser2);
    }
}
