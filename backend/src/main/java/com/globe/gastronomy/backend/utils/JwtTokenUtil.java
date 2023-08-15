package com.globe.gastronomy.backend.utils;

import com.globe.gastronomy.backend.dto.UserDto;
import com.globe.gastronomy.backend.dto.UserDtoPopulator;
import com.globe.gastronomy.backend.model.Role;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@Component
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String extractUsername(String token) {
        String claims = extractClaim(token, Claims::getSubject);
        return new UserDtoPopulator().stringConverter(claims).getEmail();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractUsername(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDto userDto, Set<Role> roles) {

        return Jwts.builder()
                .setSubject(userDto.toString())
                .claim("role", roles)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(Instant.now().plus(expiration, ChronoUnit.MILLIS)))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            LogUtil.printLog("Invalid JWT signature.", JwtTokenUtil.class);
        } catch (MalformedJwtException e) {
            LogUtil.printLog("Invalid JWT token.", JwtTokenUtil.class);
        } catch (ExpiredJwtException e) {
            LogUtil.printLog("Expired JWT token.", JwtTokenUtil.class);
        } catch (UnsupportedJwtException e) {
            LogUtil.printLog("Unsupported JWT token.", JwtTokenUtil.class);
        } catch (IllegalArgumentException e) {
            LogUtil.printLog("JWT token compact of handler are invalid.", JwtTokenUtil.class);
        }
        return false;
    }

    public String getToken(HttpServletRequest httpServletRequest) {
        final String bearerToken = httpServletRequest.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // The part after "Bearer "
        }
        return null;
    }
}
