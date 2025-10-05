package ru.truebusiness.eventhub_backend.security

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*

@Component
class AuthenticationContextRequestFilter(
    private val userDetailsService: UserDetailsService,
) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader != null && authHeader.startsWith("Basic ")) {
            val base64Credentials = authHeader.substring("Basic ".length)
            val credentials = String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8)

            val parts = credentials.split(":", limit = 2)
            if (parts.size == 2) {
                val email = parts[0]

                setAuth(email)
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun setAuth(email: String) {
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(email)

        val auth = UsernamePasswordAuthenticationToken(
            UUID.fromString(userDetails.username),
            null,
            userDetails.authorities
        )

        SecurityContextHolder.getContext().authentication = auth
    }
}
