package com.maiguoer.component.http.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 保存置顶数据的数据库
 * Create by www.lijin@foxmail.com on 2019/1/23 0023.
 * <br/>
 */
@Entity
public class TopDataBase {
    /**
     * 主键 自增
     */
    @Id(autoincrement = true)
    private Long id;
    /**
     * 被置顶的ID
     */
    @Property(nameInDb = "TopTagId")
    private String tagId;
    /**
     * 添加置顶的时间
     */
    @Property(nameInDb = "TopTagId")
    private long tagTime;
    @Generated(hash = 1833622195)
    public TopDataBase(Long id, String tagId, long tagTime) {
        this.id = id;
        this.tagId = tagId;
        this.tagTime = tagTime;
    }
    @Generated(hash = 2090432140)
    public TopDataBase() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTagId() {
        return this.tagId;
    }
    public void setTagId(String tagId) {
        this.tagId = tagId;
    }
    public long getTagTime() {
        return this.tagTime;
    }
    public void setTagTime(long tagTime) {
        this.tagTime = tagTime;
    }


}
