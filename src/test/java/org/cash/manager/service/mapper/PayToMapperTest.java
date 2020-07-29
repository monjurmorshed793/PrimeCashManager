package org.cash.manager.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PayToMapperTest {

    private PayToMapper payToMapper;

    @BeforeEach
    public void setUp() {
        payToMapper = new PayToMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(payToMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(payToMapper.fromId(null)).isNull();
    }
}
