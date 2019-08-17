package com.raaivan.modules.forms;

import com.raaivan.modules.forms.beans.*;
import com.raaivan.modules.forms.enums.FormElementTypes;
import com.raaivan.modules.rv.beans.MutableUUID;
import com.raaivan.util.PublicMethods;
import com.raaivan.util.dbutil.RVConnection;
import com.raaivan.util.dbutil.RVStructuredParam;
import com.raaivan.util.rvsettings.RaaiVanSettings;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.mutable.MutableInt;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@ApplicationScope
public class FGDAO {
    private RVConnection rvConnection;
    private FGParsers parser;
    private PublicMethods publicMethods;
    private RaaiVanSettings raaivanSettings;

    @Autowired
    public void _setDependencies(RVConnection rvConnection, FGParsers parser, PublicMethods publicMethods,
                                 RaaiVanSettings raaivanSettings) {
        if (this.rvConnection == null) this.rvConnection = rvConnection;
        if (this.parser == null) this.parser = parser;
        if (this.publicMethods == null) this.publicMethods = publicMethods;
        if (this.raaivanSettings == null) this.raaivanSettings = raaivanSettings;
    }

    private String getQualifiedName(String name) {
        return "[dbo].[FG_" + name + "]";
    }

    public boolean createForm(UUID applicationId, UUID formId, String title, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("CreateForm"),
                applicationId, formId, title, currentUserId, publicMethods.now());
    }

    public boolean setFormTitle(UUID applicationId, UUID formId, String title, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("SetFormTitle"),
                applicationId, formId, title, currentUserId, publicMethods.now());
    }

    public boolean setFormName(UUID applicationId, UUID formId, String name, UUID currentUserId, StringBuilder errorMessage) {
        return rvConnection.succeed(errorMessage, getQualifiedName("SetFormName"),
                applicationId, formId, name, currentUserId, publicMethods.now());
    }

    public boolean setFormDescription(UUID applicationId, UUID formId, String description, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("SetFormDescription"),
                applicationId, formId, description, currentUserId, publicMethods.now());
    }

    public boolean removeForm(UUID applicationId, UUID formId, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("ArithmeticDeleteForm"),
                applicationId, formId, currentUserId, publicMethods.now());
    }

    public boolean recycleForm(UUID applicationId, UUID formId, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("RecycleForm"),
                applicationId, formId, currentUserId, publicMethods.now());
    }

    public List<FormType> getForms(UUID applicationId, String searchText,
                                   Integer count, Integer lowerBoundary, Boolean hasName, Boolean archive) {
        return parser.formTypes(rvConnection.read(getQualifiedName("GetForms"), applicationId,
                rvConnection.getSearchText(searchText), count, lowerBoundary, hasName, archive));
    }

    public List<FormType> getForms(UUID applicationId, String searchText, Integer count, Integer lowerBoundary) {
        return getForms(applicationId, searchText, count, lowerBoundary, null, null);
    }

    public List<FormType> getForms(UUID applicationId, List<UUID> formIds) {
        return parser.formTypes(rvConnection.read(getQualifiedName("GetFormsByIDs"), applicationId,
                formIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ','));
    }

    public FormType getForm(UUID applicationId, UUID formId) {
        List<UUID> ids = new ArrayList<UUID>() {{
            add(formId);
        }};

        return getForms(applicationId, ids).stream().findFirst().orElse(null);
    }

    public boolean addFormElement(UUID applicationId, UUID elementId, UUID formId, String title, String name,
                                  int sequenceNumber, FormElementTypes type, String info, UUID currentUserId,
                                  StringBuilder errorMessage) {
        return rvConnection.succeed(errorMessage, getQualifiedName("AddFormElement"), applicationId, elementId,
                formId, title, name, sequenceNumber, type == FormElementTypes.None ? FormElementTypes.Text : type,
                info, currentUserId, publicMethods.now());
    }

    public boolean modifyFormElement(UUID applicationId, UUID elementId, String title, String name, String info,
                                     Double weight, UUID currentUserId, StringBuilder errorMessage) {
        return rvConnection.succeed(errorMessage, getQualifiedName("ModifyFormElement"), applicationId, elementId,
                title, StringUtils.isBlank(name) ? null : name, info, weight, currentUserId, publicMethods.now());
    }

    public boolean setElementsOrder(UUID applicationId, List<UUID> elementIds) {
        return rvConnection.succeed(getQualifiedName("SetElementsOrder"), applicationId,
                elementIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',');
    }

    public boolean setFormElementNecessity(UUID applicationId, UUID elementId, boolean necessity) {
        return rvConnection.succeed(getQualifiedName("SetFormElementNecessity"), applicationId, elementId, necessity);
    }

    public boolean setFormElementUniqueness(UUID applicationId, UUID elementId, boolean value) {
        return rvConnection.succeed(getQualifiedName("SetFormElementUniqueness"), applicationId, elementId, value);
    }

    public boolean removeElement(UUID applicationId, UUID elementId, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("ArithmeticDeleteFormElement"),
                applicationId, elementId, currentUserId, publicMethods.now());
    }

    public List<FormElement> getFormElements(UUID applicationId, UUID formId, UUID ownerId, FormElementTypes type) {
        return parser.formElements(rvConnection.read(getQualifiedName("GetFormElements"),
                applicationId, formId, ownerId, type == FormElementTypes.None ? null : type.toString()));
    }

    public List<FormElement> getFormElements(UUID applicationId, UUID formId) {
        return getFormElements(applicationId, formId, null, FormElementTypes.None);
    }

    public List<FormElement> getFormElements(UUID applicationId, List<UUID> elementIds) {
        return parser.formElements(rvConnection.read(getQualifiedName("GetFormElementsByIDs"), applicationId,
                elementIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ','));
    }

    public FormElement getFormElement(UUID applicationId, UUID elementId) {
        List<UUID> ids = new ArrayList<UUID>() {{
            add(elementId);
        }};

        return getFormElements(applicationId, ids).stream().findFirst().orElse(null);
    }

    public List<UUID> isFormElement(UUID applicationId, List<UUID> ids) {
        return rvConnection.getUUIDList(getQualifiedName("IsFormElement"), applicationId,
                ids.stream().map(UUID::toString).collect(Collectors.joining(",")), ',');
    }

    public boolean isFormElement(UUID applicationId, UUID id) {
        List<UUID> ids = new ArrayList<UUID>() {{
            add(id);
        }};

        return isFormElement(applicationId, ids).size() > 0;
    }

    public boolean createFormInstances(UUID applicationId, List<FormType> instances, UUID currentUserId) {
        RVStructuredParam instancesDT = new RVStructuredParam("FormInstanceTableType")
                .addColumnMetaData("InstanceID", UUID.class)
                .addColumnMetaData("FormID", UUID.class)
                .addColumnMetaData("OwnerID", UUID.class)
                .addColumnMetaData("DirectorID", UUID.class)
                .addColumnMetaData("Admin", Boolean.class)
                .addColumnMetaData("IsTemporary", Boolean.class);
        instances.forEach(u -> instancesDT.addRow(u.getInstanceID(), u.getFormID(),
                u.getOwnerID(), u.getDirectorID(), u.getAdmin(), u.getTemporary()));

        return rvConnection.succeed(getQualifiedName("CreateFormInstance"),
                applicationId, instancesDT, currentUserId, publicMethods.now());
    }

    public boolean createFormInstance(UUID applicationId, FormType instance, UUID currentUserId) {
        List<FormType> items = new ArrayList<FormType>() {{
            add(instance);
        }};

        return createFormInstances(applicationId, items, currentUserId);
    }

    public boolean removeFormInstances(UUID applicationId, List<UUID> instanceIds, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("RemoveFormInstances"), applicationId,
                instanceIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                currentUserId, publicMethods.now());
    }

    public boolean removeFormInstance(UUID applicationId, UUID instanceId, UUID currentUserId) {
        List<UUID> ids = new ArrayList<UUID>() {{
            add(instanceId);
        }};

        return removeFormInstances(applicationId, ids, currentUserId);
    }

    public boolean removeOwnerFormInstances(UUID applicationId, UUID ownerId, UUID formId, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("RemoveOwnerFormInstances"),
                applicationId, ownerId, formId, currentUserId, publicMethods.now());
    }

    public List<FormType> getOwnerFormInstances(UUID applicationId, List<UUID> ownerIds,
                                                UUID formId, Boolean isTemporary, UUID userId) {
        return parser.formInstances(rvConnection.read(getQualifiedName("GetOwnerFormInstances"), applicationId,
                ownerIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                formId, isTemporary, userId));
    }

    public List<FormType> getOwnerFormInstances(UUID applicationId, List<UUID> ownerIds) {
        return getOwnerFormInstances(applicationId, ownerIds, null, null, null);
    }

    public List<FormType> getOwnerFormInstances(UUID applicationId, UUID ownerId,
                                                UUID formId, Boolean isTemporary, UUID userId) {
        List<UUID> ids = new ArrayList<UUID>() {{
            add(ownerId);
        }};

        return getOwnerFormInstances(applicationId, ids, formId, isTemporary, userId);
    }

    public List<FormType> getOwnerFormInstances(UUID applicationId, UUID ownerId) {
        return getOwnerFormInstances(applicationId, ownerId, null, null, null);
    }

    public List<FormType> getFormInstances(UUID applicationId, List<UUID> instanceIds) {
        return parser.formInstances(rvConnection.read(getQualifiedName("GetFormInstances"), applicationId,
                instanceIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ','));
    }

    public FormType getFormInstance(UUID applicationId, UUID instanceId) {
        List<UUID> ids = new ArrayList<UUID>() {{
            add(instanceId);
        }};

        return getFormInstances(applicationId, ids).stream().findFirst().orElse(null);
    }

    public UUID getFormInstanceOwnerID(UUID applicationId, UUID instanceIdOrElementId) {
        return rvConnection.getUUID(getQualifiedName("GetFormInstanceOwnerID"), applicationId, instanceIdOrElementId);
    }

    public UUID getFormInstanceHierarchyOwnerId(UUID applicationId, UUID instanceId) {
        return rvConnection.getUUID(getQualifiedName("GetFormInstanceHierarchyOwnerID"), applicationId, instanceId);
    }

    public boolean meetsUniqueConstraint(UUID applicationId, UUID instanceId,
                                         UUID elementId, String textValue, Double floatValue) {
        return rvConnection.succeed(getQualifiedName("MeetsUniqueConstraint"),
                applicationId, instanceId, elementId, textValue, floatValue);
    }

    public boolean saveFormInstanceElements(UUID applicationId, List<FormElement> elements,
                                            List<UUID> elementsToClear, UUID currentUserId, StringBuilder errorMessage) {
        RVStructuredParam elementsDT = new RVStructuredParam("FormElementTableType")
                .addColumnMetaData("ElementID", UUID.class)
                .addColumnMetaData("InstanceID", UUID.class)
                .addColumnMetaData("RefElementID", UUID.class)
                .addColumnMetaData("Title", String.class)
                .addColumnMetaData("SequenceNumber", Integer.class)
                .addColumnMetaData("Type", String.class)
                .addColumnMetaData("Info", String.class)
                .addColumnMetaData("TextValue", String.class)
                .addColumnMetaData("FloatValue", Double.class)
                .addColumnMetaData("BitValue", Boolean.class)
                .addColumnMetaData("DateValue", DateTime.class);
        elements.forEach(u -> elementsDT.addRow(u.getElementID(), u.getFormInstanceID(), u.getRefElementID(),
                    publicMethods.verifyString(u.getTitle()), u.getSequenceNumber(),
                    u.getType() == FormElementTypes.None ? null : u.getType().toString(),
                    u.getInfo(), publicMethods.verifyString(u.getTextValue()),
                    u.getFloatValue(), u.getBitValue(), u.getDateValue()));

        RVStructuredParam guidItemsDT = new RVStructuredParam("GuidPairTableType")
                .addColumnMetaData("FirstValue", UUID.class)
                .addColumnMetaData("SecondValue", UUID.class);
        elements.stream().filter(u -> u.getType() == FormElementTypes.Node || u.getType() == FormElementTypes.User)
                .forEach(x -> x.getGuidItems().forEach(y -> guidItemsDT.addRow(x.getElementID(), y.getKey())));

        RVStructuredParam clearDT = new RVStructuredParam("GuidTableType")
                .addColumnMetaData("Value", UUID.class);
        elementsToClear.forEach(clearDT::addRow);

        RVStructuredParam filesDT = new RVStructuredParam("DocFileInfoTableType")
                .addColumnMetaData("FileID", UUID.class)
                .addColumnMetaData("FileName", String.class)
                .addColumnMetaData("Extension", String.class)
                .addColumnMetaData("MIME", String.class)
                .addColumnMetaData("Size", Long.class)
                .addColumnMetaData("OwnerID", UUID.class)
                .addColumnMetaData("OwnerType", String.class);
        elements.stream().filter(u -> u.getAttachedFiles() != null).forEach(x -> x.getAttachedFiles()
                .forEach(y -> filesDT.addRow(y.getFileID(), y.getFileName(), y.getExtension(),
                        y.MIME(), y.getSize(), y.getOwnerID(), y.getOwnerType())));

        return rvConnection.succeed(getQualifiedName("SaveFormInstanceElements"), applicationId,
                elementsDT, guidItemsDT, clearDT, filesDT, currentUserId, publicMethods.now());
    }

    public boolean saveFormInstanceElements(UUID applicationId, List<FormElement> elements,
                                            List<UUID> elementsToClear, UUID currentUserId) {
        StringBuilder error = new StringBuilder();
        return saveFormInstanceElements(applicationId, elements, elementsToClear, currentUserId);
    }

    public boolean saveFormInstanceElement(UUID applicationId, FormElement element, UUID currentUserId) {
        List<FormElement> items = new ArrayList<FormElement>() {{
            add(element);
        }};

        return saveFormInstanceElements(applicationId, items, new ArrayList<>(), currentUserId);
    }

    public List<FormElement> getFormInstanceElements(UUID applicationId, UUID instanceId,
                                                     List<UUID> elementIds, Boolean filled) {
        return parser.formInstanceElements(rvConnection.read(getQualifiedName("GetFormInstanceElements"),
                applicationId, instanceId, filled,
                elementIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ','));
    }

    public List<FormElement> getFormInstanceElements(UUID applicationId, UUID instanceId, List<UUID> elementIds) {
        return getFormInstanceElements(applicationId, instanceId, elementIds, null);
    }

    public List<FormElement> getFormInstanceElements(UUID applicationId, UUID instanceId, Boolean filled) {
        return getFormInstanceElements(applicationId, instanceId, new ArrayList<>(), filled);
    }

    public List<FormElement> getFormInstanceElements(UUID applicationId, UUID instanceId) {
        return getFormInstanceElements(applicationId, instanceId, new ArrayList<>(), null);
    }

    public Map<UUID, List<Map.Entry<UUID, String>>> getSelectedGuids(UUID applicationId, List<UUID> elementIds) {
        return parser.selectedGuids(rvConnection.read(getQualifiedName("GetSelectedGuids"), applicationId,
                elementIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ','));
    }

    public List<FormElement> getElementChanges(UUID applicationId, UUID elementId, Integer count, Integer lowerBoundary) {
        return parser.elementChanges(rvConnection.read(getQualifiedName("GetElementChanges"),
                applicationId, elementId, count, lowerBoundary));
    }

    public boolean setFormInstanceAsFilled(UUID applicationId, UUID instanceId, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("SetFormInstanceAsFilled"),
                applicationId, instanceId, publicMethods.now(), currentUserId);
    }

    public boolean setFormInstanceAsNotFilled(UUID applicationId, UUID instanceId, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("SetFormInstanceAsNotFilled"), applicationId, instanceId, currentUserId);
    }

    public boolean isDirector(UUID applicationId, UUID instanceId, UUID userId) {
        return rvConnection.succeed(getQualifiedName("IsDirector"), applicationId, instanceId, userId);
    }

    public boolean setFormOwner(UUID applicationId, UUID ownerId, UUID formId, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("SetFormOwner"),
                applicationId, ownerId, formId, currentUserId, publicMethods.now());
    }

    public boolean removeFormOwner(UUID applicationId, UUID ownerId, UUID formId, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("ArithmeticDeleteFormOwner"),
                applicationId, ownerId, formId, currentUserId, publicMethods.now());
    }

    public FormType getOwnerForm(UUID applicationId, UUID ownerId) {
        List<FormType> lst = parser.formTypes(rvConnection.read(getQualifiedName("GetOwnerForm"), applicationId, ownerId));
        return lst.size() > 0 ? lst.get(0) : null;
    }

    public UUID initializeOwnerFormInstance(UUID applicationId, UUID ownerId, UUID currentUserId) {
        return rvConnection.getUUID(getQualifiedName("InitializeOwnerFormInstance"),
                applicationId, ownerId, currentUserId, publicMethods.now());
    }

    public boolean setElementLimits(UUID applicationId, UUID ownerId, List<UUID> elementIds, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("SetElementLimits"), applicationId, ownerId,
                elementIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                currentUserId, publicMethods.now());
    }

    public List<FormElement> getElementLimits(UUID applicationId, UUID ownerId) {
        return parser.elementLimits(rvConnection.read(getQualifiedName("GetElementLimits"), applicationId, ownerId));
    }

    public boolean setElementLimitNecessity(UUID applicationId, UUID ownerId,
                                            UUID elementId, boolean necessary, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("SetElementLimitNecessity"),
                applicationId, ownerId, elementId, necessary, currentUserId, publicMethods.now());
    }

    public boolean removeElementLimit(UUID applicationId, UUID ownerId, UUID elementId, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("ArithmeticDeleteElementLimit"),
                applicationId, ownerId, elementId, currentUserId, publicMethods.now());
    }

    public List<UUID> getCommonFormInstanceIDs(UUID applicationId, UUID ownerId, UUID filledOwnerId, boolean hasLimit) {
        return rvConnection.getUUIDList(getQualifiedName("GetCommonFormInstanceIDs"),
                applicationId, ownerId, filledOwnerId, hasLimit);
    }

    public List<FormRecord> getFormRecords(UUID applicationId, UUID formId, List<UUID> elementIds,
                                           List<UUID> instanceIds, List<UUID> ownerIds, List<FormFilter> filters,
                                           Integer lowerBoundary, Integer count, Integer sortByElementId, Boolean descending) {
        List<FormElement> elements = getFormElements(applicationId, formId);

        if (elementIds != null && elementIds.size() > 0) {
            final List<FormElement> els = elements;

            elementIds = elementIds.stream()
                    .filter(u -> els.stream().anyMatch(v -> v.getElementID() == u)).collect(Collectors.toList());

            final List<UUID> elIds = elementIds;

            elements = elements.stream().filter(u -> elIds.stream().anyMatch(v -> v == u.getElementID()))
                    .sorted((a, b) -> a.getSequenceNumber() < b.getSequenceNumber() ? -1 : 1).collect(Collectors.toList());
        }

        RVStructuredParam elementIdsDT = new RVStructuredParam("GuidTableType")
                .addColumnMetaData("Value", UUID.class);
        elementIds.forEach(elementIdsDT::addRow);

        RVStructuredParam instanceIdsDT = new RVStructuredParam("GuidTableType")
                .addColumnMetaData("Value", UUID.class);
        instanceIds.forEach(instanceIdsDT::addRow);

        RVStructuredParam ownerIdsDT = new RVStructuredParam("GuidTableType")
                .addColumnMetaData("Value", UUID.class);
        ownerIds.forEach(ownerIdsDT::addRow);

        RVStructuredParam filtersDT = new RVStructuredParam("FormFilterTableType")
                .addColumnMetaData("ElementID", UUID.class)
                .addColumnMetaData("OwnerID", UUID.class)
                .addColumnMetaData("Text", String.class)
                .addColumnMetaData("TextItems", String.class)
                .addColumnMetaData("Or", Boolean.class)
                .addColumnMetaData("Exact", Boolean.class)
                .addColumnMetaData("DateFrom", DateTime.class)
                .addColumnMetaData("DateTo", DateTime.class)
                .addColumnMetaData("FloatFrom", Double.class)
                .addColumnMetaData("FloatTo", Double.class)
                .addColumnMetaData("Bit", Boolean.class)
                .addColumnMetaData("Guid", UUID.class)
                .addColumnMetaData("GuidItems", String.class);
        filters.forEach(u -> filtersDT.addRow(u.getElementID(), u.getOwnerID(), u.getText(),
                String.join(",", u.getTextItems()), u.getOr(), u.getExact(), u.getDateFrom(), u.getDateTo(),
                u.getFloatFrom(), u.getFloatTo(), u.getBit(), u.getGuid(),
                u.getGuidItems().stream().map(UUID::toString).collect(Collectors.joining(","))));

        return parser.formRecords(rvConnection.read(getQualifiedName("GetFormRecords"), applicationId, formId,
                elementIdsDT, instanceIdsDT, ownerIdsDT, filtersDT, lowerBoundary, count, sortByElementId, descending), elements);
    }

    public List<FormRecord> getFormRecords(UUID applicationId, UUID formId, List<FormFilter> filters,
                                           Integer lowerBoundary, Integer count, Integer sortByElementId, Boolean descending) {
        return getFormRecords(applicationId, formId, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                filters, lowerBoundary, count, sortByElementId, descending);
    }

    public List<FormRecord> getFormRecords(UUID applicationId, UUID formId,
                                           Integer lowerBoundary, Integer count, Integer sortByElementId, Boolean descending) {
        return getFormRecords(applicationId, formId, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), lowerBoundary, count, sortByElementId, descending);
    }

    public void getFormStatistics(UUID applicationId, UUID ownerId, UUID instanceId, MutableDouble weightSum,
                                  MutableDouble sum, MutableDouble weightedSum, MutableDouble avg, MutableDouble weightedAvg,
                                  MutableDouble min, MutableDouble max, MutableDouble var, MutableDouble stDev) {
        parser.formStatistics(rvConnection.read(getQualifiedName("GetFormStatistics"), applicationId, ownerId, instanceId),
                weightSum, sum, weightedSum, avg, weightedAvg, min, max, var, stDev);
    }

    public boolean convertFormToTable(UUID applicationId, UUID formId) {
        return rvConnection.succeed(getQualifiedName("ConvertFormToTable"), applicationId, formId);
    }

    //Polls

    public List<Poll> getPolls(UUID applicationId, UUID isCopyOfPollId, UUID ownerId,
                               Boolean archive, String searchText, Integer count, Long lowerBoundary) {
        return parser.polls(rvConnection.read(getQualifiedName("GetPolls"), applicationId, isCopyOfPollId,
                ownerId, archive, rvConnection.getSearchText(searchText), count, lowerBoundary));
    }

    public List<Poll> getPolls(UUID applicationId, List<UUID> pollIds) {
        return parser.polls(rvConnection.read(getQualifiedName("GetPollsByIDs"), applicationId,
                pollIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ','));
    }

    public Poll getPolls(UUID applicationId, UUID pollId) {
        List<UUID> ids = new ArrayList<UUID>() {{
            add(pollId);
        }};

        return getPolls(applicationId, ids).stream().findFirst().orElse(null);
    }

    public boolean addPoll(UUID applicationId, UUID pollId, UUID copyFromPollId,
                               UUID ownerId, String name, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("AddPoll"),
                applicationId, pollId, copyFromPollId, ownerId, name, currentUserId, publicMethods.now());
    }

    public UUID getPollInstance(UUID applicationId, UUID pollId, UUID copyFromPollId, UUID ownerId, UUID currentUserId) {
        return rvConnection.getUUID(getQualifiedName("GetPollInstance"),
                applicationId, pollId, copyFromPollId, ownerId, currentUserId, publicMethods.now());
    }

    public List<UUID> getOwnerPollIDs(UUID applicationId, UUID isCopyOfPollId, UUID ownerId) {
        return rvConnection.getUUIDList(getQualifiedName("GetOwnerPollIDs"), applicationId, isCopyOfPollId, ownerId);
    }

    public boolean renamePoll(UUID applicationId, UUID pollId, String name, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("RenamePoll"),
                applicationId, pollId, name, currentUserId, publicMethods.now());
    }

    public boolean setPollDescription(UUID applicationId, UUID pollId, String description, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("SetPollDescription"),
                applicationId, pollId, description, currentUserId, publicMethods.now());
    }

    public boolean setPollBeginDate(UUID applicationId, UUID pollId, DateTime beginDate, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("SetPollBeginDate"),
                applicationId, pollId, beginDate, currentUserId, publicMethods.now());
    }

    public boolean setPollFinishDate(UUID applicationId, UUID pollId, DateTime finishDate, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("SetPollFinishDate"),
                applicationId, pollId, finishDate, currentUserId, publicMethods.now());
    }

    public boolean setPollShowSummary(UUID applicationId, UUID pollId, boolean showSummary, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("SetPollShowSummary"),
                applicationId, pollId, showSummary, currentUserId, publicMethods.now());
    }

    public boolean setPollHideContributors(UUID applicationId, UUID pollId, boolean hideContributors, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("SetPollHideContributors"),
                applicationId, pollId, hideContributors, currentUserId, publicMethods.now());
    }

    public boolean removePoll(UUID applicationId, UUID pollId, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("RemovePoll"), applicationId, pollId, currentUserId, publicMethods.now());
    }

    public boolean recyclePoll(UUID applicationId, UUID pollId, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("RecyclePoll"), applicationId, pollId, currentUserId, publicMethods.now());
    }

    public void getPollStatus(UUID applicationId, UUID pollId, UUID isCopyOfPollId, UUID currentUserId,
                              StringBuilder description, MutableDateTime beginDate, MutableDateTime finishDate,
                              MutableUUID instanceId, MutableInt elementsCount, MutableInt filledElementsCount,
                              MutableInt allFilledFormsCount) {
        parser.pollStatus(rvConnection.read(getQualifiedName("GetPollStatus"),
                applicationId, pollId, isCopyOfPollId, currentUserId), description, beginDate, finishDate,
                instanceId, elementsCount, filledElementsCount, allFilledFormsCount);
    }

    public Map<UUID, Integer> getPollElementsInstanceCount(UUID applicationId, UUID pollId) {
        return rvConnection.getItemsCount(getQualifiedName("GetPollElementsInstanceCount"), applicationId, pollId);
    }

    public List<PollAbstract> getPollAbstractText(UUID applicationId, UUID pollId,
                                                  List<UUID> elementIds, Integer count, Integer lowerBoundary) {
        return parser.pollAbstract(rvConnection.read(getQualifiedName("GetPollAbstractText"), applicationId,
                pollId, elementIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                count, lowerBoundary));
    }

    public List<PollAbstract> getPollAbstractGuid(UUID applicationId, UUID pollId,
                                                  List<UUID> elementIds, Integer count, Integer lowerBoundary) {
        return parser.pollAbstract(rvConnection.read(getQualifiedName("GetPollAbstractGuid"), applicationId,
                pollId, elementIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                count, lowerBoundary));
    }

    public List<PollAbstract> getPollAbstractBool(UUID applicationId, UUID pollId, List<UUID> elementIds) {
        return parser.pollAbstract(rvConnection.read(getQualifiedName("GetPollAbstractBool"), applicationId,
                pollId, elementIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ','));
    }

    public List<PollAbstract> getPollAbstractNumber(UUID applicationId, UUID pollId,
                                                    List<UUID> elementIds, Integer count, Integer lowerBoundary) {
        return parser.pollAbstract(rvConnection.read(getQualifiedName("GetPollAbstractNumber"), applicationId,
                pollId, elementIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                count, lowerBoundary));
    }

    public List<FormElement> getPollElementInstances(UUID applicationId, UUID pollId,
                                                     UUID elementId, Integer count, Integer lowerBoundary) {
        return parser.pollElementInstances(rvConnection.read(getQualifiedName("GetPollElementInstances"),
                applicationId, pollId, elementId, count, lowerBoundary));
    }

    public void getCurrentPollsCount(UUID applicationId, UUID currentUserId, MutableInt count, MutableInt doneCount) {
        parser.currentPollsCount(rvConnection.read(getQualifiedName("GetCurrentPollsCount"),
                applicationId, currentUserId, publicMethods.now(), raaivanSettings.DefaultPrivacy(applicationId)),
                count, doneCount);
    }

    public Map<UUID, Boolean> getCurrentPolls(UUID applicationId, UUID currentUserId, MutableInt count, MutableInt lowerBoundary) {
        return rvConnection.getItemsStatusBool(getQualifiedName("GetCurrentPolls"), applicationId, currentUserId,
                publicMethods.now(), raaivanSettings.DefaultPrivacy(applicationId), count, lowerBoundary);
    }

    public List<UUID> isPoll(UUID applicationId, List<UUID> ids) {
        return rvConnection.getUUIDList(getQualifiedName("IsPoll"), applicationId,
                ids.stream().map(UUID::toString).collect(Collectors.joining(",")), ',');
    }

    public boolean isPoll(UUID applicationId, UUID id) {
        List<UUID> ids = new ArrayList<UUID>() {{
            add(id);
        }};

        return isPoll(applicationId, ids).size() > 0;
    }

    //end of Polls
}