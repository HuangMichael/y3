 UPDATE t_locations l 
SET 
    l.location_type = 1
WHERE
    l.location LIKE '%BJ02%'
        OR l.location LIKE '%BJ08%'
        OR l.location LIKE '%BJ10%'
        OR l.location LIKE '%BJ13%'