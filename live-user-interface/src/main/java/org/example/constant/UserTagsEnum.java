package org.example.constant;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum UserTagsEnum {


    IS_VIP((long) Math.pow(2,1),"是否是VIP用户","tag_info_01","tagInfo01"),
    IS_OLD_USER((long) Math.pow(2,2),"是否是老用户","tag_info_01","tagInfo01");

    UserTagsEnum(long tag, String desc, String tableName, String filedName) {
        this.tag = tag;
        this.desc = desc;
        this.filedName = filedName;
        this.tableName = tableName;
    }

    private long tag;

    private String desc;

    private String filedName;

    private String tableName;

}
