# Git 学习


## 远程仓库操作

- 查看远程仓库地址

```
andy@andy:~/project/script-tool$ git remote -v
origin  https://github.com/wangyong-chengdu/script-tool.git (fetch)
origin  https://github.com/wangyong-chengdu/script-tool.git (push)
```

- 修改远程仓库地址

```
andy@andy:~/project/script-tool$ git remote set-url origin https://github.com/wangyong-chengdu/kit.git
andy@andy:~/project/script-tool$ git remote -v
origin  https://github.com/wangyong-chengdu/kit.git (fetch)
origin  https://github.com/wangyong-chengdu/kit.git (push)
```