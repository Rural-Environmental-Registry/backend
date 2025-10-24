package br.car.registration.api.v1;

import br.car.registration.api.v1.request.AttributeDefinitionReq;
import br.car.registration.api.v1.request.AttributeSetReq;
import br.car.registration.api.v1.response.AttributeDefinitionRes;
import br.car.registration.api.v1.response.AttributeSetRes;
import br.car.registration.enums.AttributeTypesEnum;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/v1/admin")
public interface AdminApi {

    @GetMapping(path = "/attributesets", produces = MediaType.APPLICATION_JSON_VALUE)
    List<AttributeSetRes> getAttributeSets();

    @PostMapping(path = "/attributesets", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    AttributeSetRes createAttributeSet(@RequestBody AttributeSetReq attributeSet);

    @GetMapping(path = "/attributetypes", produces = MediaType.APPLICATION_JSON_VALUE)
    List<AttributeTypesEnum> getAttributeTypes();

    @GetMapping(path = "/attributedefinitions", produces = MediaType.APPLICATION_JSON_VALUE)
    List<AttributeDefinitionRes> getAttributeDefinitions();

    @PostMapping(path = "/attributedefinitions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    AttributeDefinitionRes createAttributeDefinition(@RequestBody AttributeDefinitionReq attributeDefinition);

    @GetMapping(path = "/app-info", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> getAppInfo();
}
