package org.cross.cauth.Jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${app.jwt-secret}")
    private String JWT_SECRET;

    @Value("${app.jwt-expiration}")
    private long JWT_EXP;

    private SecretKey getSignInKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_SECRET));
    }

    public String generateToken(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .issuer("Cross-Auth-v1")
                .expiration(new Date(System.currentTimeMillis() + JWT_EXP))
                .signWith(getSignInKey())
                .compact();
    }

    public String generateTokenWithRole(String username, String role){
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .issuer("Cross-Auth-v1")
                .expiration(new Date(System.currentTimeMillis() + JWT_EXP))
                .signWith(getSignInKey())
                .compact();
    }

    public String generateTokenForClient(String email, String appId, SecretKey signInKey){
        return Jwts.builder()
                .header()
                .add("X-CROSS-AUTH-VERSION", "v1.0.0")
                .and()
                .subject(email)
                .claim("app", appId)
                .issuer("Cross-Auth-v1")
                .expiration(new Date(System.currentTimeMillis() + 1000*60*60*10))
                .issuedAt(new Date())
                .signWith(signInKey)
                .compact();
    }

    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    public String extractSubject(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public boolean isValid(String token, UserDetails userDetails){
        String userName = userDetails.getUsername();
        String subject = extractSubject(token);

        return userName.equals(subject) && !isExpired(token);
    }
}
