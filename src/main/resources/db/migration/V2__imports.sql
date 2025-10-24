INSERT INTO attribute_definition (attribute_definition_id,"type",name,allowed_values) VALUES
	 ('96c90dd1-e3d3-4cd8-b065-270999db86b7'::uuid,'ENUM','landholderType','{natural_person,legal_entity}'),
	 ('e052c89c-910a-46ab-ae85-229fe3994dac'::uuid,'ENUM','holdingType','{property,landholding}'),
	 ('28a400b0-8512-4c76-ad4a-76f65cbc2276'::uuid,'STRING','deed',NULL),
	 ('4a446f51-67f9-4f14-a60a-6443493ce7f5'::uuid,'DATE','documentDate',NULL),
	 ('af19af6c-9a89-4e68-8480-fd6c8aeb6858'::uuid,'STRING','book',NULL),
	 ('8fbc35c6-5c17-4291-ba35-4e3639ae9a62'::uuid,'STRING','page',NULL),
	 ('f9e0d98a-be38-4330-9ff8-50cd259bbcde'::uuid,'STRING','state_of_notary_office',NULL),
	 ('ada526b4-0783-484d-8bab-6e884d6de570'::uuid,'STRING','city_of_notary_office',NULL),
	 ('26c6185d-823e-43ef-9d73-d709fd43e36b'::uuid,'STRING','national_rural_land_system_code',NULL),
	 ('7924ebb8-b94a-479f-90fa-f821ea147b43'::uuid,'STRING','property_certification',NULL);
INSERT INTO attribute_definition (attribute_definition_id,"type",name,allowed_values) VALUES
	 ('f1a149d8-fdc8-4bf1-adda-16966eb16295'::uuid,'STRING','national_rural_land_registration_number',NULL),
	 ('4cdd0322-bef8-49a8-948f-59b96f79eedc'::uuid,'STRING','mailing_address_recipient_name',NULL),
	 ('e115a314-08c8-44d0-b084-82c1e6ad4eac'::uuid,'STRING','mailing_address_street',NULL),
	 ('fe6c7ab2-69cf-49e4-855a-3fa1e5b9bb53'::uuid,'STRING','mailing_address_number',NULL),
	 ('04f6276d-2e0d-4380-b19f-08e92d8a5da9'::uuid,'STRING','mailing_address_neighborhood',NULL),
	 ('b5c6d275-a815-4b38-ab6e-059da22669be'::uuid,'STRING','mailing_address_zip_code',NULL),
	 ('0d718ee7-3db0-4b42-8cd6-e90e29844c8d'::uuid,'STRING','mailing_address_state',NULL),
	 ('df10e2bd-65d7-4a2c-873f-3247807e22d2'::uuid,'STRING','mailing_address_city',NULL),
	 ('79a0a783-4f7a-4a03-993d-1e042df910d4'::uuid,'STRING','property_access_description',NULL),
	 ('93c82c49-4c8a-4077-9824-825a632bd044'::uuid,'STRING','mailing_address_additional_info',NULL);
INSERT INTO attribute_definition (attribute_definition_id,"type",name,allowed_values) VALUES
	 ('73bb0954-4026-415f-9901-7f10b109589e'::uuid,'STRING','mailing_address_email',NULL),
	 ('3a901bc8-594e-44ab-90e6-90383f55f147'::uuid,'STRING','mailing_address_telephone',NULL);
INSERT INTO attribute_set (attribute_set_id,entity_type) VALUES
	 ('35fe4c03-7f21-4592-9674-c9ec010b6ff2'::uuid,'OWNER'),
	 ('8aebbef0-c835-43e3-a3a8-a61ea5d2ec50'::uuid,'PROPERTY_DOCUMENT'),
	 ('f44b108a-5b67-42e2-b073-2d55274f7d29'::uuid,'PROPERTY');
INSERT INTO att_set_def (attribute_set_id,attribute_definition_id) VALUES
	 ('35fe4c03-7f21-4592-9674-c9ec010b6ff2'::uuid,'96c90dd1-e3d3-4cd8-b065-270999db86b7'::uuid),
	 ('8aebbef0-c835-43e3-a3a8-a61ea5d2ec50'::uuid,'e052c89c-910a-46ab-ae85-229fe3994dac'::uuid),
	 ('8aebbef0-c835-43e3-a3a8-a61ea5d2ec50'::uuid,'28a400b0-8512-4c76-ad4a-76f65cbc2276'::uuid),
	 ('8aebbef0-c835-43e3-a3a8-a61ea5d2ec50'::uuid,'4a446f51-67f9-4f14-a60a-6443493ce7f5'::uuid),
	 ('8aebbef0-c835-43e3-a3a8-a61ea5d2ec50'::uuid,'af19af6c-9a89-4e68-8480-fd6c8aeb6858'::uuid),
	 ('8aebbef0-c835-43e3-a3a8-a61ea5d2ec50'::uuid,'8fbc35c6-5c17-4291-ba35-4e3639ae9a62'::uuid),
	 ('8aebbef0-c835-43e3-a3a8-a61ea5d2ec50'::uuid,'f9e0d98a-be38-4330-9ff8-50cd259bbcde'::uuid),
	 ('8aebbef0-c835-43e3-a3a8-a61ea5d2ec50'::uuid,'ada526b4-0783-484d-8bab-6e884d6de570'::uuid),
	 ('8aebbef0-c835-43e3-a3a8-a61ea5d2ec50'::uuid,'26c6185d-823e-43ef-9d73-d709fd43e36b'::uuid),
	 ('8aebbef0-c835-43e3-a3a8-a61ea5d2ec50'::uuid,'7924ebb8-b94a-479f-90fa-f821ea147b43'::uuid);
INSERT INTO att_set_def (attribute_set_id,attribute_definition_id) VALUES
	 ('8aebbef0-c835-43e3-a3a8-a61ea5d2ec50'::uuid,'f1a149d8-fdc8-4bf1-adda-16966eb16295'::uuid),
	 ('f44b108a-5b67-42e2-b073-2d55274f7d29'::uuid,'4cdd0322-bef8-49a8-948f-59b96f79eedc'::uuid),
	 ('f44b108a-5b67-42e2-b073-2d55274f7d29'::uuid,'e115a314-08c8-44d0-b084-82c1e6ad4eac'::uuid),
	 ('f44b108a-5b67-42e2-b073-2d55274f7d29'::uuid,'fe6c7ab2-69cf-49e4-855a-3fa1e5b9bb53'::uuid),
	 ('f44b108a-5b67-42e2-b073-2d55274f7d29'::uuid,'04f6276d-2e0d-4380-b19f-08e92d8a5da9'::uuid),
	 ('f44b108a-5b67-42e2-b073-2d55274f7d29'::uuid,'b5c6d275-a815-4b38-ab6e-059da22669be'::uuid),
	 ('f44b108a-5b67-42e2-b073-2d55274f7d29'::uuid,'0d718ee7-3db0-4b42-8cd6-e90e29844c8d'::uuid),
	 ('f44b108a-5b67-42e2-b073-2d55274f7d29'::uuid,'df10e2bd-65d7-4a2c-873f-3247807e22d2'::uuid),
	 ('f44b108a-5b67-42e2-b073-2d55274f7d29'::uuid,'79a0a783-4f7a-4a03-993d-1e042df910d4'::uuid),
	 ('f44b108a-5b67-42e2-b073-2d55274f7d29'::uuid,'93c82c49-4c8a-4077-9824-825a632bd044'::uuid);
INSERT INTO att_set_def (attribute_set_id,attribute_definition_id) VALUES
	 ('f44b108a-5b67-42e2-b073-2d55274f7d29'::uuid,'73bb0954-4026-415f-9901-7f10b109589e'::uuid),
	 ('f44b108a-5b67-42e2-b073-2d55274f7d29'::uuid,'3a901bc8-594e-44ab-90e6-90383f55f147'::uuid);