# Working Status

Cap nhat cuoi: `2026-03-30`

## Da lam
- Go co che Auto ViewModel runtime khoi `baseme`
- Doi app demo sang cach tao ViewModel explicit bang factory
- Nang build/dependency stack cua `baseme`
- Nang toolchain goc cua repo de chay duoc voi JBR 21
- Va cham tay vao mot so file source trong `baseme` de sua incompatibility lo ra sau khi nang stack

## Da verify
- `:baseme:assembleDebug --no-daemon` pass tren JBR 21
- Da push commit `e6bebf3` len `origin/master`

## Diem can nho
- `AGENTS.md` local khong duoc dua vao commit push tru khi user noi ro
- Cac file `Auto.kt`, `AutoExt.kt`, `AutoFactory.kt` dang o trang thai placeholder, chua xoa file vi chua co user approval cho thao tac xoa
- `app` demo van co kha nang fail full build neu tiep tuc nang dependency moi ma van giu `minSdk 17`

## Viec con ton dong hop ly nhat
- Sua cac deprecated/lint issue that trong `baseme`, bat dau tu `BaseFragment`
- Hien dai hoa permission/back handling trong `baseme`
- Neu can lam sach hon nua, xin user phep xoa han cac file Auto placeholder
