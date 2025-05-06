package tools.jackson.datatype.joda.ser;

import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.jupiter.api.Test;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.datatype.joda.JodaTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;

// [dataformat-joda#92] DateTime serialization result is not same as Java 8 ZonedDateTime
public class DateTimeOwnZoneSerialization92Test
        extends JodaTestBase
{
    private final ObjectMapper MAPPER = mapperWithModuleBuilder().disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS).build();

    @Test
    public void dateTimeShouldRetainItsOwnZone() throws Exception {
        DateTime jodaZonedDateTime = new DateTime(2023, 10, 1, 12, 2, 3, 123, DateTimeZone.forID("Asia/Shanghai"));

        // without WRITE_DATES_WITH_CONTEXT_TIME_ZONE
        assertEquals("\"2023-10-01T12:02:03.123+08:00\"",
                MAPPER.writer()
                        .with(TimeZone.getTimeZone("UTC"))
                        .without(DateTimeFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE)
                        .writeValueAsString(jodaZonedDateTime));

        // with WRITE_DATES_WITH_CONTEXT_TIME_ZONE
        assertEquals("\"2023-10-01T04:02:03.123Z\"",
                MAPPER.writer()
                        .with(TimeZone.getTimeZone("UTC"))
                        .with(DateTimeFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE)
                        .writeValueAsString(jodaZonedDateTime));
    }
}
