package org.postgresql.sql2.communication.packets.parsers;

import jdk.incubator.sql2.SqlException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.StringTokenizer;

public class TextParser {
  private static final DateTimeFormatter timestampWithoutTimeZoneFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
  private static final DateTimeFormatter timestampWithTimeZoneFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSX");
  private static final DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter localTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS");
  private static final DateTimeFormatter offsetTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSSX");

  public static Object boolout(String in) {
    return in.equals("t");
  }

  public static Object byteaout(String in) {
    int len = in.length() - 2;
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[i / 2] = (byte) ((Character.digit(in.charAt(i + 2), 16) << 4)
          + Character.digit(in.charAt(i + 3), 16));
    }
    return data;
  }

  public static Object charout(String in) {
    return null;
  }

  public static Object nameout(String in) {
    return null;
  }

  public static Object int8out(String in) {
    return Long.parseLong(in);
  }

  public static Object int2out(String in) {
    return Short.parseShort(in);
  }

  public static Object int2vectorout(String in) {
    return null;
  }

  public static Object int4out(String in) {
    return Integer.parseInt(in);
  }

  public static Object regprocout(String in) {
    return null;
  }

  public static Object oidout(String in) {
    return null;
  }

  public static Object tidout(String in) {
    return null;
  }

  public static Object xidout(String in) {
    return null;
  }

  public static Object cidout(String in) {
    return null;
  }

  public static Object oidvectorout(String in) {
    return null;
  }

  public static Object pg_ddl_command_out(String in) {
    return null;
  }

  public static Object json_out(String in) {
    return null;
  }

  public static Object xml_out(String in) {
    return null;
  }

  public static Object pg_node_tree_out(String in) {
    return null;
  }

  public static Object smgrout(String in) {
    return null;
  }

  public static Object point_out(String in) {
    return null;
  }

  public static Object lseg_out(String in) {
    return null;
  }

  public static Object path_out(String in) {
    return null;
  }

  public static Object box_out(String in) {
    return null;
  }

  public static Object poly_out(String in) {
    return null;
  }

  public static Object line_out(String in) {
    return null;
  }

  public static Object cidr_out(String in) {
    return null;
  }

  public static Object float4out(String in) {
    return Float.parseFloat(in);
  }

  public static Object float8out(String in) {
    return Double.parseDouble(in);
  }

  public static Object abstimeout(String in) {
    return null;
  }

  public static Object reltimeout(String in) {
    return null;
  }

  public static Object tintervalout(String in) {
    return null;
  }

  public static Object unknownout(String in) {
    return null;
  }

  public static Object circle_out(String in) {
    return null;
  }

  public static Object cash_out(String in) {
    return in;
  }

  public static Object macaddr_out(String in) {
    return null;
  }

  public static Object inet_out(String in) {
    return null;
  }

  public static Object aclitemout(String in) {
    return null;
  }

  public static Object bpcharout(String in) {
    return in.charAt(0);
  }

  public static Object varcharout(String in) {
    return in;
  }

  public static Object date_out(String in) {
    return LocalDate.parse(in, localDateFormatter);
  }

  public static Object time_out(String in) {
    return LocalTime.parse(in, localTimeFormatter);
  }

  public static Object timestamp_out(String in) {
    return LocalDateTime.parse(in, timestampWithoutTimeZoneFormatter);
  }

  public static Object timestamptz_out(String in) {
    return OffsetDateTime.parse(in, timestampWithTimeZoneFormatter);
  }

  public static Object interval_out(String in) {
    final boolean ISOFormat = !in.startsWith("@");

    // Just a simple '0'
    if (!ISOFormat && in.length() == 3 && in.charAt(2) == '0') {
      return Duration.of(0, ChronoUnit.MICROS);
    }

    int years = 0;
    int months = 0;
    int days = 0;
    int hours = 0;
    int minutes = 0;
    double seconds = 0;

    try {
      String valueToken = null;

      in = in.replace('+', ' ').replace('@', ' ');
      final StringTokenizer st = new StringTokenizer(in);
      for (int i = 1; st.hasMoreTokens(); i++) {
        String token = st.nextToken();

        if ((i & 1) == 1) {
          int endHours = token.indexOf(':');
          if (endHours == -1) {
            valueToken = token;
            continue;
          }

          // This handles hours, minutes, seconds and microseconds for
          // ISO intervals
          int offset = (token.charAt(0) == '-') ? 1 : 0;

          hours = nullSafeIntGet(token.substring(offset + 0, endHours));
          minutes = nullSafeIntGet(token.substring(endHours + 1, endHours + 3));

          // Pre 7.4 servers do not put second information into the results
          // unless it is non-zero.
          int endMinutes = token.indexOf(':', endHours + 1);
          if (endMinutes != -1) {
            seconds = nullSafeDoubleGet(token.substring(endMinutes + 1));
          }

          if (offset == 1) {
            hours = -hours;
            minutes = -minutes;
            seconds = -seconds;
          }

          valueToken = null;
        } else {
          // This handles years, months, days for both, ISO and
          // Non-ISO intervals. Hours, minutes, seconds and microseconds
          // are handled for Non-ISO intervals here.

          if (token.startsWith("year")) {
            years = nullSafeIntGet(valueToken);
          } else if (token.startsWith("mon")) {
            months = nullSafeIntGet(valueToken);
          } else if (token.startsWith("day")) {
            days = nullSafeIntGet(valueToken);
          } else if (token.startsWith("hour")) {
            hours = nullSafeIntGet(valueToken);
          } else if (token.startsWith("min")) {
            minutes = nullSafeIntGet(valueToken);
          } else if (token.startsWith("sec")) {
            seconds = nullSafeDoubleGet(valueToken);
          }
        }
      }
    } catch (NumberFormatException e) {
      throw new SqlException("Conversion of interval failed", e, "", 0, "", 0);
    }

    if (!ISOFormat && in.endsWith("ago")) {
      // Inverse the leading sign
      return Duration.of((long)((years * -31556952000000L) + (months * -2592000000000L) + (days * -86400000000L) + (hours * -3600000000L) +
          (minutes * -60000000L) + (seconds * -1000000L)), ChronoUnit.MICROS);
    } else {
      return Duration.of((long)((years * 31556952000000L) + (months * 2592000000000L) + (days * 86400000000L) + (hours * 3600000000L) +
          (minutes * 60000000L) + (seconds * 1000000L)), ChronoUnit.MICROS);
    }
  }

  public static Object timetz_out(String in) {
    return OffsetTime.parse(in, offsetTimeFormatter);
  }

  public static Object bit_out(String in) {
    return null;
  }

  public static Object varbit_out(String in) {
    return null;
  }

  public static Object numeric_out(String in) {
    return new BigDecimal(in);
  }

  public static Object textout(String in) {
    return in;
  }

  public static Object regprocedureout(String in) {
    return null;
  }

  public static Object regoperout(String in) {
    return null;
  }

  public static Object regoperatorout(String in) {
    return null;
  }

  public static Object regclassout(String in) {
    return null;
  }

  public static Object regtypeout(String in) {
    return null;
  }

  public static Object cstring_out(String in) {
    return null;
  }

  public static Object any_out(String in) {
    return null;
  }

  public static Object anyarray_out(String in) {
    return null;
  }

  public static Object void_out(String in) {
    return null;
  }

  public static Object trigger_out(String in) {
    return null;
  }

  public static Object language_handler_out(String in) {
    return null;
  }

  public static Object internal_out(String in) {
    return null;
  }

  public static Object opaque_out(String in) {
    return null;
  }

  public static Object anyelement_out(String in) {
    return null;
  }

  public static Object anynonarray_out(String in) {
    return null;
  }

  public static Object uuid_out(String in) {
    return null;
  }

  public static Object txid_snapshot_out(String in) {
    return null;
  }

  public static Object fdw_handler_out(String in) {
    return null;
  }

  public static Object pg_lsn_out(String in) {
    return null;
  }

  public static Object tsm_handler_out(String in) {
    return null;
  }

  public static Object anyenum_out(String in) {
    return null;
  }

  public static Object tsvectorout(String in) {
    return null;
  }

  public static Object tsqueryout(String in) {
    return null;
  }

  public static Object gtsvectorout(String in) {
    return null;
  }

  public static Object regconfigout(String in) {
    return null;
  }

  public static Object regdictionaryout(String in) {
    return null;
  }

  public static Object jsonb_out(String in) {
    return null;
  }

  public static Object anyrange_out(String in) {
    return null;
  }

  public static Object event_trigger_out(String in) {
    return null;
  }

  public static Object range_out(String in) {
    return null;
  }

  public static Object regnamespaceout(String in) {
    return null;
  }

  public static Object regroleout(String in) {
    return null;
  }

  public static Object array_out(String in) {
    return null;
  }

  public static Object record_out(String in) {
    return null;
  }

  public static Object passthrough(String in) {
    return in;
  }

  /**
   * Returns integer value of value or 0 if value is null
   *
   * @param value integer as string value
   * @return integer parsed from string value
   * @throws NumberFormatException if the string contains invalid chars
   */
  private static int nullSafeIntGet(String value) throws NumberFormatException {
    return (value == null) ? 0 : Integer.parseInt(value);
  }

  /**
   * Returns double value of value or 0 if value is null
   *
   * @param value double as string value
   * @return double parsed from string value
   * @throws NumberFormatException if the string contains invalid chars
   */
  private static double nullSafeDoubleGet(String value) throws NumberFormatException {
    return (value == null) ? 0 : Double.parseDouble(value);
  }
}
