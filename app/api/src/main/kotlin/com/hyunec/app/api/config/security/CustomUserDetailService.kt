package com.hyunec.app.api.config.security

import com.hyunec.app.api.persistence.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        // todo custom exception 처리 필요
        val user = userRepository.findByEmail(username) ?: throw IllegalArgumentException("User not found")
        return User(
            user.email,
            user.password,
            user.roles.map { SimpleGrantedAuthority(it.name) }.toMutableList()
        )
    }
}
