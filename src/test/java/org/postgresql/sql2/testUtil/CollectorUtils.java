package org.postgresql.sql2.testUtil;

import jdk.incubator.sql2.Result;

import java.util.stream.Collector;

public class CollectorUtils {
  public static<T> Collector<Result.Row, T[], T> singleCollector(Class<T> clazz) {
    return Collector.of(
        () -> (T[])new Object[1],
        (a, r) -> a[0] = r.get("t", clazz),
        (l, r) -> null,
        a -> a[0]);
  }
}