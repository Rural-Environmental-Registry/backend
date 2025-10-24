CREATE TABLE IF NOT EXISTS person_history (
  history_id        BIGSERIAL     PRIMARY KEY,
  person_id         UUID          NOT NULL,
  changed_at        TIMESTAMPTZ   NOT NULL DEFAULT now(),
  change_type       CHAR(1)       NOT NULL,  -- ‘I’, ‘U’ ou ‘D’ 
  identifier        varchar(255)  NOT NULL,
  name              varchar(255),
  date_of_birth     date,
  mothers_name      varchar(255)
);

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_constraint WHERE conname = 'fk_ph_person'
  ) THEN
    ALTER TABLE person_history
      ADD CONSTRAINT fk_ph_person FOREIGN KEY(person_id) REFERENCES person(id);
  END IF;
END;
$$;

CREATE OR REPLACE FUNCTION fn_audit_person() RETURNS TRIGGER AS $$
DECLARE
  v_op   CHAR(1);
  v_row  person%ROWTYPE;
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

  INSERT INTO person_history ( person_id, changed_at, change_type, identifier, name, date_of_birth, mothers_name) 
  VALUES ( v_row.id, now(), v_op, v_row.identifier, v_row.name, v_row.date_of_birth, v_row.mothers_name);

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_person_audit ON person;

CREATE TRIGGER trg_person_audit
  AFTER INSERT OR UPDATE OR DELETE ON person
  FOR EACH ROW EXECUTE FUNCTION fn_audit_person();