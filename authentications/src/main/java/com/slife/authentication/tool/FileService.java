package com.slife.authentication.tool;

import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 操作文件
 * @author JingY
 */
public class FileService {

	boolean isOk=false;
	String state=Environment.getExternalStorageState();
	FileOutputStream outputStream=null;
	File rootFile=Environment.getExternalStorageDirectory();//获取sdcard的根路径
	File file=null;

	/**
	 * 存放在SDCard的根目录中
	 * @param fileName 文件名称
	 * @param data	 数据流
	 * @return boolean
	 */
	public boolean SaveFileToSdCardRoot(String fileName,byte[] data){
		/*
         * 表示sdcard挂载在手机上，并且可以读写
         */
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			file=new File(rootFile,fileName);
			try {
				outputStream=new FileOutputStream(file);
				outputStream.write(data, 0, data.length);
				isOk=true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if (outputStream!=null) {
					try {
						outputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return isOk;
	}
	/**
	 * 自定义文件夹下面
	 * @param name  文件夹名称
	 * @param fileName 文件名称
	 * @param data	数据流
	 * @return boolean
	 */
	public boolean SaveToSdCardDir(String name,String fileName,byte[] data){
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			file=new File(rootFile.getAbsoluteFile()+"/"+name+"/");
			if (!file.exists()) {
				file.mkdirs();
			}
			try {
				outputStream=new FileOutputStream(new File(file, fileName));
				outputStream.write(data, 0, data.length);
				isOk=true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if (outputStream!=null) {
					try {
						outputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return isOk;
	}
	/**
	 * 用于读取SdCard的数据
	 * @param name 文件夹名
	 * @param fileName 文件名
	 * @return string
	 */
	public String ReadFromSdCard(String name,String fileName){
		FileInputStream inputStream=null;//读取数据流
		ByteArrayOutputStream bOutputStream=new ByteArrayOutputStream();//存放读取的数据流
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			file=new File(rootFile.getAbsoluteFile()+"/"+name+"/");
			File file2=new File(file,fileName);
			@SuppressWarnings("unused")
			int length=0;
			byte[] data=new byte[1024];
			if (file2.exists()) {
				try {
					inputStream=new FileInputStream(file2);
					while ((length=inputStream.read(data))!=-1) {
						bOutputStream.write(data, 0, data.length);
					}
					return new String(bOutputStream.toByteArray());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					if (bOutputStream!=null) {
						try {
							bOutputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return null;
	}
	/**
	 * 自定义文件夹下面
	 * @param name  文件夹名称
	 * @param fileName 文件名称
	 * @param data	数据流
	 * @return boolean
	 */
	public boolean SaveToSdCardDirs(String path,String name,String fileName,byte[] data){
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			file=new File(path+"/"+name+"/");
			if (!file.exists()) {
				file.mkdirs();
			}
			try {
				outputStream=new FileOutputStream(new File(file, fileName));
				outputStream.write(data, 0, data.length);
				isOk=true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if (outputStream!=null) {
					try {
						outputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return isOk;
	}
	/**
	 * 对文件进行分类
	 * @param fileName 文件名
	 * @param date 数据流
	 */
	public void SaveFileToSdCardBySuff(String fileName,byte[] date){
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			if (fileName.endsWith(".mp3")) {
				file=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
			}
			if (fileName.endsWith(".jpg")||fileName.endsWith(".png")||fileName.endsWith(".gif")) {
				file=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			}
			if (fileName.endsWith(".mp4")||fileName.endsWith(".avi")||fileName.endsWith(".3gp")) {
				file=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
			}else{
				file=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			}
			try {
				outputStream=new FileOutputStream(new File(file, fileName));
				outputStream.write(date, 0, date.length);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if (outputStream!=null) {
					try {
						outputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}
	/**
	 * 删除Sdcard文件
	 * @param name 文件夹名称
	 * @param fileName 文件名称
	 * @return boolean
	 */
	public boolean DeleteFileFromSdcard(String name,String fileName){
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			File file=new File(rootFile.getAbsoluteFile()+"/"+name);
			if (file.exists()) {
				file.delete();
			}
		}
		return isOk;
	}
	/**
	 * 获取文件目录
	 * @return
	 */
	public String getFilePath(){
		return file.getAbsoluteFile().toString();
	}
}
