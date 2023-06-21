# Pet-Hub Project

<img width="600" alt="Pet-Hub" src="https://github.com/adopt-pet-project/Back-End/assets/91787050/28bdcadc-d8da-40cc-a858-2e447636037a"/>

## 👆 Introduce
개인과 개인간의 반려동물 분양 중개 서비스를 제공하는 분양 플랫폼입니다.

### 서비스 링크
서비스 링크 👉 [Pet-Hub로 이동](https://pet-hub.site)


### 팀 구성
백엔드 개발자 2명, 프론트엔드 개발자 2명이 진행한 프로젝트 입니다.


### 주요 기능
- 작성된 분양 게시글에 실시간 채팅을 이용한 문의가 가능하고, 채팅을 상대가 읽었는지 여부를 확인할 수 있는 안읽음 표시 기능이 제공됩니다.
- 게시글에 댓글이 달리거나, 채팅방에 새로운 채팅이 있을 경우 SSE를 이용한 실시간 알람이 발송됩니다.
- 게시글과, 분양글을 작성하고 사진을 업로드 할 수 있습니다. 게시글과 분양글에 좋아요를 눌러줄 수 있습니다.
- Google, Naver, Kakao 3가지 플랫폼으로 간편 로그인을 지원하고 있습니다.


## 🚀 Back-End Skills

<img width="1000" alt="Pet-Hub" src="https://github.com/adopt-pet-project/Back-End/assets/91787050/daa077ad-9561-490f-b854-3fccd194242d">

- Open JDK 11
- Spring Boot 2.7.11
- MySQL
- MongoDB
- Spring Data JPA & QueryDsl
- Spring Security & OAuth 2
- Spring Data Redis
- Spring Rest Docs
- AWS Infra (EC2, S3, IAM, CodeDeploy, Route 53)
- Docker(Docker Compose)
- Kafka & Stomp
- NginX
- Prometheus & Grafana
- Github Actions
- Web Socket

## ✍ Achieved

### 트러블슈팅 & 요구사항 해결
-  [Blue/Green 배포시 런타임 에러로 인한 서버다운 문제해결](https://velog.io/@ch4570/Github-Actions-Nginx%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-CICD-%EB%AC%B4%EC%A4%91%EB%8B%A8-%EB%B0%B0%ED%8F%AC-%EC%9E%90%EB%8F%99%ED%99%94-%EA%B5%AC%EC%B6%95-%EC%A7%84%EC%A7%9C-%EB%AC%B4%EC%A4%91%EB%8B%A8-%EB%B0%B0%ED%8F%AC%EA%B0%80-%EB%A7%9E%EC%9D%84%EA%B9%8Cfeat.-%EB%9F%B0%ED%83%80%EC%9E%84-%EC%97%90%EB%9F%AC) 
-  [MySQL 데이터 분산 처리를 위한 Master-Slave 이중화 구성](https://velog.io/@ch4570/MySQL-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EB%B6%84%EC%82%B0-%EC%B2%98%EB%A6%AC%EB%A5%BC-%EC%9C%84%ED%95%9C-Master-Slave-%EC%9D%B4%EC%A4%91%ED%99%94%EB%A5%BC-%EA%B5%AC%EC%84%B1%ED%95%98%EB%8B%A4MySQL-Replication-%EC%84%A4%EC%A0%95%EA%B3%BC-%EA%B5%AC%EC%84%B1) 
-  [AOP를 활용한 로깅 기능 분리하기](https://velog.io/@ch4570/Spring-AOP%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-%EB%A1%9C%EA%B9%85-%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0) 
-  [채팅 안읽음 기능 구현을 위한 채팅방 접속자 관리하기](https://velog.io/@ch4570/Pet-Hub-11-%EC%B1%84%ED%8C%85-%EA%B8%B0%EB%8A%A5%EC%97%90%EC%84%9C-%EC%B1%84%ED%8C%85-%EC%9D%BD%EC%9D%8C-%EC%97%AC%EB%B6%80-%ED%91%9C%EC%8B%9C%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84)
-  [채팅방 퇴장시 Authorization Header를 포함하지 못하는 문제 해결](https://velog.io/@ch4570/Pet-Hub-%EC%B1%84%ED%8C%85%EB%B0%A9-%ED%87%B4%EC%9E%A5%EC%8B%9C-Disconnect-Header%EC%97%90-Authorization-Header%EB%A5%BC-%ED%8F%AC%ED%95%A8%ED%95%98%EC%A7%80-%EB%AA%BB%ED%95%98%EB%8A%94-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0)
-  [Spring Security 환경에서 Spring Rest Docs Custom 인증객체 사용 문제 해결](https://velog.io/@ch4570/Pet-Hub-Spring-RestDocs-%EB%AC%B8%EC%84%9C%ED%99%94%EB%A5%BC-%EC%9C%84%ED%95%9C-Custom-Security-%EC%9D%B8%EC%A6%9D%EA%B0%9D%EC%B2%B4-%EC%A3%BC%EC%9E%85%ED%95%98%EA%B8%B0)
-  [DB Server 인스턴스의 용량 부족으로 인해 발생한 PessimisticLockException 문제 해결](https://velog.io/@ch4570/Pet-Hub-Lock%EC%9D%84-%EA%B1%B8%EC%A7%80-%EC%95%8A%EC%95%84%EB%8F%84-%EB%B0%9C%EC%83%9D%ED%95%9C-PessimisticLockException%ED%95%B4%EA%B2%B0)
-  [SSE(Sever-Sent-Event)를 이용한 실시간 알림 구현시 문제 해결](https://wimoney.tistory.com/entry/Pet-Hub-SSE-%EC%8B%A4%EC%8B%9C%EA%B0%84-%EC%95%8C%EB%A6%BC-%EA%B5%AC%ED%98%84%EC%8B%9C-%ED%8A%B8%EB%9F%AC%EB%B8%94-%EC%8A%88%ED%8C%85)


### ERD

<img width="800" alt="Pet-Hub" src="https://github.com/adopt-pet-project/Back-End/assets/91787050/81e1aa75-eb02-40c2-bfcf-a3cc1626c644">

### API 명세 문서화

- [Spring Rest Docs로 정리한 API 명세 문서화](https://adopt-pet-project.github.io/Back-End/docs/)

<img width="800" alt="스크린샷 2023-06-19 오후 5 30 32" src="https://github.com/adopt-pet-project/Back-End/assets/91787050/1e12a97e-2784-46e6-a145-de6d157ff8a2">


