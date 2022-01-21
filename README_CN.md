# PanelCraft 面板工艺

这是一个实用的 Minecraft Bukkit/Spigot/PaperSpigot 服务器 Web 面板

他可以通过 WebHook 给您的服务使用，也可以让您在远程不启动 Minecraft 也不打开服务器控制台执行命令，制裁作弊玩家。

## 特色

1. 不侵入
    - 您可以使用任何您喜欢的服务器核心，只需支持 Spigot & Bukkit API 即可
    - 同时无需安装任何其他服务，只需安装插件，在您的设备上安装一个客户端即可同时管理多个服务器
2. 十分轻量化
    - 这个插件是模块化设计，你可以自己添加自己想要的功能
    - 这个插件的主体功能也是按需激活，这需要看您安装了什么插件
3. 开放且免费
    - 我在这上面花了很多时间来构建它，但他依然是免费且开源的，请勿相信任何人在任何平台售卖此插件。也请您不要做那个倒卖的人！
4. 安全
    - 动态 ROOT 密码(请在日志查看)
    - 行为记录
    - 安全分级
5. 简单使用
6. 跨版本
    - 主体支持 `1.8.x` - `1.18.x`

## Getting Start

1. 在此下载插件 [GitHub](https://github.com/LittleSheep2010/PanelCraft-Core/releases)
2. 下载 PanelCraft 支持的依赖(选做)
    - [Vault](https://www.spigotmc.org/resources/vault.34315/)
    - [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
    - ~~[ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/)~~
3. 安装所下载的插件
4. 启动服务器
5. 获取生成的 ROOT 密码
6. 创建一个管理员账户
7. 全部完成啦～享受插件和游戏吧～

## Use it

**你可以下载 [PanelCraft-CLI](https://github.com/LittleSheep2010/PanelCraft-CLI) 来简单使用 PanelCraft**

*我们正在制作更简单易用的工具 ^o^*

### APIs

*Italics are optional*
**Bold are important**

1. `Controller` Class
    - `/console/reload`
        - Method: `POST`
        - Body: `password`
        - Response: None
    - `/console/poweroff`
        - Method: `POST`
        - Body: `password`
        - Response: None
    - `/console/vault/economy`
        - Method: `POST`
        - Body: `password`, *`operation`*, *`amount`*, **`player`**
        - Response: Player balance, Your action
    - `/console/placeholder/process`
        - Method: `POST`
        - Body: `password`, **`message`**, **`player`**
        - Response: Processed message
2. `Status` Class
    - `/status/database`
        - Method: `GET`
        - Body: None
        - Response: Current database status
    - `/console/permission`
        - Method: `POST`
        - Body: `password`
        - Response: You provide password is correct or not correct
3. Other Class
    - `/`
        - Method: `GET`
        - Body: None
        - Response: Server overview

### Status Code

1. `200` Success
    - When your requets is processed and completed
2. `400` Request Problem *Client problem*
    - When client send requets missing some parameter or some parameter is wrong
3. `500` Request Problem *Server problem*
    - When the module isn't activate