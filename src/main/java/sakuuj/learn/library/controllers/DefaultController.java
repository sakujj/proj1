package sakuuj.learn.library.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.stereotype.Controller
@RequestMapping("/*")
public class DefaultController {
    @GetMapping
    public String redirectUnknownUri() {
        return "redirect:/books";
    }
}
