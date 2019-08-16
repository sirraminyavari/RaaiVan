package com.raaivan.util;

import com.raaivan.modules.rv.RVDAO;
import com.raaivan.modules.documents.util.DocumentUtilities;
import com.raaivan.util.rvsettings.RaaiVanSettings;
import com.raaivan.modules.rv.beans.MutableUUID;
import com.raaivan.modules.rv.enums.TextDirection;
import io.micrometer.core.instrument.util.StringUtils;
import ir.huri.jcal.JalaliCalendar;
import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.mutable.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@ApplicationScope
public class PublicMethods {
    private RVDAO rvDao;
    private DocumentUtilities documentUtilities;
    private RaaiVanSettings raaivanSettings;
    private RVRequest request;

    @Autowired
    public void _setDependencies(RVDAO rvDao, DocumentUtilities documentUtilities, RaaiVanSettings raaivanSettings,
                                 RVRequest request) {
        if (this.rvDao == null) this.rvDao = rvDao;
        if (this.documentUtilities == null) this.documentUtilities = documentUtilities;
        if (this.raaivanSettings == null) this.raaivanSettings = raaivanSettings;
        if (this.request == null) this.request = request;
    }

    private static String _SYSTEMVERSION;

    public String getSystemVersion() {
        if (StringUtils.isBlank(_SYSTEMVERSION)) _SYSTEMVERSION = rvDao.getSystemVersion();
        return _SYSTEMVERSION;
    }

    private static Map<UUID, Map<UUID, Boolean>> _SystemAdmins = new ConcurrentHashMap<>();

    public boolean isSystemAdmin(UUID applicationId, UUID userId, boolean ignoreAuthentication)
    {
        if (userId == null || (!ignoreAuthentication && !request.isAuthenticated())) return false;

        if (!_SystemAdmins.containsKey(applicationId)) _SystemAdmins.put(applicationId, new ConcurrentHashMap<>());

        if (!_SystemAdmins.get(applicationId).containsKey(userId))
            _SystemAdmins.get(applicationId).put(userId, rvDao.isSystemAdmin(applicationId, userId));
        return _SystemAdmins.get(applicationId).get(userId);
    }

    public boolean isSystemAdmin(UUID applicationId, UUID userId){
        return isSystemAdmin(applicationId, userId, false);
    }

    public DateTime now() {
        return DateTime.now(DateTimeZone.UTC);
    }

    public JalaliCalendar getJalaliCalendar(DateTime date){
        return new JalaliCalendar(new GregorianCalendar(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth()));
    }

    public JalaliCalendar getJalaliCalendar(){
        return getJalaliCalendar(now());
    }

    public String getPersianDate(DateTime date, boolean detail, boolean reverse)
    {
        if (date == null) return "";

        JalaliCalendar jalaliCalendar = getJalaliCalendar(date);

        int _day = jalaliCalendar.getDay();
        int _month = jalaliCalendar.getMonth();
        int _year = jalaliCalendar.getYear();

        String Day = (_day < 10 ? "0" : "") + _day;
        String Month = (_month < 10 ? "0" : "") + _month;
        String Year = Integer.toString(_year);
        String PDate = reverse ? Day + "/" + Month + "/" + Year : Year + "/" + Month + "/" + Day;

        if (detail) PDate = (date.getHourOfDay() < 10 ? "0" : "") + date.getHourOfDay() + ":" +
                (date.getMinuteOfHour() < 10 ? "0" : "") + date.getMinuteOfHour() + " " + PDate;

        return PDate;
    }

    public String getPersianDate(DateTime date, boolean detail){
        return getPersianDate(date, detail, false);
    }

    public String getPersianDate(DateTime date){
        return getPersianDate(date, false, false);
    }

    public String getPersianDate(){
        return getPersianDate(now(), false, false);
    }

    public DateTime persianToGregorianDate(int year, int month, int day, Integer hour, Integer minute, Integer second) {
        try {
            if (year < 100) year += year > 20 ? 1300 : 1400;

            DateTime val = new DateTime(new JalaliCalendar(year, month, day).toGregorian());

            if(hour != null) val = val.plusHours(hour);
            if(minute != null) val = val.plusMinutes(minute);
            if(second != null) val = val.plusSeconds(second);

            return val;
        } catch(Exception ex) {
            return null;
        }
    }

    public DateTime persianToGregorianDate(int year, int month, int day) {
        return persianToGregorianDate(year, month, day, null, null, null);
    }

    public boolean tryParseInt(String input, MutableInt outInt) {
        try {
            if (StringUtils.isBlank(input)) return false;
            outInt.setValue(Integer.parseInt(input));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public Integer parseInt(String input, Integer defaultValue) {
        MutableInt val = new MutableInt(0);
        return tryParseInt(input, val) ? val.getValue() : defaultValue;
    }

    public Integer parseInt(String input) {
        return parseInt(input, null);
    }

    public boolean tryParseLong(String input, MutableLong outLong) {
        try {
            if (StringUtils.isBlank(input)) return false;
            outLong.setValue(Long.parseLong(input));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public Long parseLong(String input, Long defaultValue) {
        MutableLong val = new MutableLong(0);
        return tryParseLong(input, val) ? val.getValue() : defaultValue;
    }

    public Long parseLong(String input) {
        return parseLong(input, null);
    }

    public boolean tryParseDouble(String input, MutableDouble outDouble) {
        try {
            if (StringUtils.isBlank(input)) return false;
            outDouble.setValue(Double.parseDouble(input));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public Double parseDouble(String input, Double defaultValue) {
        MutableDouble val = new MutableDouble(0);
        return tryParseDouble(input, val) ? val.getValue() : defaultValue;
    }

    public Double parseDouble(String input) {
        return parseDouble(input, null);
    }

    public boolean tryParseFloat(String input, MutableFloat outFloat) {
        try {
            if (StringUtils.isBlank(input)) return false;
            outFloat.setValue(Float.parseFloat(input));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public Float parseFloat(String input, Float defaultValue) {
        MutableFloat val = new MutableFloat(0);
        return tryParseFloat(input, val) ? val.getValue() : defaultValue;
    }

    public Float parseFloat(String input) {
        return parseFloat(input, null);
    }

    public boolean tryParseBoolean(String input, MutableBoolean outBoolean) {
        try {
            if (StringUtils.isBlank(input)) return false;
            else input = input.toLowerCase();

            if (input.equals("true") || input.equals("1")) outBoolean.setValue(true);
            else if (input.equals("false") || input.equals("0")) outBoolean.setValue(false);
            else outBoolean.setValue(Boolean.parseBoolean(input));

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public Boolean parseBoolean(String input) {
        MutableBoolean val = new MutableBoolean(false);
        return tryParseBoolean(input, val) ? val.getValue() : null;
    }

    public boolean tryParseUUID(String input, MutableUUID outUUID) {
        try {
            outUUID.setValue(UUID.fromString(input));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public UUID parseUUID(String input, UUID alternativeValue) {
        MutableUUID val = new MutableUUID(null);
        return tryParseUUID(input, val) ? val.getValue() : alternativeValue;
    }

    public UUID parseUUID(String input) {
        return parseUUID(input, null);
    }

    public boolean tryParseDate(String input, MutableDateTime outDateTime) {
        outDateTime.setDateTime(0, 0, 0, 0, 0, 0, 0);

        if (!StringUtils.isBlank(input)) input = input.trim();
        if (StringUtils.isBlank(input)) return false;

        String[] strDate = (StringUtils.isBlank(input) ? "" : input).split("-");

        if (strDate.length < 3) return false;

        try {
            outDateTime.setDateTime(Integer.parseInt(strDate[0]), Integer.parseInt(strDate[1]),
                    Integer.parseInt(strDate[2]), 0, 0, 0, 0);
            return true;
        } catch (Exception ex) {
            try {
                DateTime t = DateTime.parse(input);
                outDateTime.setYear(t.getYear());
                outDateTime.setMonthOfYear(t.getMonthOfYear());
                outDateTime.setDayOfMonth(t.getDayOfMonth());
                return true;
            } catch (Exception ex2) {
                return false;
            }
        }
    }

    public DateTime parseDate(String input, Integer days2Add) {
        MutableDateTime val = new MutableDateTime(DateTime.now());
        return tryParseDate(input, val) ? val.toDateTime().plusDays(days2Add == null ? 0 : days2Add) : null;
    }

    public DateTime parseDate(String input) {
        return parseDate(input, null);
    }

    public String parseString(String input, boolean decode, String defaultValue) {
        if (StringUtils.isBlank(input)) return defaultValue;
        return decode ? Base64.decode(input) : input;
    }

    public String parseString(String input, boolean decode) {
        return parseString(input, decode, null);
    }

    public String parseString(String input, String defaultValue) {
        return parseString(input, true, defaultValue);
    }

    public String parseString(String input) {
        return parseString(input, true, null);
    }

    private static Random _RND = new Random(System.currentTimeMillis());

    public int getRandomNumber(int min, int max) {
        return _RND.nextInt(max - min + 1) + min;
    }

    public int getRandomNumber(int length) {
        return getRandomNumber((int) Math.pow(10, (double) length - 1), (int) Math.pow(10, (double) length) - 1);
    }

    public int getRandomNumber() {
        return getRandomNumber(5);
    }

    public String getRandomString(int length) {
        String refStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_@^*$!";

        Random rnd = new Random(System.currentTimeMillis());

        String ret = "";
        for (int i = 0; i < length; ++i)
            ret += refStr.charAt(rnd.nextInt(refStr.length() - 1));

        return ret;
    }

    public String sha1(byte[] input) {
        if (input == null || input.equals("")) return "";

        String sha1 = null;

        try {
            MessageDigest msdDigest = MessageDigest.getInstance("SHA-1");
            msdDigest.update(input, 0, input.length);
            sha1 = new String(Base64.encode(msdDigest.digest()));
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.toString());
        }

        return sha1;
    }

    public String sha1(String input) {
        try {
            if (input == null || input.equals("")) return input;
            return sha1(input.getBytes(Charsets.UTF_16LE));
        } catch (Exception ex) {
            return input;
        }
    }

    public String fitNumberToSize(Integer number, int size) {
        String retStr = number.toString();
        while (retStr.length() < size) retStr = "0" + retStr;
        return retStr;
    }

    public String verifyString(String str, boolean removeHtmlTags) {
        if (removeHtmlTags && !StringUtils.isBlank(str))
            str = Expressions.replace(str, Expressions.Patterns.HTMLTag, " ");
        return StringUtils.isBlank(str) ? str : str.replaceAll("ي", "ی").replaceAll("ك", "ک");
    }

    public String verifyString(String str) {
        return verifyString(str, true);
    }

    //Arabic - Range: 0600–06FF, Arabic Supplement - Range: 0750–077F, Arabic Presentation Forms-A - Range: FB50–FDFF, Arabic Presentation Forms-B - Range: FE70–FEFF
    private static String RTLCharacters = "\u0600-\u06FF\u0750-\u077F\uFB50-\uFDFF\uFE70-\uFEFF";

    public TextDirection textDirection(String text) {
        //ASCII Punctuation - Range: 0000-0020, General Punctuation - Range: 2000-200D
        String controlChars = "\u0000-\u0020\u2000-\u200D*\"'.0-9()$%^&@!#,=?/\\+-:<>|;";

        String reRTL = "^[" + controlChars + "]*[" + RTLCharacters + "]";
        String reControl = "^[" + controlChars + "]*$";

        return text.matches(reRTL) ? TextDirection.RTL :
                (text.matches(reControl) ? TextDirection.None : TextDirection.LTR);
    }

    public boolean hasRtlCharacters(String text) {
        return !StringUtils.isBlank(text) && text.matches("[" + RTLCharacters + "]");
    }

    public int rtlCharactersCount(String text) {
        return Expressions.matchesCount(text, "[" + RTLCharacters + "]");
    }

    public int dangerousCharactersCount(String text) {
        return Expressions.matchesCount(text, "[<>?/\\~!@#$%^&*+=\\-]");
    }

    public boolean isSecureTitle(String text) {
        return StringUtils.isBlank(text) || ((float) dangerousCharactersCount(text) / (float) text.length() <= 0.3);
    }

    private static Map<String, String> NUMBERS_DIC = new HashMap<String, String>() {{
        put("0", "۰");
        put("1", "۱");
        put("2", "۲");
        put("3", "۳");
        put("4", "۴");
        put("5", "۵");
        put("6", "۶");
        put("7", "۷");
        put("8", "۸");
        put("9", "۹");
    }};

    public String convertNumbersToPersian(String input) {
        if (StringUtils.isBlank(input)) return input;
        for (String key : NUMBERS_DIC.keySet())
            input = input.replaceAll(key, NUMBERS_DIC.get(key));
        return input;
    }

    public String convertNumbersFromPersian(String input) {
        if (StringUtils.isBlank(input)) return input;
        for (String key : NUMBERS_DIC.keySet())
            input = input.replaceAll(NUMBERS_DIC.get(key), key);
        return input;
    }

    public String getThemeColor(String theme) {
        if (StringUtils.isBlank(theme)) return "";
        int _ind = theme.lastIndexOf('_');
        return _ind < 0 ? "" : theme.substring(_ind + 1);
    }

    private static String _Themes;

    public String getThemes() {
        if (!StringUtils.isBlank(_Themes)) return _Themes;

        /*
        List<FileInfo> themes = (new DirectoryInfo(HttpContext.Current.Server.MapPath("~/CSS/Themes"))).GetFiles().ToList();
        string thms = "[{\"Name\":\"Default\",\"Color\":\"\"}";
        foreach (FileInfo thm in themes)
        {
            string name = thm.Name.Substring(0, thm.Name.LastIndexOf('.'));
            if (name.ToLower() == "default") continue;
            string color = get_theme_color(name);

            thms += "," + "{\"Name\":\"" + name + "\",\"Color\":\"" + color + "\"}";
        }
        thms += "]";

        _Themes = thms;
        */

        return _Themes;
    }

    public String shuffleText(String text) {
        return StringUtils.isBlank(text) ? "" :
                Arrays.stream(text.split("."))
                        .map(u -> new AbstractMap.SimpleEntry<String, Integer>(u, getRandomNumber()))
                        .sorted((a, b) -> a.getValue() < b.getValue() ? -1 : 1)
                        .map(AbstractMap.SimpleEntry::getKey)
                        .collect(Collectors.joining(". "));
    }

    public String diff(String newStr, String oldStr) {
        int newLen = StringUtils.isBlank(newStr) ? 0 : newStr.length();
        int oldLen = StringUtils.isBlank(oldStr) ? 0 : oldStr.length();
        String dif = "";

        for (int i = 0, pos = 0; i < newLen; ++i) {
            if (pos < oldLen && newStr.charAt(i) == oldStr.charAt(pos)) ++pos;
            else dif += newStr.charAt(i);
        }

        return dif;
    }

    public String markup2plaintext(UUID applicationId, String markupText, boolean htmlTags) {
        //if 'htmlTags' is set to false, it means that all tags in the text will be replace with their observable value
        //for example, a node tag will replace with the node name that exists in the tag data

        if (StringUtils.isBlank(markupText)) return "";

        Map<String, String> dic = new HashMap<>();

        List<String> matches = Expressions.getMatches(markupText, Expressions.Patterns.Tag);

        for (String mch : matches) {
            if (dic.containsKey(mch)) continue;

            String[] tagVals = Expressions.getValue(mch, Expressions.Patterns.Tag).split(":");
            String decodedValue = Base64.decode(tagVals[2]);

            if (!htmlTags) dic.put(mch, decodedValue);
            else {
                switch (tagVals[1].toLowerCase()) {
                    case "knowledge":
                    case "node":
                        dic.put(mch, "<a href='" +
                                PublicConsts.getCompleteUrl(applicationId, PublicConsts.NodePage) +
                                "/" + tagVals[0] + "'>" + decodedValue + "</a>");
                        break;
                    case "user":
                        dic.put(mch, "<a href='" +
                                PublicConsts.getCompleteUrl(applicationId, PublicConsts.ProfilePage) +
                                "/" + tagVals[0] + "'>" + decodedValue + "</a>");
                        break;
                    case "file":
                        Map<String, String> info = tagVals.length < 4 ?
                                new HashMap<>() : json2dictionary(Base64.decode(tagVals[3]));
                        UUID fileId = parseUUID(tagVals[0]);
                        if (fileId == null) break;

                        String downloadUrl = documentUtilities.getDownloadUrl(applicationId, fileId);

                        String ext = info.getOrDefault("Extension", "");

                        if (ext.equals("jpg") || ext.equals("gif") || ext.equals("png")) //is image
                        {
                            int width = parseInt(info.getOrDefault("W", "0"));
                            int heigth = parseInt(info.getOrDefault("H", "0"));

                            dic.put(mch, "<img src='" + downloadUrl +
                                    "' style='" + (width > 0 ? "width:" + width + "px;" : "") +
                                    (heigth > 0 ? "height:" + heigth + "px;" : "") + "' />");
                        } else
                            dic.put(mch, "<a href='" + downloadUrl + "'>" + decodedValue + "</a>");

                        break;
                    default:
                        dic.put(mch, decodedValue);
                        break;
                }
            }
        }

        for (String key : matches)
            markupText = markupText.replaceAll(key, dic.get(key));

        return markupText;
    }

    public String markup2plaintext(UUID applicationId, String markupText) {
        return markup2plaintext(applicationId, markupText, false);
    }

    public boolean sendEmail(UUID applicationId, String email, String emailSubject, String body, String senderEmail,
                             String senderPassword, String senderName, String smtpAddress, Integer smtpPort, Boolean useSSL) {
        try {
            if (StringUtils.isBlank(emailSubject))
                emailSubject = raaivanSettings.getSystemEmail().EmailSubject(applicationId);
            if (StringUtils.isBlank(senderEmail)) senderEmail = raaivanSettings.getSystemEmail().Address(applicationId);
            if (StringUtils.isBlank(senderPassword))
                senderPassword = raaivanSettings.getSystemEmail().Password(applicationId);
            if (StringUtils.isBlank(senderName)) senderName = raaivanSettings.getSystemEmail().DisplayName(applicationId);
            if (StringUtils.isBlank(smtpAddress)) smtpAddress = raaivanSettings.getSystemEmail().SMTPAddress(applicationId);
            if (smtpPort == null || smtpPort <= 0) smtpPort = raaivanSettings.getSystemEmail().SMTPPort(applicationId);
            if (useSSL == null) useSSL = raaivanSettings.getSystemEmail().UseSSL(applicationId);

            String protocol = useSSL ? "smtps" : "smtp";

            Properties props = new Properties();

            props.put("mail." + protocol + ".host", smtpAddress);
            //props.put("mail." + protocol + ".socketFactory.port", smtpPort.toString());
            //if(useSSL) props.put("mail.\" + protocol + \".socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail." + protocol + ".auth", "true");
            props.put("mail." + protocol + ".port", smtpPort.toString());
            if (raaivanSettings.getSystemEmail().Timeout(applicationId) > 0)
                props.put("mail." + protocol + ".timeout", raaivanSettings.getSystemEmail().Timeout(applicationId));
            props.put("mail." + protocol + ".connectiontimeout", 3000);

            final String fromEmail = senderEmail, fromPassword = senderPassword;

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, fromPassword);
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setHeader("Disposition-Notification-To", senderEmail);
            message.setFrom(new InternetAddress(senderEmail, senderName));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject(emailSubject);
            message.setContent(body, "text/html; charset=utf-8");
            //message.setText(body, "utf-8", "html");

            Transport.send(message);

            return true;
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return false;
        }
    }

    public boolean sendEmail(UUID applicationId, String email, String emailSubject, String body) {
        return sendEmail(applicationId, email, emailSubject, body,
                null, null, null, null, null, null);
    }

    public boolean sendEmail(UUID applicationId, String email, String body) {
        return sendEmail(applicationId, email, null, body);
    }

    public boolean sendSms(String phoneNumber, String message) {
        return true;
    }

    public <E extends Enum<E>> E lookupEnum(Class<E> e, String value, E defaultValue) {
        try {
            return Enum.valueOf(e, value);
        } catch (IllegalArgumentException ex) {
            return defaultValue;
        }
    }

    public boolean isValidUsername(UUID applicationId, String username) {
        try {
            if (StringUtils.isBlank(username)) return false;
            else if (StringUtils.isBlank(raaivanSettings.getUsers().UserNamePattern(applicationId))) return true;
            return username.matches(raaivanSettings.getUsers().UserNamePattern(applicationId));
        } catch (Exception ex) {
            return false;
        }
    }


    public String mapPath(String path) {
        return "";
    }

    public String getMimeTypeByExtension(String extension) {
        return "";
    }

    public String httpPostRequest(String url, Map<String, String> values) {
        return "";
    }

    public String webRequest(String url, Map<String, String> values, boolean post) {
        return "";
    }

    public String webRequest(String url, Map<String, String> values) {
        return webRequest(url, values, true);
    }

    public String webRequest(String url, boolean post) {
        return webRequest(url, null, post);
    }

    public String webRequest(String url) {
        return webRequest(url, null, true);
    }

    public String uploadFile(UUID applicationId, String url, String fileAddress) {
        return "";
    }

    public void setTimeout(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                if (delay > 0) Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                //System.err.println(e);
            }
        }).start();
    }

    //to be removed
    public Map<String, String> json2dictionary(String json) {
        return new HashMap<>();
    }
}