package org.cash.manager.service.mapper;


import org.cash.manager.domain.*;
import org.cash.manager.service.dto.PayToDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PayTo} and its DTO {@link PayToDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PayToMapper extends EntityMapper<PayToDTO, PayTo> {



    default PayTo fromId(Long id) {
        if (id == null) {
            return null;
        }
        PayTo payTo = new PayTo();
        payTo.setId(id);
        return payTo;
    }
}
