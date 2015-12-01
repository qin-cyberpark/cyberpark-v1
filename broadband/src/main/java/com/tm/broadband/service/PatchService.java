package com.tm.broadband.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tm.broadband.mapper.AddonServiceRecordMapper;
import com.tm.broadband.mapper.CallingRecordMapper;
import com.tm.broadband.mapper.UploadedFileMapper;
import com.tm.broadband.model.AddonServiceRecord;
import com.tm.broadband.model.CallingRecord;
import com.tm.broadband.model.UploadedFile;
import com.tm.broadband.model.UsageBill;
import com.tm.broadband.model.User;
import com.tm.broadband.util.PatchUtility;
import com.tm.broadband.util.TMUtils;

@Service
public class PatchService {
	private UploadedFileMapper uploadedFileMapper;
	private CallingRecordMapper callingRecordMapper;
	private AddonServiceRecordMapper addonSerRecordMapper;

	@Value("${app.UploadedFilePath}")
	private String uploadedFilePath;

	@Autowired
	public PatchService(UploadedFileMapper uploadedFileMapper, CallingRecordMapper callingRecordMapper,
			AddonServiceRecordMapper addonSerRecordMapper) {
		this.uploadedFileMapper = uploadedFileMapper;
		this.callingRecordMapper = callingRecordMapper;
		this.addonSerRecordMapper = addonSerRecordMapper;
	}

	public PatchService() {
	}

	@Transactional
	public UsageBill UploadUsageBillFile(MultipartFile csvFile, User user) {
		if (csvFile.isEmpty()) {
			return null;
		}
		String fileName = csvFile.getOriginalFilename();
		String extension = TMUtils.getExtension(fileName);

		UploadedFile uf = new UploadedFile();
		uf.setExtension(extension);
		uf.setOriFileName(fileName);
		uf.setUploadDate(new Date());
		uf.setUploadBy(user.getId());

		this.uploadedFileMapper.insert(uf);
		int file_id = uf.getId();
		String save_path = this.uploadedFilePath + File.separator + file_id + extension;
		try {
			csvFile.transferTo(new File(save_path));
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			return null;
		}
		
		// get bill
		UsageBill usageBill = PatchUtility.ParseBill(save_path);

		if (usageBill == null) {
			return null;
		}
				
		//set file type
		uf.setType(usageBill.getType());
		this.uploadedFileMapper.updateById(uf);

		// calling record
		Set<String> oriNums = usageBill.getCallingOriginalNumbers();
		String oriNum;
		// iterate number
		Iterator<String> oriNumIter = oriNums.iterator();
		while (oriNumIter.hasNext()) {
			oriNum = oriNumIter.next();
			List<CallingRecord> records = usageBill.getCallingRecords(oriNum);

			// iterate record
			Iterator<CallingRecord> recIter = records.iterator();
			while (recIter.hasNext()) {
				CallingRecord cr = recIter.next();
				cr.setFileid(file_id);

				// insert
				this.callingRecordMapper.insert(cr);
			}
		}
		
		//add-on service record
		oriNums = usageBill.getAddonServiceOriginalNumbers();
		// iterate number
		oriNumIter = oriNums.iterator();
		while (oriNumIter.hasNext()) {
			oriNum = oriNumIter.next();
			List<AddonServiceRecord> records = usageBill.getAddonServiceRecords(oriNum);

			// iterate record
			Iterator<AddonServiceRecord> recIter = records.iterator();
			while (recIter.hasNext()) {
				AddonServiceRecord asr = recIter.next();
				asr.setFileid(file_id);

				// insert
				this.addonSerRecordMapper.insert(asr);
			}
		}

		return usageBill;
	}
}