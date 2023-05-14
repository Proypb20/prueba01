package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Resolucion} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ResolucionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /resolucions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResolucionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fecha;

    private StringFilter expediente;

    private StringFilter resolucion;

    private Boolean distinct;

    public ResolucionCriteria() {}

    public ResolucionCriteria(ResolucionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fecha = other.fecha == null ? null : other.fecha.copy();
        this.expediente = other.expediente == null ? null : other.expediente.copy();
        this.resolucion = other.resolucion == null ? null : other.resolucion.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ResolucionCriteria copy() {
        return new ResolucionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getFecha() {
        return fecha;
    }

    public LocalDateFilter fecha() {
        if (fecha == null) {
            fecha = new LocalDateFilter();
        }
        return fecha;
    }

    public void setFecha(LocalDateFilter fecha) {
        this.fecha = fecha;
    }

    public StringFilter getExpediente() {
        return expediente;
    }

    public StringFilter expediente() {
        if (expediente == null) {
            expediente = new StringFilter();
        }
        return expediente;
    }

    public void setExpediente(StringFilter expediente) {
        this.expediente = expediente;
    }

    public StringFilter getResolucion() {
        return resolucion;
    }

    public StringFilter resolucion() {
        if (resolucion == null) {
            resolucion = new StringFilter();
        }
        return resolucion;
    }

    public void setResolucion(StringFilter resolucion) {
        this.resolucion = resolucion;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ResolucionCriteria that = (ResolucionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(expediente, that.expediente) &&
            Objects.equals(resolucion, that.resolucion) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fecha, expediente, resolucion, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResolucionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fecha != null ? "fecha=" + fecha + ", " : "") +
            (expediente != null ? "expediente=" + expediente + ", " : "") +
            (resolucion != null ? "resolucion=" + resolucion + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
