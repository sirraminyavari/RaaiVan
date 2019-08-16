package com.raaivan.modules.forms;

import com.raaivan.modules.forms.beans.FormElement;
import com.raaivan.modules.forms.beans.FormType;
import com.raaivan.modules.forms.enums.FormElementTypes;
import com.raaivan.util.PublicMethods;
import com.raaivan.util.dbutil.RVConnection;
import com.raaivan.util.dbutil.RVStructuredParam;
import io.micrometer.core.instrument.util.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.security.cert.CollectionCertStoreParameters;
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

    @Autowired
    public void _setDependencies(RVConnection rvConnection, FGParsers parser, PublicMethods publicMethods) {
        if (this.rvConnection == null) this.rvConnection = rvConnection;
        if (this.parser == null) this.parser = parser;
        if (this.publicMethods == null) this.publicMethods = publicMethods;
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

    public List<FormType> getForms(UUID applicationId, List<UUID> formIds) {
        return parser.formTypes(rvConnection.read(getQualifiedName("GetFormsByIDs"), applicationId,
                formIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ','));
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

    public List<FormElement> getFormElements(UUID applicationId, List<UUID> elementIds) {
        return parser.formElements(rvConnection.read(getQualifiedName("GetFormElementsByIDs"), applicationId,
                elementIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ','));
    }

    public List<UUID> isFormElement(UUID applicationId, List<UUID> ids) {
        return rvConnection.getUUIDList(getQualifiedName("IsFormElement"), applicationId,
                ids.stream().map(UUID::toString).collect(Collectors.joining(",")), ',');
    }

    public boolean createFormInstance(UUID applicationId, List<FormType> instances, UUID currentUserId) {
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

    public boolean removeFormInstances(UUID applicationId, List<UUID> instanceIds, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("RemoveFormInstances"), applicationId,
                instanceIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                currentUserId, publicMethods.now());
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

    public List<FormType> getFormInstances(UUID applicationId, List<UUID> instanceIds) {
        return parser.formInstances(rvConnection.read(getQualifiedName("GetFormInstances"), applicationId,
                instanceIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ','));
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

    public List<FormElement> getFormInstanceElements(UUID applicationId, UUID instanceId,
                                                     List<UUID> elementIds, Boolean filled) {
        return parser.formInstanceElements(rvConnection.read(getQualifiedName("GetFormInstanceElements"),
                applicationId, instanceId, filled,
                elementIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ','));
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

    public static bool SetFormOwner(Guid applicationId, Guid ownerId, Guid formId, Guid currentUserId) {
        string spName = GetFullyQualifiedName("SetFormOwner");

        try {
            return ProviderUtil.succeed(ProviderUtil.execute_reader(spName, applicationId,
                    ownerId, formId, currentUserId, DateTime.Now));
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
            return false;
        }
    }

    public static bool ArithmeticDeleteFormOwner(Guid applicationId,
                                                 Guid ownerId, Guid formId, Guid currentUserId) {
        string spName = GetFullyQualifiedName("ArithmeticDeleteFormOwner");

        try {
            return ProviderUtil.succeed(ProviderUtil.execute_reader(spName, applicationId,
                    ownerId, formId, currentUserId, DateTime.Now));
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
            return false;
        }
    }

    public static FormType GetOwnerForm(Guid applicationId, Guid ownerId) {
        string spName = GetFullyQualifiedName("GetOwnerForm");

        try {
            IDataReader reader = ProviderUtil.execute_reader(spName, applicationId, ownerId);
            List<FormType> forms = new List<FormType>();
            _parse_form_types(ref reader, ref forms);
            return forms.FirstOrDefault();
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
            return null;
        }
    }

    public static Guid?

    InitializeOwnerFormInstance(Guid applicationId, Guid ownerId, Guid currentUserId) {
        string spName = GetFullyQualifiedName("InitializeOwnerFormInstance");

        try {
            Guid ? instanceId = ProviderUtil.succeed_guid(ProviderUtil.execute_reader(spName, applicationId,
                    ownerId, currentUserId, DateTime.Now));

            if (instanceId == Guid.Empty) instanceId = null;

            return instanceId;
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
            return null;
        }
    }

    public static bool SetElementLimits(Guid applicationId,
                                        Guid ownerId, ref List<Guid>elementIds, Guid currentUserId) {
        string spName = GetFullyQualifiedName("SetElementLimits");

        try {
            return ProviderUtil.succeed(ProviderUtil.execute_reader(spName, applicationId,
                    ownerId, ProviderUtil.list_to_string < Guid > (ref elementIds), ',', currentUserId, DateTime.Now));
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
            return false;
        }
    }

    public static void GetElementLimits(Guid applicationId, ref List<FormElement>retElements, Guid ownerId) {
        string spName = GetFullyQualifiedName("GetElementLimits");

        try {
            IDataReader reader = ProviderUtil.execute_reader(spName, applicationId, ownerId);
            _parse_element_limits(ref reader, ref retElements);
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
        }
    }

    public static bool SetElementLimitNecessity(Guid applicationId,
                                                Guid ownerId, Guid elementId, bool necessary, Guid currentUserId) {
        string spName = GetFullyQualifiedName("SetElementLimitNecessity");

        try {
            return ProviderUtil.succeed(ProviderUtil.execute_reader(spName, applicationId,
                    ownerId, elementId, necessary, currentUserId, DateTime.Now));
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
            return false;
        }
    }

    public static bool ArithmeticDeleteElementLimit(Guid applicationId,
                                                    Guid ownerId, Guid elementId, Guid currentUserId) {
        string spName = GetFullyQualifiedName("ArithmeticDeleteElementLimit");

        try {
            return ProviderUtil.succeed(ProviderUtil.execute_reader(spName, applicationId,
                    ownerId, elementId, currentUserId, DateTime.Now));
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
            return false;
        }
    }

    public static void GetCommonFormInstanceIDs(Guid applicationId,
                                                ref List<Guid>retIds, Guid ownerId, Guid filledOwnerId, bool hasLimit) {
        string spName = GetFullyQualifiedName("GetCommonFormInstanceIDs");

        try {
            IDataReader reader = ProviderUtil.execute_reader(spName, applicationId,
                    ownerId, filledOwnerId, hasLimit);
            ProviderUtil.parse_guids(ref reader, ref retIds);
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
        }
    }

    public static void GetFormRecords(Guid applicationId, ref List<FormRecord>retRecords, Guid formId,
                                      List<Guid> elementIds, List<Guid> instanceIds, List<Guid> ownerIds, List<FormFilter> filters,
                                      int?lowerBoundary, int?count, Guid?sortByElementId, bool?descending) {
        List<FormElement> elements = FGController.get_form_elements(applicationId, formId);

        if (elementIds != null && elementIds.Count > 0) {
            elementIds = elementIds.Where(u = > elements.Any(v = > v.ElementID == u)).ToList();

            elements = elements.Where(u = > elementIds.Any(v = > v == u.ElementID))
                    .OrderBy(x = > x.SequenceNumber).ToList();
        }

        SqlConnection con = new SqlConnection(ProviderUtil.ConnectionString);
        SqlCommand cmd = new SqlCommand();
        cmd.Connection = con;

        if (sortByElementId == Guid.Empty) sortByElementId = null;

        //Add ElementIDs
        DataTable elementIdsTable = new DataTable();
        elementIdsTable.Columns.Add("Value", typeof(Guid));

        foreach(Guid eId in elementIds)
        elementIdsTable.Rows.Add(eId);

        SqlParameter elementIdsParam = new SqlParameter("@ElementIDs", SqlDbType.Structured);
        elementIdsParam.TypeName = "[dbo].[GuidTableType]";
        elementIdsParam.Value = elementIdsTable;
        //end of Add ElementIDs

        //Add InstanceIDs
        DataTable instanceIdsTable = new DataTable();
        instanceIdsTable.Columns.Add("Value", typeof(Guid));

        foreach(Guid iId in instanceIds)
        instanceIdsTable.Rows.Add(iId);

        SqlParameter instanceIdsParam = new SqlParameter("@InstanceIDs", SqlDbType.Structured);
        instanceIdsParam.TypeName = "[dbo].[GuidTableType]";
        instanceIdsParam.Value = instanceIdsTable;
        //end of Add InstanceIDs

        //Add OwnerIDs
        DataTable ownerIdsTable = new DataTable();
        ownerIdsTable.Columns.Add("Value", typeof(Guid));

        foreach(Guid oId in ownerIds)
        ownerIdsTable.Rows.Add(oId);

        SqlParameter ownerIdsParam = new SqlParameter("@OwnerIDs", SqlDbType.Structured);
        ownerIdsParam.TypeName = "[dbo].[GuidTableType]";
        ownerIdsParam.Value = ownerIdsTable;
        //end of Add OwnerIDs

        //Add Filters
        DataTable filtersTable = new DataTable();
        filtersTable.Columns.Add("ElementID", typeof(Guid));
        filtersTable.Columns.Add("OwnerID", typeof(Guid));
        filtersTable.Columns.Add("Text", typeof(string));
        filtersTable.Columns.Add("TextItems", typeof(string));
        filtersTable.Columns.Add("Or", typeof(bool));
        filtersTable.Columns.Add("Exact", typeof(bool));
        filtersTable.Columns.Add("DateFrom", typeof(DateTime));
        filtersTable.Columns.Add("DateTo", typeof(DateTime));
        filtersTable.Columns.Add("FloatFrom", typeof( double));
        filtersTable.Columns.Add("FloatTo", typeof( double));
        filtersTable.Columns.Add("Bit", typeof(bool));
        filtersTable.Columns.Add("Guid", typeof(Guid));
        filtersTable.Columns.Add("GuidItems", typeof(string));

        foreach(FormFilter f in filters)
        {
            filtersTable.Rows.Add(f.ElementID, f.OwnerID, f.Text, ProviderUtil.list_to_string < string > (f.TextItems),
                    f.Or, f.Exact, f.DateFrom, f.DateTo, f.FloatFrom, f.FloatTo, f.Bit, f.Guid,
                    ProviderUtil.list_to_string < Guid > (f.GuidItems));
        }

        SqlParameter filtersParam = new SqlParameter("@Filters", SqlDbType.Structured);
        filtersParam.TypeName = "[dbo].[FormFilterTableType]";
        filtersParam.Value = filtersTable;
        //end of Add Filters

        cmd.Parameters.AddWithValue("@ApplicationID", applicationId);
        cmd.Parameters.AddWithValue("@FormID", formId);
        cmd.Parameters.Add(elementIdsParam);
        cmd.Parameters.Add(instanceIdsParam);
        cmd.Parameters.Add(ownerIdsParam);
        cmd.Parameters.Add(filtersParam);
        if (lowerBoundary.HasValue) cmd.Parameters.AddWithValue("@LowerBoundary", lowerBoundary);
        if (count.HasValue) cmd.Parameters.AddWithValue("@Count", count);
        if (sortByElementId.HasValue) cmd.Parameters.AddWithValue("@SortByElementID", sortByElementId);
        if (descending.HasValue) cmd.Parameters.AddWithValue("@DESC", descending);

        string spName = GetFullyQualifiedName("GetFormRecords");

        string sep = ", ";
        string arguments = "@ApplicationID" + sep + "@FormID" + sep + "@ElementIDs" + sep +
                "@InstanceIDs" + sep + "@OwnerIDs" + sep + "@Filters" + sep +
                (!lowerBoundary.HasValue ? "null" : "@LowerBoundary") + sep +
                (!count.HasValue ? "null" : "@Count") + sep +
                (!sortByElementId.HasValue ? "null" : "@SortByElementID") + sep +
                (!descending.HasValue ? "null" : "@DESC");
        cmd.CommandText = ("EXEC" + " " + spName + " " + arguments);

        con.Open();
        try {
            IDataReader reader = (IDataReader) cmd.ExecuteReader();
            _parse_form_records(ref reader, ref retRecords, elements);
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
        } finally {
            con.Close();
        }
    }

    public static void GetFormStatistics(Guid applicationId, Guid?ownerId, Guid?instanceId,
                                         ref double weightSum, ref double sum, ref double weightedSum, ref double avg, ref double weightedAvg,
                                         ref double min, ref double max, ref double var, ref double stDev) {
        string spName = GetFullyQualifiedName("GetFormStatistics");

        try {
            if (ownerId == Guid.Empty) ownerId = null;
            if (instanceId == Guid.Empty) instanceId = null;

            IDataReader reader = ProviderUtil.execute_reader(spName, applicationId, ownerId, instanceId);
            _parse_form_statistics(ref reader, ref weightSum, ref sum, ref weightedSum, ref avg,
                    ref weightedAvg, ref min, ref max, ref var, ref stDev);
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
        }
    }

    public static bool ConvertFormToTable(Guid applicationId, Guid formId) {
        string spName = GetFullyQualifiedName("ConvertFormToTable");

        try {
            return ProviderUtil.succeed(ProviderUtil.execute_reader(spName, applicationId, formId));
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
            return false;
        }
    }

    //Polls

    public static void GetPolls(Guid applicationId, ref List<Poll>ret, Guid?isCopyOfPollId, Guid?ownerId,
                                bool?archive, string searchText, int?count, long?lowerBoundary) {
        string spName = GetFullyQualifiedName("GetPolls");

        try {
            if (isCopyOfPollId == Guid.Empty) isCopyOfPollId = null;
            if (ownerId == Guid.Empty) ownerId = null;

            IDataReader reader = ProviderUtil.execute_reader(spName, applicationId, isCopyOfPollId, ownerId,
                    archive, ProviderUtil.get_search_text(searchText), count, lowerBoundary);
            _parse_polls(ref reader, ref ret);
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
        }
    }

    public static void GetPolls(Guid applicationId, ref List<Poll>ret, List<Guid> pollIds) {
        string spName = GetFullyQualifiedName("GetPollsByIDs");

        try {
            IDataReader reader = ProviderUtil.execute_reader(spName, applicationId, string.Join(",", pollIds), ',');
            _parse_polls(ref reader, ref ret);
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
        }
    }

    public static bool AddPoll(Guid applicationId, Guid pollId, Guid?copyFromPollId,
                               Guid?ownerId, string name, Guid currentUserId) {
        string spName = GetFullyQualifiedName("AddPoll");

        try {
            if (string.IsNullOrEmpty(name)) name = null;
            if (copyFromPollId == Guid.Empty) copyFromPollId = null;
            if (ownerId == Guid.Empty) ownerId = null;

            return ProviderUtil.succeed(ProviderUtil.execute_reader(spName, applicationId,
                    pollId, copyFromPollId, ownerId, name, currentUserId, DateTime.Now));
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
            return false;
        }
    }

    public static Guid?

    GetPollInstance(Guid applicationId,
                    Guid?pollId, Guid copyFromPollId, Guid?ownerId, Guid currentUserId) {
        string spName = GetFullyQualifiedName("GetPollInstance");

        try {
            if (pollId == Guid.Empty) pollId = null;
            if (ownerId == Guid.Empty) ownerId = null;

            Guid ? result = ProviderUtil.succeed_guid(ProviderUtil.execute_reader(spName, applicationId,
                    pollId, copyFromPollId, ownerId, currentUserId, DateTime.Now));

            return result == Guid.Empty ? null : result;
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
            return null;
        }
    }

    public static void GetOwnerPollIDs(Guid applicationId, ref List<Guid>ret, Guid isCopyOfPollId, Guid ownerId) {
        string spName = GetFullyQualifiedName("GetOwnerPollIDs");

        try {
            IDataReader reader = ProviderUtil.execute_reader(spName, applicationId, isCopyOfPollId, ownerId);
            ProviderUtil.parse_guids(ref reader, ref ret);
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
        }
    }

    public static bool RenamePoll(Guid applicationId, Guid pollId, string name, Guid currentUserId) {
        string spName = GetFullyQualifiedName("RenamePoll");

        try {
            return ProviderUtil.succeed(ProviderUtil.execute_reader(spName, applicationId,
                    pollId, name, currentUserId, DateTime.Now));
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
            return false;
        }
    }

    public static bool SetPollDescription(Guid applicationId, Guid pollId, string description, Guid currentUserId) {
        string spName = GetFullyQualifiedName("SetPollDescription");

        try {
            if (string.IsNullOrEmpty(description)) description = null;

            return ProviderUtil.succeed(ProviderUtil.execute_reader(spName, applicationId,
                    pollId, description, currentUserId, DateTime.Now));
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
            return false;
        }
    }

    public static bool SetPollBeginDate(Guid applicationId, Guid pollId, DateTime?beginDate, Guid currentUserId) {
        string spName = GetFullyQualifiedName("SetPollBeginDate");

        try {
            return ProviderUtil.succeed(ProviderUtil.execute_reader(spName, applicationId,
                    pollId, beginDate, currentUserId, DateTime.Now));
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
            return false;
        }
    }

    public static bool SetPollFinishDate(Guid applicationId, Guid pollId, DateTime?finishDate, Guid currentUserId) {
        string spName = GetFullyQualifiedName("SetPollFinishDate");

        try {
            return ProviderUtil.succeed(ProviderUtil.execute_reader(spName, applicationId,
                    pollId, finishDate, currentUserId, DateTime.Now));
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
            return false;
        }
    }

    public static bool SetPollShowSummary(Guid applicationId, Guid pollId, bool showSummary, Guid currentUserId) {
        string spName = GetFullyQualifiedName("SetPollShowSummary");

        try {
            return ProviderUtil.succeed(ProviderUtil.execute_reader(spName, applicationId,
                    pollId, showSummary, currentUserId, DateTime.Now));
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
            return false;
        }
    }

    public static bool SetPollHideContributors(Guid applicationId, Guid pollId, bool hideContributors, Guid currentUserId) {
        string spName = GetFullyQualifiedName("SetPollHideContributors");

        try {
            return ProviderUtil.succeed(ProviderUtil.execute_reader(spName, applicationId,
                    pollId, hideContributors, currentUserId, DateTime.Now));
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
            return false;
        }
    }

    public static bool RemovePoll(Guid applicationId, Guid pollId, Guid currentUserId) {
        string spName = GetFullyQualifiedName("RemovePoll");

        try {
            return ProviderUtil.succeed(ProviderUtil.execute_reader(spName, applicationId,
                    pollId, currentUserId, DateTime.Now));
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
            return false;
        }
    }

    public static bool RecyclePoll(Guid applicationId, Guid pollId, Guid currentUserId) {
        string spName = GetFullyQualifiedName("RecyclePoll");

        try {
            return ProviderUtil.succeed(ProviderUtil.execute_reader(spName, applicationId,
                    pollId, currentUserId, DateTime.Now));
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
            return false;
        }
    }

    public static void GetPollStatus(Guid applicationId, Guid?pollId, Guid?isCopyOfPollId,
                                     Guid currentUserId, ref string description, ref DateTime?beginDate, ref DateTime?finishDate,
                                     ref Guid?instanceId, ref int?elementsCount, ref int?filledElementsCount, ref int?allFilledFormsCount) {
        string spName = GetFullyQualifiedName("GetPollStatus");

        try {
            if (pollId == Guid.Empty) pollId = null;
            if (isCopyOfPollId == Guid.Empty) isCopyOfPollId = null;

            IDataReader reader = ProviderUtil.execute_reader(spName, applicationId, pollId, isCopyOfPollId, currentUserId);
            _parse_poll_status(ref reader, ref description, ref beginDate, ref finishDate, ref instanceId,
                    ref elementsCount, ref filledElementsCount, ref allFilledFormsCount);
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
        }
    }

    public static Dictionary<Guid, int> GetPollElementsInstanceCount(Guid applicationId, Guid pollId) {
        string spName = GetFullyQualifiedName("GetPollElementsInstanceCount");

        try {
            IDataReader reader = ProviderUtil.execute_reader(spName, applicationId, pollId);
            return ProviderUtil.parse_items_count(ref reader);
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
            return new Dictionary<Guid, int>();
        }
    }

    public static void GetPollAbstractText(Guid applicationId, ref List<PollAbstract>ret,
                                           Guid pollId, List<Guid> elementIds, int?count, int?lowerBoundary) {
        string spName = GetFullyQualifiedName("GetPollAbstractText");

        try {
            IDataReader reader = ProviderUtil.execute_reader(spName, applicationId, pollId,
                    string.Join(",", elementIds), ',', count, lowerBoundary);
            _parse_poll_abstract(ref reader, ref ret);
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
        }
    }

    public static void GetPollAbstractGuid(Guid applicationId, ref List<PollAbstract>ret,
                                           Guid pollId, List<Guid> elementIds, int?count, int?lowerBoundary) {
        string spName = GetFullyQualifiedName("GetPollAbstractGuid");

        try {
            IDataReader reader = ProviderUtil.execute_reader(spName, applicationId, pollId,
                    string.Join(",", elementIds), ',', count, lowerBoundary);
            _parse_poll_abstract(ref reader, ref ret);
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
        }
    }

    public static void GetPollAbstractBool(Guid applicationId, ref List<PollAbstract>ret,
                                           Guid pollId, List<Guid> elementIds) {
        string spName = GetFullyQualifiedName("GetPollAbstractBool");

        try {
            IDataReader reader = ProviderUtil.execute_reader(spName, applicationId, pollId,
                    string.Join(",", elementIds), ',');
            _parse_poll_abstract(ref reader, ref ret);
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
        }
    }

    public static void GetPollAbstractNumber(Guid applicationId, ref List<PollAbstract>ret,
                                             Guid pollId, List<Guid> elementIds, int?count, int?lowerBoundary) {
        string spName = GetFullyQualifiedName("GetPollAbstractNumber");

        try {
            IDataReader reader = ProviderUtil.execute_reader(spName, applicationId, pollId,
                    string.Join(",", elementIds), ',', count, lowerBoundary);
            _parse_poll_abstract(ref reader, ref ret);
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
        }
    }

    public static void GetPollElementInstances(Guid applicationId, ref List<FormElement>ret,
                                               Guid pollId, Guid elementId, int?count, int?lowerBoundary) {
        string spName = GetFullyQualifiedName("GetPollElementInstances");

        try {
            IDataReader reader = ProviderUtil.execute_reader(spName, applicationId,
                    pollId, elementId, count, lowerBoundary);
            _parse_poll_element_instances(ref reader, ref ret);
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
        }
    }

    public static void GetCurrentPollsCount(Guid applicationId,
                                            Guid?currentUserId, ref int count, ref int doneCount) {
        string spName = GetFullyQualifiedName("GetCurrentPollsCount");

        try {
            IDataReader reader = ProviderUtil.execute_reader(spName, applicationId,
                    currentUserId, DateTime.Now, RaaiVanSettings.DefaultPrivacy(applicationId));
            _parse_current_polls_count(ref reader, ref count, ref doneCount);
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
        }
    }

    public static Dictionary<Guid, bool> GetCurrentPolls(Guid applicationId,
                                                         Guid?currentUserId, int?count, int?lowerBoundary) {
        string spName = GetFullyQualifiedName("GetCurrentPolls");

        try {
            IDataReader reader = ProviderUtil.execute_reader(spName, applicationId,
                    currentUserId, DateTime.Now, RaaiVanSettings.DefaultPrivacy(applicationId), count, lowerBoundary);
            return ProviderUtil.parse_items_status_bool(ref reader);
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
            return new Dictionary<Guid, bool>();
        }
    }

    public static List<Guid> IsPoll(Guid applicationId, List<Guid> ids) {
        string spName = GetFullyQualifiedName("IsPoll");

        try {
            IDataReader reader = ProviderUtil.execute_reader(spName, applicationId,
                    ProviderUtil.list_to_string < Guid > (ids), ',');
            List<Guid> ret = new List<Guid>();
            ProviderUtil.parse_guids(ref reader, ref ret);
            return ret;
        } catch (Exception ex) {
            LogController.save_error_log(applicationId, null, spName, ex, ModuleIdentifier.FG);
            return new List<Guid>();
        }
    }

    //end of Polls
}