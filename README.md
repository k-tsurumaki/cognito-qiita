# Cognito を用いた認証機能つきバックエンドAPI

このリポジトリでは、Amazon Cognito を活用して自作アプリのバックエンド API を保護するための認証機能を実装しています。

---

## 主な機能

### 1. 認証フロー
- 認証されていない状態でバックエンド API にリクエストを送信すると、Cognito のログイン画面にリダイレクトされます。
- ログイン画面で認証情報を入力すると、**ID トークン**と**ユーザー ID**がレスポンスボディとして返されます。
- 次回以降のリクエストでは、この**ID トークン**を `Authorization` ヘッダーに含め、**ユーザー ID**をパスパラメータやクエリパラメータに加えてリクエストすることで、個別のユーザーに応じたサービスを提供できます。

---

## ディレクトリ構成
![image](https://github.com/user-attachments/assets/7d115616-7631-49d5-9c4c-e39a66bc0140)



---

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

### 1. 依存関係のインストール
このプロジェクトは Spring Boot を使用しています。Maven または Gradle を利用して依存関係をインストールしてください。

---

### 2. プロパティの設定
`application.properties` または `application.yaml` に必要な Cognito 設定情報を入力してください。

---

### 3. データベースの準備
`schema.sql` と `data.sql` を用いてデータベーススキーマを作成してください。

---

### 4. アプリケーションの起動
以下のコマンドでアプリケーションを起動します。

```bash
./mvnw spring-boot:run
```


# 起動方法
![image](https://github.com/user-attachments/assets/4e6a8aa8-e18e-45b8-af5d-07058f7a30fe)
![image](https://github.com/user-attachments/assets/5874ab57-3f1b-40db-a3ae-353d8ded58f0)
![image](https://github.com/user-attachments/assets/219e9fc7-68cf-4a2c-94cc-72cbbc3e6ac3)
![image](https://github.com/user-attachments/assets/bd45f963-8cf5-4794-835a-2b962919f890)

# 使用方法
認証されていない状態で API エンドポイントにリクエストを送信すると、ログイン画面にリダイレクトされます。

ログイン後、取得したIDトークンを以下の形式でリクエストに含めて送信してください。
```
Authorization: Bearer <ID_TOKEN>
```
必要に応じて、ユーザーID をリクエストパラメータとして指定します。


