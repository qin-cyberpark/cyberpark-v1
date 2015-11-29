/**
 * 
 */
package test.com.tm.broadband.service;

import static org.junit.Assert.*;

import java.text.ParseException;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.tm.broadband.quartz.*;

import test.com.tm.broadband.base.BaseJunit4Test;

/**
 * @author Qin
 *
 */
public class TestInvoiceService extends BaseJunit4Test{

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

    @Resource  //自动注入,默认按名称  
	private CustomerCreateInvoicePDFEachMonth invPerMonth;
 
    @Test
	public void CustomerCreateInvoicePDFEachMonth() throws ParseException {
		if(invPerMonth == null)
			fail("Not yet implemented");
		
		invPerMonth.createNextInvoicePDF();
	
	}

}
