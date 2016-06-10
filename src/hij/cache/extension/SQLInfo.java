package hij.cache.extension;

public final class SQLInfo {
	public SQLParams getSql() {
		return sql;
	}

	public void setSql(SQLParams sql) {
		this.sql = sql;
	}

	public SQLParams getCountSQL() {
		return countSQL;
	}

	public void setCountSQL(SQLParams countSQL) {
		this.countSQL = countSQL;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	SQLParams sql;
	SQLParams countSQL;
	
	/**
	 * 数据库类型
	 */
	String dbType;
}
