package com.autobid.dao;

import com.autobid.model.LoanInfo;
import java.util.List;

public interface LoanInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table loan_info
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table loan_info
     *
     * @mbggenerated
     */
    int insert(LoanInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table loan_info
     *
     * @mbggenerated
     */
    LoanInfo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table loan_info
     *
     * @mbggenerated
     */
    List<LoanInfo> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table loan_info
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(LoanInfo record);
}