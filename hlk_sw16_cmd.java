package com.slydiman.hlk_sw16_cmd;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class hlk_sw16_cmd {
	public static void main(String[] args) {
		
		if (args.length != 2 && args.length != 4) {
			System.err.println("usage: hlk_sw16_cmd <ip> <port> [<relay_#> <value>]");
			System.err.println("relay_#: 0..15 or all");
			System.err.println("value: 1/0 or on/off");
			return;
		}
		
		String ip = args[0];
		int port = Integer.parseInt(args[1]);
		
		byte cmd[] = {'#','*',0x01,0x30,0x01, 0x01,'*','#'};
		
		int response_len;
		
		if (args.length == 2) {
			response_len = 19;
			cmd[2] = (byte)(0x4F);
		}else{
			int num = -1;
			if (!"all".equals(args[2])) {
				num = Integer.parseInt(args[2]);
			}
			
			if (num < -1 || num > 15) {
				System.err.println("hlk_sw16_cmd: invalid <relay_#>");
				return;
			}
			
			boolean val = ("1".equals(args[3]) || "on".equals(args[3]));
			
			if (num == -1) {	// all
				response_len = 19;
				cmd[2] = (byte)(val ? 0x1F : 0x1E);
			} else {
				response_len = 8;
				cmd[2] = (byte)(0x30 + num);
				cmd[3] = (byte)(val ? 0x30 : 0x20);
			}
		}
		cmd[5] = (byte)(cmd[2] + cmd[3] + cmd[4]);		
		
		System.out.println("hlk_sw16_cmd: connecting...");
		Socket socket = null;
		try {
			socket = new Socket(ip, port);
			BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
			System.out.println("hlk_sw16_cmd: sending...");
			out.write(cmd);
			out.flush();
			
			System.out.println("hlk_sw16_cmd: reading...");
			BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
			byte [] data = new byte[response_len];
		    int len = in.read(data);
		    
			if (len == 8)
			{
				if((data[0]=='a')&&(data[1]=='a')&&(data[6]=='b')&&(data[7]=='b')) {
					System.out.println("Relay # " + (data[2] - 0x30) + ": " + (data[3] == 0x30 ? "ON" : (data[3] == 0x20 ? "OFF" : "UNKNOWN")));
				} else {
					System.err.println("Error response [8]");
				}
			}
			else if (len == 19)
			{
				int sum = 0;
				for (int i=1; i<=16; ++i) {
					sum += data[i];
				}
				if ((data[0]=='#') && (data[18]=='*') && ((sum & 0xFF) == data[17]))
				{
					for (int i=0; i<16; ++i) {
						System.out.println("Relay # " + ((i+1) & 0xF) + ": " + (data[i+1] == 0x02 ? "ON" : (data[i+1] == 0x01 ? "OFF" : "UNKNOWN(0x"+Integer.toHexString(data[i+1])+")")));
					}
				} else {
					System.err.println("Error response [19]");
				}
			} else {
				System.err.println("Error response ["+len+"]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				socket = null;
			}
		}
	}
}
