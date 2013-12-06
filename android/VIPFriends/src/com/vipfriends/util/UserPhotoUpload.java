package com.vipfriends.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.vipfriends.MyApplication;
import com.vipfriends.network.MultipartRequest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class UserPhotoUpload implements OnClickListener {
	private final static String TAG = "UserPhotoUpload";
	private static int RROM_LOCAL = 1;
	private static int RROM_CAMERA = 2;
	private static int PHOTO_ZOOM = 3;
	private Activity myActivity;

	// 相机按钮弹出的对话框
	private AlertDialog alertDialog = null;

	public UserPhotoUpload(Activity myActivity) {
		this.myActivity = myActivity;
		init();
	}

	/**
	 * 初始化方法实现
	 */
	public void init() {
		// mUserImageView.setOnClickListener(this);
	}

	/**
	 * 控件点击事件实现
	 */
	@Override
	public void onClick(View v) {
		ShowPickDialog();
	}

	/**
	 * 选择提示对话框
	 */
	public void ShowPickDialog() {
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				myActivity);
		alertDialogBuilder.setTitle("修改头像");
		final String[] items = { "拍照上传", "本地上传" };

		alertDialogBuilder.setItems(items,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						alertDialog.dismiss();
						String select = items[which];
						if (select.equals("拍照上传")) {
							getFromCamera();
						} else if (select.equals("本地上传")) {
							getFromLocal();
						}
					}
				});

		alertDialog = alertDialogBuilder.show();

	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		DLog.d("Temp", "startPhotoZoom uri -->> " + uri.toString());
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		myActivity.startActivityForResult(intent, PHOTO_ZOOM);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	public void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			File imageFile = getUserPhotoFile();
			try {
				saveMyBitmap(photo, imageFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			uploadBitmap(imageFile);
		}
	}

	/**
	 * 从相机拍照
	 */
	public void getFromCamera() {
		if (CommonUtil.checkSDcard()) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(getUserPhotoFile()));
			myActivity.startActivityForResult(intent, RROM_CAMERA);
		} else {
			Toast.makeText(myActivity, "您的手机没有安装SD卡，请安装SD卡后重试！",
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 从本地获取照片
	 */
	public void getFromLocal() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);

		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		myActivity.startActivityForResult(intent, RROM_LOCAL);
	}

	/**
	 * bitmap转成jpg
	 * @param mBitmap
	 * @throws IOException
	 */
	public void saveMyBitmap(Bitmap mBitmap, File file) throws IOException {
		if (file == null) {
			return;
		}
		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 上传照片
	 * 
	 * @param bitmap
	 *            要上传的bitmap
	 */
	private void uploadBitmap(File imageFile) {

		if(!imageFile.exists()){
			try {
				throw new FileNotFoundException("the upload file is not exists:" + imageFile.getAbsolutePath());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return;
		}

		RequestQueue mQueue = Volley
				.newRequestQueue(MyApplication.getContext());

		mQueue.add(new MultipartRequest("http://192.168.201.75:8000/upload",
				new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						DLog.i(TAG,
								"uploadBitmap error = " + error.getMessage());
					}
				}, new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						DLog.i(TAG, "uploadBitmap onResponse = " + arg0);
					}
				}, imageFile, "userFace.jpg"));

		mQueue.start();
	}

	/**
	 * 回收照片
	 * 
	 * @param photo
	 *            要回收的照片
	 */
	private void recycleBitmap(Bitmap photo) {
		if (photo != null && photo.isRecycled()) {
			photo.recycle();
		}
	}

	// 标志上传是否成功
	private boolean mFirstUploadSucess;

	public boolean getFirstUploadSucess() {
		return mFirstUploadSucess;
	}

	public void setFirstUploadSucess(boolean firstUpload) {
		this.mFirstUploadSucess = firstUpload;
	}

	/**
	 * 获取图片的路径
	 * 
	 * @return 图片路径
	 */
	public File getUserPhotoFile() {
		File imageFile = null;
		try {
			if (CommonUtil.checkSDcard()) {
				File file = new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath() + FileService.IMAGE_CHILD_DIR);
				DLog.i(TAG, "getUserPhotoFile:" + file.getAbsolutePath());
				if (!file.exists()) {
					file.mkdirs();
				}
				imageFile = new File(file, "image_userFace.jpg");
				DLog.i(TAG,
						"getUserPhotoFile:imageFile:"
								+ imageFile.getAbsolutePath());
			}
			return imageFile;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imageFile;
	}

	/**
	 * 保存图片到缓存
	 * 
	 * @param bitmap
	 *            要操作的图片
	 * @return
	 */
	// public Boolean savePhotoToCache(Bitmap bitmap) {
	// String fileName = null;
	// boolean result = false;
	// Directory directory = FileService.getDirectory(FileService.IMAGE_DIR);
	// if (null != directory) {
	// try {
	// String userPath = getUserName();
	//
	//
	// if (null != userPath) {
	// fileName = userPath + FileService.CACHE_EXT_NAME_IMAGE;
	// byte[] fileContent = Bitmap2Bytes(bitmap);
	// result = FileService.saveToSDCard(directory, fileName, fileContent);
	// return result;
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// return result;
	// }

	/**
	 * bitmap to byte数组
	 * 
	 * @param bm
	 * @return
	 */
	private byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

}
