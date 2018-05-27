package mvanbrummen.gitforge.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class SignupController {

    @GetMapping("/signup")
    fun signupPage(model: Model): String {
        return "signup"
    }
}