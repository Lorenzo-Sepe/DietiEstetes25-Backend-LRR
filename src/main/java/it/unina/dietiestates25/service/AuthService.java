package it.unina.dietiestates25.service;

import it.unina.dietiestates25.repository.AuthorityRepository;
import it.unina.dietiestates25.utils.Msg;
import it.unina.dietiestates25.utils.GenericMail;
import it.unina.dietiestates25.entity.Authority;
import it.unina.dietiestates25.entity.User;
import it.unina.dietiestates25.dto.request.SignUpRequest;
import it.unina.dietiestates25.exception.ConflictException;
import it.unina.dietiestates25.exception.ResourceNotFoundException;
import it.unina.dietiestates25.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
   private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
   private final AuthorityRepository authorityRepository;
   private final JwtService jwtService;

    public String signup(SignUpRequest request){
        if(userRepository.existsByUsernameOrEmail(request.username(), request.email()))
            throw new ConflictException(Msg.USER_ALREADY_PRESENT);
        Authority authority = authorityRepository.findByDefaultAuthorityTrue()
                .orElseThrow(() -> new ResourceNotFoundException("Authority", "defaultAuthority", true));
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .enabled(false)
                .authority(authority)
                .confirmCode(UUID.randomUUID().toString())
                .build();
        userRepository.save(user);
        return Msg.USER_SIGNUP_FIRST_STEP;
    }


}
