/*
Navicat MySQL Data Transfer

Source Server         : mysql_242
Source Server Version : 50718
Source Host           : 192.168.16.242:3306
Source Database       : bbd

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2018-01-08 17:56:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for bid_list
-- ----------------------------
DROP TABLE IF EXISTS `bid_list`;
CREATE TABLE `bid_list` (
  `listing_id` int(8) NOT NULL,
  `debt_id` int(8) DEFAULT NULL,
  `amount` decimal(8,0) DEFAULT NULL,
  `price_for_sale_rate` decimal(6,0) DEFAULT NULL,
  `credit_code` varchar(5) DEFAULT NULL,
  `bid_time` datetime DEFAULT NULL,
  PRIMARY KEY (`listing_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bid_list
-- ----------------------------

-- ----------------------------
-- Table structure for debt_info
-- ----------------------------
DROP TABLE IF EXISTS `debt_info`;
CREATE TABLE `debt_info` (
  `debt_id` int(11) NOT NULL,
  `seller` varchar(100) DEFAULT NULL,
  `status_id` int(2) DEFAULT NULL,
  `lender` varchar(100) NOT NULL,
  `bid_date_time` varchar(100) DEFAULT NULL,
  `owing_number` int(2) DEFAULT NULL,
  `owing_principal` decimal(6,0) DEFAULT NULL,
  `owing_interest` varchar(5) DEFAULT NULL,
  `days` int(2) DEFAULT NULL,
  `price_for_sale_rate` decimal(6,0) DEFAULT NULL,
  `price_for_sale` decimal(6,0) DEFAULT NULL,
  `preference_degree` decimal(5,0) DEFAULT NULL,
  `listing_id` int(9) NOT NULL,
  `credit_code` varchar(5) DEFAULT NULL,
  `listing_amount` decimal(6,0) DEFAULT NULL,
  `listing_mouths` int(3) DEFAULT NULL,
  `listing_time` datetime DEFAULT NULL,
  `listing_rate` int(20) DEFAULT NULL,
  `past_due_number` int(2) DEFAULT NULL,
  `current_credit_code` varchar(5) NOT NULL,
  `allowance_radio` decimal(5,0) DEFAULT NULL,
  `PastDueDay` int(2) DEFAULT NULL,
  PRIMARY KEY (`debt_id`,`current_credit_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of debt_info
-- ----------------------------

-- ----------------------------
-- Table structure for loan_info
-- ----------------------------
DROP TABLE IF EXISTS `loan_info`;
CREATE TABLE `loan_info` (
  `first_bid_time` datetime DEFAULT NULL,
  `last_bid_time` datetime DEFAULT NULL,
  `lender_count` int(11) DEFAULT NULL,
  `auditing_time` datetime DEFAULT NULL,
  `remain_funding` decimal(10,0) DEFAULT NULL,
  `dead_line_time_or_remind_time_str` varchar(200) NOT NULL,
  `credit_code` varchar(5) DEFAULT NULL,
  `listing_id` int(10) NOT NULL,
  `amount` decimal(10,0) DEFAULT NULL,
  `mounths` int(2) DEFAULT NULL,
  `current_rate` double(6,0) DEFAULT NULL,
  `borrow_name` varchar(200) DEFAULT NULL,
  `gender` tinyint(2) DEFAULT NULL,
  `education_degree` varchar(50) DEFAULT NULL,
  `graduate_school` varchar(100) DEFAULT NULL,
  `stydy_style` varchar(100) DEFAULT NULL,
  `age` int(3) DEFAULT NULL,
  `success_count` int(3) DEFAULT NULL,
  `waste_count` int(3) DEFAULT NULL,
  `cancel_count` int(3) DEFAULT NULL,
  `failed_count` int(3) DEFAULT NULL,
  `normal_count` int(3) DEFAULT NULL,
  `overdue_less_count` int(3) DEFAULT NULL,
  `overdue_more_count` int(3) DEFAULT NULL,
  `owing_principal` decimal(8,0) DEFAULT NULL,
  `owing_amount` decimal(8,0) DEFAULT NULL,
  `amount_to_receive` decimal(8,0) DEFAULT NULL,
  `first_success_borrow_time` datetime DEFAULT NULL,
  `register_time` datetime DEFAULT NULL,
  `certificate_validate` int(3) DEFAULT NULL,
  `nciic_identity_check` int(3) DEFAULT NULL,
  `phone_validate` int(3) DEFAULT NULL,
  `video_validate` int(3) DEFAULT NULL,
  `credit_validate` int(3) DEFAULT NULL,
  `educate_validate` int(3) DEFAULT NULL,
  `last_success_borrow_time` datetime DEFAULT NULL,
  `highest_principal` decimal(10,0) DEFAULT NULL,
  `highest_debt` decimal(10,0) DEFAULT NULL,
  `total_principal` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`listing_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of loan_info
-- ----------------------------
