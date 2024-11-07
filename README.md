# nanglang-eats
프로젝트 소개글 작성
```
- 반드시 포함해야 할 것
  - 팀원 역할분담
  - 서비스 구성 및 실행방법
  - 프로젝트 목적/상세
  - ERD
  - 기술 스택
  - (선택)API docs
```

## R&R
역할분담 작성

## Dependency
- Java SDK 17
- ㅇㅇㅇ

## Git Convention

### Branch Rule

- `main`         : 최종 확인 완료
- `hotfix`       : 최종 배포 버전에서의 오류 긴급 수정
- `develop`      : 기능 개발 및 수정, 오류 해결 완료 버전
- `feature`      : develop에서 따서 각자 작업 (feature/작업내용)
    ```
    ex1) feature/asset-vm
    ex2) feature/fix-login-error
    ex3) feature/mod-properties
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
| | | +-- common
| | | | +-- util
| | | +-- controller
| | | +-- service
| | | | +-- admin    // 기능(메뉴) 단위로 패키지 생성
| | +-- resources
```

### Naming Rule

**기본 규칙**
- 클래스명 : Pascal Case 사용 `ex) UserService.java`
- 메소드명 : Camel Case 사용 `ex) getUser()`
- DB스키마명 : Snake Case & Upper Case 사용 `ex) USER_PERMITION`

**메소드**
- getXxxList : 목록 조회
- getXxxDetail : 단건, 상세 조회
- insertXxx : 등록
- updateXxx : 수정
- deleteXxx : 삭제

## Deploy
배포 매뉴얼 작성

## Test
테스트 매뉴얼 작성
