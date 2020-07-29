package org.cash.manager.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.cash.manager.web.rest.TestUtil;

public class ExpanseSeqTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpanseSeq.class);
        ExpanseSeq expanseSeq1 = new ExpanseSeq();
        expanseSeq1.setId(1L);
        ExpanseSeq expanseSeq2 = new ExpanseSeq();
        expanseSeq2.setId(expanseSeq1.getId());
        assertThat(expanseSeq1).isEqualTo(expanseSeq2);
        expanseSeq2.setId(2L);
        assertThat(expanseSeq1).isNotEqualTo(expanseSeq2);
        expanseSeq1.setId(null);
        assertThat(expanseSeq1).isNotEqualTo(expanseSeq2);
    }
}
