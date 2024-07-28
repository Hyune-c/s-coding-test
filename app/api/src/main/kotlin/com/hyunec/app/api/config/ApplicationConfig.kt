package com.hyunec.app.api.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(
    basePackages = [
        "com.hyunec.app.api",
        "com.hyunec.domain.core",
    ]
)
class ApplicationConfig
