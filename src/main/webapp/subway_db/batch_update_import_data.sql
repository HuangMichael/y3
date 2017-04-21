    
#批量更新设备位置
    
UPDATE t_basedata b
        JOIN
    v_locations v ON v.loc_Name = CONCAT(b.line, b.station, b.locdesc) 
SET 
    b.location_id = v.id,
    b.loccode = v.location;
    
    
   #批量更新设备分类 
    
    UPDATE t_basedata b
        JOIN
    t_equipments_classification c ON c.description = b.eqname 
SET 
    b.eq_class_id = c.id
WHERE
    c.class_type = 2