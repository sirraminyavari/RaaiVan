package com.raaivan.modules.users.beans;

import com.raaivan.modules.documents.util.DocumentUtilities;
import com.raaivan.modules.rv.beans.EmailTemplates;
import com.raaivan.util.Base64;
import com.raaivan.util.RVJSON;
import com.raaivan.util.PublicMethods;
import com.raaivan.util.rvsettings.RaaiVanSettings;
import io.micrometer.core.instrument.util.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class User {
    private DocumentUtilities documentUtilities;

    @Autowired
    public void _setDependencies(DocumentUtilities documentUtilities){
        if(this.documentUtilities == null) this.documentUtilities = documentUtilities;
    }

    private UUID UserID;
    private String UserName;
    private String Password;
    private String PasswordSalt;
    private String SaltedPassword;
    private String FirstName;
    private String LastName;
    private DateTime Birthday;
    private String JobTitle;
    private UUID MainPhoneID;
    private UUID MainEmailID;
    private List<PhoneNumber> PhoneNumbers;
    private List<EmailAddress> Emails;
    private Boolean IsApproved;
    private Boolean IsLockedOut;
    private DateTime LastLockoutDate;

    public User(){
        PhoneNumbers = new ArrayList<>();
        Emails = new ArrayList<>();
    }

    public UUID getUserID() {
        return UserID;
    }

    public void setUserID(UUID userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPasswordSalt() {
        return PasswordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        PasswordSalt = passwordSalt;
    }

    public String getSaltedPassword() {
        return SaltedPassword;
    }

    public void setSaltedPassword(String saltedPassword) {
        SaltedPassword = saltedPassword;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public DateTime getBirthday() {
        return Birthday;
    }

    public void setBirthday(DateTime birthday) {
        Birthday = birthday;
    }

    public String getJobTitle() {
        return JobTitle;
    }

    public void setJobTitle(String jobTitle) {
        JobTitle = jobTitle;
    }

    public UUID getMainPhoneID() {
        return MainPhoneID;
    }

    public void setMainPhoneID(UUID mainPhoneID) {
        MainPhoneID = mainPhoneID;
    }

    public UUID getMainEmailID() {
        return MainEmailID;
    }

    public void setMainEmailID(UUID mainEmailID) {
        MainEmailID = mainEmailID;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return PhoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        PhoneNumbers = phoneNumbers;
    }

    public List<EmailAddress> getEmails() {
        return Emails;
    }

    public void setEmails(List<EmailAddress> emails) {
        Emails = emails;
    }

    public Boolean isApproved() {
        return IsApproved;
    }

    public void setApproved(Boolean approved) {
        IsApproved = approved;
    }

    public Boolean isLockedOut() {
        return IsLockedOut;
    }

    public void setLockedOut(Boolean lockedOut) {
        IsLockedOut = lockedOut;
    }

    public DateTime getLastLockoutDate() {
        return LastLockoutDate;
    }

    public void setLastLockoutDate(DateTime lastLockoutDate) {
        LastLockoutDate = lastLockoutDate;
    }

    public String getFullName(){
        return ((StringUtils.isBlank(FirstName) ? "" : FirstName) + " " +
                (StringUtils.isBlank(LastName) ? "" : LastName)).trim();
    }

    public RVJSON toJson(UUID applicationId, boolean profileImageUrl){
        RVJSON ret = new RVJSON();

        ret.add("UserID", UserID == null ? "" : UserID.toString())
                .add("UserName", Base64.encode(UserName))
                .add("FirstName", Base64.encode(FirstName))
                .add("LastName", Base64.encode(LastName));

        if(applicationId != null && profileImageUrl)
            ret.add("ProfileImageURL", documentUtilities.getPersonalImageAddress(applicationId, UserID));

        return ret;
    }

    public RVJSON toJson(){
        return toJson(null, false);
    }
}
