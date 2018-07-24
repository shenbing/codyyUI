package com.coddy.DB;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ColumnDefinitions.Definition;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.DowngradingConsistencyRetryPolicy;

public class CassandraUtil {
	private static Logger logger = LoggerFactory.getLogger(CassandraUtil.class);
	private int maxRequestsPerConnection = 0;
	private int coreConnectionsPerHost = 0;
	private int maxConnectionsPerHost = 0;
	private String[] contactPoints = null;
	private int port = 0;
	private int timeOut = 0;
	private String credentialsAccount = null;
	private String credentialsPassword = null;
	private String keyspace = null;
	private Properties properties;
	private Cluster cluster;

	public CassandraUtil(String fileName) {
		this.properties = loadProperties(fileName);
		this.maxRequestsPerConnection = Integer.valueOf(properties.getProperty("MaxRequestsPerConnection"));
		this.coreConnectionsPerHost = Integer.valueOf(properties.getProperty("CoreConnectionsPerHost"));
		this.maxConnectionsPerHost = Integer.valueOf(properties.getProperty("MaxConnectionsPerHost"));
		this.contactPoints = properties.getProperty("ContactPoints").split(",");
		this.port = Integer.valueOf(properties.getProperty("Port"));
		this.timeOut = Integer.valueOf(properties.getProperty("TimeOut"));
		this.credentialsAccount = properties.getProperty("CredentialsAccount");
		this.credentialsPassword = properties.getProperty("CredentialsPassword");
		this.keyspace = properties.getProperty("Keyspace");
	}

	public static Properties loadProperties(String fullFile) {
		Properties properties = new Properties();
		if (fullFile == "" || "".equals(fullFile)) {
			logger.info(fullFile + "，该文件不存在！");
		} else {
			try {
				InputStream inStream = DbPoolUtil.class.getClassLoader().getResourceAsStream(fullFile);
				if (null == inStream) {
					inStream = new FileInputStream(new java.io.File(fullFile));
				}
				properties.load(inStream);
			} catch (IOException e) {
				logger.error("数据库配置文件导入失败！");
				e.printStackTrace();
			}
		}
		return properties;
	}

	public void connect() {
		PoolingOptions poolingOptions = new PoolingOptions();
		poolingOptions.setIdleTimeoutSeconds(timeOut);
		poolingOptions.setMaxRequestsPerConnection(HostDistance.LOCAL, maxRequestsPerConnection);
		poolingOptions.setCoreConnectionsPerHost(HostDistance.LOCAL, coreConnectionsPerHost)
				.setMaxConnectionsPerHost(HostDistance.LOCAL, maxConnectionsPerHost);
		if (("".equals(credentialsAccount) || null == credentialsAccount)
				&& ("".equals(credentialsPassword) || null == credentialsPassword)) {
			cluster = Cluster.builder().addContactPoints(contactPoints).withPort(port)
					.withPoolingOptions(poolingOptions)
					.withQueryOptions(new QueryOptions().setConsistencyLevel(ConsistencyLevel.ONE))
					.withRetryPolicy(DowngradingConsistencyRetryPolicy.INSTANCE).build();
		} else {
			cluster = Cluster.builder().addContactPoints(contactPoints).withPort(port)
					.withCredentials(credentialsAccount, credentialsPassword).withPoolingOptions(poolingOptions)
					.withQueryOptions(new QueryOptions().setConsistencyLevel(ConsistencyLevel.ONE))
					.withRetryPolicy(DowngradingConsistencyRetryPolicy.INSTANCE).build();
		}
	}

	public Session getSession() {
		if (null == cluster || cluster.isClosed()) {
			connect();
		}
		Session session;
		if ("".equals(keyspace) || null == keyspace) {
			session = cluster.connect();
		} else {
			session = cluster.connect(keyspace);
		}
		return session;
	}

	public void closeSession(Session session) {
		session.close();
	}

	/**
	 * 创建键空间
	 */
	public void createKeyspace(String cql) {
		Session session = null;
		try {
			session = getSession();
			session.execute(cql);
		} finally {
			logger.info(cql);
			closeSession(session);
			cluster.close();
		}
	}

	/**
	 * 创建表
	 */
	public void createTable(String cql) {
		Session session = null;
		try {
			session = getSession();
			session.execute(cql);
		} finally {
			logger.info(cql);
			closeSession(session);
			cluster.close();
		}
	}

	/**
	 * 插入
	 */
	public void insert(String cql) {
		Session session = null;
		try {
			session = getSession();
			session.execute(cql);
		} finally {
			logger.info(cql);
			closeSession(session);
			cluster.close();
		}
	}

	/**
	 * 修改
	 */
	public void update(String cql) {
		Session session = null;
		try {
			session = getSession();
			session.execute(cql);
		} finally {
			logger.info(cql);
			closeSession(session);
			cluster.close();
		}
	}

	/**
	 * 删除
	 */
	public void delete(String cql) {
		Session session = null;
		try {
			session = getSession();
			session.execute(cql);
		} finally {
			logger.info(cql);
			closeSession(session);
			cluster.close();
		}
	}

	/**
	 * 查询
	 */
	public List<HashMap<String, String>> query(String cql) {
		ArrayList<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
		Session session = null;
		session = getSession();
		try {
			ResultSet resultSet = session.execute(cql);
			for (Row row : resultSet) {
				HashMap<String, String> rowMap = new HashMap<String, String>();
				for (Definition definition : row.getColumnDefinitions()) {
					String result = "";
					switch (definition.getType().getName().name().toUpperCase()) {
					case "VARCHAR":
					case "ASCII":
					case "INET":
					case "BLOB":
					case "TEXT":
						result = row.getString(definition.getName());
						break;
					case "BIGINT":
					case "VARINT":
					case "SMALLINT":
					case "INT":
						result = String.valueOf(row.getInt(definition.getName()));
						break;
					case "DOUBLE":
						result = String.valueOf(row.getDouble(definition.getName()));
						break;
					case "FLOAT":
						result = String.valueOf(row.getFloat(definition.getName()));
						break;
					case "DECIMAL":
						result = String.valueOf(row.getDecimal(definition.getName()));
						break;
					case "TIMESTAMP":
						result = String.valueOf(row.getTimestamp(definition.getName()));
						break;
					case "TIME":
						result = String.valueOf(row.getTime(definition.getName()));
						break;
					case "UUID":
						result = String.valueOf(row.getUUID(definition.getName()));
						break;
					case "DATE":
						result = String.valueOf(row.getDate(definition.getName()));
						break;
					case "SET":
						Set<String> resultSets = row.getSet(definition.getName(), String.class);
						Iterator<String> ito = resultSets.iterator();
						while (ito.hasNext()) {
							result = result + ito.next() + ",";
						}
						result = result.substring(0, result.length() - 1);
					case "LIST":
						List<String> resultLists = row.getList(definition.getName(), String.class);
						for (String value : resultLists) {
							result = result + value + ",";
						}
						result = result.substring(0, result.length() - 1);
					default:
						break;
					}
					rowMap.put(definition.getName(), result);
				}
				resultList.add(rowMap);
			}
		} finally {
			logger.info(cql);
			closeSession(session);
			cluster.close();
		}
		return resultList;
	}

	public void close() {
		cluster.close();
	}

	public static void main(String[] args) {
		String cql0 = "insert into statistic_score_student(survey_task_id,survey_scope,scope_parent_id,score,base_student_id,area_id_path,base_area_id,base_class_id,base_classlevel_id,class_name,cls_school_id) VALUES ('1','CITY','1',1,'1',{'1','2'},'1','1','1','1','1');";
		new CassandraUtil("cassandra.properties").insert(cql0);
		String cql1 = "select * from statistic_score_student where survey_task_id='1' and survey_scope = 'CITY';";
		HashMap<String, String> result = new CassandraUtil("cassandra.properties").query(cql1).get(0);
		for (String key : result.keySet()) {
			System.out.println(key + ":" + result.get(key));
		}
		String cql2 = "delete from statistic_score_student where survey_task_id='1' and survey_scope = 'CITY';";
		new CassandraUtil("cassandra.properties").delete(cql2);
	}
}
