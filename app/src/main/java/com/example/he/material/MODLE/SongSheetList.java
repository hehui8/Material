package com.example.he.material.MODLE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * project: Material
 * author : Android研发部_姓名
 * date : 2019/2/18
 * time : 19:48
 * email : 企业邮箱
 * note : 说明
 */
public class SongSheetList implements Serializable {
    private String SheetName;
    private String SheetPic;
    public List<Song> SheetList= new ArrayList();

}
