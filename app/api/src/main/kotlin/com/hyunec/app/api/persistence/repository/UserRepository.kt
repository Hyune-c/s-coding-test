package com.hyunec.app.api.persistence.repository

import com.hyunec.app.api.domain.entity.User
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class UserRepository(
    private val pool: ConcurrentHashMap<String, User> = ConcurrentHashMap()
) {
    fun save(user: User): String {
        pool[user.email] = user
        return user.email
    }

    fun findByEmail(email: String): User? {
        return pool[email]
    }

    fun findAll(): List<User> {
        return pool.values.toList()
    }
}
