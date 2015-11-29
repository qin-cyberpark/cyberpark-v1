
package com.mycompany.hr.definitions;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import com.mycompany.hr.schemas.HolidayRequest;
import com.mycompany.hr.schemas.ObjectFactory;
	
		
/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebService(name = "HumanResource", targetNamespace = "http://mycompany.com/hr/definitions")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface HumanResource {


    /**
     * 
     * @param holidayRequest
     */
    @WebMethod(operationName = "Holiday")
    @Oneway
    public void holiday(
        @WebParam(name = "HolidayRequest", targetNamespace = "http://mycompany.com/hr/schemas", partName = "HolidayRequest")
        HolidayRequest holidayRequest);

}
