package com.raaivan.modules.users.beans;

import com.raaivan.util.PublicMethods;
import com.raaivan.util.rvsettings.RaaiVanSettings;
import com.raaivan.modules.rv.beans.EmailTemplates;
import com.raaivan.modules.rv.enums.EmailTemplateType;
import com.raaivan.modules.rv.enums.Messages;
import com.raaivan.modules.users.util.UserUtilities;
import com.raaivan.util.Base64;
import com.raaivan.util.RVJSON;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class VerificationCode {
    private PublicMethods publicMethods;
    private EmailTemplates emailTemplates;
    private RaaiVanSettings raaivanSettings;

    @Autowired
    public void _setDependencies(PublicMethods publicMethods, RaaiVanSettings raaivanSettings, EmailTemplates emailTemplates){
        if(this.publicMethods == null) this.publicMethods = publicMethods;
        if(this.emailTemplates == null) this.emailTemplates = emailTemplates;
        if(this.emailTemplates == null) this.raaivanSettings = raaivanSettings;
    }

    private Integer TimeOut;
    private static int TotalTimeOutCoefficient = 4;

    protected UUID ApplicationID;
    private String Token;
    protected Integer Code;
    private DateTime ExpirationDate;
    private String EmailAddress;
    private String PhoneNumber;
    private String Media;
    private Integer TTL;

    protected static Map<String, VerificationCode> Tokens = new ConcurrentHashMap<>();

    @Autowired
    public VerificationCode(UUID applicationId, String emailAddress, String phoneNumber)
    {
        ApplicationID = applicationId;
        resetCode();
        EmailAddress = emailAddress;
        PhoneNumber = phoneNumber;
        TTL = 3;
        ExpirationDate = null;
        Code = null;
    }

    @PostConstruct
    public void _postConstruct(){
        TimeOut = raaivanSettings.getUsers().TwoStepAuthenticationTimeout(ApplicationID);
        Token = publicMethods.getRandomString(20);
    }

    private boolean updateTtl()
    {
        if (TTL <= 0) return false;
        else {
            --TTL;
            return true;
        }
    }

    private void resetCode() { Code = publicMethods.getRandomNumber(5); }

    private void resetExpirationDate()
    {
        ExpirationDate = DateTime.now().plusSeconds(TimeOut + 10);
    }

    public String getToken(){ return Token; }

    public Integer getCode() { return Code; }

    private boolean use()
    {
        if (ExpirationDate != null) return false;

        Tokens.put(Token, this);

        resetExpirationDate();

        publicMethods.setTimeout(() -> {
            Tokens.remove(Token);
        }, (TimeOut + 10) * 1000 * TotalTimeOutCoefficient);

        return true;
    }

    public boolean sendCode()
    {
        boolean result = sendEmail() || sendSms();
        if (result) use();
        return result;
    }

    private boolean resend()
    {
        resetExpirationDate();
        resetCode();

        return Media.equals("Email") ? sendEmail() : Media.equals("SMS") && sendSms();
    }

    protected void emailBodySubject(StringBuilder body, StringBuilder subject)
    {
        if(ApplicationID == null) return;

        Map<String, String> dic = new HashMap<>();
        dic.put("Code", Code.toString());

        body.setLength(0);
        subject.setLength(0);

        body.append(emailTemplates.getEmailTemplate(ApplicationID, EmailTemplateType.ConfirmationCode, dic));
        subject.append(emailTemplates.getEmailSubjectTemplate(ApplicationID, EmailTemplateType.ConfirmationCode, dic));
    }

    protected String smsBody()
    {
        return "کد تایید شما در رای ون " + Code.toString() + " است.";
    }

    private boolean sendEmail()
    {
        if (StringUtils.isBlank(EmailAddress) || ApplicationID == null) return false;

        StringBuilder emailBody = new StringBuilder("");
        StringBuilder emailSubject = new StringBuilder("");

        emailBodySubject(emailBody, emailSubject);

        boolean result = publicMethods.sendEmail(ApplicationID, EmailAddress,
                emailSubject.toString(), emailBody.toString());

        if (result) Media = "Email";

        return result;
    }

    private boolean sendSms()
    {
        if (StringUtils.isBlank(PhoneNumber)) return false;

        boolean result = publicMethods.sendSms(PhoneNumber, smsBody());
        if (result) Media = "SMS";

        return result;
    }

    public RVJSON toJson(UUID applicationId)
    {
        return (new RVJSON())
                .add("Token", Token)
                .add("Media", Media)
                .add("EmailAddress", Base64.encode(EmailAddress))
                .add("PhoneNumber", Base64.encode(PhoneNumber))
                .add("Timeout", TimeOut.toString())
                .add("TotalTimeout", TimeOut * (TotalTimeOutCoefficient - 1));
    }

    public static boolean resendCode(String token)
    {
        return !StringUtils.isBlank(token) && Tokens.containsKey(token) && Tokens.get(token).resend();
    }

    public static VerificationCode validate(String token, long code, MutableBoolean disposed)
    {
        disposed.setFalse();
        if (!Tokens.containsKey(token)) return null;

        VerificationCode t = Tokens.get(token);

        if (!t.updateTtl())
        {
            disposed.setTrue();
            return null;
        }

        if (t.Code == code && t.ExpirationDate != null &&
                t.ExpirationDate.compareTo(DateTime.now()) > 0) return t;
        else return null;
    }

    public static boolean processRequest(UUID applicationId, String emailAddress, String phoneNumber,
                                       String token, Long code, RVJSON response) {
        MutableBoolean disposed = new MutableBoolean(false);

        if (StringUtils.isBlank(token) || code == null)
        {
            VerificationCode vc = new VerificationCode(applicationId, emailAddress, phoneNumber);

            if (vc.sendCode()) response.add("VerificationCode", vc.toJson(applicationId));
            else response.add("ErrorText", Messages.SendingVerificationCodeFailed.toString());

            return false;
        }
        else if (VerificationCode.validate(token, code, disposed) == null)
        {
            response.add("ErrorText", Messages.VerificationCodeDidNotMatch.toString())
                    .add("CodeDisposed", disposed.getValue());
            return false;
        }

        return true;
    }
}
