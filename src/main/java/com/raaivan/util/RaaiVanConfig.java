package com.raaivan.util;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RaaiVanConfig {
    public static class Modules {
        public static boolean WorkFlow = true;
        public static boolean FormGenerator = true;
        public static boolean Notifications = true;
        public static boolean SMSEMailNotifier = true;
        public static boolean SocialNetwork = true;
        public static boolean KnowledgeAdmin = true;
        public static boolean Knowledge = true;
        public static boolean Documents = true;
        public static boolean PDFViewer = true;
        public static boolean QA = true;
        public static boolean QAAdmin = true;
        public static boolean Events = true;
        public static boolean Messaging = true;
        public static boolean Chat = true;
        public static boolean Resume = true;
        public static boolean VisualMap = true;
        public static boolean RealTime = true;
        public static boolean Explorer = true;
        public static boolean RestAPI = true;
        public static boolean Recommender = true;
    }

    public static class GlobalSettings {
        public static Integer MaxAllowedActiveUsersCount = null;
        public static DateTime SystemExpirationDate = null;
        public static List<UUID> TenantIDs = new ArrayList<>();

        private static int _MaxTenantsCount = 1;

        public static int MaxTenantsCount() {
            return TenantIDs != null && TenantIDs.size() > 0 ? TenantIDs.size() : _MaxTenantsCount;
        }
    }
}
