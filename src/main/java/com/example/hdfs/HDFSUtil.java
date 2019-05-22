package com.example.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.*;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhanghongjian
 * @Date 2019/3/26 11:41
 * @Description
 */
public class HDFSUtil {

    static Logger logger = LoggerFactory.getLogger(HDFSUtil.class);

    public static FileSystem getFileSystem() {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://10.100.23.92:9000");
        //hdfs://101.132.37.5:9010
        FileSystem fs = null;
        try {
            fs = FileSystem.get(new URI("hdfs://10.100.23.92:9000"), configuration, "root");
            //hdfs://101.132.37.5:9010
            //  FileSystem fs = FileSystem.get(configuration);
            Path path = new Path("/user/input/a.txt");
            boolean b = fs.exists(path);

            System.out.println("----------------------");
            System.out.println(b);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return fs;
    }

    public static boolean mkdir(FileSystem fs, String pathStr) {
        boolean f = false;

        Path path = new Path(pathStr);
        try {
            // even the path exist,it can also create the path.
            f = fs.mkdirs(path);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.toString());
        } finally {
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.toString());
            }
        }
        return f;
    }

    public static boolean rmdir(FileSystem fs, String pathStr) {
        boolean b = false;
        // drop the last path
        Path path = new Path(pathStr);
        try {
            b = fs.delete(path, true);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.toString());
        } finally {
        }

        return b;
    }

    public static boolean rename_dir(FileSystem hdfs, String oldPathStr, String newPathStr) {
        boolean b = false;
        Path oldPath = new Path(oldPathStr);
        Path newPath = new Path(newPathStr);

        try {
            b = hdfs.rename(oldPath, newPath);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.toString());
        } finally {
            try {
                hdfs.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.toString());
            }
        }

        return b;
    }

    /*
     * @date: 2019/3/26
     * @Desc: 遍历HDFS
     */
    public static Set<String> recursive_fire(FileSystem hdfs, Path listPath) {
        FileStatus[] files = null;
        Set<String> set = new HashSet<>();
        try {
            files = hdfs.listStatus(listPath);

            // 实际上并不是每个文件夹都会有文件的。
            if (files.length == 0) {
                // 如果不使用toUri()，获取的路径带URL。
                set.add(listPath.toUri().getPath());
            } else {
                // 判断是否为文件
                for (FileStatus f : files) {
                    if (files.length == 0 || f.isFile()) {
                        set.add(f.getPath().toUri().getPath());
                        System.out.println(f.getPath().toUri().getPath());
                    } else {
                        // 是文件夹，且非空，就继续遍历
                        recursive_fire(hdfs, f.getPath());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
        return set;
    }

    public static String check_fire_isDir(FileSystem fs, String pathStr) {
        boolean isExists = false;
        boolean isDirectorys = false;
        boolean isFiles = false;

        Path path = new Path(pathStr);

        try {
            isExists = fs.exists(path);
            isDirectorys = fs.isDirectory(path);
            isFiles = fs.isFile(path);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.toString());
        } finally {
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.toString());
            }
        }

        if (!isExists) {
            System.out.println("文件不存在.");
            return null;
        } else {
            if (isDirectorys) {
                System.out.println("Directory");
                return "dir";
            } else if (isFiles) {
                System.out.println("Files");
                return "file";
            }
        }
        return null;
    }

    /**
     * 文件上传
     *
     * @param fs
     */
    public static void getFileFromHDFS(FileSystem fs, String HDFSPathStr, String localPathStr) {
        Path HDFSPath = new Path(HDFSPathStr);
        Path localPath = new Path(localPathStr);

        try {
            fs.copyToLocalFile(HDFSPath, localPath);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("zhe li you cuo wu !", e);
        } finally {
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("zhe li you cuo wu !", e);
            }
        }
    }

    public static void putFileToHDFS(FileSystem fs, String localPathStr, String hdfsPathStr) {

        boolean pathExists = false;
        // 如果上传的路径不存在会创建
        // 如果该路径文件已存在，就会覆盖
        Path localPath = new Path(localPathStr);
        Path hdfsPath = new Path(hdfsPathStr);

        try {
            fs.copyFromLocalFile(localPath, hdfsPath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /*
     * @date: 2019/3/26
     * @Desc: HDFS之间拷贝文件
     */
    public static void copyFileBetweenHDFS(FileSystem hdfs, String inPathStr, String outPathStr) {
        Path inPath = new Path(inPathStr);
        Path outPath = new Path(outPathStr);

        // byte[] ioBuffer = new byte[1024*1024*64];
        // int len = 0;

        FSDataInputStream hdfsIn = null;
        FSDataOutputStream hdfsOut = null;

        try {
            hdfsIn = hdfs.open(inPath);
            hdfsOut = hdfs.create(outPath, true);
            IOUtils.copyBytes(hdfsIn, hdfsOut, 1024 * 1024 * 64, false);

        } catch (IOException e) {
            logger.error(e.toString());
        } finally {
            try {
                hdfsOut.close();
                hdfsIn.close();
            } catch (IOException e) {

            }

        }

    }

    /*
     * @date: 2019/3/26
     * @Desc: 追加文件内容 本地文件追加到HDFS
     */
    public static void appendToFile(FileSystem fs, String localFilePath, String remoteFilePath) {
        Path remotePath = new Path(remoteFilePath);
        try (
                FileInputStream in = new FileInputStream(localFilePath);) {
            FSDataOutputStream out = fs.append(remotePath);
            byte[] data = new byte[1024];
            int read = -1;
            while ((read = in.read(data)) > 0) {
                out.write(data, 0, read);
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * @date: 2019/3/26
     * @Desc:  查看HDFS文件内容
     */
    public static void cat_file(FileSystem fs, String pathStr) {
        Path path = new Path(pathStr);
        try {
            DataInputStream inputStream = fs.open(path);
            IOUtils.copyBytes(inputStream, System.out, 4096, false);
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    public static void copy(FileSystem fs, String src, String dsc) {
        /**
         * 1: 建立输入流
         * 2：建立输出流
         * 3: 两个流的对接
         * 4: 资源的关闭
         */

        //1:建立输入流
        FSDataInputStream input = null;
        try {
            input = fs.open(new Path(src));

            //2:建立输出流
            FSDataOutputStream output = fs.create(new Path(dsc));

            //3:两个流的对接
            byte[] b = new byte[1024];
            int hasRead = 0;
            while ((hasRead = input.read(b)) > 0) {
                output.write(b, 0, hasRead);
            }
            //4:资源的关闭
            input.close();
            output.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     * @date: 2019/3/27
     * @Desc: HDFS 之间文件夹拷贝  不会删除之前文件
     */
    public static void copyDir(FileSystem fs, String src, String dsc) {
        Path srcPath = new Path(src);
        String[] strs = src.split("/");
        String lastName = strs[strs.length - 1];
        try {
            if (fs.isDirectory(srcPath)) {
                fs.mkdirs(new Path(dsc + "/" + lastName));

                //遍历
                FileStatus[] fileStatus = fs.listStatus(srcPath);
                for (FileStatus fileSta : fileStatus) {
                    copyDir(fs, fileSta.getPath().toString(), dsc + "/" + lastName);
                }

            } else {
                fs.mkdirs(new Path(dsc));
                System.out.println("src" + src + "\n" + dsc + "/" + lastName);
                copy(fs, src, dsc + "/" + lastName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void remove_Nginx_output(FileSystem fs, String year, String month, String day) {
        String[] list = {"accesslog", "area", "browser", "daypv", "device", "errorlog", "exceptioncount", "exceptionrank", "hourpv", "ipcount", "monthpv", "pvcount", "qps", "request_time", "totalpv", "urlpv", "uvcount", "yearpv"};

        for (String s : list
        ) {
            // copyDir(fs,"/user/flume/nginx_log_output_need/"+s,"/user/flume/nginx_log_output_need/zjxxw");
            rmdir(fs, "/user/flume/nginx_log_output_need/zjxxw/" + s + "/" + year + month + day);
            rmdir(fs, "/user/flume/nginx_log_output_need/zjxxw/" + s + "/mobile/" + year + month + day);
            rmdir(fs, "/user/flume/nginx_log_output_need/zjxxw/" + s + "/pc/" + year + month + day);
            rmdir(fs, "/user/flume/nginx_log_output_need/zjxxw/" + s + "/mobile/" + year + month + day);
            rmdir(fs, "/user/flume/nginx_log_output_need/zjxxw/" + s + "/" + year + "/" + month + "/" + year + month + day);
            rmdir(fs, "/user/flume/nginx_log_output_need/zjxxw/monthpv/" + year + "/" + month);
            rmdir(fs, "/user/flume/nginx_log_output_need/zjxxw/totalpv/" + year);
            rmdir(fs, "/user/flume/nginx_log_output_need/zjxxw/yearpv/" + year);


        }
    }

    public static void remove_Statictis_output(FileSystem fs, String year, String month, String day) {
        rmdir(fs, "user/flume/nginx_log_output_need/zjxxw/monthpv/" + year + "/" + month);


    }

    public static void main(String[] args) throws IOException {
        System.setProperty("hadoop.home.dir", "D:\\hadoop\\hadoop-common-2.6.0-bin-master");
        FileSystem fs = getFileSystem();
        //    boolean b = rmdir(fs, "/user/a.txt");
        //    System.out.println(b);
        //    copyFileBetweenHDFS(fs,"/user/input/a.txt","/user/a.txt");

        //   mkdir(fs,"/user");
        //  appendToFile(fs,"D:\\a.txt","/user/input/a.txt");
        //     String path="/user/flume/nginx_log_output_need/zjxxw/yearpv/2019";
        //     cat_file(fs,path+"/part-00000");
        //     copyDir(fs,"/user/AA","/user/BB");
        //      putFileToHDFS(fs,"D:\\b.txt","/user/AA");

        String path = "/user/flume/nginx_log_output_need/zjxxw/daypv/2019/3/20190328";
        path = "/user/flume/nginx_log_output_need/zjxxw/pvcount/20190327";
        path = "/user/DD";
        path = "/user/flume/nginx_log_output_need/zjxxw/exceptionrank/20190408";
        cat_file(fs, path + "/part-00000");
        //  path="/user/flume/nginx_logs/zjxxw/20190328/2019-03-28-08.1553731200050.log";
        //  cat_file(fs,path);

        fs.close();

    }


}
