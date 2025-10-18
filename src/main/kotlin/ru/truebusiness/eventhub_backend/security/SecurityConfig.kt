package ru.truebusiness.eventhub_backend.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import ru.truebusiness.eventhub_backend.exceptions.users.UserNotFoundException
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import ru.truebusiness.eventhub_backend.repository.UserCredentialsRepository

@Configuration
class SecurityConfig(
    private val userCredentialsRepository: UserCredentialsRepository
) {
    @Bean
    fun userDetailsService(): UserDetailsService = UserDetailsService { email ->
        val credentials = userCredentialsRepository.findByEmail(email) ?: run {
            throw UserNotFoundException.withEmail(email)
        }

        User.builder()
            .username(credentials.id.toString())
            .password(credentials.password)
            .roles("USER")
            .build()
    }

    @Bean
    fun filterChain(http: HttpSecurity, authContextFilter: AuthenticationContextRequestFilter): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { authRequest ->
                authRequest
                    .requestMatchers("/api/v1/auth/**").permitAll()
                    .requestMatchers("/actuator/**").permitAll()
                    .requestMatchers(
                        "/api/event-hub",
                        "/api/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                    ).permitAll()
                    .anyRequest().authenticated()
            }
            .httpBasic { }
            .addFilterAfter(authContextFilter, BasicAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}