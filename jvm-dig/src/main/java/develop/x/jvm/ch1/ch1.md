# 1장 자바 기술 시스템 소개
## 1.1 들어가며
- 자바는 프로그래밍 언어뿐 아니라 여러 가지 소프트웨어와 명세로 구성된 기술 시스템을 통칭한다. 
- 자바 기술 시스템은 크로스 플랫폼 소프트웨어를 개발하고 배포하는 데 필요한 모든 것을 제공한다. 
- 자바 대표적인 특징 
  - 하드웨어 플랫폼이라는 족쇄를 제거하여, `한 번 작성하면 어디서든 실행된다'라는 이상을 실현한다. 
  - 상당한 안전한 메모리 시스템을 갖춘 덕에 메모리 누수 문제와 메모리를 가리키는 문제 대부분을 피할 수 있다. 
  - 런타임에 핫 코드(빈번하게 실행되어 성능에 영향을 주는) 감지 컴파일하고 최적화하여 자바 애플리케이션이 최상의 성능을 낼 수 있게 한다. 
  - 표준 API가 풍부할 뿐 아니라, 수많은 기업과 오픈 소스 커뮤니티에서 제공하는 다양한 기능의 서드 파티 라이브러리를 활용할 수 있다. 

## 1.2 자바 기술 시스템 
- 일반적으로 자바 가상 머신 위에서 동작하는 코틀린, 클로저, jRuby, 그루비 등의 프래그래밍 언어와 관련 기술도 자바 기술 시스템에 속하는 것으로 본다. 
- 전통적으로 자바 기술 시스템은 다음 요소들을 포괄한다. 
  - 자바 프로그래밍 언어(코틀린, 클로저, jRuby, 그루비)
  - (다양한 하드웨어 플랫폼용) 자바 가상 머신 구현
  - 클래스 파일 포맷
  - 자바 클래스 라이브러리 API(표준 API)
  - 오픈 소스, 서드 파티 클래스 라이브러리 
- 이 중 특히 자바 프래그래밍 언어, 자바 가상 머신, 자바 클래스 라이브러리를 묶어 JDK 라고 한다. 
- JDK 는 자바 프로그램 개발에 필요한 최소한의 환경이다. 
- 자바 SE API 와 자바 가상 머신 그리고 배포 기술까지를 묶어 JRE 라고 한다. 

## 1.3 자바의 과거와 현재 
### 1.3.1 자바의 탄생 
- 1991년 4월: 제임스 고슬링 박사는 그린 프로젝트(셋톱박스, 냉장고, 라디오 같은 가전제품에서 동작하는 프로그램)
  이 프로젝트는 자바 언어의 시초가 된 오크를 낳았다. 
- 1995년 5월: 오크 언어는 이름을 자바로 변경하고, 자바 1.0이 정식 데뷔한다. 
- 1996년 1월: JDK 1.0이 출시되면서 자바 언어는 정식 런타임 환경을 갖추게 된다.

### 1.3.2 유년기 
- 1997년 2월: JDK 1.1 이 출시된다. JDBC 등 자바의 가장 기본이 되는 기술도 상당수가 등작했다. 
  - JDK 1.1 의 대쵸 기술은 JAR 파일 포맷, JDBC, 자바빈스, RMI 등이다. 
  - 자바 언어 문법도 개선되면서 내부 클래스와 리플렉션이 등작했다. 
- 1998년 12월: JDK 1.2(Playground) 를 발표했다. 
  - 자바 기술 시스템을 3개로 나눴는데 J2SE, J2EE, J2ME
  - EJB, 자바 플러그인 바바 IDL, 스윙, 컬렉션 API
- 1999년 4월: 핫스팟 가상 머신의 출시일이다. JDK 1.3 부터 기본 자바 가상 머신으로 승격되었다.
- 2000년 5월: JDK 1.3이 출시된다. 
  - 수학 연산과 새로운 타이버 API 등 자바 클래스 라이브러리가 주로 개선
  - JNDI(Java Naming and Directory Interface)는 디렉터리 서비스에서 제공하는 데이터 및 객체를 발견(discover)하고 참고(lookup) 하기 위한 자바 API다.
- 2002년 2월: JDK 1.3 출시
  - 정규 표현식, 에외 연쇄, NIO, 로그 클래스, XML 파서, XSLT 변환 기능 
- 2004년 9월: JDK 5가 출시
  - 오토박싱, 제네릭스, 동적 애너테이션, 열거형, 가변길이 매개변수, foreach 순환ㅁ문등 
  - 자바 메모리 모델 java.util.concurrent 패키지 도입 

### 1.3.3 오픈소스 세계로
- 2006년 12월: JDK 6이 출시된다. 
  - 스크립트 언어 지원(모질라 자바스크립트 라이노 엔진 내장), 컴파일타임 애너테이선 처리기, 마이크로 Http 서버 API 제공등
  - 자바가상 머신도 락과 동기화 구현, 가비지 컬렉션, 클래스 로딩 등의 많은 면에서 개선됨
  - 자바를 오픈 소스로 전환 

### 1.3.4 오라클의 품으로 
- 2009년 2월 JDK 7 버전이 완성된다. 
  - 람다식과 함수형 프로그래밍 지원
  - 가상 머신 수준에서의 모듈화 지원 
  - 동적언어 지원, G1 컬렉터, 코인 프로젝트 
- 2009년 4월: 오라클은 74억 달러에 썬을 인수함 

### 1.3.5 모던 자바의 시작
- 2014년 3월 JDK 8이 마침내 출시함 
  - JEP 126: 람다식 지원 (자바 언어로 함수식을 표현)
  - JEP 104: 나스혼 자바스크립트 엔진 내장
  - JEP 150: 새로운 시간 및 날짜 API 
  - JEP 122: 핫스팟에서 영구 세대 완전 제거 
- 2017년 9월: JDK 9가 출시된다. 
  - 직소를 표준으로 하는 표준 모듈 시스템 
  - JShell, HLink, JHSDB 등의 도구를 개선하고 핫스팟을 구성하는 모듈들의 로깅 시스템을 수정
  - HTTP 2 단일 TCP 연결 API 등 

### 1.3.6 기민하게
- 2018년 3월: JDK 10 출시
  - 소스 저장소 통합, GC 인터페이스 통합, JIT 인터페이스 통합
  - 지역 변수 타입 추론
- 2018년 3월: 안드로이드 자바 사용 손해 배상 (88억 달라)
- 2018년 3월: 자바 EE 를 이클립스 재단에 넘겨버렸다 (자카르타 EE)

- 2018년 9월: JDK 11 출시 (LTS 버전)
  - 혁신적인 가비지 컬렉터인 ZGC 의 실험버전 추가 
  - 타입 추론의 람다 구문 지원등 

- 2019년 13월: JDK 12 출시
  - 새넌도어 가비지 컬렉터 추가 (레드헷)