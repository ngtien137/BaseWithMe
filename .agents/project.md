# Project Snapshot

## Repo
- Ten repo: `BaseWithMe`
- Thuc te branch hien tai: chi co `master` va `origin/master`
- Khong co cap `dev/main` trong repo nay

## Modules
- `baseme`: Android library dung lam phan reusable/base code, day moi la module uu tien
- `app`: demo host app, chi dung de smoke test va minh hoa cach dung library

## Dinh huong lam viec
- Khi audit/sua code, uu tien `baseme` truoc
- `app` khong phai source of truth cho kien truc
- Khong xoa file/thu muc neu chua co user confirm ro rang

## Build / Toolchain Snapshot
- Gradle wrapper: `8.7`
- Android Gradle Plugin: `8.5.2`
- Kotlin Gradle plugin: `1.9.25`
- Root `compileSdkVersion`: `34`
- Root `targetSdkVersion`: `34`
- Root `minSdkVersion`: `17`
- Java/Kotlin target trong module da duoc nang len `17`

## Rang buoc ky thuat hien tai
- `baseme` assemble duoc tren JBR 21
- Repo nay co baseline `minSdk 17` rat cu
- Neu day dependency AndroidX/Jetpack qua moi cho toan repo, `app` demo de gap loi minSdk metadata
- Vi `app` chi la demo, uu tien giu `baseme` build on dinh truoc
