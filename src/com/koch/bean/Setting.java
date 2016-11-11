package com.koch.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class Setting implements Serializable {
	public static final String CACHE_NAME = "setting";

	public static enum WatermarkPosition {
		no, topLeft, topRight, center, bottomLeft, bottomRight;
	}

	public static enum RoundType {
		roundHalfUp, roundUp, roundDown;
	}

	public static enum CaptchaType {
		memberLogin, memberRegister, adminLogin, review, consultation, findPassword, resetPassword, other;
	}

	public static enum AccountLockType {
		member, admin;
	}

	public static enum StockAllocationTime {
		order, payment, deliver;
	}

	public static enum ReviewAuthority {
		anyone, member, purchased;
	}

	public static enum ConsultationAuthority {
		anyone, member;
	}

	public static final Integer CACHE_KEY = Integer.valueOf(0);
	private static final String SEPARATOR = ",";
	private String siteName; //站点名称
	private String siteUrl; //站点地址
	private String logo; //站点LOGO
	private String hotSearch; //热门搜索
	private String address; //联系地址
	private String phone; //联系电话
	private String zipCode; //邮编
	private String email; //邮箱
	private String certtext; //备案编号
	private Boolean isSiteEnabled; //是否网站开启
	private String siteCloseMessage; //网站关闭消息
	private Integer largeProductImageWidth; //商品图片(大)  宽度
	private Integer largeProductImageHeight; //商品图片(大)  高度
	private Integer mediumProductImageWidth; //商品图片(中)  宽度
	private Integer mediumProductImageHeight; //商品图片(中)  高度
	private Integer thumbnailProductImageWidth; //商品图片(缩略)  高度
	private Integer thumbnailProductImageHeight; //商品图片(缩略)  高度
	private String defaultLargeProductImage; //默认商品图片(大)
	private String defaultMediumProductImage; //默认商品图片(中)
	private String defaultThumbnailProductImage; //默认商品图片(缩略)
	private Integer watermarkAlpha; //水印透明度
	private String watermarkImage; //水印图片
	private WatermarkPosition watermarkPosition; //水印位置
	private Integer priceScale; //价格精确位数
	private RoundType priceRoundType; //价格精确方式
	private Boolean isShowMarketPrice; //是否前台显示市场价
	private Double defaultMarketPriceScale; //默认市场价换算比例
	private Boolean isRegisterEnabled; //是否开放注册
	private Boolean isDuplicateEmail; //是否允许E-mail重复注册
	private String disabledUsername; //禁用用户名
	private Integer usernameMinLength; //用户名最小长度
	private Integer usernameMaxLength; //用户名最大长度
	private Integer passwordMinLength; //密码最小长度
	private Integer passwordMaxLength; //密码最大长度
	private Long registerPoint; //注册初始积分
	private String registerAgreement; //注册协议
	private Boolean isEmailLogin; //是否允许E-mail登录
	private CaptchaType[] captchaTypes; //验证码类型
	private AccountLockType[] accountLockTypes; //账号锁定类型
	private Integer accountLockCount; //连续登录失败最大次数
	private Integer accountLockTime; //自动解锁时间
	private Integer safeKeyExpiryTime; //安全密匙有效时间
	private Integer uploadMaxSize; //上传文件最大限制
	private String uploadImageExtension; //允许上传图片扩展名
	private String uploadFlashExtension; //允许上传Flash扩展名
	private String uploadMediaExtension; //允许上传Flash扩展名
	private String uploadFileExtension; //允许上传媒体扩展名
	private String imageUploadPath; //图片上传路径
	private String flashUploadPath; //Flash上传路径
	private String mediaUploadPath; //媒体上传路径
	private String fileUploadPath; //文件上传路径
	private String smtpFromMail; //*发件人邮箱
	private String smtpHost; //SMTP服务器地址
	private Integer smtpPort; //SMTP服务器端口
	private String smtpUsername; //SMTP用户名
	private String smtpPassword; //SMTP密码
	private String currencySign; //货币符号
	private String currencyUnit; //货币单位
	private Integer stockAlertCount; //库存警告数
	private StockAllocationTime stockAllocationTime; //库存分配时间点
	private Double defaultPointScale; //默认积分换算比例
	private Boolean isDevelopmentEnabled; //是否开启开发模式
	private Boolean isReviewEnabled; //是否开启评论
	private Boolean isReviewCheck; //是否审核评论
	private ReviewAuthority reviewAuthority; //评论权限
	private Boolean isConsultationEnabled; //是否开启咨询
	private Boolean isConsultationCheck; //是否审核咨询
	private ConsultationAuthority consultationAuthority; //咨询权限
	private Boolean isInvoiceEnabled;  //是否开启发票功能
	private Boolean isTaxPriceEnabled; //是否开启含税价
	private Double taxRate; //税率
	private String cookiePath; //Cookie路径
	private String cookieDomain; //Cookie作用域
	private String kuaidi100Key;
	private Boolean isCnzzEnabled;
	private String cnzzSiteId;
	private String cnzzPassword;

	@NotEmpty
	@Length(max = 200)
	public String getSiteName() {
		return this.siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	@NotEmpty
	@Length(max = 200)
	public String getSiteUrl() {
		return this.siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = StringUtils.removeEnd(siteUrl, "/");
	}

	@NotEmpty
	@Length(max = 200)
	public String getLogo() {
		return this.logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Length(max = 200)
	public String getHotSearch() {
		return this.hotSearch;
	}

	public void setHotSearch(String hotSearch) {
		if (hotSearch != null) {
			hotSearch = hotSearch.replaceAll("[,\\s]*,[,\\s]*", ",")
					.replaceAll("^,|,$", "");
		}
		this.hotSearch = hotSearch;
	}

	@Length(max = 200)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Length(max = 200)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(max = 200)
	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Email
	@Length(max = 200)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Length(max = 200)
	public String getCerttext() {
		return this.certtext;
	}

	public void setCerttext(String certtext) {
		this.certtext = certtext;
	}

	@NotNull
	public Boolean getIsSiteEnabled() {
		return this.isSiteEnabled;
	}

	public void setIsSiteEnabled(Boolean isSiteEnabled) {
		this.isSiteEnabled = isSiteEnabled;
	}

	@NotEmpty
	public String getSiteCloseMessage() {
		return this.siteCloseMessage;
	}

	public void setSiteCloseMessage(String siteCloseMessage) {
		this.siteCloseMessage = siteCloseMessage;
	}

	@NotNull
	@Min(1L)
	public Integer getLargeProductImageWidth() {
		return this.largeProductImageWidth;
	}

	public void setLargeProductImageWidth(Integer largeProductImageWidth) {
		this.largeProductImageWidth = largeProductImageWidth;
	}

	@NotNull
	@Min(1L)
	public Integer getLargeProductImageHeight() {
		return this.largeProductImageHeight;
	}

	public void setLargeProductImageHeight(Integer largeProductImageHeight) {
		this.largeProductImageHeight = largeProductImageHeight;
	}

	@NotNull
	@Min(1L)
	public Integer getMediumProductImageWidth() {
		return this.mediumProductImageWidth;
	}

	public void setMediumProductImageWidth(Integer mediumProductImageWidth) {
		this.mediumProductImageWidth = mediumProductImageWidth;
	}

	@NotNull
	@Min(1L)
	public Integer getMediumProductImageHeight() {
		return this.mediumProductImageHeight;
	}

	public void setMediumProductImageHeight(Integer mediumProductImageHeight) {
		this.mediumProductImageHeight = mediumProductImageHeight;
	}

	@NotNull
	@Min(1L)
	public Integer getThumbnailProductImageWidth() {
		return this.thumbnailProductImageWidth;
	}

	public void setThumbnailProductImageWidth(Integer thumbnailProductImageWidth) {
		this.thumbnailProductImageWidth = thumbnailProductImageWidth;
	}

	@NotNull
	@Min(1L)
	public Integer getThumbnailProductImageHeight() {
		return this.thumbnailProductImageHeight;
	}

	public void setThumbnailProductImageHeight(
			Integer thumbnailProductImageHeight) {
		this.thumbnailProductImageHeight = thumbnailProductImageHeight;
	}

	@NotEmpty
	@Length(max = 200)
	public String getDefaultLargeProductImage() {
		return this.defaultLargeProductImage;
	}

	public void setDefaultLargeProductImage(String defaultLargeProductImage) {
		this.defaultLargeProductImage = defaultLargeProductImage;
	}

	@NotEmpty
	@Length(max = 200)
	public String getDefaultMediumProductImage() {
		return this.defaultMediumProductImage;
	}

	public void setDefaultMediumProductImage(String defaultMediumProductImage) {
		this.defaultMediumProductImage = defaultMediumProductImage;
	}

	@NotEmpty
	@Length(max = 200)
	public String getDefaultThumbnailProductImage() {
		return this.defaultThumbnailProductImage;
	}

	public void setDefaultThumbnailProductImage(
			String defaultThumbnailProductImage) {
		this.defaultThumbnailProductImage = defaultThumbnailProductImage;
	}

	@NotNull
	@Min(0L)
	@Max(100L)
	public Integer getWatermarkAlpha() {
		return this.watermarkAlpha;
	}

	public void setWatermarkAlpha(Integer watermarkAlpha) {
		this.watermarkAlpha = watermarkAlpha;
	}

	public String getWatermarkImage() {
		return this.watermarkImage;
	}

	public void setWatermarkImage(String watermarkImage) {
		this.watermarkImage = watermarkImage;
	}

	@NotNull
	public WatermarkPosition getWatermarkPosition() {
		return this.watermarkPosition;
	}

	public void setWatermarkPosition(WatermarkPosition watermarkPosition) {
		this.watermarkPosition = watermarkPosition;
	}

	@NotNull
	@Min(0L)
	@Max(3L)
	public Integer getPriceScale() {
		return this.priceScale;
	}

	public void setPriceScale(Integer priceScale) {
		this.priceScale = priceScale;
	}

	@NotNull
	public RoundType getPriceRoundType() {
		return this.priceRoundType;
	}

	public void setPriceRoundType(RoundType priceRoundType) {
		this.priceRoundType = priceRoundType;
	}

	@NotNull
	public Boolean getIsShowMarketPrice() {
		return this.isShowMarketPrice;
	}

	public void setIsShowMarketPrice(Boolean isShowMarketPrice) {
		this.isShowMarketPrice = isShowMarketPrice;
	}

	@NotNull
	@Min(0L)
	@Digits(integer = 3, fraction = 3)
	public Double getDefaultMarketPriceScale() {
		return this.defaultMarketPriceScale;
	}

	public void setDefaultMarketPriceScale(Double defaultMarketPriceScale) {
		this.defaultMarketPriceScale = defaultMarketPriceScale;
	}

	@NotNull
	public Boolean getIsRegisterEnabled() {
		return this.isRegisterEnabled;
	}

	public void setIsRegisterEnabled(Boolean isRegisterEnabled) {
		this.isRegisterEnabled = isRegisterEnabled;
	}

	@NotNull
	public Boolean getIsDuplicateEmail() {
		return this.isDuplicateEmail;
	}

	public void setIsDuplicateEmail(Boolean isDuplicateEmail) {
		this.isDuplicateEmail = isDuplicateEmail;
	}

	@Length(max = 200)
	public String getDisabledUsername() {
		return this.disabledUsername;
	}

	public void setDisabledUsername(String disabledUsername) {
		if (disabledUsername != null) {
			disabledUsername = disabledUsername.replaceAll("[,\\s]*,[,\\s]*",
					",").replaceAll("^,|,$", "");
		}
		this.disabledUsername = disabledUsername;
	}

	@NotNull
	@Min(1L)
	@Max(117L)
	public Integer getUsernameMinLength() {
		return this.usernameMinLength;
	}

	public void setUsernameMinLength(Integer usernameMinLength) {
		this.usernameMinLength = usernameMinLength;
	}

	@NotNull
	@Min(1L)
	@Max(117L)
	public Integer getUsernameMaxLength() {
		return this.usernameMaxLength;
	}

	public void setUsernameMaxLength(Integer usernameMaxLength) {
		this.usernameMaxLength = usernameMaxLength;
	}

	@NotNull
	@Min(1L)
	@Max(117L)
	public Integer getPasswordMinLength() {
		return this.passwordMinLength;
	}

	public void setPasswordMinLength(Integer passwordMinLength) {
		this.passwordMinLength = passwordMinLength;
	}

	@NotNull
	@Min(1L)
	@Max(117L)
	public Integer getPasswordMaxLength() {
		return this.passwordMaxLength;
	}

	public void setPasswordMaxLength(Integer passwordMaxLength) {
		this.passwordMaxLength = passwordMaxLength;
	}

	@NotNull
	@Min(0L)
	public Long getRegisterPoint() {
		return this.registerPoint;
	}

	public void setRegisterPoint(Long registerPoint) {
		this.registerPoint = registerPoint;
	}

	@NotEmpty
	public String getRegisterAgreement() {
		return this.registerAgreement;
	}

	public void setRegisterAgreement(String registerAgreement) {
		this.registerAgreement = registerAgreement;
	}

	@NotNull
	public Boolean getIsEmailLogin() {
		return this.isEmailLogin;
	}

	public void setIsEmailLogin(Boolean isEmailLogin) {
		this.isEmailLogin = isEmailLogin;
	}

	public CaptchaType[] getCaptchaTypes() {
		return this.captchaTypes;
	}

	public void setCaptchaTypes(CaptchaType[] captchaTypes) {
		this.captchaTypes = captchaTypes;
	}

	public AccountLockType[] getAccountLockTypes() {
		return this.accountLockTypes;
	}

	public void setAccountLockTypes(AccountLockType[] accountLockTypes) {
		this.accountLockTypes = accountLockTypes;
	}

	@NotNull
	@Min(1L)
	public Integer getAccountLockCount() {
		return this.accountLockCount;
	}

	public void setAccountLockCount(Integer accountLockCount) {
		this.accountLockCount = accountLockCount;
	}

	@NotNull
	@Min(0L)
	public Integer getAccountLockTime() {
		return this.accountLockTime;
	}

	public void setAccountLockTime(Integer accountLockTime) {
		this.accountLockTime = accountLockTime;
	}

	@NotNull
	@Min(0L)
	public Integer getSafeKeyExpiryTime() {
		return this.safeKeyExpiryTime;
	}

	public void setSafeKeyExpiryTime(Integer safeKeyExpiryTime) {
		this.safeKeyExpiryTime = safeKeyExpiryTime;
	}

	@NotNull
	@Min(0L)
	public Integer getUploadMaxSize() {
		return this.uploadMaxSize;
	}

	public void setUploadMaxSize(Integer uploadMaxSize) {
		this.uploadMaxSize = uploadMaxSize;
	}

	@Length(max = 200)
	public String getUploadImageExtension() {
		return this.uploadImageExtension;
	}

	public void setUploadImageExtension(String uploadImageExtension) {
		if (uploadImageExtension != null) {
			uploadImageExtension = uploadImageExtension.replaceAll(
					"[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "")
					.toLowerCase();
		}
		this.uploadImageExtension = uploadImageExtension;
	}

	@Length(max = 200)
	public String getUploadFlashExtension() {
		return this.uploadFlashExtension;
	}

	public void setUploadFlashExtension(String uploadFlashExtension) {
		if (uploadFlashExtension != null) {
			uploadFlashExtension = uploadFlashExtension.replaceAll(
					"[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "")
					.toLowerCase();
		}
		this.uploadFlashExtension = uploadFlashExtension;
	}

	@Length(max = 200)
	public String getUploadMediaExtension() {
		return this.uploadMediaExtension;
	}

	public void setUploadMediaExtension(String uploadMediaExtension) {
		if (uploadMediaExtension != null) {
			uploadMediaExtension = uploadMediaExtension.replaceAll(
					"[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "")
					.toLowerCase();
		}
		this.uploadMediaExtension = uploadMediaExtension;
	}

	@Length(max = 200)
	public String getUploadFileExtension() {
		return this.uploadFileExtension;
	}

	public void setUploadFileExtension(String uploadFileExtension) {
		if (uploadFileExtension != null) {
			uploadFileExtension = uploadFileExtension.replaceAll(
					"[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "")
					.toLowerCase();
		}
		this.uploadFileExtension = uploadFileExtension;
	}

	@NotEmpty
	@Length(max = 200)
	public String getImageUploadPath() {
		return this.imageUploadPath;
	}

	public void setImageUploadPath(String imageUploadPath) {
		if (imageUploadPath != null) {
			if (!imageUploadPath.startsWith("/")) {
				imageUploadPath = "/" + imageUploadPath;
			}
			if (!imageUploadPath.endsWith("/")) {
				imageUploadPath = imageUploadPath + "/";
			}
		}
		this.imageUploadPath = imageUploadPath;
	}

	@NotEmpty
	@Length(max = 200)
	public String getFlashUploadPath() {
		return this.flashUploadPath;
	}

	public void setFlashUploadPath(String flashUploadPath) {
		if (flashUploadPath != null) {
			if (!flashUploadPath.startsWith("/")) {
				flashUploadPath = "/" + flashUploadPath;
			}
			if (!flashUploadPath.endsWith("/")) {
				flashUploadPath = flashUploadPath + "/";
			}
		}
		this.flashUploadPath = flashUploadPath;
	}

	@NotEmpty
	@Length(max = 200)
	public String getMediaUploadPath() {
		return this.mediaUploadPath;
	}

	public void setMediaUploadPath(String mediaUploadPath) {
		if (mediaUploadPath != null) {
			if (!mediaUploadPath.startsWith("/")) {
				mediaUploadPath = "/" + mediaUploadPath;
			}
			if (!mediaUploadPath.endsWith("/")) {
				mediaUploadPath = mediaUploadPath + "/";
			}
		}
		this.mediaUploadPath = mediaUploadPath;
	}

	@NotEmpty
	@Length(max = 200)
	public String getFileUploadPath() {
		return this.fileUploadPath;
	}

	public void setFileUploadPath(String fileUploadPath) {
		if (fileUploadPath != null) {
			if (!fileUploadPath.startsWith("/")) {
				fileUploadPath = "/" + fileUploadPath;
			}
			if (!fileUploadPath.endsWith("/")) {
				fileUploadPath = fileUploadPath + "/";
			}
		}
		this.fileUploadPath = fileUploadPath;
	}

	@NotEmpty
	@Email
	@Length(max = 200)
	public String getSmtpFromMail() {
		return this.smtpFromMail;
	}

	public void setSmtpFromMail(String smtpFromMail) {
		this.smtpFromMail = smtpFromMail;
	}

	@NotEmpty
	@Length(max = 200)
	public String getSmtpHost() {
		return this.smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	@NotNull
	@Min(0L)
	public Integer getSmtpPort() {
		return this.smtpPort;
	}

	public void setSmtpPort(Integer smtpPort) {
		this.smtpPort = smtpPort;
	}

	@NotEmpty
	@Length(max = 200)
	public String getSmtpUsername() {
		return this.smtpUsername;
	}

	public void setSmtpUsername(String smtpUsername) {
		this.smtpUsername = smtpUsername;
	}

	@Length(max = 200)
	public String getSmtpPassword() {
		return this.smtpPassword;
	}

	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

	@NotEmpty
	@Length(max = 200)
	public String getCurrencySign() {
		return this.currencySign;
	}

	public void setCurrencySign(String currencySign) {
		this.currencySign = currencySign;
	}

	@NotEmpty
	@Length(max = 200)
	public String getCurrencyUnit() {
		return this.currencyUnit;
	}

	public void setCurrencyUnit(String currencyUnit) {
		this.currencyUnit = currencyUnit;
	}

	@NotNull
	@Min(0L)
	public Integer getStockAlertCount() {
		return this.stockAlertCount;
	}

	public void setStockAlertCount(Integer stockAlertCount) {
		this.stockAlertCount = stockAlertCount;
	}

	@NotNull
	public StockAllocationTime getStockAllocationTime() {
		return this.stockAllocationTime;
	}

	public void setStockAllocationTime(StockAllocationTime stockAllocationTime) {
		this.stockAllocationTime = stockAllocationTime;
	}

	@NotNull
	@Min(0L)
	@Digits(integer = 3, fraction = 3)
	public Double getDefaultPointScale() {
		return this.defaultPointScale;
	}

	public void setDefaultPointScale(Double defaultPointScale) {
		this.defaultPointScale = defaultPointScale;
	}

	@NotNull
	public Boolean getIsDevelopmentEnabled() {
		return this.isDevelopmentEnabled;
	}

	public void setIsDevelopmentEnabled(Boolean isDevelopmentEnabled) {
		this.isDevelopmentEnabled = isDevelopmentEnabled;
	}

	@NotNull
	public Boolean getIsReviewEnabled() {
		return this.isReviewEnabled;
	}

	public void setIsReviewEnabled(Boolean isReviewEnabled) {
		this.isReviewEnabled = isReviewEnabled;
	}

	@NotNull
	public Boolean getIsReviewCheck() {
		return this.isReviewCheck;
	}

	public void setIsReviewCheck(Boolean isReviewCheck) {
		this.isReviewCheck = isReviewCheck;
	}

	@NotNull
	public ReviewAuthority getReviewAuthority() {
		return this.reviewAuthority;
	}

	public void setReviewAuthority(ReviewAuthority reviewAuthority) {
		this.reviewAuthority = reviewAuthority;
	}

	@NotNull
	public Boolean getIsConsultationEnabled() {
		return this.isConsultationEnabled;
	}

	public void setIsConsultationEnabled(Boolean isConsultationEnabled) {
		this.isConsultationEnabled = isConsultationEnabled;
	}

	@NotNull
	public Boolean getIsConsultationCheck() {
		return this.isConsultationCheck;
	}

	public void setIsConsultationCheck(Boolean isConsultationCheck) {
		this.isConsultationCheck = isConsultationCheck;
	}

	@NotNull
	public ConsultationAuthority getConsultationAuthority() {
		return this.consultationAuthority;
	}

	public void setConsultationAuthority(
			ConsultationAuthority consultationAuthority) {
		this.consultationAuthority = consultationAuthority;
	}

	@NotNull
	public Boolean getIsInvoiceEnabled() {
		return this.isInvoiceEnabled;
	}

	public void setIsInvoiceEnabled(Boolean isInvoiceEnabled) {
		this.isInvoiceEnabled = isInvoiceEnabled;
	}

	@NotNull
	public Boolean getIsTaxPriceEnabled() {
		return this.isTaxPriceEnabled;
	}

	public void setIsTaxPriceEnabled(Boolean isTaxPriceEnabled) {
		this.isTaxPriceEnabled = isTaxPriceEnabled;
	}

	@NotNull
	@Min(0L)
	@Digits(integer = 3, fraction = 3)
	public Double getTaxRate() {
		return this.taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	@NotEmpty
	@Length(max = 200)
	public String getCookiePath() {
		return this.cookiePath;
	}

	public void setCookiePath(String cookiePath) {
		if ((cookiePath != null) && (!cookiePath.endsWith("/"))) {
			cookiePath = cookiePath + "/";
		}
		this.cookiePath = cookiePath;
	}

	@Length(max = 200)
	public String getCookieDomain() {
		return this.cookieDomain;
	}

	public void setCookieDomain(String cookieDomain) {
		this.cookieDomain = cookieDomain;
	}

	@Length(max = 200)
	public String getKuaidi100Key() {
		return this.kuaidi100Key;
	}

	public void setKuaidi100Key(String kuaidi100Key) {
		this.kuaidi100Key = kuaidi100Key;
	}

	public Boolean getIsCnzzEnabled() {
		return this.isCnzzEnabled;
	}

	public void setIsCnzzEnabled(Boolean isCnzzEnabled) {
		this.isCnzzEnabled = isCnzzEnabled;
	}

	public String getCnzzSiteId() {
		return this.cnzzSiteId;
	}

	public void setCnzzSiteId(String cnzzSiteId) {
		this.cnzzSiteId = cnzzSiteId;
	}

	public String getCnzzPassword() {
		return this.cnzzPassword;
	}

	public void setCnzzPassword(String cnzzPassword) {
		this.cnzzPassword = cnzzPassword;
	}

	public String[] getHotSearches() {
		return StringUtils.split(this.hotSearch, ",");
	}

	public String[] getDisabledUsernames() {
		return StringUtils.split(this.disabledUsername, ",");
	}

	public String[] getUploadImageExtensions() {
		return StringUtils.split(this.uploadImageExtension, ",");
	}

	public String[] getUploadFlashExtensions() {
		return StringUtils.split(this.uploadFlashExtension, ",");
	}

	public String[] getUploadMediaExtensions() {
		return StringUtils.split(this.uploadMediaExtension, ",");
	}

	public String[] getUploadFileExtensions() {
		return StringUtils.split(this.uploadFileExtension, ",");
	}

	public BigDecimal setScale(BigDecimal amount) {
		if (amount == null) {
			return null;
		}
		int roundingMode;
		if (getPriceRoundType() == RoundType.roundUp) {
			roundingMode = 0;
		} else {
			if (getPriceRoundType() == RoundType.roundDown) {
				roundingMode = 1;
			} else {
				roundingMode = 4;
			}
		}
		return amount.setScale(getPriceScale().intValue(), roundingMode);
	}
}
