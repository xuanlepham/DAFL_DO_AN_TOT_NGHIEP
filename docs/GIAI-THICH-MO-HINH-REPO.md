# GIẢI THÍCH MÔ HÌNH REPO — PHẦN CHUẨN BỊ BẢO VỆ

Quyết định của đồ án: dùng **1 repo** (`DAFL-Automation`) chứa cả Automation, Performance,
pipeline và tài liệu. Đây là lựa chọn CÓ CHỦ ĐÍCH, không phải do không biết mô hình thực tế.

## Câu hỏi hội đồng dễ hỏi: "Thực tế doanh nghiệp/ngân hàng có gộp chung repo thế này không?"

**Trả lời mẫu:**
"Thực tế tại các ngân hàng và doanh nghiệp lớn, kiểm thử chức năng tự động (Automation)
và kiểm thử hiệu năng (Performance) thường nằm ở **hai repo riêng biệt**, vì ba lý do:
(1) hai đội khác nhau sở hữu — QA Automation và Performance Engineering — cần phân quyền
và truy vết thay đổi độc lập; (2) nhịp thay đổi khác nhau — test chức năng sửa theo từng
tính năng, còn kịch bản tải chỉ đổi khi đổi ngưỡng/yêu cầu hiệu năng; (3) môi trường chạy
khác nhau — kiểm thử hiệu năng cần agent sinh tải và dữ liệu riêng, không dùng chung
agent với test chức năng.

Đồ án này chọn **một repo (monorepo)** vì quy mô và mục tiêu khác: nhóm thực hiện nhỏ,
cả hai loại kiểm thử dùng chung một SUT (DAFL) và một pipeline, ưu tiên triển khai/demo
đơn giản trên máy cá nhân. Tuy nhiên cấu trúc bên trong vẫn tách bạch đúng theo hai miền:
hai thư mục riêng (`automation`, `performance`), **hai
Jenkinsfile riêng và hai job Jenkins độc lập** — nên về mặt tổ chức code, ranh giới
tương đương mô hình hai repo; khi đưa vào môi trường doanh nghiệp, việc tách vật lý
thành hai repo là thao tác cơ học (lift-and-shift), không phải thiết kế lại."

## Câu hỏi phụ dễ gặp: "Nếu tách thành 2 repo thật thì phải sửa những gì?"
1. Mỗi Jenkinsfile phải checkout đa repo (repo SUT + repo test tương ứng) thay vì một repo chung.
2. Pipeline tổng hợp chuyển thành job điều phối (orchestrator) gọi hai job con và tổng hợp 2 Quality Gate.
3. Cấu hình phân quyền và agent riêng cho repo Performance (máy sinh tải riêng).
→ Cả 3 điểm đều là thay đổi ở tầng CI/cấu hình, không đổi thiết kế test bên trong.

## Câu hỏi phụ: "Monorepo có nhược điểm gì, đồ án khắc phục thế nào?"
- Quyền truy cập dùng chung cho cả hai miền (chấp nhận được ở quy mô đồ án).
- Lịch sử commit hai miền lẫn nhau → khắc phục bằng quy ước tiền tố commit (test/feat/fix + phạm vi).
- Sửa một bên có thể kích hoạt CI của bên còn lại → khắc phục bằng 2 job Jenkins độc lập,
  mỗi job chỉ chạy pipeline của miền mình; job tổng hợp chỉ chạy ở mốc phát hành/demo.
