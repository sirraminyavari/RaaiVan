package com.raaivan.modules.forms.util;

import com.raaivan.modules.documents.enums.FileOwnerTypes;
import com.raaivan.modules.documents.util.DocumentUtilities;
import com.raaivan.modules.forms.beans.FormElement;
import com.raaivan.modules.forms.beans.FormFilter;
import com.raaivan.modules.forms.enums.FormElementTypes;
import com.raaivan.modules.rv.beans.MutableUUID;
import com.raaivan.util.Base64;
import com.raaivan.util.PublicMethods;
import com.raaivan.util.RVBeanFactory;
import com.raaivan.util.RVJSON;
import com.raaivan.util.rvsettings.RaaiVanSettings;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.joda.time.MutableDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@ApplicationScope
public class FGUtilities {
    private PublicMethods publicMethods;
    private RaaiVanSettings raaivanSettings;
    private DocumentUtilities documentUtilities;

    @Autowired
    public void _setDependencies(PublicMethods publicMethods, RaaiVanSettings raaivanSettings,
                                 DocumentUtilities documentUtilities) {
        if (this.publicMethods == null) this.publicMethods = publicMethods;
        if (this.raaivanSettings == null) this.raaivanSettings = raaivanSettings;
        if (this.documentUtilities == null) this.documentUtilities = documentUtilities;
    }

    public List<FormElement> getFormElements(String strElements)
    {
        try
        {
            List<FormElement> retList = new ArrayList<>();

            if (StringUtils.isBlank(strElements)) return retList;

            RVJSON dic = new RVJSON(Base64.decode(strElements));

            if (!dic.isArray("Elements")) return retList;

            dic.getJSONArray("Elements").forEach(x -> {
                RVJSON elem = (RVJSON) x;

                FormElement newElement = RVBeanFactory.getBean(FormElement.class);

                newElement.setElementID(publicMethods.parseUUID(elem.getString("ElementID")));
                newElement.setFormInstanceID(publicMethods.parseUUID(elem.getString("InstanceID")));
                newElement.setTitle(elem.getString("Title"));
                newElement.setFilled(elem.getBoolean("Filled"));
                newElement.setSequenceNumber(-1);
                newElement.setInfo(Base64.decode(elem.getString("Info")));

                if (elem.hasValue("RefElementID"))
                    newElement.setRefElementID(publicMethods.parseUUID(elem.getString("RefElementID")));

                if (elem.hasValue("SequenceNumber"))
                    newElement.setSequenceNumber(publicMethods.parseInt(elem.get("SequenceNumber").toString()));

                String textValue = Base64.decode(elem.getString("TextValue"));
                if (!StringUtils.isBlank(textValue)) newElement.setTextValue(textValue);

                if (elem.hasValue("FloatValue"))
                    newElement.setFloatValue(publicMethods.parseDouble(elem.get("FloatValue").toString()));

                if (elem.hasValue("BitValue"))
                    newElement.setBitValue(publicMethods.parseBoolean(elem.get("BitValue").toString()));

                if (elem.hasValue("DateValue"))
                    newElement.setDateValue(publicMethods.parseDate(elem.get("DateValue").toString()));

                if (elem.hasValue("Files"))
                    newElement.setAttachedFiles(documentUtilities.getFilesInfo(Base64.decode(elem.getString("Files"))));

                if (elem.isArray("GuidItems")) {
                    elem.getJSONArray("GuidItems").forEach(i -> {
                        RVJSON item = (RVJSON) i;

                        newElement.getGuidItems().add(new AbstractMap.SimpleEntry<UUID, String>(
                                publicMethods.parseUUID(item.getString("ID")), Base64.decode(item.getString("Name"))));
                    });
                }

                if (newElement.getFilled() == null || !newElement.getFilled())
                {
                    newElement.setRefElementID(newElement.getElementID());
                    newElement.setElementID(UUID.randomUUID());
                }

                newElement.setType(publicMethods.lookupEnum(FormElementTypes.class,
                        elem.getString("Type"), FormElementTypes.None));

                if (newElement.getAttachedFiles() != null)
                {
                    newElement.getAttachedFiles().forEach(f -> {
                        f.setOwnerID(newElement.getElementID());
                        f.setOwnerType(FileOwnerTypes.FormElement);
                    });
                }

                retList.add(newElement);
            });

            return retList;
        }
        catch (Exception ex) { return new ArrayList<>(); }
    }

    public boolean setElementValue(String value, FormElement element)
    {
        if (StringUtils.isBlank(value)) value = "";
        FormElementTypes type = element.getType().equals(FormElementTypes.None) ? FormElementTypes.Text : element.getType();

        switch (type)
        {
            case Text:
            case Select:
            case Checkbox:
                element.setTextValue(value);
                return !StringUtils.isBlank(element.getTextValue());
            case Binary:
                element.setBitValue(value.toLowerCase().equals("true"));
                return true;
            case Date:
                if (value.toLowerCase().equals("now"))
                    element.setDateValue(publicMethods.now());
                else
                {
                    MutableDateTime dt = new MutableDateTime();
                    if(publicMethods.tryParseDate(value, dt)) element.setDateValue(dt.toDateTime());
                }
                return element.getDateValue() != null;
            case Node:
            case User:
                MutableUUID id = new MutableUUID();
                if(publicMethods.tryParseUUID(value, id))
                    element.getGuidItems().add(new AbstractMap.SimpleEntry<UUID, String>(id.getValue(), ""));
                return id.getValue() != null;
            case Numeric:
                MutableDouble flt = new MutableDouble(0);
                if(publicMethods.tryParseDouble(value, flt)) element.setFloatValue(flt.getValue());
                return element.getFloatValue() != null;
        }

        return false;
    }

    public List<FormFilter> getFiltersFromJson(RVJSON dic, UUID ownerId)
    {
        List<FormFilter> retFilters = new ArrayList<>();

        dic.keySet().forEach(id -> {
            try
            {
                MutableUUID elementId = new MutableUUID();
                if(!publicMethods.tryParseUUID(id, elementId) || !dic.isJson(id)) return;

                RVJSON elemDic = (RVJSON)dic.getJSONObject(id);

                FormElementTypes type =
                        publicMethods.lookupEnum(FormElementTypes.class, elemDic.getString("Type"), FormElementTypes.None);
                if(type.equals(FormElementTypes.None)) return;;

                if (type.equals(FormElementTypes.Form))
                {
                    List<FormFilter> subFilters = getFiltersFromJson(elemDic, elementId.getValue());

                    if (subFilters != null && subFilters.size() > 0)
                    {
                        FormFilter ff = RVBeanFactory.getBean(FormFilter.class);
                        ff.setElementID(elementId.getValue());
                        ff.setOwnerID(ownerId);

                        retFilters.add(ff); //just to say that this field has filters
                        retFilters.addAll(subFilters);
                    }

                    return;
                }

                FormFilter filter = RVBeanFactory.getBean(FormFilter.class);
                filter.setElementID(elementId.getValue());
                filter.setOwnerID(ownerId);

                if (elemDic.hasValue("Exact"))
                    filter.setExact(publicMethods.parseBoolean(elemDic.get("Exact").toString()));
                if (elemDic.hasValue("Or"))
                    filter.setOr(publicMethods.parseBoolean(elemDic.get("Or").toString()));
                if (elemDic.hasValue("Bit"))
                    filter.setBit(publicMethods.parseBoolean(elemDic.get("Bit").toString()));
                if (elemDic.hasValue("FloatFrom"))
                    filter.setFloatFrom(publicMethods.parseDouble(elemDic.get("FloatFrom").toString()));
                if (elemDic.hasValue("FloatTo"))
                    filter.setFloatTo(publicMethods.parseDouble(elemDic.get("FloatTo").toString()));
                if (elemDic.hasValue("DateFrom"))
                    filter.setDateFrom(publicMethods.parseDate(elemDic.get("DateFrom").toString()));
                if (elemDic.hasValue("DateTo"))
                    filter.setDateTo(publicMethods.parseDate(elemDic.get("DateTo").toString(), 1));

                if (elemDic.isArray("TextItems"))
                {
                    elemDic.getJSONArray("TextItems").forEach(o -> {
                        filter.getTextItems().add(Base64.decode(o.toString()));
                    });
                }

                if (elemDic.isArray("GuidItems"))
                {
                    elemDic.getJSONArray("GuidItems").forEach(o -> {
                        MutableUUID val = new MutableUUID();
                        if(publicMethods.tryParseUUID(o.toString(), val)) filter.getGuidItems().add(val.getValue());
                    });
                }

                retFilters.add(filter);
            }
            catch (Exception ex) { }
        });

        return retFilters;
    }

    public List<FormFilter> getFiltersFromJson(RVJSON dic){
        return getFiltersFromJson(dic, null);
    }

    public List<FormFilter> getFiltersFromJson(String jsonInput) {
        return getFiltersFromJson(new RVJSON(jsonInput));
    }

    public boolean isValidName(String formOrElementName) {
        try
        {
            return StringUtils.isBlank(formOrElementName) || formOrElementName.matches("^[a-zA-Z0-9_]{1,100}$");
        }
        catch (Exception ex) { return false; }
    }
}
