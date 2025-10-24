package br.car.registration.api.v1;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.car.registration.api.v1.request.PropertyFilter;
import br.car.registration.api.v1.request.PropertyReq;
import br.car.registration.api.v1.response.PagedRes;
import br.car.registration.api.v1.response.PropertyRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RequestMapping("/v1/properties")
@Tag(name = "Property", description = "The Property API")
public interface PropertyApi {

        @Operation(summary = "Get properties by filter", description = "Retrieve properties based on dinamic filters")
        @ApiResponse(responseCode = "200", description = "Properties successfully retrieved")
        @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
        PagedRes<PropertyRes> getProperties(PropertyFilter filter, Pageable pageable);

        @Operation(summary = "Get property by ID", description = "Retrieve a property by its unique identifier")
        @ApiResponse(responseCode = "200", description = "Property successfully retrieved")
        @ApiResponse(responseCode = "404", description = "Property not found")
        @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
        PropertyRes getProperty(
                        @Parameter(description = "Unique identifier of the property", required = true) @PathVariable("id") UUID id);

        @Operation(summary = "Add a new property", description = "Create a new property using the provided details")
        @ApiResponse(responseCode = "200", description = "Property successfully created")
        @ApiResponse(responseCode = "400", description = "Invalid request payload")
        @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        PropertyRes addProperty(
                        @Parameter(description = "Property details", required = true) @RequestPart("property") @Valid PropertyReq propertyReq,
                        @RequestPart(value = "mapImage", required = false) MultipartFile mapImage);

        @Operation(summary = "Add a new property", description = "Update an existing property using the provided details")
        @ApiResponse(responseCode = "200", description = "Property successfully updated")
        @ApiResponse(responseCode = "400", description = "Invalid request payload")
        @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        PropertyRes updateProperty(
                        @Parameter(description = "Property details", required = true) @RequestPart("property") @Valid PropertyReq propertyReq,
                        @RequestPart(value = "mapImage", required = false) MultipartFile mapImage,
                        @PathVariable UUID id);

        @GetMapping(path = "/{id}/image")
        ResponseEntity<byte[]> getPropertyImage(@PathVariable UUID id);

        @Operation(summary = "Get the property receipt by ID", description = "Generate the pdf receipt of a property")
        @ApiResponse(responseCode = "200", description = "Receipt successfully generated")
        @ApiResponse(responseCode = "404", description = "Property not found")
        @GetMapping(path = "/{id}/receipt", produces = MediaType.APPLICATION_PDF_VALUE)
        ResponseEntity<byte[]> getReceipt(
                        @Parameter(description = "Unique identifier of the property", required = true) @PathVariable("id") UUID id,
                        @RequestParam String locationZone);

}
