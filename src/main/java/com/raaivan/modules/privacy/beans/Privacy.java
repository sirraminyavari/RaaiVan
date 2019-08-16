package com.raaivan.modules.privacy.beans;

import com.raaivan.modules.documents.util.DocumentUtilities;
import com.raaivan.util.RVBeanFactory;
import com.raaivan.util.RVJSON;
import com.raaivan.util.PublicMethods;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class Privacy {
    private PublicMethods publicMethods;

    @Autowired
    public void _setDependencies(PublicMethods publicMethods){
        if(this.publicMethods == null) this.publicMethods = publicMethods;
    }

    private UUID ObjectID;
    private Boolean CalculateHierarchy;
    private ConfidentialityLevel Confidentiality;
    private List<DefaultPermission> DefaultPermissions;
    private List<Audience> AudienceList;

    public Privacy() {
        Confidentiality = RVBeanFactory.getBean(ConfidentialityLevel.class);
        DefaultPermissions = new ArrayList<>();
        AudienceList = new ArrayList<>();
    }

    public UUID getObjectID() {
        return ObjectID;
    }

    public void setObjectID(UUID objectID) {
        ObjectID = objectID;
    }

    public Boolean getCalculateHierarchy() {
        return CalculateHierarchy;
    }

    public void setCalculateHierarchy(Boolean calculateHierarchy) {
        CalculateHierarchy = calculateHierarchy;
    }

    public ConfidentialityLevel getConfidentiality() {
        return Confidentiality;
    }

    public void setConfidentiality(ConfidentialityLevel confidentiality) {
        Confidentiality = confidentiality;
    }

    public List<DefaultPermission> getDefaultPermissions() {
        return DefaultPermissions;
    }

    public void setDefaultPermissions(List<DefaultPermission> defaultPermissions) {
        DefaultPermissions = defaultPermissions;
    }

    public List<Audience> getAudienceList() {
        return AudienceList;
    }

    public void setAudienceList(List<Audience> audience) {
        AudienceList = audience;
    }

    public RVJSON toJson()
    {
        return (new RVJSON())
                .add("ObjectID", ObjectID)
                .add("CalculateHierarchy", CalculateHierarchy)
                .add("Confidentiality", Confidentiality == null ? null : Confidentiality.toJson())
                .add("Audience", AudienceList.stream().map(Audience::toJson).toArray())
                .add("DefaultPermissions", DefaultPermissions.stream().map(DefaultPermission::toJson).toArray());
    }

    private Privacy _fromJson(JSONObject json)
    {
        if(json == null) return null;

        if (!json.isNull("CalculateHierarchy") && json.get("CalculateHierarchy") != null)
            this.setCalculateHierarchy(publicMethods.parseBoolean(json.get("CalculateHierarchy").toString()));
        if (!json.isNull("ConfidentialityID") && json.get("ConfidentialityID") != null)
            this.Confidentiality.setID(publicMethods.parseUUID(json.get("ConfidentialityID").toString()));

        if (!json.isNull("Audience") && json.get("Audience") instanceof JSONArray)
        {
            for (Object a : (JSONArray)json.get("Audience"))
            {
                if (!(a instanceof JSONObject)) continue;
                Audience o = Audience.fromJson((JSONObject) a);
                if (o != null) this.AudienceList.add(o);
            }
        }

        if (!json.isNull("DefaultPermissions") && json.get("DefaultPermissions") != null &&
                json.get("DefaultPermissions") instanceof JSONArray)
        {
            for (Object d : (JSONArray)json.get("DefaultPermissions"))
            {
                if (!(d instanceof JSONObject)) continue;
                DefaultPermission o = DefaultPermission.fromJson((JSONObject) d);
                if (o != null) this.DefaultPermissions.add(o);
            }
        }

        return this.CalculateHierarchy != null || this.Confidentiality.getID() != null ||
                this.AudienceList.size() > 0 || this.DefaultPermissions.size() > 0 ? this : null;
    }

    public static Privacy fromJson(JSONObject json){
        return RVBeanFactory.getBean(Privacy.class)._fromJson(json);
    }
}
