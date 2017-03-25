
<<<<<<< HEAD
=======

>>>>>>> cfede88eada1f11b4dbb0fdb6ee27512d6f2ec99
set sql_safe_updates = 0;
UPDATE t_locations l
        JOIN
    v_line ln ON LEFT(l.location, 4) = ln.line_no
        JOIN
    v_station s ON LEFT(l.location, 6) = s.station_no 
SET 
    l.line_id = ln.id,
    l.station_id = s.id
