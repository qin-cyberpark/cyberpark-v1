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

import scala.Console;


/**
 * @author Qin
 *
 */
public class TestPatchService extends BaseJunit4Test {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Resource // 自动注入,默认按名称
	private PatchService patchSrv;

	@Test
	public void UploadUsageBillFile() throws ParseException {
		if (patchSrv == null)
			fail("Not yet implemented");

		String choursFile = "chours28082015.csv";
		String vosFile = "VOS20150803-20150902.csv";
		String callpulsFile = "callplus31082015.csv";
		Path path = Paths.get("D:/works/CyberPark/ori/usage detail data/" + choursFile);
		String contentType = "text/plain";
		byte[] content = null;
		try {
			content = Files.readAllBytes(path);
		} catch (Exception e) {
			fail("can not read file");
		}
		MultipartFile result = new MockMultipartFile(choursFile, choursFile, contentType, content);

		User user = new User();
		user.setId(9999);
		if(patchSrv.UploadUsageBillFile(result, user) == null){
			fail("create failed");
		}
	}
}