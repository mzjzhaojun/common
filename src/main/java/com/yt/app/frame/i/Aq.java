package com.yt.app.frame.i;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.yt.app.frame.p.DateTimeUtil;
import com.yt.app.frame.p.DbConnection;

public class Aq {
	private static Aq I;
	private static Ar J;
	File file;
	FileWriter K;
	BufferedWriter L;
	private DbConnection M = DbConnection.getInstance();
	private String FILE_PATH_NAME = "/config/mysql.properties";

	private Aq() {
		init();
	}

	public static Aq u() {
		if (I == null) {
			I = new Aq();
		}
		if (J == null)
			J = new Ar(1L, 1L);
		return I;
	}

	private void init() {
		try {
			File localFile1 = new File("");
			String str1 = localFile1.getCanonicalPath();
			String str2 = DbConnection.class.getResource(this.FILE_PATH_NAME).getPath();
			File localFile2 = new File(str2);
			FileInputStream localFileInputStream = new FileInputStream(localFile2);
			Properties localProperties = new Properties();
			localProperties.load(localFileInputStream);
			localFileInputStream.close();
			DbConnection.basePage = "com.yt.app.api." + DbConnection.version;
			DbConnection.commndPage = "com.yt.app.common.base";
			DbConnection.filePath = str1 + "/src/main/java/com/yt/app/api/" + DbConnection.version;
			DbConnection.pagefilePath = str1 + "/resources/templates/static/project/dfgj/html/";
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
	}

	private void b(String paramString) {
		this.file = new File(DbConnection.filePath + "/mapper/impl");
		this.file.mkdirs();
		this.file = new File(DbConnection.filePath + "/service/impl");
		this.file.mkdirs();
		this.file = new File(DbConnection.filePath + "/controller");
		this.file.mkdirs();
		this.file = new File(DbConnection.filePath + "/entity");
		this.file.mkdirs();
		this.file = new File(DbConnection.filePath + "/mapper");
		this.file.mkdirs();
		this.file = new File(DbConnection.filePath + "/resource");
		this.file.mkdirs();
		this.file = new File(DbConnection.pagefilePath + "/" + paramString.toLowerCase());
		this.file.mkdirs();
	}

	private File c(String paramString) throws Exception {
		this.file = new File(DbConnection.filePath + "/mapper/" + paramString + "Mapper.java");
		if (this.file.exists())
			this.file.delete();
		else
			this.file.createNewFile();
		return this.file;
	}

	private File d(String paramString) throws Exception {
		this.file = new File(DbConnection.filePath + "/service/" + paramString + "Service.java");
		if (this.file.exists())
			this.file.delete();
		else
			this.file.createNewFile();
		return this.file;
	}

	private File e(String paramString) throws Exception {
		this.file = new File(DbConnection.filePath + "/service/impl/" + paramString + "ServiceImpl.java");
		if (this.file.exists())
			this.file.delete();
		else
			this.file.createNewFile();
		return this.file;
	}

	private File f(String paramString) throws Exception {
		this.file = new File(DbConnection.filePath + "/controller/" + paramString + "Controller.java");
		if (this.file.exists())
			this.file.delete();
		else
			this.file.createNewFile();
		return this.file;
	}

	private File g(String paramString) throws Exception {
		this.file = new File(DbConnection.filePath + "/entity/" + paramString + ".java");
		if (this.file.exists())
			this.file.delete();
		else
			this.file.createNewFile();
		return this.file;
	}

	private File h(String paramString) throws Exception {
		this.file = new File(DbConnection.filePath + "/mapper/impl/" + paramString + "Mapper.xml");
		if (this.file.exists())
			this.file.delete();
		else
			this.file.createNewFile();
		return this.file;
	}

	private File i(String paramString) throws Exception {
		this.file = new File(DbConnection.filePath + "/resource/" + paramString + "Resource.java");
		if (this.file.exists())
			this.file.delete();
		else
			this.file.createNewFile();
		return this.file;
	}

	private File j(String paramString) throws Exception {
		this.file = new File(DbConnection.filePath + "/resource/" + paramString + "ResourceAssembler.java");
		if (this.file.exists())
			this.file.delete();
		else
			this.file.createNewFile();
		return this.file;
	}

	private File a(String paramString1, String paramString2) throws Exception {
		this.file = new File(DbConnection.pagefilePath + "/" + paramString1.toLowerCase() + "/" + paramString2 + ".html");
		if (this.file.exists())
			this.file.delete();
		else
			this.file.createNewFile();
		return this.file;
	}

	public List<String> t() {
		List<String> localArrayList = new ArrayList<String>();
		try {
			Connection localConnection = this.M.getCon();
			Statement localStatement = localConnection.createStatement(1004, 1007);
			ResultSet localResultSet = null;
			localResultSet = localStatement.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '"
					+ DbConnection.dbName + "'");
			while (localResultSet.next()) {
				localArrayList.add(localResultSet.getString("TABLE_NAME"));
			}
			localResultSet.close();
		} catch (SQLException localSQLException) {
			localSQLException.printStackTrace();
		}
		return localArrayList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void p(List<String> tables, boolean code, boolean html, boolean data, String sysid) {
		try {
			Connection conn = this.M.getCon();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = null;
			if (tables == null || tables.size() == 0)
				return;
			List<String[]> db2java = new ArrayList<String[]>();
			db2java.add(new String[] { "bit", "Boolean" });
			db2java.add(new String[] { "varchar", "String" });
			db2java.add(new String[] { "numeric", "Double" });
			db2java.add(new String[] { "double", "Double" });
			db2java.add(new String[] { "int", "Integer" });
			db2java.add(new String[] { "datetime", "java.util.Date" });
			db2java.add(new String[] { "date", "java.util.Date" });
			db2java.add(new String[] { "tinyint", "Integer" });
			db2java.add(new String[] { "smallint", "Integer" });
			db2java.add(new String[] { "bigint", "Long" });
			db2java.add(new String[] { "char", "String" });
			db2java.add(new String[] { "float", "float" });
			for (int ti = 0; ti < tables.size(); ti++) {
				String tb = tables.get(ti);
				System.out.println("========>>>>>>：" + tb);
				String tn = "";
				String columnname = "";
				if (tb.indexOf("_") != -1) {
					String tns = tb.substring(tb.indexOf("_") + 1).toLowerCase();
					tn = tb.substring(0, tb.indexOf("_")) + tns.substring(0, 1).toUpperCase() + tns.substring(1);
					tn = tn.replaceAll("_", "");
				} else {
					tn = tb;
				}
				b(tn);
				HashMap[] r = (HashMap[]) null;
				int iRowNum;
				r = (HashMap[]) null;
				iRowNum = 0;
				int iColCnt = 0;
				rs = stmt
						.executeQuery("select column_name,data_type,column_comment,character_maximum_length from information_schema.columns where table_schema = '"
								+ DbConnection.dbName + "' and table_name='" + tb + "'");
				ResultSetMetaData MetaData = rs.getMetaData();
				iColCnt = MetaData.getColumnCount();
				if (rs.next()) {
					rs.last();
					r = new HashMap[rs.getRow()];
					rs.beforeFirst();
				} else {
					System.out.println("表不存在！");
					return;
				}

				while (rs.next()) {
					r[iRowNum] = new HashMap();
					for (int j = 0; j < iColCnt; j++) {
						String szColName = MetaData.getColumnName(j + 1);
						String szColValue = rs.getString(szColName);
						if (szColValue == null)
							szColValue = "";
						r[iRowNum].put(szColName, szColValue);
					}
					iRowNum++;
				}
				rs.close();
				String bt = tn.substring(0, 1).toUpperCase() + tn.substring(1);
				String[][] ts = new String[r.length][4];
				String data_type = "DATA_TYPE";
				String column_name = "COLUMN_NAME";
				String column_comment = "COLUMN_COMMENT";
				String character_maximum_length = "CHARACTER_MAXIMUM_LENGTH";
				for (int i = 0; i < r.length; i++) {
					ts[i][0] = "Object";
					for (String[] temp : db2java)
						if (r[i].get(data_type).equals(temp[0]))
							ts[i][0] = temp[1];
					ts[i][1] = r[i].get(column_name).toString().toLowerCase();
					ts[i][2] = r[i].get(column_comment).toString();
					ts[i][3] = r[i].get(character_maximum_length).toString();
				}
				if (code) {
					file = g(bt);
					this.K = new FileWriter(file);
					this.L = new BufferedWriter(this.K);
					this.L.write("package " + DbConnection.basePage + ".entity;\r\n\n");
					this.L.write("import lombok.Getter;\r\n");
					this.L.write("import lombok.Setter;\r\n");
					this.L.write("import java.io.Serializable;\r\n");
					this.L.write("/**\r\n");
					this.L.write("* @author zj default\r\n");
					this.L.write("* \r\n");
					this.L.write("* @version " + DbConnection.version + "\r\n");
					this.L.write("* @createdate" + DateTimeUtil.getDateTime() + "\r\n");
					this.L.write("*/\r\n");
					this.L.write("@Getter\r\n");
					this.L.write("@Setter\r\n");
					this.L.write("public class " + bt + " implements Serializable{\r\n");
					this.L.write("\r\n");
					this.L.write("private static final long serialVersionUID=1L;\r\n");
					this.L.write("\r\n");
					for (int l = 0; l < r.length; l++) {
						this.L.write("" + ts[l][0] + " " + ts[l][1] + ";\r\n");
					}
					this.L.write("public " + bt + "(){\r\n");
					this.L.write("}\r\n");
					String gz = "";
					for (int i = 0; i < r.length; i++)
						gz += "," + ts[i][0] + " " + ts[i][1];
					this.L.write("public " + bt + "(" + gz.substring(1) + "){\r\n");
					for (int i = 0; i < r.length; i++)
						this.L.write("this." + ts[i][1] + "=" + ts[i][1] + ";\r\n");
					this.L.write("}\r\n");
					this.L.write("}");
					this.L.flush();
					String column = "", value = "", update = "", insertColumn = "", insertValue = "";
					for (int i = 0; i < r.length; i++) {
						column += "" + ts[i][1] + ",";
						value += "#{" + ts[i][1] + "},";
						update += "" + ts[i][1] + "" + "=#{" + ts[i][1] + "},";
						if (i >= 0) {
							if (!ts[i][1].equals("version")) {
								insertColumn += "" + ts[i][1] + ",";
								insertValue += "#{" + ts[i][1] + "},";
							}
						}
					}
					column = (column + ",").replace(",,", "");
					value = (value + ",").replace(",,", "");
					update = (update + ",").replace(",,", "");

					insertColumn = (insertColumn + ",").replace(",,", "");
					insertValue = (insertValue + ",").replace(",,", "");
					this.L.flush();

					file = h(bt);
					this.K = new FileWriter(file);
					this.L = new BufferedWriter(this.K);
					this.L.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
					this.L.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\r\n");
					this.L.write("<mapper namespace=\"" + DbConnection.basePage + ".mapper." + bt + "Mapper\">\r\n");

					// Base_Column_List
					this.L.write("<!-- 数据库列 -->\r\n");
					this.L.write("<sql id=\"Base_Column_List\">\r\n");
					this.L.write("" + column + "\r\n");
					this.L.write("</sql>\r\n");

					// ResultMap
					this.L.write("<!-- 基础返回对象封装 -->\r\n");
					this.L.write("<resultMap id=\"ResultMap\" type=\"" + DbConnection.basePage + ".entity." + bt + "\">\r\n");
					for (int i = 0; i < r.length; i++) {
						this.L.write("<id property=\"" + ts[i][1] + "\" column=\"" + ts[i][1] + "\"/>\r\n");
					}
					this.L.write("</resultMap>\r\n");

					this.L.write("<!-- 默认新增 -->\r\n");
					this.L.write("<insert id=\"post\" parameterType=\"" + DbConnection.basePage + ".entity." + bt + "\">\r\n");
					this.L.write("insert into " + tb + "(" + insertColumn + ")\r\n");
					this.L.write("values (" + insertValue + ")\r\n");
					this.L.write("</insert>\r\n");

					// deleteByIds
					this.L.write("<!-- 默认删除id对象 -->\r\n");
					this.L.write("<delete id=\"delete\" parameterType=\"java.lang.String\">\r\n");
					this.L.write("delete from " + tb + "\r\n");
					this.L.write("where id = #{id}\r\n");
					this.L.write("</delete>\r\n");

					// update
					this.L.write("<!-- 默认更新id对象 -->\r\n");
					this.L.write("<update id=\"put\" parameterType=\"" + DbConnection.basePage + ".entity." + bt + "\">\r\n");
					this.L.write("update " + tb + "\r\n");
					this.L.write("<set>\r\n");
					for (int i = 0; i < r.length; i++) {
						if (ts[i][1].equals("id"))
							continue;
						this.L.write("<if test=\"" + ts[i][1] + "!= null\">\r\n");
						if (i < r.length - 1) {
							if (ts[i][1].equals("version"))
								this.L.write("" + ts[i][1] + "= #{" + ts[i][1] + "}+1,\r\n");
							else
								this.L.write("" + ts[i][1] + "= #{" + ts[i][1] + "},\r\n");
						} else if (i == r.length - 1) {
							if (ts[i][1].equals("version"))
								this.L.write("" + ts[i][1] + "= #{" + ts[i][1] + "}+1\r\n");
							else
								this.L.write("" + ts[i][1] + "= #{" + ts[i][1] + "}\r\n");
						}
						this.L.write("</if>\r\n");
					}
					this.L.write("</set>\r\n");
					this.L.write("where id = #{id} andversion = #{version}\r\n");
					this.L.write("</update>\r\n");

					// getById
					this.L.write("<!-- 默认获得id对象 -->\r\n");
					this.L.write("<select id=\"get\" parameterType=\"java.lang.String\" resultMap=\"ResultMap\">\r\n");
					this.L.write("select\r\n");
					this.L.write("<include refid=\"Base_Column_List\"/>\r\n");
					this.L.write("from " + tb + "\r\n");
					this.L.write("where id = #{id}\r\n");
					this.L.write("</select>\r\n");

					// getList
					this.L.write("<!-- 默认集合 -->\r\n");
					this.L.write("<select id=\"list\" parameterType=\"java.util.HashMap\" resultMap=\"ResultMap\">\r\n");
					this.L.write("select\r\n");
					this.L.write("<include refid=\"Base_Column_List\"/>\r\n");
					this.L.write("from " + tb + "\r\n");
					this.L.write("<where>\r\n");
					this.L.write("1=1\r\n");
					this.L.write("<if test=\"name != null and name != ''\">\r\n");
					this.L.write("and name like \"%\"#{name}\"%\"\r\n");
					this.L.write("</if>\r\n");
					this.L.write("<if test=\"orderby != null and dir != null\">\r\n");
					this.L.write("order by ${orderby} ${dir}\r\n");
					this.L.write("</if>\r\n");
					this.L.write("<if test=\"pageStart != null and pageEnd != null\">\r\n");
					this.L.write("LIMIT #{pageStart},#{pageEnd}\r\n");
					this.L.write("</if>\r\n");
					this.L.write("</where>\r\n");
					this.L.write("</select>\r\n");

					// getMap
					this.L.write("<!-- 默认返回MAP集合 -->\r\n");
					this.L.write("<select id=\"map\" parameterType=\"java.util.HashMap\" resultType=\"java.util.HashMap\">\r\n");
					this.L.write("select\r\n");
					this.L.write("<include refid=\"Base_Column_List\"/>\r\n");
					this.L.write("from " + tb + "\r\n");
					this.L.write("<where>\r\n");
					this.L.write("1=1\r\n");
					this.L.write("<if test=\"name != null and name != ''\">\r\n");
					this.L.write("and name like \"%\"#{name}\"%\"\r\n");
					this.L.write("</if>\r\n");
					this.L.write("<if test=\"orderby != null and dir != null\">\r\n");
					this.L.write("order by ${orderby} ${dir}\r\n");
					this.L.write("</if>\r\n");
					this.L.write("<if test=\"pageStart != null and pageEnd != null\">\r\n");
					this.L.write("LIMIT #{pageStart},#{pageEnd}\r\n");
					this.L.write("</if>\r\n");
					this.L.write("</where>\r\n");
					this.L.write("</select>\r\n");
					// getCount
					this.L.write("<!-- 默认集合统计 -->\r\n");
					this.L.write("<select id=\"countlist\" parameterType=\"java.util.HashMap\" resultType=\"int\">\r\n");
					this.L.write("select count(*) from " + tb + "\r\n");
					this.L.write("<where>\r\n");
					this.L.write("1=1\r\n");
					this.L.write("<if test=\"name != null and name != ''\">\r\n");
					this.L.write("and name like \"%\"#{name}\"%\"\r\n");
					this.L.write("</if>\r\n");
					this.L.write("</where>\r\n");
					this.L.write("</select>\r\n");
					this.L.write("<!-- 默认返回MAP集合统计 -->\r\n");
					this.L.write("<select id=\"countmap\" parameterType=\"java.util.HashMap\" resultType=\"int\">\r\n");
					this.L.write("select count(*) from " + tb + "\r\n");
					this.L.write("<where>\r\n");
					this.L.write("1=1\r\n");
					this.L.write("<if test=\"name != null and name != ''\">\r\n");
					this.L.write("and name like \"%\"#{name}\"%\"\r\n");
					this.L.write("</if>\r\n");
					this.L.write("</where>\r\n");
					this.L.write("</select>\r\n");
					this.L.write("<!-- 默认获取Ids的对象 -->\r\n");
					this.L.write("<select id=\"listByArrayId\" parameterType=\"java.util.HashMap\" resultMap=\"ResultMap\">\r\n");
					this.L.write("select\r\n");
					this.L.write("<include refid=\"Base_Column_List\"/>\r\n");
					this.L.write("from " + tb + "\r\n");
					this.L.write("<where>\r\n");
					this.L.write("1=1\r\n");
					this.L.write("<if test=\"array != null and array != ''\">\r\n");
					this.L.write("and id in\r\n");
					this.L.write("<foreach item=\"item\" index=\"index\" collection=\"array\" open=\"(\" separator=\",\" close=\")\">\r\n");
					this.L.write("#{item}\r\n");
					this.L.write("</foreach>\r\n");
					this.L.write("</if>\r\n");
					this.L.write("</where>\r\n");
					this.L.write("</select>\r\n");
					this.L.write("</mapper>");
					this.L.flush();

					file = c(bt);
					this.K = new FileWriter(file);
					this.L = new BufferedWriter(this.K);
					this.L.write("package " + DbConnection.basePage + ".mapper;\r\n");
					this.L.write("import java.util.List;\r\n");
					this.L.write("import java.util.Map;\r\n");
					this.L.write("import " + DbConnection.basePage + "." + "entity." + bt + ";\r\n");
					this.L.write("import com.yt.app.frame.b.RedisCacheAnnotation;\r\n");
					this.L.write("import com.yt.app.frame.b.RedisCacheEvictAnnotation;\r\n");
					this.L.write("import " + DbConnection.commndPage + ".IBaseMapper;\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("/**\r\n");
					this.L.write("* @author zj default\r\n");
					this.L.write("* \r\n");
					this.L.write("* @version " + DbConnection.version + "\r\n");
					this.L.write("* @createdate" + DateTimeUtil.getDateTime() + "\r\n");
					this.L.write("*/\r\n");
					this.L.write("\r\n");
					this.L.write("public interface " + bt + "Mapper extends IBaseMapper<" + bt + "> {\r\n");
					this.L.write("/**\r\n");
					this.L.write("* 保存（持久化）对象\r\n");
					this.L.write("* \r\n");
					this.L.write("* @param o\r\n");
					this.L.write("*要持久化的对象\r\n");
					this.L.write("* @return 执行成功的记录个数\r\n");
					this.L.write("*/\r\n");
					this.L.write("@RedisCacheEvictAnnotation(classs = { " + bt + ".class})\r\n");
					this.L.write("public Integer post(Object t);\r\n");
					this.L.write("/**\r\n");
					this.L.write("* 更新（持久化）对象\r\n");
					this.L.write("* \r\n");
					this.L.write("* @param o\r\n");
					this.L.write("*要持久化的对象\r\n");
					this.L.write("* @return 执行成功的记录个数\r\n");
					this.L.write("*/\r\n");
					this.L.write("@RedisCacheEvictAnnotation(classs = { " + bt + ".class})\r\n");
					this.L.write("public Integer put(Object t);\r\n");
					this.L.write("\r\n");
					this.L.write("/**\r\n");
					this.L.write("* 获取指定的唯一标识符对应的持久化对象\r\n");
					this.L.write("*\r\n");
					this.L.write("* @param id\r\n");
					this.L.write("*指定的唯一标识符\r\n");
					this.L.write("* @return 指定的唯一标识符对应的持久化对象，如果没有对应的持久化对象，则返回null。\r\n");
					this.L.write("*/\r\n");
					this.L.write("@RedisCacheAnnotation(classs = " + bt + ".class)\r\n");
					this.L.write("public " + bt + " get(Long id);\r\n");
					this.L.write("\r\n");
					this.L.write("/**\r\n");
					this.L.write("* 删除指定的唯一标识符数组对应的持久化对象\r\n");
					this.L.write("*\r\n");
					this.L.write("* @param ids\r\n");
					this.L.write("*指定的唯一标识符数组\r\n");
					this.L.write("* @return 删除的对象数量\r\n");
					this.L.write("*/\r\n");
					this.L.write("@RedisCacheEvictAnnotation(classs = { " + bt + ".class})\r\n");
					this.L.write("public Integer delete(Long id);\r\n");
					this.L.write("\r\n");
					this.L.write("/**\r\n");
					this.L.write("* 获取满足查询参数条件的数据总数\r\n");
					this.L.write("* \r\n");
					this.L.write("* @param param\r\n");
					this.L.write("*查询参数\r\n");
					this.L.write("* @return 数据总数\r\n");
					this.L.write("*/\r\n");
					this.L.write("@RedisCacheAnnotation(classs = " + bt + ".class)\r\n");
					this.L.write("public Integer countlist(Map<String, Object> param);\r\n");
					this.L.write("\r\n");
					this.L.write("/**\r\n");
					this.L.write("* 获取满足查询参数条件的数据总数\r\n");
					this.L.write("* \r\n");
					this.L.write("* @param param\r\n");
					this.L.write("*查询参数\r\n");
					this.L.write("* @return 数据总数\r\n");
					this.L.write("*/\r\n");
					this.L.write("@RedisCacheAnnotation(classs = " + bt + ".class)\r\n");
					this.L.write("public Integer countmap(Map<String, Object> param);\r\n");
					this.L.write("\r\n");
					this.L.write("/**\r\n");
					this.L.write("* 获取满足查询参数条件的数据\r\n");
					this.L.write("* \r\n");
					this.L.write("* @param param\r\n");
					this.L.write("*查询参数\r\n");
					this.L.write("* @return 数据\r\n");
					this.L.write("*/\r\n");
					this.L.write("@RedisCacheAnnotation(classs = " + bt + ".class)\r\n");
					this.L.write("public List<" + bt + "> list(Map<String, Object> param);\r\n");
					this.L.write("\r\n");
					this.L.write("/**\r\n");
					this.L.write("* 获取满足查询参数条件的数据\r\n");
					this.L.write("* \r\n");
					this.L.write("* @param param\r\n");
					this.L.write("*查询参数\r\n");
					this.L.write("* @return 数据\r\n");
					this.L.write("*/\r\n");
					this.L.write("@RedisCacheAnnotation(classs = " + bt + ".class)\r\n");
					this.L.write("public List<Map<String, Object>> map(Map<String, Object> param);\r\n");
					this.L.write("\r\n");
					this.L.write("/**\r\n");
					this.L.write("* 获取满足查询参数条件的数据\r\n");
					this.L.write("* \r\n");
					this.L.write("* @param id\r\n");
					this.L.write("*查询参数\r\n");
					this.L.write("* @return 数据\r\n");
					this.L.write("*/\r\n");
					this.L.write("@RedisCacheAnnotation(classs = " + bt + ".class)\r\n");
					this.L.write("public List<" + bt + "> listByArrayId(long[] id);\r\n");
					this.L.write("}");
					this.L.flush();

					file = d(bt);
					this.K = new FileWriter(file);
					this.L = new BufferedWriter(this.K);
					this.L.write("package " + DbConnection.basePage + ".service;\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("import " + DbConnection.basePage + "." + "entity." + bt + ";\r\n");
					this.L.write("import " + DbConnection.commndPage + ".IBaseService;\r\n");
					this.L.write("/**\r\n");
					this.L.write("* @author zj default\r\n");
					this.L.write("* \r\n");
					this.L.write("* @version " + DbConnection.version + "\r\n");
					this.L.write("* @createdate" + DateTimeUtil.getDateTime() + "\r\n");
					this.L.write("*/\r\n");
					this.L.write("\r\n");
					this.L.write("public interface " + bt + "Service extends IBaseService<" + bt + ", Long>{\r\n");
					this.L.write("}");
					this.L.flush();

					file = e(bt);
					this.K = new FileWriter(file);
					this.L = new BufferedWriter(this.K);
					this.L.write("package " + DbConnection.basePage + ".service.impl;\r\n");
					this.L.write("\r\n");
					this.L.write("import org.springframework.beans.factory.annotation.Autowired;\r\n");
					this.L.write("import org.springframework.transaction.annotation.Transactional;\r\n");
					this.L.write("import org.springframework.http.RequestEntity;\r\n");
					this.L.write("import org.springframework.stereotype.Service;\r\n");
					this.L.write("import " + DbConnection.basePage + "." + "mapper." + bt + "Mapper;\r\n");
					this.L.write("import " + DbConnection.basePage + "." + "service." + bt + "Service;\r\n");
					this.L.write("import " + DbConnection.commndPage + ".impl.BaseServiceImpl;\r\n");
					this.L.write("import " + DbConnection.basePage + "." + "entity." + bt + ";\r\n");
					this.L.write("import com.yt.app.frame.m.IPage;\r\n");
					this.L.write("import com.yt.app.frame.n.PageBean;\r\n");
					this.L.write("import com.yt.app.frame.p.RequestUtil;\r\n");
					this.L.write("import java.util.List;\r\n");
					this.L.write("import java.util.Map;\r\n");
					this.L.write("\r\n");
					this.L.write("/**\r\n");
					this.L.write("* @author zj default\r\n");
					this.L.write("* \r\n");
					this.L.write("* @version " + DbConnection.version + "\r\n");
					this.L.write("* @createdate" + DateTimeUtil.getDateTime() + "\r\n");
					this.L.write("*/\r\n");
					this.L.write("\r\n");
					this.L.write("@Service\r\n");
					this.L.write("public class " + bt + "ServiceImpl extends BaseServiceImpl<" + bt + ", Long> implements " + bt + "Service{\r\n");
					this.L.write("@Autowired\r\n");
					this.L.write("private " + bt + "Mapper mapper;\r\n");
					this.L.write("\r\n");
					this.L.write("@Override\r\n");
					this.L.write("@Transactional\r\n");
					this.L.write("public Integer post(" + bt + " t) {\r\n");
					this.L.write("Integer i = mapper.post(t);\r\n");
					this.L.write("return i;\r\n");
					this.L.write("}\r\n");
					this.L.write("\r\n");
					this.L.write("@SuppressWarnings(\"unchecked\")\r\n");
					this.L.write("@Override\r\n");
					this.L.write("public IPage<" + bt + "> list(RequestEntity<Object> requestEntity) {\r\n");
					this.L.write("Map<String, Object> param = RequestUtil.requestEntityToParamMap(requestEntity);\r\n");
					this.L.write("int count = 0;\r\n");
					this.L.write("if (PageBean.isPaging(param)) {\r\n");
					this.L.write("count = mapper.countlist(param);\r\n");
					this.L.write(" if (count == 0) {\r\n");
					this.L.write("return PageBean.EMPTY_PAGE;\r\n");
					this.L.write("}\r\n");
					this.L.write("}\r\n");
					this.L.write("List<" + bt + "> list = mapper.list(param);\r\n");
					this.L.write("return new PageBean<" + bt + ">(param, list, count);\r\n");
					this.L.write("}\r\n");
					this.L.write("\r\n");
					this.L.write("@Override\r\n");
					this.L.write("public " + bt + " get(Long id) {\r\n");
					this.L.write("" + bt + " t = mapper.get(id);\r\n");
					this.L.write("return t;\r\n");
					this.L.write("}\r\n");
					this.L.write("}");
					this.L.flush();

					file = i(bt);
					this.K = new FileWriter(file);
					this.L = new BufferedWriter(this.K);
					this.L.write("package " + DbConnection.basePage + ".resource;\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("import org.springframework.hateoas.ResourceSupport;\r\n");
					this.L.write("import " + DbConnection.basePage + ".controller." + bt + "Controller;\r\n");
					this.L.write("import " + DbConnection.basePage + "." + "entity." + bt + ";\r\n");
					this.L.write("import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;\r\n");
					this.L.write("import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("/**\r\n");
					this.L.write("* @author zj default\r\n");
					this.L.write("* \r\n");
					this.L.write("* @version " + DbConnection.version + "\r\n");
					this.L.write("* @createdate" + DateTimeUtil.getDateTime() + "\r\n");
					this.L.write("*/\r\n");
					this.L.write("\r\n");
					this.L.write("public class " + bt + "Resource extends ResourceSupport {\r\n");
					this.L.write("private final " + bt + " t;\r\n");
					this.L.write("public " + bt + "Resource(" + bt + " entity) {\r\n");
					this.L.write("this.t = entity;\r\n");
					this.L.write("this.add(linkTo(" + bt + "Controller.class).withRel(\"post\"));\r\n");
					this.L.write("this.add(linkTo(methodOn(" + bt + "Controller.class).list(null, null, null)).withRel(\"list\"));\r\n");

					this.L.write("\r\n");
					this.L.write("}\r\n");
					this.L.write("public " + bt + " get" + bt + "() {\r\n");
					this.L.write("return t;\r\n");
					this.L.write("}\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("}");
					this.L.flush();

					file = j(bt);
					this.K = new FileWriter(file);
					this.L = new BufferedWriter(this.K);
					this.L.write("package " + DbConnection.basePage + ".resource;\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("import org.springframework.hateoas.mvc.ResourceAssemblerSupport;\r\n");
					this.L.write("import " + DbConnection.basePage + ".controller." + bt + "Controller;\r\n");
					this.L.write("import " + DbConnection.basePage + "." + "entity." + bt + ";\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("/**\r\n");
					this.L.write("* @author zj default\r\n");
					this.L.write("* \r\n");
					this.L.write("* @version " + DbConnection.version + "\r\n");
					this.L.write("* @createdate" + DateTimeUtil.getDateTime() + "\r\n");
					this.L.write("*/\r\n");
					this.L.write("\r\n");
					this.L.write("public class " + bt + "ResourceAssembler extends ResourceAssemblerSupport<" + bt + ", " + bt + "Resource> {\r\n");
					this.L.write("public " + bt + "ResourceAssembler() {\r\n");
					this.L.write("super(" + bt + "Controller.class, " + bt + "Resource.class);\r\n");
					this.L.write("}\r\n");
					this.L.write("@Override\r\n");
					this.L.write("public " + bt + "Resource toResource(" + bt + " t) {\r\n");
					this.L.write("return createResourceWithId(t.getId(), t);\r\n");
					this.L.write("}\r\n");
					this.L.write("@Override\r\n");
					this.L.write("protected " + bt + "Resource instantiateResource(" + bt + " t) {\r\n");
					this.L.write("return new " + bt + "Resource(t);\r\n");
					this.L.write("}\r\n");
					this.L.write("\r\n");
					this.L.write("}");
					this.L.flush();

					file = f(bt);
					this.K = new FileWriter(file);
					this.L = new BufferedWriter(this.K);
					this.L.write("package " + DbConnection.basePage + ".controller;\r\n");
					this.L.write("import javax.servlet.http.HttpServletRequest;\r\n");
					this.L.write("import javax.servlet.http.HttpServletResponse;\r\n");
					this.L.write("import org.slf4j.Logger;\r\n");
					this.L.write("import org.slf4j.LoggerFactory;\r\n");
					this.L.write("import org.springframework.beans.factory.annotation.Autowired;\r\n");
					this.L.write("import org.springframework.http.HttpStatus;\r\n");
					this.L.write("import org.springframework.http.MediaType;\r\n");
					this.L.write("import org.springframework.http.RequestEntity;\r\n");
					this.L.write("import org.springframework.http.ResponseEntity;\r\n");
					this.L.write("import org.springframework.web.bind.annotation.RequestMethod;\r\n");
					this.L.write("import org.springframework.web.bind.annotation.RestController;\r\n");
					this.L.write("import org.springframework.web.bind.annotation.RequestMapping;\r\n");
					this.L.write("import com.yt.app.frame.m.IPage;\r\n");
					this.L.write("import io.swagger.annotations.ApiOperation;\r\n");
					this.L.write("import " + DbConnection.commndPage + ".impl.BaseControllerImpl;\r\n");
					this.L.write("import " + DbConnection.basePage + ".resource." + bt + "ResourceAssembler;\r\n");
					this.L.write("import " + DbConnection.basePage + "." + "service." + bt + "Service;\r\n");
					this.L.write("import " + DbConnection.basePage + "." + "entity." + bt + ";\r\n");
					this.L.write("\r\n");
					this.L.write("/**\r\n");
					this.L.write("* @author zj defaulttest\r\n");
					this.L.write("* \r\n");
					this.L.write("* @version " + DbConnection.version + "\r\n");
					this.L.write("* @createdate" + DateTimeUtil.getDateTime() + "\r\n");
					this.L.write("*/\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("@RestController\r\n");
					this.L.write("@RequestMapping(\"/" + DbConnection.osName + "/" + DbConnection.version + "/" + bt.toLowerCase() + "\")\r\n");
					this.L.write("public class " + bt + "Controller extends BaseControllerImpl<" + bt + ", Long> {\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("protected Logger logger = LoggerFactory.getLogger(this.getClass());\r\n");
					this.L.write("@Autowired\r\n");
					this.L.write("private " + bt + "Service service;\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("@Override\r\n");
					this.L.write("@ApiOperation(value = \"列表分页\", response = " + bt + ".class)\r\n");
					this.L.write("@RequestMapping(value = \"/\", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)\r\n");
					this.L.write("public ResponseEntity<Object> list(RequestEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {\r\n");
					this.L.write("IPage<" + bt + "> pagebean = service.list(requestEntity);\r\n");
					this.L.write("return new ResponseEntity<Object>(new " + bt
							+ "ResourceAssembler().toResources(pagebean.getPageList()), pagebean.getHeaders(), HttpStatus.OK);\r\n");
					this.L.write("}\r\n");
					this.L.write("}\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.flush();
				}

				// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				if (html) {
					file = a(bt, "add");
					this.K = new FileWriter(file);
					this.L = new BufferedWriter(this.K);
					this.L.write("<!DOCTYPE html>\r\n");
					this.L.write("<html ng-app=\"add\">\r\n");
					this.L.write("<head>\r\n");
					this.L.write("<title>新增</title>\r\n");
					this.L.write("<meta http-equiv=\"keywords\"content=\"keyword1,keyword2,keyword3\">\r\n");
					this.L.write("<meta http-equiv=\"description\"content=\"this is my page\">\r\n");
					this.L.write("<meta http-equiv=\"content-type\"content=\"text/html; charset=UTF-8\">\r\n");
					this.L.write("<!-- 基础样式 -->\r\n");
					this.L.write("<link rel=\"icon\"type=\"image/x-icon\"href=\"../../../resource/img/logo.ico\">\r\n");
					this.L.write("<link rel=\"stylesheet\"type=\"text/css\"href=\"../../../../resource/css/bootstrap.css\">\r\n");
					this.L.write("<link rel=\"stylesheet\"type=\"text/css\"href=\"../../../../resource/css/common.css\">\r\n");
					this.L.write("<link rel=\"stylesheet\"type=\"text/css\"href=\"../../../../resource/js/validate/css/validate.css\"/>\r\n");
					this.L.write("<!-- 基础js -->\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/angular-1.0.1.min.js\"></script>\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/jquery-1.9.1.min.js\"></script>\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/bootstrap.min.js\"></script>\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/response.js\"></script>\r\n");
					this.L.write("<!-- 表单验证 -->\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/date/js/eap.core.js\"></script>\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/validate/js/eap.tip.js\"></script>\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/validate/js/eap.validate.js\"></script>\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/date/js/eap.lang-zh_CN.js\"></script>\r\n");
					this.L.write("<!-- datetime -->\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/bootstrap-datetimepicker.js\"charset=\"UTF-8\"></script>\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/locales/bootstrap-datetimepicker.fr.js\"charset=\"UTF-8\"></script>\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/date/js/eap.util.date.js\"></script>\r\n");
					this.L.write("<!-- 初始化 -->\r\n");
					this.L.write("<script type=\"text/javascript\">\r\n");
					this.L.write("var add = angular.module(\"add\", []);\r\n");
					this.L.write("add.controller(\"controller\",function($scope, $http) {\r\n");
					this.L.write("$scope.formData = {};\r\n");
					this.L.write("//返回\r\n");
					this.L.write("$scope.onReturn=function(){\r\n");
					this.L.write("history.go(-1);\r\n");
					this.L.write("}\r\n");
					this.L.write("//保存\r\n");
					this.L.write("$scope.onPreserved=function(){\r\n");
					this.L.write("var isValid = $(\"#saveForm\").valid();\r\n");
					this.L.write("if(isValid){\r\n");
					this.L.write("$http({\r\n");
					this.L.write("url:parent.getMenuLinksHref(\"" + tb.toLowerCase() + "post\"),\r\n");
					this.L.write("method:'POST',\r\n");
					this.L.write("dataType : \"json\",\r\n");
					this.L.write("withCredentials: true,\r\n");
					this.L.write("headers: {'Content-Type': 'application/json;charset=UTF-8;'},\r\n");
					this.L.write("data: JSON.stringify($scope.formData)\r\n");
					this.L.write("}).success(function(data, status, headers, config){\r\n");
					this.L.write("if(data == 1){\r\n");
					this.L.write("location.href=\"list.html\";\r\n");
					this.L.write("}\r\n");
					this.L.write("}).error(function(data, status, headers, config){\r\n");
					this.L.write("response(status);\r\n");
					this.L.write("})\r\n");
					this.L.write("}\r\n");
					this.L.write("}\r\n");
					this.L.write("$scope.initselect = function() {\r\n");
					this.L.write("$http({\r\n");
					this.L.write("url : parent.getMenuLinksHref(\"dictionarylist\"),\r\n");
					this.L.write("method : 'POST',\r\n");
					this.L.write("dataType : \"json\",\r\n");
					this.L.write("withCredentials : true,\r\n");
					this.L.write("headers : {\r\n");
					this.L.write("'Content-Type' : 'application/json;charset=UTF-8;'\r\n");
					this.L.write("},\r\n");
					this.L.write("data : JSON.stringify([20])\r\n");
					this.L.write("}).success(function(data, status, headers, config) {\r\n");
					this.L.write("$scope.selects = data;\r\n");
					this.L.write("}).error(function(data, status, headers, config) {\r\n");
					this.L.write("response(status);\r\n");
					this.L.write("})\r\n");
					this.L.write("}\r\n");
					this.L.write("$scope.initselect();\r\n");
					this.L.write("})\r\n");
					this.L.write("</script>\r\n");
					this.L.write("</head>\r\n");
					this.L.write("\r\n");
					this.L.write("<body ng-controller=\"controller\">\r\n");
					this.L.write("<form method=\"post\"id=\"saveForm\"action=\"\">\r\n");
					this.L.write("<!-- 操作按钮操作区 -->\r\n");
					this.L.write("<div class=\"col-lg-12 topActionPanel\">\r\n");
					this.L.write("<div>\r\n");
					this.L.write("<button type=\"button\"class=\"btn btn-primary\"ng-click=\"onPreserved()\">确 定</button>\r\n");
					this.L.write("</div>\r\n");
					this.L.write("<div>\r\n");
					this.L.write("<button type=\"button\"class=\"btn btn-primary\"ng-click=\"onReturn()\">返 回</button>\r\n");
					this.L.write("</div>\r\n");
					this.L.write("</div>\r\n");
					this.L.write("<div class=\"col-lg-6\">\r\n");
					for (int i = 0; i < r.length; i++) {
						if (ts[i][1].toString().equals("id") || ts[i][1].toString().equals("version"))
							continue;
						if (ts[i][1].toString().indexOf("id") != -1) {
							this.L.write("<div class=\"input-group input-group-sm\">\r\n");
							this.L.write("<span class=\"input-group-addon text-right spanwidth\">" + ts[i][2] + "</span>\r\n");
							this.L.write("<select class=\"form-control required\"name=\"" + ts[i][1] + "\"ng-model=\"formData." + ts[i][1]
									+ "\"ng-options=\"data.code as data.name for data in selects| filter: {typecode: '20'}\">\r\n");
							this.L.write("<option value=''>请选择" + ts[i][2] + "</option>\r\n");
							this.L.write("</select>\r\n");
							this.L.write("<span class=\"input-group-addon verifyprompt\">*</span>\r\n");
							this.L.write("</div>\r\n");
						} else if (ts[i][1].toString().indexOf("date") != -1) {
							columnname = ts[i][1];
							this.L.write("<div class=\"input-group input-group-sm\"id=\"" + ts[i][1] + "hidden\">\r\n");
							this.L.write("<span class=\"input-group-addon text-right spanwidth\">" + ts[i][2] + "</span>\r\n");
							this.L.write("<div class=\"input-group date form_datetime\"id=\"" + ts[i][1]
									+ "date\"style=\"width:400px;\"data-date-format=\"dd MM yyyy - HH:ii p\"data-link-field=\"dtp_input\">\r\n");
							this.L.write("<input class=\"form-control required\"id=\"" + ts[i][1] + "\"ng-model=\"formData." + ts[i][1]
									+ "\"size=\"14\"type=\"text\"value=\"\"readonly>\r\n");
							this.L.write("<span class=\"input-group-addon\"><span class=\"glyphicon glyphicon-th\"></span></span>\r\n");
							this.L.write("</div>\r\n");
							this.L.write("<input type=\"hidden\"id=\"dtp_input\"value=\"\"/>\r\n");
							this.L.write("</div>\r\n");
						} else {
							this.L.write("<div class=\"input-group input-group-sm\">\r\n");
							this.L.write("<span class=\"input-group-addon text-right spanwidth\">" + ts[i][2] + "</span>\r\n");
							this.L.write("<input type=\"text\"class=\"form-control required\"placeholder=\"请输入" + ts[i][2] + "\"name=\"" + ts[i][1]
									+ "\"ng-model=\"formData." + ts[i][1] + "\"maxlength=\"" + ts[i][3] + "\">\r\n");
							this.L.write("<span class=\"input-group-addon verifyprompt\">*</span>\r\n");
							this.L.write("</div>\r\n");
						}
					}
					this.L.write("</div>\r\n");
					this.L.write("</form>\r\n");
					this.L.write("</body>\r\n");
					this.L.write("<script type=\"text/javascript\">\r\n");
					this.L.write("$('#" + columnname + "date').datetimepicker({\r\n");
					this.L.write("format : 'yyyy-mm-dd',\r\n");
					this.L.write("language : 'cn',\r\n");
					this.L.write("weekStart : 1,\r\n");
					this.L.write("minView : \"month\",\r\n");
					this.L.write("todayBtn : 1,\r\n");
					this.L.write("initialDate:new Date(),\r\n");
					this.L.write("autoclose : 1,\r\n");
					this.L.write("todayHighlight : 1,\r\n");
					this.L.write("startView : 2,\r\n");
					this.L.write("forceParse : 0,\r\n");
					this.L.write("showMeridian : 0\r\n");
					this.L.write("});\r\n");
					this.L.write("</html>\r\n");
					this.L.flush();

					file = a(bt, "info");
					this.K = new FileWriter(file);
					this.L = new BufferedWriter(this.K);
					this.L.write("<!DOCTYPE html>\r\n");
					this.L.write("<html ng-app=\"info\">\r\n");
					this.L.write("<head>\r\n");
					this.L.write("<title>查看</title>\r\n");
					this.L.write("<meta http-equiv=\"keywords\"content=\"keyword1,keyword2,keyword3\">\r\n");
					this.L.write("<meta http-equiv=\"description\"content=\"this is my page\">\r\n");
					this.L.write("<meta http-equiv=\"content-type\"content=\"text/html; charset=UTF-8\">\r\n");
					this.L.write("<!-- 基础样式 -->\r\n");
					this.L.write("<link rel=\"icon\"type=\"image/x-icon\"href=\"../../../resource/img/logo.ico\">\r\n");
					this.L.write("<link rel=\"stylesheet\"type=\"text/css\"href=\"../../../../resource/css/bootstrap.css\">\r\n");
					this.L.write("<link rel=\"stylesheet\"type=\"text/css\"href=\"../../../../resource/css/common.css\">\r\n");
					this.L.write("<!-- 基础js -->\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/angular-1.0.1.min.js\"></script>\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/jquery-1.9.1.min.js\"></script>\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/response.js\"></script>\r\n");
					this.L.write("<!-- 初始化 -->\r\n");
					this.L.write("<script type=\"text/javascript\">\r\n");
					this.L.write("var info = angular.module(\"info\", []);\r\n");
					this.L.write("info.controller(\"controller\",function($scope, $http) {\r\n");
					this.L.write("//返回\r\n");
					this.L.write("$scope.onReturn=function(){\r\n");
					this.L.write("history.go(-1);\r\n");
					this.L.write("}\r\n");
					this.L.write("$http({\r\n");
					this.L.write("url:parent.getFunctionLinksHref(\"self\"),\r\n");
					this.L.write("method:'GET',\r\n");
					this.L.write("dataType : \"json\",\r\n");
					this.L.write("withCredentials: true,\r\n");
					this.L.write("headers: {'Content-Type': 'application/json;charset=UTF-8;'},\r\n");
					this.L.write("data: ''\r\n");
					this.L.write("}).success(function(data, status, headers, config){\r\n");
					this.L.write("$scope.formData= data;\r\n");
					this.L.write("}).error(function(data, status, headers, config){\r\n");
					this.L.write("response(status);\r\n");
					this.L.write("});\r\n");
					this.L.write("})\r\n");
					this.L.write("</script>\r\n");
					this.L.write("</head>\r\n");
					this.L.write("<body ng-controller=\"controller\">\r\n");
					this.L.write("<div class=\"col-lg-12 topActionPanel\">\r\n");
					this.L.write("<div>\r\n");
					this.L.write("<button type=\"button\"class=\"btn btn-primary\"ng-click=\"onReturn()\">返 回</button>\r\n");
					this.L.write("</div>\r\n");
					this.L.write("</div>\r\n");
					this.L.write("<div class=\"col-lg-6 viewFixedWidth\">\r\n");
					for (int i = 0; i < r.length; i++) {
						if (ts[i][1].toString().equals("id") || ts[i][1].toString().equals("version"))
							continue;
						this.L.write("<div class=\"input-group input-group-sm\">\r\n");
						this.L.write("<span class=\"input-group-addon text-right spanwidth\">" + ts[i][2] + "</span>\r\n");
						this.L.write("<input type=\"text\"class=\"form-control\"value=\"{{formData." + ts[i][1] + "}}\"disabled>\r\n");
						this.L.write("</div>\r\n");
					}
					this.L.write("</div>\r\n");
					this.L.write("</body>\r\n");
					this.L.write("</html>\r\n");
					this.L.flush();

					file = a(bt, "list");
					this.K = new FileWriter(file);
					this.L = new BufferedWriter(this.K);
					this.L.write("<!DOCTYPE html>\r\n");
					this.L.write("<html ng-app=\"list\">\r\n");
					this.L.write("<head>\r\n");
					this.L.write("<title>列表</title>\r\n");
					this.L.write("<meta http-equiv=\"keywords\"content=\"keyword1,keyword2,keyword3\">\r\n");
					this.L.write("<meta http-equiv=\"description\"content=\"this is my page\">\r\n");
					this.L.write("<meta http-equiv=\"content-type\"content=\"text/html; charset=UTF-8\">\r\n");
					this.L.write("<!-- 基础样式 -->\r\n");
					this.L.write("<link rel=\"icon\"type=\"image/x-icon\"href=\"../../../resource/img/logo.ico\">\r\n");
					this.L.write("<link rel=\"stylesheet\"type=\"text/css\"href=\"../../../../resource/css/bootstrap.css\">\r\n");
					this.L.write("<link rel=\"stylesheet\"type=\"text/css\"href=\"../../../../resource/css/list.css\">\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/jquery-1.9.1.min.js\"></script>\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/angular-1.0.1.min.js\"></script>\r\n");
					this.L.write("<!-- 弹出框 -->\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/artDialog/skins/js/artDialog.source.js\"></script>\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/artDialog/skins/js/iframeTools.source.js\"></script>\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/artDialog/skins/js/artDialogZJ.js\"></script>\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/getUrlParam.js\"></script>\r\n");
					this.L.write("<!-- 分页 -->\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/paging.js\"></script>\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/response.js\"></script>\r\n");
					this.L.write("<script type=\"text/javascript\">\r\n");
					this.L.write("var flag = true;\r\n");
					this.L.write("var list = angular.module(\"list\", []);\r\n");
					this.L.write("list.controller(\"controller\",function($scope, $http) {\r\n");
					this.L.write("$scope.toInfo=function(data){\r\n");
					this.L.write("parent.setFunctionLinksHref(data.links==null?data._links:data.links);\r\n");
					this.L.write("location.href=\"info.html\";\r\n");
					this.L.write("}\r\n");
					this.L.write("$scope.toUpdate=function(data){\r\n");
					this.L.write("parent.setFunctionLinksHref(data.links==null?data._links:data.links);\r\n");
					this.L.write("location.href=\"update.html\";\r\n");
					this.L.write("}\r\n");
					this.L.write("$scope.toDelete=function(data){\r\n");
					this.L.write("parent.setFunctionLinksHref(data.links==null?data._links:data.links);\r\n");
					this.L.write("parent.onPopupDelete();\r\n");
					this.L.write("}\r\n");
					this.L.write("$scope.onSearch=function(){\r\n");
					this.L.write("$scope.pageList();\r\n");
					this.L.write("}\r\n");
					this.L.write("$scope.changePage=function(){\r\n");
					this.L.write("$scope.pageList();\r\n");
					this.L.write("}\r\n");
					this.L.write("$scope.onAdd=function(){\r\n");
					this.L.write("location.href=\"add.html\";\r\n");
					this.L.write("}\r\n");
					this.L.write("$scope.pageList = function(){\r\n");
					this.L.write("pageNo = parent.currentPage;\r\n");
					this.L.write("var data = {\"name\":$(\"#name\").val()};\r\n");
					this.L.write("$http({\r\n");
					this.L.write("url:parent.getMenuLinksHref(\"" + tb.toLowerCase() + "list\"),\r\n");
					this.L.write("method:'POST',\r\n");
					this.L.write("dataType : \"json\",\r\n");
					this.L.write("withCredentials: true,\r\n");
					this.L.write("headers: {'Content-Type': 'application/json;charset=UTF-8;',\"pageNo\":pageNo,\"pageSize\":pageSize,\"orderby\":\"id\",\"dir\":\"desc\"},\r\n");
					this.L.write("data: JSON.stringify(data)\r\n");
					this.L.write("}).success(function(data, status, headers, config){\r\n");
					this.L.write("totalCount = headers('totalCount');\r\n");
					this.L.write("pageCount = headers('pageCount');\r\n");
					this.L.write("$scope.datalist= data;\r\n");
					this.L.write("paging();\r\n");
					this.L.write("initPage();\r\n");
					this.L.write("}).error(function(data, status, headers, config){\r\n");
					this.L.write("response(status);\r\n");
					this.L.write("});\r\n");
					this.L.write("}\r\n");
					this.L.write("$scope.pageList();\r\n");
					this.L.write("$scope.onDelete = function(){\r\n");
					this.L.write("$http({\r\n");
					this.L.write("url:parent.getFunctionLinksHref(\"self\"),\r\n");
					this.L.write("method:'DELETE',\r\n");
					this.L.write("dataType : \"json\",\r\n");
					this.L.write("withCredentials: true,\r\n");
					this.L.write("headers: {'Content-Type': 'application/json;charset=UTF-8;'},\r\n");
					this.L.write("data: \"\"\r\n");
					this.L.write("}).success(function(data, status, headers, config){\r\n");
					this.L.write("$scope.pageList();\r\n");
					this.L.write("}).error(function(data, status, headers, config){\r\n");
					this.L.write("response(status);\r\n");
					this.L.write("});\r\n");
					this.L.write("}\r\n");
					this.L.write("})\r\n");
					this.L.write("\r\n");
					this.L.write("function confirmDeletion(){\r\n");
					this.L.write("var appElement = document.querySelector('[ng-controller=controller]');\r\n");
					this.L.write("var $scope = angular.element(appElement).scope();\r\n");
					this.L.write("$scope.onDelete();\r\n");
					this.L.write("}\r\n");
					this.L.write("</script>\r\n");
					this.L.write("</head> \r\n");
					this.L.write("<body ng-controller=\"controller\"style=\"color: #000;\">\r\n");
					this.L.write("<!-- 基本操作区 -->\r\n");
					this.L.write("<h5>\r\n");
					this.L.write("<span class=\"glyphicon glyphicon-search\"style=\"margin:0px 5px;color:#99DFF8;\"></span>\r\n");
					this.L.write("<input type=\"text\"id=\"name\"class=\"form-control\"value=\"\"placeholder=\"请输入需要查找的名称\"style=\"width: 20%;display: inline;\">\r\n");
					this.L.write("<i class=\"glyphicon glyphicon-remove\"onclick=\"removeSearch()\"></i>\r\n");
					this.L.write("<a class=\"btn btn-primary\"style=\"margin: 0px auto;width: 80px;\"href=\"#\"role=\"button\"ng-click=\"onSearch()\">搜索</a>\r\n");
					this.L.write("<a class=\"btn btn-primary\"style=\"margin: 0px auto;width: 80px;\"href=\"#\"role=\"button\"ng-click=\"onAdd()\">添加</a>\r\n");
					this.L.write("</h5>\r\n");
					this.L.write("<!-- 列表区 -->\r\n");
					this.L.write("<table class=\"table\">\r\n");
					this.L.write("<thead>\r\n");
					this.L.write("<tr class=\"info\">\r\n");
					this.L.write("<th>序号</th>\r\n");
					for (int i = 0; i < r.length; i++) {
						if (ts[i][1].toString().equals("id") || ts[i][1].toString().equals("version"))
							continue;
						this.L.write("<th>" + ts[i][2] + "</th>\r\n");
					}
					this.L.write("<th>相关操作</th>\r\n");
					this.L.write("</tr>\r\n");
					this.L.write("</thead>\r\n");
					this.L.write("<tbody>\r\n");
					this.L.write("<tr ng-repeat=\"data in datalist\"class='tr{{$index%2+2}}'ng-class=\"{selected: $index==selectedRow}\">\r\n");
					this.L.write("<td ng-dblclick=\"toInfo(data)\">{{$index+1}}</td>\r\n");
					for (int i = 0; i < r.length; i++) {
						if (ts[i][1].toString().equals("id") || ts[i][1].toString().equals("version"))
							continue;
						this.L.write("<td ng-dblclick=\"toInfo(data)\">{{data." + tn + "." + ts[i][1] + "}}</td>\r\n");
					}
					this.L.write("<td>\r\n");
					this.L.write("<h4 ng-click=\"toInfo(data)\"class=\"glyphicon glyphicon-search\"style=\"margin:0px 5px;color:#99DFF8;\"title=\"查看\"></h4>\r\n");
					this.L.write("<h4 ng-click=\"toUpdate(data)\"class=\"glyphicon glyphicon-pencil\"style=\"margin:0px 5px;color:#99DFF8;\"title=\"修改\"></h4>\r\n");
					this.L.write("<h4 ng-click=\"toDelete(data)\"class=\"glyphicon glyphicon-remove\"style=\"margin:0px 5px;color:#99DFF8;\"title=\"删除\"></h4>\r\n");
					this.L.write("</td>\r\n");
					this.L.write("</tr>\r\n");
					this.L.write("</tbody>\r\n");
					this.L.write("</table>\r\n");
					this.L.write("<!-- 分页区 -->\r\n");
					this.L.write("<div class=\"pagepanel\">\r\n");
					this.L.write("<span class=\"text-info\"style=\"position:relative;top:-10px;\">共<label id=\"totalCount\"></label>条</span>\r\n");
					this.L.write("<ul class=\"pagination\"style=\"margin: 0px 10px;padding:0px;\">\r\n");
					this.L.write("</ul>\r\n");
					this.L.write("<span class=\"text-info\"style=\"position:relative;top:-10px;\">第<label id=\"pageNo\"></label>页/共<label id=\"pageCount\"></label>页</span>\r\n");
					this.L.write("<select id=\"pageSize\"class=\"form-control\"style=\"width: 70px;float: right;\">\r\n");
					this.L.write("</select>\r\n");
					this.L.write("<span class=\"text-info\"style=\"position:relative;top:6px;float: right;margin: 0px 5px;\">每页显示</span>\r\n");
					this.L.write("</div>\r\n");
					this.L.write("</body>\r\n");
					this.L.write("</html>\r\n");
					this.L.flush();

					file = a(bt, "update");
					this.K = new FileWriter(file);
					this.L = new BufferedWriter(this.K);
					this.L.write("<!DOCTYPE html>\r\n");
					this.L.write("<html ng-app=\"add\">\r\n");
					this.L.write("<head>\r\n");
					this.L.write("<title>修改</title>\r\n");
					this.L.write("<meta http-equiv=\"keywords\"content=\"keyword1,keyword2,keyword3\">\r\n");
					this.L.write("<meta http-equiv=\"description\"content=\"this is my page\">\r\n");
					this.L.write("<meta http-equiv=\"content-type\"content=\"text/html; charset=UTF-8\">\r\n");
					this.L.write("<link rel=\"icon\"type=\"image/x-icon\"href=\"../../../resource/img/logo.ico\">\r\n");
					this.L.write("<link rel=\"stylesheet\"type=\"text/css\"href=\"../../../../resource/css/bootstrap.css\">\r\n");
					this.L.write("<link rel=\"stylesheet\"type=\"text/css\"href=\"../../../../resource/css/common.css\">\r\n");
					this.L.write("<link rel=\"stylesheet\"type=\"text/css\"href=\"../../../../resource/js/validate/css/validate.css\"/>\r\n");
					this.L.write("<!-- 基础js -->\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/angular-1.0.1.min.js\"></script>\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/jquery-1.9.1.min.js\"></script>\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/bootstrap.min.js\"></script>\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/response.js\"></script>\r\n");
					this.L.write("<!-- 表单验证 -->\r\n");
					this.L.write("<script src=\"../../../../resource/js/date/js/eap.core.js\"></script>\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/validate/js/eap.tip.js\"></script>\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/validate/js/eap.validate.js\"></script>\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/date/js/eap.lang-zh_CN.js\"></script>\r\n");
					this.L.write("<!-- datetime -->\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/bootstrap-datetimepicker.js\"charset=\"UTF-8\"></script>\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/locales/bootstrap-datetimepicker.fr.js\"charset=\"UTF-8\"></script>\r\n");
					this.L.write("<script type=\"text/javascript\"src=\"../../../../resource/js/date/js/eap.util.date.js\"></script>\r\n");
					this.L.write("<!-- 初始化 -->\r\n");
					this.L.write("<script type=\"text/javascript\">\r\n");
					this.L.write("var add = angular.module(\"add\", []);\r\n");
					this.L.write("add.controller(\"controller\",function($scope, $http) {\r\n");
					this.L.write("$scope.formData = {};\r\n");
					this.L.write("$http({\r\n");
					this.L.write("url:parent.getFunctionLinksHref(\"self\"),\r\n");
					this.L.write("method:'GET',\r\n");
					this.L.write("dataType : \"json\",\r\n");
					this.L.write("withCredentials: true,\r\n");
					this.L.write("headers: {'Content-Type': 'application/json;charset=UTF-8;'},\r\n");
					this.L.write("data: ''\r\n");
					this.L.write("}).success(function(data, status, headers, config){\r\n");
					this.L.write("$scope.formData= data\r\n");
					this.L.write("}).error(function(data, status, headers, config){\r\n");
					this.L.write("response(status);\r\n");
					this.L.write("});\r\n");
					this.L.write("$scope.onReturn=function(){\r\n");
					this.L.write("history.go(-1);\r\n");
					this.L.write("}\r\n");
					this.L.write("$scope.onPreserved=function(){\r\n");
					this.L.write("var isValid = $(\"#saveForm\").valid();\r\n");
					this.L.write("if(isValid){\r\n");
					this.L.write("$http({\r\n");
					this.L.write("url:parent.getFunctionLinksHref(\"self\"),\r\n");
					this.L.write("method:'PUT',\r\n");
					this.L.write("dataType : \"json\",\r\n");
					this.L.write("withCredentials: true,\r\n");
					this.L.write("headers: {'Content-Type': 'application/json;charset=UTF-8;'},\r\n");
					this.L.write("data: JSON.stringify($scope.formData)\r\n");
					this.L.write("}).success(function(data, status, headers, config){\r\n");
					this.L.write("location.href=\"list.html\";\r\n");
					this.L.write("}).error(function(data, status, headers, config){\r\n");
					this.L.write("response(status);\r\n");
					this.L.write("})\r\n");
					this.L.write("}\r\n");
					this.L.write("}\r\n");
					this.L.write("$scope.initselect = function() {\r\n");
					this.L.write("$http({\r\n");
					this.L.write("url : parent.getMenuLinksHref(\"dictionarylist\"),\r\n");
					this.L.write("method : 'POST',\r\n");
					this.L.write("dataType : \"json\",\r\n");
					this.L.write("withCredentials : true,\r\n");
					this.L.write("headers : {\r\n");
					this.L.write("'Content-Type' : 'application/json;charset=UTF-8;'\r\n");
					this.L.write("},\r\n");
					this.L.write("data : JSON.stringify([20])\r\n");
					this.L.write("}).success(function(data, status, headers, config) {\r\n");
					this.L.write("$scope.selects = data;\r\n");
					this.L.write("}).error(function(data, status, headers, config) {\r\n");
					this.L.write("response(status);\r\n");
					this.L.write("})\r\n");
					this.L.write("}\r\n");
					this.L.write("$scope.initselect();\r\n");
					this.L.write("})\r\n");
					this.L.write("</script>\r\n");
					this.L.write("</head>\r\n");
					this.L.write("<body ng-controller=\"controller\">\r\n");
					this.L.write("<form method=\"post\"id=\"saveForm\"action=\"\">\r\n");
					this.L.write("<!-- 操作按钮操作区 -->\r\n");
					this.L.write("<div class=\"col-lg-12 topActionPanel\">\r\n");
					this.L.write("<div>\r\n");
					this.L.write("<button type=\"button\"class=\"btn btn-primary\"ng-click=\"onPreserved()\">确 定</button>\r\n");
					this.L.write("</div>\r\n");
					this.L.write("<div>\r\n");
					this.L.write("<button type=\"button\"class=\"btn btn-primary\"ng-click=\"onReturn()\">返 回</button>\r\n");
					this.L.write("</div>\r\n");
					this.L.write("</div>\r\n");
					this.L.write("<div class=\"col-lg-6\">\r\n");
					for (int i = 0; i < r.length; i++) {
						if (ts[i][1].toString().equals("id") || ts[i][1].toString().equals("version"))
							continue;
						if (ts[i][1].toString().indexOf("id") != -1) {
							this.L.write("<div class=\"input-group input-group-sm\">\r\n");
							this.L.write("<span class=\"input-group-addon text-right spanwidth\">" + ts[i][2] + "</span>\r\n");
							this.L.write("<select class=\"form-control required\"name=\"" + ts[i][1] + "\"ng-model=\"formData." + ts[i][1]
									+ "\"ng-options=\"data.code as data.name for data in selects| filter: {typecode: '20'}\">\r\n");
							this.L.write("<option value=''>请选择" + ts[i][2] + "</option>\r\n");
							this.L.write("</select>\r\n");
							this.L.write("<span class=\"input-group-addon verifyprompt\">*</span>\r\n");
							this.L.write("</div>\r\n");
						} else if (ts[i][1].toString().indexOf("date") != -1) {
							columnname = ts[i][1];
							this.L.write("<div class=\"input-group input-group-sm\"id=\"" + ts[i][1] + "hidden\">\r\n");
							this.L.write("<span class=\"input-group-addon text-right spanwidth\">" + ts[i][2] + "</span>\r\n");
							this.L.write("<div class=\"input-group date form_datetime\"id=\"" + ts[i][1]
									+ "date\"style=\"width:400px;\"data-date-format=\"dd MM yyyy - HH:ii p\"data-link-field=\"dtp_input\">\r\n");
							this.L.write("<input class=\"form-control required\"id=\"" + ts[i][1] + "\"ng-model=\"formData." + ts[i][1]
									+ "\"size=\"14\"type=\"text\"value=\"\"readonly>\r\n");
							this.L.write("<span class=\"input-group-addon\"><span class=\"glyphicon glyphicon-th\"></span></span>\r\n");
							this.L.write("</div>\r\n");
							this.L.write("<input type=\"hidden\"id=\"dtp_input\"value=\"\"/>\r\n");
							this.L.write("</div>\r\n");
						} else {
							this.L.write("<div class=\"input-group input-group-sm\">\r\n");
							this.L.write("<span class=\"input-group-addon text-right spanwidth\">" + ts[i][2] + "</span>\r\n");
							this.L.write("<input type=\"text\"class=\"form-control required\"placeholder=\"请输入" + ts[i][2] + "\"name=\"" + ts[i][1]
									+ "\"ng-model=\"formData." + ts[i][1] + "\"maxlength=\"" + ts[i][3] + "\">\r\n");
							this.L.write("<span class=\"input-group-addon verifyprompt\">*</span>\r\n");
							this.L.write("</div>\r\n");
						}
					}
					this.L.write("</div>\r\n");
					this.L.write("</form>\r\n");
					this.L.write("</body>\r\n");
					this.L.write("<script type=\"text/javascript\">\r\n");
					this.L.write("$('#" + columnname + "date').datetimepicker({\r\n");
					this.L.write("format : 'yyyy-mm-dd',\r\n");
					this.L.write("language : 'cn',\r\n");
					this.L.write("weekStart : 1,\r\n");
					this.L.write("minView : \"month\",\r\n");
					this.L.write("todayBtn : 1,\r\n");
					this.L.write("initialDate:new Date(),\r\n");
					this.L.write("autoclose : 1,\r\n");
					this.L.write("todayHighlight : 1,\r\n");
					this.L.write("startView : 2,\r\n");
					this.L.write("forceParse : 0,\r\n");
					this.L.write("showMeridian : 0\r\n");
					this.L.write("});\r\n");
					this.L.write("</script>\r\n");
					this.L.write("</html>\r\n");
					this.L.flush();
					this.L.close();
				}

				// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				if (data) {
					String comment = "";
					Long lid1 = Long.valueOf(J.nextId());
					Long lid2 = Long.valueOf(J.nextId());
					Long lid3 = Long.valueOf(J.nextId());
					Long lid4 = Long.valueOf(J.nextId());
					Long lid5 = Long.valueOf(J.nextId());
					String tl = tb.toLowerCase();
					rs = stmt.executeQuery("SELECT TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'edu' and TABLE_NAME = '" + tb
							+ "'");
					if (rs.next()) {
						comment = rs.getString("TABLE_COMMENT");
						if (!comment.equals(""))
							comment = comment.replace("表", "");
						else
							comment = tl;
					}
					if (comment.length() >= 10)
						comment = comment.substring(0, 10);
					rs.close();
					stmt.executeUpdate("INSERT INTO link VALUES (" + lid1 + ", '" + tb + "新增资源', '" + tl
							+ "post', 'http://localhost:17000/dfgj/rest/v1/" + tl + "', '0')");
					stmt.executeUpdate("INSERT INTO link VALUES (" + lid2 + ", '" + tb + "集合资源', '" + tl
							+ "list', 'http://localhost:17000/dfgj/rest/v1/" + tl + "/', '0')");
					stmt.executeUpdate("INSERT INTO menu VALUES (" + lid3 + ", '" + comment + "', '', '31', '../../dfgj/html/" + tl
							+ "/list.html', null, '" + sysid + "', '1', '1', '41', '0')");
					stmt.executeUpdate("INSERT INTO menu_links VALUES (" + lid4 + ", " + lid3 + ", " + lid1 + ", '0')");
					stmt.executeUpdate("INSERT INTO menu_links VALUES (" + lid5 + ", " + lid3 + ", " + lid2 + ", '0')");
				}
				System.out.println("<<<<<<<========：" + tb);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
