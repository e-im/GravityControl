# GravityControl
### ***Liberate your server from the anti-dupe bourgeoisie.***
#### Paper plugin to enable gravity/sand duplication

*This is my first plugin! Please tell me in issue if you notice any problem. I want to improve!*

# [Downloads](https://github.com/laymanuel/GravityControl/releases) | [Discord](https://discord.gg/TNvq9y7esy)

## **Features:**

* Enable duping for each different block
* Allow duping in some worlds but not others
* Enable sand duping on Paper servers
* 1.13-1.18+ servers. Complete cross-version compatible. No NMS or reflection!

## **Commands**

| Command | Description | Permission |
| --- | --- | --- |
| /gcr | Reload GravityControl Configuration | `gravitycontrol.reload` |

## **Configuration**

Default configuration below. Everything is explained with comments. If you have issues or need help, reply here.

```yaml
# Worlds where gravity dupes will be allowed. Default "*" means all worlds.
worlds:
  - "*"

# Blocks that you want to allow duping. Default all enabled.
enabled-blocks:
  sand: true
  red-sand: true
  anvil: true
  dragon-egg: true
  gravel: true
  concrete-powder: true
```

## Support / Discord / Contact
Join [Discord](https://discord.gg/TNvq9y7esy) for support/help or feature request. You may also make issue here on GitHub or reply on PaperMC Forums to my post.
