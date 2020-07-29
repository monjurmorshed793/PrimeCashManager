package org.cash.manager.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.cash.manager.web.rest.TestUtil;

public class DepositSeqDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepositSeqDTO.class);
        DepositSeqDTO depositSeqDTO1 = new DepositSeqDTO();
        depositSeqDTO1.setId(1L);
        DepositSeqDTO depositSeqDTO2 = new DepositSeqDTO();
        assertThat(depositSeqDTO1).isNotEqualTo(depositSeqDTO2);
        depositSeqDTO2.setId(depositSeqDTO1.getId());
        assertThat(depositSeqDTO1).isEqualTo(depositSeqDTO2);
        depositSeqDTO2.setId(2L);
        assertThat(depositSeqDTO1).isNotEqualTo(depositSeqDTO2);
        depositSeqDTO1.setId(null);
        assertThat(depositSeqDTO1).isNotEqualTo(depositSeqDTO2);
    }
}
