package it.unina.dietiestates25.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import it.unina.dietiestates25.dto.request.SignInRequest;
import it.unina.dietiestates25.dto.request.SignUpRequest;
import it.unina.dietiestates25.dto.response.JwtAuthenticationResponse;
import it.unina.dietiestates25.entity.Authority;
import it.unina.dietiestates25.entity.User;
import it.unina.dietiestates25.entity.enumeration.AuthorityName;
import it.unina.dietiestates25.exception.*;
import it.unina.dietiestates25.repository.AuthorityRepository;
import it.unina.dietiestates25.repository.UserRepository;
import it.unina.dietiestates25.utils.Msg;
import it.unina.dietiestates25.utils.UserContex;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private static final String FIELD_EMAIL = "email";

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
                .orElseThrow(() -> new ResourceNotFoundException("Account", FIELD_EMAIL
, request.Email()));

        if(!passwordEncoder.matches(request.password(), user.getPassword()))
            throw new BadCredentialsException("Password Errata");


        String jwt = jwtService.generateToken(user);

        return JwtAuthenticationResponse.fromEntityToDto(user,jwt);
    }


    public String modifyUserAuthority(int id, String auth) {
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
        userRepository.save(user);
        return Msg.AUTHORITY_CHANGED;
    }



    public String changePassword(String oldPassword, String newPassword, String confirmPassword) {

        //1
        if(UserContex.getUserCurrent() == null)
            throw new UnauthorizedException("Utente non autenticato. Effettua il login per cambiare la password.");

        //2
        User user = userRepository.findById(UserContex.getUserCurrent().getId())
                .orElseThrow(() -> new ResourceNotFoundException(UserContex.getUserCurrent().getEmail(),"Utente non trovato. Assicurati di essere autenticato correttamente."));

        //3
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadCredentialsException("La password inserita non corrisponde alla password attuale");
        }

        //4
        if(!newPassword.equals(confirmPassword))
            throw new ConflictException("la nuova password e la password di conferma non corrispondono");

        //5
        if(passwordEncoder.matches(newPassword, user.getPassword()))
            throw new ConflictException("La nuova password non può essere uguale alla password attuale");




        user.setPassword(passwordEncoder.encode(newPassword));
        try {//6
        userRepository.save(user);
        }catch (Exception e){
            throw new InternalServerErrorException("Errore al momento del salvataggio della nuova password");
        }

        return Msg.PASSWORD_CHANGED;
    }

    public JwtAuthenticationResponse loginIdProv(String accessToken) {
        DecodedJWT token = jwtService.decodeJWT(accessToken);
        String email = token.getClaim(FIELD_EMAIL
).asString();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", FIELD_EMAIL
, email));

        return JwtAuthenticationResponse.fromEntityToDto(user,jwtService.generateToken(user));
    }

}
