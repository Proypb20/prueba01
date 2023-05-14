package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Resolucion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResolucionDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer resolucion;

    private LocalDate fecha;

    @Size(max = 10)
    private String expediente;

    private Integer resolucionb;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getResolucion() {
        return resolucion;
    }

    public void setResolucion(Integer resolucion) {
        this.resolucion = resolucion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public Integer getResolucionb() {
        return resolucionb;
    }

    public void setResolucionb(Integer resolucionb) {
        this.resolucionb = resolucionb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResolucionDTO)) {
            return false;
        }

        ResolucionDTO resolucionDTO = (ResolucionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, resolucionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResolucionDTO{" +
            "id=" + getId() +
            ", resolucion=" + getResolucion() +
            ", fecha='" + getFecha() + "'" +
            ", expediente='" + getExpediente() + "'" +
            ", resolucionb=" + getResolucionb() +
            "}";
    }
}
