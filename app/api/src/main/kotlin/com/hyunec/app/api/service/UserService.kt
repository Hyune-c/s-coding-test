package com.hyunec.app.api.service

import com.hyunec.app.api.domain.entity.User
import com.hyunec.app.api.persistence.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun signup(email: String, password: String, name: String): String {
        val user = User(email, password, name)
        return userRepository.save(user)
    }

    fun signin(email: String, password: String): User? {
        val user = userRepository.findByEmail(email) ?: throw IllegalArgumentException("User not found")
        if (user.password != password) {
            throw IllegalArgumentException("Password not matched")
        }

        return user
    }
}
