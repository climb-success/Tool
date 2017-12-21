/*
 * Copyright (c) 2017 Du Tengfei. All Rights Reserved.
 */
package com.content;

import java.util.Date;

/**
 * SchoolProfessional.
 * @author <A HREF="mailto:dtfgongzuo@163.com">Du Tengfei</A>
 * @version 1.0, $Revision: 0$, $Date: Dec 3, 2017$
 * @since 1.0
 */
public class SchoolProfessional
{
    private Integer id;
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
