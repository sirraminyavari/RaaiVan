package com.raaivan.modules.users.beans;

import com.raaivan.modules.rv.beans.EmailTemplates;
import com.raaivan.modules.rv.enums.EmailTemplateType;
import com.raaivan.util.PublicMethods;
import com.raaivan.util.rvsettings.RaaiVanSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class TwoStepAuthenticationToken extends VerificationCode {
    private EmailTemplates emailTemplates;

    @Autowired
    public void _setDependencies(EmailTemplates emailTemplates){
        if(this.emailTemplates == null) this.emailTemplates = emailTemplates;
    }

    private UUID UserID;
    private Boolean WasNormalUserPassLogin;

    public UUID getUserID() {
        return UserID;
    }

    @Override
    protected void emailBodySubject(StringBuilder body, StringBuilder subject)
    {
        Map<String, String> dic = new HashMap<>();
        dic.put("Code", super.Code.toString());

        body.setLength(0);
        subject.setLength(0);

        body.append(emailTemplates.getEmailTemplate(super.ApplicationID, EmailTemplateType.TwoStepAuthenticationCode, dic));
        subject.append(emailTemplates.getEmailSubjectTemplate(super.ApplicationID, EmailTemplateType.TwoStepAuthenticationCode, dic));
    }

    @Override
    protected String smsBody()
    {
        return "کد احراز هویت شما در رای ون " + super.Code.toString() + " است.";
    }

    public TwoStepAuthenticationToken(UUID applicationId, UUID userId,
                                      boolean wasNormalUserPassLogin, String emailAddress, String phoneNumber)
    {
        super(applicationId, emailAddress, phoneNumber);

        UserID = userId;
        WasNormalUserPassLogin = wasNormalUserPassLogin;
    }

    public boolean getWasNormalUserPassLogin() {
        return WasNormalUserPassLogin != null && WasNormalUserPassLogin;
    }
}
