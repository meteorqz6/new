package skyguide.controller;

import com.mysql.cj.Session;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import skyguide.domain.User;
import skyguide.respository.UserRepository;
import skyguide.service.EmailService;
import skyguide.service.UserService;

import java.time.LocalDateTime;
import java.util.Random;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

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
    public String findPassword(@RequestParam String email, @RequestParam String username, @RequestParam String phone, @RequestParam(required = false) String verificationCode, Model model) {
        User user = userRepository.findByEmail(email);

        if (verificationCode != null && user != null && user.getVerificationCode().equals(verificationCode)) {
            return "resetPW";
        } else {
//            model.addAttribute("errorMessage", "잘못된 인증코드입니다.");
            return "findPW";
        }
    }

    @PostMapping("/sendVerificationEmail")
    public String sendVerificationEmail(@RequestParam String email, @RequestParam String username, @RequestParam String phone, Model model) {
        // 매개변수 순서 안 맞춰서 오류났음
        boolean userExists = userService.checkUserExists(username, email, phone);

        if (userExists) {
            User user = userRepository.findByEmail(email);
            String verificationCode = generateVerificationCode();
            user.setVerificationCode(verificationCode);
            userService.saveUser(user);

            try {
                emailService.sendVerificationEmail(email, verificationCode);
                model.addAttribute("email", email);
                return "findPW";
            } catch (Exception e) {
//                model.addAttribute("errorMessage", "인증 이메일 전송에 실패했습니다.");
                return "findPW";
            }
        } else {
//            model.addAttribute("errorMessage", "일치하는 회원정보가 없습니다. 다시 확인해주세요.");
            return "findPW";
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int random6num = 100000 + random.nextInt(900000);
        String verificationCode = String.valueOf(random6num);
        return verificationCode;
    }

    @GetMapping("/verifyPW")
    public String verifyPage(){
        return "verify";
    }

    @PostMapping("/verifyPW")
    public String verifyPassword(@RequestParam String username,
                                 @RequestParam String email,
                                 @RequestParam String phone,
                                 @RequestParam String verificationCode,
                                 Model model) {
        boolean isVerified = emailService.verifyUser(username, email, phone, verificationCode);

        if (isVerified) {
            model.addAttribute("email", email);
            return "resetPW";
        } else {
//            model.addAttribute("errorMessage", "잘못된 인증코드입니다.");
            return "findPW";
        }
    }

    @GetMapping("/resetPW")
    public String showResetPasswordForm() {
        return "resetPW";
    }

    @PostMapping("/resetPW")
    public String resetPassword(@RequestParam String email,
                                @RequestParam String newPassword,
                                Model model){
        boolean resetSuccess = userService.resetPassword(email, newPassword);

        if (resetSuccess){
            return "redirect:/login";
        } else {
//            model.addAttribute("errorMessage", "비밀번호 재설정에 실패했습니다.");
            return "resetPW";
        }
    }


}


