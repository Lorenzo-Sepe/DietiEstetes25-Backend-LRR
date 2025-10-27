package it.unina.dietiestates25;

import it.unina.dietiestates25.entity.User;
import it.unina.dietiestates25.exception.BadCredentialsException;
import it.unina.dietiestates25.repository.AuthorityRepository;
import it.unina.dietiestates25.repository.UserRepository;
import it.unina.dietiestates25.service.AuthService;
import it.unina.dietiestates25.service.JwtService;
import it.unina.dietiestates25.utils.Msg;
import it.unina.dietiestates25.utils.UserContex;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestChangePassword {
    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthorityRepository authorityRepository;

    private PasswordEncoder passwordEncoder;
    private AuthService authService;
    @Mock
    private JwtService jwtService;

    MockedStatic<UserContex> mocked;

    @BeforeEach
    void setup() {
        passwordEncoder = new BCryptPasswordEncoder();
        authService = new AuthService(userRepository, passwordEncoder, authorityRepository, jwtService);
        mocked = Mockito.mockStatic(UserContex.class);

    }

    @AfterEach
    void tearDown() {

        mocked.close();
    }

    /**
     * Questo test copre i nodi da 1 al 6 percorrendo solo il cammino senza errori.
     */
    @Test
    void changePassword_ShouldSave_WhenValid() {
        User mockUser = new User();
        mockUser.setId(1);

        mockUser.setPassword(passwordEncoder.encode("oldPass"));

        mocked.when(UserContex::getUserCurrent).thenReturn(mockUser);

        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));

        String result = authService.changePassword("oldPass", "newPass", "newPass");

        verify(userRepository).save(mockUser);
        assertEquals(Msg.PASSWORD_CHANGED, result);
        // assert true when password encorder match new password with mockUser password
        assertTrue(passwordEncoder.matches("newPass", mockUser.getPassword()));

    }

    /**
     * Questo test copre il nodo 1 e 1.e
     * * verifica che venga lanciata una UnauthorizedException quando l'utente non
     * viene trovato nel userContex.
     */
    @Test
    void changePassword_ShouldThrowUnauthorizedException_WhenUserContextNotFound() {
        mocked.when(UserContex::getUserCurrent).thenReturn(null);
        assertThrows(it.unina.dietiestates25.exception.UnauthorizedException.class,
                () -> authService.changePassword("oldPass", "newPass", "newPass"));
    }

    /**
     * Questo test copre i nodi 1, 2, 2.e
     * * verifica che venga lanciata una ResourceNotFoundException quando l'utente
     * non viene trovato nel database.
     */
    @Test
    void changePassword_ShouldThrowResourceNotFoundException_WhenUserNotFoundInDB() {

        User mockUser = new User();
        mockUser.setId(1);

        mocked.when(UserContex::getUserCurrent).thenReturn(mockUser);

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(it.unina.dietiestates25.exception.ResourceNotFoundException.class,
                () -> authService.changePassword("oldPass", "newPass", "newPass"));

    }

    /**
     * Questo test copre i nodi 1, 2, 3, 3.e
     * * verifica che venga lanciata una BadCredentialsException quando la vecchia
     * password non corrisponde alla password corrente del user.
     */

    @Test
    void changePassword_ShouldThrowBadCredentialsException_WhenOldPasswordNotMatch() {
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setPassword(passwordEncoder.encode("oldPass"));

        mocked.when(UserContex::getUserCurrent).thenReturn(mockUser);

        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));

        assertThrows(BadCredentialsException.class,
                () -> authService.changePassword("wrongOldPass", "newPass", "newPass"));
    }

    /**
     * Questo test copre i nodi 1, 2, 3, 4, 4.e
     * * verifica che venga lanciata una ConflictException quando la nuova password
     * e la password di conferma non corrispondono.
     */
    @Test
    void changePassword_ShouldThrowConflictException_WhenNewPasswordNotMatchConfirmPassword() {
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setPassword(passwordEncoder.encode("oldPass"));

        mocked.when(UserContex::getUserCurrent).thenReturn(mockUser);

        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));

        assertThrows(it.unina.dietiestates25.exception.ConflictException.class,
                () -> authService.changePassword("oldPass", "newPass", "differentNewPass"));
    }

    /**
     * Questo test copre i nodi 1, 2, 3, 4, 5, 5.e
     * * verifica che venga lanciata una ConflictException quando la nuova password
     * è uguale alla password attuale.
     */
    @Test
    void changePassword_ShouldThrowConflictException_WhenNewPasswordIsSameAsOldPassword() {
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setPassword(passwordEncoder.encode("oldPass"));

        mocked.when(UserContex::getUserCurrent).thenReturn(mockUser);

        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));

        assertThrows(it.unina.dietiestates25.exception.ConflictException.class,
                () -> authService.changePassword("oldPass", "oldPass", "oldPass"));
    }

    /**
     * Questo test copre i nodi 1, 2, 3, 4, 5, 6, 6.e
     * * verifica che venga lanciata una RuntimeException quando si verifica un
     * errore durante il salvataggio della nuova password.
     */
    @Test
    void changePassword_ShouldThrowRuntimeException_WhenErrorOnSave() {
        User mockUser = new User();

        mockUser.setId(1);
        mockUser.setPassword(passwordEncoder.encode("oldPass"));

        mocked.when(UserContex::getUserCurrent).thenReturn(mockUser);

        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
        // simula un errore durante il salvataggio
        when(userRepository.save(mockUser)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> authService.changePassword("oldPass", "newPass", "newPass"));
    }
}
