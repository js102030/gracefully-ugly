# 곱게 못난이, gracefully-ugly
### 못난이 농산물 공동구매 사이트
- 최근 치솟는 농산물 가격에 소비자들이 부담을 느끼고 있으니 '공동구매 + 못난이 농산물 구매' 두 가지 장치로 가격 절감을 하자는 아이디어로 이어졌습니다. 판매자는 못났다는 이유로 폐기되는 못난이 농산물로 수익을 창출할 수 있고 구매자는 비싼 농산물 가격 대신 공동구매를 통해 좀 더 싼 가격으로 농산물을 구매할 수 있습니다.
- 판매자는 원하는 공동구매 조건을 정해 인원을 모집할 수 있고, 구매자는 공동구매에 자유롭게 참여할 수 있습니다.
- 이스트소프트의 앨런 AI 기능을 사용해 메인 페이지에서 농산물 관련 최신 뉴스를 확인할 수 있습니다.

## 팀 소개
<table>
    <tr>
        <th>박준석</th>
        <th>정진환</th>
        <th>류석현</th>
        <th>이현진</th>
	<th>권현수</th>    
    </tr>
    <tr>
	    <td>사진</td>
	    <td><img src="readme_img/Jung_Jinhwan.jpg" style="width: 500px" alt="정진환"></td>
	    <td><img src="readme_img/seokhyun.jpg" style="width: 500px" alt="류석현"></td>
	    <td><img src="readme_img/Lee_Hyunjin.jpg" style="width: 500px" alt="이현진진"></td>
	    <td>사진</td>
    </tr>
    <tr>
	    <td>ERD설계/프로젝트 관리/앨런 뉴스/로그/인증/신고/리뷰/QnA/유저/상품/이미지 등의 API 개발</td>
	    <td>찜 목록,주문/결제/공동구매 프로세스 개발 및 카카오 페이 API 연동</td>
	    <td>JWT, OAuth2 활용한 회원가입/로그인/로그아웃 개발, 코드 리팩토링, 버그 관리</td>
	    <td>메인 화면 CRUD 개발</td>
	    <td>구매내역 페이지 제작</td>
    </tr>
</table>    
    
## 개발 일정
![image](https://github.com/gracefully-ugly/gracefully-ugly/assets/141889885/ef78e070-455a-40ee-8ef5-7214c1d0b327)

## 사용 스택
### Back-end
|   Java   |   Spring   |   Spring Boot   |   MySQL   |   AWS   |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| <div style="display: flex; align-items: flex-start;"><img src="https://techstack-generator.vercel.app/java-icon.svg" alt="icon" width="65" height="65" /></div> | <img alt="spring logo" src="https://www.vectorlogo.zone/logos/springio/springio-icon.svg" height="50" width="50" > | <img alt="spring-boot logo" src="https://t1.daumcdn.net/cfile/tistory/27034D4F58E660F616" width="65" height="65" > | <div style="display: flex; align-items: flex-start;"><img src="https://techstack-generator.vercel.app/mysql-icon.svg" alt="icon" width="65" height="65" /></div> | <div style="display: flex; align-items: flex-start;"><img src="https://techstack-generator.vercel.app/aws-icon.svg" alt="icon" width="65" height="65" /></div> |

### Front-end
|     Html     |     CSS     |     JavaScript     |     Figma     |  
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | 
| <img alt="Html" src ="https://upload.wikimedia.org/wikipedia/commons/thumb/6/61/HTML5_logo_and_wordmark.svg/440px-HTML5_logo_and_wordmark.svg.png" width="65" height="65" /> | <div style="display: flex; align-items: flex-start;"><img src="https://user-images.githubusercontent.com/111227745/210204643-4c3d065c-59ec-481d-ac13-cea795730835.png" alt="CSS" width="50" height="65" /></div> | <div style="display: flex; align-items: flex-start;"><img src="https://techstack-generator.vercel.app/js-icon.svg" alt="icon" width="75" height="75" /></div> | <div style="display: flex; align-items: flex-start;"><img src="https://www.vectorlogo.zone/logos/figma/figma-icon.svg" width="100" height="65"/></div>  |

### Tools
| Github | Discord | Notion | 
| :--------: | :--------: | :------: |
| <img alt="github logo" src="https://techstack-generator.vercel.app/github-icon.svg" width="65" height="65"> | <img alt="Discord logo" src="https://assets-global.website-files.com/6257adef93867e50d84d30e2/62595384e89d1d54d704ece7_3437c10597c1526c3dbd98c737c2bcae.svg" height="65" width="65"> | <img alt="Notion logo" src="https://www.notion.so/cdn-cgi/image/format=auto,width=640,quality=100/front-static/shared/icons/notion-app-icon-3d.png" height="65" width="65"> |

## 기능 명세
### 요구사항 기능 정리
![image](https://github.com/gracefully-ugly/gracefully-ugly/assets/138424719/bc98996b-9695-497f-b75b-ecc97352103d)


### 공동 구매 프로세스 (왼쪽 - 결제 프로세스 / 오른쪽 - 공구 진행 프로세스)
![image](https://github.com/gracefully-ugly/gracefully-ugly/assets/138424719/2bebdbce-e866-4161-82f0-e8fb0c863a00)


## 화면 설계서

페이지 다 완성되면 여기에 기능 명세에 있는 페이지 각각 캡쳐해서 보여주기

## 시스템 구조도
![system_architecture.jpg](readme_img%2Fsystem_architecture.jpg)
## erd 
링크 : https://www.erdcloud.com/d/Ry8D7FKy5oLp4nBSd
![Ry8D7FKy5oLp4nBSd](https://github.com/gracefully-ugly/gracefully-ugly/assets/121748946/618e839e-4caa-4b57-8785-b84bf0860494)

## api 명세
링크 : http://15.164.14.204:8080/swagger-ui/index.html
![image](https://github.com/gracefully-ugly/gracefully-ugly/assets/138424719/727bc9dd-16ba-42f4-9eed-be69ad41d153)

## 시연 영상
[![Video Label](http://img.youtube.com/vi/9vSYWfruOlk/0.jpg)](https://youtu.be/9vSYWfruOlk)
<br>
https://youtu.be/9vSYWfruOlk
