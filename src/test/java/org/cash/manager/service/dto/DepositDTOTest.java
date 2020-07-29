package org.cash.manager.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.cash.manager.web.rest.TestUtil;

public class DepositDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepositDTO.class);
        DepositDTO depositDTO1 = new DepositDTO();
        depositDTO1.setId(1L);
        DepositDTO depositDTO2 = new DepositDTO();
        assertThat(depositDTO1).isNotEqualTo(depositDTO2);
        depositDTO2.setId(depositDTO1.getId());
        assertThat(depositDTO1).isEqualTo(depositDTO2);
        depositDTO2.setId(2L);
        assertThat(depositDTO1).isNotEqualTo(depositDTO2);
        depositDTO1.setId(null);
        assertThat(depositDTO1).isNotEqualTo(depositDTO2);
    }
}
