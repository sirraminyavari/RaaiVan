package com.raaivan.util;

import com.raaivan.util.rvsettings.RaaiVanSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

public class PublicConsts {
    private static RaaiVanSettings raaivanSettings;

    public static void _setDependencies(RaaiVanSettings raaivanSettings){
        if(PublicConsts.raaivanSettings == null) PublicConsts.raaivanSettings = raaivanSettings;
    }

    public static String LoginPage = "~/login";
    public static String HomePage = "~/home";
    public static String ProfilePage = "~/user";
    public static String ChangePasswordPage = "~/changepassword";
    public static String NoAccessPage = "~/accessdenied";
    public static String NodePage = "~/node";
    public static String PostPage = "~/posts";
    public static String RSSPage = "~/Ajax/RSS.aspx";
    public static String ServiceUnavailablePage = "~/Default.aspx";
    public static String SearchPage = "~/dosearch";
    public static String ReportsPage = "~/reportspanel";
    public static String FileDownload = "~/Ajax/FileDownload.ashx";
    public static String VisualGraphPage = "~/graph";
    public static String ExpolorerPage = "~/explorer";
    public static String QuestionsPage = "~/questions";
    public static String QuestionViewPage = "~/question";
    public static String UserSearchPage = "~/usersearch";
    public static String SystemSettingsPage = "~/systemsettings";
    public static String NetworkPage = "~/network";
    public static String MessagesPage = "~/messages";
    public static String DocumentsBrowserPage = "~/documentsbrowser";
    public static String NoPDFPage = "~/ReDesign/images/no_pdf_page.png";
    public static String Themes = "~/css/themes";

    public static String NotAuthenticatedResponse = "{\"NotAuthenticated\":" + "true" + "}";
    public static String NullTenantResponse = "{\"NoApplicationFound\":" + "true" + "}";

    public static String getCompleteUrl(UUID applicationId, String page)
    {
        return page.replace("~", raaivanSettings.RaaiVanURL(applicationId));
    }

    public static String getClientUrl(String page)
    {
        return page.replace("~", "../..");
    }
}
