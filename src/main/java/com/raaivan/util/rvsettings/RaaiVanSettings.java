package com.raaivan.util.rvsettings;

import com.raaivan.modules.rv.beans.*;
import com.raaivan.util.PublicConsts;
import com.raaivan.util.PublicMethods;
import com.raaivan.util.RVJSON;
import com.raaivan.util.RaaiVanConfig;
import com.raaivan.web.config.RVCustomProperties;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.io.File;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ApplicationScope
public class RaaiVanSettings {
    private PublicMethods publicMethods;
    private RVSettingsUtil util;
    private RVCustomProperties rvConfig;
    private RVSettingsTenants tenants;

    private RVSettingsOrganization Organization;
    private RVSettingsUsers Users;
    private RVSettingsHomePagePriorities HomePagePriorities;
    private RVSettingsSSO SSO;
    private RVSettingsSensitivePages ReautheticationForSensitivePages;
    private RVSettingsCoreNetwork CoreNetwork;
    private RVSettingsKnowledge Knowledge;
    private RVSettingsNotifications Notifications;
    private RVSettingsFileContentExtraction FileContentExtraction;
    private RVSettingsIndexUpdate IndexUpdate;
    private RVSettingsEmailQueue EmailQueue;
    private RVSettingsSystemEmail SystemEmail;
    private RVSettingsRecommender Recommender;
    private RVSettingsJobs Jobs;

    @Autowired
    public void _setDependencies(PublicMethods publicMethods, RVSettingsUtil util,
                                 RVCustomProperties rvConfig, RVSettingsTenants tenants,
                                 RVSettingsOrganization organization, RVSettingsUsers users,
                                 RVSettingsHomePagePriorities homePagePriorities, RVSettingsSSO sso,
                                 RVSettingsSensitivePages reautheticationForSensitivePages, RVSettingsCoreNetwork coreNetwork,
                                 RVSettingsKnowledge knowledge, RVSettingsNotifications notifications,
                                 RVSettingsFileContentExtraction fileContentExtraction, RVSettingsIndexUpdate indexUpdate,
                                 RVSettingsEmailQueue emailQueue, RVSettingsSystemEmail systemEmail,
                                 RVSettingsRecommender recommender, RVSettingsJobs jobs) {
        if (this.publicMethods == null) this.publicMethods = publicMethods;
        if (this.util == null) this.util = util;
        if (this.rvConfig == null) this.rvConfig = rvConfig;
        if (this.tenants == null) this.tenants = tenants;

        if(this.Organization == null) this.Organization = organization;
        if(this.Users == null) this.Users = users;
        if(this.HomePagePriorities == null) this.HomePagePriorities = homePagePriorities;
        if(this.SSO == null) this.SSO = sso;
        if(this.ReautheticationForSensitivePages == null) this.ReautheticationForSensitivePages = reautheticationForSensitivePages;
        if(this.CoreNetwork == null) this.CoreNetwork = coreNetwork;
        if(this.Knowledge == null) this.Knowledge = knowledge;
        if(this.Notifications == null) this.Notifications = notifications;
        if(this.FileContentExtraction == null) this.FileContentExtraction = fileContentExtraction;
        if(this.IndexUpdate == null) this.IndexUpdate = indexUpdate;
        if(this.EmailQueue == null) this.EmailQueue = emailQueue;
        if(this.SystemEmail == null) this.SystemEmail = systemEmail;
        if(this.Recommender == null) this.Recommender = recommender;
        if(this.Jobs == null) this.Jobs = jobs;
    }

    public RVSettingsOrganization getOrganization() {
        return Organization;
    }

    public RVSettingsUsers getUsers() {
        return Users;
    }

    public RVSettingsHomePagePriorities getHomePagePriorities() {
        return HomePagePriorities;
    }

    public RVSettingsSSO getSSO() {
        return SSO;
    }

    public RVSettingsSensitivePages getReautheticationForSensitivePages() {
        return ReautheticationForSensitivePages;
    }

    public RVSettingsCoreNetwork getCoreNetwork() {
        return CoreNetwork;
    }

    public RVSettingsKnowledge getKnowledge() {
        return Knowledge;
    }

    public RVSettingsNotifications getNotifications() {
        return Notifications;
    }

    public RVSettingsFileContentExtraction getFileContentExtraction() {
        return FileContentExtraction;
    }

    public RVSettingsIndexUpdate getIndexUpdate() {
        return IndexUpdate;
    }

    public RVSettingsEmailQueue getEmailQueue() {
        return EmailQueue;
    }

    public RVSettingsSystemEmail getSystemEmail() {
        return SystemEmail;
    }

    public RVSettingsRecommender getRecommender() {
        return Recommender;
    }

    public RVSettingsJobs getJobs() {
        return Jobs;
    }

    public String RaaiVanURL(UUID applicationId) {
        Tenant tenant = tenants.get(applicationId);
        String val = util.getValue(applicationId, RVSettingsItem.SystemURL);

        return tenant == null || StringUtils.isBlank(tenant.getDomain()) ?
                (StringUtils.isBlank(val) ? "http://127.0.0.1" : val) :
                tenant.getProtocol() + "://" + tenant.getDomain();
    }

    public boolean IgnoreReturnURLOnLogin(UUID applicationId) {
        return util.getValue(applicationId, RVSettingsItem.IgnoreReturnURLOnLogin).toLowerCase().equals("true");
    }

    public boolean UserSignUp(UUID applicationId) {
        return util.getValue(applicationId, RVSettingsItem.UserSignUp).toLowerCase().equals("true");
    }

    public boolean SignUpViaInvitation(UUID applicationId) {
        return util.getValue(applicationId, RVSettingsItem.SignUpViaInvitation).toLowerCase().equals("true");
    }

    public String LoginPageInfo(UUID applicationId) {
        return util.getValue(applicationId, RVSettingsItem.LoginPageInfo);
    }

    public String LoginPageModel(UUID applicationId) {
        return util.getValue(applicationId, RVSettingsItem.LoginPageModel);
    }

    public String DefaultPrivacy(UUID applicationId) {
        return util.getValue(applicationId, RVSettingsItem.DefaultPrivacy, "Public");
    }

    public String DefaultPrivacyForReadableFiles(UUID applicationId) {
        return util.getValue(applicationId, RVSettingsItem.DefaultPrivacyForReadableFiles, "Public");
    }

    public boolean DailyDatabaseBackup() {
        return util.getValue(UUID.randomUUID(), RVSettingsItem.DailyDatabaseBackup, "true").toLowerCase().equals("true");
    }

    public boolean RemoveOldDatabaseBackups() {
        return util.getValue(UUID.randomUUID(), RVSettingsItem.RemoveOldDatabaseBackups).toLowerCase().equals("true");
    }

    public boolean AllowNotAuthenticatedUsers(UUID applicationId) {
        return util.getValue(applicationId, RVSettingsItem.AllowNotAuthenticatedUsers, "true").toLowerCase().equals("true");
    }

    public boolean EnableThemes(UUID applicationId) {
        return util.getValue(applicationId, RVSettingsItem.EnableThemes, "true").toLowerCase().equals("true");
    }

    public String DefaultTheme(UUID applicationId) {
        return util.getValue(applicationId, RVSettingsItem.DefaultTheme, "Default");
    }

    private static JSONArray _Themes;
    public JSONArray Themes() {
        try {
            if (_Themes == null) {
                _Themes = new JSONArray();

                File[] files = (new File(publicMethods.mapPath(PublicConsts.Themes))).listFiles();
                if(files == null) files = new File[0];

                for (File f : files) {
                    String fileName = f.getName();
                    //fileName = f.substring(f.lastIndexOf('\\') + 1);
                    fileName = fileName.substring(0, fileName.lastIndexOf('.'));

                    String txt = FileUtils.readFileToString(f, Charsets.UTF_8);
                    txt = txt.substring(0, txt.indexOf('}') + 1);
                    txt = txt.substring(txt.indexOf("{"))
                            .replace("\n", "").replace("\r", "")
                            .replace("\t", "").replace(" ", "");

                    _Themes.put((new RVJSON()).add("Name", fileName).add("Codes", txt));
                }
            }

            return _Themes;
        }catch (Exception ex){
            return new JSONArray();
        }
    }

    public String DefaultLang(UUID applicationId) {
        return util.getValue(applicationId, RVSettingsItem.DefaultLang, "fa").toLowerCase();
    }

    public String DefaultDirection(UUID applicationId) {
        return DefaultLang(applicationId).equals("fa") ? "RTL" : "LTR";
    }

    public boolean ShowSystemVersion(UUID applicationId) {
        return util.getValue(applicationId, RVSettingsItem.ShowSystemVersion, "true").toLowerCase().equals("true");
    }

    public String SystemName(UUID applicationId) {
        return util.getValue(applicationId, RVSettingsItem.SystemName).toLowerCase();
    }

    public String SystemTitle(UUID applicationId) {
        return util.getValue(applicationId, RVSettingsItem.SystemTitle).toLowerCase();
    }

    public boolean ServiceUnavailable() {
        return util.getValue(UUID.randomUUID(), RVSettingsItem.ServiceUnavailable).toLowerCase().equals("true");
    }

    private static List<SimpleEntry<String, String>> _DefaultDomains;
    private static Map<UUID, List<SimpleEntry<String, String>>> _Domains;

    public List<SimpleEntry<String, String>> Domains(UUID applicationId) {
        if (_Domains != null && _Domains.containsKey(applicationId))
            return _Domains.get(applicationId).size() > 0 || tenants.get().size() > 1 ?
                    _Domains.get(applicationId) : _DefaultDomains;

        if (_DefaultDomains == null) _DefaultDomains = new ArrayList<>();
        if (_Domains == null) _Domains = new ConcurrentHashMap<>();

        if (!_Domains.containsKey(applicationId)) _Domains.put(applicationId, new ArrayList<>());

        for(SimpleEntry<String, String> entry : rvConfig.getKeyValues(RVSettingsItem.DomainName)){
            String[] vals = entry.getValue().split("\\|");

            String value = vals[0];
            String text = vals.length > 1 ? vals[1] : value;

            if (entry.getKey().lastIndexOf("_") < 0)
                _DefaultDomains.add(new SimpleEntry<String, String>(value, text));
            else {
                String tenantName = entry.getKey().substring(entry.getKey().lastIndexOf("_") + 1);

                Tenant tnt = tenants.get().stream()
                        .filter(u -> u.getName().toLowerCase().equals(tenantName.toLowerCase())).findFirst().orElse(null);

                if (tnt != null && tnt.getID() != null) {
                    if (!_Domains.containsKey(tnt.getID())) _Domains.put(tnt.getID(), new ArrayList<>());

                    _Domains.get(tnt.getID()).add(new SimpleEntry<String, String>(value, text));
                }
            }
        }

        return _Domains.containsKey(applicationId) &&
                (_Domains.get(applicationId).size() > 1 || tenants.get().size() > 1) ?
                _Domains.get(applicationId) : _DefaultDomains;
    }

    public List<SimpleEntry<String, String>> ActiveDirectoryDomains() {
        List<SimpleEntry<String, String>> retList = new ArrayList<>();

        for(String domain : rvConfig.getValues(RVSettingsItem.DomainName)){
            String[] vals = domain.split("\\|");

            String value = vals[0];
            String text = vals.length > 1 ? vals[1] : value;

            SimpleEntry<String, String> item = new SimpleEntry<String, String>(text, value);
            retList.add(item);
        }

        return retList;
    }

    public String LoginMessage(UUID applicationId) {
        return util.getValue(applicationId, RVSettingsItem.LoginMessage).toLowerCase();
    }

    public int InformLastLogins(UUID applicationId) {
        return util.getInt(applicationId, RVSettingsItem.InformLastLogins, 0, 0);
    }

    public int AuthCookieLifeTimeInSeconds(UUID applicationId) {
        int def = 24 * 60 * 60;
        return util.getDurationInSeconds(applicationId, RVSettingsItem.AuthCookieLifeTime, def, def);
    }

    public int AuthCookieLifeTimeForAdminInSeconds(UUID applicationId) {
        int def = 24 * 60 * 60;
        return util.getDurationInSeconds(applicationId, RVSettingsItem.AuthCookieLifeTimeForAdmin, def, def);
    }

    public int MaxAllowedInactiveTimeInSeconds(UUID applicationId) {
        int def = 24 * 60 * 60;
        return util.getDurationInSeconds(applicationId, RVSettingsItem.MaxAllowedInactiveTime, def, def);
    }

    public int AllowedConsecutiveFailedLoginAttempts(UUID applicationId) {
        return util.getInt(applicationId, RVSettingsItem.AllowedConsecutiveFailedLoginAttempts, 3, 0);
    }

    public int LoginLockoutDurationInSeconds(UUID applicationId) {
        return util.getDurationInSeconds(applicationId, RVSettingsItem.LoginLockoutDuration, 3 * 60, 0);
    }

    public int ReplayAttackQueueLength() {
        return util.getInt(UUID.randomUUID(), RVSettingsItem.ReplayAttackQueueLength, 10000, 100);
    }

    public boolean UseSecureAuthCookie(UUID applicationId) {
        return util.getValue(applicationId, RVSettingsItem.UseSecureAuthCookie).toLowerCase().equals("true");
    }

    public boolean PreventConcurrentSessions(UUID applicationId) {
        return util.getValue(applicationId, RVSettingsItem.PreventConcurrentSessions).toLowerCase().equals("true");
    }

    public String GATrackingID(UUID applicationId) //TrackingID for Google Analytics Account
    {
        return util.getValue(applicationId, RVSettingsItem.GATrackingID).toLowerCase();
    }

    public boolean RealTime() {
        return RaaiVanConfig.Modules.RealTime &&
                !util.getValue(UUID.randomUUID(), RVSettingsItem.RealTime).toLowerCase().equals("false");
    }

    public boolean Explorer(UUID applicationId) {
        return RaaiVanConfig.Modules.Explorer &&
                !util.getValue(applicationId, RVSettingsItem.Explorer).toLowerCase().equals("false");
    }
}