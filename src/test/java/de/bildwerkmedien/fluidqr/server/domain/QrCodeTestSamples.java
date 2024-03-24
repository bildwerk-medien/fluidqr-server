package de.bildwerkmedien.fluidqr.server.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class QrCodeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static QrCode getQrCodeSample1() {
        return new QrCode().id(1L).code("code1");
    }

    public static QrCode getQrCodeSample2() {
        return new QrCode().id(2L).code("code2");
    }

    public static QrCode getQrCodeRandomSampleGenerator() {
        return new QrCode().id(longCount.incrementAndGet()).code(UUID.randomUUID().toString());
    }
}
