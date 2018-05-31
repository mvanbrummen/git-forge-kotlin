package mvanbrummen.gitforge.config

import mvanbrummen.gitforge.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
open class SecurityConfig(
        private val userService: UserService,
        private val passwordEncoder: PasswordEncoder) : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(authenticationProvider())
    }

    @Bean
    open fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        with(authProvider) {
            setUserDetailsService(userService)
            setPasswordEncoder(passwordEncoder)
        }

        return authProvider
    }

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
                .antMatchers("/webjars/**", "/images/**", "/js/**", "/css/**", "/about", "/signup").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/about")
                .permitAll()
    }
}