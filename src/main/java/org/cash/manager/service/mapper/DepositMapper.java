package org.cash.manager.service.mapper;


import org.cash.manager.domain.*;
import org.cash.manager.service.dto.DepositDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Deposit} and its DTO {@link DepositDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DepositMapper extends EntityMapper<DepositDTO, Deposit> {



    default Deposit fromId(Long id) {
        if (id == null) {
            return null;
        }
        Deposit deposit = new Deposit();
        deposit.setId(id);
        return deposit;
    }
}
