# 객체지향 생활체조 메모

아래 내용은 리팩토링/코드리뷰 시 참고하기 위한 메모입니다.

## 1. 한 메서드에 오직 한 단계의 들여쓰기만 한다

- 한 메서드에 들여쓰기가 여러 개 존재한다면, 해당 메서드는 여러 가지 일을 하고 있다고 봐도 무관하다.
- 메서드는 맡은 일이 적을수록(잘게 쪼갤수록), 재사용성이 높고 디버깅도 용이하다.
- 메서드명을 통해 직관적으로 분리하고 재사용 가능한 코드로 만든다.

```java
public class JamieObject {

    String JamieAndNewLine() {
        StringBuilder stringBuilder = new StringBuilder();
        int raw = 10;
        int repeat = 5;
        for (int i = 0; i < raw; i++) {
            for (int j = 0; j < repeat; j++) {
                stringBuilder.append("Jamie");
                stringBuilder.append(" ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
```

```java
public class JamieObject {

    String JamieAndNewLine() {
        StringBuilder stringBuilder = new StringBuilder();
        int raw = 10;
        int repeat = 5;
        RefeatJamieNewLine(stringBuilder, raw, repeat);
        return stringBuilder.toString();
    }

    private void RefeatJamieNewLine(StringBuilder stringBuilder, int raw, int repeat) {
        for (int i = 0; i < raw; i++) {
            RepeatJamie(stringBuilder, repeat);
            stringBuilder.append("\n");
        }
    }

    private void RepeatJamie(StringBuilder stringBuilder, int repeat) {
        for (int j = 0; j < repeat; j++) {
            stringBuilder.append("Jamie");
            stringBuilder.append(" ");
        }
    }
}
```

## 2. else 키워드를 쓰지 않는다

- 조건문은 복제의 원인이 되기도 하고, 가독성도 좋지 않을 수 있다.
- 상태 분기가 여러 곳에 중복될 때 Strategy 패턴(다형성)이 특히 유용하다.
- 간단한 경우에는 guard clause(보호 구문), early return을 사용한다.

```java
public class JamieObject {

    String JamieStatus(int hour, boolean isStudy) {
        String status = "";
        if (hour > 4 && hour <= 12) {
            status = "취침";
        } else {
            if (isStudy) {
                status = "공부";
            } else {
                status = "여가";
            }
        }
        return status;
    }
}
```

```java
public class JamieObject {

    String JamieStatus(int hour, boolean isStudy) {
        if (hour > 4 && hour <= 12) {
            return "취침";
        }
        return isStudy ? "공부" : "여가";
    }
}
```

## 3. 모든 원시값과 문자열을 포장(wrap)한다

- 원시형 변수는 의미를 잃기 쉽다.
- 포장 객체를 쓰면 값의 의도와 제약을 코드에 담을 수 있다.
- 시간/돈 같은 단위 값은 행위를 둘 자리도 생긴다.

```java
public class JamieMoney {

    private final int money;

    public JamieMoney(int money) {
        validMoney(money <= 0, "현금은 0원 이상이여야 합니다.");
        validMoney(money % 10 != 0, "현금은 10원 단위 이상만 허용 합니다.");
        this.money = money;
    }

    private void validMoney(boolean expression, String exceptionMessage) {
        if (expression) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    public int getMoney() {
        return money;
    }
}
```

## 4. 한 줄에 점을 하나만 찍는다

- (스트림 체이닝 등 일부 예외 제외)
- 한 줄에 점이 둘 이상이면 객체 내부를 너무 깊게 건드리고 있을 가능성이 있다.
- 디미터 법칙(친구하고만 대화하라): 자신의 객체/생성한 객체/파라미터 객체와만 메시지를 주고받는다.

```java
public class JamieObject {

    void getMoney() {
        jamieWallet.getTotalMoney().getMoney();
    }
}

class JamieWallet {
    private final JamieMoney totalMoney;

    JamieMoney getTotalMoney() {
        return totalMoney;
    }
}

class JamieMoney {

    private final int money;

    int getMoney() {
        return getMoney();
    }
}
```

```java
public class JamieObject {

    void getMoney() {
        jamieWallet.getTotalMoney();
    }
}

class JamieWallet {

    private final JamieMoney totalMoney;

    int getTotalMoney() {
        return totalMoney.getMoney();
    }
}

class JamieMoney {

    private final int money;

    int getMoney() {
        return getMoney();
    }
}
```

## 5. 줄여쓰지 않는다

- 과도한 축약은 가독성을 해친다.
- 이름이 길다면 책임 분리가 필요한 신호일 수 있다.
- 문맥상 중복되는 단어는 줄이고, 의미는 정확히 드러낸다.

```java
public class Jamie {

    void printJamieName() {
        String EName = "Jamie";
        String KName = "제이미";
    }
}
```

```java
public class Jamie {

    void printName() {
        String englishName = "Jamie";
        String koreanName = "제이미";
    }
}
```

## 6. 모든 entity를 작게 유지한다

- 50줄 이상 되는 클래스 또는 10개 파일 이상의 패키지는 지양한다.
- 클래스가 커지면 보통 한 가지 일만 하지 않는다.
- 패키지가 작을수록 목적과 정체성이 명확해진다.

## 7. 2개 이상의 인스턴스 변수를 가진 클래스를 쓰지 않는다

- 인스턴스 변수가 많아질수록 응집도가 떨어진다.
- 관련 인스턴스 변수는 wrapper/일급 컬렉션 등 협력 객체로 분해한다.

```java
public class Jamie {

    private final String name;
    private final String job;
    private final int age;

    public Jamie(String name, String job, int age) {
        this.name = name;
        this.job = job;
        this.age = age;
    }
}
```

```java
public class Jamie {

    private final Name name;
    private final Job job;
    private final Age age;

    public Jamie(Name name, Job job, Age age) {
        this.name = name;
        this.job = job;
        this.age = age;
    }
}
```

## 8. 일급 컬렉션을 쓴다

- 컬렉션을 포함한 클래스는 반드시 다른 멤버 변수가 없어야 한다.
- 참고: 일급 컬렉션 개념을 별도 학습한다.

## 9. getter/setter/property를 쓰지 않는다

- (메모) 도메인 객체 중심 원칙으로 이해한다.
- 객체끼리는 값을 꺼내 조작하기보다 메시지를 보내 동작하게 한다.
- 캡슐화 경계를 지키면 중복/오류를 줄이고 변경 지점을 지역화할 수 있다.

```java
public class Jamie {

    private final Name name;
    private final Money money;

    public Jamie(Name name, Money money) {
        this.name = name;
        this.money = money;
    }

    boolean canBuySomething(int somthing) {
        return somthing <= money.getMoney();
    }
}
```

```java
public class Jamie {

    private final Name name;
    private final Money money;

    public Jamie(Name name, Money money) {
        this.name = name;
        this.money = money;
    }

    boolean canBuySomething(int somthing) {
        return money.moreThanOrEqualsPrice(somthing);
    }
}
```

## 결론

- 9가지 규칙(축약 관련 제외)은 데이터 캡슐화를 가시화/실현하기 위한 방안이다.
- `else`를 쓰지 않는 규칙은 다형성의 적절한 사용을 유도한다.
- 명명 전략은 코드/아이디어의 중복을 줄이고 의도를 드러내는 방향으로 간다.

## 이 프로젝트 적용 원칙 (DTO 예외)

아래 원칙은 도메인 객체 기준으로 강하게 적용하고, 요청/응답 DTO에는 예외를 둔다.

- **강적용 대상**: `domain` 객체, 비즈니스 로직 클래스
- **완화 대상**: `data/dto/request`, `data/dto/response`

DTO 완화 기준:

- 3번(원시값/문자열 포장) 완화
- 7번(인스턴스 변수 2개 제한) 완화
- 8번(일급 컬렉션) 완화
- 9번(getter/setter/property 금지) 완화

이유:

- DTO는 네트워크 스키마를 정확히 표현하는 것이 1순위 목적이다.
- 단순 `data class` + `@SerialName` 중심이 유지보수/직렬화 안정성에 유리하다.


