package club.smartsheep.panelcraftcore.Common.Tokens;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DynamicPasswordGenerator {
    static private class PasswordCharacterList {
        enum CharsetList {
            lower,
            upper,
            special,
            number
        }

        public static final String characters = "abcdefghijklmnopqrstuvwxyz";
        public static final String special = "~!@#$%^&*()_+/-=[]{};:'<>?.";
        public static final String numbers = "0123456789";
    }

    public enum PasswordStrength {
        strong(20),
        normal(10),
        weak(8);

        final int length;
        PasswordStrength(int length) {
            this.length = length;
        }
    }

    public String GenerateRandomPassword(PasswordStrength strength) {
        List<Character> password = new ArrayList<>();

        for (int i = 0; i < strength.length; i++) {
            SecureRandom random = new SecureRandom();
            int number = random.nextInt(4);
            switch (number) {
                case 0: password.add(SummonRandomCharacter(PasswordCharacterList.CharsetList.lower)); break;
                case 1: password.add(SummonRandomCharacter(PasswordCharacterList.CharsetList.upper)); break;
                case 2: password.add(SummonRandomCharacter(PasswordCharacterList.CharsetList.special)); break;
                case 3: password.add(SummonRandomCharacter(PasswordCharacterList.CharsetList.number)); break;
            }
        }

        Collections.shuffle(password);
        return password.stream().map(String::valueOf).collect(Collectors.joining(""));
    }

    private char GetRandomCharacter(String string) {
        SecureRandom random = new SecureRandom();
        return string.charAt(random.nextInt(string.length()));
    }

    private char SummonRandomCharacter(PasswordCharacterList.CharsetList type) {
        switch (type) {
            case lower: return GetRandomCharacter(PasswordCharacterList.characters);
            case upper: return Character.toLowerCase(GetRandomCharacter(PasswordCharacterList.characters));
            case number: return GetRandomCharacter(PasswordCharacterList.numbers);
            case special: return GetRandomCharacter(PasswordCharacterList.special);
        }

        return ' ';
    }
}
