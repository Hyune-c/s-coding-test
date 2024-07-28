package com.hyunec.app.api.domain.entity

import java.time.Instant

data class User(
    val email: String,
    val password: String,
    val name: String,
    val roles: Set<Role>,

    val signupAt: Instant
) {
    enum class Role {
        ROLE_USER,
        ROLE_ADMIN,
        ;
    }
}
