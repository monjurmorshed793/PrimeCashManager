package org.cash.manager.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.cash.manager.web.rest.TestUtil;

public class ExpanseSeqDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpanseSeqDTO.class);
        ExpanseSeqDTO expanseSeqDTO1 = new ExpanseSeqDTO();
        expanseSeqDTO1.setId(1L);
        ExpanseSeqDTO expanseSeqDTO2 = new ExpanseSeqDTO();
        assertThat(expanseSeqDTO1).isNotEqualTo(expanseSeqDTO2);
        expanseSeqDTO2.setId(expanseSeqDTO1.getId());
        assertThat(expanseSeqDTO1).isEqualTo(expanseSeqDTO2);
        expanseSeqDTO2.setId(2L);
        assertThat(expanseSeqDTO1).isNotEqualTo(expanseSeqDTO2);
        expanseSeqDTO1.setId(null);
        assertThat(expanseSeqDTO1).isNotEqualTo(expanseSeqDTO2);
    }
}
