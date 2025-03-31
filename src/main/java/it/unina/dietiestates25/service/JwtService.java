package it.unina.dietiestates25.service;

import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import it.unina.dietiestates25.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String jwtSigningKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${spring.application.name}")
    private String issuer;
    @Value("${application.security.jwt.auth0-domain}")
    private String auth0Domain;

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder().setClaims(extraClaims)
                .setIssuer(issuer) // Identifica chi ha emesso il token
                .setSubject(userDetails.getUsername()) // Identifica il soggetto del JWT
                .setIssuedAt(new Date(System.currentTimeMillis())) //Data-Ora creazione del JWT
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Data-Ora scadenza del JWT
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public DecodedJWT decodeJWT(String accessToken) {
        try{
            JwkProvider provider = new JwkProviderBuilder(auth0Domain)
                    .cached(10, 24, TimeUnit.HOURS)
                    .build();
            log.debug("Access Token: " + accessToken);
            accessToken=accessToken.replace("\"","" );
            accessToken=accessToken.trim();
            DecodedJWT jwt = JWT.decode(accessToken);
            log.debug("Access Token: " + accessToken);
            RSAPublicKey publicKey = (RSAPublicKey) provider.get(jwt.getKeyId()).getPublicKey();
            // Verifica la firma del token
            Algorithm algorithm = Algorithm.RSA256(publicKey, null);
            algorithm.verify(jwt);

            // Controlla se il token è scaduto
            if (jwt.getExpiresAt() != null && jwt.getExpiresAt().getTime() < System.currentTimeMillis()) {
                throw new UnauthorizedException("Token scaduto");
            }
            return jwt;

        }catch (JWTVerificationException e){
            throw new JWTVerificationException("Token non valido");
        } catch (JwkException e) {
            throw new UnauthorizedException("Errore durante il recupero della chiave pubblica");
        }

    }
}
