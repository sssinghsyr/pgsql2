package org.postgresql.sql2.communication.packets.parts;

import org.postgresql.sql2.communication.packets.parsers.BinaryParser;
import org.postgresql.sql2.communication.packets.parsers.TextParser;
import org.postgresql.sql2.util.TriFunction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.function.Function;

public enum ColumnTypes {
  BOOL(16, TextParser::boolout, BinaryParser::boolsend, Boolean.class, PGAdbaType.BOOLEAN),
  BYTEA(17, TextParser::byteaout, BinaryParser::byteasend, byte[].class, PGAdbaType.BINARY),
  CHAR(18, TextParser::charout, BinaryParser::charsend, Character.class, PGAdbaType.CHAR),
  NAME(19, TextParser::nameout, BinaryParser::namesend, String.class, PGAdbaType.VARCHAR),
  INT8(20, TextParser::int8out, BinaryParser::int8send, Long.class, PGAdbaType.BIGINT),
  INT2(21, TextParser::int2out, BinaryParser::int2send, Short.class, PGAdbaType.SMALLINT),
  INT2VECTOR(22, TextParser::int2vectorout, BinaryParser::int2vectorsend, int[].class, PGAdbaType.ARRAY),
  INT4(23, TextParser::int4out, BinaryParser::int4send, Integer.class, PGAdbaType.INTEGER),
  REGPROC(24, TextParser::regprocout, BinaryParser::regprocsend, null, null),
  TEXT(25, TextParser::textout, BinaryParser::textsend, String.class, PGAdbaType.LONGVARCHAR),
  OID(26, TextParser::oidout, BinaryParser::oidsend, null, null),
  TID(27, TextParser::tidout, BinaryParser::tidsend, null, null),
  XID(28, TextParser::xidout, BinaryParser::xidsend, null, null),
  CID(29, TextParser::cidout, BinaryParser::cidsend, null, null),
  OIDVECTOR(30, TextParser::oidvectorout, BinaryParser::oidvectorsend, null, null),
  PG_DDL_COMMAND(32, TextParser::pg_ddl_command_out, BinaryParser::pg_ddl_command_send, null, null),
  PG_TYPE(71, TextParser::record_out, BinaryParser::record_send, null, null),
  PG_ATTRIBUTE(75, TextParser::record_out, BinaryParser::record_send, null, null),
  PG_PROC(81, TextParser::record_out, BinaryParser::record_send, null, null),
  PG_CLASS(83, TextParser::record_out, BinaryParser::record_send, null, null),
  JSON(114, TextParser::json_out, BinaryParser::json_send, null, null),
  XML(142, TextParser::xml_out, BinaryParser::xml_send, null, null),
  _XML(143, TextParser::array_out, BinaryParser::array_send, null, null),
  PG_NODE_TREE(194, TextParser::pg_node_tree_out, BinaryParser::pg_node_tree_send, null, null),
  _JSON(199, TextParser::array_out, BinaryParser::array_send, null, null),
  SMGR(210, TextParser::smgrout, null, null, null),
  POINT(600, TextParser::point_out, BinaryParser::point_send, null, null),
  LSEG(601, TextParser::lseg_out, BinaryParser::lseg_send, null, null),
  PATH(602, TextParser::path_out, BinaryParser::path_send, null, null),
  BOX(603, TextParser::box_out, BinaryParser::box_send, null, null),
  POLYGON(604, TextParser::poly_out, BinaryParser::poly_send, null, null),
  LINE(628, TextParser::line_out, BinaryParser::line_send, null, null),
  _LINE(629, TextParser::array_out, BinaryParser::array_send, null, null),
  CIDR(650, TextParser::cidr_out, BinaryParser::cidr_send, null, null),
  _CIDR(651, TextParser::array_out, BinaryParser::array_send, null, null),
  FLOAT4(700, TextParser::float4out, BinaryParser::float4send, Float.class, PGAdbaType.FLOAT),
  FLOAT8(701, TextParser::float8out, BinaryParser::float8send, Double.class, PGAdbaType.DOUBLE),
  ABSTIME(702, TextParser::abstimeout, BinaryParser::abstimesend, null, null),
  RELTIME(703, TextParser::reltimeout, BinaryParser::reltimesend, null, null),
  TINTERVAL(704, TextParser::tintervalout, BinaryParser::tintervalsend, null, null),
  UNKNOWN(705, TextParser::unknownout, BinaryParser::unknownsend, null, null),
  CIRCLE(718, TextParser::circle_out, BinaryParser::circle_send, null, null),
  _CIRCLE(719, TextParser::array_out, BinaryParser::array_send, null, null),
  MONEY(790, TextParser::cash_out, BinaryParser::cash_send, null, null),
  _MONEY(791, TextParser::array_out, BinaryParser::array_send, null, null),
  MACADDR(829, TextParser::macaddr_out, BinaryParser::macaddr_send, null, null),
  INET(869, TextParser::inet_out, BinaryParser::inet_send, null, null),
  _BOOL(1000, TextParser::array_out, BinaryParser::array_send, boolean[].class, PGAdbaType.ARRAY),
  _BYTEA(1001, TextParser::array_out, BinaryParser::array_send, byte[].class, PGAdbaType.BINARY),
  _CHAR(1002, TextParser::array_out, BinaryParser::array_send, char[].class, PGAdbaType.ARRAY),
  _NAME(1003, TextParser::array_out, BinaryParser::array_send, String[].class, PGAdbaType.ARRAY),
  _INT2(1005, TextParser::array_out, BinaryParser::array_send, short[].class, PGAdbaType.ARRAY),
  _INT2VECTOR(1006, TextParser::array_out, BinaryParser::array_send, short[].class, PGAdbaType.ARRAY),
  _INT4(1007, TextParser::array_out, BinaryParser::array_send, int[].class, PGAdbaType.ARRAY),
  _REGPROC(1008, TextParser::array_out, BinaryParser::array_send, null, null),
  _TEXT(1009, TextParser::array_out, BinaryParser::array_send, String.class, PGAdbaType.ARRAY),
  _TID(1010, TextParser::array_out, BinaryParser::array_send, null, null),
  _XID(1011, TextParser::array_out, BinaryParser::array_send, null, null),
  _CID(1012, TextParser::array_out, BinaryParser::array_send, null, null),
  _OIDVECTOR(1013, TextParser::array_out, BinaryParser::array_send, null, null),
  _BPCHAR(1014, TextParser::array_out, BinaryParser::array_send, String.class, PGAdbaType.VARCHAR),
  _VARCHAR(1015, TextParser::array_out, BinaryParser::array_send, String.class, PGAdbaType.VARCHAR),
  _INT8(1016, TextParser::array_out, BinaryParser::array_send, long[].class, PGAdbaType.VARCHAR),
  _POINT(1017, TextParser::array_out, BinaryParser::array_send, null, null),
  _LSEG(1018, TextParser::array_out, BinaryParser::array_send, null, null),
  _PATH(1019, TextParser::array_out, BinaryParser::array_send, null, null),
  _BOX(1020, TextParser::array_out, BinaryParser::array_send, null, null),
  _FLOAT4(1021, TextParser::array_out, BinaryParser::array_send, float[].class, PGAdbaType.VARCHAR),
  _FLOAT8(1022, TextParser::array_out, BinaryParser::array_send, double[].class, PGAdbaType.VARCHAR),
  _ABSTIME(1023, TextParser::array_out, BinaryParser::array_send, null, null),
  _RELTIME(1024, TextParser::array_out, BinaryParser::array_send, null, null),
  _TINTERVAL(1025, TextParser::array_out, BinaryParser::array_send, null, null),
  _POLYGON(1027, TextParser::array_out, BinaryParser::array_send, null, null),
  _OID(1028, TextParser::array_out, BinaryParser::array_send, null, null),
  ACLITEM(1033, TextParser::aclitemout, null, null, null),
  _ACLITEM(1034, TextParser::array_out, BinaryParser::array_send, null, null),
  _MACADDR(1040, TextParser::array_out, BinaryParser::array_send, null, null),
  _INET(1041, TextParser::array_out, BinaryParser::array_send, null, null),
  BPCHAR(1042, TextParser::bpcharout, BinaryParser::bpcharsend, null, null),
  VARCHAR(1043, TextParser::varcharout, BinaryParser::varcharsend, String.class, PGAdbaType.VARCHAR),
  DATE(1082, TextParser::date_out, BinaryParser::date_send, LocalDate.class, PGAdbaType.DATE),
  TIME(1083, TextParser::time_out, BinaryParser::time_send, LocalTime.class, PGAdbaType.TIME),
  TIMESTAMP(1114, TextParser::timestamp_out, BinaryParser::timestamp_send, LocalDateTime.class, PGAdbaType.TIMESTAMP),
  _TIMESTAMP(1115, TextParser::array_out, BinaryParser::array_send, LocalDateTime[].class, PGAdbaType.ARRAY),
  _DATE(1182, TextParser::array_out, BinaryParser::array_send, LocalDate[].class, PGAdbaType.ARRAY),
  _TIME(1183, TextParser::array_out, BinaryParser::array_send, LocalTime.class, PGAdbaType.ARRAY),
  TIMESTAMPTZ(1184, TextParser::timestamptz_out, BinaryParser::timestamptz_send, OffsetDateTime.class, PGAdbaType.TIMESTAMP_WITH_TIME_ZONE),
  _TIMESTAMPTZ(1185, TextParser::array_out, BinaryParser::array_send, OffsetDateTime[].class, PGAdbaType.ARRAY),
  INTERVAL(1186, TextParser::interval_out, BinaryParser::interval_send, null, null),
  _INTERVAL(1187, TextParser::array_out, BinaryParser::array_send, null, null),
  _NUMERIC(1231, TextParser::array_out, BinaryParser::array_send, BigDecimal[].class, PGAdbaType.ARRAY),
  PG_DATABASE(1248, TextParser::record_out, BinaryParser::record_send, null, null),
  _CSTRING(1263, TextParser::array_out, BinaryParser::array_send, null, null),
  TIMETZ(1266, TextParser::timetz_out, BinaryParser::timetz_send, OffsetTime.class, PGAdbaType.TIME_WITH_TIME_ZONE),
  _TIMETZ(1270, TextParser::array_out, BinaryParser::array_send, OffsetTime[].class, PGAdbaType.ARRAY),
  BIT(1560, TextParser::bit_out, BinaryParser::bit_send, null, null),
  _BIT(1561, TextParser::array_out, BinaryParser::array_send, null, null),
  VARBIT(1562, TextParser::varbit_out, BinaryParser::varbit_send, null, null),
  _VARBIT(1563, TextParser::array_out, BinaryParser::array_send, null, null),
  NUMERIC(1700, TextParser::numeric_out, BinaryParser::numeric_send, BigDecimal.class, PGAdbaType.NUMERIC),
  REFCURSOR(1790, TextParser::textout, BinaryParser::textsend, null, null),
  _REFCURSOR(2201, TextParser::array_out, BinaryParser::array_send, null, null),
  REGPROCEDURE(2202, TextParser::regprocedureout, BinaryParser::regproceduresend, null, null),
  REGOPER(2203, TextParser::regoperout, BinaryParser::regopersend, null, null),
  REGOPERATOR(2204, TextParser::regoperatorout, BinaryParser::regoperatorsend, null, null),
  REGCLASS(2205, TextParser::regclassout, BinaryParser::regclasssend, null, null),
  REGTYPE(2206, TextParser::regtypeout, BinaryParser::regtypesend, null, null),
  _REGPROCEDURE(2207, TextParser::array_out, BinaryParser::array_send, null, null),
  _REGOPER(2208, TextParser::array_out, BinaryParser::array_send, null, null),
  _REGOPERATOR(2209, TextParser::array_out, BinaryParser::array_send, null, null),
  _REGCLASS(2210, TextParser::array_out, BinaryParser::array_send, null, null),
  _REGTYPE(2211, TextParser::array_out, BinaryParser::array_send, null, null),
  RECORD(2249, TextParser::record_out, BinaryParser::record_send, null, null),
  CSTRING(2275, TextParser::cstring_out, BinaryParser::cstring_send, null, null),
  ANY(2276, TextParser::any_out, null, null, null),
  ANYARRAY(2277, TextParser::anyarray_out, BinaryParser::anyarray_send, null, null),
  VOID(2278, TextParser::void_out, BinaryParser::void_send, null, null),
  TRIGGER(2279, TextParser::trigger_out, null, null, null),
  LANGUAGE_HANDLER(2280, TextParser::language_handler_out, null, null, null),
  INTERNAL(2281, TextParser::internal_out, null, null, null),
  OPAQUE(2282, TextParser::opaque_out, null, null, null),
  ANYELEMENT(2283, TextParser::anyelement_out, null, null, null),
  _RECORD(2287, TextParser::array_out, BinaryParser::array_send, null, null),
  ANYNONARRAY(2776, TextParser::anynonarray_out, null, null, null),
  PG_AUTHID(2842, TextParser::record_out, BinaryParser::record_send, null, null),
  PG_AUTH_MEMBERS(2843, TextParser::record_out, BinaryParser::record_send, null, null),
  _TXID_SNAPSHOT(2949, TextParser::array_out, BinaryParser::array_send, null, null),
  UUID(2950, TextParser::uuid_out, BinaryParser::uuid_send, null, null),
  _UUID(2951, TextParser::array_out, BinaryParser::array_send, null, null),
  TXID_SNAPSHOT(2970, TextParser::txid_snapshot_out, BinaryParser::txid_snapshot_send, null, null),
  FDW_HANDLER(3115, TextParser::fdw_handler_out, null, null, null),
  PG_LSN(3220, TextParser::pg_lsn_out, BinaryParser::pg_lsn_send, null, null),
  _PG_LSN(3221, TextParser::array_out, BinaryParser::array_send, null, null),
  TSM_HANDLER(3310, TextParser::tsm_handler_out, null, null, null),
  ANYENUM(3500, TextParser::anyenum_out, null, null, null),
  TSVECTOR(3614, TextParser::tsvectorout, BinaryParser::tsvectorsend, null, null),
  TSQUERY(3615, TextParser::tsqueryout, BinaryParser::tsquerysend, null, null),
  GTSVECTOR(3642, TextParser::gtsvectorout, null, null, null),
  _TSVECTOR(3643, TextParser::array_out, BinaryParser::array_send, null, null),
  _GTSVECTOR(3644, TextParser::array_out, BinaryParser::array_send, null, null),
  _TSQUERY(3645, TextParser::array_out, BinaryParser::array_send, null, null),
  REGCONFIG(3734, TextParser::regconfigout, BinaryParser::regconfigsend, null, null),
  _REGCONFIG(3735, TextParser::array_out, BinaryParser::array_send, null, null),
  REGDICTIONARY(3769, TextParser::regdictionaryout, BinaryParser::regdictionarysend, null, null),
  _REGDICTIONARY(3770, TextParser::array_out, BinaryParser::array_send, null, null),
  JSONB(3802, TextParser::jsonb_out, BinaryParser::jsonb_send, null, null),
  _JSONB(3807, TextParser::array_out, BinaryParser::array_send, null, null),
  ANYRANGE(3831, TextParser::anyrange_out, null, null, null),
  EVENT_TRIGGER(3838, TextParser::event_trigger_out, null, null, null),
  INT4RANGE(3904, TextParser::range_out, BinaryParser::range_send, null, null),
  _INT4RANGE(3905, TextParser::array_out, BinaryParser::array_send, null, null),
  NUMRANGE(3906, TextParser::range_out, BinaryParser::range_send, null, null),
  _NUMRANGE(3907, TextParser::array_out, BinaryParser::array_send, null, null),
  TSRANGE(3908, TextParser::range_out, BinaryParser::range_send, null, null),
  _TSRANGE(3909, TextParser::array_out, BinaryParser::array_send, null, null),
  TSTZRANGE(3910, TextParser::range_out, BinaryParser::range_send, null, null),
  _TSTZRANGE(3911, TextParser::array_out, BinaryParser::array_send, null, null),
  DATERANGE(3912, TextParser::range_out, BinaryParser::range_send, null, null),
  _DATERANGE(3913, TextParser::array_out, BinaryParser::array_send, null, null),
  INT8RANGE(3926, TextParser::range_out, BinaryParser::range_send, null, null),
  _INT8RANGE(3927, TextParser::array_out, BinaryParser::array_send, null, null),
  REGNAMESPACE(4089, TextParser::regnamespaceout, BinaryParser::regnamespacesend, null, null),
  _REGNAMESPACE(4090, TextParser::array_out, BinaryParser::array_send, null, null),
  REGROLE(4096, TextParser::regroleout, BinaryParser::regrolesend, null, null),
  _REGROLE(4097, TextParser::array_out, BinaryParser::array_send, null, null),
  OTHER(0, TextParser::passthrough, null, null, null);

  private final int oid;
  private final Function<String, Object> textParser;
  private final TriFunction<byte[], Integer, Integer, Object> binaryParser;
  private final Class c;
  private final PGAdbaType type;

  ColumnTypes(int oid, Function<String, Object> textParser, TriFunction<byte[], Integer, Integer, Object> binaryParser, Class c, PGAdbaType type) {
    this.oid = oid;
    this.textParser = textParser;
    this.binaryParser = binaryParser;
    this.c = c;
    this.type = type;
  }

  public static ColumnTypes lookup(int oid) {
    for(ColumnTypes ct : values()) {
      if(ct.oid == oid)
        return ct;
    }

    return OTHER;
  }

  public Function<String, Object> getTextParser() {
    return textParser;
  }

  public TriFunction<byte[], Integer, Integer, Object> getBinaryParser() {
    return binaryParser;
  }

  public Class javaType() {
    return c;
  }

  public PGAdbaType sqlType() {
    return type;
  }
}
