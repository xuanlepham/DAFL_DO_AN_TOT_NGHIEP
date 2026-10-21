package vn.dafl.commons;

/**
 * Hang so dung chung cho toan bo framework.
 * Du lieu demo (tai khoan, voucher) lay tu du lieu seed cua SUT DAFL
 * (Backend/Infrastructure/Data/DbInitializer.cs) - tai khoan demo cong khai
 * ma chinh ung dung cung cap san nut "dung tai khoan demo" tren giao dien.
 */
public class GlobalConstants {

    public static final long SHORT_TIMEOUT = 5;
    public static final long LONG_TIMEOUT = 30;

    public static final String DEFAULT_APP_URL = "http://localhost:3000";

    // Tai khoan demo duoc seed san trong CSDL cua SUT
    public static final String DEMO_EMAIL = "demo@dafl.vn";
    public static final String DEMO_PASSWORD = "Password123!";
    public static final String DEMO_FULL_NAME = "Nguyễn Văn Demo";
    public static final String DEMO_PHONE = "0987654321";
    public static final String DEMO_ADDRESS = "Số 1 Đại Cồ Việt, Hai Bà Trưng, Hà Nội";

    // Ma giam gia duoc seed san trong CSDL cua SUT
    public static final String VOUCHER_PERCENT_10 = "DAFL10"; // giam 10% (don dau)
    public static final String VOUCHER_FIXED_20K = "DAFL20";  // giam thang 20.000d

    // Thong diep loi thuc te tra ve tu Backend cua SUT
    public static final String LOGIN_ERROR_MESSAGE = "Email hoặc mật khẩu không chính xác";
    public static final String VOUCHER_ERROR_MESSAGE = "Mã giảm giá không tồn tại hoặc đã hết hiệu lực";

    private GlobalConstants() {
    }

    /** URL cua SUT: uu tien -Dapp.url, sau do bien moi truong APP_URL, cuoi cung la mac dinh. */
    public static String getAppUrl() {
        String propUrl = System.getProperty("app.url");
        if (propUrl != null && !propUrl.trim().isEmpty()) {
            return propUrl.trim();
        }
        String envUrl = System.getenv("APP_URL");
        if (envUrl != null && !envUrl.trim().isEmpty()) {
            return envUrl.trim();
        }
        return DEFAULT_APP_URL;
    }
}
