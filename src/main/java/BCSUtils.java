

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

import com.baidu.inf.iis.bcs.BaiduBCS;
import com.baidu.inf.iis.bcs.auth.BCSCredentials;
import com.baidu.inf.iis.bcs.model.DownloadObject;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;
import com.baidu.inf.iis.bcs.request.GetObjectRequest;
import com.baidu.inf.iis.bcs.request.PutObjectRequest;
import com.baidu.inf.iis.bcs.response.BaiduBCSResponse;
import com.qihua.util.FileUtils;

public class BCSUtils {

  static String host = "bcs.duapp.com";
  static String accessKey = "sWUwI8vgZyAs4A4d52DANq2h";
  static String secretKey = "FLYuuRkbcYKDR6AjrNNB7AxWrt2C677t";
  static String bucket = "qihuacloud";
  static BCSCredentials credentials = new BCSCredentials(accessKey, secretKey);
  static BaiduBCS baiduBCS = new BaiduBCS(credentials, host);

  static {
    baiduBCS.setDefaultEncoding("UTF-8");
  }

  public static void putObject(String filename, byte[] bytes) {
    PutObjectRequest request = new PutObjectRequest(bucket, "/" + filename, createTempFile(filename, bytes));
    ObjectMetadata metadata = new ObjectMetadata();
    request.setMetadata(metadata);
    BaiduBCSResponse<ObjectMetadata> response = baiduBCS.putObject(request);
    ObjectMetadata objectMetadata = response.getResult();
    // System.out.println("x-bs-request-id: " + response.getRequestId());
    // System.out.println(objectMetadata);
  }

  public static File getObject(String filename) throws IOException {
    File tmp =
        File.createTempFile(filename.replaceFirst("_.*", "") + "_", "." + StringUtils.getFilenameExtension(filename));
    tmp.deleteOnExit();

    GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, "/" + filename);
    BaiduBCSResponse<DownloadObject> response = baiduBCS.getObject(getObjectRequest, tmp);

    return tmp;
  }

  public static void download(HttpServletRequest request, HttpServletResponse response, File file) throws Exception {
    BufferedInputStream is = null;
    BufferedOutputStream os = null;

    try {
      String filename = FileUtils.generateFilename(request, file.getName());

      response.addHeader("Content-disposition", "attachment; filename=" + filename);
      response.addHeader("Content-Length", String.valueOf(file.length()));

      is = new BufferedInputStream(new FileInputStream(file));
      os = new BufferedOutputStream(response.getOutputStream());

      byte[] buff = new byte[2048];
      int bytesRead;
      while (-1 != (bytesRead = is.read(buff, 0, buff.length))) {
        os.write(buff, 0, bytesRead);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (is != null) {
        is.close();
      }

      if (os != null) {
        os.close();
      }
    }
  }

  private static File createTempFile(String filename, byte[] bytes) {
    BufferedOutputStream bos = null;
    FileOutputStream fos = null;

    try {
      File file = File.createTempFile("tmp", StringUtils.getFilenameExtension(filename));
      file.deleteOnExit();

      fos = new FileOutputStream(file);
      bos = new BufferedOutputStream(fos);
      bos.write(bytes);

      return file;
    } catch (IOException e) {
      System.out.println("tmp file create failed.");

      return null;
    } finally {
      if (bos != null) {
        try {
          bos.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
      if (fos != null) {
        try {
          fos.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    }
  }

}
