insert into ImageCopyright(imageid, property, `value`)
select im.imageId imageId, 'creator' property, 'Ulli Gilles' value
from ImageMetadata im left join ImageCopyright ic on im.imageid = ic.imageid and ic.property = 'creator'
where (im.make = 'NIKON CORPORATION' and im.model in ('NIKON D60'))
and ic.id is null 