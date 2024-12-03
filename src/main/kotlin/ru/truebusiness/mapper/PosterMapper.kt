package ru.truebusiness.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import ru.truebusiness.controller.data.request.NewPosterDto
import ru.truebusiness.service.model.PosterModel

@Mapper
interface PosterMapper {

    @Mapping(target = "posterName", source = "posterName")
    @Mapping(target = "dateFrom", source = "dateFrom")
    @Mapping(target = "dateTo", source = "dateTo")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "content", source = "content")
    fun dtoToModel(dto: NewPosterDto): PosterModel

}