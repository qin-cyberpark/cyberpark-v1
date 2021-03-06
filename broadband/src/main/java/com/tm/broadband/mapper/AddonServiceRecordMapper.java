package com.tm.broadband.mapper;

import com.tm.broadband.model.AddonServiceRecord;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AddonServiceRecordMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table t_addon_service_record
	 * @mbggenerated  Wed Dec 02 03:34:08 NZDT 2015
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table t_addon_service_record
	 * @mbggenerated  Wed Dec 02 03:34:08 NZDT 2015
	 */
	int insert(AddonServiceRecord record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table t_addon_service_record
	 * @mbggenerated  Wed Dec 02 03:34:08 NZDT 2015
	 */
	AddonServiceRecord selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table t_addon_service_record
	 * @mbggenerated  Wed Dec 02 03:34:08 NZDT 2015
	 */
	List<AddonServiceRecord> selectAll();

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table t_addon_service_record
	 * @mbggenerated  Wed Dec 02 03:34:08 NZDT 2015
	 */
	int updateByPrimaryKey(AddonServiceRecord record);
}