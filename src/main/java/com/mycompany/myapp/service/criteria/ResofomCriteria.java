package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Resofom} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ResofomResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /resofoms?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResofomCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter limite_fc;

    private DoubleFilter limite_fom;

    private LongFilter resolucionId;

    private LongFilter gerdisId;

    private Boolean distinct;

    public ResofomCriteria() {}

    public ResofomCriteria(ResofomCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.limite_fc = other.limite_fc == null ? null : other.limite_fc.copy();
        this.limite_fom = other.limite_fom == null ? null : other.limite_fom.copy();
        this.resolucionId = other.resolucionId == null ? null : other.resolucionId.copy();
        this.gerdisId = other.gerdisId == null ? null : other.gerdisId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ResofomCriteria copy() {
        return new ResofomCriteria(this);
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

    public DoubleFilter getLimite_fc() {
        return limite_fc;
    }

    public DoubleFilter limite_fc() {
        if (limite_fc == null) {
            limite_fc = new DoubleFilter();
        }
        return limite_fc;
    }

    public void setLimite_fc(DoubleFilter limite_fc) {
        this.limite_fc = limite_fc;
    }

    public DoubleFilter getLimite_fom() {
        return limite_fom;
    }

    public DoubleFilter limite_fom() {
        if (limite_fom == null) {
            limite_fom = new DoubleFilter();
        }
        return limite_fom;
    }

    public void setLimite_fom(DoubleFilter limite_fom) {
        this.limite_fom = limite_fom;
    }

    public LongFilter getResolucionId() {
        return resolucionId;
    }

    public LongFilter resolucionId() {
        if (resolucionId == null) {
            resolucionId = new LongFilter();
        }
        return resolucionId;
    }

    public void setResolucionId(LongFilter resolucionId) {
        this.resolucionId = resolucionId;
    }

    public LongFilter getGerdisId() {
        return gerdisId;
    }

    public LongFilter gerdisId() {
        if (gerdisId == null) {
            gerdisId = new LongFilter();
        }
        return gerdisId;
    }

    public void setGerdisId(LongFilter gerdisId) {
        this.gerdisId = gerdisId;
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
        final ResofomCriteria that = (ResofomCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(limite_fc, that.limite_fc) &&
            Objects.equals(limite_fom, that.limite_fom) &&
            Objects.equals(resolucionId, that.resolucionId) &&
            Objects.equals(gerdisId, that.gerdisId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, limite_fc, limite_fom, resolucionId, gerdisId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResofomCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (limite_fc != null ? "limite_fc=" + limite_fc + ", " : "") +
            (limite_fom != null ? "limite_fom=" + limite_fom + ", " : "") +
            (resolucionId != null ? "resolucionId=" + resolucionId + ", " : "") +
            (gerdisId != null ? "gerdisId=" + gerdisId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
