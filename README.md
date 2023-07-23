![image.png](https://cdn.nlark.com/yuque/0/2023/png/2548312/1690078539162-4a2c1ab0-6ab8-4c04-b83b-b15517f0df8a.png#averageHue=%23040100&clientId=u8654682c-ec90-4&from=paste&id=u94bb9b06&originHeight=86&originWidth=340&originalType=binary&ratio=2&rotation=0&showTitle=false&size=14643&status=done&style=none&taskId=u395217fa-8bbb-4b5c-9a6d-cfce8f3e434&title=)
本文是《Maven实战》的读书笔记，实战代码仓库：[https://github.com/goSilver/mvn_in_action](https://github.com/goSilver/mvn_in_action)
## 第五章 坐标和依赖
### 5.1 坐标的定义
Maven定义了这样一组规则：**世界上任何一个构件都可以使用Maven坐标唯一标识，Maven坐标的元素包括groupId、artifactId、version、packaging、classifier。**只要我们提供正确的坐标元素，Maven就能找到对应的构件。
```xml
<!-- 项目坐标 -->
<!-- groupId定义了项目属于哪个组，往往是组织名或公司名-->
<groupId>org.chensh</groupId>
<!-- artifactId定义了当前Maven项目在组中唯一的ID -->
<artifactId>chapter_3</artifactId>
<!-- 版本号，分为快照版本号和稳定版本号 -->
<version>1.0-SNAPSHOT</version>
<!-- name元素声明一个更加友好的项目名称，非必填 -->
<name>hello-world</name>
```
groupId：定义当前Maven项目隶属的实际项目。
artifactId：该元素定义实际项目中的一个Maven项目（模块）
version：该元素定义Maven项目当前所处的版本
packaging：该元素定义Maven项目的打包方式
classifier：该元素用来帮助定义构建输出的一些附属构件
上述5个元素中，**groupId、artifactId、version是必须定义的，packaging是可选的（默认为jar），而classifier是不能直接定义的。**

### 5.2 依赖范围

1. **classpath**

首先需要知道，Maven在**编译项目主代码**的时候需要使用一套classpath。在上例中，编译项目主代码的时候需要用到spring-core，该文件以依赖的方式被引入到classpath中。其次，Maven在**编译和执行测试**的时候会使用另外一套classpath。上例中的JUnit就是一个很好的例子，该文件也以依赖的方式引入到测试使用的classpath中，不同的是这里的依赖范围是test。最后，**实际运行Maven项目**的时候，又会使用一套classpath，上例中的spring-core需要在该classpath中，而JUnit则不需要。

2. **依赖范围**

**依赖范围就是用来控制依赖与这三种classpath（编译classpath、测试classpath、运行classpath）的关系**，Maven有以下几种依赖范围：

   1. **compile：编译依赖范围**。如果没有指定，就会默认使用该依赖范围。使用此依赖范围的Maven依赖，对于编译、测试、运行三种classpath都有效。典型的例子是spring-core，在编译、测试和运行的时候都需要使用该依赖。
   2. **test：测试依赖范围**。使用此依赖范围的Maven依赖，只对于测试classpath有效，在编译主代码或者运行项目的使用时将无法使用此类依赖。典型的例子是JUnit，它只有在编译测试代码及运行测试的时候才需要。
   3. **provided：已提供依赖范围**。使用此依赖范围的Maven依赖，对于编译和测试class-path有效，但在运行时无效。典型的例子是servlet-api，编译和测试项目的时候需要该依赖，但在运行项目的时候，由于容器已经提供，就不需要Maven重复地引入一遍。
   4. **runtime：运行时依赖范围**。使用此依赖范围的Maven依赖，对于测试和运行class-path有效，但在编译主代码时无效。典型的例子是JDBC驱动实现，项目主代码的编译只需要JDK提供的JDBC接口，只有在执行测试或者运行项目的时候才需要实现上述接口的具体JDBC驱动。
   5. **import（Maven 2.0.9及以上）：导入依赖范围**。该依赖范围不会对三种classpath产生实际的影响，本书将在8.3.3节介绍Maven依赖和dependencyManagement的时候详细介绍此依赖范围。

![image.png](https://cdn.nlark.com/yuque/0/2023/png/2548312/1689471938182-f6ee42fb-c867-43fa-b259-ed883318fdaa.png#averageHue=%23eeeeed&clientId=u979c3bc5-5e4e-4&from=paste&height=262&id=ufb33cc94&originHeight=524&originWidth=1492&originalType=binary&ratio=1&rotation=0&showTitle=false&size=316157&status=done&style=none&taskId=ubd2a9c91-8574-4c7c-b291-d8a2e33e85d&title=&width=746)
### 5.3 传递性依赖

1. **定义**

有了传递性依赖机制，在使用Spring Framework的时候就不用去考虑它依赖了什么，也不用担心引入多余的依赖。**Maven会解析各个直接依赖的POM，将那些必要的间接依赖，以传递性依赖的形式引入到当前的项目中。**

2. **传递性依赖和依赖范围**

依赖范围不仅可以控制依赖与三种classpath的关系，还对传递性依赖产生影响。

假设A依赖于B,B依赖于C，我们说A对于B是第一直接依赖，B对于C是第二直接依赖，A对于C是传递性依赖。**第一直接依赖的范围和第二直接依赖的范围决定了传递性依赖的范围**，如表5-2所示，最左边一列表示第一直接依赖范围，最上面一行表示第二直接依赖范围，中间的交叉单元格则表示传递性依赖范围。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/2548312/1689472695677-71db6f0f-136f-46f1-b014-2ab58e308a60.png#averageHue=%23efede9&clientId=u979c3bc5-5e4e-4&from=paste&height=215&id=u58df876f&originHeight=430&originWidth=1520&originalType=binary&ratio=1&rotation=0&showTitle=false&size=299647&status=done&style=none&taskId=u6b46f39e-6002-4812-a194-52088655566&title=&width=760)
仔细观察一下表5-2，可以发现这样的规律：

- **当第二直接依赖的范围是compile的时候，传递性依赖的范围与第一直接依赖的范围一致；当第二直接依赖的范围是test的时候，依赖不会得以传递；**
- **当第二直接依赖的范围是provided的时候，只传递第一直接依赖范围也为provided的依赖，且传递性依赖的范围同样为provided；**
- **当第二直接依赖的范围是runtime的时候，传递性依赖的范围与第一直接依赖的范围一致，但compile例外，此时传递性依赖的范围为runtime。**
### 5.4 依赖调解
Maven引入的传递性依赖机制，一方面大大简化和方便了依赖声明，另一方面，大部分情况下我们只需要关心项目的直接依赖是什么，而不用考虑这些直接依赖会引入什么传递性依赖。但有时候，当传递性依赖造成问题的时候，我们就需要清楚地知道该传递性依赖是从哪条依赖路径引入的。

例如，项目A有这样的依赖关系：A-＞B-＞C-＞X（1.0）、A-＞D-＞X（2.0），X是A的传递性依赖，但是两条依赖路径上有两个版本的X，那么哪个X会被Maven解析使用呢？两个版本都被解析显然是不对的，因为那会造成依赖重复，因此必须选择一个。**Maven依赖调解（Dependency Mediation）的第一原则是：路径最近者优先。**该例中X（1.0）的路径长度为3，而X（2.0）的路径长度为2，因此X（2.0）会被解析使用。

依赖调解第一原则不能解决所有问题，比如这样的依赖关系：A-＞B-＞Y（1.0）、A-＞C-＞Y（2.0），Y（1.0）和Y（2.0）的依赖路径长度是一样的，都为2。那么到底谁会被解析使用呢？在Maven 2.0.8及之前的版本中，这是不确定的，但是从Maven 2.0.9开始，**为了尽可能避免构建的不确定性，Maven定义了依赖调解的第二原则：第一声明者优先。在依赖路径长度相等的前提下，在POM中依赖声明的顺序决定了谁会被解析使用，顺序最靠前的那个依赖优胜。**该例中，如果B的依赖声明在C之前，那么Y（1.0）就会被解析使用。
### 5.5 可选依赖

1. **为什么要使用可选依赖这一特性呢？**

可能项目B实现了两个特性，其中的特性一依赖于X，特性二依赖于Y，而且这两个特性是互斥的，用户不可能同时使用两个特性。比如B是一个持久层隔离工具包，它支持多种数据库，包括MySQL、PostgreSQL等，在构建这个工具包的时候，需要这两种数据库的驱动程序，但在使用这个工具包的时候，只会依赖一种数据库。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/2548312/1689473093061-3e13e325-3239-41f9-bfba-73cec3c5cd45.png#averageHue=%23fafafa&clientId=u979c3bc5-5e4e-4&from=paste&height=205&id=uea9e3aa9&originHeight=410&originWidth=1244&originalType=binary&ratio=1&rotation=0&showTitle=false&size=106835&status=done&style=none&taskId=u5e9800dc-73c0-4b67-9ccc-17aa3cbf576&title=&width=622)

2. **原则**

关于可选依赖需要说明的一点是，**在理想的情况下，是不应该使用可选依赖的**。前面我们可以看到，使用可选依赖的原因是某一个项目实现了多个特性，在面向对象设计中，有个**单一职责性原则**，意指一个类应该只有一项职责，而不是糅合太多的功能。
### 5.6 排除依赖
**传递性依赖会给项目隐式地引入很多依赖，这极大地简化了项目依赖的管理，但是有些时候这种特性也会带来问题。**例如，当前项目有一个第三方依赖，而这个第三方依赖由于某些原因依赖了另外一个类库的SNAPSHOT版本，那么这个SNAPSHOT就会成为当前项目的传递性依赖，而SNAPSHOT的不稳定性会直接影响到当前的项目。这时就需要排除掉该SNAPSHOT，并且在当前项目中声明该类库的某个正式发布的版本。

**代码中使用exclusions元素声明排除依赖，exclusions可以包含一个或者多个exclusion子元素，因此可以排除一个或者多个传递性依赖。需要注意的是，声明exclusion的时候只需要groupId和artifactId，而不需要version元素，这是因为只需要groupId和artifactId就能唯一定位依赖图中的某个依赖。**换句话说，Maven解析后的依赖中，不可能出现groupId和artifactId相同，但是version不同的两个依赖，这一点在5.6节中已做过解释。该例的依赖解析逻辑如图5-4所示。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/2548312/1689473574508-d2fa3e44-4752-4936-a969-8901b623317c.png#averageHue=%23f9f9f8&clientId=u979c3bc5-5e4e-4&from=paste&height=174&id=uabda08f6&originHeight=348&originWidth=1232&originalType=binary&ratio=1&rotation=0&showTitle=false&size=168281&status=done&style=none&taskId=ucb99ca71-a386-4a59-9d75-481e7a0063c&title=&width=616)
### 5.7 归类依赖
```xml
<properties>
    <springframework.version>2.5.6</springframework.version>
</properties>


<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-core</artifactId>
        <version>${springframework.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-beans</artifactId>
        <version>${springframework.version}</version>
    </dependency>
</dependencies>
```
这里简单用到了Maven属性（14.1节会详细介绍Maven属性），**首先使用properties元素定义Maven属性**，该例中定义了一个springframework.version子元素，其值为2.5.6。**有了这个属性定义之后，Maven运行的时候会将POM中的所有的${springframework.version}替换成实际值2.5.6**。也就是说，可以使用美元符号和大括弧环绕的方式引用Maven属性。然后，将所有Spring Framework依赖的版本值用这一属性引用表示。这和在Java中用常量PI替换3.14是同样的道理，不同的只是语法。
### 5.8 依赖优化
Maven会自动解析所有项目的直接依赖和传递性依赖，并且根据规则正确判断每个依赖的范围，对于一些依赖冲突，也能进行调节，以确保任何一个构件只有唯一的版本在依赖中存在。在这些工作之后，**最后得到的那些依赖被称为已解析依赖（Resolved Dependency）**。可以运行如下的命令查看当前项目的已解析依赖：
```shell
mvn dependency:list
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/2548312/1689476250789-28e87438-bf6d-4e97-be94-1560bae727d8.png#averageHue=%23323030&clientId=u979c3bc5-5e4e-4&from=paste&height=419&id=ufac4b9ea&originHeight=838&originWidth=1330&originalType=binary&ratio=1&rotation=0&showTitle=false&size=119823&status=done&style=none&taskId=ua420bbb7-c532-4f16-af1c-96a33d6bc8e&title=&width=665)

在此基础上，还能进一步了解已解析依赖的信息。将直接在当前项目POM声明的依赖定义为顶层依赖，而这些顶层依赖的依赖则定义为第二层依赖，以此类推，有第三、第四层依赖。**当这些依赖经Maven解析后，就会构成一个依赖树，通过这棵依赖树就能很清楚地看到某个依赖是通过哪条传递路径引入的。**可以运行如下命令查看当前项目的依赖树：
```shell
mvn dependency:tree
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/2548312/1689476355198-a3fafe08-aa07-4e50-8182-d341b342bca4.png#averageHue=%23323030&clientId=u979c3bc5-5e4e-4&from=paste&height=375&id=u965863da&originHeight=750&originWidth=1394&originalType=binary&ratio=1&rotation=0&showTitle=false&size=117357&status=done&style=none&taskId=u826162cb-aeb0-4ad6-9f4e-0577a883862&title=&width=697)

使用dependency:list和dependency:tree可以帮助我们详细了解项目中所有依赖的具体信息，在此基础上，还有dependency:analyze工具可以帮助分析当前项目的依赖。
```shell
mvn dependency:analyze
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/2548312/1689476566540-de59c156-319d-4223-bd2f-c10a202a7db6.png#averageHue=%23333130&clientId=u979c3bc5-5e4e-4&from=paste&height=400&id=u3670caf5&originHeight=800&originWidth=1468&originalType=binary&ratio=1&rotation=0&showTitle=false&size=153287&status=done&style=none&taskId=uaa98b842-3d73-4cac-b8e8-10202618282&title=&width=734)
该结果中重要的是两个部分。**首先是Used undeclared dependencies，意指项目中使用到的，但是没有显式声明的依赖，这里是spring-context。**这种依赖意味着潜在的风险，当前项目直接在使用它们，例如有很多相关的Java import声明，而这种依赖是通过直接依赖传递进来的，当升级直接依赖的时候，相关传递性依赖的版本也可能发生变化，这种变化不易察觉，但是有可能导致当前项目出错。例如由于接口的改变，当前项目中的相关代码无法编译。这种隐藏的、潜在的威胁一旦出现，就往往需要耗费大量的时间来查明真相。**因此，显式声明任何项目中直接用到的依赖。**

结果中还有一个重要的部分**是Unused declared dependencies，意指项目中未使用的，但显式声明的依赖，这里有spring-core和spring-beans。**需要注意的是，对于这样一类依赖，我们不应该简单地直接删除其声明，而是应该仔细分析。由于dependency:analyze只会分析编译主代码和测试代码需要用到的依赖，一些执行测试和运行时需要的依赖它就发现不了。很显然，该例中的spring-core和spring-beans是运行Spring Framework项目必要的类库，因此不应该删除依赖声明。当然，有时候确实能通过该信息找到一些没用的依赖，但一定要小心测试。
## 第六章 仓库
### 6.1 仓库的定义
在一台工作站上，可能会有几十个Maven项目，所有项目都使用maven-compiler-plugin，这些项目中的大部分都用到了log4j，有一小部分用到了Spring Framework，还有另外一小部分用到了Struts2。**在每个有需要的项目中都放置一份重复的log4j或者struts2显然不是最好的解决方案，这样做不仅造成了磁盘空间的浪费，而且也难于统一管理，文件的复制等操作也会降低构建的速度。**而实际情况是，在不使用Maven的那些项目中，我们往往就能发现命名为lib/的目录，各个项目lib/目录下的内容存在大量的重复。

**得益于坐标机制，任何Maven项目使用任何一个构件的方式都是完全相同的**。在此基础上，**Maven可以在某个位置统一存储所有Maven项目共享的构件，这个统一的位置就是仓库。**实际的Maven项目将不再各自存储其依赖文件，它们只需要声明这些依赖的坐标，在需要的时候（例如，编译项目的时候需要将依赖加入到classpath中），Maven会自动根据坐标找到仓库中的构件，并使用它们。

**为了实现重用，项目构建完毕后生成的构件也可以安装或者部署到仓库中，供其他项目使用。**
### 6.2 仓库的布局
**任何一个构件都有其唯一的坐标，根据这个坐标可以定义其在仓库中的唯一存储路径，这便是Maven的仓库布局方式。**

Maven仓库是基于简单文件系统存储的，我们也理解了其存储方式，因此，当遇到一些与仓库相关的问题时，可以很方便地查找相关文件，方便定位问题。例如，当Maven无法获得项目声明的依赖时，可以查看该依赖对应的文件在仓库中是否存在，如果不存在，查看是否有其他版本可用，等等。
### 6.3 仓库的分类
对于Maven来说，仓库只分为两类：**本地仓库和远程仓库**。**当Maven根据坐标寻找构件的时候，它首先会查看本地仓库，如果本地仓库存在此构件，则直接使用；如果本地仓库不存在此构件，或者需要查看是否有更新的构件版本，Maven就会去远程仓库查找，发现需要的构件之后，下载到本地仓库再使用。如果本地仓库和远程仓库都没有需要的构件，Maven就会报错。**

在这个最基本分类的基础上，还有必要介绍一些特殊的远程仓库。**中央仓库是Maven核心自带的远程仓库**，它包含了绝大部分开源的构件。在默认配置下，当本地仓库没有Maven需要的构件的时候，它就会尝试从中央仓库下载。

**私服是另一种特殊的远程仓库**，为了节省带宽和时间，应该在局域网内架设一个私有的仓库服务器，用其代理所有外部的远程仓库。内部的项目还能部署到私服上供其他项目使用。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/2548312/1689487919685-1622cc07-caf2-4f3e-8f6e-886f6cc73dd9.png#averageHue=%23fafafa&clientId=u979c3bc5-5e4e-4&from=paste&height=231&id=u286a3212&originHeight=462&originWidth=1158&originalType=binary&ratio=1&rotation=0&showTitle=false&size=82123&status=done&style=none&taskId=u6830e4f0-05fa-4baf-abdc-5c3f856d759&title=&width=579)

1. **本地仓库**

一般来说，在Maven项目目录下，没有诸如lib/这样用来存放依赖文件的目录。**当Maven在执行编译或测试时，如果需要使用依赖文件，它总是基于坐标使用本地仓库的依赖文件。**

一个构件只有在本地仓库中之后，才能由其他Maven项目使用，**那么构件如何进入到本地仓库中呢？最常见的是依赖Maven从远程仓库下载到本地仓库中。还有一种常见的情况是，将本地项目的构件安装到Maven仓库中。**

**Install插件的install目标将项目的构建输出文件安装到本地仓库。**

2. **远程仓库**

**安装好Maven后，如果不执行任何Maven命令，本地仓库目录是不存在的。当用户输入第一条Maven命令之后，Maven才会创建本地仓库，然后根据配置和需要，从远程仓库下载构件至本地仓库。**

3. **中央仓库**

**由于最原始的本地仓库是空的，Maven必须知道至少一个可用的远程仓库，才能在执行Maven命令的时候下载到需要的构件。中央仓库就是这样一个默认的远程仓库，Maven的安装文件自带了中央仓库的配置。**

中央仓库包含了这个世界上绝大多数流行的开源Java构件，以及源码、作者信息、SCM、信息、许可证信息等，每个月这里都会接受全世界Java程序员大概1亿次的访问，它对全世界Java开发者的贡献由此可见一斑。由于中央仓库包含了超过2000个开源项目的构件，因此，一般来说，一个简单Maven项目所需要的依赖构件都能从中央仓库下载到。这也解释了为什么Maven能做到“开箱即用”。

4. **私服**

**私服是一种特殊的远程仓库，它是架设在局域网内的仓库服务，私服代理广域网上的远程仓库，供局域网内的Maven用户使用。当Maven需要下载构件的时候，它从私服请求，如果私服上不存在该构件，则从外部的远程仓库下载，缓存在私服上之后，再为Maven的下载请求提供服务。此外，一些无法从外部仓库下载到的构件也能从本地上传到私服上供大家使用，**如图6-2所示。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/2548312/1689489022696-da113172-277a-416f-8eee-4ea38a8ab5ec.png#averageHue=%23f4f4f4&clientId=u979c3bc5-5e4e-4&from=paste&height=446&id=ua1a662d2&originHeight=892&originWidth=1282&originalType=binary&ratio=1&rotation=0&showTitle=false&size=261233&status=done&style=none&taskId=u4263fd09-dcdd-4177-9b15-b05907cda76&title=&width=641)
私服的好处：

- 节省自己的外网带宽。
- 加速Maven构建。
- 部署第三方构件。
- 提高稳定性，增强控制。
- 降低中央仓库的负荷。
### 6.5 快照版本
快照版本的出现是为了提高团队内部协作时的协作效率。

默认情况下，Maven每天检查一次更新（由仓库配置的updatePolicy控制，见第6.4节），用户也可以使用命令行-U参数强制让Maven检查更新，如**mvn clean install-U**。

当项目经过完善的测试后需要发布的时候，就应该将快照版本更改为发布版本。

**快照版本只应该在组织内部的项目或模块间依赖使用**，因为这时，组织对于这些快照版本的依赖具有完全的理解及控制权。**项目不应该依赖于任何组织外部的快照版本依赖，由于快照版本的不稳定性，这样的依赖会造成潜在的危险。**也就是说，即使项目构建今天是成功的，由于外部的快照版本依赖实际对应的构件随时可能变化，项目的构建就可能由于这些外部的不受控制的因素而失败。
## 第七章 生命周期
### 7.1 生命周期的定义
Maven的生命周期就是为了对所有的构建过程进行**抽象和统一**。Maven从大量项目和构建工具中学习和反思，然后总结了一套**高度完善的、易扩展**的生命周期。这个**生命周期包含了项目的清理、初始化、编译、测试、打包、集成测试、验证、部署和站点生成**等几乎所有构建步骤。

Maven的生命周期是抽象的，这意味着生命周期本身不做任何实际的工作，在Maven的设计中，**实际的任务（如编译源代码）都交由插件来完成。**每个构建步骤都可以绑定一个或者多个插件行为，而且Maven为大多数构建步骤编写并绑定了默认插件。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/2548312/1689494757270-3d989e4b-b78d-4071-8ea1-198d24f8b6e3.png#averageHue=%23efefee&clientId=u979c3bc5-5e4e-4&from=paste&height=210&id=ua4caf264&originHeight=420&originWidth=1238&originalType=binary&ratio=1&rotation=0&showTitle=false&size=288081&status=done&style=none&taskId=u4ce49e83-d6db-436a-93e0-76ac2c2faf2&title=&width=619)

Maven定义的生命周期和插件机制一方面保证了所有Maven项目有一致的构建标准，另一方面又通过默认插件简化和稳定了实际项目的构建。此外，该机制还提供了足够的扩展空间，用户可以通过配置现有插件或者自行编写插件来自定义构建行为。
### 7.2 生命周期详解
Maven拥有三套相互独立的生命周期，它们分别为**clean、default和site。**

- clean生命周期的目的是清理项目；
- default生命周期的目的是构建项目；
- site生命周期的目的是建立项目站点。
## 第八章 聚合与继承
### 8.2 聚合
当我们的项目下存在多个模块时，一个简单的需求就会自然而然地显现出来：我们会想要一次构建两个项目，而不是到两个模块的目录下分别执行mvn命令。**Maven聚合（或者称为多模块）这一特性就是为该需求服务的。**
```xml
    <groupId>com.mypaas.bigdata</groupId>
    <artifactId>bigdata-quality</artifactId>
    <packaging>pom</packaging>
    <version>1.0-SNAPSHOT</version>
    <modules>
        <module>quality-common</module>
        <module>quality-dao</module>
        <module>quality-stub</module>
        <module>quality-service</module>
        <module>quality-web</module>
    </modules>
```
**这里的第一个特殊的地方为packaging，其值为POM。**回顾account-email和account-persist，它们都没有声明packaging，即使用了默认值jar。对于聚合模块来说，其打包方式packaging的值必须为pom，否则就无法构建。

之后是本书之前都没提到过的**元素modules，这是实现聚合的最核心的配置。用户可以通过在一个打包方式为pom的Maven项目中声明任意数量的module元素来实现模块的聚合。**

**聚合模块仅仅是帮助聚合其他模块构建的工具，它本身并无实质的内容。**

**Maven会首先解析聚合模块的POM、分析要构建的模块、并计算出一个反应堆构建顺序（Reactor Build Order），然后根据这个顺序依次构建各个模块。反应堆是所有模块组成的一个构建结构。**
### 8.3 继承
面向对象设计中，程序员可以建立一种类的父子结构，然后在父类中声明一些字段和方法供子类继承，这样就可以做到“一处声明，多处使用”。类似地，我们需要创建POM的父子结构，然后在父POM中声明一些配置供子POM继承，以实现“**一处声明，多处使用**”的目的。

Maven提供的**dependencyManagement元素**既能让子模块继承到父模块的依赖配置，又能保证子模块依赖使用的灵活性。**在dependencyManagement元素下的依赖声明不会引入实际的依赖，**不过它能够约束dependencies下的依赖使用**。如果子模块不声明依赖的使用，即使该依赖已经在父POM的dependencyManagement中声明了，也不会产生任何实际的效果。**

**当项目中的多个模块有同样的插件配置时，应当将配置移到父POM的pluginManagement元素中。**
### 8.4 聚合与继承的关系
多模块Maven项目中的聚合与继承其实是两个概念，其目的完全是不同的。**前者主要是为了方便快速构建项目，后者主要是为了消除重复配置。**

**对于聚合模块来说，它知道有哪些被聚合的模块，但那些被聚合的模块不知道这个聚合模块的存在。对于继承关系的父POM来说，它不知道有哪些子模块继承于它，但那些子模块都必须知道自己的父POM是什么。如果非要说这两个特性的共同点，那么可以看到，聚合POM与继承关系中的父POM的packaging都必须是pom，同时，聚合模块与继承关系中的父模块除了POM之外都没有实际的内容。**
### 8.5 约定由于配置
### 8.6 反应堆
在一个多模块的Maven项目中，**反应堆（Reactor）是指所有模块组成的一个构建结构**。对于单模块的项目，反应堆就是该模块本身，但对于多模块项目来说，反应堆就包含了各模块之间继承与依赖的关系，从而能够自动计算出合理的模块构建顺序。

1. **反应堆的构建顺序**

**Maven按序读取POM，如果该POM没有依赖模块，那么就构建该模块，否则就先构建其依赖模块，如果该依赖还依赖于其他模块，则进一步先构建依赖的依赖。**

模块间的依赖关系会将反应堆构成一个有向非循环图（Directed Acyclic Graph,DAG），各个模块是该图的节点，依赖关系构成了有向边。这个图不允许出现循环，因此，当出现模块A依赖于B，而B又依赖于A的情况时，Maven就会报错。

2. **反应堆裁剪**

一般来说，用户会选择构建整个项目或者选择构建单个模块，但有些时候，用户会想要仅仅构建完整反应堆中的某些个模块。换句话说，用户需要实时地裁剪反应堆。

Maven提供很多的命令行选项支持裁剪反应堆，输入mvn-h可以看到这些选项：

- -am，also-make同时构建所列模块的依赖模块
- -amd，also-make-dependents同时构建依赖于所列模块的模块
- -pl，projects＜arg＞构建指定的模块，模块间用逗号分隔
- -rf，resume-from＜arg＞从指定的模块回复反应堆
## 第十章 使用Maven进行测试
### 10.1 maven-surefire-plugin简介
Maven本身并不是一个单元测试框架，Java世界中主流的单元测试框架为JUnit（[http://www.junit.org/](http://www.junit.org/)）和TestNG（[http://testng.org/](http://testng.org/)）。**Maven所做的只是在构建执行到特定生命周期阶段的时候，通过插件来执行JUnit或者TestNG的测试用例。这一插件就是maven-surefire-plugin，可以称之为测试运行器（Test Runner），它能很好地兼容JUnit 3、JUnit 4以及TestNG。**

在默认情况下，maven-surefire-plugin的test目标会自动执行测试源码路径（默认为src/test/java/）下所有符合一组命名模式的测试类。这组模式为：
**/Test*.java：任何子目录下所有命名以Test开头的Java类。
**/*Test.java：任何子目录下所有命名以Test结尾的Java类。
**/*TestCase.java：任何子目录下所有命名以TestCase结尾的Java类。
### 10.2 跳过测试
不管怎样，我们总会要求Maven跳过测试，这很简单，在命令行加入参数skipTests就可以了。例如：
```shell
$mvn package-DskipTests
```
当然，也可以在POM中配置maven-surefire-plugin插件来提供该属性，如代码清单10-12所示。但这是不推荐的做法，如果配置POM让项目长时间地跳过测试，则还要测试代码做什么呢？
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.0.0-M5</version>
    <configuration>
        <skipTests>true</skipTests>
    </configuration>
</plugin>
```
有时候用户不仅仅想跳过测试运行，还想临时性地跳过测试代码的编译，Maven也允许你这么做，但记住这是不推荐的：
```shell
$mvn package-Dmaven.test.skip=true
```

参数maven.test.skip同时控制了maven-compiler-plugin和maven-surefire-plugin两个插件的行为，测试代码编译跳过了，测试运行也跳过了。
### 10.3 动态指定要运行的测试用例
maven-surefire-plugin提供了一个test参数让Maven用户能够在命令行指定要运行的测试用例。

1. 指定单个要运行的测试类
```shell
$mvn test-Dtest=RandomGeneratorTest
```

2. 使用星号指定运行匹配的测试类名
```shell
$mvn test-Dtest=Random*Test
```

3. 使用逗号指定运行多个测试类
```shell
$mvn test-Dtest=RandomGeneratorTest,AccountCaptchaServiceTest
```

4. 逗号和星号组合使用
```shell
$mvn test-Dtest=Random*Test,AccountCaptchaServiceTest
```

使用test参数用户可以从命令行灵活地指定要运行的测试类。**可惜的是，maven-surefire-plugin并没有提供任何参数支持用户从命令行跳过指定的测试类，好在用户可以通过在POM中配置maven-surefire-plugin排除特定的测试类。**
### 10.4 包含与排除测试用例
```xml
  <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-surefire-plugin</artifactId>
      <version>3.0.0-M5</version>
      <configuration>
          <includes>
              <include>**/*Tests.java</include>
          </includes>
      </configuration>
  </plugin>
```
使用了**/*Tests.java来匹配所有以Tests结尾的Java类，两个星号**用来匹配任意路径，一个星号*匹配除路径风格符外的0个或者多个字符。

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.0.0-M5</version>
    <configuration>
        <excludes>
            <exclude>**/*UserTest.java</exclude>
        </excludes>
    </configuration>
</plugin>
```
以上配置排除了以UserTest结尾的测试类。
### 10.5 测试报告

1. **基本的测试报告**

默认情况下，maven-surefire-plugin会在项目的target/surefire-reports目录下生成两种格式的错误报告：

- 简单文本格式
- 与JUnit兼容的XML格式
2. **测试覆盖率报告**

测试覆盖率是衡量项目代码质量的一个重要的参考指标。Cobertura是一个优秀的开源测试覆盖率统计工具（详见[http://cobertura.sourceforge.net/](http://cobertura.sourceforge.net/)），Maven通过cobertura-maven-plugin与之集成，用户可以使用简单的命令为Maven项目生成测试覆盖率报告。
```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>cobertura-maven-plugin</artifactId>
    <version>2.7</version>
    <configuration>
        <formats>
            <format>html</format>
        </formats>
        <check/>
    </configuration>
</plugin>
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/2548312/1690012351864-712398de-e418-4ac6-9fe6-4c927f55345e.png#averageHue=%23fdfdfd&clientId=u60e40f1b-82da-4&from=paste&height=262&id=u03236aea&originHeight=524&originWidth=1918&originalType=binary&ratio=2&rotation=0&showTitle=false&size=66327&status=done&style=none&taskId=u4946b729-b990-4193-8de9-2749e6aabeb&title=&width=959)
### 10.6 运行TestNG测试
TestNG是Java社区中除JUnit之外另一个流行的单元测试框架。NG是Next Generation的缩写，译为“下一代”。

TestNG较JUnit的一大优势在于它支持**测试组**的概念，如下的注解会将测试方法加入到两个测试组util和medium中：
```java
@Test（groups={"util"，"medium"}）
```
由于用户可以自由地标注方法所属的测试组，因此这种机制能让用户在方法级别对测试进行归类。这一点JUnit无法做到，它只能实现类级别的测试归类。
## 第十三章 版本管理
阅读本章的时候还需要分清**版本管理（Version Management）**和**版本控制（Version Control）**的区别。**版本管理是指项目整体版本的演变过程管理，如从1.0-SNAPSHOT到1.0，再到1.1-SNAPSHOT。版本控制是指借助版本控制工具（如Subversion）追踪代码的每一个变更。**本章重点讲述的是版本管理，但是读者将会看到，版本管理通常也会涉及一些版本控制系统的操作及概念。请在阅读的时候特别留意这两者的关系和区别。


