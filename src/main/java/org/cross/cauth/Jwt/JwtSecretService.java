package org.cross.cauth.Jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.cross.cauth.Exception.InvalidCredentialsException;
import org.cross.cauth.utils.UniqueIdGenerator;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
@RequiredArgsConstructor
public class JwtSecretService {
    private final JwtSecretRepository jwtSecretRepository;
    private final UniqueIdGenerator uniqueIdGenerator;

     public SecretKey createJwtSecret(String appId){
         String secret = uniqueIdGenerator.generateSLCode(312);

         jwtSecretRepository.save(new JwtSecretKey(appId, secret));

         return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
     }

     public SecretKey getJwtSecret(String appId){
         JwtSecretKey jwtSecretKey = jwtSecretRepository.findByAppId(appId)
                 .orElseThrow(() -> new InvalidCredentialsException("Invalid app id"));

         return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey.getSecret()));
     }

    public String getJwtSecretString(String appId){
        JwtSecretKey jwtSecretKey = jwtSecretRepository.findByAppId(appId)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid app id"));

        return jwtSecretKey.getSecret();
    }

     public SecretKey updateJwtSecret(String appId){
         JwtSecretKey jwtSecretKey = jwtSecretRepository.findByAppId(appId)
                 .orElseThrow(() -> new InvalidCredentialsException("Invalid app id"));

         String secret = uniqueIdGenerator.generateSLCode(312);

         jwtSecretKey.setSecret(secret);

         return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
     }

     public boolean deleteJwtSecret(String appId){
         return jwtSecretRepository.deleteByAppId(appId);
     }
}
