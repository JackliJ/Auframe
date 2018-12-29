Android模块化开发基础框架
===================


应对模块化开发的基础框架，网络层基于okhttp做的一个简单封装，项目分为三层：业务，基础组件，和主项目，业务依赖的library可以单独运行，打包需要更改业务项目为主项目的依赖项目，更改文件在**gradle.properties** **isBuildModule**字段

----------


Documents
-------------

关于模块化开发的具体的集成方式和项目的构建方式，请参考 https://www.aufuture.cn

> **Note:**

> - 网络基于RxJava 2.0的封装版本 OkGo https://github.com/jeasonlzy/okhttp-OkGo
> - fastjson 解析库  https://github.com/alibaba/fastjson
> - 数据库 https://github.com/greenrobot/greenDAO.


----------
Introduction
-------------
项目中模块化开发的跳转方式使用的是**阿里云路由**  https://github.com/alibaba/ARouter

请保证主项目中的
```java
minSdkVersion 16
targetSdkVersion 25
```
**与library中一致   并且上面的包也要在其中引用 不然注解无法生效**

然后我们在library项目中需要跳转的Activity上添加注解
```java
@Route(path = "/small/model/ui/testActivity")
public class TestSmallUiActivity extends Activity {
```
项目中的跳转示例
```java
List<TestSmallBean> list = new ArrayList<>();
TestSmallBean bean = null;
for (int i = 0; i < 3; i++) {
    bean = new TestSmallBean();
    bean.setID(i);
    bean.setName("name is " + i);
}

ARouter.getInstance().build("/small/model/ui/testActivity")
             .withString("testString", "我是一个字符串")
             .withBoolean("testBoolean", true)
             .withSerializable("testBean", (Serializable) list)
             .navigation();
```



然后就是取值   取值有两种  一种是通过getIntent() 正常取值  一种是通过注解的方式

```java
@Autowired(name = "testString")
public String mName;
@Autowired(name = "testBoolean")
public boolean mboolean;
@Autowired(name = "testBean")
public List<TestSmallBean> mData;
```

在onCreate中
```java
ARouter.getInstance().inject(this);
```

另外一种方式为：
```java
mData = (List<TestSmallBean>) getIntent().getSerializableExtra("testBean");
```

TestSmallBean写在library中  方便主项目调用


我们通过gradle.properties中的 **isBuildModel** 来切换   当我们在开发过程中的时候  将其设置为true 
  
在运行工程中选择为model   那么model就可以作为独立的项目进行运行   设置为false 则项目model作为library应用于主项目

**需要注意的是**

 1. 我们应该尽量的规避主项目中使用到的jar资源文件出现在library中.
 2. 在library中，请尽量避免使用switch case 去判断资源文件
 3. 在声明Route Path的时候，以 **"/"** 开头，并保证第一个斜杠后的library名字与其他的不同

