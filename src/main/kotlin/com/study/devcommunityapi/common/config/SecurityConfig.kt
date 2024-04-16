package com.study.devcommunityapi.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.servlet.handler.HandlerMappingIntrospector


@Configuration
@EnableMethodSecurity
@EnableWebSecurity
class SecurityConfig {

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity, introspector: HandlerMappingIntrospector): SecurityFilterChain? {

//        val mvcMatcherBuilder = MvcRequestMatcher.Builder(introspector)

        http.cors { it.configurationSource(corsConfigurationSource()) }
        http.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        http.csrf { it.disable() }

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