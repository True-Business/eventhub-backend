package ru.truebusiness.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.truebusiness.repository.entity.Poster

interface PosterRepository: JpaRepository<Poster, Long> {
}