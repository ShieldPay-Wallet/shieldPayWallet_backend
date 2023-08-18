package utils;

import java.security.SecureRandom;

public class OTPGeneratorImplementation {
    private static SecureRandom secureRandom = new SecureRandom();
    public static String generate() {
        StringBuilder OTPBuild = new StringBuilder()
                .append(digitGenerator())
                .append(digitGenerator())
                .append(digitGenerator())
                .append(digitGenerator());

        return OTPBuild.toString();
    }
    private static int digitGenerator(){
        return secureRandom.nextInt(9);
    }


}
