package com.raaivan.modules.forms;

import com.raaivan.modules.forms.beans.*;
import com.raaivan.modules.forms.enums.FormElementTypes;
import com.raaivan.modules.rv.beans.MutableUUID;
import com.raaivan.util.Base64;
import com.raaivan.util.PublicMethods;
import com.raaivan.util.RVBeanFactory;
import com.raaivan.util.dbutil.RVConnection;
import com.raaivan.util.dbutil.RVResultSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.mutable.MutableInt;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.*;
import java.util.stream.Collectors;

@Component
@ApplicationScope
public class FGParsers {
    private PublicMethods publicMethods;
    private RVConnection rvConnection;

    @Autowired
    public void _setDependencies(PublicMethods publicMethods, RVConnection rvConnection) {
        if (this.publicMethods == null) this.publicMethods = publicMethods;
        if (this.rvConnection == null) this.rvConnection = rvConnection;
    }

    public List<FormType> formTypes(RVResultSet resultSet) {
        List<FormType> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                FormType e = RVBeanFactory.getBean(FormType.class);

                e.setFormID(publicMethods.parseUUID((String) resultSet.getValue(i, "FormID")));
                e.setTitle((String) resultSet.getValue(i, "Title"));
                e.setName((String) resultSet.getValue(i, "Name"));
                e.setDescription((String) resultSet.getValue(i, "Description"));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<FormElement> formElements(RVResultSet resultSet) {
        List<FormElement> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                FormElement e = RVBeanFactory.getBean(FormElement.class);

                e.setElementID(publicMethods.parseUUID((String) resultSet.getValue(i, "ElementID")));
                e.setFormID(publicMethods.parseUUID((String) resultSet.getValue(i, "FormID")));
                e.setTitle((String) resultSet.getValue(i, "Title"));
                e.setName((String) resultSet.getValue(i, "Name"));
                e.setNecessary((Boolean) resultSet.getValue(i, "Necessary"));
                e.setUniqueValue((Boolean) resultSet.getValue(i, "UniqueValue"));
                e.setSequenceNumber((Integer) resultSet.getValue(i, "SequenceNumber"));
                e.setType(publicMethods.lookupEnum(FormElementTypes.class,
                        (String) resultSet.getValue(i, "Type"), FormElementTypes.None));
                e.setInfo((String) resultSet.getValue(i, "Info"));
                e.setWeight((Double) resultSet.getValue(i, "Weight"));

                if (!StringUtils.isBlank(e.getInfo()) && e.getInfo().charAt(0) != '{') {
                    e.setInfo("{\"Options\":[" + rvConnection.getTagsList(e.getInfo()).stream()
                            .map(u -> "\"" + Base64.encode(u) + "\"").collect(Collectors.joining(",")) + "]}");
                }

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<FormElement> elementLimits(RVResultSet resultSet) {
        List<FormElement> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                FormElement e = RVBeanFactory.getBean(FormElement.class);

                e.setElementID(publicMethods.parseUUID((String) resultSet.getValue(i, "ElementID")));
                e.setTitle((String) resultSet.getValue(i, "Title"));
                e.setNecessary((Boolean) resultSet.getValue(i, "Necessary"));
                e.setType(publicMethods.lookupEnum(FormElementTypes.class,
                        (String) resultSet.getValue(i, "Type"), FormElementTypes.None));
                e.setInfo((String) resultSet.getValue(i, "Info"));

                if (!StringUtils.isBlank(e.getInfo()) && e.getInfo().charAt(0) != '{') {
                    e.setInfo("{\"Options\":[" + rvConnection.getTagsList(e.getInfo()).stream()
                            .map(u -> "\"" + Base64.encode(u) + "\"").collect(Collectors.joining(",")) + "]}");
                }

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<FormType> formInstances(RVResultSet resultSet) {
        List<FormType> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                FormType e = RVBeanFactory.getBean(FormType.class);

                e.setInstanceID(publicMethods.parseUUID((String) resultSet.getValue(i, "InstanceID")));
                e.setOwnerID(publicMethods.parseUUID((String) resultSet.getValue(i, "OwnerID")));
                e.setFormID(publicMethods.parseUUID((String) resultSet.getValue(i, "FormID")));
                e.setTitle((String) resultSet.getValue(i, "FormTitle"));
                e.setDescription((String) resultSet.getValue(i, "Description"));
                e.setFilled((Boolean) resultSet.getValue(i, "Filled"));
                e.setFillingDate((DateTime) resultSet.getValue(i, "FillingDate"));
                e.getCreator().setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "CreatorUserID")));
                e.getCreator().setUserName((String) resultSet.getValue(i, "CreatorUserName"));
                e.getCreator().setFirstName((String) resultSet.getValue(i, "CreatorFirstName"));
                e.getCreator().setLastName((String) resultSet.getValue(i, "CreatorLastName"));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<FormElement> formInstanceElements(RVResultSet resultSet) {
        List<FormElement> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                FormElement e = RVBeanFactory.getBean(FormElement.class);

                e.setElementID(publicMethods.parseUUID((String) resultSet.getValue(i, "ElementID")));
                e.setFormInstanceID(publicMethods.parseUUID((String) resultSet.getValue(i, "InstanceID")));
                e.setRefElementID(publicMethods.parseUUID((String) resultSet.getValue(i, "RefElementID")));
                e.setTitle((String) resultSet.getValue(i, "Title"));
                e.setName((String) resultSet.getValue(i, "Name"));
                e.setSequenceNumber((Integer) resultSet.getValue(i, "SequenceNumber"));
                e.setTextValue((String) resultSet.getValue(i, "TextValue"));
                e.setFloatValue((Double) resultSet.getValue(i, "FloatValue"));
                e.setBitValue((Boolean) resultSet.getValue(i, "BitValue"));
                e.setDateValue((DateTime) resultSet.getValue(i, "DateValue"));
                e.setNecessary((Boolean) resultSet.getValue(i, "Necessary"));
                e.setUniqueValue((Boolean) resultSet.getValue(i, "UniqueValue"));
                e.setType(publicMethods.lookupEnum(FormElementTypes.class,
                        (String) resultSet.getValue(i, "Type"), FormElementTypes.None));
                e.setInfo((String) resultSet.getValue(i, "Info"));
                e.setWeight((Double) resultSet.getValue(i, "Weight"));
                e.setFilled((Boolean) resultSet.getValue(i, "Filled"));
                e.setEditionsCount((Integer) resultSet.getValue(i, "EditionsCount"));

                if (!StringUtils.isBlank(e.getInfo()) && e.getInfo().charAt(0) != '{') {
                    e.setInfo("{\"Options\":[" + rvConnection.getTagsList(e.getInfo()).stream()
                            .map(u -> "\"" + Base64.encode(u) + "\"").collect(Collectors.joining(",")) + "]}");
                }

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public Map<UUID, List<Map.Entry<UUID, String>>> selectedGuids(RVResultSet resultSet) {
        Map<UUID, List<Map.Entry<UUID, String>>> ret = new HashMap<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                UUID elementId = publicMethods.parseUUID((String) resultSet.getValue(i, "ElementID"));
                UUID id = publicMethods.parseUUID((String) resultSet.getValue(i, "ID"));
                String name = (String) resultSet.getValue(i, "Name");

                if (elementId == null || id == null) continue;

                if (!ret.containsKey(elementId)) ret.put(elementId, new ArrayList<>());

                ret.get(elementId).add(new AbstractMap.SimpleEntry<UUID, String>(id, name));
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<FormElement> elementChanges(RVResultSet resultSet) {
        List<FormElement> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                FormElement e = RVBeanFactory.getBean(FormElement.class);

                e.setChangeID((Long) resultSet.getValue(i, "ID"));
                e.setElementID(publicMethods.parseUUID((String) resultSet.getValue(i, "ElementID")));
                e.setTextValue((String) resultSet.getValue(i, "TextValue"));
                e.setFloatValue((Double) resultSet.getValue(i, "FloatValue"));
                e.setBitValue((Boolean) resultSet.getValue(i, "BitValue"));
                e.setDateValue((DateTime) resultSet.getValue(i, "DateValue"));
                e.setCreationDate((DateTime) resultSet.getValue(i, "CreationDate"));
                e.getCreator().setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "CreatorUserID")));
                e.getCreator().setUserName((String) resultSet.getValue(i, "CreatorUserName"));
                e.getCreator().setFirstName((String) resultSet.getValue(i, "CreatorFirstName"));
                e.getCreator().setLastName((String) resultSet.getValue(i, "CreatorLastName"));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<FormRecord> formRecords(RVResultSet resultSet, List<FormElement> elements) {
        List<FormRecord> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                FormRecord e = RVBeanFactory.getBean(FormRecord.class);

                e.setInstanceID(publicMethods.parseUUID((String) resultSet.getValue(i, "InstanceID")));

                if (e.getInstanceID() == null) continue;

                e.setOwnerID(publicMethods.parseUUID((String) resultSet.getValue(i, "OwnerID")));
                e.setCreationDate((DateTime) resultSet.getValue(i, "CreationDate"));

                final int rowNumber = i;

                elements.forEach(elem -> {
                    RecordCell cell = RVBeanFactory.getBean(RecordCell.class);

                    cell.setElementID(elem.getElementID());
                    cell.setValue((String) resultSet.getValue(rowNumber, elem.getElementID().toString()));

                    e.getCells().add(cell);
                });

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public void formStatistics(RVResultSet resultSet, MutableDouble weightSum, MutableDouble sum,
                               MutableDouble weightedSum, MutableDouble avg, MutableDouble weightedAvg,
                               MutableDouble min, MutableDouble max, MutableDouble var, MutableDouble stDev) {
        if (resultSet.getRowsCount() > 0) {
            try {
                publicMethods.tryParseDouble(resultSet.getValue(0, "WeightSum").toString(), weightSum);
                publicMethods.tryParseDouble(resultSet.getValue(0, "Sum").toString(), sum);
                publicMethods.tryParseDouble(resultSet.getValue(0, "WeightedSum").toString(), weightedSum);
                publicMethods.tryParseDouble(resultSet.getValue(0, "Avg").toString(), avg);
                publicMethods.tryParseDouble(resultSet.getValue(0, "WeightedAvg").toString(), weightedAvg);
                publicMethods.tryParseDouble(resultSet.getValue(0, "Min").toString(), min);
                publicMethods.tryParseDouble(resultSet.getValue(0, "Max").toString(), max);
                publicMethods.tryParseDouble(resultSet.getValue(0, "Var").toString(), var);
                publicMethods.tryParseDouble(resultSet.getValue(0, "StDev").toString(), stDev);
            } catch (Exception ex) {
            }
        }
    }

    public List<Poll> polls(RVResultSet resultSet) {
        List<Poll> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                Poll e = RVBeanFactory.getBean(Poll.class);

                e.setPollID(publicMethods.parseUUID((String) resultSet.getValue(i, "PollID")));
                e.setIsCopyOfPollID(publicMethods.parseUUID((String) resultSet.getValue(i, "IsCopyOfPollID")));
                e.setOwnerID(publicMethods.parseUUID((String) resultSet.getValue(i, "OwnerID")));
                e.setName((String) resultSet.getValue(i, "Name"));
                e.setRefName((String) resultSet.getValue(i, "RefName"));
                e.setDescription((String) resultSet.getValue(i, "Description"));
                e.setRefDescription((String) resultSet.getValue(i, "RefDescription"));
                e.setBeginDate((DateTime) resultSet.getValue(i, "BeginDate"));
                e.setFinishDate((DateTime) resultSet.getValue(i, "FinishDate"));
                e.setShowSummary((Boolean) resultSet.getValue(i, "ShowSummary"));
                e.setHideContributors((Boolean) resultSet.getValue(i, "HideContributors"));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public void pollStatus(RVResultSet resultSet, StringBuilder description, MutableDateTime beginDate,
                           MutableDateTime finishDate, MutableUUID instanceId, MutableInt elementsCount,
                           MutableInt filledElementsCount, MutableInt allFilledFormsCount) {
        if (resultSet.getRowsCount() > 0) {
            try {
                description.append(publicMethods.parseString(
                        (String) resultSet.getValue(0, "Description"), false, ""));
                publicMethods.tryParseDate(resultSet.getValue(0, "BeginDate").toString(), beginDate);
                publicMethods.tryParseDate(resultSet.getValue(0, "FinishDate").toString(), finishDate);
                publicMethods.tryParseUUID(resultSet.getValue(0, "InstanceID").toString(), instanceId);
                publicMethods.tryParseInt(resultSet.getValue(0, "ElementsCount").toString(), elementsCount);
                publicMethods.tryParseInt(resultSet.getValue(0, "FilledElementsCount").toString(), filledElementsCount);
                publicMethods.tryParseInt(resultSet.getValue(0, "AllFilledFormsCount").toString(), allFilledFormsCount);
            } catch (Exception ex) {
            }
        }
    }

    public List<PollAbstract> pollAbstract(RVResultSet resultSet) {
        Map<UUID, PollAbstract> dic = new HashMap<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                UUID elementId = publicMethods.parseUUID((String) resultSet.getValue(i, "ElementID"));
                if (elementId == null) continue;

                if (!dic.containsKey(elementId)) {
                    PollAbstract pa = RVBeanFactory.getBean(PollAbstract.class);
                    pa.setElementID(elementId);
                    dic.put(elementId, pa);
                }

                dic.get(elementId).setTotalCount((Integer) resultSet.getValue(i, "TotalValuesCount"));

                PollAbstractValue val = RVBeanFactory.getBean(PollAbstractValue.class);

                val.setCount((Integer) resultSet.getValue(i, "Count"));
                if (val.getCount() == null) continue;

                Object obj = resultSet.getValue(i, "Value");

                if (obj == null) continue;
                else {
                    if (obj instanceof String) val.setTextValue((String) obj);
                    else if (obj instanceof Boolean) val.setBitValue((Boolean) obj);
                    else if (obj instanceof Double) val.setNumberValue((Double) obj);
                    else continue;
                }

                dic.get(elementId).getValues().add(val);
            } catch (Exception ex) {
            }
        }

        if (resultSet.getTablesCount() > 1) {
            for (int j = 0, lnt2 = resultSet.getRowsCount(1); j < lnt2; ++j) {
                try {
                    UUID elementId = publicMethods.parseUUID((String) resultSet.getValue(1, j, "ElementID"));
                    if (elementId == null || !dic.containsKey(elementId)) continue;

                    dic.get(elementId).setMin((Double) resultSet.getValue(1, j, "Min"));
                    dic.get(elementId).setMax((Double) resultSet.getValue(1, j, "Max"));
                    dic.get(elementId).setAvg((Double) resultSet.getValue(1, j, "Avg"));
                    dic.get(elementId).setVar((Double) resultSet.getValue(1, j, "Var"));
                    dic.get(elementId).setStDev((Double) resultSet.getValue(1, j, "StDev"));
                } catch (Exception exp) {
                }
            }
        }

        return new ArrayList<>(dic.values());
    }

    public List<FormElement> pollElementInstances(RVResultSet resultSet) {
        List<FormElement> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                FormElement e = RVBeanFactory.getBean(FormElement.class);

                e.getCreator().setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "UserID")));
                e.getCreator().setUserName((String) resultSet.getValue(i, "UserName"));
                e.getCreator().setFirstName((String) resultSet.getValue(i, "FirstName"));
                e.getCreator().setLastName((String) resultSet.getValue(i, "LastName"));
                e.setElementID(publicMethods.parseUUID((String) resultSet.getValue(i, "ElementID")));
                e.setRefElementID(publicMethods.parseUUID((String) resultSet.getValue(i, "RefElementID")));
                e.setTextValue((String) resultSet.getValue(i, "TextValue"));
                e.setFloatValue((Double) resultSet.getValue(i, "FloatValue"));
                e.setBitValue((Boolean) resultSet.getValue(i, "BitValue"));
                e.setDateValue((DateTime) resultSet.getValue(i, "DateValue"));
                e.setCreationDate((DateTime) resultSet.getValue(i, "CreationDate"));
                e.setLastModificationDate((DateTime) resultSet.getValue(i, "LastModificationDate"));
                e.setType(publicMethods.lookupEnum(FormElementTypes.class,
                        (String) resultSet.getValue(i, "Type"), FormElementTypes.None));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public void currentPollsCount(RVResultSet resultSet, MutableInt count, MutableInt doneCount) {
        if (resultSet.getRowsCount() > 0) {
            try {
                publicMethods.tryParseInt(resultSet.getValue(0, "Count").toString(), count);
                publicMethods.tryParseInt(resultSet.getValue(0, "DoneCount").toString(), doneCount);
            } catch (Exception ex) {
            }
        }
    }
}
