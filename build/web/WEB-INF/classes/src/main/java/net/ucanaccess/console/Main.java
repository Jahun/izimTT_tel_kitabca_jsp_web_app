/*
Copyright (c) 2012 Marco Amadei.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package net.ucanaccess.console;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import net.ucanaccess.util.Logger;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;

public class Main {
	private static boolean batchMode=false;
	private Connection conn;
	private boolean connected = true;
	private BufferedReader input;
	private String lastSqlQuery;
	
	public Main(Connection conn, BufferedReader input) {
		this.conn = conn;
		this.input = input;
		
	}
	
	private static boolean hasPassword(File fl) throws IOException {
		Database db;
		try{
		db = DatabaseBuilder.open(fl);
	      }catch(IOException e){
	    	  DatabaseBuilder dbb=new  DatabaseBuilder();
	    	  dbb.setReadOnly(true);
	    	  dbb.setFile(fl);
		      db= dbb.open();
		
	    }
		String pwd = db.getDatabasePassword();
		db.close();
		return pwd != null;
	}
	
	private static void lcProperties(Properties pr) {
		Properties nb=new Properties();
		
		for( Entry<Object, Object> entry:pr.entrySet()){
			String key=(String)entry.getKey();
			if(key!=null){
				nb.put(key.toLowerCase(), entry.getValue());
			}
		}
		pr.clear();
		pr.putAll(nb);
	}
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Logger.setLogPrintWriter(new PrintWriter(System.out));
		BufferedReader input = new BufferedReader(new InputStreamReader(
				System.in));
		// password properties info
		Properties info=new Properties();
		if(args.length>0){
			File pfl =new File(args[0]);
			if(pfl.exists()){
				FileInputStream fis=new FileInputStream (pfl);
				info.load(fis);
				lcProperties(info);
			}
		}
		
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
		} catch (ClassNotFoundException e) {
		
			System.out.println(e.getMessage());
			System.out.println("Check your classpath! ");
			System.exit(1);
		}
		Connection conn = null;
		File fl = null;
		long size=0;
		while (fl == null || !fl.exists()) {
			if (fl != null) {
				System.out.println("Given file does not exist");
			}
			System.out.print("Please, enter the full path to the access file (.mdb or accdb): ");
			String path = input.readLine().trim();
			if (path.endsWith(";"))
				path = path.substring(0, path.length() - 1);
			if(path.equalsIgnoreCase("quit")){
				System.out.println("I'm so unhappy.Goodbye.");
				System.exit(1);
			}
			fl = new File(path);
			size=fl.length();
		}
		try {
			String passwordEntry = "";
			String noMem="";
			if (info.containsKey("jackcessopener")||hasPassword(fl)) {
				System.out.print("Please, enter password: ");
				passwordEntry = ";password=" + input.readLine().trim();
			}
			
			if(!info.containsKey("jackcessopener"))
			noMem=size>30000000?";memory=false":"";
			
			conn = DriverManager.getConnection("jdbc:ucanaccess://"
					+ fl.getAbsolutePath() + passwordEntry+noMem,info
					);
			
			SQLWarning sqlw= conn.getWarnings();
			while(sqlw!=null){
				System.out.println(sqlw.getMessage());
				sqlw=sqlw.getNextWarning();	
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.exit(1);
		}
		Main main = new Main(conn, input);
		main.sayHello(conn.getMetaData().getDriverVersion());
		main.start();
	}
	
	public static void setBatchMode(boolean batchMode) {
		Main.batchMode = batchMode;
	}
	
	private void consoleDump(ResultSet rs, int cols, PrintStream out)
			throws SQLException {
		while (rs.next()) {
			System.out.print("| ");
			for (int i = 1; i <= cols; ++i) {
				Object o = rs.getObject(i);
				if(o!=null&&o.getClass().isArray()){
					o=Arrays.toString((Object[])o);
				}
				
				out.print(o + " | ");
			}
			out.println();
			out.println();
		}
	}
	
	private void csvDump(ResultSet rs, int cols, PrintStream out)
			throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		String comma = "";
		for (int i = 1; i <= cols; ++i) {
			String lb=meta.getColumnLabel(i);
			out.print(comma + lb);
			comma = ";";
		}
		out.println();
		while (rs.next()) {
			comma = "";
			for (int i = 1; i <= cols; ++i) {
				Object o = rs.getObject(i);
				if(o==null)o="";
				if(o!=null&&o.getClass().isArray()){
					o=Arrays.toString((Object[])o);
				}
				if(o instanceof Date){
					SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					o=df.format((Date)o);
				}
				if(o instanceof BigDecimal){
					DecimalFormat df = new DecimalFormat("0.0########");
					DecimalFormatSymbols dfs=new DecimalFormatSymbols();
					dfs.setDecimalSeparator('.');
					df.setDecimalFormatSymbols(dfs);
					df.setGroupingUsed(false);
					o=df.format(o);
					
				}
				if(o instanceof String){
					o=((String) o).replaceAll("\n", " ");
				}
				out.print(comma + o);
				comma = ";";
			}
			out.println();
			
		}
	}
	
	public void dump(ResultSet rs, PrintStream out, boolean consoleMode)
			throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		int cols = meta.getColumnCount();
		if (consoleMode) {
			StringBuffer header = new StringBuffer("| ");
			for (int i = 1; i <= cols; ++i) {
				header.append(meta.getColumnLabel(i) + " | ");
			}
			String interline = "";
			for (int i = 0; i < header.length(); ++i) {
				interline += "-";
			}
			out.println(interline);
			out.println(header);
			out.println(interline);
			out.println();
		}
		if (consoleMode) {
			consoleDump(rs, cols, out);
		} else {
			csvDump(rs, cols, out);
		}
	}
	
	private void executeStatement(String sql) {
		try {
			Statement st = conn.createStatement();
			if (st.execute(sql)) {
				ResultSet rs = st.getResultSet();
				if (rs != null) {
					dump(rs, System.out, true);
					this.lastSqlQuery = sql;
				} else {
					System.out.println("Ok!");
				}
			} else {
				int num = st.getUpdateCount();
				prompt(num == 0 ? "No rows affected" : num + " rows affected");
			}
		} catch (Exception e) {
			prompt(e.getMessage());
			//e.printStackTrace();
		}
	}

	private void prompt() {
		System.out.println();
		if(!batchMode)
		System.out.print("UCanAccess>");
	}
	
	private void prompt(String content) {
		if(!batchMode)
		System.out.println("UCanAccess>" + content);
	}
	
	private String readInput() {
		try {
			String ret = input.readLine();
			if (ret == null) {
				prompt("Ciao!");
				System.exit(0);
			}
			return ret.trim();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private void sayHello(String version) {
		prompt("");
		System.out.println("Copyright (c) 2012 Marco Amadei");
		System.out.println("UCanAccess version "+version);
		System.out.println("You are connected!! ");
		
		System.out.println("Type quit to exit ");
		System.out.println();
		System.out.println("Commands end with ; ");
		System.out.println();
		System.out.println("use:   ");
		System.out.println("   export <pathToCsv>;");
		System.out.println("for exporting into a .csv file the result set from the last executed query");
		prompt();
		
	}
	
	private void start() throws SQLException {
		String userInput;
		StringBuffer sb = new StringBuffer();
		while (connected) {
			if ((userInput = readInput()).toLowerCase()
					.equalsIgnoreCase("quit")) {
				connected = false;
				break;
			}
			sb.append(" ").append(userInput);
			if (sb.length() == 0) {
				this.prompt();
			}
			if (userInput.trim().endsWith(";")) {
				String cmd = sb.toString().substring(0, sb.length() - 1).trim();
				if (cmd.toLowerCase().startsWith("export ")) {
				
					StringTokenizer st = new StringTokenizer(cmd);
					if (st.countTokens() != 2) {
						prompt("Export Command right sintax is:export <pathToCsv>");
					}
					if (this.lastSqlQuery == null) {
						prompt("You must first execute a SQL query, then export the ResultSet!");
					} else {
						Statement statement = conn.createStatement();
						ResultSet rs = statement
								.executeQuery(this.lastSqlQuery);
						st.nextToken();
						File fl = new File(st.nextToken());
						try {
							PrintStream out = new PrintStream(fl);
							dump(rs, out, false);
							out.flush();
							out.close();
							prompt("Created file: "+fl.getAbsolutePath());
						} catch (FileNotFoundException e) {
							prompt(e.getMessage());
						}
					}
				} else {
					executeStatement(cmd);
				}
				sb = new StringBuffer();
				this.prompt();
			}
		}
		System.out.println("Cheers. Thank you for using UCanAccess Jdbc Driver.");
	}
}
