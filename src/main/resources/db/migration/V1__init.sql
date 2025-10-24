-- V1__init.sql
-- Flyway migration for the initial schema

-- Enable PostGIS and UUID extensions (if using PostgreSQL)
CREATE
EXTENSION IF NOT EXISTS postgis;
CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";

---------------------------------------------
-- Table: person
---------------------------------------------
CREATE TABLE person
(
    id              uuid         NOT NULL,
    identifier      varchar(255) NOT NULL,
    name            varchar(255),
    date_of_birth   date,
    mothers_name    varchar(255),     
    CONSTRAINT pk_person PRIMARY KEY (id),
    CONSTRAINT uq_person_identifier UNIQUE (identifier)
);

---------------------------------------------
-- Table: property
---------------------------------------------
CREATE TABLE property
(
    id              uuid NOT NULL,
    geometry        geometry(Geometry,4326), -- PostGIS geometry column
    property_name   varchar(255),
    state_district  varchar(255),
    municipality    varchar(255),
    zipcode         varchar(255),
    location_zone   varchar(255),
    CONSTRAINT pk_property PRIMARY KEY (id)
);

---------------------------------------------
-- Table: property_document
---------------------------------------------
CREATE TABLE property_document
(
    id                          uuid NOT NULL,
    property_id                 uuid NOT NULL,
    registered_property_name    varchar(255),
    area                        double precision,
    document_type               varchar(255),
    has_legal_reserve           boolean,
    CONSTRAINT pk_property_document PRIMARY KEY (id),
    CONSTRAINT fk_property_document_property FOREIGN KEY (property_id) REFERENCES property (id) ON DELETE CASCADE
);

---------------------------------------------
-- Table: sub_area
---------------------------------------------
CREATE TABLE sub_area
(
    id           uuid NOT NULL,
    geometry     geometry(Geometry,4326), -- PostGIS geometry column
    property_id  uuid NOT NULL,
    area         double precision,
    area_type    varchar(255),
    CONSTRAINT pk_sub_area PRIMARY KEY (id),
    CONSTRAINT fk_sub_area_property FOREIGN KEY (property_id) REFERENCES property (id) ON DELETE CASCADE
);

---------------------------------------------
-- Table: ship -> ownership, registraship and representativeship merged
---------------------------------------------
CREATE TABLE ship
(
    ship_id uuid NOT NULL,
    type         varchar(50) NOT NULL, -- Enum stored as String (e.g. OWNERSHIP, REGISTRARSHIP, REPRESENTATIVESHIP)
    id           uuid NOT NULL, -- References Person.id
    property_id  uuid NOT NULL,
    from_date    date NOT NULL,
    to_date      date,
    CONSTRAINT pk_ship PRIMARY KEY (ship_id),
    CONSTRAINT fk_ship_person FOREIGN KEY (id) REFERENCES person (id) ON DELETE CASCADE,
    CONSTRAINT fk_ship_property FOREIGN KEY (property_id) REFERENCES property (id) ON DELETE CASCADE
);

---------------------------------------------
-- Table: attribute_definition
---------------------------------------------
CREATE TABLE attribute_definition
(
    attribute_definition_id uuid        NOT NULL,
    type                    varchar(50) NOT NULL, -- Enum stored as String (e.g. STRING, INTEGER, DATE, BOOLEAN)
    name                    varchar(255),
    allowed_values          text[], -- Array of allowed values for ENUM types
    CONSTRAINT pk_attribute_definition PRIMARY KEY (attribute_definition_id)
);

---------------------------------------------
-- Table: attribute_set
---------------------------------------------
CREATE TABLE attribute_set
(
    attribute_set_id uuid        NOT NULL,
    entity_type      varchar(50) NOT NULL, -- Enum stored as String (e.g. PERSON, OWNER, PROPERTY, etc.)
    CONSTRAINT pk_attribute_set PRIMARY KEY (attribute_set_id)
);

---------------------------------------------
-- Join Table: att_set_def -> attribute_set_required_attributes and attribute_set_optional_attributes merged 
---------------------------------------------
CREATE TABLE att_set_def
(
    att_set_def_id        BIGSERIAL NOT NULL,
    attribute_set_id        uuid NOT NULL,
    attribute_definition_id uuid NOT NULL,
    CONSTRAINT pk_att_set_def PRIMARY KEY (att_set_def_id),
    CONSTRAINT fk_attribute_set FOREIGN KEY (attribute_set_id) REFERENCES attribute_set (attribute_set_id) ON DELETE CASCADE,
    CONSTRAINT fk_attribute_definition FOREIGN KEY (attribute_definition_id) REFERENCES attribute_definition (attribute_definition_id) ON DELETE CASCADE
);

---------------------------------------------
-- Table: attributes -> person_attributes, property_attributes, property_document_attributes, subarea_attributes merged
---------------------------------------------
CREATE TABLE attributes
(
    attribute_id  uuid         NOT NULL,
    origin_check varchar(255) NOT NULL, -- Enum stored as String (e.g. PERSON, PROPERTY, PROPERTY_DOCUMENT, SUB_AREA)
    name          varchar(255) NOT NULL,
    value_string  varchar(255),
    value_integer integer,
    value_boolean boolean,
    value_date    date,
    value_json    jsonb,
    person_id              uuid, -- References Person.id
    property_id            uuid, -- References Property.id
    property_document_id   uuid, -- References Property_Document.id
    sub_area_id            uuid, -- References Sub_area.id
    CONSTRAINT pk_attributes PRIMARY KEY (attribute_id),
    CONSTRAINT fk_attributes_person FOREIGN KEY (person_id) REFERENCES person (id) ON DELETE CASCADE,
    CONSTRAINT fk_attributes_property FOREIGN KEY (property_id) REFERENCES property (id) ON DELETE CASCADE,
    CONSTRAINT fk_attributes_property_document FOREIGN KEY (property_document_id) REFERENCES property_document (id) ON DELETE CASCADE,
    CONSTRAINT fk_attributes_sub_area FOREIGN KEY (sub_area_id) REFERENCES sub_area (id) ON DELETE CASCADE
);
