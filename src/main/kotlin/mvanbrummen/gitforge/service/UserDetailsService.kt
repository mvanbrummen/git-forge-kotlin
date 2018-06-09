package mvanbrummen.gitforge.service

import mvanbrummen.gitforge.repository.UserRepository
import org.jooq.generated.tables.pojos.Account
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.getUser(username) ?: throw UsernameNotFoundException(username)

        return User(user.username, user.password, listOf(GrantedAuthority { "USER" }))
    }

    fun getUserByEmailAddress(emailAddress: String): Account? = userRepository.getUserByEmailAddress(emailAddress)
    
    fun createUser(username: String, emailAddress: String, password: String): User {
        userRepository.saveUser(UUID.randomUUID(), username, emailAddress,
                passwordEncoder.encode(password))

        val user = userRepository.getUser(username) ?: throw RuntimeException()

        return User(user.username, user.password, listOf(GrantedAuthority { "USER" }))
    }
}

