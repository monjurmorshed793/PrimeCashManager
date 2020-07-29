package org.cash.manager.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DepositMapperTest {

    private DepositMapper depositMapper;

    @BeforeEach
    public void setUp() {
        depositMapper = new DepositMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(depositMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(depositMapper.fromId(null)).isNull();
    }
}
