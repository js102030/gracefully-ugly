# 곱게 못난이, gracefully-ugly
### 못난이 농산물 공동구매 사이트
- 최근 치솟는 농산물 가격에 소비자들이 부담을 느끼고 있으니 '공동구매 + 못난이 농산물 구매' 두 가지 장치로 가격 절감을 하자는 아이디어로 이어졌습니다. 판매자는 못났다는 이유로 폐기되는 못난이 농산물로 수익을 창출할 수 있고 구매자는 비싼 농산물 가격 대신 공동구매를 통해 좀 더 싼 가격으로 농산물을 구매할 수 있습니다.
- 판매자는 원하는 공동구매 조건을 정해 인원을 모집할 수 있고, 구매자는 공동구매에 자유롭게 참여할 수 있습니다.
- 이스트소프트의 앨런 AI 기능을 사용해 메인 페이지에서 농산물 관련 최신 뉴스를 확인할 수 있습니다.

- http://15.164.14.204:8080/
- 관리자 계정 admin/admin
- 상품 신고 관리 http://15.164.14.204:8080/admin-report
- 판매자 계정 seller/seller , seller100/seller100 , seller200/seller200 , hello/hello    ...


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
	    <td><img src="readme_img/jj.jpg" style="width: 500px" alt="박준석"></td>
	    <td><img src="readme_img/Jung_Jinhwan.jpg" style="width: 600px" alt="정진환"></td>
	    <td><img src="readme_img/seokhyun.jpg" style="width:190px" alt="류석현"></td>
	    <td><img src="readme_img/Lee_Hyunjin.jpg" style="width: 600px" alt="이현진진"></td>
	    <td>사진</td>
    </tr>
    <tr>
	    <td>ERD설계/프로젝트 배포/이스트소프트 앨런API 뉴스/로그/인증/신고/리뷰/QnA/유저/상품/이미지 등의 API 개발/테스트 코드 작성</td>
	    <td>찜 목록,주문/결제/공동구매 프로세스 개발 및 카카오 페이 API 연동</td>
	    <td>JWT, OAuth2 활용한 회원가입/로그인/로그아웃 개발, 상품 문의, 코드 리팩토링, 버그 관리, 발표</td>
	    <td>메인 화면 CRUD 개발/버그 관리/프로젝트 산출물 작업</td>
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
### 구매자 화면
|                             로그인 페이지                              |                          회원가입(1차) 페이지                           |
|:----------------------------------------------------------------:|:---------------------------------------------------------------:|
| <img src="readme_img/front_img/login.jpg" style="width: 1000px"> | <img src="readme_img/front_img/join.jpg" style="width: 1000px"> |

|                           회원가입(2차) 페이지                           |
|:----------------------------------------------------------------:|
| <img src="readme_img/front_img/join2.jpg" style="width: 1000px"> |

|                                메인 페이지                                |                                 공동 구매 페이지                                 |
|:--------------------------------------------------------------------:|:-------------------------------------------------------------------------:|
| <img src="readme_img/front_img/main_page.jpg" style="width: 1000px"> | <img src="readme_img/front_img/group_buy_page.jpg" style="width: 1000px"> |

|                              주문서 작성 페이지                               |                                 주문 완료 페이지                                 |
|:---------------------------------------------------------------------:|:-------------------------------------------------------------------------:|
| <img src="readme_img/front_img/order_page.jpg" style="width: 1000px"> | <img src="readme_img/front_img/order_complete.jpg" style="width: 1000px"> |

|                              주문서 조회 페이지                               |                               주문서 수정 페이지                                |
|:---------------------------------------------------------------------:|:-----------------------------------------------------------------------:|
| <img src="readme_img/front_img/order_view.jpg" style="width: 1000px"> | <img src="readme_img/front_img/order_modify.jpg" style="width: 1000px"> |

|                            찜 목록 페이지                             |                                  주문 내역 페이지                                  |
|:---------------------------------------------------------------:|:---------------------------------------------------------------------------:|
| <img src="readme_img/front_img/cart.jpg" style="width: 1000px"> | <img src="readme_img/front_img/purchase_history.jpg" style="width: 1000px"> |

|                              리뷰 페이지                               |                              신고 페이지                               |
|:-----------------------------------------------------------------:|:-----------------------------------------------------------------:|
| <img src="readme_img/front_img/review.jpg" style="width: 1000px"> | <img src="readme_img/front_img/report.jpg" style="width: 1000px"> |

|                              내 정보 페이지                              |                                회원 정보 수정 페이지                                 |
|:------------------------------------------------------------------:|:---------------------------------------------------------------------------:|
| <img src="readme_img/front_img/my_page.jpg" style="width: 1000px"> | <img src="readme_img/front_img/user_info_modify.jpg" style="width: 1000px"> |

|                               이메일 인증 페이지                                |
|:-----------------------------------------------------------------------:|
| <img src="readme_img/front_img/verify_email.jpg" style="width: 1000px"> |

### 판매자 화면
|                               판매 리스트 페이지                               |                               상세 판매 내역 페이지                               |
|:----------------------------------------------------------------------:|:------------------------------------------------------------------------:|
| <img src="readme_img/front_img/seller_list.jpg" style="width: 1000px"> | <img src="readme_img/front_img/seller_detail.jpg" style="width: 1000px"> |

|                                판매글 작성 페이지                                |                                     판매글 수정 페이지                                     |
|:------------------------------------------------------------------------:|:----------------------------------------------------------------------------------:|
| <img src="readme_img/front_img/sales_product.jpg" style="width: 1000px"> | <img src="readme_img/front_img/modify_product_describe.jpg" style="width: 1000px"> |

### 관리자 화면
|                                  관리자 회원 관리 페이지                                   |                              관리자 신고 관리 페이지                              |
|:--------------------------------------------------------------------------------:|:-----------------------------------------------------------------------:|
| <img src="readme_img/front_img/admin_user_management.jpg" style="width: 1000px"> | <img src="readme_img/front_img/admin_report.jpg" style="width: 1000px"> |

## 시스템 구조도
![system_architecture.jpg](readme_img%2Fsystem_architecture.jpg)
## erd 
링크 : https://www.erdcloud.com/d/Ry8D7FKy5oLp4nBSd
![Ry8D7FKy5oLp4nBSd](https://github.com/gracefully-ugly/gracefully-ugly/assets/121748946/618e839e-4caa-4b57-8785-b84bf0860494)

## api 명세
링크 : http://15.164.14.204:8080/swagger-ui/index.html
![image](https://github.com/gracefully-ugly/gracefully-ugly/assets/138424719/727bc9dd-16ba-42f4-9eed-be69ad41d153)

## 시연 영상
[![Video Label](http://img.youtube.com/vi/A6OFAHPrE4s/0.jpg)](https://youtu.be/A6OFAHPrE4s)
<br>
https://youtu.be/A6OFAHPrE4s
