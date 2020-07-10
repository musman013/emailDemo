CREATE TABLE s1.master_entity
(
    id bigint NOT NULL,
    master_name character varying(255) COLLATE pg_catalog."default",
    master_value character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT master_entity_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE s1.master_entity
    OWNER to postgres;


INSERT INTO s1.master_entity(
	id, master_name, master_value)
	VALUES (1, 'Date', 'MMM d, y, h:mm:ss a');


INSERT INTO s1.master_entity(
	id, master_name, master_value)
	VALUES (2, 'Date', 'M/d/yy, h:mm a');


INSERT INTO s1.master_entity(
	id, master_name, master_value)
	VALUES (3, 'Date', 'dd-MMM-yy');


INSERT INTO s1.master_entity(
	id, master_name, master_value)
	VALUES (4, 'Date', 'DD/MM/YY');

INSERT INTO s1.master_entity(
	id, master_name, master_value)
	VALUES (5, 'Date', 'YY/MM/DD');

INSERT INTO s1.master_entity(
	id, master_name, master_value)
	VALUES (6, 'Date', 'YYYY-MM-DD');


INSERT INTO s1.master_entity(
	id, master_name, master_value)
	VALUES (7, 'Currency', '$');


INSERT INTO s1.master_entity(
	id, master_name, master_value)
	VALUES (8, 'Currency', '€');


INSERT INTO s1.master_entity(
	id, master_name, master_value)
	VALUES (9, 'Currency', '£');

INSERT INTO s1.master_entity(
	id, master_name, master_value)
	VALUES (10, 'Currency', '¥');

INSERT INTO s1.master_entity(
	id, master_name, master_value)
	VALUES (11, 'Currency', '₹');






CREATE TABLE s1.email_variable_type
(
    id bigint NOT NULL,
    variable_type character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT email_variable_type_pkey PRIMARY KEY (id),
    CONSTRAINT uk_n8a2j2vo63h6o9rl60fxnq16b UNIQUE (variable_type)

)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE s1.email_variable_type
    OWNER to postgres;


INSERT INTO s1.email_variable_type(
	id, variable_type)
	VALUES (1, 'Text');

INSERT INTO s1.email_variable_type(
	id, variable_type)
	VALUES (2, 'Multi-line Text');

INSERT INTO s1.email_variable_type(
	id, variable_type)
	VALUES (3, 'Date');

INSERT INTO s1.email_variable_type(
	id, variable_type)
	VALUES (4, 'Number');

INSERT INTO s1.email_variable_type(
	id, variable_type)
	VALUES (5, 'Email');

INSERT INTO s1.email_variable_type(
	id, variable_type)
	VALUES (6, 'Phone');

INSERT INTO s1.email_variable_type(
	id, variable_type)
	VALUES (7, 'Percentage');

INSERT INTO s1.email_variable_type(
	id, variable_type)
	VALUES (8, 'Image');

INSERT INTO s1.email_variable_type(
	id, variable_type)
	VALUES (9, 'Hyperlink');

INSERT INTO s1.email_variable_type(
	id, variable_type)
	VALUES (10, 'Clickable Image');

INSERT INTO s1.email_variable_type(
	id, variable_type)
	VALUES (11, 'Currency');

INSERT INTO s1.email_variable_type(
	id, variable_type)
	VALUES (12, 'List');

INSERT INTO s1.email_variable_type(
	id, variable_type)
	VALUES (13, 'List of Images');

