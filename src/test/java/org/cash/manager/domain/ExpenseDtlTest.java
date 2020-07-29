package org.cash.manager.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.cash.manager.web.rest.TestUtil;

public class ExpenseDtlTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpenseDtl.class);
        ExpenseDtl expenseDtl1 = new ExpenseDtl();
        expenseDtl1.setId(1L);
        ExpenseDtl expenseDtl2 = new ExpenseDtl();
        expenseDtl2.setId(expenseDtl1.getId());
        assertThat(expenseDtl1).isEqualTo(expenseDtl2);
        expenseDtl2.setId(2L);
        assertThat(expenseDtl1).isNotEqualTo(expenseDtl2);
        expenseDtl1.setId(null);
        assertThat(expenseDtl1).isNotEqualTo(expenseDtl2);
    }
}
