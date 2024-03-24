package de.bildwerkmedien.fluidqr.server.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RedirectionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Redirection getRedirectionSample1() {
        return new Redirection().id(1L).description("description1").code("code1").url("url1");
    }

    public static Redirection getRedirectionSample2() {
        return new Redirection().id(2L).description("description2").code("code2").url("url2");
    }

    public static Redirection getRedirectionRandomSampleGenerator() {
        return new Redirection()
            .id(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString());
    }
}
