package de.bildwerkmedien.fluidqr.server.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.bildwerkmedien.fluidqr.server.web.rest.TestUtil;

public class RedirectionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Redirection.class);
        Redirection redirection1 = new Redirection();
        redirection1.setId(1L);
        Redirection redirection2 = new Redirection();
        redirection2.setId(redirection1.getId());
        assertThat(redirection1).isEqualTo(redirection2);
        redirection2.setId(2L);
        assertThat(redirection1).isNotEqualTo(redirection2);
        redirection1.setId(null);
        assertThat(redirection1).isNotEqualTo(redirection2);
    }
}
