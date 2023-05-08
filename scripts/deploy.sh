#!/bin/bash

# 작업 디렉토리를 /home/ec2-user/app으로 변경
cd /home/ec2-user/app

# 환경변수 DOCKER_APP_NAME을 spring으로 설정
DOCKER_APP_NAME=spring


# 실행중인 blue가 있는지 확인
# 프로젝트의 실행 중인 컨테이너를 확인하고, 해당 컨테이너가 실행 중인지 여부를 EXIST_BLUE 변수에 저장
EXIST_BLUE=$(sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps | grep Up)

# 배포 시작한 날짜와 시간을 기록
echo "배포 시작일자 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> /home/ec2-user/deploy.log

# green이 실행중이면 blue up
# EXIST_BLUE 변수가 비어있는지 확인
if [ -z "$EXIST_BLUE" ]; then

  # 로그 파일(/home/ec2-user/deploy.log)에 "blue up - blue 배포 : port:8081"이라는 내용을 추가
  echo "blue 배포 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> /home/ec2-user/deploy.log

	# docker-compose.blue.yml 파일을 사용하여 spring-blue 프로젝트의 컨테이너를 빌드하고 실행
	sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml up -d --build

  # 30초 동안 대기
  sleep 30

  # /home/ec2-user/deploy.log: 로그 파일에 "green 중단 시작"이라는 내용을 추가
  echo "green 중단 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> /home/ec2-user/deploy.log

  # docker-compose.green.yml 파일을 사용하여 spring-green 프로젝트의 컨테이너를 중지
  sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml down

   # 사용하지 않는 이미지 삭제
  sudo docker image prune -af

  echo "green 중단 완료 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> /home/ec2-user/deploy.log

# blue가 실행중이면 green up
else
	echo "green 배포 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> /home/ec2-user/deploy.log
	sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml up -d --build

  sleep 30

  echo "blue 중단 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> /home/ec2-user/deploy.log
  sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml down
  sudo docker image prune -af

  echo "blue 중단 완료 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> /home/ec2-user/deploy.log

fi
  echo "배포 종료  : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> /home/ec2-user/deploy.log

  echo "===================== 배포 완료 =====================" >> /home/ec2-user/deploy.log
  echo >> home/ec2-user/deploy.log