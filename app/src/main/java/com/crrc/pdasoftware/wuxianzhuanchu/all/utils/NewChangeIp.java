package com.crrc.pdasoftware.wuxianzhuanchu.all.utils;


import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class NewChangeIp {
    private static DatagramSocket datagramSocket;
    private static char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    //16进制查询指令
    public static String hexSearch = "5a4c000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

    static {
        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public String newChangeIpRe(String ip, int port) {

        DatagramPacket datagramPacket = null;
        String hexReceive = "";
        try {
            byte[] searchTyte = toBytes(hexSearch);
            //-------------------------查询指令--------------------------------------
            datagramPacket = new DatagramPacket
                    (searchTyte, searchTyte.length, InetAddress.getByName(ip), port);
            //发送连接数据包
            datagramSocket.send(datagramPacket);
            datagramSocket.setSoTimeout(500);
            //-------------------------查询指令--------------------------------------


            //-------------------------查询指令---返回值-----------------------------------

            //接受工装返回的信息
            byte[] dataReceive = new byte[1024];
            DatagramPacket receive = new DatagramPacket(dataReceive, dataReceive.length);
            datagramSocket.receive(receive);
            //读取数据
            byte[] dataRe = receive.getData();
            hexReceive = bytesToHex(dataRe);
            String receiveData = new String(dataRe, 0, receive.getLength(), "UTF-8");
            System.out.println("第一次，查询指令---未转换的返回值：" + hexReceive);
            System.out.println("查询指令---返回值返回值：" + receiveData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hexReceive;
    }

    public void newChangeIp(String ip, String newip, int port, Context ctx) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatagramPacket datagramPacket = null;
                byte[] searchTyte = toBytes(hexSearch);
                try {
                    //-------------------------查询指令--------------------------------------
                    datagramPacket = new DatagramPacket
                            (searchTyte, searchTyte.length, InetAddress.getByName(ip), port);
                    //发送连接数据包
                    datagramSocket.send(datagramPacket);
                    datagramSocket.setSoTimeout(2000);
                    //-------------------------查询指令--------------------------------------


                    //-------------------------查询指令---返回值-----------------------------------

                    //接受工装返回的信息
                    byte[] dataReceive = new byte[1024];
                    DatagramPacket receive = new DatagramPacket(dataReceive, dataReceive.length);
                    datagramSocket.receive(receive);
                    //读取数据
                    byte[] dataRe = receive.getData();
                    String hexReceive = bytesToHex(dataRe);
                    String receiveData = new String(dataRe, 0, receive.getLength(), "UTF-8");
                    System.out.println("第一次，查询指令---返回值：" + hexReceive);
                    System.out.println("查询指令---返回值未转换的返回值：" + receiveData);
                    //-------------------------查询指令---返回值-----------------------------------


                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    String start = "ffff00000a0001010a0001961064106400";

//                    System.out.println( hexReceive.substring( hexReceive.indexOf( start ) ) );

                    //ip
                    String[] iparr = ip.split("\\.");
                    String ipstring = "";
                    for (int i = 0; i < iparr.length; i++) {
                        ipstring += "" + intToHex(Integer.valueOf(iparr[i]));
                    }


                    //String newip = newip;
                    String[] newiparr = newip.split("\\.");
                    String newipstring = "";
                    System.out.println(newiparr.length + "--===");
                    for (int i = 0; i < newiparr.length; i++) {
                        newipstring += "" + intToHex(Integer.valueOf(newiparr[i]));
                        System.out.println(intToHex(Integer.valueOf(newiparr[i])) + "---------");
                        //newipstring += intToHex( Integer.valueOf( newiparr[i] ) );
                    }
                    System.out.println("-----newipstring------" + newipstring);


                    String ipori = "";
                    System.out.println("ipstring11-------" + ipstring.toLowerCase());
                    if (hexReceive.length() > 14) {
                        if (hexReceive.indexOf(ipstring.toLowerCase()) != -1) {
                            String[] strsiparr = hexReceive.split(ipstring.toLowerCase());
                            ipori = strsiparr[1];
                        }

                    } else {
                        return;
                    }

                    String resultip = "5a4c02" + newipstring.toLowerCase() + ipori;
                    System.out.println("----resultip-------" + resultip);
                    System.out.println("----ipstring22-------" + ipstring.toLowerCase());
                    System.out.println("----ipori-------" + ipori);

                    //-------------------------修改ip指令--------------------------------------
                    byte[] searchTyte2 = toBytes(resultip);
                    datagramPacket = new DatagramPacket(searchTyte2, searchTyte2.length, InetAddress.getByName(ip), port);
                    //发送连接数据包
                    datagramSocket.send(datagramPacket);
                    datagramSocket.setSoTimeout(2000);
                    //-------------------------修改ip指令--------------------------------------


                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //-------------------------再次发查询指令--------------------------------------
                    datagramPacket = new DatagramPacket
                            (searchTyte, searchTyte.length, InetAddress.getByName(ip), port);
                    //发送连接数据包
                    datagramSocket.send(datagramPacket);
                    datagramSocket.setSoTimeout(2000);
                    //-------------------------再次发查询指令--------------------------------------


                    //-------------------------再次发查询指令--返回值------------------------------------
                    //接受工装返回的信息
                    byte[] dataReceive3 = new byte[1024];
                    DatagramPacket receive3 = new DatagramPacket(dataReceive, dataReceive.length);
                    datagramSocket.receive(receive3);
                    //读取数据
                    byte[] dataRe3 = receive3.getData();
                    String hexReceive3 = bytesToHex(dataRe3);
                    String receiveData3 = new String(dataRe3, 0, receive3.getLength(), "UTF-8");
                    //hexReceive3为发送修改指令后 返回的数据
                    System.out.println("修改后：" + receiveData3);
                    //-------------------------再次发查询指令--返回值------------------------------------

                    System.out.println("再次发查询指令--返回值:" + hexReceive3);
                    if (hexReceive3.indexOf(newipstring.toLowerCase()) != -1) {
                        System.out.println("最终成功");
                        Looper.prepare();
                        Toast.makeText(ctx, "IP配置成功，正在重启设备", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    } else {
                        System.out.println("最终失败");
                        Looper.prepare();
                        Toast.makeText(ctx, "IP配置失败，请再次配置", Toast.LENGTH_LONG).show();
                        Looper.loop();

                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    //-------------------------发重启指令--------------------------------------
                    String againspring = "5a4c020a000264ffffff00c0a80101c0a801031064106400383838383838383838\n" +
                            "3828544878c7c20b5a4c44455630303031000003051400000000000001000000\n" +
                            "080804043139322e3136382e312e330000000000000000000000000000000000\n" +
                            "00000c3c00500000005ab6e65a4c010000054600000904000000000a04000000\n" +
                            "0000000000000000000000000000000000000000000000000000000000000000\n" +
                            "000000000000000000";
                    byte[] searchTytelast = toBytes(againspring);
                    datagramPacket = new DatagramPacket(searchTytelast, searchTytelast.length, InetAddress.getByName(newip), port);
                    //发送连接数据包
                    datagramSocket.send(datagramPacket);
                    datagramSocket.setSoTimeout(2000);
                    //-------------------------发重启指令--------------------------------------


//                    String change1 = hexReceive3.replace("5a4c01", "");
//                    String change2 = change1.substring(0, change1.indexOf("ffff00000a0001010a0001961064106400"));
//                    System.out.println("16进制的返回值：" + hexReceive3);
//                    System.out.println("未转换的返回值：" + receiveData3);
//
//                    System.out.println(newipstring.toLowerCase());
//                    System.out.println(change2);
//                    if (change2.equals(newipstring.toLowerCase())) {
//                        System.out.println("----------ip配置成功");
//                        //修改成功
//                    } else {
//                        //修改失败
//                        System.out.println("----------ip配置失败");
//
//                    }


//                    return hexReceive;
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                return "fail";

            }
        }).start();


    }

    /**
     * byte[] to 16进制
     */
    public static String bytesToHex(byte[] bytes) {
        // 一个byte为8位，可用两个十六进制位标识
        char[] buf = new char[bytes.length * 2];
        int a = 0;
        int index = 0;
        for (byte b : bytes) { // 使用除与取余进行转换
            if (b < 0) {
                a = 256 + b;
            } else {
                a = b;
            }

            buf[index++] = HEX_CHAR[a / 16];
            buf[index++] = HEX_CHAR[a % 16];
        }

        return new String(buf);
    }

    /**
     * 将16进制字符串转换为byte[]
     */
    public static byte[] toBytes(String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }

    /**
     * int值转换为ip
     */
    public static String intToIp(int addr) {
        return ((addr & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF));
    }

    public static Long ipToLong(String ipString) {
        Long[] ip = new Long[4];
        int pos1 = ipString.indexOf(".");
        int pos2 = ipString.indexOf(".", pos1 + 1);
        int pos3 = ipString.indexOf(".", pos2 + 1);
        ip[0] = Long.parseLong(ipString.substring(0, pos1));
        ip[1] = Long.parseLong(ipString.substring(pos1 + 1, pos2));
        ip[2] = Long.parseLong(ipString.substring(pos2 + 1, pos3));
        ip[3] = Long.parseLong(ipString.substring(pos3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    public static String intToHex(int n) {
        //StringBuffer s = new StringBuffer();
        StringBuilder sb = new StringBuilder(8);
        String a;
        char[] b = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        while (n != 0) {
            sb = sb.append(b[n % 16]);
            n = n / 16;
        }
        a = sb.reverse().toString();
        if (a.length() == 1) {
            a = "0" + a;
        }
        if (a.length() == 0) {
            a = "00";
        }
        return a;
    }

}
