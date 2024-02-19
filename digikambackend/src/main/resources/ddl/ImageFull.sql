select i.id AS id,
    i.name AS name,
    a.relativePath AS relativePath,
    i.status AS status,
    i.category AS category,
    i.modificationDate AS modificationDate,
    i.fileSize AS fileSize,
    ii.rating AS rating,
    ii.creationDate AS creationDate,
    ii.orientation AS orientation,
    ii.width AS width,
    ii.height AS height,
    im.make AS make,
    im.model AS model,
    im.lens AS lens,
    im.aperture AS aperture,
    im.focalLength AS focalLength,
    im.focalLength35 AS focalLength35,
    im.exposureTime AS exposureTime,
    im.exposureProgram AS exposureProgram,
    im.exposureMode AS exposureMode,
    im.sensitivity AS sensitivity,
    ic.value AS creator,
    gip.latitudeNumber AS latitudeNumber,
    gip.longitudeNumber AS longitudeNumber 
from ((((Images i join Albums a on((i.album = a.id))) 
    join (ImageInformation ii 
    left join ImageMetadata im on((ii.imageid = im.imageid)))) 
    left join ImageCopyright ic on(((ii.imageid = ic.imageid) and (ic.property = 'creator')))) 
    join (Images gi left join ImagePositions gip on((gi.id = gip.imageid)))) 
where ((ii.imageid = i.id) and (gi.id = i.id) and (ii.imageid is not null))