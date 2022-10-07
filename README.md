# Siders
## 조금 더 구체적인 사이드 프로젝트 관련 웹 앱

#### 프로젝트 정리 | [노션](https://kimsy8979.notion.site/Siders-2a7e19b7d306490299186c8447105c09)


## 팀 구성
- UI/UX : 
- Frontend : 
- Backend : [김설영](https://github.com/SeolYoungKim)


## 기술 스택 (사용중 및 사용 예정)

### 사용 중
![SpringBoot](https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=SpringBoot&logoColor=white)
![SpringSecurity](https://img.shields.io/badge/SpringSecurity-6DB33F?style=flat-square&logo=SpringSecurity&logoColor=white)
![OAuth](https://img.shields.io/badge/OAuth-6DB33F?style=flat-square&logo=OAuth&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-6DB33F?style=flat-square&logo=JPA&logoColor=white)
![QDSL](https://img.shields.io/badge/QDSL-512BD4?style=flat-square&logo=QueryDSL&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-017CEE?style=flat-square&logo=QueryDSL&logoColor=white)
![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=flat-square&logo=MariaDB&logoColor=white)

![AWS_EC2](https://img.shields.io/badge/AWS_EC2-FF9900?style=flat-square&logo=AmazonEC2&logoColor=white)
![AWS_S3](https://img.shields.io/badge/AWS_S3-569A31?style=flat-square&logo=AmazonS3&logoColor=white)
![AWS_CodeDeploy](https://img.shields.io/badge/AWS_CodeDeploy-232F3E?style=flat-square&logo=AmazonCodeDeploy&logoColor=white)
![GitHubActions](https://img.shields.io/badge/GitHubActions-2088FF?style=flat-square&logo=GitHubActions&logoColor=white)


### 사용 예정

![Redis](https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=Redis&logoColor=white)
![DOCKER](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white)


## 개발 이유

게시판을 구현하고 배포한 후, RESTful에 대한 경험이 해보고 싶어졌습니다. 
서버사이드 렌더링이 아닌, REST API 통신을 통한 웹 앱은 어떻게 설계되고 구현되는지가 궁금했습니다. 
혼자서도 해볼 수 있었겠지만, 협업을 통해 무언가를 만들어보고 싶다는 생각이 있었고, 이를 바로 실행에 옮겨 UI/UX 디자이너 한 분과 프론트엔드 개발자 한 분을 모시고 사이드 프로젝트 팀을 하나 결성하게 되었습니다.

개발자에게 사이드 프로젝트란, 성장을 위해서는 없어서는 안되는 존재라고 생각합니다.
하지만, 여러 사이트들이나 카페들이 사이드 프로젝트 구인 혹은 참여가 너무 불편하게 구성되어 있다고 생각했습니다.

그래서 이를 팀원분들과 함께 해결해보고자 "조금 더 구체적인 사이드 프로젝트 관련 웹 앱"인 "Siders"를 만들게 되었습니다.


## 간단한 소개

마주한 문제
- 사이드 프로젝트 구인 사이트에 참여 조건이 적혀있으나, 너무 추상적이다.
- 내가 신청해도 되는 것일까? 라는 의문이 들어 사이드 프로젝트 참여 신청을 넣기가 꺼려진다.
- 프로젝트 운영 정보가 부족한 곳도 많다.

해결 방안
- 참여 조건에 대한 정보를 좀 더 구체적으로 제공해보자.
- 내 수준에 대한 객관적인 지표가 필요하다고 판단 -> 각 분야에 대한 이해도나, 숙련도 등을 수치화 (Low, Mid, High)
- 프로젝트 운영 최소 정보를 기입하도록 유도 -> 기본 작성 틀을 부담스럽지 않게 마련


## 프로젝트 구조

```bash

# Directory tree
.
├── SidersWebappApplication.java
├── auth
│   ├── SecurityConfig.java
│   ├── UriList.java
│   ├── jwt
│   │   ├── JwtExceptionFilter.java
│   │   ├── JwtFilter.java
│   │   └── JwtProvider.java
│   └── oauth
│       ├── handler
│       └── service
├── config
│   ├── JpaConfig.java
│   ├── QuerydslConfig.java
│   └── WebConfig.java
├── domain
│   ├── Ability.java
│   ├── BaseTimeEntity.java
│   ├── FieldsType.java
│   ├── RecruitType.java
│   ├── fields
│   │   └── Fields.java
│   ├── member
│   │   ├── Member.java
│   │   └── RoleType.java
│   ├── post
│   │   └── Post.java
│   └── tech_stack
│       └── TechStack.java
├── exception
│   ├── ErrorUtils.java
│   ├── IsNotOwnerException.java
│   ├── JwtNotAvailable.java
│   ├── MemberNotFoundException.java
│   ├── PostNotFoundException.java
│   └── SidersException.java
├── repository
│   ├── fields
│   │   └── FieldsRepository.java
│   ├── member
│   │   └── MemberRepository.java
│   ├── post
│   │   ├── PostRepository.java
│   │   ├── PostRepositoryCustom.java
│   │   └── PostRepositoryImpl.java
│   └── tech_stack
│       └── TechStackRepository.java
├── service
│   ├── member
│   │   └── MemberService.java
│   └── post
│       └── PostService.java
└── web
    ├── controller
    │   ├── ExceptionController.java
    │   ├── MemberController.java
    │   ├── PostController.java
    │   └── ProfileController.java
    ├── request
    │   ├── FieldErrorMessage.java
    │   ├── member
    │   └── post
    └── response
        ├── exception
        ├── member
        └── post
```
