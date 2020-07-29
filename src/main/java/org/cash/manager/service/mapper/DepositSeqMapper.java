package org.cash.manager.service.mapper;


import org.cash.manager.domain.*;
import org.cash.manager.service.dto.DepositSeqDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link DepositSeq} and its DTO {@link DepositSeqDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DepositSeqMapper extends EntityMapper<DepositSeqDTO, DepositSeq> {



    default DepositSeq fromId(Long id) {
        if (id == null) {
            return null;
        }
        DepositSeq depositSeq = new DepositSeq();
        depositSeq.setId(id);
        return depositSeq;
    }
}
