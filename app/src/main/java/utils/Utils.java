package utils;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.regex.Pattern;

public class Utils {
    public static boolean isEmpty(String subject)
    {
        return subject.replace(" ", "").length() == 0;
    }

    public static boolean isEmailValid(String email)
    {
        Pattern emailPattern = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", Pattern.CASE_INSENSITIVE);
        return emailPattern.matcher(email).find();
    }

    public static boolean isCnpjValid(String cnpj) {
        // removing everything that isn't a number
        cnpj = cnpj.replace("-", "").replace(".", "").replace("/", "");

        if (
            cnpj.equals("00000000000000") || cnpj.equals("11111111111111") ||
            cnpj.equals("22222222222222") || cnpj.equals("33333333333333") ||
            cnpj.equals("44444444444444") || cnpj.equals("55555555555555") ||
            cnpj.equals("66666666666666") || cnpj.equals("77777777777777") ||
            cnpj.equals("88888888888888") || cnpj.equals("99999999999999") ||
            cnpj.length() != 14
        ) return false;

        char dig13, dig14;
        int sm, r, num, peso;

        try
        {
            sm = 0;
            peso = 2;
            for (int i = 11; i >= 0; i--)
            {
                num = (int) (cnpj.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if (r == 0 || r == 1)
                dig13 = '0';

            else
                dig13 = (char) ((11 - r) + 48);

            sm = 0;
            peso = 2;

            for (int i = 12; i >= 0; i--)
            {
                num = (int) cnpj.charAt(i) - 48;
                sm = sm + num * peso;
                peso += 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if (r == 0 || r == 1)
                dig14 = '0';

            else
                dig14 = (char) ((11 - r) + 48);

            return dig13 == cnpj.charAt(12) && dig14 == cnpj.charAt(13);
        }
        catch (InputMismatchException e)
        {
            return false;
        }
    }


    public static String generatePassword(int length)
    {
        String chars = "ABCÇDEFGHIJKLMNOPQRSTUVWXYZabcçdefghijklmnopqrstuvwxyz123456789!@#$%*()";

        Random random = new Random();
        char[] password = new char[length];

        for (int i = 0; i < length; i++)
            password[i] = chars.charAt(random.nextInt(chars.length()));

        return String.valueOf(password);
    }
}
