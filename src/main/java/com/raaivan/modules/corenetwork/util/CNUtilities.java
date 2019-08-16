package com.raaivan.modules.corenetwork.util;

import com.raaivan.modules.corenetwork.CNDAO;
import com.raaivan.modules.corenetwork.beans.Extension;
import com.raaivan.modules.corenetwork.beans.NodeMember;
import com.raaivan.modules.corenetwork.beans.NodeType;
import com.raaivan.modules.corenetwork.beans.NodesCount;
import com.raaivan.modules.corenetwork.enums.ExtensionType;
import com.raaivan.modules.rv.beans.Expressions;
import com.raaivan.util.PublicMethods;
import com.raaivan.util.RVBeanFactory;
import com.raaivan.util.RaaiVanConfig;
import com.raaivan.util.rvsettings.RaaiVanSettings;
import io.micrometer.core.instrument.util.StringUtils;
import ir.huri.jcal.JalaliCalendar;
import org.apache.commons.lang3.mutable.MutableInt;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.*;

@Component
@ApplicationScope
public class CNUtilities {
    private PublicMethods publicMethods;
    private RaaiVanSettings raaivanSettings;
    private CNDAO cnDao;

    @Autowired
    public void _setDependencies(PublicMethods publicMethods, RaaiVanSettings raaivanSettings, CNDAO cnDao) {
        if (this.publicMethods == null) this.publicMethods = publicMethods;
        if (this.raaivanSettings == null) this.raaivanSettings = raaivanSettings;
        if (this.cnDao == null) this.cnDao = cnDao;
    }

    public boolean validateAdditionalIdPattern(String pattern)
    {
        return StringUtils.isBlank(pattern) ||
                (pattern.length() <= 250 && Expressions.isMatch(pattern, Expressions.Patterns.AdditionalID));
    }

    private String _generateNewAdditionalId(UUID applicationId, UUID currentUserId, UUID nodeTypeId, String additionalIdPattern,
                                            boolean getPattern, DateTime time, Map<String, String> dic)
    {
        String defaultAdditionalIdPattern = raaivanSettings.getCoreNetwork().DefaultAdditionalIDPattern(applicationId);

        if (getPattern)
        {
            NodeType nt = nodeTypeId != null ? cnDao.getNodeType(applicationId, nodeTypeId) : null;
            additionalIdPattern = nt == null || StringUtils.isBlank(nt.getAdditionalIDPattern()) ?
                    defaultAdditionalIdPattern : nt.getAdditionalIDPattern();
        }

        List<String> tags = Expressions.getExistingTags(additionalIdPattern, Expressions.Patterns.AutoTag);
        if (tags == null || tags.size() == 0)
            return StringUtils.isBlank(additionalIdPattern) ? null : additionalIdPattern;

        dic = new HashMap<>();

        DateTime now = time != null ? time : publicMethods.now();
        JalaliCalendar pcal = publicMethods.getJalaliCalendar(now);

        DateTime lowerPersianDateLimit = publicMethods.persianToGregorianDate(pcal.getYear(), 1, 1);
        DateTime upperPersianDateLimit = null;
        DateTime lowerGregorianDateLimit = new DateTime(now.getYear(), 1, 1, 0, 0, 0);
        DateTime upperGregorianDateLimit = lowerGregorianDateLimit.plusYears(1).minusMillis(1);

        if (lowerPersianDateLimit != null) upperPersianDateLimit = lowerPersianDateLimit.plusYears(1).minusMillis(1);

        for (String _str : tags)
        {
            MutableInt num = new MutableInt(0);
            int ind = _str.length();
            while (ind > 0 && publicMethods.tryParseInt(Character.toString(_str.charAt(ind - 1)), num)) ind -= 1;
            num.setValue(ind < _str.length() ? publicMethods.parseInt(_str.substring(ind)) : 0);
            String _tg = _str.substring(0, ind).toLowerCase();

            switch (_tg)
            {
                case "pyear":
                    dic.put(_str, Integer.toString(pcal.getYear()));
                    break;
                case "pyears":
                    dic.put(_str, Integer.toString(pcal.getYear()).substring(2));
                    break;
                case "pmonth":
                    dic.put(_str, Integer.toString(pcal.getMonth()));
                    break;
                case "pday":
                    dic.put(_str, Integer.toString(pcal.getDay()));
                    break;
                case "gyear":
                    dic.put(_str, Integer.toString(now.getYear()));
                    break;
                case "gyears":
                    dic.put(_str, Integer.toString(now.getYear()).substring(2));
                    break;
                case "gmonth":
                    dic.put(_str, Integer.toString(now.getMonthOfYear()));
                    break;
                case "gday":
                    dic.put(_str, Integer.toString(now.getDayOfMonth()));
                    break;
                case "rnd":
                    dic.put(_str, Integer.toString(publicMethods.getRandomNumber(num.getValue() > 0 ? num.getValue() : 3)));
                    break;
                case "ncount":
                    dic.put(_str, publicMethods.fitNumberToSize(cnDao.getNodesCount(applicationId, null)
                            .stream().mapToInt(NodesCount::getCount).sum() + 1, num.getValue()));
                    break;
                case "ncountpy":
                    dic.put(_str, publicMethods.fitNumberToSize(cnDao.getNodesCount(applicationId,
                            lowerPersianDateLimit, upperPersianDateLimit, null)
                            .stream().mapToInt(NodesCount::getCount).sum() + 1, num.getValue()));
                    break;
                case "ncountgy":
                    dic.put(_str, publicMethods.fitNumberToSize(cnDao.getNodesCount(applicationId,
                            lowerGregorianDateLimit, upperGregorianDateLimit, null)
                            .stream().mapToInt(NodesCount::getCount).sum() + 1, num.getValue()));
                    break;
                case "ncounts":
                    NodesCount ntc = nodeTypeId == null ? null :
                            cnDao.getNodesCount(applicationId, nodeTypeId, null);
                    dic.put(_str, publicMethods.fitNumberToSize((ntc == null ? 0 : ntc.getCount()) + 1, num.getValue()));
                    break;
                case "ncountspy":
                    NodesCount ntcp = nodeTypeId == null ? null : cnDao.getNodesCount(applicationId,
                            nodeTypeId, lowerPersianDateLimit, upperPersianDateLimit, null);
                    dic.put(_str, publicMethods.fitNumberToSize((ntcp == null ? 0 : ntcp.getCount()) + 1, num.getValue()));
                    break;
                case "ncountsgy":
                    NodesCount ntcg = nodeTypeId == null ? null : cnDao.getNodesCount(applicationId,
                            nodeTypeId, lowerGregorianDateLimit, upperGregorianDateLimit, null);
                    dic.put(_str, publicMethods.fitNumberToSize((ntcg == null ? 0 : ntcg.getCount()) + 1, num.getValue()));
                    break;
                case "depid":
                    NodeMember dep = null; // CNController.get_member_nodes(applicationId, currentUserId, NodeTypes.Department).FirstOrDefault();
                    dic.put(_str, dep == null || StringUtils.isBlank(dep.getNode().getAdditionalID()) ? "0" : dep.getNode().getAdditionalID());
                    break;
            }
        } //end of 'foreach (string _str in tags)'

        return Expressions.replace(additionalIdPattern, dic, Expressions.Patterns.AutoTag, null);
    }

    public String generateNewAdditionalId(UUID applicationId, UUID currentUserId, DateTime time)
    {
        Map<String, String> dic = new HashMap<>();
        return _generateNewAdditionalId(applicationId, currentUserId, null, null, true, time, dic);
    }

    public String generateNewAdditionalId(UUID applicationId, UUID currentUserId) {
        return generateNewAdditionalId(applicationId, currentUserId, (DateTime) null);
    }

    public String generateNewAdditionalId(UUID applicationId, UUID currentUserId, UUID nodeTypeId, DateTime time)
    {
        Map<String, String> dic = new HashMap<>();
        return _generateNewAdditionalId(applicationId, currentUserId, nodeTypeId, null, true, time, dic);
    }

    public String generateNewAdditionalId(UUID applicationId, UUID currentUserId, UUID nodeTypeId) {
        return generateNewAdditionalId(applicationId, currentUserId, nodeTypeId, (DateTime) null);
    }

    public String generateNewAdditionalId(UUID applicationId, UUID currentUserId, String additionalIdPattern, DateTime time)
    {
        Map<String, String> dic = new HashMap<>();
        return _generateNewAdditionalId(applicationId, currentUserId, null, additionalIdPattern, false, time, dic);
    }

    public String generateNewAdditionalId(UUID applicationId, UUID currentUserId, String additionalIdPattern)
    {
        return generateNewAdditionalId(applicationId, currentUserId, additionalIdPattern, (DateTime) null);
    }

    public String generateNewAdditionalId(UUID applicationId, UUID currentUserId,
                                          UUID nodeTypeId, String additionalIdPattern, DateTime time)
    {
        Map<String, String> dic = new HashMap<>();
        return _generateNewAdditionalId(applicationId, currentUserId, nodeTypeId, additionalIdPattern, false, time, dic);
    }

    public String generateNewAdditionalId(UUID applicationId, UUID currentUserId, UUID nodeTypeId, String additionalIdPattern)
    {
        return generateNewAdditionalId(applicationId, currentUserId, nodeTypeId, additionalIdPattern, (DateTime) null);
    }

    public String generateNewAdditionalId(UUID applicationId, UUID currentUserId, UUID nodeTypeId,
                                          String additionalIdPattern, Map<String, String> dic, DateTime time)
    {
        return _generateNewAdditionalId(applicationId, currentUserId, nodeTypeId, additionalIdPattern, false, time, dic);
    }

    public String generateNewAdditionalId(UUID applicationId, UUID currentUserId, UUID nodeTypeId,
                                          String additionalIdPattern, Map<String, String> dic)
    {
        return generateNewAdditionalId(applicationId, currentUserId, nodeTypeId, additionalIdPattern, dic, (DateTime) null);
    }

    public List<ExtensionType> getExtensionTypes() {
        List<ExtensionType> types = new ArrayList<>();

        for (ExtensionType t : ExtensionType.values())
            if (t != ExtensionType.NotSet) types.add(t);

        return types;
    }

    public List<Extension> extendExtensions(UUID applicationId, List<Extension> extensions)
    {
        for (String ext : raaivanSettings.getCoreNetwork().DefaultCNExtensions(applicationId))
        {
            ExtensionType extType = publicMethods.lookupEnum(ExtensionType.class, ext, ExtensionType.NotSet);
            if(extType == ExtensionType.NotSet || extensions.stream().anyMatch(u -> u.getType() == extType)) continue;

            Extension newExt = RVBeanFactory.getBean(Extension.class);
            newExt.setType(extType);
            newExt.setDisabled(false);
            newExt.setInitialized(false);

            extensions.add(newExt);
        }

        List<ExtensionType> extensionTypes = getExtensionTypes();
        for (ExtensionType ext : extensionTypes)
        {
            if (extensions.stream().anyMatch(u -> u.getType() == ext)) continue;

            Extension newExt = RVBeanFactory.getBean(Extension.class);
            newExt.setType(ext);
            newExt.setDisabled(true);
            newExt.setInitialized(false);

            extensions.add(newExt);
        }

        if (!RaaiVanConfig.Modules.SocialNetwork && extensions.stream().anyMatch(u -> u.getType() == ExtensionType.Posts))
            extensions.remove(extensions.stream().filter(u -> u.getType() == ExtensionType.Posts).findFirst().orElse(null));

        if (!RaaiVanConfig.Modules.SocialNetwork && extensions.stream().anyMatch(u -> u.getType() == ExtensionType.Form))
            extensions.remove(extensions.stream().filter(u -> u.getType() == ExtensionType.Form).findFirst().orElse(null));

        if (!RaaiVanConfig.Modules.SocialNetwork && extensions.stream().anyMatch(u -> u.getType() == ExtensionType.Documents))
            extensions.remove(extensions.stream().filter(u -> u.getType() == ExtensionType.Documents).findFirst().orElse(null));

        if (!RaaiVanConfig.Modules.SocialNetwork && extensions.stream().anyMatch(u -> u.getType() == ExtensionType.Browser))
            extensions.remove(extensions.stream().filter(u -> u.getType() == ExtensionType.Browser).findFirst().orElse(null));

        return extensions;
    }
}
