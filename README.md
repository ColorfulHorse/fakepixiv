![badge](https://img.shields.io/badge/power%20by-JetBrains%20Intellij%20IDEA-blue)   
**thanks for open source license by JetBrains**

#### kotlin + MVVM + DataBinding + 协程 练手项目，仿pixiv android客户端。 
#### 目前基本功能已经完成，包括插画、漫画、小说列表；详情、评论 、搜索、排行榜、收藏、用户等相关功能；另外用vert.x做了个小服务提供热更新和浏览记录（原版会员功能）。 项目代码约1w行。

### 相关博客 https://juejin.cn/post/6969186077663821855

### MVVM解决了什么问题
- 逻辑分离，ViewModel通过Model获取数据；View与ViewModel绑定，互相监听（ui/数据）变化更新自身；View层和Model层完全分离
- 逻辑复用的问题，通过把一个大的页面拆分为若干独立模块，更加方便自由拆装。  
比如在一个作品详情页，需要显示部分评论，点击更多需要跳转到评论列表页显示更多评论，我们应该怎么做？  
显然我们应该拆分出一个评论模块作为详情页的子模块，然后和评论列表页共用ViewModel，这样很轻松就做到了逻辑复用。  

### 有必要应用MVVM吗
  就我个人目前的感觉来看，熟悉android开发的人自然能借此写出更好的代码（不需要mvvm也行），但是如果团队水平参差不齐意义也不大。另外databinding不利于调试的缺点也非常明显，不用它也未尝不可，希望能有更好用的库吧。  
  如果打算尝试一下mvvm，并不建议上手二次封装的重型库，比如提供什么文件模板一键生成的那类，它们往往会让你陷入泥沼。本身并不是那么复杂的东西，了解多了水到渠成而已。


#### 2019-10-16
- 基本功能完成

#### 2019-10-24
- gradle脚本替换为kotlin，改变依赖管理方式

#### todo list（鸽了）
- 下阶段计划引入jetpack ViewModel LiveData Navigation重构
- 下下阶段计划添加引入Compose的分支版本
- 用户页关注人列表
- 加入屏蔽设置
- commonDecoration添加新item底部分割线未刷新
