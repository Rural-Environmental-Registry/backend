package br.car.registration.domain;

import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Geometry;

import br.car.registration.domain.attributes.AttributableEntity;
import br.car.registration.domain.attributes.PropertyAttribute;
import br.car.registration.enums.EntityTypesEnum;
import br.car.registration.enums.LocationZonesEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "property")
public class Property extends AttributableEntity {

    @Transient
    private static final EntityTypesEnum entityType = EntityTypesEnum.PROPERTY;

    @Column(columnDefinition = "geometry(Geometry,4326)")
    private Geometry geometry;

    @Column(name = "area")
    private Double area;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "property_name")
    private String propertyName;

    @Column(name = "state_district")
    private String stateDistrict;

    @Column(name = "municipality")
    private String municipality;

    @Column(name = "zipcode")
    private String zipcode;

    @Column(name = "code", updatable = false)
    private String code;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "map_image", columnDefinition = "BYTEA")
    private byte[] mapImage;

    @Column(name = "location_zone")
    @Enumerated(EnumType.STRING)
    private LocationZonesEnum locationZone;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubArea> subAreas;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ownership> ownerships;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("fromDate DESC")
    private List<Registrarship> registrarships;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("fromDate DESC")
    private List<Representativeship> representativeships;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PropertyAttribute> attributes;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PropertyDocument> documents;
}
