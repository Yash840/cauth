package org.cross.cauth.utils;

import lombok.RequiredArgsConstructor;
import org.cross.cauth.Exception.InvalidCredentialsException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SecretCodeService {
    private final RedisTemplate<String, String> redisTemplate;
    private final UniqueIdGenerator uniqueIdGenerator;


    /**
     * Creates authorization code for given user (with authId and appId)
     * @param authId The authId of user
     * @param appId The appId with which user is associated
     * @return 12 characters <code>String</code> authorization code with 10 min lifespan
     */
    public String generateAuthCode(String authId, String appId){

        //Creating single string from appId and authId for storing them easily in single String
        String redisValue = appId + ":" + authId;

        //Generating cryptographically secure and unique code of length 12
        String authCode = uniqueIdGenerator.generateSLCode(12);

        //Creating redisKey from authCode
        String redisKey = "auth-code:" + authCode;

        redisTemplate.opsForValue().set(redisKey, redisValue, 10, TimeUnit.MINUTES);

        return authCode;
    }

    public String generateUserPasswordResetCode(String authId){

        //Generating cryptographically secure and unique code of length 12
        String passResetCode = uniqueIdGenerator.generateSLCode(6);

        //Creating redisKey from authCode
        String redisKey = "reset-password-u:" + passResetCode;

        redisTemplate.opsForValue().set(redisKey, authId, 10, TimeUnit.MINUTES);

        return passResetCode;
    }

    /**
     * Method to get user details using auth code
     * @param code auth code
     * @return String consisting appId and authId separated by <code>:</code> as <code>appId:authId</code>
     * @throws InvalidCredentialsException If no value with given code found
     */
    public String consumeAuthCode(String code){
        //Building redisKey from authCode
        String redisKey = "auth-code:" + code;

        String value = redisTemplate.opsForValue().getAndDelete(redisKey);

        if(value == null) throw new InvalidCredentialsException("Auth Code is invalid or expired");

        return value;
    }

    public String consumeResetPassCode(String code){
        //Building redisKey from authCode
        String redisKey = "reset-password-u:" + code;

        String value = redisTemplate.opsForValue().getAndDelete(redisKey);

        if(value == null) throw new InvalidCredentialsException("Code is invalid or expired");

        return value;
    }
}
