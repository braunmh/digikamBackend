select i.id AS id,
   i.name AS name,
   substr(ar.identifier, 16) root,
   a.relativePath AS relativePath, 
   i.status AS status,
   i.category AS category,
   ii.creationDate AS creationDate,
   i.fileSize AS fileSize,
   ii.rating AS rating,
   i.modificationDate AS modificationDate,
   ii.orientation AS orientation,
   ii.width AS width,
   ii.height AS height,
   vm.duration as duration, -- Dauer in Minuten * 1000 + Sekunden * x
   vm.videoCodec as codec,
   ic.value AS creator,
   gip.latitudeNumber AS latitudeNumber,
   gip.longitudeNumber AS longitudeNumber 
from ((((Images i join Albums a on((i.album = a.id))) join AlbumRoots ar on (ar.id = a.albumRoot)
   join (ImageInformation ii 
   left join VideoMetadata vm on((ii.imageid = vm.imageid)))) 
   left join ImageCopyright ic on(((ii.imageid = ic.imageid) and (ic.property = 'creator')))) 
   join (Images gi left join ImagePositions gip on((gi.id = gip.imageid)))) 
where ((ii.imageid = i.id) and (gi.id = i.id) and (vm.imageid is not null))
order by a.relativePath, i.name