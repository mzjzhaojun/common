package com.yt.app.frame.generate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.yt.app.util.DateTimeUtil;
import com.yt.app.util.DbConnection;

public class GenerateCode {
	private static GenerateCode ac;
	private static GenerateId gid;

	private GenerateCode() {
		init();
	}

	public static GenerateCode getInstance() {
		if (ac == null) {
			ac = new GenerateCode();
		}
		if (gid == null)
			gid = new GenerateId(1, 1);
		return ac;
	}

	File file;
	FileWriter fw;
	BufferedWriter bw;
	private DbConnection sqlcon = DbConnection.getInstance();
	private String FILE_PATH_NAME = "/config/mysql.properties";

	private void init() {
		try {
			File directory = new File("");
			String courseFile = directory.getCanonicalPath();
			String filePaths = DbConnection.class.getResource(FILE_PATH_NAME).getPath();
			File file = new File(filePaths);
			InputStream in = new FileInputStream(file);
			Properties props = new Properties();
			props.load(in);
			in.close();
			DbConnection.basePage = "com.yt.app.api." + DbConnection.version;
			DbConnection.commndPage = "com.yt.app.common.base";
			DbConnection.filePath = courseFile + "/src/main/java/com/yt/app/api/" + DbConnection.version;
			DbConnection.pagefilePath = courseFile + "/resources/templates/static/project/dfgj/html/";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createNewFile(String table) {
		file = new File(DbConnection.filePath + "/mapper/impl");
		file.mkdirs();
		file = new File(DbConnection.filePath + "/service/impl");
		file.mkdirs();
		file = new File(DbConnection.filePath + "/controller");
		file.mkdirs();
		file = new File(DbConnection.filePath + "/entity");
		file.mkdirs();
		file = new File(DbConnection.filePath + "/mapper");
		file.mkdirs();
		file = new File(DbConnection.filePath + "/resource");
		file.mkdirs();
		file = new File(DbConnection.pagefilePath + "/" + table.toLowerCase());
		file.mkdirs();
	}

	private File getFileDao(String table) throws Exception {
		file = new File(DbConnection.filePath + "/mapper/" + table + "Mapper.java");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		return file;
	}

	private File getService(String table) throws Exception {
		file = new File(DbConnection.filePath + "/service/" + table + "Service.java");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		return file;
	}

	private File getServiceImpl(String table) throws Exception {
		file = new File(DbConnection.filePath + "/service/impl/" + table + "ServiceImpl.java");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		return file;
	}

	private File getController(String table) throws Exception {
		file = new File(DbConnection.filePath + "/controller/" + table + "Controller.java");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		return file;
	}

	private File getEntity(String table) throws Exception {
		file = new File(DbConnection.filePath + "/entity/" + table + ".java");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		return file;
	}

	private File getMapper(String table) throws Exception {
		file = new File(DbConnection.filePath + "/mapper/impl/" + table + "Mapper.xml");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		return file;
	}

	private File getResource(String table) throws Exception {
		file = new File(DbConnection.filePath + "/resource/" + table + "Resource.java");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		return file;
	}

	private File getResourceAssembler(String table) throws Exception {
		file = new File(DbConnection.filePath + "/resource/" + table + "ResourceAssembler.java");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		return file;
	}

	private File getFileHtml(String table, String htmlname) throws Exception {
		file = new File(DbConnection.pagefilePath + "/" + table.toLowerCase() + "/" + htmlname + ".html");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		return file;
	}

	public List<String> getTables() {
		List<String> list = new ArrayList<String>();
		try {
			Connection conn = sqlcon.getCon();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = null;
			rs = stmt.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '" + DbConnection.dbName + "'");
			while (rs.next()) {
				list.add(rs.getString("TABLE_NAME"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public void crud(List<String> tables, boolean code) {
		this.crud(tables, code, false, false, null);
	}

	public void crud(List<String> tables, boolean code, boolean html) {
		this.crud(tables, code, html, false, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void crud(List<String> tables, boolean code, boolean html, boolean data, String sysid) {
		try {
			Connection conn = sqlcon.getCon();
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
				createNewFile(tn);
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
					file = getEntity(bt);
					fw = new FileWriter(file);
					bw = new BufferedWriter(fw);
					bw.write("package " + DbConnection.basePage + ".entity;\r\n\n");
					bw.write("import lombok.Getter;\r\n");
					bw.write("import lombok.Setter;\r\n");
					bw.write("import java.io.Serializable;\r\n");
					bw.write("/**\r\n");
					bw.write("* @author zj    default  \r\n");
					bw.write("* \r\n");
					bw.write("* @version " + DbConnection.version + "\r\n");
					bw.write("* @createdate  " + DateTimeUtil.getDateTime() + "\r\n");
					bw.write("*/\r\n");
					bw.write("@Getter\r\n");
					bw.write("@Setter\r\n");
					bw.write("public class " + bt + " implements Serializable{\r\n");
					bw.write("\r\n");
					bw.write("  private static final long serialVersionUID=1L;\r\n");
					bw.write("\r\n");
					for (int l = 0; l < r.length; l++) {
						bw.write("  " + ts[l][0] + " " + ts[l][1] + ";\r\n");
					}
					bw.write("  public " + bt + "(){\r\n");
					bw.write("  }\r\n");
					String gz = "";
					for (int i = 0; i < r.length; i++)
						gz += "," + ts[i][0] + " " + ts[i][1];
					bw.write("  public " + bt + "(" + gz.substring(1) + "){\r\n");
					for (int i = 0; i < r.length; i++)
						bw.write("      this." + ts[i][1] + "=" + ts[i][1] + ";\r\n");
					bw.write("  }\r\n");
					bw.write("}");
					bw.flush();
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
					bw.flush();

					file = getMapper(bt);
					fw = new FileWriter(file);
					bw = new BufferedWriter(fw);
					bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
					bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\r\n");
					bw.write("<mapper namespace=\"" + DbConnection.basePage + ".mapper." + bt + "Mapper\">\r\n");

					// Base_Column_List
					bw.write("  <!-- 数据库列 -->\r\n");
					bw.write("  <sql id=\"Base_Column_List\">\r\n");
					bw.write("     " + column + "\r\n");
					bw.write("  </sql>\r\n");

					// ResultMap
					bw.write("  <!-- 基础返回对象封装 -->\r\n");
					bw.write("  <resultMap id=\"ResultMap\" type=\"" + DbConnection.basePage + ".entity." + bt + "\">\r\n");
					for (int i = 0; i < r.length; i++) {
						bw.write("         <id property=\"" + ts[i][1] + "\" column=\"" + ts[i][1] + "\" />\r\n");
					}
					bw.write("  </resultMap>\r\n");

					bw.write("  <!-- 默认新增 -->\r\n");
					bw.write("  <insert id=\"post\" parameterType=\"" + DbConnection.basePage + ".entity." + bt + "\">\r\n");
					bw.write("  insert into " + tb + " (" + insertColumn + ")\r\n");
					bw.write("         values (" + insertValue + ")\r\n");
					bw.write("  </insert>\r\n");

					// deleteByIds
					bw.write("  <!-- 默认删除id对象 -->\r\n");
					bw.write(" <delete id=\"delete\" parameterType=\"java.lang.String\">\r\n");
					bw.write("     delete from " + tb + " \r\n");
					bw.write("     where id = #{id}\r\n");
					bw.write(" </delete>\r\n");

					// update
					bw.write("  <!-- 默认更新id对象 -->\r\n");
					bw.write(" <update id=\"put\" parameterType=\"" + DbConnection.basePage + ".entity." + bt + "\">\r\n");
					bw.write("    update " + tb + "\r\n");
					bw.write("         <set>\r\n");
					for (int i = 0; i < r.length; i++) {
						if (ts[i][1].equals("id"))
							continue;
						bw.write("             <if test=\"" + ts[i][1] + " != null\">\r\n");
						if (i < r.length - 1) {
							if (ts[i][1].equals("version"))
								bw.write("                   " + ts[i][1] + " = #{" + ts[i][1] + "}+1,\r\n");
							else
								bw.write("                   " + ts[i][1] + " = #{" + ts[i][1] + "},\r\n");
						} else if (i == r.length - 1) {
							if (ts[i][1].equals("version"))
								bw.write("                   " + ts[i][1] + " = #{" + ts[i][1] + "}+1\r\n");
							else
								bw.write("                   " + ts[i][1] + " = #{" + ts[i][1] + "}\r\n");
						}
						bw.write("             </if>\r\n");
					}
					bw.write("          </set>\r\n");
					bw.write("     where id = #{id} and  version = #{version}\r\n");
					bw.write(" </update>\r\n");

					// getById
					bw.write("  <!-- 默认获得id对象 -->\r\n");
					bw.write(" <select id=\"get\" parameterType=\"java.lang.String\" resultMap=\"ResultMap\">\r\n");
					bw.write("     select\r\n");
					bw.write("        <include refid=\"Base_Column_List\" />\r\n");
					bw.write("        from " + tb + " \r\n");
					bw.write("        where id = #{id}\r\n");
					bw.write(" </select>\r\n");

					// getList
					bw.write("  <!-- 默认集合 -->\r\n");
					bw.write(" <select id=\"list\" parameterType=\"java.util.HashMap\" resultMap=\"ResultMap\">\r\n");
					bw.write("       select\r\n");
					bw.write("        <include refid=\"Base_Column_List\" />\r\n");
					bw.write("        from " + tb + " \r\n");
					bw.write("        <where>\r\n");
					bw.write("        	1=1\r\n");
					bw.write("             <if test=\"name != null and name != ''\">\r\n");
					bw.write("           and name like \"%\"#{name}\"%\"\r\n");
					bw.write("             </if>\r\n");
					bw.write("             <if test=\"orderby != null and dir != null\">\r\n");
					bw.write("           order by ${orderby} ${dir}\r\n");
					bw.write("             </if>\r\n");
					bw.write("             <if test=\"pageStart != null and pageEnd != null\">\r\n");
					bw.write("           LIMIT #{pageStart},#{pageEnd}\r\n");
					bw.write("             </if>\r\n");
					bw.write("        </where>\r\n");
					bw.write(" </select>\r\n");

					// getMap
					bw.write("  <!-- 默认返回MAP集合 -->\r\n");
					bw.write(" <select id=\"map\" parameterType=\"java.util.HashMap\" resultType=\"java.util.HashMap\">\r\n");
					bw.write("       select\r\n");
					bw.write("        <include refid=\"Base_Column_List\" />\r\n");
					bw.write("        from " + tb + " \r\n");
					bw.write("        <where>\r\n");
					bw.write("        	1=1\r\n");
					bw.write("             <if test=\"name != null and name != ''\">\r\n");
					bw.write("           and name like \"%\"#{name}\"%\"\r\n");
					bw.write("             </if>\r\n");
					bw.write("             <if test=\"orderby != null and dir != null\">\r\n");
					bw.write("           order by ${orderby} ${dir}\r\n");
					bw.write("             </if>\r\n");
					bw.write("             <if test=\"pageStart != null and pageEnd != null\">\r\n");
					bw.write("           LIMIT #{pageStart},#{pageEnd}\r\n");
					bw.write("             </if>\r\n");
					bw.write("        </where>\r\n");
					bw.write(" </select>\r\n");
					// getCount
					bw.write("  <!-- 默认集合统计 -->\r\n");
					bw.write(" <select id=\"countlist\" parameterType=\"java.util.HashMap\" resultType=\"int\">\r\n");
					bw.write("       select count(*) from " + tb + " \r\n");
					bw.write("        <where>\r\n");
					bw.write("        	1=1\r\n");
					bw.write("             <if test=\"name != null and name != ''\">\r\n");
					bw.write("           and name like \"%\"#{name}\"%\"\r\n");
					bw.write("             </if>\r\n");
					bw.write("        </where>\r\n");
					bw.write(" </select>\r\n");
					bw.write("  <!-- 默认返回MAP集合统计 -->\r\n");
					bw.write(" <select id=\"countmap\" parameterType=\"java.util.HashMap\" resultType=\"int\">\r\n");
					bw.write("       select count(*) from " + tb + " \r\n");
					bw.write("        <where>\r\n");
					bw.write("        	1=1\r\n");
					bw.write("             <if test=\"name != null and name != ''\">\r\n");
					bw.write("           and name like \"%\"#{name}\"%\"\r\n");
					bw.write("             </if>\r\n");
					bw.write("        </where>\r\n");
					bw.write(" </select>\r\n");
					bw.write("  <!-- 默认获取Ids的对象 -->\r\n");
					bw.write(" <select id=\"listByArrayId\" parameterType=\"java.util.HashMap\" resultMap=\"ResultMap\">\r\n");
					bw.write("       select\r\n");
					bw.write("        <include refid=\"Base_Column_List\" />\r\n");
					bw.write("        from " + tb + " \r\n");
					bw.write("        <where>\r\n");
					bw.write("        1=1\r\n");
					bw.write("             <if test=\"array != null and array != ''\">\r\n");
					bw.write("       	and id in\r\n");
					bw.write("        	<foreach item=\"item\" index=\"index\" collection=\"array\" open=\"(\" separator=\",\" close=\")\">\r\n");
					bw.write("           	#{item}\r\n");
					bw.write("        	</foreach>\r\n");
					bw.write("             </if>\r\n");
					bw.write("        </where>\r\n");
					bw.write(" </select>\r\n");
					bw.write("</mapper>");
					bw.flush();

					file = getFileDao(bt);
					fw = new FileWriter(file);
					bw = new BufferedWriter(fw);
					bw.write("package " + DbConnection.basePage + ".mapper;\r\n");
					bw.write("import java.util.List;\r\n");
					bw.write("import java.util.Map;\r\n");
					bw.write("import " + DbConnection.basePage + "." + "entity." + bt + ";\r\n");
					bw.write("import com.yt.app.annotation.RedisCacheAnnotation;\r\n");
					bw.write("import com.yt.app.annotation.RedisCacheEvictAnnotation;\r\n");
					bw.write("import " + DbConnection.commndPage + ".IBaseMapper;\r\n");
					bw.write("\r\n");
					bw.write("\r\n");
					bw.write("/**\r\n");
					bw.write("* @author zj    default  \r\n");
					bw.write("* \r\n");
					bw.write("* @version " + DbConnection.version + "\r\n");
					bw.write("* @createdate  " + DateTimeUtil.getDateTime() + "\r\n");
					bw.write("*/\r\n");
					bw.write("\r\n");
					bw.write("public interface " + bt + "Mapper extends IBaseMapper<" + bt + "> {\r\n");
					bw.write("/**\r\n");
					bw.write(" * 保存（持久化）对象\r\n");
					bw.write(" * \r\n");
					bw.write(" * @param o\r\n");
					bw.write("*            要持久化的对象\r\n");
					bw.write(" * @return 执行成功的记录个数\r\n");
					bw.write(" */\r\n");
					bw.write("@RedisCacheEvictAnnotation(classs = { " + bt + ".class})\r\n");
					bw.write("public Integer post(Object t);\r\n");
					bw.write("/**\r\n");
					bw.write("* 更新（持久化）对象\r\n");
					bw.write("* \r\n");
					bw.write("* @param o\r\n");
					bw.write("*            要持久化的对象\r\n");
					bw.write("* @return 执行成功的记录个数\r\n");
					bw.write("*/\r\n");
					bw.write("	@RedisCacheEvictAnnotation(classs = { " + bt + ".class})\r\n");
					bw.write("	public Integer put(Object t);\r\n");
					bw.write("\r\n");
					bw.write("/**\r\n");
					bw.write("* 获取指定的唯一标识符对应的持久化对象\r\n");
					bw.write("*\r\n");
					bw.write("* @param id\r\n");
					bw.write("*            指定的唯一标识符\r\n");
					bw.write("* @return 指定的唯一标识符对应的持久化对象，如果没有对应的持久化对象，则返回null。\r\n");
					bw.write("*/\r\n");
					bw.write("	@RedisCacheAnnotation(classs = " + bt + ".class)\r\n");
					bw.write("	public " + bt + " get(Long id);\r\n");
					bw.write("\r\n");
					bw.write("/**\r\n");
					bw.write("* 删除指定的唯一标识符数组对应的持久化对象\r\n");
					bw.write("*\r\n");
					bw.write("* @param ids\r\n");
					bw.write("*            指定的唯一标识符数组\r\n");
					bw.write("* @return 删除的对象数量\r\n");
					bw.write("*/\r\n");
					bw.write("	@RedisCacheEvictAnnotation(classs = { " + bt + ".class})\r\n");
					bw.write("	public Integer delete(Long id);\r\n");
					bw.write("\r\n");
					bw.write("/**\r\n");
					bw.write("* 获取满足查询参数条件的数据总数\r\n");
					bw.write("* \r\n");
					bw.write("* @param param\r\n");
					bw.write("*            查询参数\r\n");
					bw.write("* @return 数据总数\r\n");
					bw.write("*/\r\n");
					bw.write("	@RedisCacheAnnotation(classs = " + bt + ".class)\r\n");
					bw.write("	public Integer countlist(Map<String, Object> param);\r\n");
					bw.write("\r\n");
					bw.write("/**\r\n");
					bw.write("* 获取满足查询参数条件的数据总数\r\n");
					bw.write("* \r\n");
					bw.write("* @param param\r\n");
					bw.write("*            查询参数\r\n");
					bw.write("* @return 数据总数\r\n");
					bw.write("*/\r\n");
					bw.write("	@RedisCacheAnnotation(classs = " + bt + ".class)\r\n");
					bw.write("	public Integer countmap(Map<String, Object> param);\r\n");
					bw.write("\r\n");
					bw.write("/**\r\n");
					bw.write(" * 获取满足查询参数条件的数据\r\n");
					bw.write("* \r\n");
					bw.write("* @param param\r\n");
					bw.write("*            查询参数\r\n");
					bw.write("* @return 数据\r\n");
					bw.write("*/\r\n");
					bw.write("	@RedisCacheAnnotation(classs = " + bt + ".class)\r\n");
					bw.write("	public List<" + bt + "> list(Map<String, Object> param);\r\n");
					bw.write("\r\n");
					bw.write("/**\r\n");
					bw.write("* 获取满足查询参数条件的数据\r\n");
					bw.write("* \r\n");
					bw.write("* @param param\r\n");
					bw.write("*            查询参数\r\n");
					bw.write("* @return 数据\r\n");
					bw.write("*/\r\n");
					bw.write("	@RedisCacheAnnotation(classs = " + bt + ".class)\r\n");
					bw.write("	public List<Map<String, Object>> map(Map<String, Object> param);\r\n");
					bw.write("\r\n");
					bw.write("/**\r\n");
					bw.write("* 获取满足查询参数条件的数据\r\n");
					bw.write("* \r\n");
					bw.write("* @param id\r\n");
					bw.write("*            查询参数\r\n");
					bw.write("* @return 数据\r\n");
					bw.write("*/\r\n");
					bw.write("	@RedisCacheAnnotation(classs = " + bt + ".class)\r\n");
					bw.write("	public List<" + bt + "> listByArrayId(long[] id);\r\n");
					bw.write("}");
					bw.flush();

					file = getService(bt);
					fw = new FileWriter(file);
					bw = new BufferedWriter(fw);
					bw.write("package " + DbConnection.basePage + ".service;\r\n");
					bw.write("\r\n");
					bw.write("\r\n");
					bw.write("import " + DbConnection.basePage + "." + "entity." + bt + ";\r\n");
					bw.write("import " + DbConnection.commndPage + ".IBaseService;\r\n");
					bw.write("/**\r\n");
					bw.write("* @author zj    default  \r\n");
					bw.write("* \r\n");
					bw.write("* @version " + DbConnection.version + "\r\n");
					bw.write("* @createdate  " + DateTimeUtil.getDateTime() + "\r\n");
					bw.write("*/\r\n");
					bw.write("\r\n");
					bw.write("public interface " + bt + "Service  extends IBaseService<" + bt + ", Long>{\r\n");
					bw.write("}");
					bw.flush();

					file = getServiceImpl(bt);
					fw = new FileWriter(file);
					bw = new BufferedWriter(fw);
					bw.write("package " + DbConnection.basePage + ".service.impl;\r\n");
					bw.write("\r\n");
					bw.write("import org.springframework.beans.factory.annotation.Autowired;\r\n");
					bw.write("import org.springframework.transaction.annotation.Transactional;\r\n");
					bw.write("import org.springframework.http.RequestEntity;\r\n");
					bw.write("import org.springframework.stereotype.Service;\r\n");
					bw.write("import " + DbConnection.basePage + "." + "mapper." + bt + "Mapper;\r\n");
					bw.write("import " + DbConnection.basePage + "." + "service." + bt + "Service;\r\n");
					bw.write("import " + DbConnection.commndPage + ".impl.BaseServiceImpl;\r\n");
					bw.write("import " + DbConnection.basePage + "." + "entity." + bt + ";\r\n");
					bw.write("import com.yt.app.frame.page.IPage;\r\n");
					bw.write("import com.yt.app.frame.page.PageBean;\r\n");
					bw.write("import com.yt.app.util.RequestUtil;\r\n");
					bw.write("import java.util.List;\r\n");
					bw.write("import java.util.Map;\r\n");
					bw.write("\r\n");
					bw.write("/**\r\n");
					bw.write("* @author zj    default  \r\n");
					bw.write("* \r\n");
					bw.write("* @version " + DbConnection.version + "\r\n");
					bw.write("* @createdate  " + DateTimeUtil.getDateTime() + "\r\n");
					bw.write("*/\r\n");
					bw.write("\r\n");
					bw.write("@Service\r\n");
					bw.write("public class " + bt + "ServiceImpl extends BaseServiceImpl<" + bt + ", Long> implements " + bt + "Service{\r\n");
					bw.write("  @Autowired\r\n");
					bw.write("  private " + bt + "Mapper mapper;\r\n");
					bw.write("\r\n");
					bw.write("@Override\r\n");
					bw.write("@Transactional\r\n");
					bw.write("public Integer post(" + bt + " t) {\r\n");
					bw.write("	Integer i = mapper.post(t);\r\n");
					bw.write("	return i;\r\n");
					bw.write("}\r\n");
					bw.write("\r\n");
					bw.write("@SuppressWarnings(\"unchecked\")\r\n");
					bw.write("@Override\r\n");
					bw.write("public IPage<" + bt + "> list(RequestEntity<Object> requestEntity) {\r\n");
					bw.write("	Map<String, Object> param = RequestUtil.requestEntityToParamMap(requestEntity);\r\n");
					bw.write("	int count = 0;\r\n");
					bw.write("	if (PageBean.isPaging(param)) {\r\n");
					bw.write("  	count = mapper.countlist(param);\r\n");
					bw.write(" 	    if (count == 0) {\r\n");
					bw.write("			return PageBean.EMPTY_PAGE;\r\n");
					bw.write("		}\r\n");
					bw.write("	}\r\n");
					bw.write("	List<" + bt + "> list = mapper.list(param);\r\n");
					bw.write("	return new PageBean<" + bt + ">(param, list, count);\r\n");
					bw.write("}\r\n");
					bw.write("\r\n");
					bw.write("@Override\r\n");
					bw.write("public " + bt + " get(Long id) {\r\n");
					bw.write("	" + bt + " t = mapper.get(id);\r\n");
					bw.write("	return t;\r\n");
					bw.write("}\r\n");
					bw.write("}");
					bw.flush();

					file = getResource(bt);
					fw = new FileWriter(file);
					bw = new BufferedWriter(fw);
					bw.write("package " + DbConnection.basePage + ".resource;\r\n");
					bw.write("\r\n");
					bw.write("\r\n");
					bw.write("import org.springframework.hateoas.ResourceSupport;\r\n");
					bw.write("import " + DbConnection.basePage + ".controller." + bt + "Controller;\r\n");
					bw.write("import " + DbConnection.basePage + "." + "entity." + bt + ";\r\n");
					bw.write("import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;\r\n");
					bw.write("import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;\r\n");
					bw.write("\r\n");
					bw.write("\r\n");
					bw.write("\r\n");
					bw.write("/**\r\n");
					bw.write("* @author zj    default  \r\n");
					bw.write("* \r\n");
					bw.write("* @version " + DbConnection.version + "\r\n");
					bw.write("* @createdate  " + DateTimeUtil.getDateTime() + "\r\n");
					bw.write("*/\r\n");
					bw.write("\r\n");
					bw.write("public class " + bt + "Resource extends ResourceSupport {\r\n");
					bw.write("	private final " + bt + " t;\r\n");
					bw.write("	public " + bt + "Resource(" + bt + " entity) {\r\n");
					bw.write("this.t = entity;\r\n");
					bw.write("this.add(linkTo(" + bt + "Controller.class).withRel(\"post\"));\r\n");
					bw.write("this.add(linkTo(methodOn(" + bt + "Controller.class).list(null, null, null)).withRel(\"list\"));\r\n");

					bw.write("\r\n");
					bw.write("}\r\n");
					bw.write("	public " + bt + " get" + bt + "() {\r\n");
					bw.write("return t;\r\n");
					bw.write("	}\r\n");
					bw.write("\r\n");
					bw.write("\r\n");
					bw.write("}");
					bw.flush();

					file = getResourceAssembler(bt);
					fw = new FileWriter(file);
					bw = new BufferedWriter(fw);
					bw.write("package " + DbConnection.basePage + ".resource;\r\n");
					bw.write("\r\n");
					bw.write("\r\n");
					bw.write("import org.springframework.hateoas.mvc.ResourceAssemblerSupport;\r\n");
					bw.write("import " + DbConnection.basePage + ".controller." + bt + "Controller;\r\n");
					bw.write("import " + DbConnection.basePage + "." + "entity." + bt + ";\r\n");
					bw.write("\r\n");
					bw.write("\r\n");
					bw.write("\r\n");
					bw.write("/**\r\n");
					bw.write("* @author zj    default  \r\n");
					bw.write("* \r\n");
					bw.write("* @version " + DbConnection.version + "\r\n");
					bw.write("* @createdate  " + DateTimeUtil.getDateTime() + "\r\n");
					bw.write("*/\r\n");
					bw.write("\r\n");
					bw.write("public class " + bt + "ResourceAssembler extends ResourceAssemblerSupport<" + bt + ", " + bt + "Resource> {\r\n");
					bw.write("	public " + bt + "ResourceAssembler() {\r\n");
					bw.write("super(" + bt + "Controller.class, " + bt + "Resource.class);\r\n");
					bw.write("	}\r\n");
					bw.write("	@Override");
					bw.write("	public " + bt + "Resource toResource(" + bt + " t) {\r\n");
					bw.write("return createResourceWithId(t.getId(), t);\r\n");
					bw.write("	}\r\n");
					bw.write("	@Override\r\n");
					bw.write("	protected " + bt + "Resource instantiateResource(" + bt + " t) {\r\n");
					bw.write("return new " + bt + "Resource(t);\r\n");
					bw.write("	}\r\n");
					bw.write("\r\n");
					bw.write("}");
					bw.flush();

					file = getController(bt);
					fw = new FileWriter(file);
					bw = new BufferedWriter(fw);
					bw.write("package " + DbConnection.basePage + ".controller;\r\n");
					bw.write("import javax.servlet.http.HttpServletRequest;\r\n");
					bw.write("import javax.servlet.http.HttpServletResponse;\r\n");
					bw.write("import org.slf4j.Logger;\r\n");
					bw.write("import org.slf4j.LoggerFactory;\r\n");
					bw.write("import org.springframework.beans.factory.annotation.Autowired;\r\n");
					bw.write("import org.springframework.http.HttpStatus;\r\n");
					bw.write("import org.springframework.http.MediaType;\r\n");
					bw.write("import org.springframework.http.RequestEntity;\r\n");
					bw.write("import org.springframework.http.ResponseEntity;\r\n");
					bw.write("import org.springframework.web.bind.annotation.RequestMethod;\r\n");
					bw.write("import org.springframework.web.bind.annotation.RestController;\r\n");
					bw.write("import org.springframework.web.bind.annotation.RequestMapping;\r\n");
					bw.write("import com.yt.app.frame.page.IPage;\r\n");
					bw.write("import io.swagger.annotations.ApiOperation;\r\n");
					bw.write("import " + DbConnection.commndPage + ".impl.BaseControllerImpl;\r\n");
					bw.write("import " + DbConnection.basePage + ".resource." + bt + "ResourceAssembler;\r\n");
					bw.write("import " + DbConnection.basePage + "." + "service." + bt + "Service;\r\n");
					bw.write("import " + DbConnection.basePage + "." + "entity." + bt + ";\r\n");
					bw.write("\r\n");
					bw.write("/**\r\n");
					bw.write("* @author zj    default  test\r\n");
					bw.write("* \r\n");
					bw.write("* @version " + DbConnection.version + "\r\n");
					bw.write("* @createdate  " + DateTimeUtil.getDateTime() + "\r\n");
					bw.write("*/\r\n");
					bw.write("\r\n");
					bw.write("\r\n");
					bw.write("@RestController\r\n");
					bw.write("@RequestMapping(\"/" + DbConnection.osName + "/" + DbConnection.version + "/" + bt.toLowerCase() + "\")\r\n");
					bw.write("public class " + bt + "Controller extends BaseControllerImpl<" + bt + ", Long> {\r\n");
					bw.write("\r\n");
					bw.write("\r\n");
					bw.write("protected Logger logger = LoggerFactory.getLogger(this.getClass());\r\n");
					bw.write("  @Autowired\r\n");
					bw.write("  private " + bt + "Service service;\r\n");
					bw.write("\r\n");
					bw.write("\r\n");
					bw.write("\r\n");
					bw.write("	@Override\r\n");
					bw.write("	@ApiOperation(value = \"列表分页\", response = " + bt + ".class)\r\n");
					bw.write("	@RequestMapping(value = \"/\", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)\r\n");
					bw.write("	public ResponseEntity<Object> list(RequestEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {\r\n");
					bw.write("	    IPage<" + bt + "> pagebean = service.list(requestEntity);\r\n");
					bw.write("return new ResponseEntity<Object>(new " + bt
							+ "ResourceAssembler().toResources(pagebean.getPageList()), pagebean.getHeaders(), HttpStatus.OK);\r\n");
					bw.write("	}\r\n");
					bw.write("}\r\n");
					bw.write("\r\n");
					bw.write("\r\n");
					bw.write("\r\n");
					bw.write("\r\n");
					bw.write("\r\n");
					bw.write("\r\n");
					bw.write("\r\n");
					bw.write("\r\n");
					bw.write("\r\n");
					bw.write("\r\n");
					bw.flush();
				}

				// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				if (html) {
					file = getFileHtml(bt, "add");
					fw = new FileWriter(file);
					bw = new BufferedWriter(fw);
					bw.write("<!DOCTYPE html>\r\n");
					bw.write("<html ng-app=\"add\">\r\n");
					bw.write("  <head>\r\n");
					bw.write("    <title>新增</title>\r\n");
					bw.write("    <meta http-equiv=\"keywords\" content=\"keyword1,keyword2,keyword3\">\r\n");
					bw.write("    <meta http-equiv=\"description\" content=\"this is my page\">\r\n");
					bw.write("    <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\r\n");
					bw.write("    <!-- 基础样式 -->\r\n");
					bw.write("    <link rel=\"icon\" type=\"image/x-icon\" href=\"../../../resource/img/logo.ico\">\r\n");
					bw.write("    <link rel=\"stylesheet\" type=\"text/css\" href=\"../../../../resource/css/bootstrap.css\">\r\n");
					bw.write("    <link rel=\"stylesheet\" type=\"text/css\" href=\"../../../../resource/css/common.css\">\r\n");
					bw.write("    <link rel=\"stylesheet\" type=\"text/css\" href=\"../../../../resource/js/validate/css/validate.css\"/>\r\n");
					bw.write("    <!-- 基础js -->\r\n");
					bw.write("    <script type=\"text/javascript\" src=\"../../../../resource/js/angular-1.0.1.min.js\"></script>\r\n");
					bw.write("    <script type=\"text/javascript\" src=\"../../../../resource/js/jquery-1.9.1.min.js\"></script>\r\n");
					bw.write("    <script type=\"text/javascript\" src=\"../../../../resource/js/bootstrap.min.js\"></script>\r\n");
					bw.write("    <script type=\"text/javascript\" src=\"../../../../resource/js/response.js\"></script>\r\n");
					bw.write("	<!-- 表单验证 -->\r\n");
					bw.write("	<script type=\"text/javascript\" src=\"../../../../resource/js/date/js/eap.core.js\"></script>\r\n");
					bw.write("	<script type=\"text/javascript\" src=\"../../../../resource/js/validate/js/eap.tip.js\" ></script>\r\n");
					bw.write("	<script type=\"text/javascript\" src=\"../../../../resource/js/validate/js/eap.validate.js\"></script>\r\n");
					bw.write("	<script type=\"text/javascript\" src=\"../../../../resource/js/date/js/eap.lang-zh_CN.js\"></script>\r\n");
					bw.write("	<!-- datetime -->\r\n");
					bw.write("	<script type=\"text/javascript\" src=\"../../../../resource/js/bootstrap-datetimepicker.js\" charset=\"UTF-8\"></script>\r\n");
					bw.write("	<script type=\"text/javascript\" src=\"../../../../resource/js/locales/bootstrap-datetimepicker.fr.js\" charset=\"UTF-8\"></script>\r\n");
					bw.write("	<script type=\"text/javascript\" src=\"../../../../resource/js/date/js/eap.util.date.js\"></script>	\r\n");
					bw.write("	<!-- 初始化 -->\r\n");
					bw.write("	<script type=\"text/javascript\">\r\n");
					bw.write("var add = angular.module(\"add\", []);\r\n");
					bw.write("add.controller(\"controller\",function($scope, $http) {\r\n");
					bw.write("	$scope.formData = {};\r\n");
					bw.write("	//返回\r\n");
					bw.write("	$scope.onReturn=function(){\r\n");
					bw.write("history.go(-1);\r\n");
					bw.write("	}\r\n");
					bw.write("	//保存\r\n");
					bw.write("	$scope.onPreserved=function(){\r\n");
					bw.write("var isValid = $(\"#saveForm\").valid();\r\n");
					bw.write("	if(isValid){\r\n");
					bw.write("    $http({\r\n");
					bw.write("    	url:parent.getMenuLinksHref(\"" + tb.toLowerCase() + "post\"),\r\n");
					bw.write("    	method:'POST',\r\n");
					bw.write("    	dataType : \"json\",\r\n");
					bw.write("    	withCredentials: true,\r\n");
					bw.write("    	headers: {'Content-Type': 'application/json;charset=UTF-8;'},\r\n");
					bw.write("    	data: JSON.stringify($scope.formData)\r\n");
					bw.write("      }).success(function(data, status, headers, config){\r\n");
					bw.write("      	if(data == 1){\r\n");
					bw.write("      location.href=\"list.html\";\r\n");
					bw.write("      	}\r\n");
					bw.write("      }).error(function(data, status, headers, config){\r\n");
					bw.write("      	response(status);\r\n");
					bw.write("	})\r\n");
					bw.write("    	}\r\n");
					bw.write("	}\r\n");
					bw.write("	$scope.initselect = function() {\r\n");
					bw.write("$http({\r\n");
					bw.write("	url : parent.getMenuLinksHref(\"dictionarylist\"),\r\n");
					bw.write("	method : 'POST',\r\n");
					bw.write("	dataType : \"json\",\r\n");
					bw.write("	withCredentials : true,\r\n");
					bw.write("	headers : {\r\n");
					bw.write("'Content-Type' : 'application/json;charset=UTF-8;'\r\n");
					bw.write("	},\r\n");
					bw.write("	data : JSON.stringify([20])\r\n");
					bw.write("	}).success(function(data, status, headers, config) {\r\n");
					bw.write("$scope.selects = data;\r\n");
					bw.write("	}).error(function(data, status, headers, config) {\r\n");
					bw.write("response(status);\r\n");
					bw.write("	})\r\n");
					bw.write("}\r\n");
					bw.write("	$scope.initselect();\r\n");
					bw.write("})\r\n");
					bw.write("	</script>\r\n");
					bw.write("</head>\r\n");
					bw.write("  \r\n");
					bw.write("  <body ng-controller=\"controller\">\r\n");
					bw.write("  	<form method=\"post\" id=\"saveForm\" action=\"\">\r\n");
					bw.write("   <!-- 操作按钮操作区 -->\r\n");
					bw.write("   <div class=\"col-lg-12 topActionPanel\">\r\n");
					bw.write("	        <div>\r\n");
					bw.write("    	<button type=\"button\" class=\"btn btn-primary\" ng-click=\"onPreserved()\">确 定</button>\r\n");
					bw.write("	    	</div>\r\n");
					bw.write("	    	<div>\r\n");
					bw.write("    	<button type=\"button\" class=\"btn btn-primary\" ng-click=\"onReturn()\">返 回</button>\r\n");
					bw.write("	    	</div>\r\n");
					bw.write("    </div>\r\n");
					bw.write("   <div class=\"col-lg-6\">\r\n");
					for (int i = 0; i < r.length; i++) {
						if (ts[i][1].toString().equals("id") || ts[i][1].toString().equals("version"))
							continue;
						if (ts[i][1].toString().indexOf("id") != -1) {
							bw.write("	    	<div class=\"input-group input-group-sm\">\r\n");
							bw.write("	    <span class=\"input-group-addon text-right spanwidth\">" + ts[i][2] + "</span>\r\n");
							bw.write("	    <select class=\"form-control required\" name=\"" + ts[i][1] + "\" ng-model=\"formData." + ts[i][1]
									+ "\" ng-options=\"data.code as data.name for data in selects  | filter: {typecode: '20'}\">\r\n");
							bw.write("	    	<option value=''>请选择" + ts[i][2] + "</option>\r\n");
							bw.write("	    	</select>\r\n");
							bw.write("	    	<span class=\"input-group-addon verifyprompt\">*</span>\r\n");
							bw.write("	    	</div>\r\n");
						} else if (ts[i][1].toString().indexOf("date") != -1) {
							columnname = ts[i][1];
							bw.write("<div class=\"input-group input-group-sm\" id=\"" + ts[i][1] + "hidden\">\r\n");
							bw.write("	<span class=\"input-group-addon text-right spanwidth\">" + ts[i][2] + "</span>\r\n");
							bw.write("	  <div class=\"input-group date form_datetime\" id=\"" + ts[i][1]
									+ "date\" style=\"width:400px;\" data-date-format=\"dd MM yyyy - HH:ii p\" data-link-field=\"dtp_input\">\r\n");
							bw.write("        <input class=\"form-control required\" id=\"" + ts[i][1] + "\" ng-model=\"formData." + ts[i][1]
									+ "\" size=\"14\" type=\"text\" value=\"\" readonly>\r\n");
							bw.write("  <span class=\"input-group-addon\"><span class=\"glyphicon glyphicon-th\"></span></span>\r\n");
							bw.write("    </div>\r\n");
							bw.write("	<input type=\"hidden\" id=\"dtp_input\" value=\"\" />\r\n");
							bw.write("</div>\r\n");
						} else {
							bw.write("	    	<div class=\"input-group input-group-sm\">\r\n");
							bw.write("	            	<span class=\"input-group-addon text-right spanwidth\">" + ts[i][2] + "</span>\r\n");
							bw.write("	           <input type=\"text\" class=\"form-control required\" placeholder=\"请输入" + ts[i][2] + "\" name=\""
									+ ts[i][1] + "\" ng-model=\"formData." + ts[i][1] + "\" maxlength=\"" + ts[i][3] + "\">\r\n");
							bw.write("	        <span class=\"input-group-addon verifyprompt\">*</span>\r\n");
							bw.write("	        </div>\r\n");
						}
					}
					bw.write("	        </div>\r\n");
					bw.write("    </form>\r\n");
					bw.write("  </body>\r\n");
					bw.write("  <script type=\"text/javascript\">\r\n");
					bw.write("  $('#" + columnname + "date').datetimepicker({\r\n");
					bw.write("  	format : 'yyyy-mm-dd',\r\n");
					bw.write("  	language : 'cn',\r\n");
					bw.write("  	weekStart : 1,\r\n");
					bw.write("  	minView : \"month\",\r\n");
					bw.write("  	todayBtn : 1,\r\n");
					bw.write("  	initialDate:new Date(),\r\n");
					bw.write("  	autoclose : 1,\r\n");
					bw.write("  	todayHighlight : 1,\r\n");
					bw.write("  	startView : 2,\r\n");
					bw.write("  	forceParse : 0,\r\n");
					bw.write("  	showMeridian : 0\r\n");
					bw.write("  });\r\n");
					bw.write("</html>\r\n");
					bw.flush();

					file = getFileHtml(bt, "info");
					fw = new FileWriter(file);
					bw = new BufferedWriter(fw);
					bw.write("<!DOCTYPE html>\r\n");
					bw.write("<html ng-app=\"info\">\r\n");
					bw.write("  <head>\r\n");
					bw.write("    <title>查看</title>\r\n");
					bw.write("    <meta http-equiv=\"keywords\" content=\"keyword1,keyword2,keyword3\">\r\n");
					bw.write("    <meta http-equiv=\"description\" content=\"this is my page\">\r\n");
					bw.write("    <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\r\n");
					bw.write("    <!-- 基础样式 -->\r\n");
					bw.write("    <link rel=\"icon\" type=\"image/x-icon\" href=\"../../../resource/img/logo.ico\">\r\n");
					bw.write("    <link rel=\"stylesheet\" type=\"text/css\" href=\"../../../../resource/css/bootstrap.css\">\r\n");
					bw.write("    <link rel=\"stylesheet\" type=\"text/css\" href=\"../../../../resource/css/common.css\">\r\n");
					bw.write("    <!-- 基础js -->\r\n");
					bw.write("    <script type=\"text/javascript\" src=\"../../../../resource/js/angular-1.0.1.min.js\"></script>\r\n");
					bw.write("    <script type=\"text/javascript\" src=\"../../../../resource/js/jquery-1.9.1.min.js\"></script>\r\n");
					bw.write("    <script type=\"text/javascript\" src=\"../../../../resource/js/response.js\"></script>\r\n");
					bw.write("    <!-- 初始化 -->\r\n");
					bw.write("    <script type=\"text/javascript\">\r\n");
					bw.write("var info = angular.module(\"info\", []);\r\n");
					bw.write("info.controller(\"controller\",function($scope, $http) {\r\n");
					bw.write("	//返回\r\n");
					bw.write("	$scope.onReturn=function(){\r\n");
					bw.write("history.go(-1);\r\n");
					bw.write("	}\r\n");
					bw.write("	$http({\r\n");
					bw.write("url:parent.getFunctionLinksHref(\"self\"),\r\n");
					bw.write("method:'GET',\r\n");
					bw.write("dataType : \"json\",\r\n");
					bw.write("withCredentials: true,\r\n");
					bw.write("headers: {'Content-Type': 'application/json;charset=UTF-8;'},\r\n");
					bw.write("data: ''\r\n");
					bw.write("}).success(function(data, status, headers, config){\r\n");
					bw.write("	$scope.formData= data;\r\n");
					bw.write("}).error(function(data, status, headers, config){\r\n");
					bw.write("	response(status);\r\n");
					bw.write("});\r\n");
					bw.write("})\r\n");
					bw.write("	</script>\r\n");
					bw.write("  </head>\r\n");
					bw.write("  <body ng-controller=\"controller\">\r\n");
					bw.write("   <div class=\"col-lg-12 topActionPanel\">\r\n");
					bw.write("	        <div>\r\n");
					bw.write("	    	<button type=\"button\" class=\"btn btn-primary\" ng-click=\"onReturn()\">返 回</button>\r\n");
					bw.write("    </div>\r\n");
					bw.write("    	</div>\r\n");
					bw.write("   <div class=\"col-lg-6 viewFixedWidth\">\r\n");
					for (int i = 0; i < r.length; i++) {
						if (ts[i][1].toString().equals("id") || ts[i][1].toString().equals("version"))
							continue;
						bw.write("    <div class=\"input-group input-group-sm\">\r\n");
						bw.write("             <span class=\"input-group-addon text-right spanwidth\">" + ts[i][2] + "</span>\r\n");
						bw.write("           	<input type=\"text\" class=\"form-control\" value=\"{{formData." + ts[i][1] + "}}\" disabled>\r\n");
						bw.write("           </div>\r\n");
					}
					bw.write("   	</div>\r\n");
					bw.write("  </body>\r\n");
					bw.write("</html>\r\n");
					bw.flush();

					file = getFileHtml(bt, "list");
					fw = new FileWriter(file);
					bw = new BufferedWriter(fw);
					bw.write("<!DOCTYPE html>\r\n");
					bw.write("<html ng-app=\"list\">\r\n");
					bw.write("  <head>\r\n");
					bw.write("    <title>列表</title>\r\n");
					bw.write("    <meta http-equiv=\"keywords\" content=\"keyword1,keyword2,keyword3\">\r\n");
					bw.write("    <meta http-equiv=\"description\" content=\"this is my page\">\r\n");
					bw.write("    <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\r\n");
					bw.write("    <!-- 基础样式 -->\r\n");
					bw.write("    <link rel=\"icon\" type=\"image/x-icon\" href=\"../../../resource/img/logo.ico\">\r\n");
					bw.write("    <link rel=\"stylesheet\" type=\"text/css\" href=\"../../../../resource/css/bootstrap.css\">\r\n");
					bw.write("    <link rel=\"stylesheet\" type=\"text/css\" href=\"../../../../resource/css/list.css\">\r\n");
					bw.write("    <script type=\"text/javascript\" src=\"../../../../resource/js/jquery-1.9.1.min.js\"></script>\r\n");
					bw.write("    <script type=\"text/javascript\" src=\"../../../../resource/js/angular-1.0.1.min.js\"></script>\r\n");
					bw.write("   <!-- 弹出框 -->\r\n");
					bw.write("	<script type=\"text/javascript\" src=\"../../../../resource/js/artDialog/skins/js/artDialog.source.js\"></script>\r\n");
					bw.write("    <script type=\"text/javascript\" src=\"../../../../resource/js/artDialog/skins/js/iframeTools.source.js\"></script>\r\n");
					bw.write("	<script type=\"text/javascript\" src=\"../../../../resource/js/artDialog/skins/js/artDialogZJ.js\"></script>\r\n");
					bw.write("    <script type=\"text/javascript\" src=\"../../../../resource/js/getUrlParam.js\"></script>\r\n");
					bw.write("    <!-- 分页 -->\r\n");
					bw.write("	<script type=\"text/javascript\" src=\"../../../../resource/js/paging.js\"></script>\r\n");
					bw.write("	<script type=\"text/javascript\" src=\"../../../../resource/js/response.js\"></script>\r\n");
					bw.write("    <script type=\"text/javascript\">\r\n");
					bw.write("var flag = true;\r\n");
					bw.write("var list = angular.module(\"list\", []);\r\n");
					bw.write("list.controller(\"controller\",function($scope, $http) {	\r\n");
					bw.write("	$scope.toInfo=function(data){\r\n");
					bw.write("parent.setFunctionLinksHref(data.links==null?data._links:data.links);\r\n");
					bw.write("location.href=\"info.html\";\r\n");
					bw.write("	}\r\n");
					bw.write("	$scope.toUpdate=function(data){\r\n");
					bw.write("parent.setFunctionLinksHref(data.links==null?data._links:data.links);\r\n");
					bw.write("location.href=\"update.html\";\r\n");
					bw.write("	}\r\n");
					bw.write("	$scope.toDelete=function(data){	\r\n");
					bw.write("parent.setFunctionLinksHref(data.links==null?data._links:data.links);\r\n");
					bw.write("parent.onPopupDelete();\r\n");
					bw.write("	}\r\n");
					bw.write("	$scope.onSearch=function(){\r\n");
					bw.write("$scope.pageList();\r\n");
					bw.write("	}\r\n");
					bw.write("	$scope.changePage=function(){\r\n");
					bw.write("$scope.pageList();\r\n");
					bw.write("	}\r\n");
					bw.write("	$scope.onAdd=function(){\r\n");
					bw.write("location.href=\"add.html\";\r\n");
					bw.write("	}\r\n");
					bw.write("	$scope.pageList = function(){\r\n");
					bw.write("pageNo = parent.currentPage;\r\n");
					bw.write("	    var data = {\"name\":$(\"#name\").val()};\r\n");
					bw.write("$http({\r\n");
					bw.write("	url:parent.getMenuLinksHref(\"" + tb.toLowerCase() + "list\"),\r\n");
					bw.write("	method:'POST',\r\n");
					bw.write("	dataType : \"json\",\r\n");
					bw.write("	withCredentials: true,\r\n");
					bw.write("	headers: {'Content-Type': 'application/json;charset=UTF-8;',\"pageNo\":pageNo,\"pageSize\":pageSize,\"orderby\":\"id\",\"dir\":\"desc\"},\r\n");
					bw.write("	data: JSON.stringify(data)\r\n");
					bw.write("	}).success(function(data, status, headers, config){\r\n");
					bw.write("totalCount = headers('totalCount');\r\n");
					bw.write("pageCount = headers('pageCount');\r\n");
					bw.write("$scope.datalist= data;\r\n");
					bw.write("paging();\r\n");
					bw.write("initPage();\r\n");
					bw.write("	}).error(function(data, status, headers, config){\r\n");
					bw.write("response(status);\r\n");
					bw.write("	});\r\n");
					bw.write("	}\r\n");
					bw.write("	$scope.pageList();\r\n");
					bw.write("	$scope.onDelete = function(){\r\n");
					bw.write("                $http({\r\n");
					bw.write("	url:parent.getFunctionLinksHref(\"self\"),\r\n");
					bw.write("	method:'DELETE',\r\n");
					bw.write("	dataType : \"json\",\r\n");
					bw.write("	withCredentials: true,\r\n");
					bw.write("	headers: {'Content-Type': 'application/json;charset=UTF-8;'},\r\n");
					bw.write("	data: \"\"\r\n");
					bw.write("	}).success(function(data, status, headers, config){\r\n");
					bw.write("$scope.pageList();\r\n");
					bw.write("	}).error(function(data, status, headers, config){\r\n");
					bw.write("response(status);\r\n");
					bw.write("	});\r\n");
					bw.write("}\r\n");
					bw.write("})\r\n");
					bw.write("\r\n");
					bw.write("function confirmDeletion(){\r\n");
					bw.write("	var appElement = document.querySelector('[ng-controller=controller]');\r\n");
					bw.write("	var $scope = angular.element(appElement).scope();\r\n");
					bw.write("	$scope.onDelete();\r\n");
					bw.write("}\r\n");
					bw.write("	</script>\r\n");
					bw.write("  </head> \r\n");
					bw.write("  <body ng-controller=\"controller\" style=\"color: #000;\">\r\n");
					bw.write("  	<!-- 基本操作区 -->\r\n");
					bw.write("  	<h5>\r\n");
					bw.write("  <span class=\"glyphicon glyphicon-search\"  style=\"margin:0px 5px;color:#99DFF8;\" ></span>\r\n");
					bw.write("  <input type=\"text\" id=\"name\" class=\"form-control\" value=\"\" placeholder=\"请输入需要查找的名称\" style=\"width: 20%;display: inline;\">\r\n");
					bw.write("  <i class=\"glyphicon glyphicon-remove\" onclick=\"removeSearch()\"></i>\r\n");
					bw.write("  <a class=\"btn btn-primary\" style=\"margin: 0px auto;width: 80px;\"  href=\"#\" role=\"button\" ng-click=\"onSearch()\">搜索</a>\r\n");
					bw.write("  <a class=\"btn btn-primary\" style=\"margin: 0px auto;width: 80px;\"  href=\"#\" role=\"button\" ng-click=\"onAdd()\">添加</a>\r\n");
					bw.write("  	</h5>\r\n");
					bw.write("  	<!-- 列表区 -->\r\n");
					bw.write("  	<table class=\"table\">\r\n");
					bw.write("	  <thead>\r\n");
					bw.write("	    <tr class=\"info\">\r\n");
					bw.write("	      <th>序号</th>\r\n");
					for (int i = 0; i < r.length; i++) {
						if (ts[i][1].toString().equals("id") || ts[i][1].toString().equals("version"))
							continue;
						bw.write("	      <th>" + ts[i][2] + "</th>\r\n");
					}
					bw.write("	      <th>相关操作</th>\r\n");
					bw.write("	    </tr>\r\n");
					bw.write("	  </thead>\r\n");
					bw.write("	  <tbody>\r\n");
					bw.write("	    <tr ng-repeat=\"data in datalist\"  class='tr{{$index%2+2}}'  ng-class=\"{selected: $index==selectedRow}\">\r\n");
					bw.write("    <td ng-dblclick=\"toInfo(data)\">{{$index+1}}</td>\r\n");
					for (int i = 0; i < r.length; i++) {
						if (ts[i][1].toString().equals("id") || ts[i][1].toString().equals("version"))
							continue;
						bw.write("	<td ng-dblclick=\"toInfo(data)\">{{data." + tn + "." + ts[i][1] + "}}</td>\r\n");
					}
					bw.write("	<td>\r\n");
					bw.write("	    <h4 ng-click=\"toInfo(data)\" class=\"glyphicon glyphicon-search\"  style=\"margin:0px 5px;color:#99DFF8;\" title=\"查看\"></h4>\r\n");
					bw.write("	    <h4 ng-click=\"toUpdate(data)\" class=\"glyphicon glyphicon-pencil\"  style=\"margin:0px 5px;color:#99DFF8;\" title=\"修改\"></h4>\r\n");
					bw.write("	    <h4 ng-click=\"toDelete(data)\" class=\"glyphicon glyphicon-remove\"  style=\"margin:0px 5px;color:#99DFF8;\" title=\"删除\"></h4>\r\n");
					bw.write("	</td>\r\n");
					bw.write("	    </tr>\r\n");
					bw.write("	  </tbody>\r\n");
					bw.write("	</table>\r\n");
					bw.write(" 	<!-- 分页区 -->\r\n");
					bw.write("	<div class=\"pagepanel\">\r\n");
					bw.write("<span class=\"text-info\" style=\"position:relative;top:-10px;\">共<label id=\"totalCount\"></label>条</span>\r\n");
					bw.write("<ul class=\"pagination\" style=\"margin: 0px 10px;padding:0px;\">\r\n");
					bw.write("</ul>\r\n");
					bw.write("<span class=\"text-info\" style=\"position:relative;top:-10px;\">第<label id=\"pageNo\"></label>页/共<label id=\"pageCount\"></label>页</span>\r\n");
					bw.write("<select id=\"pageSize\" class=\"form-control\" style=\"width: 70px;float: right;\">\r\n");
					bw.write("</select>\r\n");
					bw.write("<span class=\"text-info\" style=\"position:relative;top:6px;float: right;margin: 0px 5px;\">每页显示</span>\r\n");
					bw.write("	</div>\r\n");
					bw.write("  </body>\r\n");
					bw.write("</html>\r\n");
					bw.flush();

					file = getFileHtml(bt, "update");
					fw = new FileWriter(file);
					bw = new BufferedWriter(fw);
					bw.write("<!DOCTYPE html>\r\n");
					bw.write("<html ng-app=\"add\">\r\n");
					bw.write("  <head>\r\n");
					bw.write("    <title>修改</title>\r\n");
					bw.write("    <meta http-equiv=\"keywords\" content=\"keyword1,keyword2,keyword3\">\r\n");
					bw.write("    <meta http-equiv=\"description\" content=\"this is my page\">\r\n");
					bw.write("    <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\r\n");
					bw.write("    <link rel=\"icon\" type=\"image/x-icon\" href=\"../../../resource/img/logo.ico\">\r\n");
					bw.write("    <link rel=\"stylesheet\" type=\"text/css\" href=\"../../../../resource/css/bootstrap.css\">\r\n");
					bw.write("    <link rel=\"stylesheet\" type=\"text/css\" href=\"../../../../resource/css/common.css\">\r\n");
					bw.write("    <link rel=\"stylesheet\" type=\"text/css\" href=\"../../../../resource/js/validate/css/validate.css\"/>\r\n");
					bw.write("    <!-- 基础js -->\r\n");
					bw.write("    <script type=\"text/javascript\" src=\"../../../../resource/js/angular-1.0.1.min.js\"></script>\r\n");
					bw.write("    <script type=\"text/javascript\" src=\"../../../../resource/js/jquery-1.9.1.min.js\"></script>\r\n");
					bw.write("    <script type=\"text/javascript\" src=\"../../../../resource/js/bootstrap.min.js\"></script>\r\n");
					bw.write("    <script type=\"text/javascript\" src=\"../../../../resource/js/response.js\"></script>\r\n");
					bw.write("	<!-- 表单验证 -->\r\n");
					bw.write("	<script src=\"../../../../resource/js/date/js/eap.core.js\"></script>\r\n");
					bw.write("	<script type=\"text/javascript\" src=\"../../../../resource/js/validate/js/eap.tip.js\" ></script>\r\n");
					bw.write("	<script type=\"text/javascript\" src=\"../../../../resource/js/validate/js/eap.validate.js\"></script>\r\n");
					bw.write("	<script type=\"text/javascript\" src=\"../../../../resource/js/date/js/eap.lang-zh_CN.js\"></script>\r\n");
					bw.write("	<!-- datetime -->\r\n");
					bw.write("	<script type=\"text/javascript\" src=\"../../../../resource/js/bootstrap-datetimepicker.js\" charset=\"UTF-8\"></script>\r\n");
					bw.write("	<script type=\"text/javascript\" src=\"../../../../resource/js/locales/bootstrap-datetimepicker.fr.js\" charset=\"UTF-8\"></script>\r\n");
					bw.write("	<script type=\"text/javascript\" src=\"../../../../resource/js/date/js/eap.util.date.js\"></script>	\r\n");
					bw.write("	<!-- 初始化 -->\r\n");
					bw.write("	<script type=\"text/javascript\">\r\n");
					bw.write("var add = angular.module(\"add\", []);\r\n");
					bw.write("add.controller(\"controller\",function($scope, $http) {\r\n");
					bw.write("	$scope.formData = {};\r\n");
					bw.write("	$http({\r\n");
					bw.write("url:parent.getFunctionLinksHref(\"self\"),\r\n");
					bw.write("method:'GET',\r\n");
					bw.write("dataType : \"json\",\r\n");
					bw.write("withCredentials: true,\r\n");
					bw.write("headers: {'Content-Type': 'application/json;charset=UTF-8;'},\r\n");
					bw.write("data: ''\r\n");
					bw.write("}).success(function(data, status, headers, config){\r\n");
					bw.write("	$scope.formData= data\r\n");
					bw.write("}).error(function(data, status, headers, config){\r\n");
					bw.write("	response(status);\r\n");
					bw.write("});\r\n");
					bw.write("	$scope.onReturn=function(){\r\n");
					bw.write("history.go(-1);\r\n");
					bw.write("	}\r\n");
					bw.write("	$scope.onPreserved=function(){\r\n");
					bw.write("var isValid = $(\"#saveForm\").valid();\r\n");
					bw.write("	if(isValid){\r\n");
					bw.write("    $http({\r\n");
					bw.write("    	url:parent.getFunctionLinksHref(\"self\"),\r\n");
					bw.write("    	method:'PUT',\r\n");
					bw.write("    	dataType : \"json\",\r\n");
					bw.write("    	withCredentials: true,\r\n");
					bw.write("    	headers: {'Content-Type': 'application/json;charset=UTF-8;'},\r\n");
					bw.write("    	data: JSON.stringify($scope.formData)\r\n");
					bw.write("      }).success(function(data, status, headers, config){\r\n");
					bw.write("      	location.href=\"list.html\";\r\n");
					bw.write("      }).error(function(data, status, headers, config){\r\n");
					bw.write("      	response(status);\r\n");
					bw.write("	})\r\n");
					bw.write("    	}\r\n");
					bw.write("	}\r\n");
					bw.write("	$scope.initselect = function() {\r\n");
					bw.write("$http({\r\n");
					bw.write("	url : parent.getMenuLinksHref(\"dictionarylist\"),\r\n");
					bw.write("	method : 'POST',\r\n");
					bw.write("	dataType : \"json\",\r\n");
					bw.write("	withCredentials : true,\r\n");
					bw.write("	headers : {\r\n");
					bw.write("'Content-Type' : 'application/json;charset=UTF-8;'\r\n");
					bw.write("	},\r\n");
					bw.write("	data : JSON.stringify([20])\r\n");
					bw.write("	}).success(function(data, status, headers, config) {\r\n");
					bw.write("$scope.selects = data;\r\n");
					bw.write("	}).error(function(data, status, headers, config) {\r\n");
					bw.write("response(status);\r\n");
					bw.write("	})\r\n");
					bw.write("}\r\n");
					bw.write("	$scope.initselect();\r\n");
					bw.write("})\r\n");
					bw.write("	</script>\r\n");
					bw.write("</head>\r\n");
					bw.write("  <body ng-controller=\"controller\">\r\n");
					bw.write("  	<form method=\"post\" id=\"saveForm\" action=\"\">\r\n");
					bw.write("   <!-- 操作按钮操作区 -->\r\n");
					bw.write("   <div class=\"col-lg-12 topActionPanel\">\r\n");
					bw.write("	        <div>\r\n");
					bw.write("    	<button type=\"button\" class=\"btn btn-primary\" ng-click=\"onPreserved()\">确 定</button>\r\n");
					bw.write("	    	</div>\r\n");
					bw.write("	    	<div>\r\n");
					bw.write("    	<button type=\"button\" class=\"btn btn-primary\" ng-click=\"onReturn()\">返 回</button>\r\n");
					bw.write("	    	</div>\r\n");
					bw.write("    	</div>\r\n");
					bw.write("   <div class=\"col-lg-6\">\r\n");
					for (int i = 0; i < r.length; i++) {
						if (ts[i][1].toString().equals("id") || ts[i][1].toString().equals("version"))
							continue;
						if (ts[i][1].toString().indexOf("id") != -1) {
							bw.write("	    	<div class=\"input-group input-group-sm\">\r\n");
							bw.write("	    <span class=\"input-group-addon text-right spanwidth\">" + ts[i][2] + "</span>\r\n");
							bw.write("	    <select class=\"form-control required\" name=\"" + ts[i][1] + "\" ng-model=\"formData." + ts[i][1]
									+ "\" ng-options=\"data.code as data.name for data in selects  | filter: {typecode: '20'}\">\r\n");
							bw.write("	    	<option value=''>请选择" + ts[i][2] + "</option>\r\n");
							bw.write("	    	</select>\r\n");
							bw.write("	    	<span class=\"input-group-addon verifyprompt\">*</span>\r\n");
							bw.write("	    	</div>\r\n");
						} else if (ts[i][1].toString().indexOf("date") != -1) {
							columnname = ts[i][1];
							bw.write("<div class=\"input-group input-group-sm\" id=\"" + ts[i][1] + "hidden\">\r\n");
							bw.write("	<span class=\"input-group-addon text-right spanwidth\">" + ts[i][2] + "</span>\r\n");
							bw.write("	  <div class=\"input-group date form_datetime\" id=\"" + ts[i][1]
									+ "date\" style=\"width:400px;\" data-date-format=\"dd MM yyyy - HH:ii p\" data-link-field=\"dtp_input\">\r\n");
							bw.write("        <input class=\"form-control required\" id=\"" + ts[i][1] + "\" ng-model=\"formData." + ts[i][1]
									+ "\" size=\"14\" type=\"text\" value=\"\" readonly>\r\n");
							bw.write("  <span class=\"input-group-addon\"><span class=\"glyphicon glyphicon-th\"></span></span>\r\n");
							bw.write("    </div>\r\n");
							bw.write("	<input type=\"hidden\" id=\"dtp_input\" value=\"\" />\r\n");
							bw.write("</div>\r\n");
						} else {
							bw.write("	    	<div class=\"input-group input-group-sm\">\r\n");
							bw.write("	            	<span class=\"input-group-addon text-right spanwidth\">" + ts[i][2] + "</span>\r\n");
							bw.write("	           <input type=\"text\" class=\"form-control required\" placeholder=\"请输入" + ts[i][2] + "\" name=\""
									+ ts[i][1] + "\" ng-model=\"formData." + ts[i][1] + "\" maxlength=\"" + ts[i][3] + "\">\r\n");
							bw.write("	        <span class=\"input-group-addon verifyprompt\">*</span>\r\n");
							bw.write("	        </div>\r\n");
						}
					}
					bw.write("   </div>\r\n");
					bw.write("    </form>\r\n");
					bw.write("  </body>\r\n");
					bw.write("  <script type=\"text/javascript\">\r\n");
					bw.write("  $('#" + columnname + "date').datetimepicker({\r\n");
					bw.write("  	format : 'yyyy-mm-dd',\r\n");
					bw.write("  	language : 'cn',\r\n");
					bw.write("  	weekStart : 1,\r\n");
					bw.write("  	minView : \"month\",\r\n");
					bw.write("  	todayBtn : 1,\r\n");
					bw.write("  	initialDate:new Date(),\r\n");
					bw.write("  	autoclose : 1,\r\n");
					bw.write("  	todayHighlight : 1,\r\n");
					bw.write("  	startView : 2,\r\n");
					bw.write("  	forceParse : 0,\r\n");
					bw.write("  	showMeridian : 0\r\n");
					bw.write("  });\r\n");
					bw.write("  </script>\r\n");
					bw.write("</html>\r\n");
					bw.flush();
					bw.close();
				}

				// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				if (data) {
					String comment = "";
					Long lid1 = gid.nextId();
					Long lid2 = gid.nextId();
					Long lid3 = gid.nextId();
					Long lid4 = gid.nextId();
					Long lid5 = gid.nextId();
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
