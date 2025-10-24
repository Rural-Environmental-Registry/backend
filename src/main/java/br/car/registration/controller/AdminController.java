package br.car.registration.controller;

import br.car.registration.api.v1.AdminApi;
import br.car.registration.api.v1.request.AttributeDefinitionReq;
import br.car.registration.api.v1.request.AttributeSetReq;
import br.car.registration.api.v1.response.AttributeDefinitionRes;
import br.car.registration.api.v1.response.AttributeSetRes;
import br.car.registration.enums.AttributeTypesEnum;
import br.car.registration.mappers.AttributeDefinitionMapper;
import br.car.registration.mappers.AttributeSetMapper;
import br.car.registration.service.AttributeService;
import br.car.registration.util.ReceiptGenerator;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "Admin", description = "The Admin API - this should be protected")
public class AdminController implements AdminApi {

    private final AttributeService attributeService;
    private final AttributeDefinitionMapper attributeDefinitionMapper;
    private final AttributeSetMapper attributeSetMapper;
    private final ReceiptGenerator receiptGenerator;

    private final String version;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${FRONTEND_URLS:}")
    private String frontendUrls;

    public AdminController(AttributeService attributeService, AttributeDefinitionMapper attributeDefinitionMapper,
            AttributeSetMapper attributeSetMapper, ReceiptGenerator receiptGenerator) {
        this.attributeService = attributeService;
        this.attributeDefinitionMapper = attributeDefinitionMapper;
        this.attributeSetMapper = attributeSetMapper;
        this.receiptGenerator = receiptGenerator;
        this.version = readVersionFromManifest();
    }

    @Override
    public List<AttributeSetRes> getAttributeSets() {
        return attributeService.getAttributeSets().stream().map(attributeSetMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AttributeSetRes createAttributeSet(@RequestBody AttributeSetReq attributeSet) {
        return attributeSetMapper
                .toResponse(attributeService.createAttributeSet(attributeSetMapper.toEntity(attributeSet)));
    }

    @Override
    public List<AttributeTypesEnum> getAttributeTypes() {
        return Arrays.asList(AttributeTypesEnum.values());
    }

    @Override
    public List<AttributeDefinitionRes> getAttributeDefinitions() {
        return attributeService.getAttributeDefinitions().stream().map(attributeDefinitionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AttributeDefinitionRes createAttributeDefinition(AttributeDefinitionReq attributeDefinition) {
        return attributeDefinitionMapper.toResponse(
                attributeService.createAttributeDefinition(attributeDefinitionMapper.toEntity(attributeDefinition)));
    }

    private String readVersionFromManifest() {
        Package pkg = this.getClass().getPackage();
        if (pkg != null && pkg.getImplementationVersion() != null) {
            return pkg.getImplementationVersion();
        }
        return "unknown";
    }

    @Override
    public Map<String, Object> getAppInfo() {
        Map<String, Object> info = new HashMap<>();
        String hashCodePrefix = System.getenv("HASH_PREFIX");
        String logoImageReceiptPath = System.getenv("RECEIPT_LOGO_PATH");
        String reportParamsJsonPath = System.getenv("REPORT_PARAMS_RECEIPT_JSON");
        String generalInformationOfReceipt = "";
        String generalInformationFilePath = System.getenv("GENERAL_INFORMATION_RECEIPT_PATH");
        Map<String, Object> receipt_params_json = new HashMap<String, Object>();
        try {
            generalInformationOfReceipt = receiptGenerator.readGeneralInformationText();
            receipt_params_json = receiptGenerator.loadJsonParameters();
        } catch (Exception e) {
            e.printStackTrace();
            generalInformationOfReceipt = new String("Failed to read general Information file");
            receipt_params_json.put("Fail", "Failed to read report_params.json file");
        }
        info.put("logo_image_receipt_path", logoImageReceiptPath);
        info.put("general_information_file_path", generalInformationFilePath);
        info.put("general_information_of_receipt", generalInformationOfReceipt);
        info.put("receipt_params_json_path", reportParamsJsonPath);
        info.put("receipt_params_json_content", receipt_params_json);
        info.put("hash_code_property_prefix", hashCodePrefix);
        info.put("application_name", applicationName);
        info.put("context_path", contextPath);
        info.put("version", version);
        info.put("frontend_urls", frontendUrls.isEmpty() ? List.of() : List.of(frontendUrls.split(",")));

        info.put("default_attributes", getDefaultAttributes());

        return info;
    }

    public Map<String, Object> getDefaultAttributes() {
        Map<String, Object> defaultAttributes = new HashMap<>();

        defaultAttributes.put("fields.person", List.of("uuid", "identifier", "date_of_birth", "mothers_name"));
        defaultAttributes.put("fields.property", List.of("uuid", "property_id", "geometry", "propertyname",
                "state_district", "municipality", "zipcode", "locationzone"));
        defaultAttributes.put("fields.property_document", List.of("uuid", "property_id", "registered_property_name",
                "area", "document_type", "has_legal_reserve"));
        defaultAttributes.put("fields.sub_area", List.of("uuid", "geometry", "property_id", "area", "areatype"));
        defaultAttributes.put("fields.ship", List.of("uuid", "type", "property_id"));

        Map<String, List<String>> shipTypes = Map.of("type", List.of("OWNER", "REGISTRAR", "REPRESENTATIVE"));
        defaultAttributes.put("fields.ship.types", shipTypes);

        defaultAttributes.put("customAttributes.person.owner", List.of("landholderType"));

        Map<String, List<String>> landholderTypes = Map.of("landholderType", List.of("natural_person", "legal_entity"));
        defaultAttributes.put("customAttributes.person.owner.types", landholderTypes);

        defaultAttributes.put("customAttributes.property", List.of(
                "mailing_address_recipient_name", "mailing_address_street", "mailing_address_number",
                "mailing_address_neighborhood", "mailing_address_zip_code", "mailing_address_state",
                "mailing_address_city", "property_access_description", "mailing_address_additional_info",
                "mailing_address_email", "mailing_address_telephone"));

        defaultAttributes.put("customAttributes.property_document", List.of(
                "holdingType", "deed", "documentDate", "book", "page", "state_of_notary_office",
                "city_of_notary_office",
                // "national_rural_land_system_code",
                "property_certification",
                "national_rural_land_registration_number"));

        Map<String, List<String>> holdingTypes = Map.of("holdingType", List.of("property", "landholding"));
        defaultAttributes.put("customAttributes.property_document.types", holdingTypes);

        return defaultAttributes;
    }
}
