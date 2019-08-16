package com.raaivan.modules.documents.beans;

import com.raaivan.modules.corenetwork.beans.Node;
import com.raaivan.modules.documents.util.DocumentUtilities;
import com.raaivan.util.*;
import com.raaivan.modules.documents.enums.FileOwnerTypes;
import com.raaivan.modules.documents.enums.FolderNames;
import com.raaivan.util.rvsettings.RaaiVanSettings;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class DocFileInfo {
    private PublicMethods publicMethods;
    private DocumentUtilities documentUtilities;
    private RaaiVanSettings raaivanSettings;

    @Autowired
    public void _setDependencies(PublicMethods publicMethods, DocumentUtilities documentUtilities, RaaiVanSettings raaivanSettings){
        if(this.publicMethods == null) this.publicMethods = publicMethods;
        if(this.documentUtilities == null) this.documentUtilities = documentUtilities;
        if(this.raaivanSettings == null) this.raaivanSettings = raaivanSettings;
    }

    private UUID OwnerID;
    private FileOwnerTypes OwnerType;
    private UUID FileID;
    private String FileName;
    private String Extension;
    private Long Size;
    private Node OwnerNode;

    public DocFileInfo(){
        OwnerType = FileOwnerTypes.None;
        OwnerNode = RVBeanFactory.getBean(Node.class);
    }

    public UUID getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(UUID ownerID) {
        OwnerID = ownerID;
    }

    public FileOwnerTypes getOwnerType() {
        return OwnerType;
    }

    public void setOwnerType(FileOwnerTypes ownerType) {
        OwnerType = ownerType;
    }

    public UUID getFileID() {
        return FileID;
    }

    public void setFileID(UUID fileID) {
        FileID = fileID;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getExtension() {
        return Extension;
    }

    public void setExtension(String extension) {
        Extension = extension;
    }

    public Long getSize() {
        return Size;
    }

    public void setSize(Long size) {
        Size = size;
    }

    public Node getOwnerNode() {
        return OwnerNode;
    }

    public void setOwnerNode(Node ownerNode) {
        OwnerNode = ownerNode;
    }

    public String MIME() {
        return publicMethods.getMimeTypeByExtension(Extension);
    }

    public String getAddress(UUID applicationId, FolderNames folderName) {
        if (FileID == null) return "";

        String sub = !documentUtilities.hasSubFolder(folderName) ? "" :
                "\\" + documentUtilities.getSubFolder(FileID);

        return documentUtilities.mapPath(applicationId, folderName) + sub +
                "\\" + FileID.toString() + (StringUtils.isBlank(Extension) ? "" : "." + Extension);
    }

    public boolean readable()
    {
        return !StringUtils.isBlank(Extension) && Arrays.stream(("jpg,png,jpeg,gif,bmp,mp4,mp3,wav,ogg,webm" +
                (RaaiVanConfig.Modules.PDFViewer ? ",pdf" : "")).split(",")).anyMatch(x -> x.equals(Extension.toLowerCase()));
    }

    public boolean downloadable(UUID applicationId)
    {
        return StringUtils.isBlank(Extension) || StringUtils.isBlank(raaivanSettings.DefaultPrivacyForReadableFiles(applicationId)) ||
                raaivanSettings.DefaultPrivacyForReadableFiles(applicationId).toLowerCase().contains("public") ||
                !RaaiVanConfig.Modules.PDFViewer || !Extension.toLowerCase().equals("pdf");
    }

    public RVJSON toJson(UUID applicationId, boolean icon) {
        if (StringUtils.isBlank(FileName)) FileName = "";

        String iconName = StringUtils.isBlank(Extension) ? "dkgadjkghdkjghkfdjh" : Extension;

        String _path = publicMethods.mapPath("~/ReDesign/images/extensions/" + iconName + ".png");
        String _clPath = "../../ReDesign/images/extensions/" + iconName + ".png";

        return (new RVJSON())
                .add("FileID", FileID)
                .add("FileName", Base64.encode(FileName))
                .add("OwnerID", OwnerID)
                .add("Extension", Base64.encode(Extension))
                .add("MIME", MIME())
                .add("Size", Size)
                .add("Downloadable", downloadable(applicationId))
                .add("IconURL", !icon ? null : (new File(_path)).exists() ? _clPath : _clPath.replace(iconName, "default"));
    }

    public RVJSON toJson(UUID applicationId){
        return toJson(applicationId, false);
    }
}
