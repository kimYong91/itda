### 운동을 어떻게 할지 모르는 사람들을 위한 맞춤 운동 보조 어플 "잇다"
* 기획 및 개발 기간 : 2024.06.11 ~ 07.05
* 배포주소 : http://itda-app-apk-bucket.s3-website.ap-northeast-2.amazonaws.com/

## 개발 동기
1. 취향에 맞는 운동 정보
2. 식단 관리 및 음식 칼로리 정보
3. chatGPT 활용

## 구현 목표
1. 사용자의 건강 정보를 기반으로 맟춤 운동 및 식단 스케줄 생성
2. 사용자의 실제 섭취 음식을 기록하고 칼로리를 계산

## 프로젝트 구조
<details>
  <summary> 프로젝트 구조 </summary>

├── .gitignore  
├── .idea  
│ ├── .gitignore  
│ ├── .name  
│ ├── compiler.xml  
│ ├── deploymentTargetSelector.xml  
│ ├── gradle.xml  
│ ├── kotlinc.xml  
│ ├── migrations.xml  
│ ├── misc.xml  
│ ├── other.xml  
│ └── vcs.xml  
├── README.md  
├── app  
│ ├── .gitignore  
│ ├── build.gradle.kts  
│ ├── proguard-rules.pro  
│ └── src  
│ ├── androidTest  
│ │ └── java  
│ │ └── com  
│ │ └── itda  
│ │ └── android_c_teamproject  
│ │ └── ExampleInstrumentedTest.kt  
│ ├── main  
│ │ ├── AndroidManifest.xml  
│ │ └── java  
│ │ └── com  
│ │ └── itda  
│ │ └── android_c_teamproject  
│ │ ├── Activity  
│ │ │ ├── AddMealActivity.kt  
│ │ │ ├── ChatMainActivity.kt  
│ │ │ ├── CounterActivity.kt  
│ │ │ ├── DeleteUserActivity.kt  
│ │ │ ├── DietActivity.kt  
│ │ │ ├── FindUserNameActivity.kt  
│ │ │ ├── FindUserPasswordActivity.kt  
│ │ │ ├── FirstActivity.kt  
│ │ │ ├── LoginActivity.kt  
│ │ │ ├── MealActivity.kt  
│ │ │ ├── MealDetailActivity.kt  
│ │ │ ├── MemoActivity.kt  
│ │ │ ├── PedometerActivity.kt  
│ │ │ ├── PopupChatActivity.kt  
│ │ │ ├── RegisterActivity.kt  
│ │ │ ├── StopWatchActivity.kt  
│ │ │ ├── UpdateUserHealthActivity.kt  
│ │ │ └── UpdateUserPersonalActivity.kt  
│ │ ├── adapter  
│ │ │ ├── FoodAdapter.kt  
│ │ │ └── MealAdapter.kt  
│ │ ├── model  
│ │ │ ├── ChatRequest.kt  
│ │ │ ├── Diet  
│ │ │ │ ├── DateTypeConverter.kt  
│ │ │ │ ├── MealDao.kt  
│ │ │ │ ├── MealDatabase.kt  
│ │ │ │ └── SharedViewModel.kt  
│ │ │ ├── LoginRequest.kt  
│ │ │ ├── Meal.kt  
│ │ │ ├── Response  
│ │ │ │ ├── ChatResponse.kt  
│ │ │ │ ├── LoginResponse.kt  
│ │ │ │ ├── UserFindNameResponse.kt  
│ │ │ │ └── UserFindPasswordResponse.kt  
│ │ │ ├── User.kt  
│ │ │ └── dto  
│ │ │ ├── FoodDTO.kt  
│ │ │ ├── UserDTO.kt  
│ │ │ ├── UserFindNameDTO.kt  
│ │ │ ├── UserFindPasswordDTO.kt  
│ │ │ ├── UserHealthDTO.kt  
│ │ │ ├── UserPersonalDTO.kt  
│ │ │ └── UserUsedNameDTO.kt  
│ │ ├── network  
│ │ │ ├── ApiClient.kt  
│ │ │ ├── ApiService.kt  
│ │ │ ├── DietRetrofitClient.kt  
│ │ │ ├── OpenAIService.kt  
│ │ │ ├── RetrofitClient.kt  
│ │ │ └── RetryInterceptor.kt  
│ │ ├── preferences  
│ │ │ └── UserPreferences.kt  
│ │ └── res  
│ │ ├── anim  
│ │ │ ├── scale_down.xml  
│ │ │ └── scale_up.xml  
│ │ ├



</details>

## 주요 기능
1. 회원 정보 관리
    - 개인정보 수정
    - 건강정보 수정, 평균 칼로리 계산
2. 식단 관리
    - 음식 정보
    - 하루 섭취 칼로리 계산
3. 맞춤형 운동 스케줄
    - 운동 취향 선택
    - 생성된 스케줄 저장
4. 운동에 필요한 유틸
    - 카운트
    - 타이머
    - 만보기

## 팀원별 역할
* 김용 (팀 매니져, 로그인과 보안 및 관련 DB)
    - 로그인
        1. [회원가입](https://github.com/kimYong91/itda/blob/develop/app/src/main/java/com/itda/android_c_teamproject/Activity/RegisterActivity.kt)
        2. [로그인, 로그아웃](https://github.com/kimYong91/itda/blob/develop/app/src/main/java/com/itda/android_c_teamproject/Activity/LoginActivity.kt)
        3. [회원 탈퇴](https://github.com/kimYong91/itda/blob/develop/app/src/main/java/com/itda/android_c_teamproject/Activity/DeleteUserActivity.kt)
        4. [아이디 찾기](https://github.com/kimYong91/itda/blob/develop/app/src/main/java/com/itda/android_c_teamproject/Activity/FindUserNameActivity.kt)
        5. [임시 비밀번호 찾기](https://github.com/kimYong91/itda/blob/develop/app/src/main/java/com/itda/android_c_teamproject/Activity/FindUserPasswordActivity.kt)
        6. [건강 정보 업데이트](https://github.com/kimYong91/itda/blob/develop/app/src/main/java/com/itda/android_c_teamproject/Activity/UpdateUserHealthActivity.kt)
        7. [개인 정보 업데이트](https://github.com/kimYong91/itda/blob/develop/app/src/main/java/com/itda/android_c_teamproject/Activity/UpdateUserPersonalActivity.kt)
    - 유틸
        1. [카운트](https://github.com/kimYong91/itda/blob/develop/app/src/main/java/com/itda/android_c_teamproject/Activity/CounterActivity.kt)
        2. [타이머](https://github.com/kimYong91/itda/blob/develop/app/src/main/java/com/itda/android_c_teamproject/Activity/StopWatchActivity.kt)
        3. [메모장](https://github.com/kimYong91/itda/blob/develop/app/src/main/java/com/itda/android_c_teamproject/Activity/MemoActivity.kt)
* 이광민 (운동추천 및 챗봇, 클라우드 서버)
    - ChatGPT API 활용 추천운동 / 챗봇
        1. DB 가입정보 자동 적용된 프롬프트 선택 입력
        2. 선택된 프롬프트 정보 ChatGPT에 전달 및 피드벡 표시
        3. 실행 중지 (Stop), 입력 초기화(Clear)
        4. 실행 오류 관리(ChatGPT 응답시간에 따른 timeout 문제)
        5. 챗봇(사용자 자유 입력)
    - 유틸
      만보기
    - AWS 서버 구축
        1. AWS EC2 (서버)
        2. AWS RDS (MariaDB)
        3. AWS S3(앱 파일(APK) 다운로드 웹페이지)
* 한규철 (식단관리 기능)
    - 식단 관리
        1. [식단 추가 기능](https://github.com/kimYong91/itda/blob/develop/app/src/main/java/com/itda/android_c_teamproject/Activity/AddMealActivity.kt)
        2. [날짜 선택 및 식사 유형 관리](https://github.com/kimYong91/itda/blob/develop/app/src/main/java/com/itda/android_c_teamproject/Activity/DietActivity.kt)
        3. [음식 검색 및 추가](https://github.com/kimYong91/itda/blob/develop/app/src/main/java/com/itda/android_c_teamproject/Activity/MealActivity.kt)
        4. [식사 상세 정보 조회](https://github.com/kimYong91/itda/blob/develop/app/src/main/java/com/itda/android_c_teamproject/Activity/MealDetailActivity.kt)
        5. [음식 목록 필터링 및 추가](https://github.com/kimYong91/itda/blob/develop/app/src/main/java/com/itda/android_c_teamproject/adapter/FoodAdapter.kt)
        6. [식사 목록 관리](https://github.com/kimYong91/itda/blob/develop/app/src/main/java/com/itda/android_c_teamproject/adapter/MealAdapter.kt)

        - [데이터 관리 (Room Database)](https://github.com/kimYong91/itda/tree/develop/app/src/main/java/com/itda/android_c_teamproject/model/Diet)


## 개발환경
- back-end : Intellij IDEA
- front-end : Android Studio
- 서비스 배포 환경 : AWS
- 버전 및 소스코드 관리 : Github
- Java 17
- Gradle - Groovy
- Spring Boot 3.3.1

## 기술스텍

 <img src="https://img.shields.io/badge/amazonaws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white"> 
 <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/mariaDB-003545?style=for-the-badge&logo=mariaDB&logoColor=white">
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
<img src="https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white">
<img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white">


## Class Diagram
![](https://github.com/kimYong91/itda/assets/159098694/b6feb6d3-bde6-41ad-b6ab-2103ce807915)
![](https://github.com/kimYong91/itda/assets/159098694/91a128b5-41de-415e-abe4-77b7437066e0)

## API 명세서
| 기능                                                   |                URL                 |   HTTP Method   |
|:-----------------------------------------------------|:----------------------------------:|:---------------:|
| 유저 건강 정보 (성별, 몸무게, 키)                                |       /itda/oneUserHealthDTO       |       GET       |
| 유저의 모든 정보 (아이디, 비밀번호, 이메일, 휴대폰 번호, 생년월일, 성별, 몸무게, 키) |         /itda/oneUserInfo          |       GET       |
| 유저 이름                                                |         /itda/oneUsername          |       GET       |
| 회원 가입                                                |          /itda/createUser          |      POST       |
| 임시 비밀번호 생성                                           |         /itda/findPassword         |      POST       |
| 아이디 찾기                                               |         /itda/findUsername         |      POST       |
| 회원 탈퇴                                                |          /itda/userDelete          |     DELETE      |
| 유저 방호벽                                               |       /itda/SecurityBarrier        |       GET       |
| 유저 건강 정보 업데이트 (성별, 몸무게, 키)                           | /itda/oneUserHealthDTO/{username}  |      PATCH      |
| 유저 개인 정보 업데이트 (아이디, 비밀번호, 이메일, 휴대폰 번호, 생년월일)         |    /itda/oneUserInfo/{username}    |      PATCH      |
| 회원 인증(JWT토큰)                                         |             /itda/auth             |      POST       |
| (식단 데이터)                                             |             /itda/foods             |      POST       |           

## 시연 영상

<details>
<summary>1. 회원가입, 로그인</summary>

![1  회원가입, 로그인](https://github.com/kimYong91/itda/assets/159098694/4025c91a-8219-4c42-8d9f-2793a49e5d8f)
</details>

<details>
<summary>2. 회원탈퇴</summary>

![2  회원탈퇴](https://github.com/kimYong91/itda/assets/159098694/9c25978f-0ddb-489e-a496-c2c2d77d6687)
</details>

<details>
<summary>3. 개인정보 수정</summary>

![3  개인정보 수정](https://github.com/kimYong91/itda/assets/159098694/350699d6-cc7c-49c7-a829-844212679f57)
</details>

<details>
<summary>4. 건강정보 수정</summary>

![4  건강정보 수정](https://github.com/kimYong91/itda/assets/159098694/08e7e47c-ffa1-4e4b-b7c7-fae01e2f14c1)
</details>

<details>
<summary>5. 추천 운동</summary>

![5  추천 운동](https://github.com/kimYong91/itda/assets/159098694/6620f745-dd22-4272-bb07-53253f7edd1f)
</details>

<details>
<summary>6. 저장된 운동 데이터 확인</summary>

![6  저장된 운동 데이터 확인](https://github.com/kimYong91/itda/assets/159098694/8ef466cf-109b-4eb7-a740-d26331394872)
</details>

<details>
<summary>7. 식단</summary>

![7  식단](https://github.com/kimYong91/itda/assets/159098694/38f11dd1-bdba-4163-9f1e-fdfc2e48922f)
</details>

<details>
<summary>8. 챗봇</summary>

![8  챗봇](https://github.com/kimYong91/itda/assets/159098694/f00e7a81-6dfa-4a27-b5f1-f459cf24eb99)
</details>

<details>
<summary>9. 스탑워치</summary>

![9  스탑워치](https://github.com/kimYong91/itda/assets/159098694/2cb04ee1-c0b7-4be0-ae9c-d4e768afc942)
</details>

<details>
<summary>10. 카운터</summary>

![10  카운터](https://github.com/kimYong91/itda/assets/159098694/77674c8e-9ca1-4b6f-932d-f78e1fe5a384)
</details>

<details>
<summary>11. 만보기</summary>

![11  만보기](https://github.com/kimYong91/itda/assets/159098694/6bf4f24e-696c-4499-a895-447fda671949)
</details>

## 참여자

<table>
  <tr>
    <td align="center">
      <img src="https://avatars.githubusercontent.com/u/159098694?v=4" width="150px;" alt=""/>
      <br />
      <sub><b>김용</b></sub>
      <br />
      <a href="https://github.com/kimYong91" target="_blank">
        <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white" alt="GitHub"/>
      </a>
    </td>
    <td align="center">
      <img src="https://avatars.githubusercontent.com/u/97494784?v=4" width="150px;" alt=""/>
      <br />
      <sub><b>이광민</b></sub>
      <br />
      <a href="https://github.com/kwang-min-lee1" target="_blank">
        <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white" alt="GitHub"/>
      </a>
    </td>
    <td align="center">
      <img src="https://avatars.githubusercontent.com/u/172355491?s=400&u=17573e3c5bcff484af2f43c93d7adbfdbb69afdd&v=4" width="150px;" alt=""/>
      <br />
      <sub><b>한규철</b></sub>
      <br />
      <a href="https://github.com/HansGyu" target="_blank">
        <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white" alt="GitHub"/>
      </a>
    </td>
  </tr>
</table>
