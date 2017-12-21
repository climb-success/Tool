/*
 * Copyright (c) 2017 Du Tengfei. All Rights Reserved.
 */
package com.content;

import java.util.Date;

/**
 * Professional.
 * @author <A HREF="mailto:dtfgongzuo@163.com">Du Tengfei</A>
 * @version 1.0, $Revision: 0$, $Date: Nov 28, 2017$
 * @since 1.0
 */
public class Professional
{
    private Integer id;
    private String name;
    private String code;
    private Date updateDate;
    
    //private String schoolURL;
    
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
    public String getCode()
    {
        return code;
    }
    public void setCode(String code)
    {
        this.code = code;
    }
    public Date getUpdateDate()
    {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate)
    {
        this.updateDate = updateDate;
    }
/*    public String getSchoolURL()
    {
        return schoolURL;
    }
    public void setSchoolURL(String schoolURL)
    {
        this.schoolURL = schoolURL;
    }*/
}
