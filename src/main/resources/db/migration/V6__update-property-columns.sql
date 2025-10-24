ALTER TABLE property
ADD COLUMN IF NOT EXISTS area double precision;
ALTER TABLE property
ADD COLUMN IF NOT EXISTS latitude double precision;
ALTER TABLE property
ADD COLUMN IF NOT EXISTS longitude double precision;