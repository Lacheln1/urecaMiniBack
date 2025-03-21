유레카 미니 프로젝트 7조 백엔드 레포지토리

프로젝트명 : Velog-Back

Velog BenthMarking 을 통한 웹구조 이해 및 CRUD, SHA-256 & Salt 를 이용한 개인정보 암호화


urecaMiniBack <br/>
├── config/   <br/>
│   └── MyConfig.java  <br/>
├── controller/  <br/>
│   └── MemberController.java  <br/>
├── dao/  <br/>
│   ├── LoginDao.java  <br/>
│   ├── MemberDao.java  <br/>
│   └── SaltDao.java  <br/>
├── dto/  <br/>
│   ├── Login.java  <br/>
│   ├── Member.java  <br/>
│   └── Salt.java  <br/>
├── service/  <br/>
│   └── MemberService.java  <br/>
├── util/  <br/>
│   └── JwtUtil.java  <br/>
└── resources/  <br/>
│    ├── mapper/  <br/>
│    └── application.properties  <br/>
│  <br/>
└── uploads  <br/>
       └── iamges.png


개발 환경 : 
Spring boot
MySQL
MyBatis
Java

구현 목록
Post 작성/수정/삭제 = >DB의 Post table에 전송
Member table의 profile_image column과 Post table profile_image column을 MyBatis를 활용하여 동기화

JWT 기반의 회원가입/로그인/로그아웃 => DB의 Member table에 전송
로그인 차단 시스템(5회 이상 실패 시 10분 차단)  => DB의 Member table에 전송

비밀번호 해쉬화 (SHA-256 + Salt)  => DB의 Member table에 전송

사용자 정보 변경(이메일, 비밀번호, 소셜)  => DB의 Member table에 전송

Logic :  JWT 파싱을 통해 이메일을 추출 => DB의 이메일과 비교 검증 이 후 진행
프로필 이미지 업로드/삭제
```
POST /api/members/login
Authorization: none
Body: { "email": "test@ex.com", "password": "12345678" }

PUT /api/members/update-profile
Authorization: Bearer <JWT>
Body: { "username": "NewName", "bio": "Hello!" }
```

프로젝트 회고 <br/>
yongGyu : <br/>

전반적인 회원관리, 인증 시스템, 프로필 관리 기능 등을 구현하면서 MyBatis를 활용한 DB 이용, JWT 이용 방법, 파일 업로드 같은 백엔드 기능 부분을 구현하는 경험이 값진 프로젝트.

JWT 토큰 만료시간, JWT BlackList 등을 구현하지 못한 아쉬움. 여러가지 해커 공격에 여전히 취약한 것에 대한 아쉬움

이번 프로젝트를 통해 풀스택의 마인드를 가진 프론트엔드 개발자가 왜 필요한지 경험할 수 있었음. 이를 통해 앞으로도 지향하는 개발자의 목표를 정할 수 있는 계기가 됨.

무엇보다 좋은 팀원과 함께할 수 있어서 영광이었음.

seonghyun :  <br/>
평소 부족하다고 느꼈던 것들을 이번 프로젝트를 통해서 완벽히 채웠다고는 못하지만 내실이 쌓여가는 느낌을 받았고 코드를 작성하는 스타일이 다르다본니 다양한 방식으로도 구현이 가능하다는걸 느낄 수 있었던 의미있는 프로젝트.

성능 향상 및 보안성 향상 관점에서 접근 자체가 어려웠으며 미흡하다고 스스로가 느껴졌던 것과 구현하지 못했던 기능들이 있어서 아쉬움.

프로젝트를 진행하며 느꼈던 것들을 가지고 주눅들지 않으며 잘 해야겠다는 각오가 생김.
프로젝트 결과물:
https://youtu.be/a5jftCSjDkA
