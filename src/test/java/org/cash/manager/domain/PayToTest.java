package org.cash.manager.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.cash.manager.web.rest.TestUtil;

public class PayToTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PayTo.class);
        PayTo payTo1 = new PayTo();
        payTo1.setId(1L);
        PayTo payTo2 = new PayTo();
        payTo2.setId(payTo1.getId());
        assertThat(payTo1).isEqualTo(payTo2);
        payTo2.setId(2L);
        assertThat(payTo1).isNotEqualTo(payTo2);
        payTo1.setId(null);
        assertThat(payTo1).isNotEqualTo(payTo2);
    }
}
