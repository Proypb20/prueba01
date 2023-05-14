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

    private LocalDate fecha;

    @Size(max = 10)
    private String expediente;

    @NotNull
    private String resolucion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getResolucion() {
        return resolucion;
    }

    public void setResolucion(String resolucion) {
        this.resolucion = resolucion;
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
            ", fecha='" + getFecha() + "'" +
            ", expediente='" + getExpediente() + "'" +
            ", resolucion='" + getResolucion() + "'" +
            "}";
    }
}
