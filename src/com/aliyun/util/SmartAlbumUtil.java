package com.aliyun.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import com.aliyun.oss.OSSClient;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.cloudphoto.model.v20170711.AddAlbumPhotosRequest;
import com.aliyuncs.cloudphoto.model.v20170711.AddAlbumPhotosResponse;
import com.aliyuncs.cloudphoto.model.v20170711.CreateAlbumRequest;
import com.aliyuncs.cloudphoto.model.v20170711.CreateAlbumResponse;
import com.aliyuncs.cloudphoto.model.v20170711.CreatePhotoRequest;
import com.aliyuncs.cloudphoto.model.v20170711.CreatePhotoResponse;
import com.aliyuncs.cloudphoto.model.v20170711.CreatePhotoStoreRequest;
import com.aliyuncs.cloudphoto.model.v20170711.CreatePhotoStoreResponse;
import com.aliyuncs.cloudphoto.model.v20170711.CreateTransactionRequest;
import com.aliyuncs.cloudphoto.model.v20170711.CreateTransactionResponse;
import com.aliyuncs.cloudphoto.model.v20170711.CreateTransactionResponse.Transaction;
import com.aliyuncs.cloudphoto.model.v20170711.CreateTransactionResponse.Transaction.Upload;
import com.aliyuncs.cloudphoto.model.v20170711.DeleteAlbumsRequest;
import com.aliyuncs.cloudphoto.model.v20170711.DeleteAlbumsResponse;
import com.aliyuncs.cloudphoto.model.v20170711.DeleteFacesRequest;
import com.aliyuncs.cloudphoto.model.v20170711.DeleteFacesResponse;
import com.aliyuncs.cloudphoto.model.v20170711.DeletePhotoStoreRequest;
import com.aliyuncs.cloudphoto.model.v20170711.DeletePhotoStoreResponse;
import com.aliyuncs.cloudphoto.model.v20170711.DeletePhotosRequest;
import com.aliyuncs.cloudphoto.model.v20170711.DeletePhotosResponse;
import com.aliyuncs.cloudphoto.model.v20170711.DeletePhotosResponse.Result;
import com.aliyuncs.cloudphoto.model.v20170711.GetDownloadUrlRequest;
import com.aliyuncs.cloudphoto.model.v20170711.GetDownloadUrlResponse;
import com.aliyuncs.cloudphoto.model.v20170711.GetPhotoStoreRequest;
import com.aliyuncs.cloudphoto.model.v20170711.GetPhotoStoreResponse;
import com.aliyuncs.cloudphoto.model.v20170711.GetPhotosRequest;
import com.aliyuncs.cloudphoto.model.v20170711.GetPhotosResponse;
import com.aliyuncs.cloudphoto.model.v20170711.GetQuotaRequest;
import com.aliyuncs.cloudphoto.model.v20170711.GetQuotaResponse;
import com.aliyuncs.cloudphoto.model.v20170711.GetQuotaResponse.Quota;
import com.aliyuncs.cloudphoto.model.v20170711.GetThumbnailRequest;
import com.aliyuncs.cloudphoto.model.v20170711.GetThumbnailResponse;
import com.aliyuncs.cloudphoto.model.v20170711.InactivatePhotosRequest;
import com.aliyuncs.cloudphoto.model.v20170711.InactivatePhotosResponse;
import com.aliyuncs.cloudphoto.model.v20170711.ListAlbumPhotosRequest;
import com.aliyuncs.cloudphoto.model.v20170711.ListAlbumPhotosResponse;
import com.aliyuncs.cloudphoto.model.v20170711.ListAlbumsRequest;
import com.aliyuncs.cloudphoto.model.v20170711.ListAlbumsResponse;
import com.aliyuncs.cloudphoto.model.v20170711.ListAlbumsResponse.Album;
import com.aliyuncs.cloudphoto.model.v20170711.ListFacePhotosRequest;
import com.aliyuncs.cloudphoto.model.v20170711.ListFacePhotosResponse;
import com.aliyuncs.cloudphoto.model.v20170711.ListFacesRequest;
import com.aliyuncs.cloudphoto.model.v20170711.ListFacesResponse;
import com.aliyuncs.cloudphoto.model.v20170711.ListFacesResponse.Face;
import com.aliyuncs.cloudphoto.model.v20170711.ListMomentPhotosRequest;
import com.aliyuncs.cloudphoto.model.v20170711.ListMomentPhotosResponse;
import com.aliyuncs.cloudphoto.model.v20170711.ListMomentsRequest;
import com.aliyuncs.cloudphoto.model.v20170711.ListMomentsResponse;
import com.aliyuncs.cloudphoto.model.v20170711.ListMomentsResponse.Moment;
import com.aliyuncs.cloudphoto.model.v20170711.ListPhotoStoresRequest;
import com.aliyuncs.cloudphoto.model.v20170711.ListPhotoStoresResponse;
import com.aliyuncs.cloudphoto.model.v20170711.ListPhotoStoresResponse.PhotoStore;
import com.aliyuncs.cloudphoto.model.v20170711.ListPhotosRequest;
import com.aliyuncs.cloudphoto.model.v20170711.ListPhotosResponse;
import com.aliyuncs.cloudphoto.model.v20170711.ListTagPhotosRequest;
import com.aliyuncs.cloudphoto.model.v20170711.ListTagPhotosResponse;
import com.aliyuncs.cloudphoto.model.v20170711.ListTagsRequest;
import com.aliyuncs.cloudphoto.model.v20170711.ListTagsResponse;
import com.aliyuncs.cloudphoto.model.v20170711.ListTagsResponse.Tag;
import com.aliyuncs.cloudphoto.model.v20170711.MergeFacesRequest;
import com.aliyuncs.cloudphoto.model.v20170711.MergeFacesResponse;
import com.aliyuncs.cloudphoto.model.v20170711.MoveAlbumPhotosRequest;
import com.aliyuncs.cloudphoto.model.v20170711.MoveAlbumPhotosResponse;
import com.aliyuncs.cloudphoto.model.v20170711.MoveFacePhotosRequest;
import com.aliyuncs.cloudphoto.model.v20170711.MoveFacePhotosResponse;
import com.aliyuncs.cloudphoto.model.v20170711.ReactivatePhotosRequest;
import com.aliyuncs.cloudphoto.model.v20170711.ReactivatePhotosResponse;
import com.aliyuncs.cloudphoto.model.v20170711.RemoveAlbumPhotosRequest;
import com.aliyuncs.cloudphoto.model.v20170711.RemoveAlbumPhotosResponse;
import com.aliyuncs.cloudphoto.model.v20170711.RemoveFacePhotosRequest;
import com.aliyuncs.cloudphoto.model.v20170711.RemoveFacePhotosResponse;
import com.aliyuncs.cloudphoto.model.v20170711.RenameAlbumRequest;
import com.aliyuncs.cloudphoto.model.v20170711.RenameAlbumResponse;
import com.aliyuncs.cloudphoto.model.v20170711.RenameFaceRequest;
import com.aliyuncs.cloudphoto.model.v20170711.RenameFaceResponse;
import com.aliyuncs.cloudphoto.model.v20170711.SearchPhotosRequest;
import com.aliyuncs.cloudphoto.model.v20170711.SearchPhotosResponse;
import com.aliyuncs.cloudphoto.model.v20170711.SearchPhotosResponse.Photo;
import com.aliyuncs.cloudphoto.model.v20170711.SetAlbumCoverRequest;
import com.aliyuncs.cloudphoto.model.v20170711.SetAlbumCoverResponse;
import com.aliyuncs.cloudphoto.model.v20170711.SetFaceCoverRequest;
import com.aliyuncs.cloudphoto.model.v20170711.SetFaceCoverResponse;
import com.aliyuncs.cloudphoto.model.v20170711.SetMeRequest;
import com.aliyuncs.cloudphoto.model.v20170711.SetMeResponse;
import com.aliyuncs.cloudphoto.model.v20170711.SetQuotaRequest;
import com.aliyuncs.cloudphoto.model.v20170711.SetQuotaResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse.Credentials;

public class SmartAlbumUtil {
	private static String region = "cn-shanghai";
	private static String apiVersion = "2015-04-01";
	private static String storeName = "ddmeitu";
	private static String sessionName = "test";
	
	private static final String accessKeyId = "LTAIv67uCLczaEgP";
	private static final String accessKeySecret = "hwT3sc07lbA5ErTke6YPsE8s6uizVa";
	private static final String roleArn = "acs:ram::1136051292329249:role/smartalbumrole";
	private static DefaultAcsClient acsClient = null;
	
	private static SimpleDateFormat sdf1 = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss.SSS");
	private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
	
	private static boolean testPhoto = true;
	private static boolean testAlbum = true;
	private static boolean testFace = true;
	private static boolean testOther = true;
	
	private static final String SUCCESS = "Success";
	private static final String EOF = "EOF";
	
	private static String State = "active";
	private static String Direction = "forward";
	private static int Size = 10;
	
	public static void main(String[] args) {
		
		try {
//			createPhoto("P70901-165435.jpg");
//			createPhoto("P70710-062654.jpg");
//			listPhotoStores();
//			listPhotos();
//			List<Long> list = new ArrayList<Long>();
//			list.add(840371603882692608L);
//			deletePhotos(list);
//			listMoments();
//			listMomentPhotos(840371604075630592L);
//			searchPhotos("2017年8月30日");
//			searchPhotos("2017年7月");
//			searchPhotos("2017年5月");
//			searchPhotos("2017年");
//			
//			searchPhotos("中国,四川省,绵阳市,涪城区,园兴东街");
//			searchPhotos("四川,涪城");
//			searchPhotos("中国");
//			searchPhotos("四川");
//			searchPhotos("绵阳");
//			searchPhotos("涪城");
//			searchPhotos("东街");
//			searchPhotos("电子设备");
//			listFaces();
//			listTags();
//			listPhotos();
//			listMoments();
//			searchPhotos("衣服");
//			listFaces();
//			listFacePhotos(839268718147067904L);
//			getDownloadUrl(839268705018896384L);
//			getDownloadUrl(839268694671548416L);
//			setFaceCover(839268677873360896L, 839268675193192448L);
//			listFaces();
//			List<Long> list = new ArrayList<Long>();
//			list.add(839268705018896384L);
//			getPhotos(list);
//			setFaceCover(839268727768793088L, 839268694671548416L);
//			listFaces();
			getPara("ListAlbums");
			/********** 照片接口 **********/
			if (!testPhoto) {
				String filePath = "d:\\11.jpg";
				Long photoId1 = 837833938499276800L;
				List<Long> photoIds1 = new ArrayList<Long>();
				photoIds1.add(photoId1);
				Long createPhotoId;
				List<Long> createPhotoIds = new ArrayList<Long>();
				
			    // 列出当前照片
				listPhotos();
				// 上传照片
				createPhotoId = createPhoto(filePath);
				createPhotoIds.add(createPhotoId);
				// 列出上传后的照片
				listPhotos();
				// 获取照片信息
				getPhotos(photoIds1);
				// 获取缩略图
				getThumbnail(photoId1);
				// 获取下载地址
				getDownloadUrl(photoId1);
				// 批量逻辑删除照片
				inactivatePhotos(photoIds1);
				// 列出删除后的照片
				listPhotos();
				// 批量恢复删除的照片
				reactivatePhotos(photoIds1);
				// 列出恢复后的照片
				listPhotos();
				// 批量物理删除照片
				deletePhotos(createPhotoIds);
				// 列出删除后的照片
				listPhotos();
			}
		   
		    /********** 相簿接口 **********/
			if (!testAlbum) {
				Long photoId2 = 839189213072334848L;
				List<Long> photoIds2 = new ArrayList<Long>();
				photoIds2.add(photoId2);
				String albumName1 = sdf2.format(new Date());
				String albumName2 = "CopyOf" + albumName1;
				String albumName3 = "NewNameOf" + albumName1;
				Long albumId1;
				Long albumId2;
				List<Long> albumIds = new ArrayList<Long>();
				
				// 列出当前相簿
				listAlbums();
				// 创建新的相簿
				albumId1 = createAlbum(albumName1);
				albumId2 = createAlbum(albumName2);
				albumIds.add(albumId1);
				albumIds.add(albumId2);
				// 给新建相簿添加照片
				addAlbumPhotos(albumId1, photoIds2);
				// 列出创建后的相簿
				listAlbums();
				// 列出新建相簿的照片
				listAlbumPhotos(albumId1);
				// 将新建相簿的照片移动到备份相簿
				moveAlbumPhotos(albumId1, albumId2, photoIds2);
				// 列出备份相簿的照片
				listAlbumPhotos(albumId2);
				// 为备份相簿设置封面
				setAlbumCover(albumId2, photoId2);
				// 将备份相簿内的照片移除
				removeAlbumPhotos(albumId2, photoIds2);
				// 列出备份相簿的照片
				listAlbumPhotos(albumId2);
				// 对新建相簿重命名
				renameAlbum(albumId1, albumName3);
				// 删除刚创建的相簿
				deleteAlbums(albumIds);
				// 列出删除后的相簿
				listAlbums();
			}
		    
			/********** 人脸接口 **********/
			if (!testFace) {
				Long faceId1 = 839268727768793088L;
				Long faceId2 = 839268718147067904L;
				Long photoId1 = 839199602484973568L;
				Long photoId2 = 839199606721232896L;
				Long photoId3 = 839268705018896384L;
				List<Long> photoIds1 = new ArrayList<Long>();
				photoIds1.add(photoId1);
				List<Long> faceIds1 = new ArrayList<Long>();
				faceIds1.add(faceId1);
				List<Long> faceIds2 = new ArrayList<Long>();
				faceIds2.add(faceId2);
				String faceName = "人脸2";
				
				// 列出当前人脸
//				listFaces();
//				// 列出人脸的照片
//				listFacePhotos(faceId1);
//				listFacePhotos(faceId2);
//				// 设置人脸2为自己
//				setMe(faceId1);
//				// 为人脸2设置封面
//				setFaceCover(faceId2, photoId2);
//				setFaceCover(faceId2, photoId2);
				// 将人脸2重命名
				renameFace(faceId2, faceName);
				listFaces();
//				// 将人脸1的照片移动到人脸2
//				moveFacePhotos(faceId1, faceId2, photoIds1);
//				// 列出移动后的人脸照片
//				listFacePhotos(faceId1);
//				listFacePhotos(faceId2);
//				// 合并照片到人脸1
//				mergeFaces(faceId1, faceIds2);
//				listFaces();
//				// 将人脸1内的照片移除
//				removeFacePhotos(faceId1, photoIds1);
//				// 列出操作后人脸的照片
//				listFacePhotos(faceId1);
//				listFacePhotos(faceId2);
//				// 删除人脸
//				deleteFaces(faceIds1);
//				// 列出删除后的人脸
//				listFaces();
			}
			
			if (!testOther) {
				Long tagId = 169332736L;
				Long momentId = 837833938553802752L;
				String keyWord = "屏幕";
				Long defaultQuota =  5 * 1024 * 1024 * 1024L;
//				String photoStoreName = "ddmeitu-bak";
//				String bucketName = "smart-album-bak";
				
				/********** PhotoStore接口 **********/
//				createPhotoStore(photoStoreName, bucketName, defaultQuota);
				listPhotoStores();
				getPhotoStore(storeName);
				deletePhotoStore(storeName);
				
				/********** 标签接口 **********/
				listTags();
				listTagPhotos(tagId);
				
				/********** 时光接口 **********/
				listMoments();
				listMomentPhotos(momentId);
				
				/********** 搜索接口 **********/
				searchPhotos(keyWord);
				
				/********** 用户接口 **********/
				getQuota();
				setQuota(defaultQuota);
			}
		} catch (Exception e) {
			print(e.toString());
		}
	}
	
	private static void print(String strContent) {
		System.out.println(sdf1.format(new Date()) + "====" + strContent);
	}
	
	public static void setGlobalDirection(String direction) {
		Direction = direction;
	}
	
	public static void setGlobalState(String state) {
		State = state;
	}
	
	public static void setGlobalSize(int size) {
		Size = size;
	}
	
	/************************************ 初始化 begin ************************************/
	private static void init() throws ClientException {
        // 从您的业务服务器处获取到临时访问凭证和其他访问云相册的信息
		if (acsClient == null) {
			Credentials credentials = getCredential();
	        // Step1: 创建DefaultAcsClient对象
	        DefaultProfile profile = DefaultProfile.getProfile(region, credentials.getAccessKeyId(),
	            credentials.getAccessKeySecret(), credentials.getSecurityToken());
	        acsClient = new DefaultAcsClient(profile);
		}
	}
	
	private static AssumeRoleResponse assumeRole(String accessKeyId,
			String accessKeySecret, String roleArn, String roleSessionName,
			String policy, ProtocolType protocolType) throws ClientException {
		try {
			// 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
			DefaultProfile profile = DefaultProfile.getProfile(region, accessKeyId, accessKeySecret);
			DefaultAcsClient client = new DefaultAcsClient(profile);

			// 创建一个 AssumeRoleRequest 并设置请求参数
			final AssumeRoleRequest request = new AssumeRoleRequest();
			request.setVersion(apiVersion);
			request.setMethod(MethodType.POST);
			request.setProtocol(protocolType);

			request.setRoleArn(roleArn);
			request.setRoleSessionName(roleSessionName);
			request.setPolicy(policy);

			// 发起请求，并得到response
			final AssumeRoleResponse response = client.getAcsResponse(request);
			return response;
		} catch (ClientException e) {
			throw e;
		}
	}
	
	private static Credentials getCredential() throws ClientException {
		AssumeRoleResponse response = assumeRole(accessKeyId,accessKeySecret, roleArn, sessionName, null, ProtocolType.HTTPS);
		return response.getCredentials();
	}
	/************************************ 初始化 end ************************************/
	
	/************************************ 照片接口 begin ************************************/
	/**
	 * 上传照片
	 * @param filePath
	 * @return photoId，异常则为null
	 */
	public static Long createPhoto(String filePath) {
		try {
			init();
			
			File file = new File(filePath);
			String fileName = file.getName();
			FileInputStream fin = null;
			String md5;
			try {
				fin = new FileInputStream(file);
				md5 = DigestUtils.md5Hex(fin);
				fin.close();
			} finally {
				if (fin != null) {
					fin.close();
				}
			}
			// 步骤一：开启上传事务：从上传事务中获取上传文件到OSS的临时密钥和其他创建照片需要的信息
			// 1.1 构建请求对象
			CreateTransactionRequest request = new CreateTransactionRequest();
			request.setMd5(md5);
			// 指定上传到哪个PhotoStore
			request.setStoreName(storeName);
			// 设置文件的扩展名帮助服务端判断文件的类型
			request.setExt(fileName.substring(fileName.lastIndexOf(".") + 1));
			request.setForce(true);
			request.setSize(file.length());
			// 1.2 发起请求：
			CreateTransactionResponse createTransactionResponse = acsClient.getAcsResponse(request);
			Transaction tnx = createTransactionResponse.getTransaction();
			Upload upload = tnx.getUpload();
			// 步骤二：以下两行代码为构建OSS客户端并上传照片到OSS，本例子采用OSS的简单上传。也可以采用OSS的分块上传，具体参见OSS的文档
			OSSClient ossClient = new OSSClient(upload.getOssEndpoint(), upload.getAccessKeyId(),
					upload.getAccessKeySecret(), upload.getStsToken());
			ossClient.putObject(upload.getBucket(), upload.getObjectKey(), file);
			// 步骤三：提交上传事务，完成上传
			CreatePhotoRequest createPhotoRequest = new CreatePhotoRequest();
			createPhotoRequest.setStoreName(storeName);
			createPhotoRequest.setFileId(upload.getFileId());
			createPhotoRequest.setSessionId(upload.getSessionId());
			createPhotoRequest.setPhotoTitle(file.getName());
			createPhotoRequest.setUploadType("manual");
			CreatePhotoResponse response = acsClient.getAcsResponse(createPhotoRequest);
			print("createPhoto: " + response.getCode() + ", PhotoId = " + response.getPhoto().getId());
			
			return response.getPhoto().getId();
		} catch (Exception e) {
			print(e.toString());
			return null;
		}
	}
	
	/**
	 * 批量物理删除照片
	 * @param photoIds
	 * @return 照片数量，异常则为0
	 */
	public static int deletePhotos(List<Long> photoIds) {
		try {
			init();
	        
	        DeletePhotosRequest request = new DeletePhotosRequest();
	        request.setStoreName(storeName);
	        request.setPhotoIds(photoIds);
	        DeletePhotosResponse response = acsClient.getAcsResponse(request);
	        List<Result> results = response.getResults();
	        for (Result result : results) {
	        	print("deletePhotos: " +  result.getCode() + ", PhotoId = " + result.getId());
			}
	        print("deletePhotos: Total Photos Count = " + results.size());
	        
	        return results.size();
		} catch (Exception e) {
			print(e.toString());
			return 0;
		}
    }
	
	/**
	 * 分批获取用户照片
	 * @return 照片集合，异常则为null
	 */
	public static List<ListPhotosResponse.Photo> listPhotos() {
		try {
			init();
	        
	        String cursor = "0";
	        List<ListPhotosResponse.Photo> photoList = new ArrayList<ListPhotosResponse.Photo>();
	        while (!EOF.equalsIgnoreCase(cursor)) {
	            ListPhotosRequest request = new ListPhotosRequest();
	            request.setStoreName(storeName);
	            request.setCursor(cursor);
	            request.setDirection(Direction);
	            request.setState(State);
	            request.setSize(Size);
	            ListPhotosResponse response = acsClient.getAcsResponse(request);
	            List<ListPhotosResponse.Photo> photos = response.getPhotos();
	            for (ListPhotosResponse.Photo photo : photos) {
	            	photoList.add(photo);
	                print("listPhotos: PhotoId = " + photo.getId() + ", PhotoName = " + photo.getTitle());
				}
	            cursor = response.getNextCursor();
	        }
	        print("listPhotos: Total Photos Count = " + photoList.size());
	        
	        return photoList;
		} catch (Exception e) {
			print(e.toString());
			return null;
		}
    }
	
	/**
	 * 批量获取特定Id的照片信息
	 * @param photoIds
	 * @return 照片集合，异常则为null
	 */
	public static List<GetPhotosResponse.Photo> getPhotos(List<Long> photoIds) {
		try {
			init();
	        
	        GetPhotosRequest request = new GetPhotosRequest();
	        request.setStoreName(storeName);
	        request.setPhotoIds(photoIds);
	        GetPhotosResponse response = acsClient.getAcsResponse(request);
	        List<GetPhotosResponse.Photo> photos = response.getPhotos();
	        for (GetPhotosResponse.Photo photo : photos) {
	            print("getPhotos[PhotoId = " + photo.getId() + "]: PhotoName = " + photo.getTitle());
	        }
	        print("getPhotos: Total Photos Count = " + photos.size());
	        
	        return photos;
		} catch (Exception e) {
			print(e.toString());
			return null;
		}
    }
	
	/**
	 * 批量逻辑删除照片
	 * @param photoIds
	 * @return 照片数量，异常则为0
	 */
	public static int inactivatePhotos(List<Long> photoIds) {
		try {
			init();
	        
	        InactivatePhotosRequest request = new InactivatePhotosRequest();
	        request.setStoreName(storeName);
	        request.setPhotoIds(photoIds);
	        InactivatePhotosResponse response = acsClient.getAcsResponse(request);
	        List<InactivatePhotosResponse.Result> results = response.getResults();
	        for (InactivatePhotosResponse.Result result : results) {
	        	print("inactivatePhotos: " +  result.getCode() + ", PhotoId = " + result.getId());
			}
	        print("inactivatePhotos: Total Photos Count = " + results.size());
	        
	        return results.size();
		} catch (Exception e) {
			print(e.toString());
			return 0;
		}
    }
	
	/**
	 * 批量恢复逻辑删除的照片
	 * @param photoIds
	 * @return 照片数量，异常则为0
	 */
	public static int reactivatePhotos(List<Long> photoIds) {
		try {
			init();
	        
	        ReactivatePhotosRequest request = new ReactivatePhotosRequest();
	        request.setStoreName(storeName);
	        request.setPhotoIds(photoIds);
	        ReactivatePhotosResponse response = acsClient.getAcsResponse(request);
	        List<ReactivatePhotosResponse.Result> results = response.getResults();
	        for (ReactivatePhotosResponse.Result result : results) {
	        	print("reactivatePhotos: " +  result.getCode() + ", PhotoId = " + result.getId());
			}
	        print("reactivatePhotos: Total Photos Count = " + results.size());
	        
	        return results.size();
		} catch (Exception e) {
			print(e.toString());
			return 0;
		}
    }
	
	/**
	 * 获取照片的缩略图地址，有效期为1小时
	 * @param photoId
	 * @return 缩略图地址，异常则为null
	 */
	public static String getThumbnail(Long photoId) {
		try {
			init();
			
			GetThumbnailRequest request = new GetThumbnailRequest();
			request.setPhotoId(photoId);
	        request.setStoreName(storeName);
	        request.setZoomType("image/resize,h_100");
	        GetThumbnailResponse response = acsClient.getAcsResponse(request);
	        String url = response.getThumbnailUrl();
	        print("getThumbnail[PhotoId = " + photoId + "]: ThumbnailUrl = " + url);
	        
	        return url;
		} catch (Exception e) {
			print(e.toString());
			return null;
		}
    }
	
	/**
	 * 获取照片的下载地址，有效期为1小时
	 * @param photoId
	 * @return 下载地址，异常则为null
	 */
	public static String getDownloadUrl(Long photoId) {
		try {
			init();
	        
	        GetDownloadUrlRequest request = new GetDownloadUrlRequest();
	        request.setPhotoId(photoId);
	        request.setStoreName(storeName);
	        GetDownloadUrlResponse response = acsClient.getAcsResponse(request);
	        String url = response.getDownloadUrl();
	        print("getDownloadUrl[PhotoId = " + photoId + "]:  DownloadUrl = " + url);
	        
	        return url;
		} catch (Exception e) {
			print(e.toString());
			return null;
		}
    }
	/************************************ 照片接口 end ************************************/
	
	/************************************ 相簿接口 begin ************************************/
	/**
	 * 创建一个相簿
	 * @param albumName
	 * @return albumId，异常则为null
	 */
	public static Long createAlbum(String albumName) {
		try {
			init();
	        
	        CreateAlbumRequest request = new CreateAlbumRequest();
	        request.setStoreName(storeName);
	        request.setAlbumName(albumName);
	        CreateAlbumResponse response = acsClient.getAcsResponse(request);
	        print("createAlbum: " + response.getCode() + ", AlbumId = " + response.getAlbum().getId() + ", AlbumName = " + response.getAlbum().getName());
	        
	        return response.getAlbum().getId();
		} catch (Exception e) {
			print(e.toString());
			return null;
		}
    }
	
	/**
	 * 批量删除相簿
	 * @param albumIds
	 * @return 相簿数量，异常则为0
	 */
	public static int deleteAlbums(List<Long> albumIds) {
		try {
			init();
	        
	        DeleteAlbumsRequest request = new DeleteAlbumsRequest();
	        request.setStoreName(storeName);
	        request.setAlbumIds(albumIds);
	        DeleteAlbumsResponse response = acsClient.getAcsResponse(request);
	        List<DeleteAlbumsResponse.Result> results = response.getResults();
	        for (DeleteAlbumsResponse.Result result : results) {
	        	print("deleteAlbums: " +  result.getCode() + ", AlbumId = " + result.getId());
			}
	        print("deleteAlbums: Total Albums Count = " + results.size());
	        
	        return results.size();
		} catch (Exception e) {
			print(e.toString());
			return 0;
		}
    }
	
	/**
	 * 分批获取当前用户的相簿
	 * @return 相簿集合，异常则为null
	 */
	public static List<Album> listAlbums() {
		try {
			init();
			
	        String cursor = "0";
	        List<Album> albumList = new ArrayList<Album>();
	        while (!EOF.equalsIgnoreCase(cursor)) {
	        	ListAlbumsRequest request = new ListAlbumsRequest();
	            request.setStoreName(storeName);
	            request.setCursor(cursor);
	            request.setDirection(Direction);
	            request.setState(State);
	            request.setSize(Size);
	            ListAlbumsResponse response = acsClient.getAcsResponse(request);
	            List<Album> albums = response.getAlbums();
	            for (Album album : albums) {
	            	albumList.add(album);
	                print("listAlbums: AlbumId = " + album.getId() + ", AlbumName = " + album.getName());
	            }
	            cursor = response.getNextCursor();
	        }
	        print("listAlbums: Total Albums Count = " + albumList.size());
	        
	        return albumList;
		} catch (Exception e) {
			print(e.toString());
			return null;
		}
	}
	
	/**
	 * 分批获取相簿中的照片
	 * @param photoIds
	 * @return photoId集合，异常则为null
	 */
	public static List<Long> listAlbumPhotos(Long AlbumId) {
		try {
			init();
	        
	        String cursor = "0";
	        List<Long> photoIds = new ArrayList<Long>();
	        while (!EOF.equalsIgnoreCase(cursor)) {
	            ListAlbumPhotosRequest request = new ListAlbumPhotosRequest();
	            request.setStoreName(storeName);
	            request.setCursor(cursor);
	            request.setDirection(Direction);
	            request.setState(State);
	            request.setSize(Size);
	            request.setAlbumId(AlbumId);
	            ListAlbumPhotosResponse response = acsClient.getAcsResponse(request);
	            List<ListAlbumPhotosResponse.Result> results = response.getResults();
	            for (ListAlbumPhotosResponse.Result result : results) {
	            	photoIds.add(result.getPhotoId());
	                print("listAlbumPhotos[AlbumId = " + AlbumId + "]: PhotoId = " + result.getPhotoId());
	            }
	            cursor = response.getNextCursor();
	        }
	        print("listAlbumPhotos[AlbumId = " + AlbumId + "]: Total Photos Count = " + photoIds.size());
	        
	        return photoIds;
		} catch (Exception e) {
			print(e.toString());
			return null;
		}
    }
	
	/**
	 * 批量将照片从一个相簿移动到另外一个相簿
	 * @param sourceAlbumId
	 * @param targetAlbumId
	 * @param photoIds
	 * @return 照片数量，异常则为0
	 */
	public static int moveAlbumPhotos(Long sourceAlbumId, Long targetAlbumId, List<Long> photoIds) {
		try {
			init();
	        
	        MoveAlbumPhotosRequest request = new MoveAlbumPhotosRequest();
	        request.setStoreName(storeName);
	        request.setPhotoIds(photoIds);
	        request.setSourceAlbumId(sourceAlbumId);
	        request.setTargetAlbumId(targetAlbumId);
	        MoveAlbumPhotosResponse response = acsClient.getAcsResponse(request);
	        List<MoveAlbumPhotosResponse.Result> results = response.getResults();
	        for (MoveAlbumPhotosResponse.Result result : results) {
	            print("moveAlbumPhotos[" + sourceAlbumId + "-->" + targetAlbumId + "]: " + result.getCode() + ", PhotoId = " + result.getId());
	        }
	        print("moveAlbumPhotos[" + sourceAlbumId + "-->" + targetAlbumId + "]:  Total Photos Count = " + results.size());
	        
	        return results.size();
		} catch (Exception e) {
			print(e.toString());
			return 0;
		}
    }
	
	/**
	 * 批量将照片从相簿中移除，该操作仅解除照片和相簿的关系，不会删除照片
	 * @param albumId
	 * @param photoIds
	 * @return 照片数量，异常则为0
	 */
	public static int removeAlbumPhotos(Long albumId, List<Long> photoIds) {
		try {
			init();
	        
	        RemoveAlbumPhotosRequest request = new RemoveAlbumPhotosRequest();
	        request.setStoreName(storeName);
	        request.setAlbumId(albumId);
	        request.setPhotoIds(photoIds);
	        RemoveAlbumPhotosResponse response = acsClient.getAcsResponse(request);
	        List<RemoveAlbumPhotosResponse.Result> results = response.getResults();
	        for (RemoveAlbumPhotosResponse.Result result : results) {
	        	print("removeAlbumPhotos[AlbumId = " + albumId + "]: " +  result.getCode() + ", PhotoId = " + result.getId());
			}
	        print("removeAlbumPhotos[AlbumId = " + albumId + "]: Total Photos Count = " + results.size());
	        
	        return results.size();
		} catch (Exception e) {
			print(e.toString());
			return 0;
		}
    }
	
	/**
	 * 修改相簿名称
	 * @param albumId
	 * @param albumName
	 * @return 成功或失败
	 */
	public static boolean renameAlbum(Long albumId, String albumName) {
		try {
			init();
	        
	        RenameAlbumRequest request = new RenameAlbumRequest();
	        request.setStoreName(storeName);
	        request.setAlbumId(albumId);
	        request.setAlbumName(albumName);
	        RenameAlbumResponse response = acsClient.getAcsResponse(request);
	        print("renameAlbum[AlbumId = " + albumId + ", AlbumName = " + albumName + "]: " +  response.getCode());
	        
	        return SUCCESS.equalsIgnoreCase(response.getCode());
		} catch (Exception e) {
			print(e.toString());
			return false;
		}
    }
	
	/**
	 * 设置相簿的封面照片
	 * @param albumId
	 * @param photoId
	 * @return 成功或失败
	 */
	public static boolean setAlbumCover(Long albumId, Long photoId) {
		try {
			init();
	        
	        SetAlbumCoverRequest request = new SetAlbumCoverRequest();
	        request.setStoreName(storeName);
	        request.setAlbumId(albumId);
	        request.setPhotoId(photoId);
	        SetAlbumCoverResponse response = acsClient.getAcsResponse(request);
	        print("setAlbumCover[AlbumId = " + albumId + ", photoId = " + photoId + "]: " +  response.getCode());
	        
	        return SUCCESS.equalsIgnoreCase(response.getCode());
		} catch (Exception e) {
			print(e.toString());
			return false;
		}
    }
	
	/**
	 * 批量将照片添加到相簿
	 * @param albumId
	 * @param photoIds
	 * @return 照片数量，异常则为0
	 */
	public static int addAlbumPhotos(Long albumId, List<Long> photoIds) {
		try {
			init();
	        
	        AddAlbumPhotosRequest request = new AddAlbumPhotosRequest();
	        request.setStoreName(storeName);
	        request.setAlbumId(albumId);
	        request.setPhotoIds(photoIds);
	        AddAlbumPhotosResponse response = acsClient.getAcsResponse(request);
	        List<AddAlbumPhotosResponse.Result> results = response.getResults();
	        for (AddAlbumPhotosResponse.Result result : results) {
	        	print("addAlbumPhotos: " +  result.getCode() + ", PhotoId = " + result.getId());
			}
	        print("addAlbumPhotos: Total Photos Count = " + results.size());
	        
	        return results.size();
		} catch (Exception e) {
			print(e.toString());
			return 0;
		}
    }
	/************************************ 相簿接口 end ************************************/
	
	/************************************ 人脸接口 begin ************************************/
	/**
	 * 批量删除人脸，该操作会解除人脸和照片的关系，照片不会被删除
	 * @param faceIds
	 * @return 人脸数量，异常则为0
	 */
	public static int deleteFaces(List<Long> faceIds) {
		try {
			init();
	        
	        DeleteFacesRequest request = new DeleteFacesRequest();
	        request.setStoreName(storeName);
	        request.setFaceIds(faceIds);
	        DeleteFacesResponse response = acsClient.getAcsResponse(request);
	        List<DeleteFacesResponse.Result> results = response.getResults();
	        for (DeleteFacesResponse.Result result : results) {
	        	print("deleteFaces: " +  result.getCode() + ", FaceId = " + result.getId());
			}
	        print("deleteFaces: Total Faces Count = " + results.size());
	        
	        return results.size();
		} catch (Exception e) {
			print(e.toString());
			return 0;
		}
    }
	
	/**
	 * 分批列出当前用户照片中包含的人脸
	 * @return 人脸集合，异常则为null
	 */
	public static List<Face> listFaces() {
		try {
			init();
			
	        String cursor = "0";
	        List<Face> faceList = new ArrayList<Face>();
	        while (!EOF.equalsIgnoreCase(cursor)) {
	        	ListFacesRequest request = new ListFacesRequest();
	            request.setStoreName(storeName);
	            request.setCursor(cursor);
	            request.setDirection(Direction);
	            request.setState(State);
	            request.setSize(Size);
	            ListFacesResponse response = acsClient.getAcsResponse(request);
	            List<Face> faces = response.getFaces();
	            for (Face face : faces) {
	            	faceList.add(face);
	                print("listFaces: FaceId = " + face.getId() + ", FaceCoverId = " + face.getCover().getId() 
	                		 + ", FaceCoverName = " + face.getCover().getTitle() + ", FaceName = " + face.getName() + ", PhotosCount = " + face.getPhotosCount()
	                		+ ", isMe = " + face.getIsMe() + ", RequestId = " + response.getRequestId());
	                listFacePhotos(face.getId());
	            }
	            cursor = response.getNextCursor();
	        }
	        print("listFaces: Total Faces Count = " + faceList.size());
	        
	        return faceList;
		} catch (Exception e) {
			print(e.toString());
			return null;
		}
	}
	
	/**
	 * 分批列出包含该人脸的照片
	 * @param faceId
	 * @return photoId集合，异常则为null
	 */
	public static List<Long> listFacePhotos(Long faceId) {
		try {
			init();
	        
	        String cursor = "0";
	        List<Long> photoIds = new ArrayList<Long>();
	        while (!EOF.equalsIgnoreCase(cursor)) {
	            ListFacePhotosRequest request = new ListFacePhotosRequest();
	            request.setStoreName(storeName);
	            request.setCursor(cursor);
	            request.setDirection(Direction);
	            request.setState(State);
	            request.setSize(Size);
	            request.setFaceId(faceId);
	            ListFacePhotosResponse response = acsClient.getAcsResponse(request);
	            List<ListFacePhotosResponse.Result> results = response.getResults();
	            for (ListFacePhotosResponse.Result result : results) {
	            	photoIds.add(result.getPhotoId());
	                print("listFacePhotos[FaceId = " + faceId + "]: PhotoId = " + result.getPhotoId());
	            }
	            cursor = response.getNextCursor();
	        }
	        print("listFacePhotos[FaceId = " + faceId + "]: Total Photos Count = " + photoIds.size());
	        
	        return photoIds;
		} catch (Exception e) {
			print(e.toString());
			return null;
		}
    }
	
	/**
	 * 批量将照片从人脸A移动到人脸B
	 * @param sourceFaceId
	 * @param targetFaceId
	 * @param photoIds
	 * @return 照片数量，异常则为0
	 */
	public static int moveFacePhotos(Long sourceFaceId, Long targetFaceId, List<Long> photoIds) {
		try {
			init();
	        
	        MoveFacePhotosRequest request = new MoveFacePhotosRequest();
	        request.setStoreName(storeName);
	        request.setPhotoIds(photoIds);
	        request.setSourceFaceId(sourceFaceId);
	        request.setTargetFaceId(targetFaceId);
	        MoveFacePhotosResponse response = acsClient.getAcsResponse(request);
	        List<MoveFacePhotosResponse.Result> results = response.getResults();
	        for (MoveFacePhotosResponse.Result result : results) {
	            print("moveFacePhotos[" + sourceFaceId + "-->" + targetFaceId + "]: " + result.getCode() + ", PhotoId = " + result.getId());
	        }
	        print("moveFacePhotos[" + sourceFaceId + "-->" + targetFaceId + "]:  Total Photos Count = " + results.size());
	        
	        return results.size();
		} catch (Exception e) {
			print(e.toString());
			return 0;
		}
    }
	
	/**
	 * 批量将照片从人脸中移除，本接口只改变人脸和照片的关联关系，不会删除照片
	 * @param FaceId
	 * @param photoIds
	 * @return 照片数量，异常则为0
	 */
	public static int removeFacePhotos(Long faceId, List<Long> photoIds) {
		try {
			init();
	        
	        RemoveFacePhotosRequest request = new RemoveFacePhotosRequest();
	        request.setStoreName(storeName);
	        request.setFaceId(faceId);
	        request.setPhotoIds(photoIds);
	        RemoveFacePhotosResponse response = acsClient.getAcsResponse(request);
	        List<RemoveFacePhotosResponse.Result> results = response.getResults();
	        for (RemoveFacePhotosResponse.Result result : results) {
	        	print("removeFacePhotos[FaceId = " + faceId + "]: " +  result.getCode() + ", PhotoId = " + result.getId());
			}
	        print("removeFacePhotos[FaceId = " + faceId + "]: Total Photos Count = " + results.size());
	        
	        return results.size();
		} catch (Exception e) {
			print(e.toString());
			return 0;
		}
    }
	
	/**
	 * 修改人脸名称
	 * @param FaceId
	 * @param faceName
	 * @return 成功或失败
	 */
	public static boolean renameFace(Long faceId, String faceName) {
		try {
			init();
	        
	        RenameFaceRequest request = new RenameFaceRequest();
	        request.setStoreName(storeName);
	        request.setFaceId(faceId);
	        request.setFaceName(faceName);
	        RenameFaceResponse response = acsClient.getAcsResponse(request);
	        print("renameFace[FaceId = " + faceId + ", FaceName = " + faceName + "]: " +  response.getCode());
	        
	        return SUCCESS.equalsIgnoreCase(response.getCode());
		} catch (Exception e) {
			print(e.toString());
			return false;
		}
    }
	
	/**
	 * 设置人脸的封面照片，photoId必须属于faceId，否则即使接口调用成功，但该face的Cover仍为空
	 * @param faceId
	 * @param photoId
	 * @return 成功或失败
	 */
	public static boolean setFaceCover(Long faceId, Long photoId) {
		try {
			init();
	        
	        SetFaceCoverRequest request = new SetFaceCoverRequest();
	        request.setStoreName(storeName);
	        request.setFaceId(faceId);
	        request.setPhotoId(photoId);
	        SetFaceCoverResponse response = acsClient.getAcsResponse(request);
	        print("setFaceCover[FaceId = " + faceId + ", photoId = " + photoId + ", RequestId = " + response.getRequestId() + "]: " +  response.getCode());
	        
	        return SUCCESS.equalsIgnoreCase(response.getCode());
		} catch (Exception e) {
			print(e.toString());
			return false;
		}
    }
	
	/**
	 * 合并人脸，将多个人脸下的照片合并到另外一个人脸下
	 * @param targetFaceId
	 * @param faceIds
	 * @return 待合并人脸数量，异常则为0
	 */
	public static int mergeFaces(Long targetFaceId, List<Long> faceIds) {
		try {
			init();
	        
	        MergeFacesRequest request = new MergeFacesRequest();
	        request.setStoreName(storeName);
	        request.setFaceIds(faceIds);
	        request.setTargetFaceId(targetFaceId);
	        MergeFacesResponse response = acsClient.getAcsResponse(request);
	        List<MergeFacesResponse.Result> results = response.getResults();
	        for (MergeFacesResponse.Result result : results) {
	            print("mergeFaces[" + targetFaceId + "]: " + result.getCode() + ", PhotoId = " + result.getId());
	        }
	        print("mergeFaces[" + targetFaceId + "]: Total Photos Count = " + results.size());
	        
	        return results.size();
		} catch (Exception e) {
			print(e.toString());
			return 0;
		}
    }
	
	/**
	 * 标记该人脸为使用者自己，所有人脸中只能有一个人脸为使用者自己
	 * @param faceId
	 * @return 成功或失败
	 */
	public static boolean setMe(Long faceId) {
		try {
			init();
	        
	        SetMeRequest request = new SetMeRequest();
	        request.setStoreName(storeName);
	        request.setFaceId(faceId);
	        SetMeResponse response = acsClient.getAcsResponse(request);
	        print("setMe[FaceId = " + faceId + "]: " + response.getCode());
	        
	        return SUCCESS.equalsIgnoreCase(response.getCode());
		} catch (Exception e) {
			print(e.toString());
			return false;
		}
    }
	/************************************ 人脸接口 end ************************************/
	
	/************************************ PhotoStore接口 begin ************************************/
	/**
	 * 获取已创建的PhotoStore
	 * @return PhotoStore集合，异常则为null
	 */
	public static List<PhotoStore> listPhotoStores() {
		try {
			init();
	        
	        ListPhotoStoresRequest request = new ListPhotoStoresRequest();
	        ListPhotoStoresResponse response = acsClient.getAcsResponse(request);
	        List<PhotoStore> photostoreList = response.getPhotoStores();
	        for (PhotoStore photostore : photostoreList) {
	        	print("listPhotoStores: PhotoStoreId = " + photostore.getId() + ", PhotoStoreName = " + photostore.getName());
			}
	        print("listPhotoStores: Total PhotoStores Count = " + photostoreList.size());
	        
	        return photostoreList;
		} catch (Exception e) {
			print(e.toString());
			return null;
		}
    }
	
	/**
	 * 获取指定PhotoStore的信息
	 * @param photoStoreName
	 * @return PhotoStore，异常则为null
	 */
	public static GetPhotoStoreResponse.PhotoStore getPhotoStore(String photoStoreName) {
		try {
			init();
	        
	        GetPhotoStoreRequest request = new GetPhotoStoreRequest();
	        request.setStoreName(photoStoreName);
	        GetPhotoStoreResponse response = acsClient.getAcsResponse(request);
	        GetPhotoStoreResponse.PhotoStore photostore = response.getPhotoStore();
	        String content = "getPhotoStore[PhotoStoreName = " + photostore.getName() + "]: PhotoStoreId = " + photostore.getId() 
	        		+ ", DefaultQuota = " + photostore.getDefaultQuota() + ", Buckets = ";
	        for (GetPhotoStoreResponse.PhotoStore.Bucket bucket : photostore.getBuckets()) {
	        	content += bucket.getName() + "|";
			}
	        print(content.substring(0, content.length() - 1));
	        
	        return photostore;
		} catch (Exception e) {
			print(e.toString());
			return null;
		}
    }

	/**
	 * 创建一个PhotoStore
	 * @param photoStoreName
	 * @param bucketName
	 * @param defaultQuota
	 * @return 成功或失败
	 */
	public static boolean createPhotoStore(String photoStoreName, String bucketName, Long defaultQuota) {
		try {
			init();
	        
	        CreatePhotoStoreRequest request = new CreatePhotoStoreRequest();
	        request.setStoreName(photoStoreName);
	        request.setBucketName(bucketName);
	        request.setDefaultQuota(defaultQuota);
	        CreatePhotoStoreResponse response = acsClient.getAcsResponse(request);
	        print("createPhotoStore: " + response.getCode() + ", getRequestId = " + response.getRequestId());
	        
	        return SUCCESS.equalsIgnoreCase(response.getCode());
		} catch (Exception e) {
			print(e.toString());
			return false;
		}
    }
	
	/**
	 * 删除一个PhotoStore，该操作是逻辑删除，删除后不可再用该photoStoreName重新创建，删除的photoStore不占用photoStore总数
	 * @param photoStoreName
	 * @return 成功或失败
	 */
	public static boolean deletePhotoStore(String photoStoreName) {
		try {
			init();
	        
	        DeletePhotoStoreRequest request = new DeletePhotoStoreRequest();
	        request.setStoreName(photoStoreName);
	        DeletePhotoStoreResponse response = acsClient.getAcsResponse(request);
	        print("deletePhotoStore: " + response.getCode() + ", getRequestId = " + response.getRequestId());
	        
	        return SUCCESS.equalsIgnoreCase(response.getCode());
		} catch (Exception e) {
			print(e.toString());
			return false;
		}
    }
	/************************************ PhotoStore接口 end ************************************/
	
	/************************************ 标签接口 begin ************************************/
	/**
	 * 全量列出当前用户照片的标签
	 * @return 标签集合
	 */
	public static List<Tag> listTags() {
		try {
			init();
	        
	        ListTagsRequest request = new ListTagsRequest();
	        request.setStoreName(storeName);
	        ListTagsResponse response = acsClient.getAcsResponse(request);
	        List<Tag> tagList = response.getTags();
	        for (Tag tag : tagList) {
	        	print("listTags: TagId = " + tag.getId() + ", TagName = " + tag.getName());
			}
	        print("listTags: Total Tags Count = " + tagList.size());
	        
	        return tagList;
		} catch (Exception e) {
			print(e.toString());
			return null;
		}
    }
	
	/**
	 * 分批列出当前用户照片的指定标签的照片
	 * @param tagId
	 * @return photoId集合，异常则为null
	 */
	public static List<Long> listTagPhotos(Long tagId) {
		try {
			init();
	        
	        String cursor = "0";
	        List<Long> photoIds = new ArrayList<Long>();
	        while (!EOF.equalsIgnoreCase(cursor)) {
	            ListTagPhotosRequest request = new ListTagPhotosRequest();
	            request.setStoreName(storeName);
	            request.setCursor(cursor);
	            request.setDirection(Direction);
	            request.setState(State);
	            request.setSize(Size);
	            request.setTagId(tagId);
	            ListTagPhotosResponse response = acsClient.getAcsResponse(request);
	            List<ListTagPhotosResponse.Result> results = response.getResults();
	            for (ListTagPhotosResponse.Result result : results) {
	            	photoIds.add(result.getPhotoId());
	                print("listTagPhotos[TagId = " + tagId + "]: PhotoId = " + result.getPhotoId());
	            }
	            cursor = response.getNextCursor();
	        }
	        print("listTagPhotos[TagId = " + tagId + "]: Total Photos Count = " + photoIds.size());
	        
	        return photoIds;
		} catch (Exception e) {
			print(e.toString());
			return null;
		}
    }
	/************************************ 标签接口 end ************************************/
	
	/************************************ 时光接口 begin ************************************/
	/**
	 * 分批列出当前用户的时光相簿
	 * @return 时光集合，异常则为null
	 */
	public static List<Moment> listMoments() {
		try {
			init();
	        
	        String cursor = "0";
	        List<Moment> momentList = new ArrayList<Moment>();
	        while (!EOF.equalsIgnoreCase(cursor)) {
	            ListMomentsRequest request = new ListMomentsRequest();
	            request.setStoreName(storeName);
	            request.setCursor(cursor);
	            request.setDirection(Direction);
	            request.setState(State);
	            request.setSize(Size);
	            ListMomentsResponse response = acsClient.getAcsResponse(request);
	            List<Moment> moments = response.getMoments();
	            for (Moment moment : moments) {
	            	momentList.add(moment);
	            	print("listMoments: MomentId = " + moment.getId() + ", MomentLocationName = " + moment.getLocationName()
	            			+ ", MomentMtime = " + sdf2.format(moment.getMtime()));
				}
	            cursor = response.getNextCursor();
	        }
	        print("listMoments: Total Moments Count = " + momentList.size());
	        
	        return momentList;	
		} catch (Exception e) {
			print(e.toString());
			return null;
		}
    }
	
	/**
	 * 分批列出时光相簿中的照片
	 * @param momentId
	 * @return photoId集合，异常则为null
	 */
	public static List<Long> listMomentPhotos(Long momentId) {
		try {
			init();
	        
	        String cursor = "0";
	        List<Long> photoIds = new ArrayList<Long>();
	        while (!EOF.equalsIgnoreCase(cursor)) {
	            ListMomentPhotosRequest request = new ListMomentPhotosRequest();
	            request.setStoreName(storeName);
	            request.setCursor(cursor);
	            request.setDirection(Direction);
	            request.setState(State);
	            request.setSize(Size);
	            request.setMomentId(momentId);
	            ListMomentPhotosResponse response = acsClient.getAcsResponse(request);
	            List<ListMomentPhotosResponse.Result> results = response.getResults();
	            for (ListMomentPhotosResponse.Result result : results) {
	            	photoIds.add(result.getPhotoId());
	            	print("listMomentPhotos[MomentId = " + momentId + "]: PhotoId = " + result.getPhotoId());
	            }
	            cursor = response.getNextCursor();
	        }
	        print("listMomentPhotos[MomentId = " + momentId + "]: Total Photos Count = " + photoIds.size());
	        
	        return photoIds;
		} catch (Exception e) {
			print(e.toString());
			return null;
		}
    }
	/************************************ 时光接口 end ************************************/
	
	/************************************ 搜索接口 end ************************************/
	/**
	 * 搜索照片并分页返回结果，可根据tag、文件名、时间等关键字搜索
	 * @param keyWord
	 * @return 照片集合，异常则为null
	 */
	public static List<Photo> searchPhotos(String keyWord) {
		try {
			init();
	        
	        SearchPhotosRequest request = new SearchPhotosRequest();
	        request.setStoreName(storeName);
	        request.setKeyword(keyWord);
	        request.setPage(1);
	        request.setSize(50);
	        SearchPhotosResponse response = acsClient.getAcsResponse(request);
	        List<Photo> photos = response.getPhotos();
	        for (Photo photo : photos) {
	            print("searchPhotos[Keyword = " + keyWord + "]: PhotoId = " + photo.getId() + ", PhotoName = " + photo.getTitle());
	        }
	        print("searchPhotos[Keyword = " + keyWord + "]: Total Photos Count = " + photos.size());
	        
	        return photos;
		} catch (Exception e) {
			print(e.toString());
			return null;
		}
    }
	/************************************ 搜索接口 end ************************************/
	
	/************************************ 用户接口 end ************************************/
	/**
	 * 获取照片库的存储配额
	 * @return 配额，异常则为null
	 */
	public static Quota getQuota() {
		try {
			init();
			
			GetQuotaRequest request = new GetQuotaRequest();
			request.setStoreName(storeName);
			GetQuotaResponse response = acsClient.getAcsResponse(request);
			print("getQuota: " + response.getCode() + ", totalQuota = " + response.getQuota().getTotalQuota()
					+ ", usedQuota = " + response.getQuota().getUsedQutoa());
			
			return response.getQuota();
		} catch (Exception e) {
			print(e.toString());
			return null;
		}
    }
	
	/**
	 * 设置照片库的存储配额
	 * @param totalQuota
	 * @return 成功或失败
	 */
	public static boolean setQuota(Long totalQuota) {
		try {
			init();
			
			SetQuotaRequest request = new SetQuotaRequest();
			request.setStoreName(storeName);
			request.setTotalQuota(totalQuota);
			SetQuotaResponse response = acsClient.getAcsResponse(request);
			print("setQuota: " + response.getCode() + ", totalQuota = " + totalQuota);
			
			return SUCCESS.equalsIgnoreCase(response.getCode());
		} catch (Exception e) {
			print(e.toString());
			return false;
		}
    }
	/************************************ 用户接口 end ************************************/
	
	public static String getPara(String action) {
		StringBuffer sb = new StringBuffer();
		sb.append("https://cloudphoto.cn-shanghai.aliyuncs.com/");
		sb.append("?Action=" + action);
		sb.append("&Cursor=0");
		sb.append("&Direction=forward");
		sb.append("&Size=" + Size);
		sb.append("&State=all");
		sb.append("&StoreName=" + storeName);
		
		
		System.out.println("====" + sb);
		String para = encodePara(sb.toString());
		System.out.println("====" + para);
//		String StringToSign=
//				 "get" + "&" +
//				 percentEncode（"/"） + "&" +
//				 percentEncode（CanonicalizedQueryString）
//		String str = "http://cloudphoto.cn-shanghai.aliyuncs.com/?Format=json&Version=2017-07-11"
//				+ "&Signature=vpEEL0zFHfxXYzSFV0n7%2FZiFL9o%3D 
//		    &SignatureMethod=Hmac-SHA1
//		    &SignatureNonce=9166ab59-f445-4005-911d-664c1570df0f
//		    &SignatureVersion=1.0
//		    &Action=ListPhotos
//		    &AccessKeyId=tkHh5O7431CgWayx  
//		    &Timestamp=2017-07-29T09%3A22%3A32Z
//		    &SecurityToken=<SecurityTokenFromBusinessServer>
//		String str = "https://cloudphoto.cn-shanghai.aliyuncs.com/?Action=ListAlbums&Cursor=0&Direction=forward&Size=10&State=all&StoreName=storeName";
//		String strParam = "";
//		System.out.println("====" + str);
//		strParam = URLEncoder.encode("abadf adf* \"");
//		System.out.println("====" + strParam);
//		strParam = strParam.replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
//		System.out.println("====" + strParam);
//		strParam = getUTF8XMLString("\" a b个");
//		System.out.println("====" + strParam);
		return para;
	}
	
	public static final String encodePara(String text) {
	    return URLEncoder.encode(text).replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
	}
	
}
