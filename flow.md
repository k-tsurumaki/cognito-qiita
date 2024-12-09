```mermaid
sequenceDiagram
    participant User as ユーザー
    participant Browser as ブラウザ
    participant Frontend as フロントエンド
    participant Backend as バックエンド
    participant Cognito as Cognito

    User->>Browser: ログイン要求
    Browser->>Frontend: ログイン要求
    Frontend->>Cognito: 認可リクエスト
    Cognito-->>Browser: ユーザ認証画面
    User->>Cognito: 認証情報を入力
    Cognito-->>Browser: 認証成功後、認可コードをリダイレクトURIに付与
    Browser->>Frontend: 認可コードをフロントエンドに送信
    Frontend->>Backend: 認可コードをバックエンドに送信
    Backend->>Cognito: 認可コードを使用してアクセストークンとIDトークンを要求
    Cognito-->>Backend: IDトークンを返却
    Backend->>Cognito: IDトークン認証用の公開鍵要求
    Cognito-->>Backend: 公開鍵返却
    Backend->>Backend: IDトークンを検証し、ペイロード部からユーザ情報（name, email）を取得
    Backend->>Backend: ユーザ情報をDBに登録し、ユーザIDを取得
    Backend-->>Frontend: IDトークンとユーザIDをJSONレスポンスで返却
    Frontend-->>Browser: IDトークンをブラウザに保存
    Browser->>Backend: APIリクエスト（IDトークンをAuthorizationヘッダーに含める）
    Backend->>Cognito: IDトークン認証用の公開鍵要求
    Cognito-->>Backend: 公開鍵返却
    Backend->>Backend: IDトークン認証
    Backend-->>Browser: APIレスポンス


