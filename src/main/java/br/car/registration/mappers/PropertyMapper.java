package br.car.registration.mappers;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.geojson.GeoJsonObject;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Geometry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.car.registration.api.v1.request.OwnerReq;
import br.car.registration.api.v1.request.PropertyDocumentReq;
import br.car.registration.api.v1.request.PropertyReq;
import br.car.registration.api.v1.request.RegistrarReq;
import br.car.registration.api.v1.request.RepresentativeReq;
import br.car.registration.api.v1.request.SubAreaReq;
import br.car.registration.api.v1.response.AttributeRes;
import br.car.registration.api.v1.response.OwnerRes;
import br.car.registration.api.v1.response.PropertyDocumentRes;
import br.car.registration.api.v1.response.PropertyRes;
import br.car.registration.api.v1.response.RegistrarRes;
import br.car.registration.api.v1.response.RepresentativeRes;
import br.car.registration.api.v1.response.SubAreaRes;
import br.car.registration.domain.Ownership;
import br.car.registration.domain.Person;
import br.car.registration.domain.Property;
import br.car.registration.domain.PropertyDocument;
import br.car.registration.domain.Registrarship;
import br.car.registration.domain.Representativeship;
import br.car.registration.domain.SubArea;
import br.car.registration.domain.TemporalEntity;
import br.car.registration.domain.attributes.PersonAttribute;
import br.car.registration.domain.attributes.PropertyAttribute;
import br.car.registration.domain.attributes.PropertyDocumentAttribute;
import br.car.registration.domain.attributes.SubAreaAttribute;

@Mapper(componentModel = "spring")
@Component
public interface PropertyMapper {

    @Named("toGeometry")
    static Geometry geoJsonToGeometry(GeoJsonObject geoJson) throws IOException {
        if (geoJson == null) {
            return null;
        }
        Geometry geom = new GeometryJSON().read(new ObjectMapper().writeValueAsString(geoJson));
        geom.setSRID(4326);
        return geom;
    }

    @Named("toGeoJson")
    static GeoJsonObject geometryToGeoJson(Geometry geometry) throws IOException {
        if (geometry == null) {
            return null;
        }
        GeometryJSON geometryJSON = new GeometryJSON();

        // Write the geometry to a JSON string
        StringWriter writer = new StringWriter();
        geometryJSON.write(geometry, writer);
        String json = writer.toString();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, GeoJsonObject.class);
    }

    @Named("toRegistrarship")
    default List<Registrarship> toRegistrarship(RegistrarReq registrarReq) {
        if (registrarReq == null) {
            return List.of();
        }
        Registrarship registrarship = new Registrarship();

        registrarship.setFromDate(java.time.LocalDate.now());
        registrarship.setRegistrar(toPerson(registrarReq));

        return List.of(registrarship);
    }

    @Named("toRepresentativeship")
    default List<Representativeship> toRepresentativeship(RepresentativeReq representativeReq) {
        if (representativeReq == null) {
            return List.of();
        }

        Representativeship representativeship = new Representativeship();
        representativeship.setFromDate(java.time.LocalDate.now());
        representativeship.setRepresentative(toPerson(representativeReq));

        return List.of(representativeship);
    }

    @Named("toOwnerRes")
    default List<OwnerRes> toOwnerRes(List<Ownership> ownerships) {
        List<Ownership> validToday = findAllValidToday(ownerships);
        if (validToday.isEmpty()) {
            return null;
        }

        return validToday.stream().map(ownership -> toOwnerRes(ownership.getOwner())).collect(Collectors.toList());
    }

    @Named("toRegistrarRes")
    default RegistrarRes toRegistrarRes(List<Registrarship> registrarships) {
        List<Registrarship> validToday = findAllValidToday(registrarships);
        if (validToday.isEmpty()) {
            return null;
        }

        return toRegistrarRes(validToday.getFirst().getRegistrar());
    }

    @Named("toRepresentativeRes")
    default RepresentativeRes toRepresentativeRes(List<Representativeship> representativeships) {
        List<Representativeship> validToday = findAllValidToday(representativeships);
        if (validToday.isEmpty()) {
            return null;
        }

        return toRepresentativeRes(validToday.getFirst().getRepresentative());
    }

    static <T extends TemporalEntity> List<T> findAllValidToday(List<T> temporalEntities) {
        LocalDate today = LocalDate.now();
        return temporalEntities.stream().filter(entity -> entity.isValidAt(today)).collect(Collectors.toList());
    }

    @Named("mapImageToByteArray")
    default byte[] mapImageToByteArray(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Falha ao converter imagem para byte[]", e);
        }
    }

    @Mapping(target = "attributes", source = "attributes")
    RepresentativeRes toRepresentativeRes(Person person);

    @Mapping(target = "attributes", source = "attributes")
    RegistrarRes toRegistrarRes(Person person);

    @Mapping(target = "attributes", source = "attributes")
    OwnerRes toOwnerRes(Person person);

    @Mapping(target = "attributes", source = "attributes", ignore = true)
    Person toPerson(OwnerReq ownerReq);

    @Mapping(target = "attributes", source = "attributes", ignore = true)
    Person toPerson(RepresentativeReq representativeReq);

    @Mapping(target = "attributes", source = "attributes", ignore = true)
    Person toPerson(RegistrarReq registrarReq);

    @Mapping(target = "shipId", ignore = true)
    @Mapping(target = "fromDate", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "owner.name", source = "name")
    @Mapping(target = "owner.mothersName", source = "mothersName")
    @Mapping(target = "owner.dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "owner.attributes", source = "attributes")
    @Mapping(target = "owner.identifier", source = "identifier")
    Ownership toOwnership(OwnerReq ownerReq);

    PropertyDocument toEntity(PropertyDocumentReq propertyDocumentReq);

    @Mapping(target = "attributes", source = "attributes")
    PropertyDocumentRes toResponse(PropertyDocument propertyDocument);

    @Mapping(target = "ownerships", source = "owners")
    @Mapping(target = "registrarships", source = "registrar", qualifiedByName = "toRegistrarship")
    @Mapping(target = "representativeships", source = "representative", qualifiedByName = "toRepresentativeship")
    @Mapping(target = "mapImage", source = "mapImage", qualifiedByName = "mapImageToByteArray")
    @Mapping(target = "geometry", source = "mainArea.geometry", qualifiedByName = "toGeometry")
    @Mapping(source = "mainArea.area", target = "area")
    @Mapping(source = "mainArea.lat", target = "latitude")
    @Mapping(source = "mainArea.lon", target = "longitude")
    Property toEntity(PropertyReq propertyReq);

    @Mapping(target = "geometry", source = "geometry", qualifiedByName = "toGeometry")
    @Mapping(target = "attributes", source = "attributes", ignore = true)
    SubArea toEntity(SubAreaReq subAreaReq);

    @Mapping(target = "geometry", source = "geometry", qualifiedByName = "toGeoJson")
    @Mapping(target = "attributes", source = "attributes")
    SubAreaRes toResponse(SubArea subArea);

    @Mapping(target = "value", expression = "java(propertyAttribute.getValue() != null ? propertyAttribute.getValue().toString() : null)")
    AttributeRes toAttributeRes(PropertyAttribute propertyAttribute);

    @Mapping(target = "value", expression = "java(personAttribute.getValue() != null ? personAttribute.getValue().toString() : null)")
    AttributeRes toAttributeRes(PersonAttribute personAttribute);

    @Mapping(target = "value", expression = "java(subAreaAttribute.getValue() != null ? subAreaAttribute.getValue().toString() : null)")
    AttributeRes toAttributeRes(SubAreaAttribute subAreaAttribute);

    @Mapping(target = "value", expression = "java(propertyDocumentAttribute.getValue() != null ? propertyDocumentAttribute.getValue().toString() : null)")
    AttributeRes toAttributeRes(PropertyDocumentAttribute propertyDocumentAttribute);

    @Mapping(target = "owners", source = "ownerships", qualifiedByName = "toOwnerRes")
    @Mapping(target = "registrar", source = "registrarships", qualifiedByName = "toRegistrarRes")
    @Mapping(target = "representative", source = "representativeships", qualifiedByName = "toRepresentativeRes")
    @Mapping(target = "attributes", source = "attributes")
    @Mapping(target = "mainArea.geometry", source = "geometry", qualifiedByName = "toGeoJson")
    @Mapping(source = "area", target = "mainArea.area")
    @Mapping(source = "latitude", target = "mainArea.lat")
    @Mapping(source = "longitude", target = "mainArea.lon")
    PropertyRes toResponse(Property property);
}
