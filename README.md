## Dokcer , Github Action, AWS EC2 (FreeTier)
- 위의 기술들을 활용하여 간단한 AWS 서버를 구축해보았습니다.
- **AWS EC2 프리티어의 비용은 약 1000원이 들었습니다.** 😊

---

서버 구축과정은 아래의 로직과 같습니다.
- Github Action을 활용하여 Git Repository에 Push시 아래의 로직들이 수행됩니다.
  - AWS 접속에 필요한 Private Key나 기타 개인정보들은 `Github Secrets and variables`에 등록하여 사용했습니다.
- Dokcer Hub에 Spring Boot Dokcer image를 배포합니다.
- AWS 인스턴스에 접속하여 Spring Boot 구동에 필요한 환경을 세팅합니다. 
  - 이후 Dokcer Hub에서 배포한 Image를 내려받아 Container를 구동합니다.


```yml

name: Spring + Docker + AWS EC2

on:
  push:
    branches: [ "master" ]

permissions:
  contents: read

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v3

      - name: SetUp Java 17 😊
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: SetUp Java 17 😊
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Grant execute permission for gradlew 👍
        run: chmod +x gradlew

      - name: Build with Gradle 👍
        run: ./gradlew build -x test

      - name: Docker Image Build and Push to Docker Hub 👍
        run: |
          docker login -u ${{ secrets.DOCKER_USERNAME }} -p ${{ secrets.DOCKER_PASSWORD }}
          docker build -t app .
          docker tag app ${{ secrets.DOCKER_USERNAME }}/${{ secrets.DOCKER_IMAGE }}
          docker push ${{ secrets.DOCKER_USERNAME }}/${{ secrets.DOCKER_IMAGE }}

      - name: Deploy to AWS EC2 👍
        uses: appleboy/ssh-action@v1.0.3
        with:
          host: ${{ secrets.AWS_HOST }}
          username: ubuntu
          key: ${{ secrets.AWS_SECRET_KEY }}
          port: ${{ secrets.AWS_PORT }}
          script: |
            sudo docker rm -f $(docker ps -qa)
            sudo docker pull ${{ secrets.DOCKER_USERNAME }}/${{ secrets.DOCKER_IMAGE }}
            docker-compose up -d
            docker image prune -f


```
