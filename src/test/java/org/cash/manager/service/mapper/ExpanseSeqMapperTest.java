package org.cash.manager.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ExpanseSeqMapperTest {

    private ExpanseSeqMapper expanseSeqMapper;

    @BeforeEach
    public void setUp() {
        expanseSeqMapper = new ExpanseSeqMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(expanseSeqMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(expanseSeqMapper.fromId(null)).isNull();
    }
}
