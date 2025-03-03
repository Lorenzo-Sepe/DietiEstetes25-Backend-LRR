package it.unina.dietiestates25.service;

import it.unina.dietiestates25.dto.request.AgenziaImmobiliareRequest;
import it.unina.dietiestates25.dto.request.NewAgentRequest;
import it.unina.dietiestates25.dto.request.SignInRequest;
import it.unina.dietiestates25.dto.request.SignUpRequest;
import it.unina.dietiestates25.dto.response.JwtAuthenticationResponse;
import it.unina.dietiestates25.entity.AgenziaImmobiliare;
import it.unina.dietiestates25.entity.Authority;
import it.unina.dietiestates25.entity.DatiImpiegato;
import it.unina.dietiestates25.entity.User;
import it.unina.dietiestates25.entity.enumeration.AuthorityName;
import it.unina.dietiestates25.exception.BadCredentialsException;
import it.unina.dietiestates25.exception.ConflictException;
import it.unina.dietiestates25.exception.ResourceNotFoundException;
import it.unina.dietiestates25.repository.AgenziaImmobiliareRepository;
import it.unina.dietiestates25.repository.AuthorityRepository;
import it.unina.dietiestates25.repository.DatiImpiegatoRepository;
import it.unina.dietiestates25.repository.UserRepository;
import it.unina.dietiestates25.utils.Msg;
import it.unina.dietiestates25.utils.UserContex;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final SecureRandom RANDOM = new SecureRandom();
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final JwtService jwtService;
    private final AgenziaImmobiliareRepository agenziaImmobiliareRepository;
    private final DatiImpiegatoRepository datiImpiegatoRepository;

    public String signup(SignUpRequest request){
        if(userRepository.existsByUsernameOrEmail(request.username(), request.email()))
            throw new ConflictException(Msg.USER_ALREADY_PRESENT);
        Authority authority = authorityRepository.findByDefaultAuthorityTrue()
                .orElseThrow(() -> new ResourceNotFoundException("Authority", "defaultAuthority", true));
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .authority(authority)
                .build();
        userRepository.save(user);
        return Msg.USER_SIGNUP_FIRST_STEP;
    }

    @Transactional
    public JwtAuthenticationResponse signin(SignInRequest request) {
        User user = userRepository.findByUsernameOrEmail(request.usernameOrEmail(), request.usernameOrEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username or email", request.usernameOrEmail()));

        if(!passwordEncoder.matches(request.password(), user.getPassword()))
            throw new BadCredentialsException("Bad credentials");

        String authority = user.getAuthority().getAuthorityName().name();
        String jwt = jwtService.generateToken(user);

        return JwtAuthenticationResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .authority(authority)
                .token(jwt)
                .build();
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

    public User createAdmin(AgenziaImmobiliareRequest request){
        Authority authority = authorityRepository.findByAuthorityName(AuthorityName.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Authority", "authorityName", AuthorityName.ADMIN));
        return User.builder()
                .email(request.getEmailFondatore())
                .username(request.getEmailFondatore())
                .authority(authority)
                .password("admin")
                .build();
    }

    public User createAgente(NewAgentRequest request, UserDetails userDetails) {
        User userAdmin = (User) userDetails;
        AgenziaImmobiliare agenziaImmobiliare = agenziaImmobiliareRepository.findAgenziaImmobiliareByDipendentiContains(userAdmin)
                .orElseThrow(() -> new ResourceNotFoundException("Agenzia Immobiliare", "dipendente", userAdmin.getUsername()));

        String newUsername = request.getNome() + request.getCognome();
        String newMail = request.getNome() +"."+ request.getCognome() +generateUniqueId(3)+ "@" + agenziaImmobiliare.getDominio()+"dietiestates25.com";

        if(userRepository.existsByUsernameOrEmail(newUsername, newMail))
            throw new ConflictException(Msg.USER_ALREADY_PRESENT);
        Authority authority = authorityRepository.findByAuthorityName(AuthorityName.AGENT)
                .orElseThrow(() -> new ResourceNotFoundException("Authority", "defaultAuthority", true));
        User user = User.builder()
                .username(newUsername)
                .email(newMail)
                .password(passwordEncoder.encode("admin"))
                .authority(authority)
                .build();
        userRepository.save(user);
        DatiImpiegato datiImpiegato= DatiImpiegato.builder()
                .nome(request.getNome())
                .cognome(request.getCognome())
                .user(user)
                .build();
        datiImpiegatoRepository.save(datiImpiegato);
        return user;
    }

    public static String generateUniqueId(int length) {
        String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public String changePassword(String oldPassword, String newPassword) {
        User user = userRepository.findById(UserContex.getUserCurrent().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", UserContex.getUserCurrent().getId()));
        if(!passwordEncoder.matches(oldPassword, user.getPassword()))
            throw new BadCredentialsException("La inserita non corrisponde alla password attuale");
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return Msg.PASSWORD_CHANGED;
    }
}
