package test.com.tm.broadband.service;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;

import javax.annotation.Resource;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import test.com.tm.broadband.base.BaseJunit4Test;

import com.tm.broadband.model.*;
import com.tm.broadband.service.PatchService;
import com.tm.broadband.util.PatchUtility;

import scala.Console;


/**
 * @author Qin
 *
 */
public class TestPatchUtility extends BaseJunit4Test {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}


	@Test
	public void GetUsageBill() throws ParseException {
		String folder = "D:/works/CyberPark/ori/usage detail data/";
		String choursFile = "chours28082015.csv";
		String vosFile = "VOS20150803-20150902.csv";
		String callpulsFile = "callplus31082015.csv";
		UsageBill bill;
		

		//VOS
		bill = PatchUtility.ParseBill(folder + vosFile);
		if(bill == null){
			fail("VOS failed: NULL");
		}
		if(!bill.getType().equals("VOS")){
			fail("VOS type failed:" + bill.getType());
		}
		if(bill.getCallingOriginalNumbers().size() == 0){
			fail("VOS record failed: 0 records");
		}
		
		//CHOURS
		bill = PatchUtility.ParseBill(folder + choursFile);
		if(bill == null){
			fail("chours failed: NULL");
		}
		if(!bill.getType().equals("CHOURS")){
			fail("chours type failed:" + bill.getType());
		}
		if(bill.getCallingOriginalNumbers().size() == 0){
			fail("chours record failed: 0 records");
		}
		
		//CALLPLUS
		bill = PatchUtility.ParseBill(folder + callpulsFile);
		if(bill == null){
			fail("CALLPLUS failed: NULL");
		}
		if(!bill.getType().equals("CALLPLUS")){
			fail("CALLPLUS type failed:" + bill.getType());
		}
		if(bill.getCallingOriginalNumbers().size() == 0){
			fail("CALLPLUS record failed: 0 records");
		}
	}
}