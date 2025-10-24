ALTER TABLE public.property_document DROP COLUMN IF EXISTS has_legal_reserve;

DELETE FROM public.attribute_definition WHERE attribute_definition_id='26c6185d-823e-43ef-9d73-d709fd43e36b'::uuid; --national_rural_land_system_code