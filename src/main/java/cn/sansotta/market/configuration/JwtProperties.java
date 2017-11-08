package cn.sansotta.market.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String audience;
    private String issuer;
    private String secret;
    private long expireSecs;

    public String getAudience() { return audience; }

    public void setAudience(String audience) { this.audience = audience; }

    public String getIssuer() { return issuer; }

    public void setIssuer(String issuer) { this.issuer = issuer; }

    public String getSecret() { return secret; }

    public void setSecret(String secret) { this.secret = secret; }

    public long getExpireSecs() { return expireSecs; }

    public void setExpireSecs(long expireSecs) { this.expireSecs = expireSecs; }
}
