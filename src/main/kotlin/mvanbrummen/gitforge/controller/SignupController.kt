package mvanbrummen.gitforge.controller

import mvanbrummen.gitforge.service.UserService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping

@Controller
class SignupController(private val userService: UserService) {

    @GetMapping("/signup")
    fun signupPage(model: Model): String {
        model.addAttribute("signupForm", SignupForm())
        return "signup"
    }

    @PostMapping("/signup")
    fun signup(@ModelAttribute signupForm: SignupForm, model: Model): String {
        with(signupForm) {
            userService.createUser(username ?: "", emailAddress ?: "", password ?: "")
        }

        return "redirect:/login"
    }
}

data class SignupForm(
        val username: String? = null,
        val emailAddress: String? = null,
        val password: String? = null)