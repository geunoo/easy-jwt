# easy-jwt
[![](https://jitpack.io/v/geunoo/easy-jwt.svg)](https://jitpack.io/#geunoo/easy-jwt)
>매번 jwt 코드 짜기 귀찮아서 만드는 jwt 세팅해주는 라이브러리

이 라이브러리를 사용하면 spring boot에서 jwt를 이용한 인증/인가를 할 수 있습니다.   
제가 평소 사용하는 jwt코드를 이용하기 때문에 문제가 되는 부분이 있을 수 있습니다.

### **`문제나 버그 발견 또는 건의사항은 이슈로 만들어주시면 감사하겠습니다.`**

# How To Use

### repositories
```gradle
// build.gradle
maven { url 'https://jitpack.io' }

// build.gradle.kts
maven { url = uri("https://jitpack.io") }
```

### dependencies
두 dependency를 모두 추가해야합니다.

**easy-jwt**
```gradle
// build.gradle
implementation 'com.github.geunoo:easy-jwt:0.1.1'

// build.gradle.kts
implementation("com.github.geunoo:easy-jwt:0.1.1")
```
**jjwt**
```gradle
// build.gradle
implementation 'io.jsonwebtoken:jjwt-api:0.12.5'
runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.5'
runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.5'

// build.gradle.kts
implementation("io.jsonwebtoken:jjwt-api:0.12.5")
runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")
```

# Setting

### application.yml/application.properties
```yaml
# application.yml
easy-jwt:
  access-exp: # access token 만료시간
  refresh-exp: # refresh token 만료시간
  secret: # token secret
```
```properties
# application.properties
easy-jwt.access-exp= # access token 만료시간
easy-jwt.refresh-exp= # refresh token 만료시간
easy-jwt.secret= # token secret
```
### User
인증에 사용할 유저 객체를 JwtUser에 상속시켜야합니다.
그리고 아래와 같이 getter를 구현해야합니다.
아래는 JPA를 이용했을때의 예제입니다.
```java
@Getter
@NoArgsConstructor
@Entity
public class User extends JwtUser { 
    private String accountId;
    private Authority authority;

    //...// 
    
    @Override
    public String getUsername() {
        return getAccountId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(getAuthority().name()));
    }
}
```
### SecurityConfig
JwtFilter를 적용해야합니다.
```java
//...//
.addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
```
만약 현재 토큰의 유저정보를 불러오고싶을때 빈등록하여 사용합니다.    
제네릭 타입에 JwtUser를 상속한 User를 적으면 됩니다.
```java
@Bean
public CurrentUserService<User> currentUserService() {     
    return new CurrentUserService<>();
}
```
### QueryJwtUserService
QueryJwtUserService를 구현해야합니다.   
아래 예제는 Spring Data JPA를 이용해 구현한 예제입니다.
```java
@RequiredArgsConstructor
@Component
public class QueryJwtUserService implements com.gil.easyjwt.user.QueryJwtUserService {

    private final UserRepository userRepository;

    @Override
    public Optional<JwtUser> execute(String username) {
        return userRepository.findByAccountId(username)
                .map(JwtUser.class::cast);
    }
}
```
# Features
기능들을 소개합니다.    
>*은  필수 매개변수입니다.
## JwtUser
인증에 사용할 User객체가 상속받아야하는 클래스입니다.    

- *username(``String``) : JWT에서 subject로 이용할 값이 들어갑니다.
- authorities(``Collection<? extends GrantedAuthority>``) : 권한에 대한 값이 들어갑니다.
### constructor
```java
public JwtUser(String username)
```
일반적으로 사용하는 생성자입니다.

```java
public JwtUser(String username, Collection<? extends GrantedAuthority> authorities)
```
권한이 필요할때 사용하는 생성자입니다.       
User의 생성자를 만들어줄때 다음과 같이 사용될 수 있습니다.
```java
public User(String accountId, String password, Authority authority) {
    super(
            accountId,
            Collections.singletonList(new SimpleGrantedAuthority(authority.name()))
    );
    this.accountId = accountId;
    this.password = password;
    this.authority = authority;
}
```
## JwtTokenProvider

### getExpiredAt() : `LocalDateTime`
만료시간을 가져올때 사용됩니다.

- *type(TokenType) : JWT 토큰의 타입입니다. (ACCESS, REFRESH)

### generateAccessToken() : `String`
access token을 생성하는 메서드입니다.
- *subject(`String`) : JWT의 subject값이 들어갑니다.
- claims(`Map<String, String>`) : claims을 추가하고싶다면 사용합니다.

### generateRefreshToken() : `String`
refresh token을 생성하는 메서드입니다.
- *subject(`String`) : JWT의 subject값이 들어갑니다.
- claims(`Map<String, String>`) : claims을 추가하고싶다면 사용합니다.
