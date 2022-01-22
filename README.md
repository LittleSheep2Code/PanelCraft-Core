# PanelCraft

This is a useful web server on your minecraft server

You can manage your minecraft server by web api and use webhook

## Feature & Roadmap

1. Non-invasive
   - [x] PanelCraft Web Server are fully asynchronous. It means didn't block main process(server game process), let your game experience better and better
   - [x] PanelCraft didn't change any server setup step, just use your like command to startup the server!
   - [x] PanelCraft also didn't need you install php or other web environment, it all integration in the one plugin file!
2. Light, very light
   - [x] PanelCraft has a lot of feature, we don't rebuild any code, we use dynamic depend, if you add corresponding plugin then the feature will enable
   - [x] PanelCraft use module design, you can according to your needs to install the other parts!
3. Core free, and pay for you love
   - **PanelCraft-Core** are opensource with **GPL v3** license, please use our program **strictly** in accordance with the license!
   - In the feature, other module may need you pay. But **PanelCraft-Core** is always opensource and free.
   - If you didn't want pay or you have not any money to pay. You can view the wiki at GitHub and make the other module youself!
   - We accept your donates. Now we only support the Chinse [Afdian](https://www.afdian.net/@LittleSheep) platform.
4. Security
   - [x] You can set permission to per user, and easy to setup this part!
   - [ ] Every user do every things can be record down
   - [ ] LuckPerms like permission management
5. Easy to use
6. Cross version
    - [x] Support `1.8.x` - `1.18.x`

## Getting Start

1. Download latest version plugin at [GitHub](https://github.com/LittleSheep2010/PanelCraft-Core/releases)
2. Download dependency for PanelCraft(optional)
   - [EssentialX](https://essentialsx.net/downloads.html)
   - [Vault](https://www.spigotmc.org/resources/vault.34315/)
   - [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
   - ~~[ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/)~~
3. Put all downloaded jar file in your plugin folder
4. Launch server
4. Open the server fire wall TCP port `25564`
5. Create a admin user and set it permission as `{'*': true}`
5. Init datbase with new create admin user
7. All are set!

**Alert: Although ADMIN > PLAYER > ANYONE, but ROOT permission didn't has more access than ADMIN**

## Use it

**You can download [PanelCraft-CLI](https://github.com/LittleSheep2010/PanelCraft-CLI) easy to use PanelCraft (Outdated)**

*We are makeing the official GUI manage tool*

### APIs

| Name             | URL        | Method | Description                                                  | Parameters  | Permission or Role | Category | Module |
| ---------------- | ---------- | -------- | ------------------------------------------------------------ | ----------- | ------------------ | -------- | -------- |
| Status Getter    | `/`        | `GET` | The root of PanelCraft, when method not allowed, prove you request are 404 | *None* | *Anyone* |Other|Core|
| Command Executor | `/console` | `PUT`                                | Execute command you want | **Command** | **Root** |Console|Core|
| Vault Economy Manager | `/console/vault/economy` | `PUT` | Modfiy or query player vault money. **When request with `operation`, you must request with `amount` too!** | **Player**, Operation, Amount | **Admin** |Console|**D-Vault**|
| Placeholder  Replacer | `/console/placeholder` | `PUT` | Process the PlaceholderAPI text with your server | **Player**, **Message** | **Admin** |Console|**D-PlaceholderAPI**|
| Server Reload | `/danger-zone/reload` | `PATCH` | Reload your server | *None* | **Admin** |Dangerous|Core|
| Server Shutdown | `/danger-zone/shutdown` | `PATCH` | Shutdown server | *None* | **Root** |Dangerous|Core|
| Initiation Database | `/danger-zone/initialize/database` | `PATCH` | Init PanelCraft's database struct | *None* | **Admin** |Dangerous|Core|
| Account Create | `/security/account-management/create` | `PUT` | Create user | **Username**, **Password**, Role, Permission, Mail, Description | **Root** |Security|Core|
| Account Login | `/security/authorization` | `PUT` | Login | **Username**, **Password** | *Anyone* |Security|Core|

### Status Code

1. `200` Success
   - When your requets is processed and completed
2. `400` Request Problem *Client problem*
   - When client send requets missing some parameter or some parameter is wrong
3. `500` Request Problem *Server problem*
   - When the module isn't activate