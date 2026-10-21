package vn.dafl.commons;

/** Hang so dung chung theo mau WebRA (GlobalConstants).
 *  Moi gia tri nhay cam/mo truong deu doc tu bien moi truong hoac system property, khong hardcode. */
public class GlobalConstants {
    /** URL FE: uu tien -Dapp.url > env APP_URL > mac dinh localhost:3000 (khop Jenkinsfile: APP_URL=http://dafl-frontend:80 trong Docker). */
    public static String getAppUrl() {
        String prop = System.getProperty("app.url");
        if (prop != null && !prop.isEmpty()) return prop;
        String env = System.getenv("APP_URL");
        if (env != null && !env.isEmpty()) return env;
        return "http://localhost:3000";
    }
    /** Tai khoan demo duoc seed boi DbInitializer cua Backend (du lieu demo cong khai trong repo). */
    public static final String DEMO_EMAIL = "demo@dafl.vn";
    public static String getDemoPassword() {
        String env = System.getenv("DEMO_PASSWORD");
        return (env != null && !env.isEmpty()) ? env : "Password123!";
    }
    /** Checksum key PayOS chi dung gia lap webhook o moi truong test; lay tu env PAYOS_CHECKSUM_KEY.
     *  Gia tri mac dinh la demo key cong khai trong .env.example cua repo - KHONG dung cho production. */
    public static String getPayosChecksumKey() {
        String env = System.getenv("PAYOS_CHECKSUM_KEY");
        return (env != null && !env.isEmpty()) ? env : "3a3ea89ceb36ac6477f7e5dc49e3b0abc8951ab1a932760ca78151670d19ef1f";
    }
    public static final long LONG_TIMEOUT = 10;
    public static final long PAGE_LOAD_TIMEOUT = 20;
}
