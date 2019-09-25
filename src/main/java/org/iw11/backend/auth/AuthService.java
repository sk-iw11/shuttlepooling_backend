package org.iw11.backend.auth;

import com.google.common.hash.Hashing;
import org.iw11.backend.config.BusesConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    private static final int TOKEN_LENGTH = 32;

    private static final String CHARS_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHARS_UPPER = CHARS_LOWER.toUpperCase();
    private static final String NUMBERS = "0123456789";

    private static final String RANDOM_SET = CHARS_LOWER + CHARS_UPPER + NUMBERS;

    private SecureRandom random = new SecureRandom();

    private Map<String, String> tokenMap = new ConcurrentHashMap<>();

    private BusesConfigService busesConfigService;

    @Autowired
    public AuthService(BusesConfigService busesConfigService) {
        this.busesConfigService = busesConfigService;
    }

    public Optional<String> authorize(String bus, String password) {
        var sha256 = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
        var trueSha256 = busesConfigService.getBusPassword(bus);
        if (trueSha256.isEmpty())
            return Optional.empty();
        if (!sha256.equals(trueSha256.get()))
            return Optional.empty();
        var token = generateToken();
        tokenMap.put(token, bus);
        return Optional.of(token);
    }

    public Optional<String> getBusByToken(String token) {
        return Optional.ofNullable(tokenMap.get(token));
    }

    private String generateToken() {
        var builder = new StringBuilder();
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            var randomIndex = random.nextInt(RANDOM_SET.length());
            var randomChar = RANDOM_SET.charAt(randomIndex);
            builder.append(randomChar);
        }
        return builder.toString();
    }
}
