# HƯỚNG DẪN TỪNG BƯỚC — DAFL + CONTINUOUS TESTING

Làm lần lượt từ Bước 0 đến Bước 7. Mỗi bước có tiêu chí "đạt" để tự kiểm tra trước khi đi tiếp.
Đường dẫn ví dụ: giải nén gói triển khai ra `D:\DAFL` (đổi theo máy bạn nếu khác).

---

## Bước 0 — Chuẩn bị máy
- Windows 10 bản 22H2 (build 19045) trở lên hoặc Windows 11, tài khoản có quyền Admin.
- RAM tối thiểu 8GB (khuyên 16GB), ổ trống ≥ 15GB, có Internet ổn định.
- Nếu máy báo ảo hóa (VT-x/AMD-V) đang tắt: vào BIOS/UEFI bật Intel VT-x / SVM Mode trước.
- Đạt khi: máy đăng nhập bằng tài khoản Admin, mạng bình thường.

## Bước 1 — Cài đặt 1-click (tự cài mọi cấu phần)
1. Giải nén file `DAFL-goi-trien-khai.zip` ra thư mục `D:\DAFL` (giải nén hẳn, không chạy trong zip).
2. Double-click `D:\DAFL\CAI-DAT-DAFL.bat` → bấm **Yes** khi Windows hỏi quyền Admin.
3. Để máy tự chạy (30–60 phút): script tự bật WSL 2 → cài Git, JDK 17, Maven, Chrome, JMeter
   → cài Docker Desktop → dựng DAFL (PostgreSQL, Kafka, Backend, Frontend) → dựng Jenkins.
4. Nếu Windows yêu cầu khởi động lại giữa chừng: cứ Restart, đăng nhập lại — script tự chạy tiếp.
5. **Nếu treo ở dòng "Starting package install..." quá 15 phút** (lỗi đã gặp thực tế):
   đóng cửa sổ cài → **Restart máy** → chạy lại `CAI-DAT-DAFL.bat`.
   Phần đã cài xong sẽ được bỏ qua, chỉ tải lại file cài Docker (~650MB).
6. **Nếu treo ở "Đang chờ Docker Engine"**: mở Docker Desktop từ Start Menu, bấm **Accept**
   điều khoản (nếu hiện), chờ biểu tượng cá voi đứng yên rồi để script chạy tiếp.
- Đạt khi: script in tổng kết cuối và không còn bước nào báo lỗi đỏ.
  (Chi tiết mọi lần chạy nằm trong `D:\DAFL\install-log.txt`.)

## Bước 2 — Kiểm chứng hệ thống đã lên
Mở trình duyệt, kiểm tra đủ 3 địa chỉ:
1. Frontend: http://localhost:3000 — thấy trang bán hàng DAFL.
2. Backend: http://localhost:5000/health — thấy trạng thái Healthy.
3. Jenkins: http://localhost:8080 — đăng nhập được (mật khẩu admin mặc định nằm trong
   `ci/jenkins/quick-setup/jenkins.yaml` — **đổi ngay sau lần đăng nhập đầu**).
4. Trên Frontend: bấm biểu tượng tài khoản → **Dùng tài khoản Demo** → Đăng nhập thành công.
- Đạt khi: cả 4 kiểm tra trên đều đúng.

## Bước 3 — Chạy Automation Test tại máy (không qua Jenkins)
Mở PowerShell:
```bat
cd D:\DAFL\tests\functional-selenium
mvn clean test
```
- Chạy 22 test case (TC_UI_01–22): trang chủ, tìm kiếm, lọc danh mục, giỏ hàng, voucher,
  đăng ký/đăng nhập/đăng xuất, validation checkout, E2E thanh toán PayOS giả lập.
- Xem kết quả: `target\surefire-reports` (JUnit) và Allure results trong `target\allure-results`.
- Đạt khi: BUILD kết thúc, 22/22 test PASS (test nào fail thì giữ nguyên màn hình/log để đối chiếu).

## Bước 4 — Chạy Performance Test + Quality Gate tại máy
```bat
cd D:\DAFL\tests\performance-jmeter
jmeter -n -t dafl-loadtest.jmx -Jtarget_host=localhost -Jtarget_port=5000 -l results\result.jtl -e -o results\dashboard
check_performance_gate.py results\result.jtl
```
(Jenkins chạy bản `.sh` của gate; máy Windows dùng bản `.py` — cùng một logic.)
- Ngưỡng Gate: tỷ lệ lỗi ≤ 2% và thời gian phản hồi trung bình ≤ 1000ms.
- Đạt khi: gate in PASS; mở `results\dashboard\index.html` xem được biểu đồ hiệu năng.

## Bước 5 — Chạy bằng Jenkins (3 job có sẵn, JCasC tự tạo)
Vào http://localhost:8080 — có sẵn 3 job:
1. **DAFL-Automation**: chỉ chạy Selenium + Functional Quality Gate (dùng `Jenkinsfile.auto`).
2. **DAFL-Performance**: chỉ chạy JMeter + Performance Quality Gate (dùng `Jenkinsfile.pt`).
3. **DAFL-Pipeline**: chạy tổng hợp cả hai + Deploy (dùng `Jenkinsfile`) — dùng cho demo cuối.
Cách chạy: mở job → **Build Now** → xem từng Stage xanh/đỏ → mở **Test Result / Allure Report**
(job Automation) hoặc **JMeter Performance Dashboard** (job Performance) ngay trong Jenkins.
- Demo Quality Gate (quan trọng cho bảo vệ): sửa tạm 1 assert trong test cho sai → Build lại
  job DAFL-Automation → pipeline phải **đỏ ở Stage Functional Quality Gate** và KHÔNG deploy.
  Chạy xong nhớ sửa lại assert như cũ.
- Đạt khi: 3 job đều build xanh ở trạng thái code đúng; job Automation đỏ đúng ở Stage Gate khi cố ý sai.

## Bước 6 — Đẩy code lên GitHub (repo của đồ án)
Repo đồ án chỉ chứa tầng kiểm thử (không chứa code BE/FE) — tên: `DAFL-Automation`, tài khoản `xuanlepham`.
1. Vào https://github.com/new → đặt tên **DAFL-Automation** → chọn **Private**
   → KHÔNG tick "Add a README" → **Create repository**.
2. Quay lại khung chat, nhắn "đã tạo repo" — Muse kiểm tra quyền và đẩy toàn bộ:
   `tests/`, `Jenkinsfile`, `Jenkinsfile.auto`, `Jenkinsfile.pt`,
   `ci/`, `deployment/`, `docs/` (dạng 1 commit snapshot).
3. Mở repo trên GitHub đối chiếu cây thư mục với file `docs/CAY-THU-MUC-CHUAN.md`.
- Đạt khi: repo hiển thị đủ các thư mục trên, file `tests/README.md` mở đọc được.

## Bước 7 — Chuẩn bị demo / bảo vệ
1. Chạy trước toàn bộ: Bước 2 (hệ thống lên) → Bước 5 job DAFL-Pipeline xanh.
2. Chuẩn bị sẵn 3 màn hình: Jenkins Stage View (pipeline + 2 Gate), Allure Report,
   JMeter Dashboard.
3. Chuẩn bị kịch bản nói: SUT là DAFL (hộp đen) → 30 test case → 22 test đã tự động hóa
   → Quality Gate quyết định PASS/FAIL thế nào → demo cố ý fail (Bước 5).
4. Tài liệu kèm theo: `docs/TEST-CASES-DAFL.xlsx` (bảng 30 test case),
   `docs/KIEM-TRA-JENKINSFILE.md` (báo cáo kiểm tra pipeline), `docs/TRIEN-KHAI-DAFL.md` (kế hoạch).

## Dọn dẹp khi không dùng nữa
```bat
cd D:\DAFL\deployment
.\stop-all.ps1
```
(Lệnh này dừng toàn bộ container DAFL + Jenkins; dữ liệu volume vẫn giữ.)

## Bảng xử lý sự cố nhanh
| Hiện tượng | Cách xử lý |
|---|---|
| Treo "Starting package install..." > 15 phút | Đóng cửa sổ → Restart máy → chạy lại `CAI-DAT-DAFL.bat` |
| Docker Engine không lên sau 5 phút | Mở Docker Desktop thủ công → Accept điều khoản → chờ cá voi đứng yên |
| Báo ảo hóa đang tắt | Vào BIOS bật VT-x/SVM → Restart → chạy lại bộ cài |
| Cổng 3000/5000/8080 bị chiếm | Đóng ứng dụng đang dùng cổng đó, hoặc chạy `stop-all.ps1` rồi cài lại |
| Test fail khi chạy `mvn clean test` | Chụp màn hình + gửi file trong `target\surefire-reports` để chẩn đoán |
| Bất kỳ lỗi nào khác | Gửi file `install-log.txt` (trong thư mục DAFL) để xem đúng dòng lỗi |
