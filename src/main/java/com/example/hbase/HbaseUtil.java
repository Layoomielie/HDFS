package com.example.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhanghongjian
 * @Date 2019/4/8 9:59
 * @Description
 */

public class HbaseUtil {

    private static Configuration conf = HBaseConfiguration.create();
    private static Connection connection;
    private static Admin admin;

    static {
        conf.set("hbase.rootdir", "hdfs://10.100.23.92:9000/hbase");
        conf.set("hbase.zookeeper.quorum", "10.100.23.91,10.100.23.92,10.100.23.93");

        try {
            connection = ConnectionFactory.createConnection(conf);
            admin = connection.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        try {
            if (null != admin) {
                admin.close();
            }
            if (null != connection) {
                connection.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     * @date: 2019/4/8
     * @Desc:   hbase删除表
     */
    public void createTable(String tableName, String... columnFamily) {
        TableName tableNameObj = TableName.valueOf(tableName);
        try {
            if (admin.tableExists(tableNameObj)) {
                System.out.println("Table : " + tableName + " already exists !");
            } else {
                HTableDescriptor td = new HTableDescriptor(tableNameObj);
                int len = columnFamily.length;
                for (int i = 0; i < len; i++) {
                    HColumnDescriptor family = new HColumnDescriptor(columnFamily[i]);
                    td.addFamily(family);
                }
                admin.createTable(td);
                System.out.println(tableName + " 表创建成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(tableName + " 表创建失败！");
        }

    }

    @Test
    public void testCreateTable() {
        createTable("cross_history", "carinfo", "parkInfo", "deviceInfo");
    }

    /*
     * @date: 2019/4/8
     * @Desc: Hbase删除表
     */

    public void delTable(String tableName) {
        TableName tableNameObj = TableName.valueOf(tableName);
        try {
            if (admin.tableExists(tableNameObj)) {
                admin.disableTable(tableNameObj);
                admin.deleteTable(tableNameObj);
                System.out.println(tableName + " 表删除成功！");
            } else {
                System.out.println(tableName + " 表不存在！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(tableName + " 表删除失败！");
        }

    }

    @Test
    public void testDelTable() {
        delTable("cross_history");
    }

    public void insertRecord(String tableName, String rowKey, String columnFamily, String qualifier, String value) {
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Put put = new Put(rowKey.getBytes());
            put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifier), Bytes.toBytes(value));
            put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifier), Bytes.toBytes(value));
            table.put(put);
            table.close();
            System.out.println(tableName + " 表插入数据成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(tableName + " 表插入数据失败！");
        }
    }

    @Test
    public void testInsertRecord() {
        /*insertRecord("cross_history", "001", "carinfo", "plateNo", "浙A12345");
        insertRecord("cross_history", "002", "carinfo", "plateNo", "浙A12345");
        insertRecord("cross_history", "003", "carinfo", "plateNo", "浙A12345");
        insertRecord("cross_history", "001", "parkInfo", "parkName", "中兴花园");
        insertRecord("cross_history", "002", "parkInfo", "parkName", "中兴花园");
        insertRecord("cross_history", "003", "parkInfo", "parkName", "中兴花园");
        insertRecord("cross_history", "001", "deviceInfo", "deviceInfo", "道闸");
        insertRecord("cross_history", "002", "deviceInfo", "deviceInfo", "道闸");
        insertRecord("cross_history", "003", "deviceInfo", "deviceInfo", "道闸");*/

        insertRecord("cross_history", "004", "carinfo", "plateNo", "GOOD");
        insertRecord("cross_history", "004", "parkInfo", "parkName", "ADD");
        insertRecord("cross_history", "004", "deviceInfo", "deviceInfo", "TEST");
    }

    public void deleteRecord(String tableName, String rowKey) {
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Delete del = new Delete(rowKey.getBytes());
            table.delete(del);
            table.close();
            System.out.println(tableName + " 表删除数据成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(tableName + " 表删除数据失败！");
        }
    }

    @Test
    public void testDeleteRecord() {
        deleteRecord("cross_history", "001");

    }

    public static Result getOneRecord(String tableName, String rowKey) {
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Get get = new Get(rowKey.getBytes());
            Result rs = table.get(get);
            System.out.println(tableName + " 表获取数据成功！");
            System.out.println("rowkey为:" + rowKey);
            return rs;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Test
    public void testGetOneRecord() {
        getOneRecord("zjxxw", "20190410");
    }

    public static Result getOneRecord(String tableName, String rowKey,String colFamily) {
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Get get = new Get(rowKey.getBytes());
            get.addFamily(Bytes.toBytes(colFamily));
            Result rs = table.get(get);
            System.out.println(tableName + " 表获取数据成功！");
            System.out.println("rowkey为:" + rowKey);
            return rs;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Result getOneRecord(String tableName, String rowKey,String colFamily,String column) {
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Get get = new Get(rowKey.getBytes());
            get.addColumn(Bytes.toBytes(colFamily),Bytes.toBytes(column));
            Result rs = table.get(get);
            System.out.println(tableName + " 表获取数据成功！");
            System.out.println("rowkey为:" + rowKey);
            return rs;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Test
    public void testOneCode(){
        Result result = getOneRecord("zjxxw", "20190411", "hour");
        printCellList(result);
    }
    /*
     * @date: 2019/4/10
     * @Desc: 获取result中的值
     */

    public List<HbaseEntiy> getCellList(Result rs) {
        List<HbaseEntiy> list = new ArrayList<>();
        List<Cell> cells = rs.listCells();
        if (cells != null) {
            for (Cell cell : cells) {
                HbaseEntiy entiy = new HbaseEntiy();
                entiy.setFamily(new String(cell.getFamily()));
                entiy.setQualifier(new String(cell.getQualifier()));
                entiy.setValue(new String(cell.getValue()));
            }
        }
        return list;
    }

    public void printCellList(Result rs) {
        List<Cell> cells = rs.listCells();
        if (cells != null) {
            for (Cell cell : cells) {
                System.out.println(new String(cell.getFamily()) + " : " + new String(cell.getQualifier()) + " : " + new String(cell.getValue()));
            }
        }
    }

    public List<Result> getAll(String tableName) {
        try {
            Connection connection = ConnectionFactory.createConnection(conf);
            Table table = connection.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            ResultScanner scanner = table.getScanner(scan);
            List<Result> list = new ArrayList<Result>();
            for (Result r : scanner) {
                list.add(r);
            }
            scanner.close();
            System.out.println(tableName + " 表获取所有记录成功！");
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void testGetAll() {
        System.out.println(getAll("cross_history"));
    }

    public static List<Result> getManyRecord(String tableName, String startRow, String stopRow,String family,String qualifier) {
        try {
            Connection connection = ConnectionFactory.createConnection(conf);
            Table table = connection.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            scan.addColumn(Bytes.toBytes(family),Bytes.toBytes(qualifier));
            scan.setStartRow(Bytes.toBytes(startRow));
            scan.setStopRow(Bytes.toBytes(stopRow));
            ResultScanner scanner = table.getScanner(scan);
            List<Result> list = new ArrayList<Result>();
            for (Result r : scanner) {
                list.add(r);
            }
            scanner.close();
            System.out.println(tableName + " 表获取指定记录成功！");
            return list;
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getResultValue(Result result,String family,String qualifier){
        byte[] bytes = result.getValue(Bytes.toBytes(family), Bytes.toBytes(qualifier));
        return Bytes.toString(bytes);
    }

    @Test
    public void testGetManyRecord() {
        List<Result> results = getManyRecord("zjxxw", "20190409", "20190411","","");
        for (Result result : results) {
            printCellList(result);
        }
    }

}