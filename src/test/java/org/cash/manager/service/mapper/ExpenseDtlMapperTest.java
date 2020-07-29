package org.cash.manager.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ExpenseDtlMapperTest {

    private ExpenseDtlMapper expenseDtlMapper;

    @BeforeEach
    public void setUp() {
        expenseDtlMapper = new ExpenseDtlMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(expenseDtlMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(expenseDtlMapper.fromId(null)).isNull();
    }
}
