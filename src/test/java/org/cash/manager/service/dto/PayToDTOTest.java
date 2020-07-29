package org.cash.manager.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.cash.manager.web.rest.TestUtil;

public class PayToDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PayToDTO.class);
        PayToDTO payToDTO1 = new PayToDTO();
        payToDTO1.setId(1L);
        PayToDTO payToDTO2 = new PayToDTO();
        assertThat(payToDTO1).isNotEqualTo(payToDTO2);
        payToDTO2.setId(payToDTO1.getId());
        assertThat(payToDTO1).isEqualTo(payToDTO2);
        payToDTO2.setId(2L);
        assertThat(payToDTO1).isNotEqualTo(payToDTO2);
        payToDTO1.setId(null);
        assertThat(payToDTO1).isNotEqualTo(payToDTO2);
    }
}
