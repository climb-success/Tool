/*
 * Copyright (c) 2017 Du Tengfei. All Rights Reserved.
 */
package com.content;

import java.util.Date;

/**
 * Group.
 * @author <A HREF="mailto:dtfgongzuo@163.com">Du Tengfei</A>
 * @version 1.0, $Revision: 0$, $Date: Dec 11, 2017$
 * @since 1.0
 */
public class Group
{
    private Integer id;
    private String name;
    private Integer qq;
    private String qqCode;
    private String weixin;
    private String weixinCode;
    private Integer year;
    private Integer schoolId;
    private Integer professionalId;
    private Date updateDate;
    
    public Integer getId()
    {
        return id;
    }
    public void setId(Integer id)
    {
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public Integer getQq()
    {
        return qq;
    }
    public void setQq(Integer qq)
    {
        this.qq = qq;
    }
    public String getQqCode()
    {
        return qqCode;
    }
    public void setQqCode(String qqCode)
    {
        this.qqCode = qqCode;
    }
    public String getWeixin()
    {
        return weixin;
    }
    public void setWeixin(String weixin)
    {
        this.weixin = weixin;
    }
    public String getWeixinCode()
    {
        return weixinCode;
    }
    public void setWeixinCode(String weixinCode)
    {
        this.weixinCode = weixinCode;
    }
    public Integer getYear()
    {
        return year;
    }
    public void setYear(Integer year)
    {
        this.year = year;
    }
    public Integer getSchoolId()
    {
        return schoolId;
    }
    public void setSchoolId(Integer schoolId)
    {
        this.schoolId = schoolId;
    }
    public Integer getProfessionalId()
    {
        return professionalId;
    }
    public void setProfessionalId(Integer professionalId)
    {
        this.professionalId = professionalId;
    }
    public Date getUpdateDate()
    {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate)
    {
        this.updateDate = updateDate;
    }
}
