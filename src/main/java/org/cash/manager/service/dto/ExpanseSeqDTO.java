package org.cash.manager.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.cash.manager.domain.ExpanseSeq} entity.
 */
public class ExpanseSeqDTO implements Serializable {
    
    private Long id;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExpanseSeqDTO expanseSeqDTO = (ExpanseSeqDTO) o;
        if (expanseSeqDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), expanseSeqDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExpanseSeqDTO{" +
            "id=" + getId() +
            "}";
    }
}
