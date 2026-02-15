package com.cineaabyss.proxy.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

@Component
public class TrafficDecider {

    public boolean routeToMovies(HttpServletRequest request, int percent) {
        if (percent >= 100) {
            return true;
        }
        if (percent <= 0) {
            return false;
        }

        String key = request.getHeader("X-Request-Id");
        if (key == null || key.isBlank()) {
            key = request.getRemoteAddr();
        }

        int bucket = bucketOto99(key);
        return bucket <= percent;
    }


    private int bucketOto99(String key) {
        CRC32 checksum = new CRC32();
        checksum.update(key.getBytes(StandardCharsets.UTF_8));
        long value = checksum.getValue();
        return (int) (value % 100);
    }
}
