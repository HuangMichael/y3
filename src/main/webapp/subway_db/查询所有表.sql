SELECT 
    t.table_name, t.table_comment
FROM
    information_schema.tables t
WHERE
    table_schema = 'bmis'