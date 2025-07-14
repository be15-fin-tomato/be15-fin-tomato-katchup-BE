<table>
  <tr>
    <td align="center"><img src="https://github.com/user-attachments/assets/16718841-7a79-4187-86f9-4134cd4aae6c" width="1300" /></td>
    <td align="center"><img src="https://github.com/user-attachments/assets/12bbb73f-0bcf-40bc-ba8b-62eb5d9a2d83" width="1300"/></td>
    <td align="center"><img src="https://github.com/user-attachments/assets/a5947ee8-37e2-4173-a958-75f46a2d2df7" width="1300"/></td>
    <td align="center"><img src="https://github.com/user-attachments/assets/4d70299d-f045-464c-bbb7-270c0b316086" width="1300"/></td>
    <td align="center"><img src="https://github.com/user-attachments/assets/d09d3065-f01a-415c-8c6c-492f6a966d10" width="1300"/></td>
    <td align="center"><img src="https://github.com/user-attachments/assets/4f097953-3860-4d2e-8203-fc791a10ac71" width="1300"/></td>
  </tr>
  <tr>
    <td align="center"><a href="https://github.com/Jangwoo0710">박장우</a></td>
    <td align="center"><a href="https://github.com/Joonspar">박준서</a></td>
    <td align="center"><a href="https://github.com/oyk0510">오유경</a></td>
    <td align="center"><a href="https://github.com/lddocy">윤채영</a></td>
    <td align="center"><a href="https://github.com/kkkwid">이승재</a></td>
    <td align="center"><a href="https://github.com/Cho-Hyun-Seung">조현승</a></td>
  </tr>
</table>

# 🍅 KATCHUP 프로젝트
> **MCN 기업 전용 광고주 관리 및 캠페인 협업을 위한 CRM 플랫폼**

---

## 📍 목차
<a href="#1">1. 프로젝트 기획</a>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#1-1">1-1. 프로젝트 소개</a>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#1-2">1-2. 주요 기능</a>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#1-3">1-3. 기술 스택</a>

<a href="#2">2. 설계 문서</a>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#2-1">2-1. 요구사항 명세서</a>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#2-2">2-2. WBS</a>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#2-3">2-3. ERDCloud</a>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#2-4">2-4. FIGMA 화면 설계서</a>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#2-5">2-5. 아키텍처 구조도</a>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#2-6">2-6. 프로그램 사양서(REST API 명세서)</a>

<a href="#3">3. Back & Front 테스트 결과 </a>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#3-1">3-1. 백엔드 단위 테스트 결과서</a>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#3-2">3-2. 프론트엔드 UI/UX 단위 테스트 결과서</a>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#3-3">3-3. git 형상관리</a>

<a href="#4">4. 시스템 통합 </a>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#4-1">4-1. CI/CD 계획서</a>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#4-2">4-2. 통합 테스트 결과서</a>

<a href="#5">5. 트러블 슈팅 </a>

<a href="#6">6. 팀원 회고 </a>
<br>

---
## <p id="1"> 🪐 1. 프로젝트 기획</p>
## <p id="1-1">1-1. 프로젝트 소개</p>
### 📌 프로젝트 개요

**프로젝트명**: `KATCHUP`  
**주제**: MCN 기업의 광고주 중심 B2B 협업을 효율화하는 CRM 시스템

---

### 🖼️ 프로젝트 배경
KATCHUP은 인플루언서 콘텐츠 관리, 캠페인 성과 분석, 계약서 처리, 커뮤니케이션 이력 등 광고주와의 관계를 중심으로 업무를 통합 관리하는 플랫폼입니다.
MCN(Multi-Channel Network) 기업은 유튜브와 인스타그램 등 다양한 플랫폼에서 인플루언서 마케팅을 진행하지만, 현재까지의 업무 방식은 플랫폼별 데이터 분석, 광고 성과 관리, 광고주 커뮤니케이션, 계약 문서 처리 등을 각각 개별적으로 운영하는 데에 많은 한계가 존재합니다. 유튜브와 인스타그램의 콘텐츠 데이터를 통합적으로 조회하거나, 광고한 영상의 성과를 체계적으로 분석할 수 있는 시스템이 부재하고, 기획부터 계약까지 이어지는 전체 캠페인 파이프라인을 통합 관리하기 어려워 담당자들의 수작업 부담과 정보 누락 가능성이 높습니다. 또한 반복적으로 작성되는 제안서나 계약서 같은 문서는 템플릿화가 되어 있지 않아 효율적인 업무 처리가 어렵습니다. 이러한 비효율성과 단절된 업무 흐름을 해결하기 위해, KATCHUP은 광고주 중심의 업무 프로세스를 하나의 CRM 시스템으로 통합하고, 인플루언서 콘텐츠 성과 분석, 캠페인 파이프라인 단계별 관리, 계약서 템플릿 기반 자동화 등을 통해 MCN 기업의 운영 효율성과 데이터 기반 의사결정을 지원하고자 합니다.

---

### 🗓️ 프로젝트 기간
- **시작일**: 2025년 5월 28일
- **종료일**: 2025년 7월 21일
- **총 기간**: 54일

---

### 🎯 프로젝트 목표
- 광고주–인플루언서 협업 과정 시스템화 및 자동화
- 캠페인 성과 통합 분석 및 시각화
- 문서 기반 업무 전자화 및 이력 관리
- 고객 커뮤니케이션 이력 기록 및 만족도 측정

---

### 👥 타겟 사용자
- MCN 기업의 마케팅 실무자 및 캠페인 담당자
- 콘텐츠 기획/제안 담당자
- 광고주 관리 책임자
- 인플루언서 운영 관리자

---

### 📈 시장 배경
- 인플루언서 마케팅 시장은 **2025년 약 28조 원 규모**로 성장 전망
- MCN 기업 내 업무는 여전히 **엑셀/수기 중심**
- 캠페인 성과 수집, 계약서 처리 등에서 **효율성 저하**  
  → KATCHUP은 이러한 한계를 해결하는 전용 CRM 시스템입니다.

---

### 💡 기대 효과
- ✅ 반복 업무 자동화 (제안/계약/견적/매출 등)
- ✅ 커뮤니케이션 체계화
- ✅ 콘텐츠 및 수익 통계 기반 분석
- ✅ 문서 전자화
- ✅ 고객 만족도 향상 (설문 기반 피드백)

---

### 🚀 추가 개발 제안
- AI 기반 콘텐츠 추천 기능
- Google Calendar API 연동
- TikTok, X(Twitter) 연동 확장
- 광고주 전용 외부 포털 뷰 제공

---

## <p id="1-2">1-2. ⚙️ 주요 기능</p>
### 🔎 1. 인플루언서 계정 및 성과 분석
- YouTube, Instagram 연동
- 평균 조회수, 구독자 변화율, 시청자 분석
- 인기 콘텐츠 자동 분류 및 캠페인별 성과 분석

### 📊 2. 캠페인 대시보드
- 콘텐츠 유입 경로, 검색 비율 분석
- 성과 리포트 생성 및 자동 전송

### 💼 3. 파이프라인 및 영업 기회 관리
- 제안서, 견적서, 계약서 등록 및 의견 관리
- 전자계약서 PDF 업로드/전송

### 💰 4. 매출 및 문서 관리
- 계약 기반 매출 등록 및 문서 이력 자동화

### 👤 5. 광고주 및 고객사 관리
- 광고 업체 및 담당자 관리, 메일 전송
- 커뮤니케이션 이력 통합 관리

### 💬 6. 채팅 및 커뮤니케이션
- 실시간 채팅, 파일 첨부, 읽음 여부 확인

### ⏰ 7. 알림 및 일정 기능
- 계약 상태 및 이벤트 알림
- 팀/개인 일정 관리

---

## <p id="1-3">1-3. 🛠️ 기술 스택</p>
<div align="center">

<h3>Backend & DB</h3>
<div dir="auto">
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"/>
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"/>
<img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white"/>
<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
<img src="https://img.shields.io/badge/mariaDB-003545?style=for-the-badge&logo=mariaDB&logoColor=white">
<img src="https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white">
<img src="https://img.shields.io/badge/Redis-FF4438?style=for-the-badge&logo=redis&logoColor=white">
<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
</div>

<h3>Frontend</h3>
<div dir="auto">
<img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white"> 
<img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white"> 
<img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"> 
<img src="https://img.shields.io/badge/vue.js-4FC08D?style=for-the-badge&logo=vue.js&logoColor=white"> 
<img src="https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white">
<img src="https://img.shields.io/badge/tailwindcss-%2338B2AC.svg?style=for-the-badge&logo=tailwind-css&logoColor=white"/>
<img src="https://img.shields.io/badge/vite-%23646CFF.svg?style=for-the-badge&logo=vite&logoColor=white"/>
</div>

<h3>Devops</h3>
<div dir="auto">
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white"/>
<img src="https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=jenkins&logoColor=white">
<img src="https://img.shields.io/badge/kubernetes-%23326ce5.svg?style=for-the-badge&logo=kubernetes&logoColor=white"/>
<img src="https://img.shields.io/badge/Argo-EF7B4D?style=for-the-badge&logo=argo&logoColor=white"/>
</div>

<h3>Tools & Communication</h3>
<div dir="auto">
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
<img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white"/>
<img src="https://img.shields.io/badge/ERDCLOUD-339AF0?style=for-the-badge&logoColor=white">
<img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white">
<img src="https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white"/>
<img src="https://img.shields.io/badge/jira-%230A0FFF.svg?style=for-the-badge&logo=jira&logoColor=white"/>
<img src="https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white"/>
<img src="https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white"/>
</div>
</div>

---
## <p id="2"> 2. 📁 설계 문서</p>
## <p id="2-1">2-1. 요구사항 명세서</p>
[![요구사항명세서](https://img.shields.io/badge/요구사항명세서-바로가기-yellow?style=for-the-badge)](https://docs.google.com/spreadsheets/d/1q12NH0h5hOcvCFU6CqA1my5h5tuPUZwJ_3G9Ezj80LA/edit?gid=759019660#gid=759019660)

---
## <p id="2-2">2-2. WBS</p>
[![WBS](https://img.shields.io/badge/WBS-바로가기-pink?style=for-the-badge)](https://docs.google.com/spreadsheets/d/1q12NH0h5hOcvCFU6CqA1my5h5tuPUZwJ_3G9Ezj80LA/edit?gid=764235756#gid=764235756)

---
## <p id="2-3">2-3. ERDCloud</p>
[![화면설계서](https://img.shields.io/badge/ERDCLoud-바로가기-purple?style=for-the-badge)](https://www.erdcloud.com/d/oHcHYPjm2Z4JXpG8f)
![image](https://github.com/user-attachments/assets/add751a8-5c6c-4e84-8c81-1c0aa7f10b80)

---
## <p id="2-4">2-4. FIGMA 화면 설계서</p>
[![화면설계서](https://img.shields.io/badge/Figma-바로가기-blue?style=for-the-badge)](https://www.figma.com/design/qBcsAhrNiPfBEJJqcAIGZk/%F0%9F%8D%85?node-id=0-1&t=q3nKm5IJwOSsfwtd-1)

---
## <p id="2-5">2-5. 아키텍처 구조도</p>
[![화면설계서](https://img.shields.io/badge/Architecture-바로가기-red?style=for-the-badge)](https://file.notion.so/f/f/d5de5c40-db63-8184-b2a5-00036fc324c3/feb97e5b-0d4b-4936-8108-4e3847f189db/%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98_%EA%B5%AC%EC%A1%B0%EB%8F%84.png?table=block&id=217e5c40-db63-8066-9741-fa2604c03940&spaceId=d5de5c40-db63-8184-b2a5-00036fc324c3&expirationTimestamp=1750377600000&signature=ZcBROztnF72hEGIFp6Z2Z47BoWMNGVdKxQm2zEEpT18&downloadName=%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98+%EA%B5%AC%EC%A1%B0%EB%8F%84.png)
![Image](https://github.com/user-attachments/assets/7b189246-8c8d-4816-b684-baf17627a9f3)

---
## <p id="2-6">2-6. 프로그램 사양서 (REST API 명세서)</p>

---
## <p id="3"> 3. 🌸 Back & Front 테스트 결과</p>
## <p id="3-1">3-1. 백엔드 단위 테스트 결과서</p>

---
## <p id="3-2">3-2. 프론트엔드 UI/UX 단위 테스트 결과서</p>

---
## <p id="3-3">3-3. git 형상관리</p>

---
## <p id="4"> 4. 시스템 통합</p>
## <p id="4-1">4-1. CI/CD 계획서</p>

---
## <p id="4-2">4-2. 통합 테스트 결과서</p>

---
## <p id="5"> 5. 트러블 슈팅</p>

---
## <p id="6"> 6. 🤝 팀원 회고</p>

| 이름  | 회고 |
|:---:|-|
| 박장우 | |
| 박준서 | |
| 오유경 | |
| 윤채영 | |
| 이승재 | |
| 조현승 | |

---
<div align="center">
  <b>Made with 💖 by Team TOMATO</b><br/>
  <i>“Catch up with your campaign. Catch up with KATCHUP.”</i>
</div>