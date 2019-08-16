package com.raaivan.modules.users.beans;

import com.raaivan.modules.users.enums.EducationalLevel;
import com.raaivan.modules.users.enums.GraduateDegree;
import com.raaivan.util.PublicMethods;
import org.joda.time.DateTime;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class EducationalExperience {
    private UUID EducationID;
    private UUID UserID;
    private String School;
    private String StudyField;
    private EducationalLevel Level;
    private DateTime StartDate;
    private DateTime EndDate;
    private GraduateDegree GraduateDegree;
    private Boolean IsSchool;

    public EducationalExperience(){
        this.Level = EducationalLevel.None;
        this.GraduateDegree = GraduateDegree.None;
    }

    public UUID getEducationID() {
        return EducationID;
    }

    public void setEducationID(UUID educationID) {
        EducationID = educationID;
    }

    public UUID getUserID() {
        return UserID;
    }

    public void setUserID(UUID userID) {
        UserID = userID;
    }

    public String getSchool() {
        return School;
    }

    public void setSchool(String school) {
        School = school;
    }

    public String getStudyField() {
        return StudyField;
    }

    public void setStudyField(String studyField) {
        StudyField = studyField;
    }

    public EducationalLevel getLevel() {
        return Level;
    }

    public void setLevel(EducationalLevel level) {
        Level = level;
    }

    public DateTime getStartDate() {
        return StartDate;
    }

    public void setStartDate(DateTime startDate) {
        StartDate = startDate;
    }

    public DateTime getEndDate() {
        return EndDate;
    }

    public void setEndDate(DateTime endDate) {
        EndDate = endDate;
    }

    public GraduateDegree getGraduateDegree() {
        return GraduateDegree;
    }

    public void setGraduateDegree(GraduateDegree graduateDegree) {
        GraduateDegree = graduateDegree;
    }

    public Boolean getIsSchool() {
        return IsSchool;
    }

    public void setIsSchool(Boolean school) {
        IsSchool = school;
    }
}
