package ru.truebusiness.controller

import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.truebusiness.controller.data.request.NewPosterDto
import ru.truebusiness.mapper.PosterMapper
import ru.truebusiness.service.PosterService

@RestController
@RequestMapping("/api/v1/posters")
@RequiredArgsConstructor
class PosterController(
    private val posterService: PosterService,
    private val posterMapper: PosterMapper,
) {
    @PostMapping
    fun addPoster(newPoster: NewPosterDto) {
        val posterModel = posterMapper.dtoToModel(newPoster)
    }

}