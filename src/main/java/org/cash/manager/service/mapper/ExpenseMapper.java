package org.cash.manager.service.mapper;


import org.cash.manager.domain.*;
import org.cash.manager.service.dto.ExpenseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Expense} and its DTO {@link ExpenseDTO}.
 */
@Mapper(componentModel = "spring", uses = {PayToMapper.class})
public interface ExpenseMapper extends EntityMapper<ExpenseDTO, Expense> {

    @Mapping(source = "payTo.id", target = "payToId")
    @Mapping(source = "payTo.name", target = "payToName")
    ExpenseDTO toDto(Expense expense);

    @Mapping(target = "expanseDtls", ignore = true)
    @Mapping(target = "removeExpanseDtl", ignore = true)
    @Mapping(source = "payToId", target = "payTo")
    Expense toEntity(ExpenseDTO expenseDTO);

    default Expense fromId(Long id) {
        if (id == null) {
            return null;
        }
        Expense expense = new Expense();
        expense.setId(id);
        return expense;
    }
}
