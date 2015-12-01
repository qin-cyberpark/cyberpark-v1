package com.tm.broadband.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.tm.broadband.model.UploadedFile;
import com.tm.broadband.model.User;
import com.tm.broadband.service.PatchService;
import com.tm.broadband.util.TMUtils;
import com.tm.broadband.util.test.Console;

@Controller
public class PatchController {

	private PatchService patchService;

	@Autowired
	public PatchController(PatchService patchService) {
		this.patchService = patchService;
	}

	
	@RequestMapping("/broadband-user/patch/uploadfile/view/{pageNo}/{status}/{billing_type}")
	public String toUploadFile(Model model
			, @PathVariable("pageNo") int pageNo
			, @PathVariable("status") String status
			, @PathVariable("billing_type") String billing_type){
		return "broadband-user/manual-manipulation/uploaded-file-view";
	}
	
	// Upload File
	@RequestMapping(value = "/broadband-user/patch/uploadfile/csv/upload", method = RequestMethod.POST)
	public String uploadCSVFile(Model model
			, @RequestParam("csv_file") MultipartFile csvFile
			, HttpServletRequest req) {
		User user = (User) req.getSession().getAttribute("userSession");
		if(this.patchService.UploadUsageBillFile(csvFile, user) != null){
			return "broadband-user/manual-manipulation/uploaded-file-view";
		}
		return null;
	}
}