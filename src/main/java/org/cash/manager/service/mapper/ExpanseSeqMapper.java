package org.cash.manager.service.mapper;


import org.cash.manager.domain.*;
import org.cash.manager.service.dto.ExpanseSeqDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExpanseSeq} and its DTO {@link ExpanseSeqDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ExpanseSeqMapper extends EntityMapper<ExpanseSeqDTO, ExpanseSeq> {



    default ExpanseSeq fromId(Long id) {
        if (id == null) {
            return null;
        }
        ExpanseSeq expanseSeq = new ExpanseSeq();
        expanseSeq.setId(id);
        return expanseSeq;
    }
}
