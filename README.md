# GravityControl

### ***Liberate your server from the anti-dupe bourgeoisie.***

#### Paper plugin to enable gravity/sand duplication

# [Downloads](https://github.com/sulu5890/GravityControl/releases) | [Discord](https://discord.gg/TNvq9y7esy)

## **Features:**

* Enable duping for each different block
* Allow duping in some worlds but not others
* WorldGuard integration
* Enable sand duping on Paper servers
* 1.16-1.18+ servers. Complete cross-version compatible. No NMS or reflection!

## **Commands**

| Command | Description                         | Permission              |
|---------|-------------------------------------|-------------------------|
| /gcr    | Reload GravityControl Configuration | `gravitycontrol.reload` |

## **Configuration**

Default configuration below. Everything is explained with comments. If you have issues or need help, reply here.

<details>
  <summary>Default Configuration</summary>

  ```yaml
  # Worlds where gravity duplication is enabled.
  # `*` means all worlds.
  # WARNING: World key is used, NOT world name.
  # For example: `minecraft:overworld` rather than `world`.
  worlds:
    - "*"

  # Blocks for which duping is allowed.
  blocks:
    - "sand"
    - "red_sand"
    - "gravel"
    - "anvil"
    - "dragon_egg"
    - "white_concrete_powder"
    - "orange_concrete_powder"
    - "magenta_concrete_powder"
    - "light_blue_concrete_powder"
    - "yellow_concrete_powder"
    - "lime_concrete_powder"
    - "pink_concrete_powder"
    - "gray_concrete_powder"
    - "light_gray_concrete_powder"
    - "cyan_concrete_powder"
    - "purple_concrete_powder"
    - "blue_concrete_powder"
    - "brown_concrete_powder"
    - "green_concrete_powder"
    - "red_concrete_powder"
    - "black_concrete_powder"

  # List of WorldGuard regions where duping is allowed
  # `*` means all regions. Checked based on end portal coordinates.
  regions:
    - "*"

  # If GravityControl should check for updates on startup
  update-checker: true
  ```

</details>

## Support / Discord / Contact

Join [Discord](https://discord.gg/TNvq9y7esy) for support/help or feature request. You may also make issue here on
GitHub or reply on PaperMC Forums to my post.
