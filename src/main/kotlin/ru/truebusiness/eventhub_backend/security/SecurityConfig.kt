package ru.truebusiness.eventhub_backend.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import ru.truebusiness.eventhub_backend.exceptions.UserNotFoundException
import ru.truebusiness.eventhub_backend.repository.UserCredentialsRepository

@Configuration
class SecurityConfig(
    private val userCredentialsRepository: UserCredentialsRepository
) {
    @Bean
    fun userDetailsService(): UserDetailsService = UserDetailsService { email ->
        val credentials = userCredentialsRepository.findByEmail(email)
            ?: throw UserNotFoundException("User with email $email not found!", null)

        User.builder()
            .username(credentials.email)
            .password(credentials.password)
            .roles("USER")
            .build()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { authRequest ->
                authRequest
                    .requestMatchers("/api/v1/auth/**").permitAll()
                    .requestMatchers("/actuator/**").permitAll()
                    .anyRequest().authenticated()
            }
            .httpBasic { }
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}