-- テーブルが存在する場合は削除
DROP TABLE IF EXISTS tb_item;
DROP TABLE IF EXISTS tb_user;

CREATE TABLE tb_item (
    item_id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 主キー
    item_name VARCHAR(255) NOT NULL,          -- アイテム名
    item_category VARCHAR(255) NOT NULL,      -- アイテムカテゴリ
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- 作成日時
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 更新日時
    deleted_flg BOOLEAN DEFAULT FALSE NOT NULL -- 削除フラグ
);

CREATE TABLE tb_user (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY, -- ユーザーID
    username VARCHAR(255) NOT NULL,               -- ユーザー名
    email VARCHAR(255) NOT NULL UNIQUE,       -- メールアドレス (一意制約)
    role VARCHAR(255),                        -- ロール (任意)
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- 作成日時
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 更新日時
    deleted_flg BOOLEAN NOT NULL DEFAULT FALSE -- 削除フラグ
);
