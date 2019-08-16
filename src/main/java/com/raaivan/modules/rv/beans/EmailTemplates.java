package com.raaivan.modules.rv.beans;

import com.raaivan.modules.documents.util.DocumentUtilities;
import com.raaivan.modules.documents.enums.FolderNames;
import com.raaivan.modules.rv.enums.EmailTemplateType;
import com.raaivan.util.Expressions;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class EmailTemplates
{
    private DocumentUtilities documentUtilities;

    @Autowired
    public void _setDependencies(DocumentUtilities documentUtilities) {
        if (this.documentUtilities == null) this.documentUtilities = documentUtilities;
    }

    public boolean Initialized = false;

    private static Map<String, String> Templates;
    private static Map<String, String> TemplateSubjects;
    private static Map<String, String> OtherTemplates = new HashMap<>();

    public void Initialize(UUID applicationId)
    {
        if (Initialized) return;
        else Initialized = true;

        Templates = new HashMap<>();
        TemplateSubjects = new HashMap<>();

        String masterTmp = Templates.put(EmailTemplateType.Master.toString(),
                _getEmailTemplate(applicationId, EmailTemplateType.Master.toString()));

        Map<String, String> dic = new HashMap<>();

        for(EmailTemplateType tp : EmailTemplateType.values()){
            if (tp != EmailTemplateType.Master)
            {
                Templates.put(tp.toString(), injectIntoMaster(_getEmailTemplate(applicationId, tp.toString())));
                TemplateSubjects.put(tp.toString(), _getEmailTemplate(applicationId, tp.toString() + "Subject"));
            }
        }
    }

    private String _getEmailTemplate(UUID applicationId, String templateType)
    {
        try {
            String fileAddress = documentUtilities.mapPath(applicationId, FolderNames.EmailTemplates) +
                    "\\" + templateType + ".txt";
            File file = new File(fileAddress);
            return !file.exists() ? "" : FileUtils.readFileToString(file, Charsets.UTF_8);
        }catch (Exception ex){
            return "";
        }
    }

    public String injectIntoMaster(String template)
    {
        if (!Initialized || StringUtils.isBlank(Templates.get(EmailTemplateType.Master.toString()))) return template;

        Map<String, String> dic = new HashMap<>();
        dic.put("Content", template);

        return Expressions.replace(Templates.get(EmailTemplateType.Master.toString()), dic, Expressions.Patterns.AutoTag);
    }

    public String getEmailTemplate(UUID applicationId,
                                            EmailTemplateType templateType, Map<String, String> dic)
    {
        Initialize(applicationId);
        if (dic == null) dic = new HashMap<>();
        return Expressions.replace(Templates.get(templateType.toString()), dic, Expressions.Patterns.AutoTag);
    }

    public String getEmailSubjectTemplate(UUID applicationId,
                                                    EmailTemplateType templateType, Map<String, String> dic)
    {
        Initialize(applicationId);
        if (dic == null) dic = new HashMap<>();
        return Expressions.replace(TemplateSubjects.get(templateType.toString()), dic, Expressions.Patterns.AutoTag);
    }

    public String getEmailTemplate(UUID applicationId,
                                            String templateName, boolean intoMaster, Map<String, String> dic)
    {
        Initialize(applicationId);

        templateName = templateName.toLowerCase();

        String template = "";

        if(OtherTemplates.containsKey(templateName))
            template = OtherTemplates.get(templateName);
        else {
            template = _getEmailTemplate(applicationId, templateName);
            OtherTemplates.put(templateName, template);
        }

        if (intoMaster) template = injectIntoMaster(template);

        return Expressions.replace(template, dic, Expressions.Patterns.AutoTag);
    }
}
