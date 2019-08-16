package com.raaivan.modules.documents.util;

import com.raaivan.modules.documents.beans.DocFileInfo;
import com.raaivan.modules.documents.enums.DefaultIconTypes;
import com.raaivan.modules.documents.enums.FileOwnerTypes;
import com.raaivan.modules.documents.enums.FolderNames;
import com.raaivan.util.PublicMethods;
import com.raaivan.util.RVBeanFactory;
import com.raaivan.util.rvsettings.RaaiVanSettings;
import com.raaivan.util.PublicConsts;
import com.raaivan.util.RVJSON;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@ApplicationScope
public class DocumentUtilities {
    private PublicMethods publicMethods;
    private RaaiVanSettings raaivanSettings;

    @Autowired
    public void _setDependencies(PublicMethods publicMethods, RaaiVanSettings raaiVanSettings) {
        if (this.publicMethods == null) this.publicMethods = publicMethods;
        if (this.raaivanSettings == null) this.raaivanSettings = raaiVanSettings;
    }

    public FolderNames getFolderName(FileOwnerTypes ownerType)
    {
        switch (ownerType)
        {
            case Node:
            case Wiki:
            case Message:
            case WorkFlow:
            case FormElement:
                return FolderNames.Attachments;
            case WikiContent:
                return FolderNames.WikiContent;
            default:
                return FolderNames.TemporaryFiles;
        }
    }

    private String _getFolderPath(UUID applicationId, FolderNames folderName)
    {
        switch (folderName)
        {
            case Attachments:
            case WikiContent:
            case Index:
            case TemporaryFiles:
            case Pictures:
            case PDFImages:
                return "App_Data/" + applicationId.toString() + "/Documents/" + folderName.toString();
            case Icons:
            case ProfileImages:
            case CoverPhoto:
                return "Global_Documents/" + applicationId.toString() + "/" + folderName.toString();
            case HighQualityIcon:
                return "Global_Documents/" + applicationId.toString() + "/" +
                        FolderNames.Icons.toString() + "/" + "HighQuality";
            case HighQualityProfileImage:
                return "Global_Documents/" + applicationId.toString() + "/" +
                        FolderNames.ProfileImages.toString() + "/" + "HighQuality";
            case HighQualityCoverPhoto:
                return "Global_Documents/" + applicationId.toString() + "/" +
                        FolderNames.CoverPhoto.toString() + "/" + "HighQuality";
            case Themes:
                return "CSS/" + folderName.toString();
            case EmailTemplates:
                return "App_Data/" + applicationId.toString() + "/" + folderName.toString();
            default:
                return "";
        }
    }

    public String getSubFolder(String guidName, boolean clientPath)
    {
        return guidName.charAt(0) + guidName.charAt(1) + (clientPath ? "/" : "\\") + guidName.charAt(2);
    }

    public String getSubFolder(String guidName)
    {
        return getSubFolder(guidName, false);
    }

    public String getSubFolder(UUID fileId, boolean clientPath)
    {
        return getSubFolder(fileId.toString(), clientPath);
    }

    public String getSubFolder(UUID fileId)
    {
        return getSubFolder(fileId, false);
    }

    public boolean hasSubFolder(FolderNames folderName)
    {
        return !(folderName == FolderNames.EmailTemplates || folderName == FolderNames.Index ||
                folderName == FolderNames.TemporaryFiles || folderName == FolderNames.Themes);
    }

    public String mapPath(UUID applicationId, FolderNames folderName, String dest)
    {
        return publicMethods.mapPath("~/" + _getFolderPath(applicationId, folderName)) +
                (StringUtils.isBlank(dest) ? "" : (dest.charAt(0) == '\\' ? "" : "\\") + dest);
    }

    public String mapPath(UUID applicationId, FolderNames folderName)
    {
        return mapPath(applicationId, folderName, null);
    }

    public String mapPath(String path)
    {
        return publicMethods.mapPath("~" + (StringUtils.isBlank(path) || path.charAt(0) == '/' ? "" : "/") + path);
    }

    public String getClientPath(UUID applicationId, FolderNames folderName, String dest)
    {
        return "../../" + _getFolderPath(applicationId, folderName) +
                (StringUtils.isBlank(dest) ? "" : (dest.charAt(0) == '/' ? "" : "/") + dest);
    }

    public String getClientPath(UUID applicationId, FolderNames folderName){
        return getClientPath(applicationId, folderName, null);
    }

    public String getPersonalImageAddress(UUID applicationId, UUID userId){
        return "";
    }

    public boolean pictureExists(UUID applicationId, UUID pictureId)
    {
        String path = mapPath(applicationId, FolderNames.Pictures) +
                "\\" + getSubFolder(pictureId, false) + "\\" + pictureId.toString() + ".jpg";
        return (new File(path)).exists();
    }

    public String getIconUrl(UUID applicationId, DefaultIconTypes defaultIcon, String extension, boolean networkAddress)
    {
        String adr = "";

        switch (defaultIcon) {
            case Document:
                adr = "../../ReDesign/images/archive.png";
                break;
            case Extension:
                adr = "../../ReDesign/images/extensions/" + extension + ".png";
                String path = publicMethods.mapPath("~/ReDesign/images/extensions") + "\\" + extension + ".png";
                adr = (new File(path)).exists() ? adr : "../../ReDesign/images/archive.png";
                break;
            default:
                adr = "../../ReDesign/images/Preview.png";
                break;
        }

        return networkAddress ? adr.replace("../..", raaivanSettings.RaaiVanURL(applicationId)) : adr;
    }

    public String getIconUrl(UUID applicationId, DefaultIconTypes defaultIcon, boolean networkAddress){
        return getIconUrl(applicationId, defaultIcon, "", networkAddress);
    }

    public String getIconUrl(UUID applicationId, DefaultIconTypes defaultIcon){
        return getIconUrl(applicationId, defaultIcon, "", false);
    }

    public String getIconUrl(UUID applicationId, UUID ownerId,
                             DefaultIconTypes defaultIcon, UUID alternateOwnerId, boolean networkAddress)
    {
        String adr = "";

        if (ownerId == null) return "";

        FolderNames folderName = FolderNames.Icons;
        String subFolder = getSubFolder(ownerId);
        String alternateSubFolder = alternateOwnerId != null ? getSubFolder(alternateOwnerId) : "";

        String serverAddress = mapPath(applicationId, folderName) + "\\" + subFolder + "\\" + ownerId.toString() + ".jpg";

        String alternatePath = alternateOwnerId == null ? "" : mapPath(applicationId, folderName) +
                "\\" + alternateSubFolder + "\\" + alternateOwnerId.toString() + ".jpg";

        subFolder = getSubFolder(ownerId, true);
        alternateSubFolder = alternateOwnerId != null ? getSubFolder(alternateOwnerId, true) : "";

        adr = (new File(serverAddress)).exists() ?
                getClientPath(applicationId, folderName) + "/" + subFolder + "/" + ownerId.toString() + ".jpg" :
                (!StringUtils.isBlank(alternatePath) && (new File(alternatePath)).exists() ?
                        getClientPath(applicationId, folderName) + "/" + alternateSubFolder +
                                "/" + alternateOwnerId.toString() + ".jpg" :
                        (defaultIcon == DefaultIconTypes.None ? "" : getIconUrl(applicationId, defaultIcon)));

        return networkAddress ? adr.replace("../..", raaivanSettings.RaaiVanURL(applicationId)) : adr;
    }

    public String getIconUrl(UUID applicationId, UUID ownerId){
        return getIconUrl(applicationId, ownerId, DefaultIconTypes.Node, null, false);
    }

    public String getIconUrl(UUID applicationId, UUID ownerId, String extension, boolean highQuality, boolean networkAddress)
    {
        String adr = "";

        if (ownerId == null) return "";

        FolderNames folderName = highQuality ? FolderNames.HighQualityIcon : FolderNames.Icons;

        String serverSubFolder = getSubFolder(ownerId);
        String clientSubFolder = getSubFolder(ownerId, true);

        String serverAddress = mapPath(applicationId, folderName) +
                "\\" + serverSubFolder + "\\" + ownerId.toString() + ".jpg";

        adr = (new File(serverAddress)).exists() ?
                getClientPath(applicationId, folderName) + "/" + clientSubFolder + "/" + ownerId.toString() + ".jpg" :
                (highQuality ? "" : getIconUrl(applicationId, DefaultIconTypes.Extension, extension, networkAddress));

        return networkAddress ? adr.replace("../..", raaivanSettings.RaaiVanURL(applicationId)) : adr;
    }

    public String getIconUrl(UUID applicationId, UUID ownerId, String extension){
        return getIconUrl(applicationId, ownerId, extension, false, false);
    }

    public String getIconUrl(UUID applicationId, String fileExtention, boolean networkAddress)
    {
        String url = "ReDesign/images/extensions/" + fileExtention + ".png";
        String adr = (new File(mapPath(url))).exists() ? "../../" + url : "";

        return networkAddress ? adr.replace("../..", raaivanSettings.RaaiVanURL(applicationId)) : adr;
    }

    public String getIconUrl(UUID applicationId, String fileExtention){
        return getIconUrl(applicationId, fileExtention, false);
    }

    public boolean iconExists(UUID applicationId, UUID ownerId)
    {
        if(applicationId == null || ownerId == null) return false;

        String path = mapPath(applicationId, FolderNames.Icons) +
                "\\" + getSubFolder(ownerId) + "\\" + ownerId.toString() + ".jpg";
        return (new File(path)).exists();
    }

    public RVJSON getIconJson(UUID applicationId, UUID ownerId)
    {
        DocFileInfo dfi = RVBeanFactory.getBean(DocFileInfo.class);

        dfi.setFileID(ownerId);
        dfi.setFileName("آیکون");
        dfi.setExtension("jpg");
        dfi.setOwnerID(ownerId);

        return dfi.toJson(applicationId);
    }

    public String getCoverPhotoUrl(UUID applicationId, UUID ownerId, boolean networkAddress, boolean highQuality)
    {
        if (ownerId == null) return "";

        FolderNames folderName = highQuality ? FolderNames.HighQualityCoverPhoto : FolderNames.CoverPhoto;

        String serverSubFolder = getSubFolder(ownerId);
        String clientSubFolder = getSubFolder(ownerId, true);

        String serverAddress = mapPath(applicationId, folderName) +
                "\\" + serverSubFolder + "\\" + ownerId.toString() + ".jpg";

        String address = !(new File(serverAddress)).exists() ? "" :
                getClientPath(applicationId, folderName) + "/" + clientSubFolder + "/" + ownerId.toString() + ".jpg";

        return networkAddress ? address.replace("../..", raaivanSettings.RaaiVanURL(applicationId)) : address;
    }

    public String getCoverPhotoUrl(UUID applicationId, UUID ownerId){
        return getCoverPhotoUrl(applicationId, ownerId, false, false);
    }

    public String getDownloadUrl(UUID applicationId, UUID fileId)
    {
        if(fileId == null) return "";

        return PublicConsts.getCompleteUrl(applicationId, PublicConsts.FileDownload) +
                "?timeStamp=" + publicMethods.now().getMillis() + "&FileID=" + fileId.toString();
    }

    protected List<String> _getGuidFileNames(List<DocFileInfo> docFiles)
    {
        if (docFiles == null) return new ArrayList<>();

        List<String> guidFileNames = new ArrayList<>();

        for (DocFileInfo f : docFiles) {
            if(f.getFileID() == null) continue;
            guidFileNames.add(f.getFileID().toString() + (StringUtils.isBlank(f.getExtension()) ? "" : "." + f.getExtension()));
        }

        return guidFileNames;
    }
}
