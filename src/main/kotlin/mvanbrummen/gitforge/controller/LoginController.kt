package mvanbrummen.gitforge.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginController {

    @GetMapping("/login")
    fun loginPage(model: Model): String {
        return "login"
    }
}