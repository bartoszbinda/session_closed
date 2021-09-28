/*
 * Copyright (c) 2014, Sabre Holdings. All Rights Reserved.
 */

package com.binda.generator;

import com.binda.domain.DstRule;
import com.binda.domain.TimeZone;
import com.binda.repository.HibernateDstRuleDao;
import com.binda.repository.HibernateTimeZoneDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TimeZoneDataGenerator {
    @SuppressWarnings({"NonConstantLogger"})
    private Logger logger = LoggerFactory.getLogger(TimeZoneDataGenerator.class);
    private Map<String, TimeZone> timeZonesByCode = new HashMap<>();
    private Map<Integer, TimeZone> timeZonesById = new HashMap<>();

    AtomicInteger id = new AtomicInteger(0);

    private HibernateTimeZoneDao timeZoneDao;
    private HibernateDstRuleDao dstRuleDao;

    @Autowired
    public TimeZoneDataGenerator(HibernateDstRuleDao dstRuleDao, HibernateTimeZoneDao timeZoneDao) {
        this.timeZoneDao = timeZoneDao;
        this.dstRuleDao = dstRuleDao;
    }

    @Transactional
    public void generate() {
        logger.info("Saving dstRule");
        for (int i = 1;  i < 100; i++) {
            DstRule dstRule = new DstRule(i, "abcd");
            dstRuleDao.saveOrUpdate(dstRule);
        }
        logger.info("saved dstrule");

        generateTimeZones();
        logger.info("saving timezones");
        for (TimeZone timeZone : timeZonesById.values()) {
            timeZoneDao.saveOrUpdate(timeZone);
        }
        logger.info("saved timezones");
    }

    public TimeZone createTimeZone(int timeZoneId, String code, Integer offset) {
        TimeZone zone = timeZonesById.get(timeZoneId);
        if (zone == null) {
            zone = new TimeZone();
            zone.setId(id.incrementAndGet());
            zone.setCode(code);
        }
        timeZonesById.put(zone.getId(), zone);
        timeZonesByCode.put(code, zone);
        return zone;
    }

    public final TimeZoneData[] timeZoneDataTable = {
            new TimeZoneData("RO01", 120),
            new TimeZoneData("CA11", -420),
            new TimeZoneData("PF03", -540),
            new TimeZoneData("NE01", 60),
            new TimeZoneData("VI01", -240),
            new TimeZoneData("TD01", 60),
            new TimeZoneData("GU01", 600),
            new TimeZoneData("MX03", -480),
            new TimeZoneData("IR01", 210),
            new TimeZoneData("CK01", -600),
            new TimeZoneData("MT01", 60),
            new TimeZoneData("GM01", 0),
            new TimeZoneData("AF01", 270),
            new TimeZoneData("CA04", -360),
            new TimeZoneData("CC01", 390),
            new TimeZoneData("KI02", 840),
            new TimeZoneData("ML01", 0),
            new TimeZoneData("GE01", 240),
            new TimeZoneData("ID02", 480),
            new TimeZoneData("KG01", 300),
            new TimeZoneData("SK01", 60),
            new TimeZoneData("BZ01", -360),
            new TimeZoneData("MD01", 120),
            new TimeZoneData("BR05", -180),
            new TimeZoneData("SC01", 240),
            new TimeZoneData("BR01", -180),
            new TimeZoneData("NZ02", 765),
            new TimeZoneData("DO01", -240),
            new TimeZoneData("BJ01", 60),
            new TimeZoneData("LS01", 120),
            new TimeZoneData("TW01", 480),
            new TimeZoneData("NP01", 345),
            new TimeZoneData("BB01", -240),
            new TimeZoneData("LK01", 360),
            new TimeZoneData("PM01", -180),
            new TimeZoneData("TO01", 780),
            new TimeZoneData("AU07", 600),
            new TimeZoneData("LC01", -240),
            new TimeZoneData("AU03", 570),
            new TimeZoneData("PE01", -300),
            new TimeZoneData("TG01", 0),
            new TimeZoneData("CV01", -60),
            new TimeZoneData("KZ01", 240),
            new TimeZoneData("ES01", 60),
            new TimeZoneData("CN01", 480),
            new TimeZoneData("MW01", 120),
            new TimeZoneData("GP01", -240),
            new TimeZoneData("AI01", -240),
            new TimeZoneData("GL03", -60),
            new TimeZoneData("KR01", 540),
            new TimeZoneData("US09", -600),
            new TimeZoneData("SV01", -360),
            new TimeZoneData("CF01", 60),
            new TimeZoneData("US05", -420),
            new TimeZoneData("MO01", 480),
            new TimeZoneData("GH01", 0),
            new TimeZoneData("US01", -300),
            new TimeZoneData("EC01", -300),
            new TimeZoneData("YU01", 60),
            new TimeZoneData("IE01", 0),
            new TimeZoneData("SN01", 0),
            new TimeZoneData("MG01", 180),
            new TimeZoneData("UM02", -660),
            new TimeZoneData("DZ01", 60),
            new TimeZoneData("QA01", 180),
            new TimeZoneData("YE01", 180),
            new TimeZoneData("HT01", -300),
            new TimeZoneData("RU09", 600),
            new TimeZoneData("BM01", -240),
            new TimeZoneData("LV01", 120),
            new TimeZoneData("FO01", 0),
            new TimeZoneData("RU05", 360),
            new TimeZoneData("TZ01", 180),
            new TimeZoneData("PT03", -60),
            new TimeZoneData("DJ01", 180),
            new TimeZoneData("RU01", 120),
            new TimeZoneData("BE01", 60),
            new TimeZoneData("TR01", 120),
            new TimeZoneData("PH01", 480),
            new TimeZoneData("TJ01", 300),
            new TimeZoneData("CY01", 120),
            new TimeZoneData("NC01", 660),
            new TimeZoneData("RE01", 240),
            new TimeZoneData("AT01", 60),
            new TimeZoneData("VG01", -240),
            new TimeZoneData("MX04", -420),
            new TimeZoneData("MZ01", 120),
            new TimeZoneData("AL01", 60),
            new TimeZoneData("ZA01", 120),
            new TimeZoneData("SY01", 120),
            new TimeZoneData("CI01", 0),
            new TimeZoneData("CA09", -300),
            new TimeZoneData("MR01", 0),
            new TimeZoneData("AD01", 60),
            new TimeZoneData("CA05", -420),
            new TimeZoneData("KM01", 180),
            new TimeZoneData("KI03", 780),
            new TimeZoneData("CA01", -210),
            new TimeZoneData("ID03", 540),
            new TimeZoneData("WS01", -660),
            new TimeZoneData("KE01", 180),
            new TimeZoneData("SI01", 60),
            new TimeZoneData("BR06", -240),
            new TimeZoneData("BR02", -240),
            new TimeZoneData("SA01", 180),
            new TimeZoneData("LY01", 120),
            new TimeZoneData("FR01", 60),
            new TimeZoneData("DM01", -240),
            new TimeZoneData("BH01", 180),
            new TimeZoneData("FJ01", 720),
            new TimeZoneData("DE01", 60),
            new TimeZoneData("LI01", 60),
            new TimeZoneData("PK01", 300),
            new TimeZoneData("TM01", 300),
            new TimeZoneData("NF01", 690),
            new TimeZoneData("AU04", 600),
            new TimeZoneData("AW01", -240),
            new TimeZoneData("LA01", 420),
            new TimeZoneData("KZ02", 300),
            new TimeZoneData("ES02", 0),
            new TimeZoneData("AO01", 60),
            new TimeZoneData("IS01", 0),
            new TimeZoneData("CL01", -240),
            new TimeZoneData("MU01", 240),
            new TimeZoneData("GN01", 0),
            new TimeZoneData("AG01", -240),
            new TimeZoneData("KP01", 540),
            new TimeZoneData("UY01", -180),
            new TimeZoneData("US06", -480),
            new TimeZoneData("ST01", 0),
            new TimeZoneData("CD01", 60),
            new TimeZoneData("MM01", 390),
            new TimeZoneData("US02", -300),
            new TimeZoneData("EC02", -360),
            new TimeZoneData("GF01", -180),
            new TimeZoneData("KH01", 420),
            new TimeZoneData("UM03", 720),
            new TimeZoneData("SL01", 0),
            new TimeZoneData("SD01", 120),
            new TimeZoneData("BS01", -300),
            new TimeZoneData("WF01", 720),
            new TimeZoneData("UA01", 120),
            new TimeZoneData("HR01", 60),
            new TimeZoneData("RU06", 420),
            new TimeZoneData("LT01", 60),
            new TimeZoneData("FM01", 600),
            new TimeZoneData("JO01", 120),
            new TimeZoneData("RU02", 180),
            new TimeZoneData("VU01", 660),
            new TimeZoneData("ZW01", 120),
            new TimeZoneData("TP01", 540),
            new TimeZoneData("NI01", -360),
            new TimeZoneData("AZ01", 240),
            new TimeZoneData("CY02", 120),
            new TimeZoneData("PF01", -570),
            new TimeZoneData("TH01", 420),
            new TimeZoneData("NA01", 60),
            new TimeZoneData("GY01", -240),
            new TimeZoneData("AR01", -180),
            new TimeZoneData("VE01", -240),
            new TimeZoneData("ET01", 180),
            new TimeZoneData("CO01", -300),
            new TimeZoneData("MX01", -360),
            new TimeZoneData("GQ01", 60),
            new TimeZoneData("IN01", 330),
            new TimeZoneData("CG01", 60),
            new TimeZoneData("CA06", -480),
            new TimeZoneData("MP01", 600),
            new TimeZoneData("GI01", 60),
            new TimeZoneData("CA02", -240),
            new TimeZoneData("OM01", 240),
            new TimeZoneData("SO01", 180),
            new TimeZoneData("MH01", 720),
            new TimeZoneData("GA01", 60),
            new TimeZoneData("SG01", 480),
            new TimeZoneData("BR03", -300),
            new TimeZoneData("HU01", 60),
            new TimeZoneData("BN01", 480),
            new TimeZoneData("PY01", -240),
            new TimeZoneData("DK01", 60),
            new TimeZoneData("BF01", 0),
            new TimeZoneData("NL01", 60),
            new TimeZoneData("AU05", 570),
            new TimeZoneData("CZ01", 60),
            new TimeZoneData("AU01", 630),
            new TimeZoneData("PA01", -300),
            new TimeZoneData("KZ03", 360),
            new TimeZoneData("TC01", -300),
            new TimeZoneData("CR01", -360),
            new TimeZoneData("GT01", -360),
            new TimeZoneData("AM01", 240),
            new TimeZoneData("CL02", -360),
            new TimeZoneData("IQ01", 180),
            new TimeZoneData("SZ01", 120),
            new TimeZoneData("MS01", -240),
            new TimeZoneData("GL01", -180),
            new TimeZoneData("US07", -540),
            new TimeZoneData("AE01", 240),
            new TimeZoneData("KN01", -240),
            new TimeZoneData("EG01", 120),
            new TimeZoneData("CD02", 120),
            new TimeZoneData("RU10", 660),
            new TimeZoneData("US03", -360),
            new TimeZoneData("SR01", -180),
            new TimeZoneData("MK01", 60),
            new TimeZoneData("GD01", -240),
            new TimeZoneData("SJ01", 60),
            new TimeZoneData("BY01", 120),
            new TimeZoneData("MC01", 60),
            new TimeZoneData("UG01", 180),
            new TimeZoneData("SB01", 660),
            new TimeZoneData("RU07", 480),
            new TimeZoneData("FM02", 660),
            new TimeZoneData("BI01", 120),
            new TimeZoneData("LR01", 0),
            new TimeZoneData("RU03", 240),
            new TimeZoneData("FK01", -240),
            new TimeZoneData("PT01", 0),
            new TimeZoneData("JM01", -300),
            new TimeZoneData("TV01", 720),
            new TimeZoneData("NO01", 60),
            new TimeZoneData("BA01", 60),
            new TimeZoneData("PL01", 60),
            new TimeZoneData("CA10", -360),
            new TimeZoneData("TN01", 60),
            new TimeZoneData("NG01", 60),
            new TimeZoneData("PF02", -600),
            new TimeZoneData("LB01", 120),
            new TimeZoneData("ZM01", 120),
            new TimeZoneData("CU01", -300),
            new TimeZoneData("GW01", 0),
            new TimeZoneData("KY01", -300),
            new TimeZoneData("VC01", -240),
            new TimeZoneData("ER01", 180),
            new TimeZoneData("MX02", -420),
            new TimeZoneData("IT01", 60),
            new TimeZoneData("CM01", 60),
            new TimeZoneData("MV01", 300),
            new TimeZoneData("UZ01", 300),
            new TimeZoneData("CA07", 0),
            new TimeZoneData("IL01", 120),
            new TimeZoneData("MN01", 480),
            new TimeZoneData("CA03", -300),
            new TimeZoneData("KI01", 720),
            new TimeZoneData("YT01", 180),
            new TimeZoneData("ID01", 420),
            new TimeZoneData("SM01", 60),
            new TimeZoneData("BR04", -120),
            new TimeZoneData("SE01", 60),
            new TimeZoneData("BT01", 360),
            new TimeZoneData("NZ01", 720),
            new TimeZoneData("LU01", 60),
            new TimeZoneData("PW01", 540),
            new TimeZoneData("JP01", 540),
            new TimeZoneData("NR01", 720),
            new TimeZoneData("HK01", 480),
            new TimeZoneData("BD01", 360),
            new TimeZoneData("AU06", 480),
            new TimeZoneData("VN01", 420),
            new TimeZoneData("PG01", 600),
            new TimeZoneData("CX01", 420),
            new TimeZoneData("AU02", 600),
            new TimeZoneData("AS01", -660),
            new TimeZoneData("MY01", 480),
            new TimeZoneData("GR01", 120),
            new TimeZoneData("GL02", -240),
            new TimeZoneData("US08", -600),
            new TimeZoneData("CH01", 60),
            new TimeZoneData("MQ01", -240),
            new TimeZoneData("RU11", 720),
            new TimeZoneData("US04", -420),
            new TimeZoneData("EE01", 120),
            new TimeZoneData("GB01", 0),
            new TimeZoneData("UM01", -600),
            new TimeZoneData("SH01", 0),
            new TimeZoneData("BW01", 120),
            new TimeZoneData("MA01", 0),
            new TimeZoneData("BO01", -240),
            new TimeZoneData("RU08", 540),
            new TimeZoneData("NU01", -660),
            new TimeZoneData("RU04", 300),
            new TimeZoneData("HN01", -360),
            new TimeZoneData("RW01", 120),
            new TimeZoneData("PT02", 0),
            new TimeZoneData("BG01", 120),
            new TimeZoneData("FI01", 120),
            new TimeZoneData("PR01", -240),
            new TimeZoneData("TT01", -240),
            new TimeZoneData("XT01", 120),
    };

    private void generateTimeZones() {
        int count = 0;
        for (TimeZoneData timeZoneData : timeZoneDataTable) {
            createTimeZone(timeZoneData.getId(), timeZoneData.getTimeZoneCode(), timeZoneData.getGMTOffset());
            count++;
        }
        logger.info("Generated time zones: {}", count);
    }

    class TimeZoneData {
        private int id;
        private String timeZoneCode;
        private int GMTOffset;
        private AtomicInteger atomicId = new AtomicInteger(0);

        public String getTimeZoneCode() {
            return timeZoneCode;
        }

        public int getGMTOffset() {
            return GMTOffset;
        }

        private int getId() {
            return id;
        }

        TimeZoneData(String timeZoneCode, int offset) {
            this.id = atomicId.incrementAndGet();
            this.timeZoneCode = timeZoneCode;
            this.GMTOffset = offset;
        }
    }
}
