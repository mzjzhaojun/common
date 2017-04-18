package com.yt.app.test;

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

public class TestCode {
	private static TestCode ac;

	private TestCode() {
		init();
	}

	public static TestCode getInstance() {
		if (ac == null) {
			ac = new TestCode();
		}
		return ac;
	}

	public File file;
	public TestConnection sqlcon = TestConnection.getInstance();
	private String FILE_PATH_NAME = "/config/mysql.properties";

	private void init() {
		try {
			File directory = new File("");
			String courseFile = directory.getCanonicalPath();
			String filePaths = TestConnection.class.getResource(FILE_PATH_NAME).getPath();
			File file = new File(filePaths);
			InputStream in = new FileInputStream(file);
			Properties props = new Properties();
			props.load(in);
			in.close();
			TestConnection.basePage = "com.yt.app.api." + TestConnection.version;
			TestConnection.commndPage = "com.yt.app.common.base";
			TestConnection.filePath = courseFile + "/src/main/java/com/yt/app/api/" + TestConnection.version;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createNewFile(String table) {
		file = new File(TestConnection.filePath + "/mapper/impl");
		file.mkdirs();
		file = new File(TestConnection.filePath + "/service/impl");
		file.mkdirs();
		file = new File(TestConnection.filePath + "/controller");
		file.mkdirs();
		file = new File(TestConnection.filePath + "/entity");
		file.mkdirs();
		file = new File(TestConnection.filePath + "/mapper");
		file.mkdirs();
		file = new File(TestConnection.filePath + "/resource");
		file.mkdirs();
	}

	public File getFileDao(String table) throws Exception {
		file = new File(TestConnection.filePath + "/mapper/" + table + "Mapper.java");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		return file;
	}

	public File getService(String table) throws Exception {
		file = new File(TestConnection.filePath + "/service/" + table + "Service.java");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		return file;
	}

	public File getServiceImpl(String table) throws Exception {
		file = new File(TestConnection.filePath + "/service/impl/" + table + "ServiceImpl.java");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		return file;
	}

	public File getController(String table) throws Exception {
		file = new File(TestConnection.filePath + "/controller/" + table + "Controller.java");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		return file;
	}

	public File getEntity(String table) throws Exception {
		file = new File(TestConnection.filePath + "/entity/" + table + ".java");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		return file;
	}

	public File getMapper(String table) throws Exception {
		file = new File(TestConnection.filePath + "/mapper/impl/" + table + "Mapper.xml");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		return file;
	}

	public File getResource(String table) throws Exception {
		file = new File(TestConnection.filePath + "/resource/" + table + "Resource.java");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		return file;
	}

	public File getResourceAssembler(String table) throws Exception {
		file = new File(TestConnection.filePath + "/resource/" + table + "ResourceAssembler.java");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		return file;
	}

	public List<Object> getTableMySql() {
		List<Object> list = new ArrayList<Object>();
		try {
			Connection conn = sqlcon.getCon();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = null;
			String table_name = "TABLE_NAME";
			if (sqlcon.getType().equals("sqlServer")) {
				table_name = "Name";
				rs = stmt.executeQuery("SELECT " + table_name + " FROM SysObjects Where XType='U'");
			} else {
				table_name = "TABLE_NAME";
				rs = stmt
						.executeQuery("SELECT " + table_name + " FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '" + TestConnection.dbName + "'");
			}
			while (rs.next()) {
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("name", rs.getString(table_name));
				list.add(hm);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void printFileMySql(String tableNames) {
		System.out.println("生成表：{" + tableNames + "}");
		try {
			Connection conn = sqlcon.getCon();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = null;
			String[] tss = tableNames.split(",");
			if (tss == null || TestConnection.filePath == null || TestConnection.filePath == "")
				return;
			List<String[]> db2java = new ArrayList<String[]>();
			db2java.add(new String[] { "bit", "Boolean" });
			db2java.add(new String[] { "varchar", "String" });
			db2java.add(new String[] { "numeric", "Double" });
			db2java.add(new String[] { "double", "Double" });
			db2java.add(new String[] { "int", "Integer" });
			// db2java.add(new String[] { "datetime", "java.util.Date" });
			// db2java.add(new String[] { "date", "java.util.Date" });
			db2java.add(new String[] { "datetime", "String" });
			db2java.add(new String[] { "date", "String" });
			db2java.add(new String[] { "tinyint", "Integer" });
			db2java.add(new String[] { "smallint", "Integer" });
			db2java.add(new String[] { "bigint", "Long" });
			db2java.add(new String[] { "char", "String" });
			db2java.add(new String[] { "float", "float" });
			for (int ti = 0; ti < tss.length; ti++) {
				String tb = tss[ti];
				String tn = "";
				if (tb.indexOf("_") != -1) {
					String tns = tb.substring(tb.indexOf("_") + 1).toLowerCase();
					tn = tb.substring(0, tb.indexOf("_")) + tns.substring(0, 1).toUpperCase() + tns.substring(1);
					;
				} else {
					tn = tb;
				}
				createNewFile(tn);
				HashMap[] r = (HashMap[]) null;
				int iRowNum;
				r = (HashMap[]) null;
				iRowNum = 0;
				int iColCnt = 0;
				if (sqlcon.getType().equals("sqlServer")) {
					rs = stmt.executeQuery("select column_name,data_type from information_schema.columns where table_name = '" + tb + "'");
				} else {
					rs = stmt.executeQuery("select column_name,data_type from information_schema.columns where table_schema = '"
							+ TestConnection.dbName + "' and table_name='" + tb + "'");
				}
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
				String bt = tn.substring(0, 1).toUpperCase() + tn.substring(1);
				file = getEntity(bt);
				FileWriter fw = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("package " + TestConnection.basePage + ".entity;\r\n\n");
				bw.write("import lombok.Getter;\r\n");
				bw.write("import lombok.Setter;\r\n");
				bw.write("import java.io.Serializable;\r\n");
				bw.write("/**\r\n");
				bw.write("* @author zj    default  \r\n");
				bw.write("* \r\n");
				bw.write("* @version " + TestConnection.version + "\r\n");
				bw.write("* @createdate  " + DateTimeUtil.getDateTime() + "\r\n");
				bw.write("*/\r\n");
				bw.write("@Getter\r\n");
				bw.write("@Setter\r\n");
				bw.write("public class " + bt + " implements Serializable{\r\n");
				bw.write("\r\n");
				bw.write("  private static final long serialVersionUID=1L;\r\n");
				bw.write("\r\n");
				String[][] ts = new String[r.length][3];
				String data_type = "";
				String column_name = "";
				if (sqlcon.getType().equals("sqlServer")) {
					data_type = "data_type";
					column_name = "column_name";
				} else {
					data_type = "DATA_TYPE";
					column_name = "COLUMN_NAME";
				}
				for (int i = 0; i < r.length; i++) {
					ts[i][0] = "Object";
					for (String[] temp : db2java)
						if (r[i].get(data_type).equals(temp[0]))
							ts[i][0] = temp[1];
					ts[i][1] = r[i].get(column_name).toString().toLowerCase();
					ts[i][2] = ts[i][1].substring(0, 1).toUpperCase() + ts[i][1].substring(1);
					bw.write("  " + ts[i][0] + " " + ts[i][1] + ";\r\n");
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

				file = getMapper(bt);
				fw = new FileWriter(file);
				bw = new BufferedWriter(fw);
				bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
				bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\r\n");
				bw.write("<mapper namespace=\"" + TestConnection.basePage + ".mapper." + bt + "Mapper\">\r\n");

				// Base_Column_List
				bw.write("  <!-- 数据库列 -->\r\n");
				bw.write("  <sql id=\"Base_Column_List\">\r\n");
				bw.write("     " + column + "\r\n");
				bw.write("  </sql>\r\n");

				// ResultMap
				bw.write("  <!-- 基础返回对象封装 -->\r\n");
				bw.write("  <resultMap id=\"ResultMap\" type=\"" + TestConnection.basePage + ".entity." + bt + "\">\r\n");
				for (int i = 0; i < r.length; i++) {
					bw.write("         <id property=\"" + ts[i][1] + "\" column=\"" + ts[i][1] + "\" />\r\n");
				}
				bw.write("  </resultMap>\r\n");

				bw.write("  <!-- 默认新增 -->\r\n");
				bw.write("  <insert id=\"post\" parameterType=\"" + TestConnection.basePage + ".entity." + bt + "\">\r\n");
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
				bw.write(" <update id=\"put\" parameterType=\"" + TestConnection.basePage + ".entity." + bt + "\">\r\n");
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
				bw.write("        			1=1\r\n");
				bw.write("             <if test=\"name != null and name != ''\">\r\n");
				bw.write("           		and name like \"%\"#{name}\"%\"\r\n");
				bw.write("             </if>\r\n");
				bw.write("             <if test=\"orderby != null and dir != null\">\r\n");
				bw.write("           		order by ${orderby} ${dir}\r\n");
				bw.write("             </if>\r\n");
				bw.write("             <if test=\"pageStart != null and pageEnd != null\">\r\n");
				bw.write("           		LIMIT #{pageStart},#{pageEnd}\r\n");
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
				bw.write("        			1=1\r\n");
				bw.write("             <if test=\"name != null and name != ''\">\r\n");
				bw.write("           		and name like \"%\"#{name}\"%\"\r\n");
				bw.write("             </if>\r\n");
				bw.write("             <if test=\"orderby != null and dir != null\">\r\n");
				bw.write("           		order by ${orderby} ${dir}\r\n");
				bw.write("             </if>\r\n");
				bw.write("             <if test=\"pageStart != null and pageEnd != null\">\r\n");
				bw.write("           		LIMIT #{pageStart},#{pageEnd}\r\n");
				bw.write("             </if>\r\n");
				bw.write("        </where>\r\n");
				bw.write(" </select>\r\n");
				// getCount
				bw.write("  <!-- 默认集合统计 -->\r\n");
				bw.write(" <select id=\"countlist\" parameterType=\"java.util.HashMap\" resultType=\"int\">\r\n");
				bw.write("       select count(*) from " + tb + " \r\n");
				bw.write("        <where>\r\n");
				bw.write("        			1=1\r\n");
				bw.write("             <if test=\"name != null and name != ''\">\r\n");
				bw.write("           		and name like \"%\"#{name}\"%\"\r\n");
				bw.write("             </if>\r\n");
				bw.write("        </where>\r\n");
				bw.write(" </select>\r\n");
				bw.write("  <!-- 默认返回MAP集合统计 -->\r\n");
				bw.write(" <select id=\"countmap\" parameterType=\"java.util.HashMap\" resultType=\"int\">\r\n");
				bw.write("       select count(*) from " + tb + " \r\n");
				bw.write("        <where>\r\n");
				bw.write("        			1=1\r\n");
				bw.write("             <if test=\"name != null and name != ''\">\r\n");
				bw.write("           		and name like \"%\"#{name}\"%\"\r\n");
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
				bw.write("      		 	and id in\r\n");
				bw.write("        			<foreach item=\"item\" index=\"index\" collection=\"array\" open=\"(\" separator=\",\" close=\")\">\r\n");
				bw.write("           			#{item}\r\n");
				bw.write("        			</foreach>\r\n");
				bw.write("             </if>\r\n");
				bw.write("        </where>\r\n");
				bw.write(" </select>\r\n");
				bw.write("</mapper>");
				bw.flush();

				file = getFileDao(bt);
				fw = new FileWriter(file);
				bw = new BufferedWriter(fw);
				bw.write("package " + TestConnection.basePage + ".mapper;\r\n");
				bw.write("import java.util.List;\r\n");
				bw.write("import java.util.Map;\r\n");
				bw.write("import " + TestConnection.basePage + "." + "entity." + bt + ";\r\n");
				bw.write("import com.yt.app.annotation.RedisCacheAnnotation;\r\n");
				bw.write("import com.yt.app.annotation.RedisCacheEvictAnnotation;\r\n");
				bw.write("import " + TestConnection.commndPage + ".IBaseMapper;\r\n");
				bw.write("\r\n");
				bw.write("\r\n");
				bw.write("/**\r\n");
				bw.write("* @author zj    default  \r\n");
				bw.write("* \r\n");
				bw.write("* @version " + TestConnection.version + "\r\n");
				bw.write("* @createdate  " + DateTimeUtil.getDateTime() + "\r\n");
				bw.write("*/\r\n");
				bw.write("\r\n");
				bw.write("public interface " + bt + "Mapper extends IBaseMapper<" + bt + "> {\r\n");
				bw.write("		/**\r\n");
				bw.write("		 * 保存（持久化）对象\r\n");
				bw.write("		 * \r\n");
				bw.write("		 * @param o\r\n");
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
				bw.write("package " + TestConnection.basePage + ".service;\r\n");
				bw.write("\r\n");
				bw.write("\r\n");
				bw.write("import " + TestConnection.basePage + "." + "entity." + bt + ";\r\n");
				bw.write("import " + TestConnection.commndPage + ".IBaseService;\r\n");
				bw.write("/**\r\n");
				bw.write("* @author zj    default  \r\n");
				bw.write("* \r\n");
				bw.write("* @version " + TestConnection.version + "\r\n");
				bw.write("* @createdate  " + DateTimeUtil.getDateTime() + "\r\n");
				bw.write("*/\r\n");
				bw.write("\r\n");
				bw.write("public interface " + bt + "Service  extends IBaseService<" + bt + ", Long>{\r\n");
				bw.write("}");
				bw.flush();

				file = getServiceImpl(bt);
				fw = new FileWriter(file);
				bw = new BufferedWriter(fw);
				bw.write("package " + TestConnection.basePage + ".service.impl;\r\n");
				bw.write("\r\n");
				bw.write("import org.springframework.beans.factory.annotation.Autowired;\r\n");
				bw.write("import org.springframework.stereotype.Service;\r\n");
				bw.write("import " + TestConnection.basePage + "." + "mapper." + bt + "Mapper;\r\n");
				bw.write("import " + TestConnection.basePage + "." + "service." + bt + "Service;\r\n");
				bw.write("import " + TestConnection.commndPage + ".impl.BaseServiceImpl;\r\n");
				bw.write("import " + TestConnection.basePage + "." + "entity." + bt + ";\r\n");
				bw.write("\r\n");
				bw.write("/**\r\n");
				bw.write("* @author zj    default  \r\n");
				bw.write("* \r\n");
				bw.write("* @version " + TestConnection.version + "\r\n");
				bw.write("* @createdate  " + DateTimeUtil.getDateTime() + "\r\n");
				bw.write("*/\r\n");
				bw.write("\r\n");
				bw.write("@Service\r\n");
				bw.write("public class " + bt + "ServiceImpl extends BaseServiceImpl<" + bt + ", Long> implements " + bt + "Service{\r\n");
				bw.write("  @Autowired\r\n");
				bw.write("  private " + bt + "Mapper mapper;\r\n");
				bw.write("\r\n");
				bw.write("}");
				bw.flush();

				file = getResource(bt);
				fw = new FileWriter(file);
				bw = new BufferedWriter(fw);
				bw.write("package " + TestConnection.basePage + ".resource;\r\n");
				bw.write("\r\n");
				bw.write("\r\n");
				bw.write("import org.springframework.hateoas.ResourceSupport;\r\n");
				bw.write("import " + TestConnection.basePage + ".controller." + bt + "Controller;\r\n");
				bw.write("import " + TestConnection.basePage + "." + "entity." + bt + ";\r\n");
				bw.write("import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;\r\n");
				bw.write("import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;\r\n");
				bw.write("\r\n");
				bw.write("\r\n");
				bw.write("\r\n");
				bw.write("/**\r\n");
				bw.write("* @author zj    default  \r\n");
				bw.write("* \r\n");
				bw.write("* @version " + TestConnection.version + "\r\n");
				bw.write("* @createdate  " + DateTimeUtil.getDateTime() + "\r\n");
				bw.write("*/\r\n");
				bw.write("\r\n");
				bw.write("public class " + bt + "Resource extends ResourceSupport {\r\n");
				bw.write("	private final " + bt + " t;\r\n");
				bw.write("	public " + bt + "Resource(" + bt + " entity) {\r\n");
				bw.write("		this.t = entity;\r\n");
				bw.write("		this.add(linkTo(" + bt + "Controller.class).withRel(\"post\"));\r\n");
				bw.write("		this.add(linkTo(methodOn(" + bt + "Controller.class).list(null, null, null)).withRel(\"list\"));\r\n");

				bw.write("		\r\n");
				bw.write("}\r\n");
				bw.write("	public " + bt + " get" + bt + "() {\r\n");
				bw.write("		return t;\r\n");
				bw.write("	}\r\n");
				bw.write("\r\n");
				bw.write("\r\n");
				bw.write("}");
				bw.flush();

				file = getResourceAssembler(bt);
				fw = new FileWriter(file);
				bw = new BufferedWriter(fw);
				bw.write("package " + TestConnection.basePage + ".resource;\r\n");
				bw.write("\r\n");
				bw.write("\r\n");
				bw.write("import org.springframework.hateoas.mvc.ResourceAssemblerSupport;\r\n");
				bw.write("import " + TestConnection.basePage + ".controller." + bt + "Controller;\r\n");
				bw.write("import " + TestConnection.basePage + "." + "entity." + bt + ";\r\n");
				bw.write("\r\n");
				bw.write("\r\n");
				bw.write("\r\n");
				bw.write("/**\r\n");
				bw.write("* @author zj    default  \r\n");
				bw.write("* \r\n");
				bw.write("* @version " + TestConnection.version + "\r\n");
				bw.write("* @createdate  " + DateTimeUtil.getDateTime() + "\r\n");
				bw.write("*/\r\n");
				bw.write("\r\n");
				bw.write("public class " + bt + "ResourceAssembler extends ResourceAssemblerSupport<" + bt + ", " + bt + "Resource> {\r\n");
				bw.write("	public " + bt + "ResourceAssembler() {\r\n");
				bw.write("		super(" + bt + "Controller.class, " + bt + "Resource.class);\r\n");
				bw.write("	}\r\n");
				bw.write("	@Override");
				bw.write("	public " + bt + "Resource toResource(" + bt + " t) {\r\n");
				bw.write("		return createResourceWithId(t.getId(), t);\r\n");
				bw.write("	}\r\n");
				bw.write("	@Override\r\n");
				bw.write("	protected " + bt + "Resource instantiateResource(" + bt + " t) {\r\n");
				bw.write("		return new " + bt + "Resource(t);\r\n");
				bw.write("	}\r\n");
				bw.write("\r\n");
				bw.write("}");
				bw.flush();

				file = getController(bt);
				fw = new FileWriter(file);
				bw = new BufferedWriter(fw);
				bw.write("package " + TestConnection.basePage + ".controller;\r\n");
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
				// bw.write("import org.springframework.http.HttpHeaders;\r\n");
				bw.write("import com.yt.app.frame.page.IPage;\r\n");
				bw.write("import io.swagger.annotations.ApiOperation;\r\n");
				bw.write("import " + TestConnection.commndPage + ".impl.BaseControllerImpl;\r\n");
				bw.write("import " + TestConnection.basePage + ".resource." + bt + "ResourceAssembler;\r\n");
				bw.write("import " + TestConnection.basePage + "." + "service." + bt + "Service;\r\n");
				bw.write("import " + TestConnection.basePage + "." + "entity." + bt + ";\r\n");
				bw.write("\r\n");
				bw.write("/**\r\n");
				bw.write("* @author zj    default  test\r\n");
				bw.write("* \r\n");
				bw.write("* @version " + TestConnection.version + "\r\n");
				bw.write("* @createdate  " + DateTimeUtil.getDateTime() + "\r\n");
				bw.write("*/\r\n");
				bw.write("\r\n");
				bw.write("\r\n");
				bw.write("@RestController\r\n");
				bw.write("@RequestMapping(\"/" + TestConnection.osName + "/" + TestConnection.version + "/" + bt.toLowerCase() + "\")\r\n");
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
				bw.write("		return new ResponseEntity<Object>(new " + bt
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
				bw.close();
			}
			System.out.println("生成表end：{" + tableNames + "}");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
