package br.car.registration.domain;

import java.util.List;

import org.locationtech.jts.geom.Geometry;

import br.car.registration.domain.attributes.AttributableEntity;
import br.car.registration.domain.attributes.SubAreaAttribute;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "sub_area")
public class SubArea extends AttributableEntity {

    @Column(columnDefinition = "geometry(Geometry,4326)") // GIS Support (Assumes PostGIS for PostgreSQL)
    private Geometry geometry;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(name = "area")
    private Double area;

    @Column(name = "area_type")
    private String areaType;

    @OneToMany(mappedBy = "subArea", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubAreaAttribute> attributes;
}
