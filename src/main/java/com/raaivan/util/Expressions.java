package com.raaivan.util;

import com.raaivan.modules.rv.beans.InlineTag;
import com.raaivan.util.RVBeanFactory;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class Expressions {
    public enum Patterns
    {
        Tag,
        AutoTag,
        AdditionalID,
        HTMLTag
        //Free = @"(~)\[\[([\w\|\\\/\^\$\u0621-\u064A\u0660-\u0669\u0671-\u06D3\u06F0-\u06F9\s]+)\]\]"
    }

    private static String _getPattern(Patterns pattern)
    {
        switch (pattern.toString().toLowerCase())
        {
            case "tag":
                return "(@)\\[\\[([a-zA-Z\\d\\-_]+):([\\w\\s\\.\\-]+):([0-9a-zA-Z\\+\\/\\=]+)(:([0-9a-zA-Z\\+\\/\\=]*))?\\]\\]";
            case "autotag":
                return "(~)\\[\\[([:\\-\\w]+)\\]\\]";
            case "additionalid":
                return "^([A-Za-z0-9_\\-\\/]|(~\\[\\[(((RND|(NCountS?(PY|GY)?))\\d?)|[PG](Year|YearS|Month|Day)|(FormField:[A-Za-z0-9\\-]{36})|(AreaID)|(DepID))\\]\\]))+$";
            case "htmltag":
                return "<.*?>";
            default:
                return "";
        }
    }

    public static boolean isMatch(String input, Patterns pattern)
    {
        return input.matches(_getPattern(pattern));
    }

    public static List<String> getMatches(String input, Patterns pattern)
    {
        List<String> ret = new ArrayList<>();

        String pat = _getPattern(pattern);
        if(StringUtils.isBlank(pat)) return ret;

        Matcher matcher = Pattern.compile(pat).matcher(input);

        while(matcher.find())
            ret.add(matcher.group());

        return ret;
    }

    public static List<String> getExistingTags(String input, Patterns pattern)
    {
        List<String> matches = getMatches(input, pattern);
        List<String> retList = new ArrayList<>();

        for (String mth : matches)
        {
            String val = getValue(mth, pattern);
            if (retList.stream().noneMatch(u -> u.equals(val))) retList.add(val);
        }

        return retList;
    }

    public static String getValue(String match, Patterns pattern)
    {
        if (StringUtils.isBlank(match) || getMatches(match, pattern).size() == 0) return "";

        switch (pattern.toString())
        {
            case "Tag":
            case "AutoTag":
                return match.substring(3, match.length() - 5);
            default:
                return "";
        }
    }

    public static String replace(String input, Patterns pattern, String replacement)
    {
        if (StringUtils.isBlank(input)) return input;
        if (pattern == Patterns.HTMLTag) input = StringEscapeUtils.unescapeHtml4(input);
        return input.replaceAll(_getPattern(pattern), replacement);
    }

    public static String replace(String input, Patterns pattern)
    {
        return replace(input, pattern, " ");
    }

    public static String replace(String input, Map<String, String> dic, Patterns pattern, String defaultReplacement)
    {
        List<String> matches = getMatches(input, pattern);
        if (matches == null || matches.size() == 0) return "";

        String retStr = input;

        for (String mth : matches)
        {
            String val = getValue(mth, pattern);
            val = dic.containsKey(val) ? dic.get(val) : (defaultReplacement == null ? mth : defaultReplacement);
            retStr = retStr.replaceAll(mth, val);
        }

        return retStr;
    }

    public static String replace(String input, Map<String, String> dic, Patterns pattern)
    {
        return replace(input, dic, pattern, " ");
    }

    public static List<InlineTag> getTaggedItems(String input, String tagType)
    {
        List<InlineTag> tags = new ArrayList<>();

        if (StringUtils.isBlank(input)) return tags;

        List<String> items = getExistingTags(input, Patterns.Tag);

        for (String itm : items)
        {
            String[] t = itm.split(":");
            InlineTag tg = RVBeanFactory.getBean(InlineTag.class);
            tg.setType(t[1]);
            tg.setValue(t[2]);
            if(t.length > 3) tg.setInfo(t[3]);
            tg.setId(t[0]);
            tags.add(tg);
        }

        return StringUtils.isBlank(tagType) ? tags :
                tags.stream().filter(u -> u.getType().toLowerCase().equals(tagType.toLowerCase())).collect(Collectors.toList());
    }

    public static List<InlineTag> getTaggedItems(String input){
        return getTaggedItems(input, null);
    }

    public static int matchesCount(String text, String pattern)
    {
        try {
            if (StringUtils.isBlank(text)) return 0;

            Matcher matcher = Pattern.compile(pattern).matcher(text);

            int cnt = 0;
            while (matcher.find()) ++cnt;

            return cnt;
        }
        catch (Exception ex){
            return 0;
        }
    }
}
