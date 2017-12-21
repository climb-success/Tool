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
public class Config
{
    private Integer id;
    private String name;
    private String input;
    private String descrption;
    private Date updateDate;
    
    public static final String POST_URL = "POST_URL";
    
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
    public String getInput()
    {
        return input;
    }
    public void setInput(String input)
    {
        this.input = input;
    }
    public String getDescrption()
    {
        return descrption;
    }
    public void setDescrption(String descrption)
    {
        this.descrption = descrption;
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
