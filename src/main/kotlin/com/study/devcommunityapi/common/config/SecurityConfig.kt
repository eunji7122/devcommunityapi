package com.study.devcommunityapi.common.config

import com.study.devcommunityapi.common.security.filter.JwtAuthenticationFilter
import com.study.devcommunityapi.common.security.provider.JwtTokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.servlet.handler.HandlerMappingIntrospector


@Configuration
@EnableMethodSecurity
@EnableWebSecurity
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity, introspector: HandlerMappingIntrospector): SecurityFilterChain? {

        http
            .httpBasic{ it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/api/members/").permitAll()
                it.requestMatchers("/api/auth/**").permitAll()
                it.requestMatchers(HttpMethod.GET, "/api/boards/").permitAll()
                it.requestMatchers("/api/**").hasAnyRole("USER")
                it.requestMatchers("/api/").authenticated()
                it.anyRequest().permitAll()
            }
            .addFilterBefore(JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource? {

        val corsConfiguration = CorsConfiguration()
        corsConfiguration.setAllowedOriginPatterns(mutableListOf("*"))
        corsConfiguration.allowedMethods = mutableListOf("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
        corsConfiguration.allowedHeaders = mutableListOf("Authorization", "Cache-Control", "Content-Type")
        corsConfiguration.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfiguration)

        return source
    }

}