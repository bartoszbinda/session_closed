package com.binda;

import com.binda.generator.TimeZoneDataGenerator;
import com.binda.repository.HibernateDstRuleDao;
import com.binda.repository.HibernateTimeZoneDao;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(PersistenceConfig.class);
        for (String beanName : context.getBeanDefinitionNames()) {
            System.out.println(beanName);
        }
        context.start();
        HibernateTimeZoneDao hibernateTimeZoneDao = (HibernateTimeZoneDao) context.getBean("hibernateTimeZoneDao");
        HibernateDstRuleDao dstRuleDao = (HibernateDstRuleDao) context.getBean("hibernateDstRuleDao");
        TimeZoneDataGenerator timeZoneDataGenerator = new TimeZoneDataGenerator(dstRuleDao, hibernateTimeZoneDao);
        timeZoneDataGenerator.generate();
        context.stop();
    }
}
