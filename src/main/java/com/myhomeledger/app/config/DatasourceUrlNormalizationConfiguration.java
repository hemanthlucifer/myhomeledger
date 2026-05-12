package com.myhomeledger.app.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Render Postgres commonly provides DATABASE_URL in the form:
 *   postgres://user:pass@host:port/db
 *
 * Spring expects a JDBC URL. This normalizes postgres URLs to jdbc:postgresql URLs and
 * also derives username/password when they are embedded in the URL.
 */
@Configuration
@ConditionalOnProperty(name = "spring.datasource.url")
public class DatasourceUrlNormalizationConfiguration {

    @Bean
    DataSource dataSource(Environment env) {
        String rawUrl = env.getProperty("spring.datasource.url");
        String username = env.getProperty("spring.datasource.username");
        String password = env.getProperty("spring.datasource.password");

        NormalizedJdbc normalized = normalize(rawUrl, username, password);

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(normalized.jdbcUrl());
        if (normalized.username() != null && !normalized.username().isBlank()) {
            ds.setUsername(normalized.username());
        }
        if (normalized.password() != null && !normalized.password().isBlank()) {
            ds.setPassword(normalized.password());
        }
        return ds;
    }

    private static NormalizedJdbc normalize(String url, String username, String password) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("spring.datasource.url must be set (via JDBC_DATABASE_URL or DATABASE_URL)");
        }
        String trimmed = url.trim();
        if (trimmed.startsWith("jdbc:")) {
            return new NormalizedJdbc(trimmed, username, password);
        }
        if (trimmed.startsWith("postgres://") || trimmed.startsWith("postgresql://")) {
            URI uri = parseUri(trimmed);
            String userInfo = uri.getUserInfo(); // user:pass
            String derivedUser = username;
            String derivedPass = password;
            if ((derivedUser == null || derivedUser.isBlank()) && userInfo != null && !userInfo.isBlank()) {
                int idx = userInfo.indexOf(':');
                derivedUser = idx >= 0 ? userInfo.substring(0, idx) : userInfo;
                derivedPass = (idx >= 0 && idx + 1 < userInfo.length()) ? userInfo.substring(idx + 1) : derivedPass;
            }

            String host = uri.getHost();
            int port = uri.getPort();
            String path = uri.getPath(); // /db
            String query = uri.getQuery(); // sslmode=require...

            StringBuilder jdbc = new StringBuilder("jdbc:postgresql://")
                    .append(host);
            if (port > 0) {
                jdbc.append(':').append(port);
            }
            if (path != null && !path.isBlank()) {
                jdbc.append(path);
            }
            if (query != null && !query.isBlank()) {
                jdbc.append('?').append(query);
            }
            return new NormalizedJdbc(jdbc.toString(), derivedUser, derivedPass);
        }
        // Fall back: let the driver try to interpret it.
        return new NormalizedJdbc(trimmed, username, password);
    }

    private static URI parseUri(String raw) {
        try {
            return new URI(raw);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid datasource URL: " + raw, e);
        }
    }

    private record NormalizedJdbc(String jdbcUrl, String username, String password) {}
}

