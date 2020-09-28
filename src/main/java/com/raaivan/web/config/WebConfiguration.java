package com.raaivan.web.config;

import com.raaivan.modules.corenetwork.CNDAO;
import com.raaivan.modules.corenetwork.util.CNUtilities;
import com.raaivan.modules.dataexchange.DEDAO;
import com.raaivan.modules.dataexchange.DEParsers;
import com.raaivan.modules.forms.FGDAO;
import com.raaivan.modules.forms.FGParsers;
import com.raaivan.modules.forms.util.FGUtilities;
import com.raaivan.util.dbutil.RVConnection;
import com.raaivan.modules.documents.DCTDAO;
import com.raaivan.modules.documents.DCTParsers;
import com.raaivan.modules.privacy.PrivacyDAO;
import com.raaivan.modules.privacy.PrivacyParsers;
import com.raaivan.modules.rv.RVDAO;
import com.raaivan.modules.rv.RVParsers;
import com.raaivan.modules.users.beans.User;
import com.raaivan.util.PublicMethods;
import com.raaivan.util.rvsettings.*;
import com.raaivan.modules.social.SHDAO;
import com.raaivan.modules.social.SHParsers;
import com.raaivan.modules.users.UsersDAO;
import com.raaivan.modules.users.UsersParsers;
import com.raaivan.modules.wiki.WKDAO;
import com.raaivan.modules.wiki.WKParsers;
import com.raaivan.modules.documents.util.DocumentUtilities;
import com.raaivan.util.rvsettings.RaaiVanSettings;
import com.raaivan.modules.users.util.UserUtilities;
import com.raaivan.util.RVAuthentication;
import com.raaivan.util.RVRequest;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@Component
public class WebConfiguration extends WebMvcConfigurationSupport {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3000);
    }

    @Bean
    public PathMatcher pathMatcher(){
        return new CaseInsensitivePathMatcher();
    }

    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping handlerMapping = new RequestMappingHandlerMapping();
        handlerMapping.setOrder(0);
        handlerMapping.setInterceptors(getInterceptors());
        handlerMapping.setPathMatcher(pathMatcher());
        return handlerMapping;
    }

    @Bean
    @ApplicationScope
    public RVConnection rvConnection(){ return new RVConnection(); }

    /* Modules */

    @Bean
    @ApplicationScope
    public RVParsers rvParsers() { return new RVParsers(); }

    @Bean
    @ApplicationScope
    public RVDAO rvdao(){ return new RVDAO(); }

    @Bean
    @ApplicationScope
    public CNUtilities cnUtilities() { return new CNUtilities(); }

    @Bean
    @ApplicationScope
    public CNDAO cnDAO(){ return new CNDAO(); }

    @Bean
    @ApplicationScope
    public UserUtilities userUtilities() { return new UserUtilities(); }

    @Bean
    @ApplicationScope
    public UsersParsers usersParsers(){ return new UsersParsers(); }

    @Bean
    @ApplicationScope
    public UsersDAO usersDAO(){ return new UsersDAO(); }

    @Bean
    @ApplicationScope
    public DocumentUtilities documentUtilities() { return new DocumentUtilities(); }

    @Bean
    @ApplicationScope
    public DCTParsers dctParsers(){ return new DCTParsers(); }

    @Bean
    @ApplicationScope
    public DCTDAO dctDAO(){ return new DCTDAO(); }

    @Bean
    @ApplicationScope
    public SHParsers shParsers(){ return new SHParsers(); }

    @Bean
    @ApplicationScope
    public SHDAO shDAO(){ return new SHDAO(); }

    @Bean
    @ApplicationScope
    public DEParsers deParsers(){ return new DEParsers(); }

    @Bean
    @ApplicationScope
    public DEDAO deDAO(){ return new DEDAO(); }

    @Bean
    @ApplicationScope
    public WKParsers wkParsers(){ return new WKParsers(); }

    @Bean
    @ApplicationScope
    public WKDAO wkDAO(){ return new WKDAO(); }

    @Bean
    @ApplicationScope
    public PrivacyParsers privacyParsers(){ return new PrivacyParsers(); }

    @Bean
    @ApplicationScope
    public PrivacyDAO privacyDAO(){ return new PrivacyDAO(); }

    /* end of Modules */

    /* RaaiVan Settings */

    @Bean
    @ApplicationScope
    public RVSettingsUtil rvSettingsUtil() { return new RVSettingsUtil(); }

    @Bean
    @ApplicationScope
    public RVSettingsTenants rvSettingsTenants() { return new RVSettingsTenants(); }

    @Bean
    @ApplicationScope
    public RVSettingsOrganization rvSettingsOrganization() { return new RVSettingsOrganization(); }

    @Bean
    @ApplicationScope
    public RVSettingsUsers rvSettingsUsers() { return new RVSettingsUsers(); }

    @Bean
    @ApplicationScope
    public RVSettingsPasswordPolicy rvSettingsPasswordPolicy() { return new RVSettingsPasswordPolicy(); }

    @Bean
    @ApplicationScope
    public RVSettingsSSO rvSettingsSSO() { return new RVSettingsSSO(); }

    @Bean
    @ApplicationScope
    public RVSettingsHomePagePriorities rvSettingsHomePagePriorities() { return new RVSettingsHomePagePriorities(); }

    @Bean
    @ApplicationScope
    public RVSettingsSensitivePages rvSettingsSensitivePages() { return new RVSettingsSensitivePages(); }

    @Bean
    @ApplicationScope
    public RVSettingsCoreNetwork rvSettingsCoreNetwork() { return new RVSettingsCoreNetwork(); }

    @Bean
    @ApplicationScope
    public RVSettingsKnowledge rvSettingsKnowledge() { return new RVSettingsKnowledge(); }

    @Bean
    @ApplicationScope
    public RVSettingsNotifications rvSettingsNotifications() { return new RVSettingsNotifications(); }

    @Bean
    @ApplicationScope
    public RVSettingsFileContentExtraction rvSettingsFileContentExtraction() { return new RVSettingsFileContentExtraction(); }

    @Bean
    @ApplicationScope
    public RVSettingsIndexUpdate rvSettingsIndexUpdate() { return new RVSettingsIndexUpdate(); }

    @Bean
    @ApplicationScope
    public RVSettingsEmailQueue rvSettingsEmailQueue() { return new RVSettingsEmailQueue(); }

    @Bean
    @ApplicationScope
    public RVSettingsSystemEmail rvSettingsSystemEmail() { return new RVSettingsSystemEmail(); }

    @Bean
    @ApplicationScope
    public RVSettingsRecommender rvSettingsRecommender() { return new RVSettingsRecommender(); }

    @Bean
    @ApplicationScope
    public RVSettingsJobs rvSettingsJobs() { return new RVSettingsJobs(); }

    @Bean
    @ApplicationScope
    public RaaiVanSettings raaivanSettings(){
        return new RaaiVanSettings();
    }

    /* end of RaaiVan Settings */

    @Bean
    @ApplicationScope
    public PublicMethods rvPublicMethods(){ return new PublicMethods(); }

    @Bean
    @RequestScope
    public RVRequest rvRequest(){
        return new RVRequest();
    }

    @Bean
    @ApplicationScope
    public RVAuthentication rvAuthentication(){ return new RVAuthentication(); }

    @Bean
    @Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
    public User user(){ return new User(); }

    @Bean
    @ApplicationScope
    public FGUtilities fgUtilities() { return new FGUtilities(); }

    @Bean
    @ApplicationScope
    public FGParsers fgParsers(){ return new FGParsers(); }

    @Bean
    @ApplicationScope
    public FGDAO fgDAO(){ return new FGDAO(); }
}
