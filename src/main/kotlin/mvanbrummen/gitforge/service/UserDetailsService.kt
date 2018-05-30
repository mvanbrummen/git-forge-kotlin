package mvanbrummen.gitforge.service

import mvanbrummen.gitforge.repository.UserRepository
import org.jooq.generated.tables.pojos.Account

import org.springframework.security.core.GrantedAuthority
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

        return UserPrincipal(user)
    }

    fun createUser(username: String, emailAddress: String, password: String): UserPrincipal {
        userRepository.saveUser(UUID.randomUUID(), username, emailAddress,
                passwordEncoder.encode(password))

        return UserPrincipal(userRepository.getUser(username) ?: throw RuntimeException())
    }
}

class UserPrincipal(private val user: Account) : UserDetails {

    override fun getAuthorities(): List<GrantedAuthority> {
        return listOf(GrantedAuthority { "USER" })
    }

    override fun isEnabled(): Boolean = true

    override fun getUsername(): String = user.username

    override fun isCredentialsNonExpired(): Boolean = true

    override fun getPassword(): String = user.password

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

}