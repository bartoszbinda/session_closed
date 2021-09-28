package com.binda.repository;

import com.binda.domain.DstRule;
import org.springframework.stereotype.Component;

@Component
public class HibernateDstRuleDao extends AbstractHibernateDao {
    public HibernateDstRuleDao() {
        setClazz(DstRule.class);
    }
}
