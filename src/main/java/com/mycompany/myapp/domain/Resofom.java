package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Resofom.
 */
@Entity
@Table(name = "resofom")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Resofom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "limite_fc")
    private Double limite_fc;

    @Column(name = "limite_fom")
    private Double limite_fom;

    @ManyToOne(optional = false)
    @NotNull
    private Resolucion resolucion;

    @ManyToOne(optional = false)
    @NotNull
    private Gerdis gerdis;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Resofom id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLimite_fc() {
        return this.limite_fc;
    }

    public Resofom limite_fc(Double limite_fc) {
        this.setLimite_fc(limite_fc);
        return this;
    }

    public void setLimite_fc(Double limite_fc) {
        this.limite_fc = limite_fc;
    }

    public Double getLimite_fom() {
        return this.limite_fom;
    }

    public Resofom limite_fom(Double limite_fom) {
        this.setLimite_fom(limite_fom);
        return this;
    }

    public void setLimite_fom(Double limite_fom) {
        this.limite_fom = limite_fom;
    }

    public Resolucion getResolucion() {
        return this.resolucion;
    }

    public void setResolucion(Resolucion resolucion) {
        this.resolucion = resolucion;
    }

    public Resofom resolucion(Resolucion resolucion) {
        this.setResolucion(resolucion);
        return this;
    }

    public Gerdis getGerdis() {
        return this.gerdis;
    }

    public void setGerdis(Gerdis gerdis) {
        this.gerdis = gerdis;
    }

    public Resofom gerdis(Gerdis gerdis) {
        this.setGerdis(gerdis);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Resofom)) {
            return false;
        }
        return id != null && id.equals(((Resofom) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Resofom{" +
            "id=" + getId() +
            ", limite_fc=" + getLimite_fc() +
            ", limite_fom=" + getLimite_fom() +
            "}";
    }
}
