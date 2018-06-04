package mvanbrummen.gitforge.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {

    @GetMapping("/about")
    fun homePage(model: Model): String {
        model.addAttribute("signupForm", SignupForm())
        return "home"
    }
}