Welcome to Auframe
===================


应对模块化开发的基础框架，网络层基于okhttp做的一个简单封装，项目分为三层：业务，基础组件，和主项目，业务依赖的library可以单独运行，打包需要更改业务项目为主项目的依赖项目，更改文件在**gradle.properties** **isBuildModule**字段

----------


Documents
-------------

关于模块化开发的具体的集成方式和项目的构建方式，请参考[本人博客](www.aufuture.cn)

> **Note:**

> - 网络基于RxJava 2.0的封装版本 OkGo https://github.com/jeasonlzy/okhttp-OkGo
> - fastjson 解析库  https://github.com/alibaba/fastjson
> - 数据库 https://github.com/greenrobot/greenDAO.


----------


Synchronization
-------------------

StackEdit can be combined with <i class="icon-provider-gdrive"></i> **Google Drive** and <i class="icon-provider-dropbox"></i> **Dropbox** to have your documents saved in the *Cloud*. The synchronization mechanism takes care of uploading your modifications or downloading the latest version of your documents.

