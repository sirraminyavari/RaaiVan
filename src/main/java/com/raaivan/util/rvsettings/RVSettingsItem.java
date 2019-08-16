package com.raaivan.util.rvsettings;

public enum RVSettingsItem
{
    None,

    Tenant,
    DomainName,

    OrganizationName,
    OrganizationLogo,

    ServiceUnavailable,
    IgnoreReturnURLOnLogin,
    DefaultPrivacy,
    DefaultPrivacyForReadableFiles,
    EnableThemes,
    DefaultTheme,
    DefaultLang,
    ShowSystemVersion,

    LoginPageModel,
    LoginPageInfo,
    UserSignUp,
    SignUpViaInvitation,
    AllowNotAuthenticatedUsers,
    UserNamePattern,
    LoginMessage,

    InformLastLogins,
    AuthCookieLifeTime,
    AuthCookieLifeTimeForAdmin,
    MaxAllowedInactiveTime,
    AllowedConsecutiveFailedLoginAttempts,
    LoginLockoutDuration,
    ReplayAttackQueueLength,
    UseSecureAuthCookie,
    PreventConcurrentSessions,

    GATrackingID,

    RestrictPasswordsToActiveDirectory,
    EnableTwoStepAuthenticationViaEmail,
    EnableTwoStepAuthenticationViaSMS,
    TwoStepAuthenticationTimeoutInSeconds,
    ForceChangeFirstPassword,
    PasswordLifeTimeInDays,
    NotAvailablePreviousPasswordsCount,
    PasswordPolicyMinLength,
    PasswordPolicyNewCharacters,
    PasswordPolicyUpperLower,
    PasswordPolicyNonAlphabetic,
    PasswordPolicyNumber,
    PasswordPolicyNonAlphaNumeric,

    ReauthenticationForSettingsAdminPage,
    ReauthenticationForUsersAdminPage,

    SSOEnabled,
    SSOTicketVariableName,
    SSOLoginURL,
    SSOValidateURL,
    SSOXMLUserNameTag,
    SSOJSONUserNamePath,
    SSOInvalidTicketCode,

    DefaultAdditionalIDPattern,

    SystemName,
    SystemTitle,
    SystemURL,

    DailyDatabaseBackup,
    RemoveOldDatabaseBackups,

    SystemEmailAddress,
    SystemEmailDisplayName,
    SystemEmailPassword,
    SystemEmailSMTPAddress,
    SystemEmailSMTPPort,
    SystemEmailUseSSL,
    SystemEmailTimeout,
    SystemEmailSubject,

    DefaultCNExtensions,
    HomePagePriorities,

    Job,

    AlertKnowledgeExpirationInDays,

    NotificationsSeenTimeout,
    NotificationsUpdateInterval,

    RealTime,
    Explorer,

    FileContents,
    FileContentExtractionInterval,
    FileContentExtractionBatchSize,
    FileContentExtractionStartTime,
    FileContentExtractionEndTime,

    Index,
    IndexUpdateInterval,
    IndexUpdateBatchSize,
    IndexUpdateStartTime,
    IndexUpdateEndTime,
    IndexUpdatePriorities,
    IndexUpdateUseRam,
    CheckPermissionsForSearchResults,

    EmailQueue,
    EmailQueueInterval,
    EmailQueueBatchSize,
    EmailQueueStartTime,
    EmailQueueEndTime,

    Recommender,
    RecommenderURL,
    RecommenderUsername,
    RecommenderPassword
}
