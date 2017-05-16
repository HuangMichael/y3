
   SELECT * FROM dev.t_basedata;


set sql_safe_updates = 0;

UPDATE t_basedata t 
SET 
    t.line = CONCAT('北京地铁', t.line),
    t.station = CONCAT(t.station, '站'),
    t.loccode = CONCAT('BJ', t.loccode); 
    
    UPDATE t_basedata t
        JOIN
    t_locations l ON t.loccode = l.location 
SET 
    t.station = l.description