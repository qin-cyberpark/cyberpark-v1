package com.tm.broadband.model;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.tm.broadband.util.TMUtils;

import scala.collection.mutable.HashTable;

enum UsageBillType {
	Unknown, VOS, CallPlus, Chours
}

class BaseUsageBillHeader {
	protected String header;
	protected HashMap<String, Integer> fields = null;
	protected UsageBillType billType = UsageBillType.Unknown;

	public BaseUsageBillHeader(String header) {
		this.header = header;
	}

	public UsageBillType getType() {
		return billType;
	}

	public boolean isValid() {
		if (fields == null) {
			return false;
		}

		String[] arr = header.split(",");
		int len = arr.length;
		for (int i = 0; i < len; i++) {
			if (fields.containsKey(arr[i])) {
				fields.put(arr[i], i);
			}
		}
		Iterator<Entry<String, Integer>> iter = fields.entrySet().iterator();
		while (iter.hasNext()) {
			if (iter.next().getValue() < 0) {
				// not include all the necessary fields
				return false;
			}
			;
		}

		return validate();
	}

	protected boolean isInclude() {
		if (fields == null) {
			return false;
		}

		return true;
	}

	protected boolean validate() {
		return false;
	}

	protected CallingRecord parseCallingRecord(String line) {
		return null;
	}

	protected AddonServiceRecord parseAddonServiceRecord(String line) {
		return null;
	}

	protected BaseUsageBillHeader getNextType() {
		return null;
	}
}

class VOSUsageBillHeader extends BaseUsageBillHeader {

	public VOSUsageBillHeader(String header) {
		super(header);
		// TODO Auto-generated constructor stub
		billType = UsageBillType.VOS;
		fields = new HashMap<String, Integer>() {
			{
				put("主叫号码", -1);
				put("被叫号码", -1);
				put("起始时间", -1);
				put("通话时长", -1);
				put("计费时长", -1);
				put("通话费用", -1);
				put("话费成本", -1);
				put("通话类型", -1);
				put("地区前缀", -1);
			}
		};
	}

	protected boolean validate() {
		return true;
	}

	protected BaseUsageBillHeader getNextType() {
		return new CallPlusUsageRecordHeader(header);
	}

	protected CallingRecord parseCallingRecord(String line) {
		CallingRecord cr = new CallingRecord();
		String arr[] = line.split(",");
		int arrLen = arr.length;
		String tmp = null;

		// ori_number
		int idx = fields.get("主叫号码");
		if (arrLen > idx) {
			cr.setOriNumber(arr[idx].trim());
		} else {
			return null;
		}

		// call_start
		idx = fields.get("起始时间");
		if (arrLen > idx) {
			tmp = arr[idx].trim();
			if (!"".equals(tmp)) {
				cr.setCallStart(TMUtils.parseDate2YYYYMMDD(tmp));
			}
		} else {
			return null;
		}

		// duration
		idx = fields.get("计费时长");
		if (arrLen > idx) {
			tmp = arr[idx].trim();
			if (!"".equals(tmp)) {
				cr.setDuration(Integer.parseInt(tmp));
			}
		} else {
			return null;
		}

		// cost / charge
		idx = fields.get("通话费用");
		if (arrLen > idx) {
			tmp = arr[idx].trim();
			if (!"".equals(tmp)) {
				cr.setCost(Double.parseDouble(tmp));
				cr.setCharge(Double.parseDouble(tmp));
			}
		} else {
			return null;
		}

		// des_number
		idx = fields.get("被叫号码");
		if (arrLen > idx) {
			cr.setDesNumber(arr[idx].trim());
		} else {
			return null;
		}

		// type
		idx = fields.get("通话类型");
		if (arrLen > idx) {
			switch (arr[idx].trim().toLowerCase()) {
			// international and national only
			case "国际长途":
			case "international":
				tmp = "I";
				break;
			case "国内长途":
			case "national":
				tmp = "N";
				break;
			default:
				return null;
			}
			cr.setType(tmp);
		} else {
			return null;
		}
		
		//area_prefix
		idx = fields.get("地区前缀");
		if (arrLen > idx) {
			cr.setAreaPrefix(arr[idx].trim());
		} else {
			return null;
		}

		return cr;
	}
}

class CallPlusUsageRecordHeader extends BaseUsageBillHeader {
	public CallPlusUsageRecordHeader(String header) {
		super(header);
		// TODO Auto-generated constructor stub
		billType = UsageBillType.CallPlus;
		fields = new HashMap<String, Integer>() {
			{
				put("Origin Number", -1);
				put("Destination Number", -1);
				put("Description", -1);
				put("Date/Time", -1);
				put("Call Length (seconds)", -1);
				put("Call Cost (NZD)", -1);
				put("Type", -1);
			}
		};
	}

	protected boolean validate() {
		return true;
	}

	protected BaseUsageBillHeader getNextType() {
		return new ChoursUsageRecordHeader(header);
	}

	protected CallingRecord parseCallingRecord(String line) {
		CallingRecord cr = new CallingRecord();
		if(line.contains("\"")){
			// Truncate middle part, generally referred to description
			String lineTempMiddle = line.substring(line.indexOf("\"")+1, line.lastIndexOf("\""));
			if(lineTempMiddle.contains(",")){
				// Truncate front part
				String lineTempFront = line.substring(0, line.indexOf("\""));
				// Truncate back part
				String lineTempBack = line.substring(line.lastIndexOf("\"")+1, line.length());
				// Process middle part
				lineTempMiddle = lineTempMiddle.substring(0, lineTempMiddle.indexOf(","))+"."+lineTempMiddle.substring(lineTempMiddle.indexOf(",")+1, lineTempMiddle.length());
				line = lineTempFront.concat(lineTempMiddle.concat(lineTempBack));
			}
		}
		
		String arr[] = line.split(",");
		int arrLen = arr.length;
		String tmp = null;

		// ori_number
		int idx = fields.get("Origin Number");
		if (arrLen > idx) {
			cr.setOriNumber(arr[idx].trim());
		} else {
			return null;
		}

		// call_start
		idx = fields.get("Date/Time");
		if (arrLen > idx) {
			tmp = arr[idx].trim();
			if (!"".equals(tmp)) {
			    tmp = tmp.substring(0, tmp.lastIndexOf(" "));
				cr.setCallStart(TMUtils.parseDateDDMMYYYYHHMMSS(tmp));
			}
		} else {
			return null;
		}

		// duration
		idx = fields.get("Call Length (seconds)");
		if (arrLen > idx) {
			tmp = arr[idx].trim();
			if (!"".equals(tmp)) {
				cr.setDuration(Integer.parseInt(tmp));
			}
		} else {
			return null;
		}

		// cost & charge
		idx = fields.get("Call Cost (NZD)");
		if (arrLen > idx) {
			tmp = arr[idx].trim().replace("$", "");
			if (!"".equals(tmp)) {
				cr.setCost(Double.parseDouble(tmp));
				cr.setCharge(Double.parseDouble(tmp));
			}
		} else {
			return null;
		}

		// des_number
		idx = fields.get("Destination Number");
		if (arrLen > idx) {
			cr.setDesNumber(arr[idx].trim());
		} else {
			return null;
		}

		// type
		idx = fields.get("Type");
		if (arrLen > idx) {
			switch (arr[idx].trim().toUpperCase()) {
			case "I":
				tmp = "I";
				break;
			case "S":
				tmp = "N";
				break;
			case "L":
				tmp = "L";
				break;
			case "M":
			case "MG":
				tmp = "M";
				break;
			default:
				return null;
			}
			cr.setType(tmp);
		} else {
			return null;
		}
		
		//description
		idx = fields.get("Description");
		if (arrLen > idx) {
			cr.setDescription(arr[idx].trim());
		} else {
			return null;
		}

		return cr;
	}
}

class ChoursUsageRecordHeader extends BaseUsageBillHeader {
	public ChoursUsageRecordHeader(String header) {
		super(header);
		// TODO Auto-generated constructor stub
		billType = UsageBillType.Chours;
		fields = new HashMap<String, Integer>() {
			{
				put("StatementDate", -1);
				put("RecordType", -1);
				put("ClearServiceID", -1);
				put("DateFrom", -1);
				put("DateTo", -1);
				put("ChargeDate", -1);
				put("ChargeTime", -1);
				put("Duration", -1);
				put("OOTID", -1);
				put("BillingDescription", -1);
				put("AmountExcl", -1);
				put("AmountIncl", -1);
				put("Juristiction", -1);
				put("PhoneCalled", -1);
			}
		};
	}

	protected boolean validate() {
		return true;
	}

	protected CallingRecord parseCallingRecord(String line) {
		CallingRecord cr = new CallingRecord();
		String arr[] = line.split(",");
		int arrLen = arr.length;
		boolean hasDesNum = false;
		String tmp = null;

		// ori_number
		int idx = fields.get("RecordType");
		if (arrLen > idx) {
			tmp = arr[idx].trim();
			if (!tmp.equals("T1") && !tmp.equals("T3")) {
				return null;
			}
			
			if(tmp.equals("T1")){
				hasDesNum = true;
			}
		}

		// ori_number
		idx = fields.get("ClearServiceID");
		if (arrLen > idx) {
			cr.setOriNumber(arr[idx].trim());
		} else {
			return null;
		}

		// call_start
		if (arrLen > fields.get("ChargeDate") && arrLen > fields.get("ChargeTime")) {
			tmp = arr[fields.get("ChargeDate")].trim() + " " + arr[fields.get("ChargeTime")].trim();
			if (!"".equals(tmp)) {
				cr.setCallStart(TMUtils.parseDateDDMMYYYYHHMMSS(tmp));
			}
		} else {
			return null;
		}

		// duration
		idx = fields.get("Duration");
		if (arrLen > idx) {
			tmp = arr[idx].trim();
			if (!"".equals(tmp)) {
				cr.setDuration(Integer.parseInt(tmp));
			}
		} else {
			return null;
		}

		// cost & charge
		idx = fields.get("AmountIncl");
		if (arrLen > idx) {
			tmp = arr[idx].trim();
			if (!"".equals(tmp)) {
				cr.setCost(Double.parseDouble(tmp));
				cr.setCharge(Double.parseDouble(tmp));
			}
		} else {
			return null;
		}

		//des_number
		if(hasDesNum){
			idx = fields.get("PhoneCalled");
			if (arrLen > idx) {
				cr.setDesNumber(arr[idx].trim());
			} else {
				return null;
			}
		}

		// type
		idx = fields.get("Juristiction");
		if (arrLen > idx) {
			switch (arr[idx].trim().toUpperCase()) {
			case "I":
			case "N":
			case "O":
			case "L":
				tmp = arr[idx].trim().toUpperCase(); break;
			default:
				return null;
			}
			cr.setType(tmp);
		} else {
			return null;
		}

		//description
		idx = fields.get("BillingDescription");
		if (arrLen > idx) {
			cr.setDescription(arr[idx].trim());
		} else {
			return null;
		}
		
		return cr;
	}
	
	protected AddonServiceRecord parseAddonServiceRecord(String line) {
		AddonServiceRecord asr = new AddonServiceRecord();
		String arr[] = line.split(",");
		int arrLen = arr.length;
		boolean hasDesNum = false;
		String tmp = null;

		//description
		int idx = fields.get("BillingDescription");
		tmp = arr[idx].trim();
		if (arrLen > idx) {
			switch(tmp){
				case "Call restrict with no Directory Access nat Res": break;
				case "Caller Display Monthly Charge per line Res": break;
				case "Call waiting nat Res": break;
				case "Faxability Monthly Rental Res": break;
				case "Smart Bundle package": break;
				default:return null;
			}
			asr.setDescription(tmp);
		} else {
			return null;
		}
		
		// ori_number
		idx = fields.get("ClearServiceID");
		if (arrLen > idx) {
			asr.setOriNumber(arr[idx].trim());
		} else {
			return null;
		}

		// date from
		idx = fields.get("DateFrom");
		if (arrLen > idx) {
			tmp = arr[idx].trim();
			if (!"".equals(tmp)) {
				asr.setDateFrom(TMUtils.parseDate1YYYYMMDD(tmp));
			}
		} else {
			return null;
		}
		
		// date to
		idx = fields.get("DateTo");
		if (arrLen > idx) {
			tmp = arr[idx].trim();
			if (!"".equals(tmp)) {
				asr.setDateTo(TMUtils.parseDate1YYYYMMDD(tmp));
			}
		} else {
			return null;
		}

		// cost & charge
		idx = fields.get("AmountIncl");
		if (arrLen > idx) {
			tmp = arr[idx].trim();
			if (!"".equals(tmp)) {
				asr.setCost(Double.parseDouble(tmp));
				asr.setCharge(Double.parseDouble(tmp));
			}
		} else {
			return null;
		}
		
		return asr;
	}
}

public class UsageBill {
	private BaseUsageBillHeader billHeader;
	private HashMap<String, List<CallingRecord>> callingRecords = new HashMap<String, List<CallingRecord>>();
	private HashMap<String, List<AddonServiceRecord>> addonSrvRecords = new HashMap<String, List<AddonServiceRecord>>();

	public UsageBill(String header) {
		billHeader = new VOSUsageBillHeader(header);
		while (billHeader != null && !billHeader.isValid()) {
			billHeader = billHeader.getNextType();
		}
	}

	public boolean isValid() {
		return billHeader != null;
	}

	public String getType() {
		if (billHeader == null) {
			return "UNKNOWN";
		}
		switch (billHeader.getType()) {
		case VOS:
			return "VOS";
		case CallPlus:
			return "CALLPLUS";
		case Chours:
			return "CHOURS";
		default:
			return "UNKNOWN";
		}
	}

	public void AddCallingRecord(String line) {
		if (billHeader == null) {
			return;
		}
		CallingRecord cr = billHeader.parseCallingRecord(line);
		if (cr != null) {
			String oriNum = cr.getOriNumber();
			if(!callingRecords.containsKey(oriNum)){
				ArrayList<CallingRecord> list = new ArrayList<CallingRecord>();
				list.add(cr);
				callingRecords.put(oriNum, list);
			}else{
				callingRecords.get(oriNum).add(cr);
			}
		}
	}

	public void AddAddonServiceRecord(String line) {
		if (billHeader == null) {
			return;
		}
		AddonServiceRecord asr = billHeader.parseAddonServiceRecord(line);
		if (asr != null) {
			String oriNum = asr.getOriNumber();
			if(!addonSrvRecords.containsKey(oriNum)){
				ArrayList<AddonServiceRecord> list = new ArrayList<AddonServiceRecord>();
				list.add(asr);
				addonSrvRecords.put(oriNum, list);
			}else{
				addonSrvRecords.get(oriNum).add(asr);
			}
		}
	}
	
	public Set<String> getCallingOriginalNumbers(){
		return callingRecords.keySet();
	}
	public List<CallingRecord> getCallingRecords(String oriNum) {
		return callingRecords.get(oriNum);
	}
	public Set<String> getAddonServiceOriginalNumbers(){
		return addonSrvRecords.keySet();
	}
	public List<AddonServiceRecord> getAddonServiceRecords(String oriNum) {
		return addonSrvRecords.get(oriNum);
	}
}