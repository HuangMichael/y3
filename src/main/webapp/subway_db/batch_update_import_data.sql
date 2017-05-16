    



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
    c.class_type = 2