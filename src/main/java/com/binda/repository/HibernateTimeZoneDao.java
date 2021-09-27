/*
 * Copyright (c) 2020, Sabre Holdings. All Rights Reserved.
 */

package com.binda.repository;


import com.binda.domain.TimeZone;

public class HibernateTimeZoneDao extends  AbstractHibernateDao {
    public HibernateTimeZoneDao(){
        setClazz(TimeZone.class);
    }
}
