/*
 * Copyright (c) 2018, PostgreSQL Global Development Group
 * See the LICENSE file in the project root for more information.
 */

package org.postgresql.sql2;

import jdk.incubator.sql2.Connection;
import jdk.incubator.sql2.ConnectionProperty;
import jdk.incubator.sql2.DataSource;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PGDataSource implements DataSource {
  //private Queue<PGConnection> connections = new ConcurrentLinkedQueue<>();
  private ConcurrentHashMap<Integer,PGConnection> connections = new ConcurrentHashMap<Integer,PGConnection>();
  private boolean closed;
  private Map<ConnectionProperty, Object> properties;
  private Selector selector;
  private static int connectionIdx = 1;

  public PGDataSource(Map<ConnectionProperty, Object> properties) {
    this.properties = properties;
    try {
		this.selector = Selector.open();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    Executor executor = new ThreadPoolExecutor(1, 2, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
    executor.execute(() -> {
//      for( PGConnection connection : connections.values()) {
//    	  connection.connectDb();
//      }
      while (selector.isOpen()) {
        try {
			if(selector.select(1000) > 0) {
				processSelectedKeys(selector.selectedKeys());
			}else {
				System.out.println("Timeout after 1 sec");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
    });
  }

  private void processSelectedKeys(Set<SelectionKey> selectedKeys) {
	  Iterator<SelectionKey> iterator = selectedKeys.iterator();
	  while (iterator.hasNext()) {
			SelectionKey key = (SelectionKey) iterator.next();
			iterator.remove();
			if (key.isReadable()) {
				connections.get(key.attachment()).read();
			}
			if (key.isWritable()) {
				connections.get(key.attachment()).visit();
			}
			if (key.isConnectable()) {
				connections.get(key.attachment()).processConnect();
			}
		}
	
  }

/**
   * Returns a {@link Connection} builder. By default that builder will return
   * {@link Connection}s with the {@code ConnectionProperty}s specified when creating this
   * DataSource. Default and unspecified {@link ConnectionProperty}s can be set with
   * the returned builder.
   *
   * @return a new {@link Connection} builder. Not {@code null}.
   */
  @Override
  public Connection.Builder builder() {
    if (closed) {
      throw new IllegalStateException("this datasource has already been closed");
    }

    return new PGConnectionBuilder(this);
  }

  @Override
  public void close() {
    for (Integer i : connections.keySet()) {
      connections.get(i).close();
    }
    closed = true;
  }

  public void registerConnection(PGConnection connection) {
	connection.setSelector(selector);
	connection.setIndex(connectionIdx);
    connections.put(connectionIdx, connection);
    connectionIdx++; //TODO check for thread safety of this static variable
  }

  public Map<ConnectionProperty, Object> getProperties() {
    return properties;
  }
}
