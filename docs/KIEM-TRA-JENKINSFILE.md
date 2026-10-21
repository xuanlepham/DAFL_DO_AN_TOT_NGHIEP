# BÁO CÁO KIỂM TRA 2 FILE JENKINSFILE — DAFL (2026-10-21)

Đối tượng: (A) Jenkinsfile người dùng upload (bản cũ, 254 dòng, 9 stage) ·
(B) Jenkinsfile trong repo DAFL/Jenkinsfile (261 dòng, 8 stage).
Cả hai cùng một pipeline gốc; bản B là bản tiến hóa của bản A.

## Kết quả theo tiêu chí

| # | Tiêu chí | A (upload) | B (repo) |
|---|---|---|---|
| 1 | Cú pháp Declarative Pipeline hợp lệ (cân bằng khối pipeline/stages/post) | Đạt (73/73) | Đạt (76/76) |
| 2 | Options: disableConcurrentBuilds, timeout 30 phút, logRotator giữ 10 build | Đạt | Đạt |
| 3 | Trigger githubPush | Đạt | Đạt |
| 4 | Đủ vòng đời Continuous Testing: Checkout → Build SUT → Functional → Gate Functional → Performance → Gate Performance → Deploy → Verify | Đạt | Đạt |
| 5 | Quality Gate chặn build thật: Stage 3 error() khi có test fail; Stage 5 gọi check_performance_gate.sh (exit code quyết định), ngưỡng qua biến môi trường MAX_ERROR_RATE=2%, MAX_AVG_RT_MS=1000 | Đạt | Đạt |
| 6 | Báo cáo LUÔN được publish kể cả khi pipeline FAIL | **Chưa đạt**: publish nằm ở Stage 6 riêng — khi Stage 3/5 fail, pipeline dừng và Stage 6 bị bỏ qua → mất JUnit/Allure/JMeter dashboard đúng ở lần chạy fail | **Đạt**: junit + archiveArtifacts + Allure trong post{always} của Stage 2; publishHTML + archive .jtl trong post{always} của Stage 4 (trước khi gate chạy) |
| 7 | Lỗi hạ tầng JMeter không che mất quyết định của Gate | **Một phần**: Stage 4 thiếu `|| true` — JMeter lỗi là fail stage, Stage 5 không chạy | **Đạt**: JMeter có `|| true`, Gate Stage 5 là nơi quyết định PASS/FAIL |
| 8 | Cô lập + giới hạn tài nguyên: git worktree riêng, Docker network riêng, --memory/--cpus cho Selenium (1.2GB) và JMeter (768MB) | Đạt | Đạt |
| 9 | Không hardcode secret: nạp .env từ máy chủ, khóa PayOS không nằm trong Jenkinsfile | Đạt (lưu ý: đường dẫn /var/repo/DAFL là quy ước theo máy local) | Đạt (tương tự) |
| 10 | Dọn dẹp sau build: post{always} xóa test runner + worktree | Đạt | Đạt |

## Điểm yếu chung của cả hai bản (đã xử lý ở bản repo)
- Chờ SUT bằng `sleep 10` cố định trong stage Build: trên máy yếu, Backend + PostgreSQL + Kafka
  khởi động lạnh có thể lâu hơn 10 giây → Stage 2 dễ fail oan (flaky).
  **ĐÃ SỬA ở bản repo (2026-10-21):** vòng lặp healthcheck tối đa 180 giây (60 lần x 3 giây) —
  container curl gắn vào đúng Docker network của SUT, kiểm tra `http://dafl-backend:8080/health`
  và `http://dafl-frontend:80`; quá hạn thì in 50 dòng log backend/frontend và fail stage.
  Logic vòng lặp đã được mô phỏng chạy thật (đường thành công READY=1, đường quá hạn exit 1).

## Kết luận
- **Bản B (trong repo): ĐẠT TIÊU CHUẨN** — dùng bản này cho đồ án/demo. Điểm sleep 10 đã được sửa thành chờ healthcheck (xem mục trên).
- **Bản A (upload): CHƯA ĐẠT** — là phiên bản cũ; 2 lỗi cấu trúc ở tiêu chí 6–7 khiến báo cáo bị mất
  khi Quality Gate fail. Khuyến nghị loại bỏ, không dùng song song.
