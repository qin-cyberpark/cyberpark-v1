package com.tm.broadband.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Date;

import com.tm.broadband.model.UsageBill;
import com.tm.broadband.util.test.Console;

public class PatchUtility {
	public static UsageBill ParseBill(String filePath) {
		String line = "";
		String header = "";
		boolean firstLine = true;
		UsageBill usageBill = null;
		try {
			File csvFile = new File(filePath); // CSV File
			InputStreamReader isr = new InputStreamReader(new FileInputStream(csvFile), "GBK");
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				if (firstLine) {
					header = line;
					usageBill = new UsageBill(header);
					if(!usageBill.isValid()){
						break;
					}else{
						firstLine = false;
						continue;
					}
				}else{
					usageBill.AddCallingRecord(line);
					usageBill.AddAddonServiceRecord(line);
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(!usageBill.isValid()){
			return null;
		}else{
			return usageBill;
		}
	}
}