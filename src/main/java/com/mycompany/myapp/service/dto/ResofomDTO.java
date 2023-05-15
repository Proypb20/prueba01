package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Resofom} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResofomDTO implements Serializable {

    private Long id;

    private Double limite_fc;

    private Double limite_fom;

    private ResolucionDTO resolucion;

    private GerdisDTO gerdis;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLimite_fc() {
        return limite_fc;
    }

    public void setLimite_fc(Double limite_fc) {
        this.limite_fc = limite_fc;
    }

    public Double getLimite_fom() {
        return limite_fom;
    }

    public void setLimite_fom(Double limite_fom) {
        this.limite_fom = limite_fom;
    }

    public ResolucionDTO getResolucion() {
        return resolucion;
    }

    public void setResolucion(ResolucionDTO resolucion) {
        this.resolucion = resolucion;
    }

    public GerdisDTO getGerdis() {
        return gerdis;
    }

    public void setGerdis(GerdisDTO gerdis) {
        this.gerdis = gerdis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResofomDTO)) {
            return false;
        }

        ResofomDTO resofomDTO = (ResofomDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, resofomDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResofomDTO{" +
            "id=" + getId() +
            ", limite_fc=" + getLimite_fc() +
            ", limite_fom=" + getLimite_fom() +
            ", resolucion=" + getResolucion() +
            ", gerdis=" + getGerdis() +
            "}";
    }
}
