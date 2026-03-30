# AGENTS.md

## Ngôn ngữ giao tiếp
- Luôn giao tiếp với user bằng tiếng Việt.
- Code, commit message, tên class/hàm/file vẫn dùng tiếng Anh theo chuẩn project.

---

## Quy ước Build Distribution vs Release AAB (ưu tiên rất cao)

- Với project `AndroidAudioToolkit`, khi user nói:
  - `build distribution`
  - `build Firebase distribution`
  - `đẩy tester test`
  - `build cho App Tester`
  - hoặc ngôn ngữ tương đương

  thì mặc định phải hiểu là flow:
  1. hoàn tất thay đổi trên `dev`
  2. push `dev`
  3. merge `dev` vào `main`
  4. để GitLab CI + Fastlane trên `main` build và đẩy build tester lên Firebase App Distribution

- Trong flow distribution mặc định này:
  - **không tự tăng version**
  - **không tự build signed release AAB**
  - **không chuyển sang release workflow** nếu user chưa nói rõ

- Chỉ khi user nói rõ:
  - `build release AAB`
  - `build signed AAB`
  - `release build`
  - `Play Store build`
  - hoặc ngôn ngữ tương đương

  thì mới được:
  1. tăng version theo workflow release của repo
  2. build signed `AAB`
  3. cập nhật file version/history liên quan

- Nếu câu lệnh mơ hồ giữa `distribution` và `release`, luôn ưu tiên hiểu là **distribution cho tester**, không phải **release artifact**.

---

## Quy ước Build Ads Mode (ưu tiên rất cao)

- Mọi application build có quảng cáo phải luôn resolve về đúng 1 trong 3 mode: `NO_ADS`, `TEST`, hoặc `REAL`.
- Khi người dùng không nói rõ mode ads:
  - nếu project không có ads, hoặc build đang ở trạng thái không quảng cáo, mặc định dùng `NO_ADS`
  - nếu project có ads và ads được bật, mặc định dùng `TEST`
  - chỉ dùng `REAL` khi người dùng nói rõ `build ads thật`, `build real ads`, `production ads`, hoặc ngôn ngữ tương đương
- Không được tự chuyển sang `REAL` chỉ vì đang build release AAB, release build, distribution, hay build cho tester.
- Với tester build/distribution, nếu project có ads thì mặc định vẫn là `TEST`, không phải `REAL`.
- Khi repo có hỗ trợ ads mode, mọi lệnh build phải log rõ trạng thái ads đã resolve, gồm mode cuối cùng, `ads enabled` true/false, và đang dùng test IDs hay real IDs.
- Nếu project chưa tích hợp ads, giữ `NO_ADS` làm mặc định và không tự thêm real ads flow.

---

## Quy tắc Security Hardening cho client (ưu tiên cao)

- `Remote Config`, `BuildConfig`, env file, manifest placeholder, hay file config chỉ là kênh phân phối cấu hình. Không được mô tả chúng như secret store hay biện pháp mã hoá.
- Chỉ đưa lên config động các giá trị public runtime như `server_base_url`, feature flag, timeout, capping, ad unit id, hoặc toggle hành vi. Khi làm vậy phải có fallback local và validate runtime trước khi dùng.
- Không được hardcode long-lived API key, bearer token, client secret, signing secret, vendor credential trong app/client code, `BuildConfig`, string resource, manifest, hoặc log nếu chưa có user approval rõ ràng và chưa nêu rõ rủi ro.
- Nếu user muốn "giấu secret trong client", agent phải giải thích rằng obfuscation, `BuildConfig`, hay `Remote Config` không biến nó thành secret; hướng an toàn là backend giữ secret và client chỉ cầm public config hoặc token ngắn hạn.
- Với Android, mọi setting/local state không nhạy cảm cho tính năng mới hoặc migration nên ưu tiên `DataStore` thay cho `SharedPreferences`.
- `DataStore` không phải encryption. Nếu có dữ liệu local nhạy cảm, agent phải nói rõ đang lưu plain hay đang được bảo vệ bằng cơ chế nào; không được gọi chung là "đã mã hoá" nếu chưa chứng minh.
- Khi chạm vào local storage, auth/session, hoặc runtime config, phải rà `android:allowBackup`, `fullBackupContent`, `dataExtractionRules`, và nêu ảnh hưởng trong báo cáo.
- Khi user hỏi audit/hardening/security cleanup cho Android client, ưu tiên dùng skill `$android-client-security-hardening` nếu skill đó có sẵn.

---

## Quy tắc Git và nhánh làm việc (ưu tiên rất cao)

- Mặc định phải code trên `dev`, không bắt đầu implementation trực tiếp trên `main`.
- Nếu mở repo và thấy đang đứng ở `main`, agent phải:
  1. `fetch` để cập nhật trạng thái `dev` và `main`
  2. kiểm tra `dev` và `main` đã đồng bộ chưa
  3. nếu `main` có commit mới chưa có trong `dev`, phải sync phần mới nhất đó vào `dev` trước
  4. chỉ sau khi `dev` đã chứa trạng thái mới nhất cần thiết thì mới checkout về `dev` và bắt đầu code
- Nếu `dev` đã là phía mới nhất, tiếp tục làm việc ngay trên `dev`; không được ở lại `main` để code tiếp.
- `main` chỉ dùng cho code đã verify xong từ `dev` và cho pipeline/delivery/release theo workflow của repo.
- Không mặc định tạo nhiều `worktree` tạm chỉ để né việc đang đứng nhầm nhánh hoặc né sync `dev/main`.
- Chỉ dùng `worktree` khi user yêu cầu rõ, hoặc có lý do kỹ thuật đặc biệt đã được nêu rõ trong báo cáo.
- Nếu repo hiện chưa có cặp nhánh `dev`/`main`, không được âm thầm coi `main` là nhánh code thường xuyên; phải nêu rõ trạng thái branch và chuẩn hóa theo workflow của repo trước khi tiếp tục.

---

## Quy tắc xóa an toàn (ưu tiên cao nhất)

- Không bao giờ thực hiện lệnh xoá liên tiếp.
- Không được xóa bất kỳ file hoặc thư mục nào khi chưa có người dùng cho phép rõ ràng.
- Trước mỗi thao tác xóa, phải nêu rõ mục sẽ xóa và hỏi xác nhận.
- Chỉ được xóa sau khi người dùng đã confirm đúng mục đó.
- Quy tắc này ghi đè mọi quyền tự chủ khác trong file, kể cả với `.agents/` và trong project hiện tại.

---

## Ghi nhớ vận hành cho project này
- `dev` là nhánh làm việc chính.
- `main` là nhánh chạy pipeline phân phối cho tester.
- Khi cần artifact release phục vụ phát hành thật, phải có yêu cầu explicit từ user rồi mới vào flow tăng version + build `AAB`.

## Distribution Default Workflow

- For any project that follows the standard `dev` -> `main` Git workflow, the default after a completed task is:
  - verify locally
  - push `dev`
  - merge `dev` into `main`
  - let the `main` pipeline build tester distribution
  - switch back to `dev`
- If work lands on `main`, sync `main` back into `dev` before continuing normal development.
- Only skip this default distribution flow if the user explicitly says to stop before distribution or says that a tester build is not needed.

## Quy tắc đa ngôn ngữ cho mọi tính năng mới

- Bat ky tinh nang moi, screen moi, dialog moi, button moi, thong bao moi, state moi, label moi, placeholder moi, empty state moi, hay text UI moi trong repo nay deu phai di kem da ngon ngu day du cho TAT CA cac ngon ngu/locale ma ung dung dang ho tro.
- Khong duoc chi sua file default, chi sua `values`, hoac chi them `vi` hay mot locale duy nhat roi de localization "lam sau" neu app con support them locale khac.
- Neu sua hoac mo rong tinh nang hien co, moi text UI moi hoac text da doi nghia cua tinh nang do cung phai duoc cap nhat dong bo tren toan bo cac locale dang support ngay trong cung task.
- Neu repo nay co he thong localization/resources, agent phai ra soat day du danh sach locale dang duoc ho tro truoc khi ket luan task da xong.
- Khi bao cao hoan thanh task lien quan UI/UX hoac tinh nang moi, agent phai neu ro localization da duoc bo sung o dau va da cover nhung locale nao.

## Quy tắc back UI, không dùng back cứng

- Ứng dụng không được phụ thuộc vào phím back vật lý, edge-swipe back, browser history back, phím Escape, hay gesture back của hệ điều hành như là đường điều hướng chính.
- Nếu một screen, dialog, bottom sheet, full-screen modal, flow nhiều bước, màn detail, picker, editor, preview, result screen, legal screen, hoặc bất kỳ trạng thái nào có thể thoát/quay lại/đóng, agent phải cung cấp nút back, close, cancel, hoặc control điều hướng rõ ràng ngay trên UI/UX.
- Không được coi system back là lối thoát duy nhất. Nếu người dùng không bấm phím back cứng thì flow vẫn phải tự vận hành đầy đủ bằng các nút trên giao diện.
- Khi sửa navigation hoặc thêm screen mới, phải rà soát toàn bộ flow liên quan để bảo đảm mọi màn không thuộc root-level đều có cách quay lại rõ ràng ngay trên UI.
- Nếu app có cơ chế chặn hoặc override back toàn cục, agent phải giữ logic đó nhất quán với các nút điều hướng trên UI thay vì để người dùng phụ thuộc vào back cứng.
