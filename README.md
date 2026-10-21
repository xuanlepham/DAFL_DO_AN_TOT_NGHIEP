# DAFL — Đồ án tốt nghiệp: Continuous Testing với Quality Gate tự động

**Đề tài:** Xây dựng và đánh giá quy trình Continuous Testing có Quality Gate tự động.

Repo này chứa **tầng kiểm thử** của đồ án (không chứa mã nguồn BE/FE). Hệ thống được kiểm thử (SUT) là ứng dụng thương mại điện tử **DAFL** — lấy từ repo gốc `sonismyname/DAFL`, được kiểm thử theo kiểu **hộp đen** qua giao diện web `http://localhost:3000` và API `http://localhost:5000`. Các pipeline trong repo này tự clone SUT vào thư mục `sut/` khi chạy.

## Cấu trúc thư mục

```text
DAFL_DO_AN_TOT_NGHIEP/
├── automation/            ← [1] Kiểm thử tự động (Java + Selenium WebDriver + TestNG, mô hình Page Object)
│   ├── pom.xml · mvnw · mvnw.cmd · .mvn/wrapper/   (Maven Wrapper — máy chỉ cần JDK 17)
│   ├── src/main/java/vn/dafl/   ← framework: commons · pageUIs · pageObjects
│   ├── src/test/java/vn/dafl/tests/  ← 9 lớp test, 22 test case tự động
│   ├── testng.xml
│   └── run-e2e-ui.bat     ← chạy nhanh có giao diện Chrome trên Windows (tự lo JDK/JAVA_HOME)
├── performance/           ← [2] Kiểm thử hiệu năng (Apache JMeter): dafl-loadtest.jmx
├── quality-gate/          ← [3] Quality Gate: check_performance_gate.sh (Jenkins/Linux) + .py (Windows)
├── ci/jenkins/quick-setup/ ← Jenkins dựng sẵn bằng Docker + JCasC, tự tạo 3 job
├── Jenkinsfile            ← Pipeline TỔNG HỢP: dựng SUT → automation → performance → 2 Quality Gate → deploy khi PASS
├── Jenkinsfile.auto       ← Pipeline riêng AUTOMATION
├── Jenkinsfile.pt         ← Pipeline riêng PERFORMANCE + Quality Gate hiệu năng
└── docs/                  ← Tài liệu: hướng dẫn từng bước, đặc tả 30 test case, kiểm tra Jenkinsfile, giải thích mô hình repo
```

## Quality Gate

- **Cổng chức năng:** bộ test automation có bất kỳ test nào FAIL → pipeline FAIL.
- **Cổng hiệu năng:** tỉ lệ lỗi ≤ **2%** và thời gian phản hồi trung bình ≤ **1000 ms** (đọc từ kết quả JMeter `.jtl` bằng script trong `quality-gate/`).

## Chạy nhanh trên Windows

Điều kiện: DAFL (SUT) đang chạy tại `localhost:3000`, máy có Internet (lần đầu để tải JDK/Maven/thư viện nếu thiếu).

```bat
cd automation
run-e2e-ui.bat                      :: chay toan bo suite (testng.xml), Chrome co giao dien
run-e2e-ui.bat --t CheckoutE2ETest  :: chay mot lop test chi dinh
```

Hoặc bằng Maven Wrapper trực tiếp:

```bat
cd automation
mvnw.cmd test -Dheadless=false -Dapp.url=http://localhost:3000
```

## Chạy bằng Jenkins

Xem `ci/jenkins/quick-setup/README.md` để dựng Jenkins bằng Docker (tự tạo 3 job: `DAFL-Pipeline`, `DAFL-Automation`, `DAFL-Performance`), hoặc xem hướng dẫn đầy đủ trong `docs/HUONG-DAN-TUNG-BUOC.md`.

## Tài liệu

- `docs/HUONG-DAN-TUNG-BUOC.md` — cài đặt → kiểm chứng → chạy automation/JMeter/Jenkins → demo.
- `docs/TEST-CASES-DAFL.md` / `.csv` — đặc tả 30 test case (27 tự động hóa hoàn toàn, 2 nằm trong luồng E2E, 1 kiểm thử tay bằng Postman).
- `docs/KIEM-TRA-JENKINSFILE.md` — biên bản kiểm tra pipeline theo 10 tiêu chí.
- `docs/GIAI-THICH-MO-HINH-REPO.md` — vì sao đồ án dùng 1 repo kiểm thử và SUT tách riêng (câu hỏi thường gặp khi bảo vệ).

## Phạm vi

Repo chỉ chứa phần thuộc đồ án (kiểm thử + pipeline + tài liệu). Mã nguồn SUT và các script cài đặt môi trường thuộc về phía SUT/máy cá nhân, không nằm trong repo này.
