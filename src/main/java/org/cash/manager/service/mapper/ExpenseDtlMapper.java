package org.cash.manager.service.mapper;


import org.cash.manager.domain.*;
import org.cash.manager.service.dto.ExpenseDtlDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExpenseDtl} and its DTO {@link ExpenseDtlDTO}.
 */
@Mapper(componentModel = "spring", uses = {ItemMapper.class, ExpenseMapper.class})
public interface ExpenseDtlMapper extends EntityMapper<ExpenseDtlDTO, ExpenseDtl> {

    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "item.name", target = "itemName")
    @Mapping(source = "expense.id", target = "expenseId")
    @Mapping(source = "expense.voucherNo", target = "expenseVoucherNo")
    ExpenseDtlDTO toDto(ExpenseDtl expenseDtl);

    @Mapping(source = "itemId", target = "item")
    @Mapping(source = "expenseId", target = "expense")
    ExpenseDtl toEntity(ExpenseDtlDTO expenseDtlDTO);

    default ExpenseDtl fromId(Long id) {
        if (id == null) {
            return null;
        }
        ExpenseDtl expenseDtl = new ExpenseDtl();
        expenseDtl.setId(id);
        return expenseDtl;
    }
}
