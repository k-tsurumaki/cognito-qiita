# Cognito を用いた認証機能つきバックエンドAPI

このリポジトリでは、Amazon Cognito を活用して自作アプリのバックエンド API を保護するための認証機能を実装しています。

## 主な機能

### 1. 認証フロー
- 認証されていない状態でバックエンド API にリクエストを送信すると、Cognito のログイン画面にリダイレクトされます。
- ログイン画面で認証情報を入力すると、**ID トークン**と**ユーザー ID**がレスポンスボディとして返されます。
- 次回以降のリクエストでは、この**ID トークン**を `Authorization` ヘッダーに含め、**ユーザー ID**をパスパラメータやクエリパラメータに加えてリクエストすることで、個別のユーザーに応じたサービスを提供できます。


## 環境
- Windows11
- IntelliJ IDEA Community Edition: 2024-3
- Java SDK: Amazon Corret 21.0.5
- SpringBoot: 3.4.0
- Maven: 3.9.9
- MySQL: 8.0.40
- Amazon Cognito

## 依存パッケージ
- Spring Web
- Spring Data JPA
- MySQL Driver
- Spring Boot Dev Tools
- Validation
- OAuth2 Resource Server
- OAuth2 Client
- Spring Security

## ディレクトリ構成
![image](https://github.com/user-attachments/assets/7d115616-7631-49d5-9c4c-e39a66bc0140)



## 主要な認証実装

認証の中核となるクラスは `SecurityConfig.java` です。このクラスでは、Cognito を利用した OAuth2 認証フローや、ログイン後の処理を実現しています。

### OAuth2 ログイン成功時の処理

- `handleOAuth2LoginSuccess` メソッドで、認証成功後に ID トークンとユーザー情報を取得します。
- ユーザー情報（名前、メールアドレスなど）を使用して、アプリケーション内でユーザーを登録または更新します。
- ID トークンとユーザー ID を JSON レスポンスとして返します。

### CORS 設定

- クライアントからのリクエストが適切に処理されるよう、CORS 設定を構成しています。

### セッション管理

- `SessionCreationPolicy.STATELESS` を使用し、ステートレスな認証フローを実現しています。

### コード例

以下は `SecurityConfig` クラスの抜粋です。

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(Customizer.withDefaults())
        .cors(c -> c.configurationSource(corsConfigurationSource()))
        .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests((authorize) -> authorize
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .anyRequest().authenticated()
        )
        .oauth2Login(oauth2 -> oauth2.successHandler(this::handleOAuth2LoginSuccess))
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

    return http.build();
}
```

## セットアップ手順

### 1. DBプロパティの設定
`resource`配下の`appllication-develop.properties`に各DBプロパティを設定してください。（動かすだけであれば直打ちでも問題ありません。実際に公開する場合は環境変数に設定することをおすすめします。）
```java
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
```

### 2. Cognitoプロパティの設定
`resource`配下に`auth-config-develop.properties`を作成し、各Cognitoプロパティを設定してください。（動かすだけであれば直打ちでも問題ありません。実際に公開する場合は環境変数に設定することをおすすめします。）
```java
auth.clientId=${CLIENT_ID} // クライアントID
auth.clientSecret=${CRIENT_SERCRET} // クライアントシークレット
auth.jwk-set-uri=${JWK_SET_URI} // トークン署名キーURL
auth.issuer-uri=${ISSURE_URI} // トークン署名キーURLの/.well-known/jwk.jsonを削除したもの
auth.redirect-uri=${REDIRECT_URI} // 許可されているコールバックURL（Cognitoで自分が設定したもの）
```
### 3.メインクラスの設定
Run> Debug Configurations> Add new ConfigurationからApplicationを選択し、`Name`に任意の名前、`Main Class`に`CognitoQiitaApplication`を入力してください。

![image](https://github.com/user-attachments/assets/5874ab57-3f1b-40db-a3ae-353d8ded58f0)

.envに環境変数を記載している場合は`Environment Variables`に.envへのパスを設定してください。
![image](https://github.com/user-attachments/assets/219e9fc7-68cf-4a2c-94cc-72cbbc3e6ac3)

### 4. 実行
内容を適用して、画面右上の▷を押して実行してください。
![image](https://github.com/user-attachments/assets/bd45f963-8cf5-4794-835a-2b962919f890)

# 使用方法
認証されていない状態で API エンドポイントにリクエストを送信すると、ログイン画面にリダイレクトされます。

ログイン後、リクエストボディから取得したIDトークンを以下の形式でリクエストに含めて送信してください。
```
Authorization: Bearer <ID_TOKEN>
```
必要に応じて、ユーザーID をリクエストパラメータとして指定してください。


