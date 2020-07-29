package org.cash.manager.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DepositSeqMapperTest {

    private DepositSeqMapper depositSeqMapper;

    @BeforeEach
    public void setUp() {
        depositSeqMapper = new DepositSeqMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(depositSeqMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(depositSeqMapper.fromId(null)).isNull();
    }
}
