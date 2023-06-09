package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Resolucion.
 */
@Entity
@Table(name = "resolucion")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Resolucion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Size(max = 10)
    @Column(name = "expediente", length = 10)
    private String expediente;

    @NotNull
    @Column(name = "resolucion", nullable = false, unique = true)
    private String resolucion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Resolucion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public Resolucion fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getExpediente() {
        return this.expediente;
    }

    public Resolucion expediente(String expediente) {
        this.setExpediente(expediente);
        return this;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public String getResolucion() {
        return this.resolucion;
    }

    public Resolucion resolucion(String resolucion) {
        this.setResolucion(resolucion);
        return this;
    }

    public void setResolucion(String resolucion) {
        this.resolucion = resolucion;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Resolucion)) {
            return false;
        }
        return id != null && id.equals(((Resolucion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Resolucion{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", expediente='" + getExpediente() + "'" +
            ", resolucion='" + getResolucion() + "'" +
            "}";
    }
}
