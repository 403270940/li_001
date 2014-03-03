package com.forFun.entry;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import com.fun.utils.Device;
import com.fun.utils.IDeviceProcesser;

public class Entry {
	public static List<Device> deviceList = new ArrayList<Device>();
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void startGetSession() throws Exception{
		for(Device device : deviceList){
			Class c =  Class.forName("com.fun.utils."+device.getType().toUpperCase());
			Constructor con=c.getConstructor(Device.class);
			IDeviceProcesser deviceProcessor = (IDeviceProcesser) con.newInstance(device);
			deviceProcessor.getActiveSessions();
			deviceProcessor.releaseSession();
		}
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		Device ilo3 = new Device("ILO4","10.239.45.30","dcm","35554199");
		Device ilo4 = new Device("iLo4","10.239.45.110","test","usertest");
		deviceList.add(ilo3);
		deviceList.add(ilo4);
		startGetSession();
	}

}
