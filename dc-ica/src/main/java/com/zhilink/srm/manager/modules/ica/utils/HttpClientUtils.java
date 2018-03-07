package com.zhilink.srm.manager.modules.ica.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import com.zhilink.manager.framework.common.exception.CommonException;

/**
 * @web http://www.mobctrl.net
 * @author Zheng Haibo
 * @Description: 文件下载 POST GET
 */
public class HttpClientUtils {

	private final String boundaryPrefix = "--";
	/**
	 * 最大线程池
	 */
	public static final int THREAD_POOL_SIZE = 5;

	public interface HttpClientDownLoadProgress {
		public void onProgress(int progress);
	}

	private static HttpClientUtils httpClientDownload;

	private ExecutorService downloadExcutorService;

	private HttpClientUtils() {
		downloadExcutorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
	}

	public static HttpClientUtils getInstance() {
		if (httpClientDownload == null) {
			httpClientDownload = new HttpClientUtils();
		}
		return httpClientDownload;
	}

	/**
	 * 下载文件
	 * 
	 * @param url
	 * @param filePath
	 */
	public void download(final String url, final String filePath) {
		downloadExcutorService.execute(new Runnable() {

			public void run() {
				httpDownloadFile(url, filePath, null, null);
			}
		});
	}

	/**
	 * 下载文件
	 * 
	 * @param url
	 * @param filePath
	 * @param progress
	 *            进度回调
	 */
	public void download(final String url, final String filePath,
			final HttpClientDownLoadProgress progress) {
		downloadExcutorService.execute(new Runnable() {

			public void run() {
				httpDownloadFile(url, filePath, progress, null);
			}
		});
	}

	/**
	 * 下载文件
	 * 
	 * @param url
	 * @param filePath
	 */
	private void httpDownloadFile(String url, String filePath,
			HttpClientDownLoadProgress progress, Map<String, String> headMap) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet(url);
			setGetHead(httpGet, headMap);
			CloseableHttpResponse response1 = httpclient.execute(httpGet);
			try {
				// System.out.println(response1.getStatusLine());
				HttpEntity httpEntity = response1.getEntity();
				long contentLength = httpEntity.getContentLength();
				InputStream is = httpEntity.getContent();
				// 根据InputStream 下载文件
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				byte[] buffer = new byte[4096];
				int r = 0;
				long totalRead = 0;
				while ((r = is.read(buffer)) > 0) {
					output.write(buffer, 0, r);
					totalRead += r;
					if (progress != null) {// 回调进度
						progress.onProgress((int) (totalRead * 100 / contentLength));
					}
				}
				FileOutputStream fos = new FileOutputStream(filePath);
				output.writeTo(fos);
				output.flush();
				output.close();
				fos.close();
				EntityUtils.consume(httpEntity);
			} finally {
				response1.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * get请求
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String httpGet(String url, String fileName) throws Exception {
		return httpGet(url, null, fileName);
	}

	/**
	 * http get请求
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String httpGet(String url, Map<String, String> headMap,
			String fileName) throws Exception {
		String responseContent = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet(url);
			CloseableHttpResponse response1 = httpclient.execute(httpGet);
			setGetHead(httpGet, headMap);
			try {
				if (response1.getStatusLine().getStatusCode() == 200) {
					System.out.println(response1.getStatusLine());
					HttpEntity entity = response1.getEntity();
					// responseContent = getRespString(entity);
					InputStream is = entity.getContent();
					File file = new File(fileName);
					if (!file.exists()) {
						file.createNewFile();
					}
					FileOutputStream fo = new FileOutputStream(file);
					byte[] buffer = new byte[4096];
					int r = 0;
					while ((r = is.read(buffer)) > 0) {
						fo.write(buffer, 0, r);
					}
					fo.flush();
					fo.close();
					// System.out.println("debug:" + responseContent);
					EntityUtils.consume(entity);
					return file.getAbsolutePath();
				} else {
					throw new Exception(response1.getStatusLine().toString());
				}
			} finally {
				if(response1!=null)
				response1.close();
			}
		} finally {
			try {
				if(httpclient!=null)
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}
		}
	}

	public String httpPost(String url, Map<String, String> paramsMap) {
		return httpPost(url, paramsMap, null);
	}

	/**
	 * http的post请求
	 * 
	 * @param url
	 * @param paramsMap
	 * @return
	 */
	public String httpPost(String url, Map<String, String> paramsMap,
			Map<String, String> headMap) {
		String responseContent = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(url);
			setPostHead(httpPost, headMap);
			setPostParams(httpPost, paramsMap);
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				System.out.println(response.getStatusLine());
				HttpEntity entity = response.getEntity();
				responseContent = getRespString(entity);
				EntityUtils.consume(entity);
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("responseContent = " + responseContent);
		return responseContent;
	}

	/**
	 * 设置POST的参数
	 * 
	 * @param httpPost
	 * @param paramsMap
	 * @throws Exception
	 */
	private void setPostParams(HttpPost httpPost, Map<String, String> paramsMap)
			throws Exception {
		if (paramsMap != null && paramsMap.size() > 0) {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			Set<String> keySet = paramsMap.keySet();
			for (String key : keySet) {
				nvps.add(new BasicNameValuePair(key, paramsMap.get(key)));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		}
	}

	/**
	 * 设置http的HEAD
	 * 
	 * @param httpPost
	 * @param headMap
	 */
	private void setPostHead(HttpPost httpPost, Map<String, String> headMap) {
		if (headMap != null && headMap.size() > 0) {
			Set<String> keySet = headMap.keySet();
			for (String key : keySet) {
				httpPost.addHeader(key, headMap.get(key));
			}
		}
	}

	/**
	 * 设置http的HEAD
	 * 
	 * @param httpGet
	 * @param headMap
	 */
	private void setGetHead(HttpGet httpGet, Map<String, String> headMap) {
		if (headMap != null && headMap.size() > 0) {
			Set<String> keySet = headMap.keySet();
			for (String key : keySet) {
				httpGet.addHeader(key, headMap.get(key));
			}
		}
	}

	/**
	 * 上传文件
	 * 
	 * @param serverUrl
	 *            服务器地址
	 * @param localFilePath
	 *            本地文件路径
	 * @param serverFieldName
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String uploadFileImpl(String serverUrl, String localFilePath,
			String serverFieldName, Map<String, String> params)
			throws Exception {
		String respStr = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httppost = new HttpPost(serverUrl);
			File file = new File(localFilePath);
			if(!file.exists()){
				return "该适配器数据文件不存在！";
			}
			FileBody binFileBody = new FileBody(file);

			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder
					.create();
			// add the file params
			multipartEntityBuilder.addPart(serverFieldName, binFileBody);
			// 设置上传的其他参数
			setUploadParams(multipartEntityBuilder, params);

			HttpEntity reqEntity = multipartEntityBuilder.build();
			httppost.setHeader("Charsert", "UTF-8");
			// httppost.setHeader("Content-Type",
			// "multipart/form-data; boundary=" + boundaryPrefix);
			httppost.setEntity(reqEntity);

			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity resEntity = response.getEntity();
				respStr = getRespString(resEntity);
				EntityUtils.consume(resEntity);
				}else{
					throw new Exception(response.getStatusLine().toString());
				}
			} finally {
				if(response!=null)
				response.close();
			}
		} finally {
			if(httpclient!=null)
			httpclient.close();
		}
		return respStr;
	}

	public String uploadFileImpl(String serverUrl, MultipartFile file,
			String serverFieldName, Map<String, String> params)
			throws Exception {
		String respStr = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httppost = new HttpPost(serverUrl);
//			FileBody binFileBody = new FileBody(file);

			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder
					.create();
			// add the file params
		//	multipartEntityBuilder.addPart(serverFieldName, file);
			multipartEntityBuilder.addBinaryBody("file", file.getInputStream(), ContentType.MULTIPART_FORM_DATA, serverFieldName);// 文件流
			// 设置上传的其他参数
			setUploadParams(multipartEntityBuilder, params);

			HttpEntity reqEntity = multipartEntityBuilder.build();
			httppost.setHeader("Charsert", "UTF-8");
			// httppost.setHeader("Content-Type",
			// "multipart/form-data; boundary=" + boundaryPrefix);
			httppost.setEntity(reqEntity);

			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity resEntity = response.getEntity();
				respStr = getRespString(resEntity);
				EntityUtils.consume(resEntity);
				}else{
					throw new Exception(response.getStatusLine().toString());
				}
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		return respStr;
	}
	/**
	 * 设置上传文件时所附带的其他参数
	 * 
	 * @param multipartEntityBuilder
	 * @param params
	 */
	private void setUploadParams(MultipartEntityBuilder multipartEntityBuilder,
			Map<String, String> params) {
		if (params != null && params.size() > 0) {
			Set<String> keys = params.keySet();
			for (String key : keys) {
				multipartEntityBuilder
						.addPart(key, new StringBody(params.get(key),
								ContentType.TEXT_PLAIN));
			}
		}
	}

	/**
	 * 将返回结果转化为String
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	private String getRespString(HttpEntity entity) throws Exception {
		if (entity == null) {
			return null;
		}
		InputStream is = entity.getContent();
		StringBuffer strBuf = new StringBuffer();
		byte[] buffer = new byte[4096];
		int r = 0;
		while ((r = is.read(buffer)) > 0) {
			strBuf.append(new String(buffer, 0, r, "UTF-8"));
		}
		return strBuf.toString();
	}

	public static void main(String[] args) {
		/**
		 * 测试下载文件 异步下载
		 */
		// HttpClientUtils.getInstance().download(
		// "http://localhost:8080/dc-manager/a/ica/api/export", "test.xlsx",
		// new HttpClientDownLoadProgress() {
		//
		// public void onProgress(int progress) {
		// System.out.println("download progress = " + progress);
		// }
		// });
		//
		// // POST 同步方法
		// Map<String, String> params = new HashMap<String, String>();
		// params.put("username", "admin");
		// params.put("password", "admin");
		// HttpClientUtils.getInstance().httpPost(
		// "http://192.168.31.183:8080/SSHMySql/register", params);
		//
		// GET 同步方法
		// HttpClientUtils.getInstance().httpGet(
		// "http://localhost:8080/dc-manager/a/ica/api/export");
		//
		try {
			Map<String, String> uploadParams = new LinkedHashMap<String, String>();
			uploadParams.put("fileName", "test.xlsx");
			HttpClientUtils.getInstance().uploadFileImpl(
					"http://localhost:8080/dc-manager/a/ica/api/import",
					"D:/1234test.xlsx", "file", uploadParams);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 上传文件 POST 同步方法
		// try {
		// Map<String,String> uploadParams = new LinkedHashMap<String,
		// String>();
		// uploadParams.put("userImageContentType", "image");
		// uploadParams.put("userImageFileName", "testaa.png");
		// HttpClientUtils.getInstance().uploadFileImpl(
		// "http://192.168.31.183:8080/SSHMySql/upload", "android_bug_1.png",
		// "userImage", uploadParams);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

	}

}
