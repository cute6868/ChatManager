*The Chinese version of the instructions is presented here.*

*For the [English version](#English), please scroll down to the bottom of the page.*

------

<br>

# 项目介绍

**项目名称**：ChatManager

**功能简述**：这是一个 AI 聊天网站项目，它能够让您和多个您喜欢的 AI 同时进行聊天，提高对话的效率。

<br><br>

# 视频演示

### 桌面端 Web

<video src="https://github.com/user-attachments/assets/d9dcdd88-2af0-4b35-8e6f-9c2940cd825e" controls width="100%" height="100%"></video>

<br>

### 移动端 Web


<video src="https://github.com/user-attachments/assets/f9bb605f-6096-4a7e-b913-7486291f1b01" controls width="375" height="667"></video>

<br><br>

# 项目技术栈

### 前端

- 使用语言：`HTML、CSS、TypeScript`
- 构建工具：`Vite`
- 前端框架：`Vue3`
- UI 框架：`ElementPlus`
- 路由工具：`vue-router`
- 状态管理：`pinia`
- HTTP 工具：`axios`
- CSS 工具：`Sass`
- 代码规范工具：`Eslint、Prettier`
- 是否响应式：是（已适配移动端）

**配置文件 `.prettierrc.json` ，编码规范：**

```json
{
  "$schema": "https://json.schemastore.org/prettierrc",
  "useTabs": false,
  "tabWidth": 2,
  "singleQuote": true,
  "semi": true,
  "arrowParens": "always",
  "trailingComma": "none",
  "bracketSpacing": true,
  "printWidth": 100,
  "endOfLine": "lf"
}
```

<br>

### 后端

- 编程语言：`Java`
- 构建工具：`Maven`
- 后端框架：`Spring boot`
- 数据库：`MySQL、Redis`
- 连接池：`Druid`
- 持久层框架：`Mybatis`
- 身份验证：`jsonwebtoken`
- HTTP 工具：`okHttp3`
- Json 工具：`gson`
- 日志框架：`Logback`
- 测试框架：`JUnit 5`

**后端架构：**![Backend Architecture](https://github.com/user-attachments/assets/a76cb66d-43e5-4f2a-8a4f-6403068bdb96)

<br><br>

# 项目配置

### 前端配置

1. 打开 `frontend 目录` =>  `src 目录` => `service 目录` => `config 目录` => `index.ts 文件` 
2. 修改文件配置，根据您的前端服务器修改 `URL`

<br>

### 后端配置

1. 打开 `backend 目录` => `src 目录` => `main 目录` => `java 目录` => `resources 目录` => `application.yml 文件` 和  `logback.xml`

2. 修改以下文件：

   - `application.yml`

     - MySQL 数据库配置

     - Redis 数据库配置

     - 邮箱配置（推荐企业邮箱）

   - `logback.xml`
     - 日志文件的输出路径

<br>

### 接口配置

**项目支持的模型**

目前项目支持的模型如下所示（模型均来自 https://www.volcengine.com/product/doubao/）

- deepseek-r1-250120

- doubao-1-5-thinking-pro-250415

如需调用其他模型的 `API` 服务，根据 ChatManager 后端统一模型响应接口完成新增模型的开发即可，无须改动前端部分。

<br>

**如何完成模型配置**

下面将为您演示如何完成模型的配置：

1. 获取自己需要的模型的密钥（这里以模型 `deepseek-r1-250120` 为例）
   - 打开网站：[https://console.volcengine.com/ark/region:ark+cn-beijing/model](https://console.volcengine.com/ark/region:ark+cn-beijing/model)
   - 在模型广场里面，找到 `Deepseek-r1` ，点击 `查看详情`
   - 向下滚动 `Deepseek-r1` 详情页，直到找到 `deepseek-r1-250120` 这个版本
   - 在这个版本的右边，有一个 `体验` 的小字，点击它
   - 在 `Deepseek-R1 | 250120` 右边，有一个 `API 接入` 的小字，点击它
   - 在 `获取API KEY` 中，创建您的 `API Key` 密钥，然后将会生成一段字符，这就是您操作这个 `deepseek-r1-250120` 模型的密钥，请保管好。
   - 如果您是开发人员，可以在 `快速接入测试` 中，根据 `Rest API` 的信息，修改 ChatManager 后端模型服务。

2. 在您编译、部署并运行 ChatManager 系统后（包括前端服务器、后端服务器、以及数据库）

3. 打开 ChatManager 网站，登录进去后，点击头像，然后点击设置

4. 进入设置页面后，将您自己的类似下面这种 `JSON` 格式的数据，覆盖到模型配置输入框里

```json
{
    "DeepSeek": {
        "apiKey": "这里要输入您上面创建的API KEY密钥",
        "model": "deepseek-r1-250120"
    },
    "DouBao": {
        "apiKey": "同理，按步骤创建密钥，并复制到这里",
        "model": "doubao-1-5-thinking-pro-250415"
    }
}
```

<br><br><a id="English"> </a>

# Introduction

**Project Name**：ChatManager

**Description**：This is an AI chat website project. It enables you to chat simultaneously with multiple AI you like, enhancing the efficiency of the conversation.

<br><br>

# Video


### Desktop Web

<video src="https://github.com/user-attachments/assets/d9dcdd88-2af0-4b35-8e6f-9c2940cd825e" controls width="100%" height="100%"></video>

<br>

### Mobile Web

<video src="https://github.com/user-attachments/assets/f9bb605f-6096-4a7e-b913-7486291f1b01" controls width="375" height="667"></video>

<br><br>

# Technology Stack

### Frontend

- Programming Languages: `HTML, CSS, TypeScript`
- Build Tool: `Vite`
- Frontend Framework: `Vue3`
- UI Framework: `ElementPlus`
- Routing Tool: `vue-router`
- State Management: `pinia`
- HTTP Client: `axios`
- CSS Preprocessor: `Sass`
- Code Formatting: `Eslint, Prettier`
- Responsive Design: Yes (Mobile-optimized)

**`.prettierrc.json` Configuration:**

```json
{
  "$schema": "https://json.schemastore.org/prettierrc",
  "useTabs": false,
  "tabWidth": 2,
  "singleQuote": true,
  "semi": true,
  "arrowParens": "always",
  "trailingComma": "none",
  "bracketSpacing": true,
  "printWidth": 100,
  "endOfLine": "lf"
}
```

<br>

### Backend

- Programming Language: `Java`
- Build Tool: `Maven`
- Backend Framework: `Spring Boot`
- Databases: `MySQL, Redis`
- Connection Pool: `Druid`
- Persistence Framework: `Mybatis`
- Authentication: `JSON Web Token (JWT)`
- HTTP Client: `okHttp3`
- JSON Processing: `Gson`
- Logging Framework: `Logback`
- Testing Framework: `JUnit 5`

**Backend Architecture:**![Backend Architecture](https://github.com/user-attachments/assets/a76cb66d-43e5-4f2a-8a4f-6403068bdb96)

<br><br>

# Configuration

### Frontend Configuration

1. Navigate to: `frontend directory` → `src directory` → `service directory` → `config directory` → `index.ts`
2. Modify the configuration file: Update the `URL` based on your frontend server settings.

<br>

### Backend Configuration

1. Navigate to: `backend directory` → `src directory` → `main directory` → `java directory` → `resources directory`
2. Edit the following files:
   - `application.yml`:
     - MySQL database configuration
     - Redis database configuration
     - Email service configuration (Enterprise email recommended)
   - `logback.xml`:
     - Configure the output path for log files.

<br>

### API Configuration

**Supported Models**

The project currently supports the following models (all sourced from https://www.volcengine.com/product/doubao/):

- deepseek-r1-250120
- doubao-1-5-thinking-pro-250415

To integrate additional models, developers can leverage ChatManager's unified model response interface in the backend without modifying the frontend.

<br>

**Model Configuration Steps**

Follow these steps to configure a model (using `deepseek-r1-250120` as an example):

1. Obtain the API Key for your chosen model:
   - Visit: https://console.volcengine.com/ark/region:ark+cn-beijing/model
   - Locate `Deepseek-r1` in the model marketplace and click `View Details`
   - Scroll down to find version `deepseek-r1-250120`
   - Click `Experience` next to the version
   - Click `API Access` on the `Deepseek-R1 | 250120` page
   - Generate an `API Key` under `Get API KEY` and securely store the generated credentials
   - Developers can reference `Rest API` documentation under `Quick Access Test` for backend integration
2. Compile, deploy, and run the ChatManager system (including frontend, backend, and database)
3. Access the ChatManager web interface, log in, click your profile avatar, and select `Settings`
4. In the Settings page, replace the default JSON configuration with your model credentials:

```json
{
    "DeepSeek": {
        "apiKey": "Replace with your generated API Key",
        "model": "deepseek-r1-250120"
    },
    "DouBao": {
        "apiKey": "Generate and paste your DouBao API Key here",
        "model": "doubao-1-5-thinking-pro-250415"
    }
}
```

<br><br>

# 项目数据库 (Database)

### 配套数据表 `SQL` (**Accompanying Data Table SQL**)

```mysql
-- 1.创建核心信息表
CREATE TABLE users_core_info (
    uid BIGINT NOT NULL UNIQUE,
    account VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    cellphone VARCHAR(20) DEFAULT NULL UNIQUE ,
    status TINYINT DEFAULT 0,
    role TINYINT DEFAULT 0,
    last_login_at DATETIME NOT NULL,
    PRIMARY KEY (uid)
);

-- 2.创建基础信息表
CREATE TABLE users_basic_info (
    uid BIGINT NOT NULL,
    nickname VARCHAR(30) NOT NULL,
    avatar VARCHAR(255) DEFAULT NULL,
    interests JSON DEFAULT NULL,
    selected JSON DEFAULT NULL,
    create_at DATETIME NOT NULL,
    PRIMARY KEY (uid)
);

-- 3.创建模型配置表
CREATE TABLE users_models_config (
    uid BIGINT NOT NULL,
    models JSON DEFAULT NULL,
    PRIMARY KEY (uid)
);

-- 4.创建记录指针表
CREATE TABLE users_record_pointer (
    uid BIGINT PRIMARY KEY,
    newest INT NOT NULL,  -- 最新记录的序号
    oldest INT NOT NULL   -- 最旧记录的序号
);

-- 5.创建记录表分片
DELIMITER //

CREATE PROCEDURE CreateUserHistoryShardTables()
BEGIN
    DECLARE i INT DEFAULT 0;
    WHILE i < 10 DO
        SET @create_table_sql = CONCAT('
            CREATE TABLE IF NOT EXISTS users_record_shard_', i, ' (
                id BIGINT PRIMARY KEY,
                uid BIGINT NOT NULL,
                question TEXT NOT NULL,
                time DATETIME NOT NULL,
                sequence_num INT NOT NULL,
                INDEX idx_uid_sequence (uid, sequence_num)
            )
        ');
        PREPARE stmt FROM @create_table_sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
        SET i = i + 1;
    END WHILE;
END //

DELIMITER ;

-- 调用存储过程创建分片表
CALL CreateUserHistoryShardTables();

-- 删除存储过程，避免残留
DROP PROCEDURE IF EXISTS CreateUserHistoryShardTables;


-- 6.创建模型头像表
CREATE TABLE models_avatar (
    modelId TINYINT UNSIGNED NOT NULL UNIQUE,
    avatar VARCHAR(255) DEFAULT NULL
);
```

<br>

### 用户核心信息表（users_core_info）

| 字段名        | 数据类型     | 描述                                                         |
| ------------- | ------------ | ------------------------------------------------------------ |
| uid           | BIGINT       | 用户唯一标识符，不使用自增，非空且唯一                       |
| account       | VARCHAR(30)  | 用户账号，30 个字符以内，非空且唯一                          |
| password      | VARCHAR(255) | 存储高安全性加密后的用户密码，255 个字符以内，非空           |
| email         | VARCHAR(255) | 存储普通加密后用户邮箱，255 个字符以内，非空且唯一           |
| cellphone     | VARCHAR(20)  | 存储普通加密后用户手机号码，20 个字符以内，可空且唯一        |
| status        | TINYINT      | 用户状态，0 表示账号正常，1 表示账号被封禁，2表示账号已注销，3表示账号被封和已注销；默认 0 |
| role          | TINYINT      | 用户身份，0 表示普通用户，1 表示管理员，2 表示超级管理员，默认 0 |
| last_login_at | DATETIME     | 用户最后登录时间，非空，格式为 'YYYY-MM-DD HH:MM:SS'         |

<br>

### 用户基础信息表（users_basic_info）

| 字段名    | 数据类型     | 描述                                                         |
| --------- | ------------ | ------------------------------------------------------------ |
| uid       | BIGINT       | 用户唯一标识符，与核心信息表的 uid 一致                      |
| nickname  | VARCHAR(30)  | 用户昵称，30 个字符以内，非空，允许重复                      |
| avatar    | VARCHAR(255) | 用户头像链接，255 个字符以内，默认为空                       |
| interests | JSON         | 用户兴趣，以 Java 字符串数组形式存储，如 ["经济类", "科技类",...]，默认为空 |
| selected  | JSON         | 用户选择的模型，JSON类型的数组'[1, 5, 0, 2]'，默认为空       |
| create_at | DATETIME     | 用户注册时间，非空，格式为 'YYYY-MM-DD HH:MM:SS'             |

<br>

### 模型配置表（users_models_config）

| 字段名 | 数据类型 | 描述                                    |
| ------ | -------- | --------------------------------------- |
| uid    | BIGINT   | 用户唯一标识符，与核心信息表的 uid 一致 |
| models | JSON     | 用户配置的模型信息，以 JSON 格式存储    |

<br>

### 用户历史记录指针表（users_record_pointer）

| 字段名 | 数据类型 | 描述                                                         |
| ------ | -------- | ------------------------------------------------------------ |
| uid    | BIGIN    | 用户 ID，主键，与核心信息表的 `uid` 一致，唯一标识用户。     |
| newest | INT      | 最新记录的序号，表示用户当前最新的历史记录位置（范围 1-20）；  （非空） |
| oldest | INT      | 最旧记录的序号，表示用户当前最旧的历史记录位置（范围 1-20），用于循环覆盖时定位要删除的记录；（非空） |

<br>

### 用户历史记录表（users_record_shard_X）

| 字段名       | 数据类型 | 描述                                                         |
| ------------ | -------- | ------------------------------------------------------------ |
| id           | BIGINT   | 记录 ID，使用雪花算法生成，主键，确保全局唯一性。            |
| uid          | BIGINT   | 用户 ID，与核心信息表的 `uid` 一致，作为分表键（按 `uid % 10` 分片），非空。 |
| question     | TEXT     | 用户提问的问题内容，非空。                                   |
| time         | DATETIME | 用户提问的时间，记录历史记录的时间戳。                       |
| sequence_num | INT      | 记录序号，表示该记录是用户的第几条历史记录（范围 1-20，用于循环覆盖）。 |

<br>

### 模型头像表（models_avatar）

| 字段名  | 数据类型         | 描述                                        |
| ------- | ---------------- | ------------------------------------------- |
| modelId | TINYINT UNSIGNED | 模型id，0~255的范围，不使用自增，非空且唯一 |
| avatar  | VARCHAR(255)     | 用户头像链接，255 个字符以内，默认为空      |
