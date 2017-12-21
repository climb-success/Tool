/*
 * Copyright (c) 2017 Du Tengfei. All Rights Reserved.
 */
package com.content;

import java.util.ArrayList;
import java.util.List;

/**
 * School.
 * @author <A HREF="mailto:dtfgongzuo@163.com">Du Tengfei</A>
 * @version 1.0, $Revision: 0$, $Date: Nov 27, 2017$
 * @since 1.0
 */
public class SchoolPost
{
    private Integer id;
    private String name;
    private String province;
    private List<SchoolProfessional> schoolProfessionals = new ArrayList<SchoolProfessional>();
    
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
    public String getProvince()
    {
        return province;
    }
    public void setProvince(String province)
    {
        this.province = province;
    }
    public List<SchoolProfessional> getSchoolProfessionals()
    {
        return schoolProfessionals;
    }
    public void setSchoolProfessionals(List<SchoolProfessional> schoolProfessionals)
    {
        this.schoolProfessionals = schoolProfessionals;
    }
}
