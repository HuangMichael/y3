SELECT 
    c.TABLE_NAME,
    c.ordinal_position,
    c.column_name,
    c.column_comment,
    c.column_type
FROM
    information_schema.columns c
WHERE
    c.table_schema = 'bmis'
        AND c.table_name = 't_app_layer'