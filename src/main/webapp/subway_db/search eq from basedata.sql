SELECT
    id,
    b.eqdesc,
    b.eq_class_id,
    b.location_id,
    b.eqcode,
    b.maintainer,
    b.manager,
    b.loccode AS location,
    '0' AS product_factory,
    DATE_FORMAT(NOW(), '%Y-%m-%d') AS purchase_date,
    0 AS net_value,
    0 AS original_value,
    0 AS sort_no,
    1 AS status,
    b.eqmodel eq_model,
    0 AS expected_year,
    1 AS manage_level,
    0 AS purchase_price,
    DATE_FORMAT(NOW(), '%Y-%m-%d') AS run_date,
    DATE_FORMAT(NOW(), '%Y-%m-%d') AS setup_date,
    0 AS warranty_period,
    1 AS running,
    DATE_FORMAT(NOW(), '%Y-%m-%d') AS product_date,
    '0' AS asset_no,
    b.location_id AS vlocations_id,
    '0' AS eam_no,
    '0' AS img_url
FROM
    t_basedata b;


set sql_safe_updates = 0;



