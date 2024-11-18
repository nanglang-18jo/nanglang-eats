# nanglang-eats

스파르타 내일배움캠프 Spring 심화 2기 낭랑 18조의 음식 주문 앱 **낭랑이츠(Nang lang Eats)** 프로젝트입니다.

## R&R

| [신진우](https://github.com/sjw0851)                                                                                                                            | [김해나](https://github.com/gogohaena)                                                                                                    | [안주환](https://github.com/Hut234)                                                                       | [이민정](https://github.com/M1ngD0ng)                                                                       
|------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|
| |
| • ERD 설계 <br>• AI 메뉴 설명 생성<br>• 리뷰 관리 기능 <br>   | • ERD 설계<br>•주문 관리 기능 <br>•결제 관리 기능 <br> •   | • ERD 설계<br>•인증 · 인가 기능 구현 <br>•유저(고객, 관리자, 매니저, 마스터) 기능 구현 <br>•리뷰 조회 기능 구현 <br>• AWS 배포                                            |• ERD 설계<br>•가게 관리 기능 구현 <br>•상품 관리 기능 구현 <br>•리뷰 관리 기능 구현                                                                                                          |
### 개발 기술과 환경

![AMAZONAWS](https://img.shields.io/badge/amazonaws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)


![JAVA](https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white)
![SpringBoot](https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![SpringSecurity](https://img.shields.io/badge/springsecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![GoogleGemini](https://img.shields.io/badge/googlegemini-8E75B2?style=for-the-badge&logo=googlegemini&logoColor=white)


![Github](https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white)
![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white)
![Slack](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=Slack&logoColor=white)
![Swagger](https://img.shields.io/badge/swagger-85EA2D?style=for-the-badge&logo=swaggeri&logoColor=white)

### 개발 일정

2024년 11월 06일 ~ 2024년 11월 18일

## 서비스 구성 및 실행 방법



## 프로젝트 목적/상세
- **프로젝트 개요**
    - **주제:** 배달 및 포장 음식 주문 관리 플랫폼 개발
    - **목표:** 광화문 근처에서 운영될 음식점들의 배달 및 포장 주문 관리, 결제, 그리고 주문 내역 관리 기능을 제공하는 플랫폼 개발
- **운영 지역**
    - **지역:** 초기에는 광화문 근처로 한정하여 운영하며, 향후 확장을 고려한 지역 분류 시스템 설계 필요
    - ***향후 확장성**: 지역별 필터링, 지역정보 수정 및 추가 등이 가능 하도록 고려*
- **음식점 분류**
    - **카테고리:** 다음과 같은 음식점 카테고리로 분류
        - 한식
        - 중식
        - 분식
        - 치킨
        - 피자
    - **향후 확장성:** 음식점 카테고리를 추가하거나 수정할 수 있도록 유연한 데이터 구조 설계 필요
- **결제 시스템**
    - **결제 방식:** 카드 결제만 가능
    - **PG사 연동:** PG사와의 결제 연동은 외주 개발로 진행하며, 결제 관련 내역만 플랫폼의 데이터베이스에 저장
    - **결제 테이블:** 결제 내역을 저장하기 위한 전용 테이블 설계

- **주문 관리**
    - **주문 취소:** 주문 생성 후 5분 이내에만 취소 가능하도록 제한
    - **주문 유형:** 온라인 주문과 대면 주문(가게에서 직접 주문) 모두 지원
    - **대면 주문 처리:** 가게 사장님이 직접 대면 주문을 접수

- **데이터 보존 및 삭제 처리**
    - **데이터 보존:** 모든 데이터는 완전 삭제되지 않고 숨김 처리로 관리
    - **상품 숨김:** 개별 상품도 숨김 처리 가능하도록 구현(숨김과 삭제는 다른 필드에서 동작해야함)
    - **데이터 감사 로그:** 모든 정보에 생성일, 생성 아이디, 수정일, 수정 아이디, 삭제일, 삭제 아이디를 포함

- **접근 권한 관리**
    - **고객:** 자신의 주문 내역만 조회 가능
    - **가게 주인:** 자신의 가게 주문 내역, 가게 정보, 주문 처리 및 메뉴 수정 가능
    - **관리자:** 모든 가게 및 주문에 대한 전체 권한 보유
- **배송지 정보**
    - ***필수 입력 사항:** 주소지, 요청 사항*
    - *‘주문’ 과 ‘배달’ 에 모두 관련된 정보 입니다!*

- **AI API 연동**
    - **상품 설명 자동 생성:** AI API를 연동하여 가게 사장님이 상품 설명을 쉽게 작성할 수 있도록 지원
    - **AI 요청 기록:** AI API 요청 질문과 대답은 모두 데이터베이스에 저장
## Dependency

- Java SDK 17
- SpringBoot jpa
- SpringBoot Security
- SPringBoot validation
- SpringBoot test
- postgresql
- QueryDSL
- jsonwebtoken
- wbemvc
- aws

## ERD
![18조 ERD 설계서 최종](https://github.com/user-attachments/assets/d1739a04-c064-4100-9723-696089bb815b)

## Swagger
[nanglang-eats Swagger문서](http://43.200.171.152/swagger-ui/index.html)

## Git Convention

### Branch Rule

- `main`         : 최종 확인 완료
- `hotfix`       : 최종 배포 버전에서의 오류 긴급 수정
- `dev`      : 기능 개발 및 수정, 오류 해결 완료 버전
- `feat`      : dev에서 따서 각자 작업 (feat/작업내용)
    ```
    ex1) feat/asset-vm
    ex2) feat/fix-login-error
    ex3) feat/mod-properties
    ```

### Commit Rule

**Type**

- `[feat]`        : 새로운 기능, 코드 추가
- `[mod]`         : 기능 개선, 수정, 코드 리팩토링
- `[fix]`         : 버그 수정
- `[etc]`         : 그 외

**Message**

```
[타입] 커밋내용
--blank line--
상세내용...
```

```
ex) 
[feat] Asset Management > VM 조회 기능 추가
 
- VM 목록 조회
- VM 상세 조회
```

```
$ git commit -m "this is Subject
>> 
>> this is Body
>> 
>> this is Footer"

// Github Desktop 사용시 
// Summary(required) 란에 Type: Subject 입력
// Description 란에 Body와 Footer입력 
```

<br />

## Coding Convention

### Project Structure

```
+-- src
| +-- main
| | +-- java.com.sparta.nanglangeats
| | | +-- domain
| | | | +-- auth  // 도메인명
| | | | | +-- controller
| | | | | +-- service
| | | | | +-- repository
| | | | | +-- dto
| | | | | +-- entity
| | | +-- global
| | | | +-- config
| | | | +-- util
```

### Naming Rule

**기본 규칙**

- 클래스명 : Pascal Case 사용 `ex) UserService.java`
- 메소드명 : Camel Case 사용 `ex) getUser()`

**메소드**

- getXxxList : 목록 조회
- getXxxDetail : 단건, 상세 조회
- createXxx : 등록
- updateXxx : 수정
- deleteXxx : 삭제

## Deploy

배포 매뉴얼 작성

## Test

테스트 매뉴얼 작성
