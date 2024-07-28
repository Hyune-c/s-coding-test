package com.hyunec.app.api.domain.entity

import java.time.Instant

data class User(
    val email: String,
    val password: String,
    val name: String,

    val roles: Set<Role> = setOf(Role.ROLE_USER),
    val signupAt: Instant = Instant.now(),
) {
    enum class Role {
        ROLE_USER,
        ROLE_ADMIN,
        ;
    }
}
