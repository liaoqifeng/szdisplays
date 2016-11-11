package com.koch.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

public class CloneUtil {
	public static Serializable cloneSerializable(Serializable src) {
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(src);
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in = new ObjectInputStream(byteIn);
			Serializable dest = (Serializable) in.readObject();
			in.close();
			byteIn.close();
			out.close();
			return dest;
		} catch (Exception e) {
			throw new RuntimeException("clone error", e);
		}
	}

	public static void writeSerializable(Serializable src, String fileName) {
		try {
			File f = new File(fileName);
			OutputStream fileout = new FileOutputStream(f);
			ObjectOutputStream out = new ObjectOutputStream(fileout);
			out.writeObject(src);
			out.close();
			fileout.close();
		} catch (Exception e) {
			throw new RuntimeException("clone error", e);
		}
	}

	public static Serializable readSerializable(String fileName) {
		try {
			File f = new File(fileName);
			InputStream fileIn = new FileInputStream(f);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			Serializable dest = (Serializable) in.readObject();
			in.close();
			fileIn.close();
			return dest;
		} catch (Exception e) {
			throw new RuntimeException("clone error", e);
		}

	}
}
