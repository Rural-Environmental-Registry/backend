CREATE TABLE IF NOT EXISTS property_history (
  history_id        BIGSERIAL   PRIMARY KEY,
  property_id       UUID        NOT NULL,
  changed_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
  change_type       CHAR(1)     NOT NULL,  -- ‘I’, ‘U’ ou ‘D’ 
  geometry          geometry(Geometry,4326),
  property_name     TEXT, 
  state_district    TEXT, 
  municipality      TEXT, 
  zipcode           TEXT, 
  location_zone     TEXT, 
  code              TEXT, 
  map_image         bytea, 
  area              double precision, 
  latitude          double precision, 
  longitude         double precision
);

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_constraint WHERE conname = 'fk_ph_prop'
  ) THEN
    ALTER TABLE property_history
      ADD CONSTRAINT fk_ph_prop FOREIGN KEY(property_id) REFERENCES property(id);
  END IF;
END;
$$;

CREATE OR REPLACE FUNCTION fn_audit_property() RETURNS TRIGGER AS $$
DECLARE
  v_op   CHAR(1);
  v_row  property%ROWTYPE;
BEGIN

  IF TG_OP = 'INSERT' THEN
    v_op  := 'I';
    v_row := NEW;
  ELSIF TG_OP = 'UPDATE' THEN
    v_op  := 'U';
    v_row := OLD;
  ELSIF TG_OP = 'DELETE' THEN
    v_op  := 'D';
    v_row := OLD;
  END IF;

  INSERT INTO property_history ( property_id, changed_at, change_type, geometry, property_name, state_district, municipality, zipcode, location_zone, code, map_image, area, latitude, longitude) 
  VALUES ( v_row.id, now(), v_op, v_row.geometry, v_row.property_name, v_row.state_district, v_row.municipality, v_row.zipcode, v_row.location_zone, v_row.code, v_row.map_image, v_row.area, v_row.latitude, v_row.longitude);

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_property_audit ON property;

CREATE TRIGGER trg_property_audit
  AFTER INSERT OR UPDATE OR DELETE ON property
  FOR EACH ROW EXECUTE FUNCTION fn_audit_property();