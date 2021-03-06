package org.postgresql.sql2.testUtil;

import jdk.incubator.sql2.Result;
import org.postgresql.sql2.util.PGCount;

import java.util.List;
import java.util.stream.Collector;

public class CollectorUtils {
  public static<T> Collector<Result.RowColumn, T[], T> singleCollector(Class<T> clazz) {
    return Collector.of(
        () -> (T[])new Object[1],
        (a, r) -> a[0] = r.at("t").get(clazz),
        (l, r) -> null,
        a -> a[0]);
  }

  public static Collector<Integer, Integer[], Integer> summingCollector() {
    return Collector.of(
        () -> new Integer[] {0},
        (a, r) -> a[0] += r,
        (l, r) -> null,
        a -> a[0]);
  }

  public static Collector<PGCount, ?, Integer> summingCountCollector() {
    return Collector.of(
        () -> new Integer[] {0},
        (a, r) -> a[0] += (int)r.getCount(),
        (l, r) -> null,
        a -> a[0]);
  }

  public static Collector<List<PGCount>, ?, Integer> summingCountListCollector() {
    return Collector.of(
        () -> new Integer[] {0},
        (a, r) -> r.stream().forEach(c -> a[0] += (int)c.getCount()),
        (l, r) -> null,
        a -> a[0]);
  }
}
