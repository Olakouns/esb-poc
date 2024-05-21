package sn.esmt.gesb.tpo_manager.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationService {

    private static final long EXPIRATION_TIME = 864_000_00; // 1 day in milliseconds
    private static final String SECRET_KEY = "q3t6w9zCFJNcQfTjWnq3t6w9zCFJNcQfTjWnZr4u7xADGKaPd";
    private static final SecretKey SIGNING_KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    private static final String PREFIX = "Bearer";

    public static void addToken(HttpServletResponse res, String username, String role) throws IOException {
        Date expiryDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        String JwtToken = Jwts.builder()
                .subject(username)
//                .audience().add(tenant).and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expiryDate)
                .signWith(SIGNING_KEY)
                .compact();
//        res.addHeader("Authorization", PREFIX + " " + JwtToken);

//        Map<String, String> responseJson = new HashMap<>();
//        responseJson.put("token", JwtToken);
//        responseJson.put("role", role);
//        responseJson.put("expiryToken", String.valueOf(expiryDate.getTime()));
        String jsonResponse = new ObjectMapper().writeValueAsString(new TokenLogin(JwtToken, role, expiryDate));

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.setContentLength(jsonResponse.length());
        res.getWriter().write(new ObjectMapper().writeValueAsString(new TokenLogin(JwtToken, role, expiryDate)));
    }

    public static Authentication getAuthentication(HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        if (token != null) {
            String user = Jwts.parser()
                    .verifyWith(SIGNING_KEY)
                    .build()
                    .parseSignedClaims(token.replace(PREFIX, "").trim())
                    .getPayload()
                    .getSubject();
            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
            }
        }
        return null;
    }

}
