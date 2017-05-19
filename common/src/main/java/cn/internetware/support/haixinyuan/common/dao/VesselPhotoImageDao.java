package cn.internetware.support.haixinyuan.common.dao;

import cn.internetware.support.haixinyuan.common.model.VesselPhoto;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class VesselPhotoImageDao {
    protected static final Logger LOGGER = LoggerFactory.getLogger(VesselPhotoImageDao.class);

    // Store path computed by photo id, like the following:
    // 1234567 => 12/34/56/1234567.jpg
    public static String getStorePath(VesselPhoto photo) {
        List<String> componentList = new ArrayList<>();
        String fileName = String.valueOf(photo.getId());
        for (int index = 0; index < fileName.length(); index += 2) {
            componentList.add(fileName.substring(index, Math.min(index + 2, fileName.length())));
        }
        componentList.set(componentList.size() - 1, fileName + ".jpg");
        return StringUtils.join(componentList, "/");
    }

    public File saveImageForPhoto(VesselPhoto photo) throws IOException {
        if (photo.getContent() == null) {
            LOGGER.error("Save image for {} error: empty content.", photo);
            return null;
        }
        File file = new File("./data/photo", getStorePath(photo));
        FileUtils.writeByteArrayToFile(file, photo.getContent());
        return file;
    }
    public VesselPhoto loadImageForPhoto(VesselPhoto photo) throws IOException {
        File file = new File("./data/photo", getStorePath(photo));
        photo.setContent(FileUtils.readFileToByteArray(file));
        return photo;
    }

    public List<VesselPhoto> loadImageForPhotoList(List<VesselPhoto> photoList) {
        for (VesselPhoto photo : photoList) {
            try {
                loadImageForPhoto(photo);
            } catch (IOException e) {
                LOGGER.error("Load image data for {} failed: {}", photo, e.getMessage());
            }
        }
        return photoList;
    }

    public List<VesselPhoto> saveImageForPhotoList(List<VesselPhoto> photoList) {
        for (VesselPhoto photo : photoList) {
            try {
                saveImageForPhoto(photo);
            } catch (IOException e) {
                LOGGER.error("Save image data for {} failed: ", photo, e);
            }
        }
        return photoList;
    }
}

