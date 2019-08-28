package com.shizy.utils.file;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class FileUtil {

    public static InputStream getResourcesFileInputStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }

    // 通过该方法将在指定目录下添加指定文件
    public static void fileupload(byte[] file, String filePath, String fileName) throws IOException {
        // 目标目录
        File targetfile = new File(filePath);
        if (!targetfile.exists()) {
            targetfile.mkdirs();
        }

        // 二进制流写入
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    public static boolean lastDownload(HttpServletResponse response, File file, String fname) {
        boolean isDownload = false;
        byte[] bytes = new byte[1024];
        FileInputStream fis = null;
        BufferedInputStream bis = null;

        try {
            fname = new String(fname.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
            response.setContentType("application/x-msdownload");
            response.addHeader("Content-Disposition", "attachment;fileName=" + fname);
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            OutputStream os = response.getOutputStream();
            int b = bis.read(bytes);
            while (b != -1) {
                os.write(bytes, 0, b);
                b = bis.read(bytes);
            }
            os.close();
            isDownload = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isDownload;
    }

    /**
     * 判断文件大小
     *
     * @param fileSize 文件长度
     * @param size     限制大小
     * @param unit     限制单位（B,K,M,G）
     */
    public static boolean checkFileSize(long fileSize, int size, String unit) {
        long fomatSize = 0;
        if ("B".equals(unit.toUpperCase())) {
            fomatSize = size;
        } else if ("K".equals(unit.toUpperCase())) {
            fomatSize = size * 1024;
        } else if ("M".equals(unit.toUpperCase())) {
            fomatSize = size * 1048576;
        } else if ("G".equals(unit.toUpperCase())) {
            fomatSize = size * 1073741824;
        }
        return fileSize <= fomatSize;
    }

    /************************图片输出*********************************/

    public static boolean outputBufferedImage(HttpServletResponse response, BufferedImage img, String format) {

        response.setContentType("image/" + format);//设置相应类型,告诉浏览器输出的内容为图片
        response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);

        try {
            return ImageIO.write(img, format, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean outputImage(HttpServletResponse response, String filePath) {
        return outputImage(response, filePath, true);
    }

    public static boolean outputImage(HttpServletResponse response, String filePath, boolean disableCache) {

        if (disableCache) {
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
        }

        return outputImageFile(response, filePath);
    }

    private static boolean outputImageFile(HttpServletResponse response, String filePath) {
        try {
            Object[] param = getBufferedImage(filePath);
            BufferedImage img = (BufferedImage) param[0];
            String imgFormat = (String) param[1];

            response.setContentType("image/" + imgFormat);//设置相应类型,告诉浏览器输出的内容为图片

            return ImageIO.write(img, imgFormat, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Object[] getBufferedImage(String filePath) throws IOException {
        ImageInputStream input = null;
        ImageReader reader = null;
        try {
            input = ImageIO.createImageInputStream(new File(filePath));

            Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
            if (readers.hasNext()) {
                reader = readers.next();
                reader.setInput(input);

                return new Object[]{reader.read(0), reader.getFormatName()};
            }
        } finally {
            if (reader != null) {
                reader.dispose();
            }
            if (input != null) {
                input.close();
            }
        }
        return null;
    }

    /*********************************************************/


    /*******************音频文件输出***************************/

    private static InputStream getInputStream(String mediaUrl) {
        InputStream inputStream;
        try {
            URL urlGet = new URL(mediaUrl);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            // 获取文件转化为byte流
            inputStream = http.getInputStream();
            return inputStream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveVoiceToDisk(String mediaUrl, String picPath) {
        InputStream inputStream = getInputStream(mediaUrl);
        byte[] data = new byte[10240];
        int len = 0;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(picPath + System.currentTimeMillis() + ".amr");
            while ((len = inputStream.read(data)) != -1) {
                fileOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(fileOutputStream);
        }
    }

    public static void saveVoiceToDisk(File file, String picPath) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        byte[] data = new byte[10240];
        int len = 0;
        FileOutputStream fileOutputStream = null;
        fileOutputStream = new FileOutputStream(picPath + System.currentTimeMillis() + ".amr");
        while ((len = inputStream.read(data)) != -1) {
            fileOutputStream.write(data, 0, len);
        }
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(fileOutputStream);
    }
    /********************************************************/
}











