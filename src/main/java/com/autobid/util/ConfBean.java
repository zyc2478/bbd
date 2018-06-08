package com.autobid.util;

@SuppressWarnings("deprecation,unused")
public class ConfBean {

    private String bidMode;
    private String bidByTime;
    private String timeInterval;
    private String redisHost;
    private String redisPort;
    private String hostName;
    private String debtMix;
    private String debtOverdueSwitch;
    private String debtSaleRate;
    private String debtMinPrice;
    private String debtPriceLimit;
    private String debtPreferLimit;
    private String debtOverdueLimit;
    private String debtCreditLimit;
    private String debtGroups;
    private String logToDb;
    private String minBidAmount;
    private String bidLevelAmount;
    private String bbdGroups;
    private String multiple;
    private String moreMultiple;
    private String miniMultiple;
    private String mediumMultiple;
    private String creditRange;
    private String creditLimit;
    private String gender;
    private String amountBegin;
    private String amountEnd;
    private String amountMrate;
    private String amountFrate;
    private String totalLimit;
    private String owingLimit;
    private String owingMrate;
    private String owingFrate;
    private String debtMrate;
    private String debtFrate;
    private String certificateValidate;
    private String overdueShortLimit;
    private String overdueRate;
    private String overdueMrate;
    private String overdueFrate;
    private String shortMdays;
    private String longMdays;
    private String shortFdays;
    private String longFdays;
    private String minMage;
    private String maxMage;
    private String minFage;
    private String maxFage;
    private String beginLimit;
    private String successLimit;
    private String normalLimit;
    private String nscountMrate;
    private String nscountFrate;
    private String runInterval;
    private String code;
    private String openId;
    private String token;
    private String refreshToken;
    private String expireDays;
    private String refreshTokenExpired;
    private String initFlag;
    private String initDate;
    private String tokenInit;
    private String refreshTokenInit;

    public ConfBean() {}

    public String getBidByTime() { return bidByTime;}

    public void setBidByTime(String bidByTime) { this.bidByTime = bidByTime; }

    public String getTimeInterval() { return timeInterval; }

    public void setTimeInterval(String timeInterval) { this.timeInterval = timeInterval; }

    public String getHostName() { return hostName; }

    public void setHostName(String hostName) { this.hostName = hostName; }

    public String getDebtGroups() {
        return debtGroups;
    }

    public void setDebtGroups(String debtGroups) { this.debtGroups = debtGroups; }

    public String getDebtPreferLimit() {
        return debtPreferLimit;
    }

    public void setDebtPreferLimit(String debtPreferLimit) {
        this.debtPreferLimit = debtPreferLimit;
    }

    public String getDebtOverdueLimit() {
        return debtOverdueLimit;
    }

    public void setDebtOverdueLimit(String debtOverdueLimit) {
        this.debtOverdueLimit = debtOverdueLimit;
    }

    public String getDebtMix() {
        return debtMix;
    }

    public void setDebtMix(String debtMix) {
        this.debtMix = debtMix;
    }

    public String getBidMode() {
        return bidMode;
    }

    public void setBidMode(String bidMode) {
        this.bidMode = bidMode;
    }

    public String getDebtMinPrice() {
        return debtMinPrice;
    }

    public void setDebtMinPrice(String debtMinPrice) {
        this.debtMinPrice = debtMinPrice;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public String getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(String redisPort) {
        this.redisPort = redisPort;
    }

    public String getDebtOverdueSwitch() {
        return debtOverdueSwitch;
    }

    public void setDebtOverdueSwitch(String debtOverdueSwitch) {
        this.debtOverdueSwitch = debtOverdueSwitch;
    }

    public String getDebtSaleRate() {
        return debtSaleRate;
    }

    public void setDebtSaleRate(String debtSaleRate) {
        this.debtSaleRate = debtSaleRate;
    }

    public String getDebtPriceLimit() {
        return debtPriceLimit;
    }

    public void setDebtPriceLimit(String debtPriceLimit) {
        this.debtPriceLimit = debtPriceLimit;
    }

    public String getDebtCreditLimit() {
        return debtCreditLimit;
    }

    public void setDebtCreditLimit(String debtCreditLimit) {
        this.debtCreditLimit = debtCreditLimit;
    }

    public String getLogToDb() { return logToDb; }

    public void setLogToDb(String logToDb) { this.logToDb = logToDb; }

    public String getMinBidAmount() {
        return minBidAmount;
    }

    public void setMinBidAmount(String minBidAmount) {
        this.minBidAmount = minBidAmount;
    }

    public String getBidLevelAmount() {
        return bidLevelAmount;
    }

    public void setBidLevelAmount(String bidLevelAmount) {
        this.bidLevelAmount = bidLevelAmount;
    }

    public String getBbdGroups() { return bbdGroups; }

    public void setBbdGroups(String bbdGroups) { this.bbdGroups = bbdGroups; }

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public String getMoreMultiple() {
        return moreMultiple;
    }

    public void setMoreMultiple(String moreMultiple) {
        this.moreMultiple = moreMultiple;
    }

    public String getMiniMultiple() {
        return miniMultiple;
    }

    public void setMiniMultiple(String miniMultiple) {
        this.miniMultiple = miniMultiple;
    }

    public String getMediumMultiple() {
        return mediumMultiple;
    }

    public void setMediumMultiple(String mediumMultiple) {
        this.mediumMultiple = mediumMultiple;
    }

    public String getCreditRange() { return creditRange; }

    public void setCreditRange(String creditRange) { this.creditRange = creditRange; }

    public String getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(String creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public String getCertificateValidate() {  return certificateValidate; }

    public void setCertificateValidate(String certificateValidate) {  this.certificateValidate = certificateValidate; }

    public String getAmountBegin() {
        return amountBegin;
    }

    public void setAmountBegin(String amountBegin) {
        this.amountBegin = amountBegin;
    }

    public String getAmountEnd() {
        return amountEnd;
    }

    public void setAmountEnd(String amountEnd) {
        this.amountEnd = amountEnd;
    }

    public String getAmountMrate() {
        return amountMrate;
    }

    public void setAmountMrate(String amountMrate) {
        this.amountMrate = amountMrate;
    }

    public String getAmountFrate() {
        return amountFrate;
    }

    public void setAmountFrate(String amountFrate) {
        this.amountFrate = amountFrate;
    }

    public String getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(String totalLimit) {
        this.totalLimit = totalLimit;
    }

    public String getOwingLimit() {
        return owingLimit;
    }

    public void setOwingLimit(String owingLimit) {
        this.owingLimit = owingLimit;
    }

    public String getOwingMrate() {
        return owingMrate;
    }

    public void setOwingMrate(String owingMrate) {
        this.owingMrate = owingMrate;
    }

    public String getOwingFrate() {
        return owingFrate;
    }

    public void setOwingFrate(String owingFrate) {
        this.owingFrate = owingFrate;
    }

    public String getDebtMrate() {
        return debtMrate;
    }

    public void setDebtMrate(String debtMrate) {
        this.debtMrate = debtMrate;
    }

    public String getDebtFrate() {
        return debtFrate;
    }

    public void setDebtFrate(String debtFrate) {
        this.debtFrate = debtFrate;
    }

    public String getOverdueShortLimit() { return overdueShortLimit; }

    public void setOverdueShortLimit(String overdueShortLimit) { this.overdueShortLimit = overdueShortLimit; }

    public String getOverdueRate() {
        return overdueRate;
    }

    public void setOverdueRate(String overdueRate) {
        this.overdueRate = overdueRate;
    }

    public String getOverdueMrate() {
        return overdueMrate;
    }

    public void setOverdueMrate(String overdueMrate) {
        this.overdueMrate = overdueMrate;
    }

    public String getOverdueFrate() {
        return overdueFrate;
    }

    public void setOverdueFrate(String overdueFrate) {
        this.overdueFrate = overdueFrate;
    }

    public String getShortMdays() {
        return shortMdays;
    }

    public void setShortMdays(String shortMdays) {
        this.shortMdays = shortMdays;
    }

    public String getLongMdays() {
        return longMdays;
    }

    public void setLongMdays(String longMdays) {
        this.longMdays = longMdays;
    }

    public String getShortFdays() {
        return shortFdays;
    }

    public void setShortFdays(String shortFdays) {
        this.shortFdays = shortFdays;
    }

    public String getLongFdays() {
        return longFdays;
    }

    public void setLongFdays(String longFdays) {
        this.longFdays = longFdays;
    }

    public String getMinMage() {
        return minMage;
    }

    public void setMinMage(String minMage) {
        this.minMage = minMage;
    }

    public String getMaxMage() {
        return maxMage;
    }

    public void setMaxMage(String maxMage) {
        this.maxMage = maxMage;
    }

    public String getMinFage() {
        return minFage;
    }

    public void setMinFage(String minFage) {
        this.minFage = minFage;
    }

    public String getMaxFage() {
        return maxFage;
    }

    public void setMaxFage(String maxFage) {
        this.maxFage = maxFage;
    }

    public String getBeginLimit() {
        return beginLimit;
    }

    public void setBeginLimit(String beginLimit) {
        this.beginLimit = beginLimit;
    }

    public String getSuccessLimit() {
        return successLimit;
    }

    public void setSuccessLimit(String successLimit) {
        this.successLimit = successLimit;
    }

    public String getNormalLimit() {
        return normalLimit;
    }

    public void setNormalLimit(String normalLimit) {
        this.normalLimit = normalLimit;
    }

    public String getNscountMrate() {
        return nscountMrate;
    }

    public void setNscountMrate(String nscountMrate) {
        this.nscountMrate = nscountMrate;
    }

    public String getNscountFrate() {
        return nscountFrate;
    }

    public void setNscountFrate(String nscountFrate) {
        this.nscountFrate = nscountFrate;
    }

    public String getRunInterval() {
        return runInterval;
    }

    public void setRunInterval(String runInterval) {
        this.runInterval = runInterval;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getExpireDays() {
        return expireDays;
    }

    public void setExpireDays(String expireDays) {
        this.expireDays = expireDays;
    }

    public String getRefreshTokenExpired() {
        return refreshTokenExpired;
    }

    public void setRefreshTokenExpired(String refreshTokenExpired) {
        this.refreshTokenExpired = refreshTokenExpired;
    }

    public String getInitFlag() { return initFlag; }

    public void setInitFlag(String initFlag) {  this.initFlag = initFlag; }

    public String getInitDate() {
        return initDate;
    }

    public void setInitDate(String initDate) {
        this.initDate = initDate;
    }

    public String getTokenInit() {
        return tokenInit;
    }

    public void setTokenInit(String tokenInit) {
        this.tokenInit = tokenInit;
    }

    public String getRefreshTokenInit() {
        return refreshTokenInit;
    }

    public void setRefreshTokenInit(String refreshTokenInit) {
        this.refreshTokenInit = refreshTokenInit;
    }

}