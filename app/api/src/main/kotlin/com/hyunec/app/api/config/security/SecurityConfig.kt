package com.hyunec.app.api.config.security

import com.hyunec.app.api.domain.entity.User
import com.hyunec.common.util.KLogging
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider
) {
    companion object : KLogging() {
        private val ROLE_USER = User.Role.ROLE_USER.name.removePrefix("ROLE_")
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests { request ->
            request
                // options 메서드는 인증 제외
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // 인증, 인가
                .requestMatchers("/api/v1/auth/**").permitAll()

                // chat completion
                .requestMatchers("/api/v1/chat/completion/**").hasAnyRole(ROLE_USER)

                .anyRequest().permitAll()
        }
            .csrf { it.disable() }
            .cors { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(STATELESS) }
            .addFilterBefore(
                JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .exceptionHandling { it.authenticationEntryPoint(CustomAuthenticationEntryPoint()) }
            .exceptionHandling { it.accessDeniedHandler(CustomAccessDeniedHandler()) }

        return http.build()
    }

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedMethods("*")
            }
        }
    }

    class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {
        override fun commence(
            request: HttpServletRequest,
            response: HttpServletResponse,
            authenticationException: AuthenticationException
        ) {
            response.apply {
                contentType = MediaType.APPLICATION_JSON_VALUE
                status = HttpServletResponse.SC_UNAUTHORIZED
                outputStream.println(("{ \"error\": \"" + authenticationException.message) + "\" }")
            }

            log.error(
                "${authenticationException::class.simpleName}: ${authenticationException.message}}\n"
                        + "request.method: ${request.method}\n"
                        + "request.getHeader(\"Authorization\"): ${request.getHeader("Authorization")}\n"
            )
        }

        companion object : KLogging()
    }

    class CustomAccessDeniedHandler : AccessDeniedHandler {
        override fun handle(
            request: HttpServletRequest,
            response: HttpServletResponse,
            accessDeniedException: AccessDeniedException
        ) {
            response.apply {
                contentType = MediaType.APPLICATION_JSON_VALUE
                status = HttpServletResponse.SC_FORBIDDEN
                outputStream.println(("{ \"error\": \"" + accessDeniedException.message) + "\" }")
            }

            log.error(
                "${accessDeniedException::class.simpleName}: ${accessDeniedException.message}}\n"
                        + "request.method: ${request.method}\n"
                        + "request.getHeader(\"Authorization\"): ${request.getHeader("Authorization")}\n"
            )
        }

        companion object : KLogging()
    }
}
