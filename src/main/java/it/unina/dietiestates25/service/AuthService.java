package it.unina.dietiestates25.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import it.unina.dietiestates25.dto.request.SignInRequest;
import it.unina.dietiestates25.dto.request.SignUpRequest;
import it.unina.dietiestates25.dto.response.JwtAuthenticationResponse;
import it.unina.dietiestates25.entity.Authority;
import it.unina.dietiestates25.entity.User;
import it.unina.dietiestates25.entity.enumeration.AuthorityName;
import it.unina.dietiestates25.exception.BadCredentialsException;
import it.unina.dietiestates25.exception.ConflictException;
import it.unina.dietiestates25.exception.ResourceNotFoundException;
import it.unina.dietiestates25.exception.UnauthorizedException;
import it.unina.dietiestates25.repository.AuthorityRepository;
import it.unina.dietiestates25.repository.UserRepository;
import it.unina.dietiestates25.utils.Msg;
import it.unina.dietiestates25.utils.UserContex;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final JwtService jwtService;



    @Transactional
    public JwtAuthenticationResponse signup(SignUpRequest request){
        if(userRepository.existsByEmail(request.email()))
            throw new ConflictException(Msg.USER_ALREADY_PRESENT);
        Authority authority = authorityRepository.findByDefaultAuthorityTrue()
                .orElseThrow(() -> new ResourceNotFoundException("Authority", "defaultAuthority", true));
        User user = User.builder()
                .email(request.email().toLowerCase())
                .nomeVisualizzato(request.nomeVisualizzato())
                .password(passwordEncoder.encode(request.password()))
                .authority(authority)
                .build();
        userRepository.save(user);
        String jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.fromEntityToDto(user,jwt);
    }

    @Transactional
    public JwtAuthenticationResponse login(SignInRequest request) {
        User user = userRepository.findByEmail(request.Email().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "email", request.Email()));

        if(!passwordEncoder.matches(request.password(), user.getPassword()))
            throw new BadCredentialsException("Password Errata");

        String authority = user.getAuthority().getAuthorityName().name();
        String jwt = jwtService.generateToken(user);

        return JwtAuthenticationResponse.fromEntityToDto(user,jwt);
    }


    public String modifyUserAuthority(int id, String auth) {
        // trovare l'utente
        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User", "id", id));
        try{
            Authority authority = authorityRepository.findByAuthorityName(AuthorityName.valueOf(auth.toUpperCase()))
                    .orElseThrow(()-> new ResourceNotFoundException("Authority", "name", auth));
            if(user.getAuthority().equals(authority))
                throw new ConflictException(Msg.USER_HAS_SAME_AUTHORITY);
            user.setAuthority(authority);
        } catch (IllegalArgumentException ex){
            return Msg.INVALID_AUTHORITY;
        }
        // salvare
        userRepository.save(user);
        return Msg.AUTHORITY_CHANGED;
    }



    public String changePassword(String oldPassword, String newPassword, String confirmPassword) {

        User user = userRepository.findById(Objects.requireNonNull(UserContex.getUserCurrent()).getId())
                .orElseThrow(() -> new UnauthorizedException("Utente non trovato. Assicurati di essere autenticato correttamente."));

        if(!newPassword.equals(confirmPassword))
            throw new ConflictException("Le password non corrispondono");

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadCredentialsException("La password inserita non corrisponde alla password attuale");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return Msg.PASSWORD_CHANGED;
    }

    public JwtAuthenticationResponse loginIdProv(String accessToken) {
        DecodedJWT token = jwtService.decodeJWT(accessToken);
        String email = token.getClaim("email").asString();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        return JwtAuthenticationResponse.fromEntityToDto(user,jwtService.generateToken(user));
    }

}
