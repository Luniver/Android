package hrst.sczd.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileToByte {
    private static FileToByte model;

    private FileToByte() {
    }

    public static FileToByte getInstance() {
        if (model == null) {
            model = new FileToByte();
        }
        return model;
    }

    public byte[] getBytesFromFile(File file) {
        byte[] bytes=null;
        try {
            InputStream isInputStream = new FileInputStream(file);
            // 获取文件大小
            long length = file.length();
            System.out.println("文件:" + length + "," + Integer.MAX_VALUE);
            if (length > Integer.MAX_VALUE) {
                System.out.println("文件太大:" + length);
            }
            // 创建一个数据来保存文件数据
            bytes = new byte[(int) length];
            // 读取数据到byte数组中
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                    && (numRead = isInputStream.read(bytes, offset,
                            bytes.length - offset)) >= 0) {
                offset += numRead;
            }

            // 确保所有数据均被读取
            if (offset < bytes.length) {
                System.out.println("长度错误");
            }

            isInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static File getFileFromBytes(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fileOutputStream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

}
