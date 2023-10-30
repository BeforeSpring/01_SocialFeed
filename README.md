![logo](https://static.wanted.co.kr/images/events/3178/58ac3248.jpg)

# 통합 소셜 피드 서비스

<br/>

## Table of Contents
- [소개](#소개)
- [기반 기술](#기반-기술)
- [API Reference](#api-reference)
- [프로젝트 진행 및 이슈 관리](#프로젝트-진행-및-이슈-관리)
- [구현과정(설계 및 의도)](#구현과정(설계-및-의도))
- [TIL 및 회고](#til-및-회고)
- [Authors](#authors)
- [References](#references)

<br/>


## 소개
여러 소셜 미디어 서비스에 올라온 게시물을 해시태그 기반으로 취합하여 통합 제공하는 서비스입니다.
<br/>


## 기반 기술
![Static Badge](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=appveyor?logo=null) ![Static Badge](https://img.shields.io/badge/SpringBoot-2.7.17-yellow?style=for-the-badge&logo=appveyor?logo=null) ![Static Badge](https://img.shields.io/badge/H2-embedded-blue?style=for-the-badge&logo=appveyor?logo=null) 
<br/>
- **언어**
  - Java 17
- **웹 프레임워크**
  - SpringBoot 2.7.17
- **DB 접근 기술**
  - Spring Data JPA
  - Spring JDBC *(Bulk Insert시 쿼리 성능을 위해 사용)*
  - QueryDSL
- **DBMS**
  - H2 *(SpringBoot embedded)*
  - *(JPA와 ANSI표준 SQL문 사용으로, 대부분의 RDBMS와 호환될 것으로 생각됨.)*

<br/>


## API Reference
작성예정
<br/>


## 프로젝트 진행 및 이슈 관리

[![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)](https://www.notion.so/Team-BeforSpring-888afbbaa2424750a7600475f4a2630f?pvs=4)
[![GithubProject](https://img.shields.io/badge/Github_Project-%23000000.svg?style=for-the-badge&logo=github&logoColor=white)](https://github.com/orgs/BeforeSpring/projects/3)
<br/>


## 구현과정(설계 및 의도) 
<details>
<summary>무한 스크롤을 고려한 유저 맞춤 컨텐츠 조회 - click</summary>

- **서비스 특성**
  - 소셜미디어의 특성상 게시물이 자주 업데이트됨.
  - 일반적으로 무한스크롤 방식의 UX를 채택할 것임.
  - 모든 SNS 게시물을 전부 크롤링하여 저장 것은 것은 사실상 불가능함.
    - 본 서비스에서 관리하는 해시태그가 포함된 게시물에 한해서 저장하게 됨.
    - 때문에, 본 서비스에 데이터가 들어오는 순서는 실제 게시물이 게시된 순서와 전혀 다를 수 있음.(본 서비스에서 관리하는 PK 생성 방식과 관련하여 고려할 필요가 있음.)
- **무한스크롤 구현**
  - **구현 방법 결정**(마지막 조회 결과 이후 시점부터 쿼리)
    - 단순 offset 사용은 적절하지 않다고 판단함.
      - 다음 페이지를 조회하기 전에 새로운 게시물이 올라온다면, 중복되는 게시물이 등장할 수 있음.
      - **UX 측면에서, 스크롤을 내릴 때, 중복된 게시물이 등장하는 것은 좋지 못함.**
    - 마지막 조회 결과를 바탕으로 그 이후의 결과를 조회할 필요가 있다고 결론을 냄.
  - **마지막 조회 결과를 DB에서 어떻게 특정할 것인가**
    - Incremental, 혹은 Time-Based로 생성된 PK를 이용하는 것이 일반적이나, 본 서비스에서는 부적절하다고 판단함.
      - 여러 SNS의 게시물 ID 타입과 관리 체계가 다름. 때문에 본 서비스에서는 대리키PK를 사용하는 것이 좋다고 판단하였음.
      - **SNS 게시물의 실제 게시 시점과, 본 서비스에서 DB에 저장한 시점은 다를 수 있음.**
    - 위 문제는 정규화를 포기하고, 해시태그마다 `Content`를 중복 저장하는 방식으로 해결할 수 있기는 하지만, 보다 온건한 방법을 채택함.
      - 문제의 심각성에 비해서 해결방법이 너무 급진적이라고 판단함.
      - 통계 관련 기능 구현시 애로사항이 있을 수도 있다고 판단함.
    - 마지막 조회된 게시물을 특정하기 위해서 게시물 생성시점인 `createdAt`을 활용하기로 결정함.
- **쿼리 성능 문제**
  - 문자열 매칭 성능 문제
    - `Content.hashtag`를 공백 구분 문자열로 저장함.
    - `like %tofind%`로 검사할 경우, 인덱스를 활용할 수 없기 때문에 성능에 큰 문제가 발생함.
    - 해결방법
      - `HashtagContent` 엔티티(테이블)를 만들어 최적화를 시도하였음.
        - `HashtagContent`
          - `Long id`(대리키, PK)
          - `String hashtag`
          - `Long contentId`(외래키)
  - Join 쿼리 성능 문제
    - 인덱스를 적절히 활용할 수 없는 문제
      - 앞서 언급한 시간 기반으로 이전 게시물을 특정하기로 결정한 이후, 인덱스를 적절히 활용할수 없다는 점을 인지하였음.
    - 해결방법
      - 정규화를 조금 포기하고, `HashtagContent`에 `createdAt` 컬럼을 추가함.
        - join시, 드라이빙 테이블의 Row 수를 최소화하기 위해서임. 
        - `createdAt`을 `HashtagContent`에 추가할 경우, 드라이빙 테이블에서 목록 컨텐츠 쿼리에 필요한 모든 조건을 완성할수 있음.
          - 쿼리 조건을 `HashtagContent`에 설정된 복합 인덱스 (`hashtag`, `createdAt`)를 통해 완전히 커버 가능함.
        - 드리븐 테이블의 값(이 경우 `Content.createdAt`)에 의존적이지 않기 때문에 쿼리 비용 예측이 쉬워짐. 
- **게시물 생성 시점의 세분성(granularity)과 관련된 문제**
  - 외부 SNS에서 게시물 생성 시점을 얼마나 상세하게 제공하느냐에 따라서 쿼리 방식에 잠재적인 문제 발생이 가능함.
    - DB에는 수~수십ms 단위로 세분화된 시간 정보를 저장 가능하지만, 외부 서비스가 시간을 얼마나 자세히 제공하는지 고려하여야함. 
    - 만약 API로 제공되는 게시물의 시간 단위가 1초, 1분과 같이 큰 단위로 제공된다면, 동일한 시간에 생성된 게시물이 여럿 존재할 수 있음.
      - **이 경우, `createdAt`을 기반으로 무한스크롤을 구현한 본 서비스에서, 동일한 결과만 지속적으로 쿼리되는 문제가 발생할 수 있음.**
  - 해결방법
    - 쿼리 파라미터에 `offset` 조건을 추가하고, 다음 쿼리에 `offset` 설정이 필요한 경우(마지막 조회된 게시물의 생성시점과, 그 직후의 생성시점이 같은 경우), 이를 판단하여 반환하도록 api 스펙을 작성하였음.
    - `createdAt` 파라미터도 여전히 사용하기 때문에, 새 게시물로 인해서 중복된 결과가 조회되는 상황을 방지할수 있을 것으로 생각됨.
  - 추가로 고려해야할 점
    - `offset`의 특성상, 엄청나게 인기있는 해시태그를 처리해야할 경우, 쿼리 성능을 개선해야할 필요가 있을 수도 있음. 
      - 쿼리 파라미터로 `contentId`까지 받고, 정렬 조건에 포함시킨 뒤, `contentId` 이후의 값을 불러오는 식으로 구현할수도 있을 것 같음.
      - 단, `contentId` 컬럼까지 인덱스에 포함될 경우, 인덱스의 크기가 커져서 전반적인 읽기 성능이 떨어지는 trade-off가 존재함. 
      - 추후 sns에서 제공하는 시간 응답 값이 어떤지 확인하고, 실제 서비스의 유즈케이스를 고려하여 다른 방안을 생각해볼것.

</details>

<details>
- 외부 소셜미디어 서비스의 OpenAPI를 호출하는 과정을 추상화하였습니다. 로그 메시지를 출력하는 구현체로 대체하였습니다.
- 구현
    - `ExternalApiHandler` 인터페이스
        - `ExternalApiHandler`를 통해 각 소셜 미디어 플랫폼의 API 호출을 추상화했습니다.
            - `InstagramApiHandler` , `FacebookpiHandler` , `TreadsApiHandler` , `TwitterApiHandler`등의 구현체가 존재합니다.
        - `getSourceType` 메서드는 각 핸들러가 어떤 소셜 미디어의 API를 다루는지 식별합니다.
        - `like`와 `share` 메서드는 게시물에 좋아요 또는 공유를 수행합니다.
    - `ExternalApiHandlerResolver`
        - 내부에 `ExternalApiHandler`의 구현체가 매핑된 `Map` 인스턴스를 가지고 있으며, `sourceType`에 따라 적절한 `ExternalApiHandler`를 반환합니다.
        - 생성자에서 `ExternalApiHandler`를 구현한 클래스들을 주입받아 `sourceType`을 기준으로 핸들러 클래스들을 매핑합니다.
</details>

<br/>


## TIL 및 회고

<br/>


## Authors

<br/>


## References



