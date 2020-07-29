package org.cash.manager.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.cash.manager.web.rest.TestUtil;

public class ExpenseDtlDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpenseDtlDTO.class);
        ExpenseDtlDTO expenseDtlDTO1 = new ExpenseDtlDTO();
        expenseDtlDTO1.setId(1L);
        ExpenseDtlDTO expenseDtlDTO2 = new ExpenseDtlDTO();
        assertThat(expenseDtlDTO1).isNotEqualTo(expenseDtlDTO2);
        expenseDtlDTO2.setId(expenseDtlDTO1.getId());
        assertThat(expenseDtlDTO1).isEqualTo(expenseDtlDTO2);
        expenseDtlDTO2.setId(2L);
        assertThat(expenseDtlDTO1).isNotEqualTo(expenseDtlDTO2);
        expenseDtlDTO1.setId(null);
        assertThat(expenseDtlDTO1).isNotEqualTo(expenseDtlDTO2);
    }
}
