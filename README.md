## 新闻网站
描述：这是一个新闻网站的Demo，侧重于解决现实生活中的实际问题，用来加深对Java web尤其是Spring相关知识的理解。
### 1. 简单的服务器：
- 处理web请求
- 静态和模板文件
- Velocity
- request/response
- 重定向/Error
- Ioc
- AOP(log为例)
### 2.数据库：
- 数据库创建与连接
- CURD(增更读删)
- Mybatis集成
- 注解配置/配置文件配置
- 与前端结合

### 项目心得：
1. pom.xml的配置很重要，在使用Mybatis的时候因为pom.xml的配置问题卡住了很久(后面查询到原因为Mybatis的版本号的问题)；
2. test文件夹下暂且多余的文件会影响整个项目的运行，因为NewsApplicationTests的存在耽误了很久进度；
3. DAO.xml里面的namespace要对应项目中的结构；
