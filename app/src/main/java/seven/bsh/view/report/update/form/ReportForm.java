package seven.bsh.view.report.update.form;

import android.content.Context;
import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import seven.bsh.db.entity.QueueFile;
import seven.bsh.utils.Resizer;
import seven.bsh.view.attributes.FileInputAttribute;
import seven.bsh.view.attributes.IInputAttribute;
import seven.bsh.view.attributes.PhotoInputAttribute;
import seven.bsh.view.attributes.SerializableData;

public class ReportForm {
    private List<IInputAttribute> mAttributeItems;
    private List<QueueFile> mFiles;
    private double mLongitude;
    private double mLatitude;
    private int mProjectId;
    private int mChecklistId;
    private int mTradeObjectId;
    private long mPhotoLimit;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public ReportForm() {
        mAttributeItems = new ArrayList<>();
        mFiles = new ArrayList<>();
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public boolean validate() {
        boolean result = true;
        for (IInputAttribute item : mAttributeItems) {
            if (!item.validate()) {
                result = false;
            }
        }
        return result;
    }

    public void prepareDataValues() {
        for (IInputAttribute item : mAttributeItems) {
            item.prepareDataValue();
        }
    }

    public JSONObject serialize(Context context) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("checklist_id", mChecklistId);
        data.put("trade_object_id", mTradeObjectId);
        data.put("project_id", mProjectId);

        if (hasGpsCoords()) {
            data.put("latitude", mLatitude);
            data.put("longitude", mLongitude);
        }

        for (IInputAttribute item : mAttributeItems) {
            if (item instanceof FileInputAttribute) {
                File file = ((FileInputAttribute) item).getFile();
                if (file != null) {
                    String fileName = file.getName();
                    if (!fileName.equals(FileInputAttribute.EMPTY_FILE)) {
                        if (item instanceof PhotoInputAttribute) {
                            file = Resizer.prepareImage(file, mPhotoLimit * 1024L, mLongitude, mLatitude, context);
                        }

                        QueueFile fileModel = new QueueFile();
                        fileModel.setPath(file.getAbsolutePath());
                        fileModel.setFieldName(item.getName());
                        mFiles.add(fileModel);
                    }
                }
            }

            String key = item.getName();
            if (data.has(key)) {
                data.remove(key);
            }

            SerializableData packedData = item.serialize();
            if (packedData != null) {
                data.put(key, packedData.getValue());
            }
        }
        return data;
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public void setProjectId(int projectId) {
        mProjectId = projectId;
    }

    public int getProjectId() {
        return mProjectId;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setChecklistId(int checklistId) {
        mChecklistId = checklistId;
    }

    public void setTradeObjectId(int tradeObjectId) {
        mTradeObjectId = tradeObjectId;
    }

    public void setPhotoLimit(long photoLimit) {
        mPhotoLimit = photoLimit;
    }

    public List<QueueFile> getFiles() {
        return mFiles;
    }

    public boolean hasGpsCoords() {
        return mLatitude != 0
            && mLongitude != 0;
    }

    public void setLocation(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
    }

    public void setAttributeItems(List<IInputAttribute> items) {
        mAttributeItems = items;
    }

    public boolean isChanged() {
        for (IInputAttribute attribute : mAttributeItems) {
            if (attribute.isChanged()) {
                return true;
            }
        }
        return false;
    }
}
