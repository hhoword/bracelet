package com.huayu.bracelet.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.huayu.bracelet.BaseApplication;
import com.huayu.bracelet.R;
import com.huayu.bracelet.activity.IOnDataListener;
import com.huayu.bracelet.http.HttpUtil;
import com.huayu.bracelet.vo.UpdateInfo;

public class UpdateManager {
	/* 下载中 */  
	private static final int DOWNLOAD = 1;  
	/* 下载结束 */  
	private static final int DOWNLOAD_FINISH = 2;  
	/* 保存解析的XML信息 */  
	HashMap<String, String> mHashMap;  
	/* 下载保存路径 */  
	private String mSavePath;  
	/* 记录进度条数量 */  
	private int progress;  
	/* 是否取消更新 */  
	private boolean cancelUpdate = false;  

	private Context mContext;  
	/* 更新进度条 */  
	private ProgressBar mProgress;  
	private Dialog mDownloadDialog;  

	private Handler mHandler = new Handler()  
	{  
		public void handleMessage(Message msg)  
		{  
			switch (msg.what)  
			{  
			// 正在下载  
			case DOWNLOAD:  
				// 设置进度条位置  
				mProgress.setProgress(progress);  
				break;  
			case DOWNLOAD_FINISH:  
				// 安装文件  
				installApk();  
				break;  
			default:  
				break;  
			}  
		};  
	};  

	public UpdateManager(Context context){  
		this.mContext = context;  
	}  

	/** 
	 * 检测软件更新 
	 */  
	public void checkUpdate()  
	{  
		
		HttpUtil httpUtil = new HttpUtil();
		httpUtil.getCheckUpdate(new IOnDataListener<UpdateInfo>() {
			
			@Override
			public void onDataResult(UpdateInfo t) {
				// TODO Auto-generated method stub
				if(t!=null&&t.getCode()==200){
					int versionCode = BaseApplication.getVersionCode(mContext);  
					mHashMap = new HashMap<String, String>();
					mHashMap.put("name", t.getData().getVer());
					mHashMap.put("url", t.getData().getUrl());
					int serviceCode = Integer.valueOf(mHashMap.get("name"));  
					// 版本判断  
					if (serviceCode > versionCode) {  
						showNoticeDialog();  
					} 
				}
			}
		});
	}  

	/** 
	 * 显示软件更新对话框 
	 */  
	private void showNoticeDialog()  
	{  
		// 构造对话框  
		AlertDialog.Builder builder = new Builder(mContext);  
		builder.setTitle("应用更新");  
		builder.setMessage("应用更新啦，立马下载看看"); 
		// 更新  
		builder.setPositiveButton("立刻更新", new OnClickListener()  
		{  
			@Override  
			public void onClick(DialogInterface dialog, int which)  
			{  
				dialog.dismiss();  
				// 显示下载对话框  
				showDownloadDialog();  
			}  
		});  
		// 稍后更新  
		builder.setNegativeButton("下次再说", new OnClickListener()  
		{  
			@Override  
			public void onClick(DialogInterface dialog, int which)  
			{  
				dialog.dismiss();  
			}  
		});  
		Dialog noticeDialog = builder.create();  
		noticeDialog.show();  
	}  

	/** 
	 * 显示软件下载对话框 
	 */  
	private void showDownloadDialog()  
	{  
		// 构造软件下载对话框  
		AlertDialog.Builder builder = new Builder(mContext);  
		builder.setTitle("应用更新");  
		// 给下载对话框增加进度条  
		final LayoutInflater inflater = LayoutInflater.from(mContext);  
		View v = inflater.inflate(R.layout.view_softupdate_progress, null);  
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);  
		builder.setView(v);  
		// 取消更新  
		builder.setNegativeButton("取消", new OnClickListener()  
		{  
			@Override  
			public void onClick(DialogInterface dialog, int which)  
			{  
				dialog.dismiss();  
				// 设置取消状态  
				cancelUpdate = true;  
			}  
		});  
		mDownloadDialog = builder.create();  
		mDownloadDialog.show();  
		// 现在文件  
		downloadApk();  
	}  

	/** 
	 * 下载apk文件 
	 */  
	private void downloadApk()  
	{  
		// 启动新线程下载软件  
		new downloadApkThread().start();  
	}  

	/** 
	 * 下载文件线程 
	 *  
	 * @author coolszy 
	 *@date 2012-4-26 
	 *@blog http://blog.92coding.com 
	 */  
	private class downloadApkThread extends Thread  
	{  
		@Override  
		public void run()  
		{  
			try  
			{  
				// 判断SD卡是否存在，并且是否具有读写权限  
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))  
				{  
					// 获得存储卡的路径  
					String sdpath = Environment.getExternalStorageDirectory() + "/";  
					mSavePath = sdpath + "download";  
					URL url = new URL(HttpUtil.url+"/"+mHashMap.get("url"));  
					// 创建连接  
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
					conn.connect();  
					// 获取文件大小  
					int length = conn.getContentLength();  
					// 创建输入流  
					InputStream is = conn.getInputStream();  

					File file = new File(mSavePath);  
					// 判断文件目录是否存在  
					if (!file.exists())  
					{  
						file.mkdir();  
					}  
					File apkFile = new File(mSavePath, "bracelet"+mHashMap.get("name")+".apk");  
					FileOutputStream fos = new FileOutputStream(apkFile);  
					int count = 0;  
					// 缓存  
					byte buf[] = new byte[1024];  
					// 写入到文件中  
					do  
					{  
						int numread = is.read(buf);  
						count += numread;  
						// 计算进度条位置  
						progress = (int) (((float) count / length) * 100);  
						// 更新进度  
						mHandler.sendEmptyMessage(DOWNLOAD);  
						if (numread <= 0)  
						{  
							// 下载完成  
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);  
							break;  
						}  
						// 写入文件  
						fos.write(buf, 0, numread);  
					} while (!cancelUpdate);// 点击取消就停止下载.  
					fos.close();  
					is.close();  
				}  
			} catch (MalformedURLException e)  
			{  
				e.printStackTrace();  
			} catch (IOException e)  
			{  
				e.printStackTrace();  
			}  
			// 取消下载对话框显示  
			mDownloadDialog.dismiss();  
		}  
	};  

	/** 
	 * 安装APK文件 
	 */  
	private void installApk()  
	{  
		File apkfile = new File(mSavePath, mHashMap.get("name"));  
		if (!apkfile.exists())  
		{  
			return;  
		}  
		// 通过Intent安装APK文件  
		Intent i = new Intent(Intent.ACTION_VIEW); 
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");  
		mContext.startActivity(i);  
		android.os.Process.killProcess(android.os.Process.myPid());
	}  
}
