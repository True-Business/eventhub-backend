package ru.truebusiness.eventhub_backend.conrollers.organizations

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import java.util.UUID
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.truebusiness.eventhub_backend.conrollers.dto.organizations.CreateOrganizationRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.ErrorResponseDto
import ru.truebusiness.eventhub_backend.conrollers.dto.UpdateOrganizationRequestDto
import ru.truebusiness.eventhub_backend.conrollers.dto.organizations.OrganizationDto

@RestController
@RequestMapping("/api/v1/organization")
interface OrganizationController {
    @Operation(
        summary = "Создание новой организации", description = """
            Создает новую организацию. 
            <p><b>ВАЖНО:</b> Параметр creatorId является временным решением. 
            В будущих версиях будет заменен на автоматическое определение через аутентификационные токены.
        """, responses = [ApiResponse(
            responseCode = "201",
            description = "Организация успешно создана",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = OrganizationDto::class),
                examples = [ExampleObject(
                    name = "Пример успешного создания",
                    value = """{
                                "id": "550e8400-e29b-41d4-a716-446655440000",
                                "name": "Acme Corporation",
                                "description": "Global technology company",
                                "address": "123 Main St, New York",
                                "pictureUrl": "https://example.com/logo.png",
                                "creatorId": "a3d8e5f0-1b2c-4d3e-5f6a-7b8c9d0e1f2a"
                            }""",
                )]
            )]
        ), ApiResponse(
            responseCode = "409",
            description = "Организация с таким именем уже существует",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorResponseDto::class),
                examples = [ExampleObject(
                    name = "Конфликт имен",
                    value = """{
                                "code": 409,
                                "message": "Organization with name 'Acme Corporation' already exists"
                            }""",
                )]
            )]
        )]
    )
    @PostMapping
    fun create(
        @RequestBody(
            description = "Данные для создания организации",
            required = true,
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = CreateOrganizationRequestDto::class),
                examples = [ExampleObject(
                    name = "Корректный запрос",
                    value = """{
                            "name": "Acme Corporation",
                            "description": "Global technology company",
                            "address": "123 Main St, New York",
                            "pictureUrl": "https://example.com/logo.png",
                            "creatorId": "a3d8e5f0-1b2c-4d3e-5f6a-7b8c9d0e1f2a"
                        }""",
                ), ExampleObject(
                    name = "Минимальный запрос",
                    value = """{
                            "name": "Test Org",
                            "description": "Short description",
                            "creatorId": "a3d8e5f0-1b2c-4d3e-5f6a-7b8c9d0e1f2a"
                        }""",
                    summary = "Без необязательных полей address и pictureUrl",
                )],
            )],
        )
        @org.springframework.web.bind.annotation.RequestBody
        createOrganizationRequestDto: CreateOrganizationRequestDto
    ): ResponseEntity<OrganizationDto>

    @GetMapping("/{organizationID}")
    fun get(@PathVariable organizationID: UUID): ResponseEntity<OrganizationDto>

    @PatchMapping("/{organizationID}")
    fun update(
        @PathVariable organizationID: UUID,
        @RequestBody updateOrganizationRequestDto: UpdateOrganizationRequestDto
    ): ResponseEntity<OrganizationDto>

    @DeleteMapping("/{organizationID}")
    fun delete(@PathVariable organizationID: UUID): ResponseEntity<Void>
}