-- form_sample: sample domain with logical delete (use_yn), payload_json for full form data
CREATE TABLE IF NOT EXISTS form_sample (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(200) NOT NULL,
    content      VARCHAR(4000) NOT NULL,
    payload_json CLOB,
    use_yn       CHAR(1) NOT NULL DEFAULT 'Y',
    created_at   TIMESTAMP NOT NULL,
    updated_at   TIMESTAMP NOT NULL
);
