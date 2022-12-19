package ru.kharina.study.springsecurityconference.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kharina.study.springsecurityconference.model.Role;
import ru.kharina.study.springsecurityconference.model.Status;
import ru.kharina.study.springsecurityconference.model.User;
import ru.kharina.study.springsecurityconference.modeldb.Speaker;
import ru.kharina.study.springsecurityconference.modeldb.Visitor;
import ru.kharina.study.springsecurityconference.modeldto.RegisterDto;
import ru.kharina.study.springsecurityconference.repository.UserRepository;
import ru.kharina.study.springsecurityconference.repositorydb.SpeakerRepository;
import ru.kharina.study.springsecurityconference.repositorydb.VisitorRepository;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final SpeakerRepository speakerRepository;
    private final VisitorRepository visitorRepository;

    public AuthController(UserRepository userRepository,
                          SpeakerRepository speakerRepository,
                          VisitorRepository visitorRepository) {
        this.userRepository = userRepository;
        this.speakerRepository = speakerRepository;
        this.visitorRepository = visitorRepository;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/success")
    public String getSuccessPage() {
        return "success";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto request) {
        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
            String hashedPassword = passwordEncoder.encode(request.getPassword());
            User user = new User(request.getEmail(),
                    hashedPassword,
                    request.getFirstName(),
                    request.getLastName(),
                    Role.VISITOR,
                    Status.ACTIVE);
            User savedUser = userRepository.save(user);
            if (request.getSpeakerType().equals("speaker")) {
                Speaker speaker = new Speaker(request.getFirstName()+" "+request.getLastName(),
                        request.getEmail(),
                        Math.toIntExact(savedUser.getId()));
                speakerRepository.save(speaker);
            } else {
                Visitor visitor = new Visitor(request.getFirstName()+" "+request.getLastName(),
                        request.getEmail(),
                        Math.toIntExact(savedUser.getId()));
                visitorRepository.save(visitor);
            }
            Map<Object, Object> response = new HashMap<>();
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid email/password combination", HttpStatus.FORBIDDEN);
        }
    }
}
