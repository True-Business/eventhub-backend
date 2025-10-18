package ru.truebusiness.eventhub_backend.exceptions

import org.springframework.security.access.AccessDeniedException

class WrongOrganizerException(message: String, cause: Throwable? = null) : AccessDeniedException(message, cause)