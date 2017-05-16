    



    UPDATE t_basedata b
        JOIN
    v_locations v ON v.location = b.loccode
SET
    b.location_id = v.id;

    
    
   #批量更新设备分类 
    
    UPDATE t_basedata b
        JOIN
    t_equipments_classification c ON c.description = b.eqname 
SET 
    b.eq_class_id = c.id
WHERE
    c.class_type = 2;


set sql_safe_updates = 0;



    UPDATE t_basedata t
        JOIN
    t_locations l ON LEFT(t.loccode, 6) = LEFT(l.location, 6)
SET
    t.station = l.description