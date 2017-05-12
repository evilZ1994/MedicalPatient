package com.example.r2d2.medicalpatient.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 实现对象与字节数组互转的工具类
 * Created by Lollipop on 2017/5/6.
 */

public class ObjectBytesUtil {

    public static byte[] ObjectToBytes(Object object) throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(object);
        byte[] bytes = bo.toByteArray();
        bo.close();
        oo.close();
        return bytes;
    }

    public static Object BytesToObject(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
        ObjectInputStream oi = new ObjectInputStream(bi);

        Object object = oi.readObject();
        bi.close();
        oi.close();
        return object;
    }
}
