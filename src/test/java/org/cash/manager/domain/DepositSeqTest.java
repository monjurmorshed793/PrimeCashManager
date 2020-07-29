package org.cash.manager.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.cash.manager.web.rest.TestUtil;

public class DepositSeqTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepositSeq.class);
        DepositSeq depositSeq1 = new DepositSeq();
        depositSeq1.setId(1L);
        DepositSeq depositSeq2 = new DepositSeq();
        depositSeq2.setId(depositSeq1.getId());
        assertThat(depositSeq1).isEqualTo(depositSeq2);
        depositSeq2.setId(2L);
        assertThat(depositSeq1).isNotEqualTo(depositSeq2);
        depositSeq1.setId(null);
        assertThat(depositSeq1).isNotEqualTo(depositSeq2);
    }
}
