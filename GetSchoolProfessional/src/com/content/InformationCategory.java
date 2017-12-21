/*
 * Copyright (c) 2017 Du Tengfei. All Rights Reserved.
 */
package com.content;

import java.util.Date;

/**
 * NewCategory.
 * @author <A HREF="mailto:dtfgongzuo@163.com">Du Tengfei</A>
 * @version 1.0, $Revision: 0$, $Date: Nov 28, 2017$
 * @since 1.0
 */
public class InformationCategory
{
    private Integer id;
    private String name;
    private String chineseName;
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
    
    public String getChineseName()
    {
        return chineseName;
    }
    public void setChineseName(String chineseName)
    {
        this.chineseName = chineseName;
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
