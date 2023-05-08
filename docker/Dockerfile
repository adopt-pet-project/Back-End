# openjdk:11을 기반으로 하는 Docker 이미지를 사용
FROM openjdk:11

# JAR_FILE 이라는 빌드 인수를 정의하고, 기본 값으로 *.jar로 정의한다.
ARG JAR_FILE=*.jar

# 빌드 컨텍스트에 있는 ${JAR_FILE} 경로의 JAR 파일을 이미지 내부의 경로에 복사한다.
COPY ${JAR_FILE} app.jar

# 컨테이너가 실행될 때 java -jar app.jar 명령이 실행되도록 설정
ENTRYPOINT ["java","-jar","app.jar"]