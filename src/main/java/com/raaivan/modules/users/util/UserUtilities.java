package com.raaivan.modules.users.util;

import com.raaivan.util.PublicMethods;
import com.raaivan.util.rvsettings.RaaiVanSettings;
import com.raaivan.modules.rv.enums.Messages;
import com.raaivan.util.Base64;
import com.raaivan.util.RVJSON;
import org.apache.commons.codec.Charsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@ApplicationScope
public class UserUtilities {
    private PublicMethods publicMethods;
    private RaaiVanSettings raaivanSettings;

    @Autowired
    public void _setDependencies(PublicMethods publicMethods, RaaiVanSettings raaivanSettings) {
        if (this.publicMethods == null) this.publicMethods = publicMethods;
        if (this.raaivanSettings == null) this.raaivanSettings = raaivanSettings;
    }

    public String generatePasswordSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return new String(Base64.encode(salt));
    }

    public String encodePassword(String password, String salt){
        byte[] bytes = password.getBytes(Charsets.UTF_16LE);
        byte[] src = org.apache.tomcat.util.codec.binary.Base64.decodeBase64(salt.getBytes());
        byte[] dst = new byte[src.length + bytes.length];
        System.arraycopy(src, 0, dst, 0, src.length);
        System.arraycopy(bytes, 0, dst, src.length, bytes.length);
        return publicMethods.sha1(dst);
    }

    public String generatePassword(int length)
    {
        return publicMethods.getRandomString(length);
    }

    public String generatePassword(){
        return generatePassword(12);
    }

    public boolean checkPasswordPolicy(UUID applicationId,
                                              String password, String oldPassword, RVJSON error)
    {
        boolean meetsLength = password.length() >= Math.max(1, raaivanSettings.getUsers().getPasswordPolicy().MinLength(applicationId));
        boolean meetsNewCharacters = oldPassword == null || publicMethods.diff(password, oldPassword).length() >=
                raaivanSettings.getUsers().getPasswordPolicy().NewCharacters(applicationId);
        boolean meetsUpperLower = !raaivanSettings.getUsers().getPasswordPolicy().UpperLower(applicationId) || (
                password.matches("[A-Z]") && password.matches("[a-z]"));
        boolean meetsNonAlphabetic = !raaivanSettings.getUsers().getPasswordPolicy().NonAlphabetic(applicationId) ||
                !password.matches("^[a-zA-Z]+$");
        boolean meetsNumber = !raaivanSettings.getUsers().getPasswordPolicy().Number(applicationId) ||
                password.matches("[0-9]");
        boolean meetsNonAlphaNumeric = !raaivanSettings.getUsers().getPasswordPolicy().NonAlphaNumeric(applicationId) ||
                !password.matches("^[a-zA-Z0-9]+$");

        List<String> notMeet = new ArrayList<>();

        if (!meetsLength) notMeet.add("MinLength_" + raaivanSettings.getUsers().getPasswordPolicy().MinLength(applicationId));
        if (!meetsNewCharacters) notMeet.add("NewCharacters_" + raaivanSettings.getUsers().getPasswordPolicy().NewCharacters(applicationId));
        if (!meetsUpperLower) notMeet.add("UpperLower");
        if (!meetsNonAlphabetic) notMeet.add("NonAlphabetic");
        if (!meetsNumber) notMeet.add("Number");
        if (!meetsNonAlphaNumeric) notMeet.add("NonAlphaNumeric");

        if (notMeet.size() > 0)
        {
            error.add("ErrorText",  Messages.PasswordPolicyDidntMeet.toString()).add("NotMeetItems", notMeet);
            return false;
        }

        return true;
    }
}
