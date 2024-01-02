package skyguide.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import skyguide.domain.User;
import skyguide.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@RequestParam String username, @RequestParam String email, @RequestParam String password, @RequestParam String phone) {
        if (!userService.isEmailAvailable(email)) {
            return "redirect:/signup?error=EmailNotAvailable";
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setPhone(phone);

        userService.saveUser(newUser);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password,
                            @RequestParam(value = "rememberMe", required = false) String rememberMe,
                            HttpSession session, HttpServletResponse response, Model model) {
        User user = userService.loginUser(email, password);

        if (user != null) {
            session.setAttribute("currentUser", user);
            model.addAttribute("currentUser", user);

            // Remember Me? 체크박스에 체크된 경우
            if (rememberMe != null && rememberMe.equals("on")) {
                String rememberMeToken = user.generateRememberMeToken();
                Cookie rememberMeCookie = new Cookie("rememberMe", "true");
                rememberMeCookie.setMaxAge(30 * 24 * 60 * 60); // 쿠키 유효기간 30일(=30*24*60*60sec)
                response.addCookie(rememberMeCookie);
                user.setRememberMeToken(rememberMeToken);
                userService.saveUser(user);
            }

            return "redirect:/main";
        } else {
            return "redirect:/login?error=InvalidCredentials";
        }
    }

    @GetMapping("/main")
    public String showMainPage(HttpSession session, HttpServletRequest request, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("rememberMe") && cookie.getValue().equals("true")) {
                        User user = userService.loginUserWithRememberMeToken(cookie.getValue());
                        if (user != null) {
                            session.setAttribute("currentUser", user);
                            model.addAttribute("currentUser", user);
                        }
                    }
                }
            }
        } else {
            model.addAttribute("currentUser", currentUser);
        }

        return "main";
    }

    @GetMapping("/findPW")
    public String findPWPage() {
        return "findPW";
    }

    @PostMapping("/findPW")
    public String findPassword(@RequestParam String email, Model model) {
        String foundPassword = userService.findPassword(email);

        if (foundPassword != null) {
            model.addAttribute("foundPassword", foundPassword);
            return "findPWResult";
        } else {
            model.addAttribute("error", "해당 ID가 존재하지 않습니다. 올바른 ID(이메일)를 입력해주세요.");
            return "findPW";
        }
    }

}
