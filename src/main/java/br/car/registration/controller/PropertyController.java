package br.car.registration.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import br.car.registration.api.v1.PropertyApi;
import br.car.registration.api.v1.request.PropertyFilter;
import br.car.registration.api.v1.request.PropertyReq;
import br.car.registration.api.v1.response.PagedRes;
import br.car.registration.api.v1.response.PropertyRes;
import br.car.registration.domain.Property;
import br.car.registration.mappers.PropertyMapper;
import br.car.registration.service.PropertyService;
import jakarta.validation.Valid;

@RestController
public class PropertyController implements PropertyApi {

    private final PropertyMapper propertyMapper;
    private final PropertyService propertyService;

    public PropertyController(PropertyMapper propertyMapper, PropertyService propertyService) {
        this.propertyMapper = propertyMapper;
        this.propertyService = propertyService;
    }

    // @Override
    // public List<PropertyRes> getProperties() {
    // return
    // propertyService.getAllProperties().stream().map(propertyMapper::toResponse).toList();

    // }

    @Override
    public PropertyRes getProperty(UUID id) {
        Optional<Property> prop = propertyService.getProperty(id);
        if (prop.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found");
        } else {
            return propertyMapper.toResponse(prop.get());
        }
    }

    @Override
    public PropertyRes addProperty(@Valid @RequestPart("property") PropertyReq propertyReq,
            @RequestPart(value = "mapImage", required = false) MultipartFile mapImage) {
        if (mapImage != null) {
            propertyReq.setMapImage(mapImage);
        }
        Property entity = propertyMapper.toEntity(propertyReq);
        return propertyMapper.toResponse(propertyService.addProperty(entity));
    }

    @Override
    public PropertyRes updateProperty(@Valid @RequestPart("property") PropertyReq propertyReq,
            @RequestPart(value = "mapImage", required = false) MultipartFile mapImage, UUID id) {
        if (mapImage != null) {
            propertyReq.setMapImage(mapImage);
        }
        Property entity = propertyMapper.toEntity(propertyReq);
        entity.setId(id);

        return propertyMapper.toResponse(propertyService.updateProperty(entity));
    }

    @Override
    public ResponseEntity<byte[]> getPropertyImage(@PathVariable UUID id) {
        Property property = propertyService.getProperty(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Propriedade não encontrada"));

        if (property.getMapImage() == null || property.getMapImage().length == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagem não encontrada");
        }

        // .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header("Content-Disposition", "inline; filename=\"map.png\"")
                .body(property.getMapImage());
    }

    public ResponseEntity<byte[]> getReceipt(UUID id, @RequestParam String locationZone) {
        byte[] receipt = propertyService.generatePropertyReceipt(id, locationZone);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header("Content-Disposition", "inline; filename=\"receipt.pdf\"")
                .body(receipt);
    }

    @Override
    public PagedRes<PropertyRes> getProperties(PropertyFilter filter, Pageable pageable) {
        Page<Property> page = propertyService.getFilteredProperties(filter, pageable);
        List<PropertyRes> properties = page.map(propertyMapper::toResponse).getContent();
        return new PagedRes<>(
            properties, 
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.hasNext()
        );
    }
}
