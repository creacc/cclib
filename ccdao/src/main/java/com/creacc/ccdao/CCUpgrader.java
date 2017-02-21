package com.creacc.ccdao;

import android.database.sqlite.SQLiteDatabase;

import com.creacc.ccdao.annotation.CCTableEntity;

import java.util.List;

/**
 * Created by Creacc on 2016/7/31.
 */

public abstract class CCUpgrader<OriginalType extends CCTableEntity, FinalType extends CCTableEntity> {

    public abstract Version getUpgradeVersion();

    public abstract List<OriginalType> queryOriginalRows(SQLiteDatabase database);

    public abstract List<FinalType> generateFinalRows(List<OriginalType> originalRows);

    public class Version {

        int originalVersion;

        int finalVersion;

        public Version(int originalVersion, int finalVersion) {
            this.originalVersion = originalVersion;
            this.finalVersion = finalVersion;
        }
    }
}
