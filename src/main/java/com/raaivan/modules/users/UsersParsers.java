package com.raaivan.modules.users;

import com.raaivan.util.dbutil.RVResultSet;
import com.raaivan.modules.users.beans.*;
import com.raaivan.modules.users.enums.*;
import com.raaivan.util.PublicMethods;
import com.raaivan.util.RVBeanFactory;
import org.apache.commons.lang3.mutable.MutableLong;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.ArrayList;
import java.util.List;

@Component
@ApplicationScope
public class UsersParsers {
    private PublicMethods publicMethods;

    @Autowired
    public void _setDependencies(PublicMethods publicMethods) {
        if (this.publicMethods == null) this.publicMethods = publicMethods;
    }

    public List<User> users(RVResultSet resultSet, MutableLong totalCount, boolean systemAlso)
    {
        totalCount.setValue(0);

        List<User> ret = new ArrayList<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                User u = RVBeanFactory.getBean(User.class);

                u.setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "UserID")));
                u.setUserName((String) resultSet.getValue(i, "UserName"));
                u.setFirstName((String) resultSet.getValue(i, "FirstName"));
                u.setLastName((String) resultSet.getValue(i, "LastName"));

                if (!systemAlso && u.getUserName() != null &&
                        u.getUserName().toLowerCase().equals("system")) continue;

                u.setJobTitle((String) resultSet.getValue(i, "JobTitle"));
                u.setBirthday((DateTime) resultSet.getValue(i, "BirthDay"));
                u.setMainPhoneID(publicMethods.parseUUID((String) resultSet.getValue(i, "MainPhoneID")));
                u.setMainEmailID(publicMethods.parseUUID((String) resultSet.getValue(i, "MainEmailID")));
                u.setApproved((Boolean) resultSet.getValue(i, "IsApproved"));
                u.setLockedOut((Boolean) resultSet.getValue(i, "IsLockedOut"));

                ret.add(u);
            }
            catch (Exception e){
            }
        }

        if(resultSet.getTablesCount() > 1){
            totalCount.setValue((Long) resultSet.getValue(1, 0, 0));
        }

        return ret;
    }

    public List<User> users(RVResultSet resultSet, MutableLong totalCount){
        return users(resultSet, totalCount, false);
    }

    public List<User> users(RVResultSet resultSet, boolean systemAlso)
    {
        MutableLong totalCount = new MutableLong(0);
        return users(resultSet, totalCount, systemAlso);
    }

    public List<User> users(RVResultSet resultSet)
    {
        return users(resultSet, false);
    }

    public List<Friend> friends(RVResultSet resultSet, MutableLong totalCount)
    {
        totalCount.setValue(0);

        List<Friend> ret = new ArrayList<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                Friend f = RVBeanFactory.getBean(Friend.class);

                f.getUser().setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "FriendID")));
                f.getUser().setUserName((String) resultSet.getValue(i, "UserName"));
                f.getUser().setFirstName((String) resultSet.getValue(i, "FirstName"));
                f.getUser().setLastName((String) resultSet.getValue(i, "LastName"));

                f.setRequestDate((DateTime) resultSet.getValue(i, "RequestDate"));
                f.setAcceptionDate((DateTime) resultSet.getValue(i, "AcceptionDate"));
                f.setAreFriends((Boolean) resultSet.getValue(i, "AreFriends"));
                f.setSender((Boolean) resultSet.getValue(i, "IsSender"));
                f.setMutualFriendsCount((Integer) resultSet.getValue(i, "MutualFriendsCount"));

                totalCount.setValue((Long) resultSet.getValue(i, "TotalCount"));

                ret.add(f);
            }
            catch (Exception e){}
        }

        return ret;
    }

    public List<Friend> friendshipStatuses(RVResultSet resultSet)
    {
        List<Friend> ret = new ArrayList<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                Friend f = RVBeanFactory.getBean(Friend.class);

                f.getUser().setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "UserID")));
                f.setAreFriends((Boolean) resultSet.getValue(i, "IsFriend"));
                f.setSender((Boolean) resultSet.getValue(i, "IsSender"));
                f.setMutualFriendsCount((Integer) resultSet.getValue(i, "MutualFriendsCount"));

                ret.add(f);
            }
            catch (Exception e){}
        }

        return ret;
    }

    public List<PhoneNumber> phoneNumbers(RVResultSet resultSet) {
        List<PhoneNumber> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                PhoneNumber p = RVBeanFactory.getBean(PhoneNumber.class);

                p.setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "UserID")));
                p.setNumberID(publicMethods.parseUUID((String) resultSet.getValue(i, "NumberID")));
                p.setNumber((String) resultSet.getValue(i, "PhoneNumber"));
                p.setPhoneType(publicMethods.lookupEnum(PhoneType.class,
                        (String) resultSet.getValue(i, "PhoneType"), PhoneType.NotSet));

                ret.add(p);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<EmailAddress> emailAddresses(RVResultSet resultSet)
    {
        List<EmailAddress> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                EmailAddress e = RVBeanFactory.getBean(EmailAddress.class);

                e.setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "UserID")));
                e.setEmailID(publicMethods.parseUUID((String) resultSet.getValue(i, "EmailID")));
                e.setEmailAddress((String) resultSet.getValue(i, "EmailAddress"));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<EmailContactStatus> emailContactsStatus(RVResultSet resultSet)
    {
        List<EmailContactStatus> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                EmailContactStatus e = RVBeanFactory.getBean(EmailContactStatus.class);

                e.setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "UserID")));
                e.setEmail((String) resultSet.getValue(i, "Email"));
                e.setFriendRequestReceived((Boolean) resultSet.getValue(i, "FriendRequestReceived"));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<JobExperience> jobExperiences(RVResultSet resultSet)
    {
        List<JobExperience> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                JobExperience e = RVBeanFactory.getBean(JobExperience.class);

                e.setJobID(publicMethods.parseUUID((String) resultSet.getValue(i, "JobID")));
                e.setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "UserID")));
                e.setTitle((String) resultSet.getValue(i, "Title"));
                e.setEmployer((String) resultSet.getValue(i, "Employer"));
                e.setStartDate((DateTime) resultSet.getValue(i, "StartDate"));
                e.setEndDate((DateTime) resultSet.getValue(i, "EndDate"));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<EducationalExperience> educationalExperiences(RVResultSet resultSet)
    {
        List<EducationalExperience> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                EducationalExperience e = RVBeanFactory.getBean(EducationalExperience.class);

                e.setEducationID(publicMethods.parseUUID((String) resultSet.getValue(i, "EducationID")));
                e.setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "UserID")));
                e.setSchool((String) resultSet.getValue(i, "School"));
                e.setStudyField((String) resultSet.getValue(i, "StudyField"));
                e.setStartDate((DateTime) resultSet.getValue(i, "StartDate"));
                e.setEndDate((DateTime) resultSet.getValue(i, "EndDate"));
                e.setIsSchool((Boolean) resultSet.getValue(i, "IsSchool"));
                e.setLevel(publicMethods.lookupEnum(EducationalLevel.class,
                        (String) resultSet.getValue(i, "Level"), EducationalLevel.None));
                e.setGraduateDegree(publicMethods.lookupEnum(GraduateDegree.class,
                        (String) resultSet.getValue(i, "GraduateDegree"), GraduateDegree.None));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<HonorsAndAwards> honorsAndAwards(RVResultSet resultSet)
    {
        List<HonorsAndAwards> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                HonorsAndAwards e = RVBeanFactory.getBean(HonorsAndAwards.class);

                e.setID(publicMethods.parseUUID((String) resultSet.getValue(i, "ID")));
                e.setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "UserID")));
                e.setTitle((String) resultSet.getValue(i, "Title"));
                e.setIssuer((String) resultSet.getValue(i, "Issuer"));
                e.setOccupation((String) resultSet.getValue(i, "Occupation"));
                e.setIssueDate((DateTime) resultSet.getValue(i, "IssueDate"));
                e.setDescription((String) resultSet.getValue(i, "Description"));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<Language> languages(RVResultSet resultSet, boolean isUserLanguage)
    {
        List<Language> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                Language e = RVBeanFactory.getBean(Language.class);

                e.setLanguageName((String) resultSet.getValue(i, "LanguageName"));

                if(!isUserLanguage)
                    e.setLanguageID(publicMethods.parseUUID((String) resultSet.getValue(i, "LanguageID")));
                else {
                    e.setID(publicMethods.parseUUID((String) resultSet.getValue(i, "ID")));
                    e.setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "UserID")));
                    e.setLevel(publicMethods.lookupEnum(LanguageLevel.class,
                            (String) resultSet.getValue(i, "Level"), LanguageLevel.None));
                }

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<FriendSuggestion> friendSuggestions(RVResultSet resultSet, MutableLong totalCount)
    {
        totalCount.setValue(0);

        List<FriendSuggestion> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                FriendSuggestion e = RVBeanFactory.getBean(FriendSuggestion.class);

                e.getUser().setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "UserID")));
                e.getUser().setFirstName((String) resultSet.getValue(i, "FirstName"));
                e.getUser().setLastName((String) resultSet.getValue(i, "LastName"));
                e.setMutualFriends((Integer) resultSet.getValue(i, "MutualFriendsCount"));

                totalCount.setValue((Long) resultSet.getValue(i, "TotalCount"));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<Invitation> invitations(RVResultSet resultSet, MutableLong totalCount)
    {
        totalCount.setValue(0);

        List<Invitation> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                Invitation e = RVBeanFactory.getBean(Invitation.class);

                e.getReceiverUser().setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "ReceiverUserID")));
                e.getReceiverUser().setFirstName((String) resultSet.getValue(i, "ReceiverFirstName"));
                e.getReceiverUser().setLastName((String) resultSet.getValue(i, "ReceiverLastName"));
                e.setEmail((String) resultSet.getValue(i, "Email"));
                e.setSendDate((DateTime) resultSet.getValue(i, "SendDate"));
                e.setActivated((Boolean) resultSet.getValue(i, "Activated"));

                totalCount.setValue((Long) resultSet.getValue(i, "TotalCount"));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public void password(RVResultSet resultSet, StringBuilder password, StringBuilder passwordSalt)
    {
        password.setLength(0);
        passwordSalt.setLength(0);

        password.append(resultSet.getValue(0, "Password"));
        passwordSalt.append(resultSet.getValue(0, "PasswordSalt"));
    }

    public List<Password> lastPasswords(RVResultSet resultSet)
    {
        List<Password> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                Password e = RVBeanFactory.getBean(Password.class);

                e.setValue((String) resultSet.getValue(i, "Password"));
                e.setAutoGenerated((Boolean) resultSet.getValue(i, "AutoGenerated"));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<UserGroup> userGroups(RVResultSet resultSet)
    {
        List<UserGroup> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                UserGroup e = RVBeanFactory.getBean(UserGroup.class);

                e.setGroupID(publicMethods.parseUUID((String) resultSet.getValue(i, "GroupID")));
                e.setTitle((String) resultSet.getValue(i, "Title"));
                e.setDescription((String) resultSet.getValue(i, "Description"));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<AccessRole> accessRoles(RVResultSet resultSet)
    {
        List<AccessRole> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                AccessRole e = RVBeanFactory.getBean(AccessRole.class);

                e.setRoleID(publicMethods.parseUUID((String) resultSet.getValue(i, "RoleID")));
                e.setTitle((String) resultSet.getValue(i, "Title"));
                e.setName(publicMethods.lookupEnum(AccessRoleName.class,
                        (String) resultSet.getValue(i, "Name"), AccessRoleName.None));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<AccessRoleName> permissions(List<String> resultSet)
    {
        List<AccessRoleName> roles = new ArrayList<>();

        for(String str : resultSet){
            AccessRoleName n = publicMethods.lookupEnum(AccessRoleName.class, str, AccessRoleName.None);
            if(!n.equals(AccessRoleName.None)) roles.add(n);
        }

        return roles;
    }

    public List<AdvancedUserSearch> advancedUserSearch(RVResultSet resultSet, MutableLong totalCount)
    {
        totalCount.setValue(0);

        List<AdvancedUserSearch> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                AdvancedUserSearch e = RVBeanFactory.getBean(AdvancedUserSearch.class);

                e.setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "UserID")));
                e.setRank((Double) resultSet.getValue(i, "Rank"));
                e.setIsMemberCount((Integer) resultSet.getValue(i, "IsMemberCount"));
                e.setIsExpertCount((Integer) resultSet.getValue(i, "IsExpertCount"));
                e.setIsContributorCount((Integer) resultSet.getValue(i, "IsContributorCount"));
                e.setHasPropertyCount((Integer) resultSet.getValue(i, "HasPropertyCount"));
                e.setResume((Integer) resultSet.getValue(i, "Resume"));

                totalCount.setValue((Long) resultSet.getValue(i, "TotalCount"));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<AdvancedUserSearchMeta> advancedUserSearchMeta(RVResultSet resultSet)
    {
        List<AdvancedUserSearchMeta> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                AdvancedUserSearchMeta e = RVBeanFactory.getBean(AdvancedUserSearchMeta.class);

                e.setNodeID(publicMethods.parseUUID((String) resultSet.getValue(i, "NodeID")));
                e.setRank((Double) resultSet.getValue(i, "Rank"));
                e.setMember((Boolean) resultSet.getValue(i, "IsMember"));
                e.setExpert((Boolean) resultSet.getValue(i, "IsExpert"));
                e.setContributor((Boolean) resultSet.getValue(i, "IsContributor"));
                e.setHasProperty((Boolean) resultSet.getValue(i, "HasProperty"));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public User lockoutDate(RVResultSet resultSet)
    {
        User user = RVBeanFactory.getBean(User.class);

        user.setUserID(publicMethods.parseUUID((String) resultSet.getValue(0, "UserID")));
        user.setLockedOut((Boolean) resultSet.getValue(0, "IsLockedOut"));
        user.setLastLockoutDate((DateTime) resultSet.getValue(0, "LastLockoutDate"));
        user.setApproved((Boolean) resultSet.getValue(0, "IsApproved"));

        return user;
    }
}