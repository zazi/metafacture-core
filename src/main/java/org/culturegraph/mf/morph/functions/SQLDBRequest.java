package org.culturegraph.mf.morph.functions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.culturegraph.mf.exceptions.MorphException;
import org.culturegraph.mf.morph.functions.utils.SQLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tgaengler on 05.09.16.
 */
public class SQLDBRequest extends AbstractCollectionStatelessFunction implements Cloneable {

	private static final Logger LOG = LoggerFactory.getLogger(SQLDBRequest.class);

	private boolean isUninitialized = true;

	private Connection conn;
	private String     host;
	private String     port;
	private String     login;
	private String     password;
	private String     database;
	private String     query;
	private String     driver;
	private String     databaseType;

	private PreparedStatement preparedStatement;

	public void init() {

		try {

			LOG.debug("generate a prepared statement with the following query string '{}'", query);

			preparedStatement = getMySqlConnection().prepareStatement(query);
		} catch (final SQLException e) {

			throw new MorphException(e);
		}

		isUninitialized = false;
	}

	public void close() throws IOException {

		try {

			if (conn != null) {

				conn.close();
			}
		} catch (final SQLException e) {

			throw new MorphException(e);
		}
	}

	private Connection getMySqlConnection() {

		conn = SQLUtils.createSQLConnection(driver, databaseType, host, port, database, login, password);

		return conn;
	}

	public void setDriver(final String driver) {

		this.driver = driver;
	}

	public void setHost(final String host) {

		this.host = host;
	}

	public void setLogin(final String login) {

		this.login = login;
	}

	public void setPassword(final String password) {

		this.password = password;
	}

	public void setDatabase(final String database) {

		this.database = database;
	}

	public void setQuery(final String query) {

		this.query = query;
	}

	public void setPort(final String port) {

		this.port = port;
	}

	public void setDatabaseType(final String databaseType) {

		this.databaseType = databaseType;
	}

	@Override
	protected Collection<String> process(final String key) {

		if (key == null || key.trim().isEmpty()) {

			final String msg = "no key for the prepared SQL statement available";

			LOG.error(msg);

			throw new MorphException(msg);
		}

		if (isUninitialized) {

			init();
		}

		try {

			preparedStatement.setObject(1, key.toString());
			final ResultSet resultSet = preparedStatement.executeQuery();

			if (!resultSet.first()) {

				LOG.debug("could not fetch any results for query '{}' and key '{}'", query, key);

				return new ArrayList<>();
			}

			final ArrayList<String> values = new ArrayList<>();

			while(resultSet.next()) {

				values.add(resultSet.getString(1));
			}

			resultSet.close();

			return values;
		} catch (final SQLException e) {

			throw new MorphException(e);
		}
	}

	@Override
	protected void finalize() throws Throwable {

		close();
	}
}
