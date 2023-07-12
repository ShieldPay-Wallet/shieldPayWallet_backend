package utils;

public class AccountNumberGenerator {
    public static String generateAccountNumber(String phoneNumber){
        String accountNumberGenerated = phoneNumber.substring(1, phoneNumber.length());
        return accountNumberGenerated;
    }


}
