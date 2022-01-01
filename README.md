# PanelCraft

This is a useful web server on your minecraft server

You can manage your minecraft server by web api and use webhook

## Feature

1. Don't invade
   - This plugin all feature is didn't block main process, your game can run smoothly
2. Light, very light
   - This plugin has a lot of feature, we don't rebuild any code, we use dynamic depend, if you add corresponding plugin then the feature will enable
3. All free and opensource
   - Although I did a lot of work on it, but I want to promote open source development, and I don't like a plugin need paid, and I didn't know does it useful, but I need pay for it! So This plugin all are free and open source!
4. Security
   - We use one server, one root key, current we only have single user, and database didn't record user action. But more user and action record is on my way!
5. Easy to use
6. Cross version
    - Support `1.8.x` - `1.18.x`

## Getting Start

1. Download plugin at [GitHub](https://github.com/LittleSheep2010/PanelCraft-Core/releases)
2. Download dependency for PanelCraft(optional)
   - [EssentialX](https://essentialsx.net/downloads.html)
   - [Vault](https://www.spigotmc.org/resources/vault.34315/)
   - [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
   - ~~[ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/)~~
3. Put all downloaded jar file in your plugin folder
4. Launch server
5. **Change the config file root password part**
6. Reload/Restart server
7. All are set!

## Use it

**You can download [PanelCraft-CLI](https://github.com/LittleSheep2010/PanelCraft-CLI) easy to use PanelCraft**

*We are makeing the official GUI manage tool*

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