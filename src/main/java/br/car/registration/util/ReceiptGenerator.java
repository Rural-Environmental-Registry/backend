package br.car.registration.util;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.time.ZoneId;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.repository.init.ResourceReader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.car.registration.api.v1.response.PropertyRes;
import br.car.registration.domain.Property;
import br.car.registration.domain.PropertyDocument;
import br.car.registration.mappers.PropertyMapper;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Component
public class ReceiptGenerator {

  private final PropertyMapper propertyMapper;

  public ReceiptGenerator(PropertyMapper propertyMapper) {
    this.propertyMapper = propertyMapper;
  }

  public byte[] createPdf(Property property, String locationZone) {
    String WATERMARK_IMAGE_PATH = System.getenv("WATERMARK_IMAGE_PATH");
    String defaultLocationZone = System.getenv("DEFAULT_LOCATION_ZONE");
    String zoneToUse = (locationZone == null && locationZone.isBlank()) ? defaultLocationZone : locationZone;
    if (!isValidTimeZone(zoneToUse)) {
      zoneToUse = defaultLocationZone; // fallback para o default
    }
    ZoneId zoneId = ZoneId.of(zoneToUse);
    String issueDate = getStringDatetimeFormatted(Instant.now(), zoneId);
    String createdAtForJasper = getStringDatetimeFormatted(property.getCreatedAt(), zoneId);

    Property cleanedProperty = cleanNullPropertyAttributes(property);

    PropertyRes prop = propertyMapper.toResponse(cleanedProperty);
    List<Object> properties = List.of(prop);

    byte[] image = (property.getMapImage() != null) ? image = property.getMapImage() : null;
    String imageEnvPath = System.getenv("RECEIPT_LOGO_PATH");
    String imagePath = "";
    imagePath = (imageEnvPath == null) ? "classpath:images/govbr.svg" : "classpath:" + imageEnvPath;

    try (InputStream jasperStream = getClass().getResourceAsStream("/reports/receipt_template.jasper")) {
      String textContent = this.readGeneralInformationText();
      JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
      JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(properties);
      Map<String, Object> jsonParams = loadJsonParameters();
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("createdBy", "Dataprev");
      parameters.put("LOGO_PATH", imagePath);
      parameters.put("WATERMARK_IMAGE_PATH", WATERMARK_IMAGE_PATH);
      parameters.put("ISSUE_DATE", issueDate);
      parameters.put("CREATED_AT", createdAtForJasper);
      parameters.put("GENERAL_INFORMATION", textContent);
      parameters.put("imagemParam", new ByteArrayInputStream(image));
      parameters.put("property_area", prop.getMainArea().getArea());
      parameters.put("property_latitude", prop.getMainArea().getLat());
      parameters.put("property_longitude", prop.getMainArea().getLon());
      parameters.putAll(jsonParams);
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
          parameters, dataSource);
      return JasperExportManager.exportReportToPdf(jasperPrint);
    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
      return null;
    }

  }

  public String readGeneralInformationText() throws IOException {
    ClassLoader classLoader = ResourceReader.class.getClassLoader();
    String generalInformationFilePath = System.getenv("GENERAL_INFORMATION_RECEIPT_PATH");
    if (generalInformationFilePath == null || generalInformationFilePath.isBlank()) {
      throw new IllegalArgumentException(
          "Variável de ambiente 'GENERAL_INFORMATION_RECEIPT_PATH' não definida!");
    }
    try (InputStream inputStream = classLoader.getResourceAsStream(generalInformationFilePath)) {
      if (inputStream == null) {
        throw new FileNotFoundException("Arquivo não encontrado: " + generalInformationFilePath);
      }
      return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }
  }

  public Map<String, Object> loadJsonParameters() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    String reportParamsJsonPath = System.getenv("REPORT_PARAMS_RECEIPT_JSON");
    InputStream jsonStream = new ClassPathResource(reportParamsJsonPath).getInputStream();
    return mapper.readValue(jsonStream, new TypeReference<Map<String, Object>>() {
    });
  }

  private boolean isValidTimeZone(String zoneId) {
    try {
      ZoneId.of(zoneId);
      return true;
    } catch (DateTimeException e) {
      return false;
    }
  }

  private String getStringDatetimeFormatted(Instant instant, ZoneId zoneId) {
    return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        .withZone(zoneId)
        .format(instant);
  }

  private Property cleanNullPropertyAttributes(Property property) {
    Property cleanedProperty = new Property();

    cleanedProperty.setId(property.getId());
    cleanedProperty.setGeometry(property.getGeometry());
    cleanedProperty.setArea(property.getArea());
    cleanedProperty.setLatitude(property.getLatitude());
    cleanedProperty.setLongitude(property.getLongitude());
    cleanedProperty.setPropertyName(property.getPropertyName());
    cleanedProperty.setStateDistrict(property.getStateDistrict());
    cleanedProperty.setMunicipality(property.getMunicipality());
    cleanedProperty.setZipcode(property.getZipcode());
    cleanedProperty.setCode(property.getCode());
    cleanedProperty.setCreatedAt(property.getCreatedAt());
    cleanedProperty.setUpdatedAt(property.getUpdatedAt());
    cleanedProperty.setMapImage(property.getMapImage());
    cleanedProperty.setLocationZone(property.getLocationZone());
    cleanedProperty.setSubAreas(property.getSubAreas());
    cleanedProperty.setOwnerships(property.getOwnerships());
    cleanedProperty.setRegistrarships(property.getRegistrarships());
    cleanedProperty.setRepresentativeships(property.getRepresentativeships());

    cleanedProperty.setAttributes(
        property.getAttributes().stream()
            .filter(attr -> attr.getValue() != null)
            .collect(java.util.stream.Collectors.toList()));

    cleanedProperty.setDocuments(
        property.getDocuments().stream()
            .map(doc -> {
              PropertyDocument cleanedDoc = new PropertyDocument();
              cleanedDoc.setId(doc.getId());
              cleanedDoc.setProperty(doc.getProperty());
              cleanedDoc.setRegisteredPropertyName(doc.getRegisteredPropertyName());
              cleanedDoc.setArea(doc.getArea());
              cleanedDoc.setDocumentType(doc.getDocumentType());
              cleanedDoc.setAttributes(
                  doc.getAttributes().stream()
                      .filter(attr -> attr.getValue() != null)
                      .collect(java.util.stream.Collectors.toList()));
              return cleanedDoc;
            })
            .collect(java.util.stream.Collectors.toList()));

    return cleanedProperty;
  }
}
