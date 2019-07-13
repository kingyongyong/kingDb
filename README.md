# kingDb
## 1、集成
#### 项目根目录下的build.gradle
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
#### app目录下的build.gradle
```
dependencies {
	...
    implementation 'com.github.kingyongyong:kingDb:1.0.1'

}
```
## 2、初始化
```
(1) 将数据库文件置于内部存储中
KingDbHelper.withThis(this).
      setSavePathType(SavePathType.INNER_CARD).
      setDatabaseName("king_db_demo").
      setVersion(1).
      setDatabasePath("","").
      complete();
	  
(2) 将数据库文件置于外部存储中（需要申请动态权限）
KingDbHelper.withThis(this).
      setSavePathType(SavePathType.EXTERNAL_CARD).
      setDatabaseName("king_db_demo").
	  setDatabasePath("aaa","bbb").
      setVersion(1).
      complete();
	  
	  
----------------------------------------------------

```
#### withThis(this) 
    this为当前上下文
#### setSavePathType(SavePathType.INNER_CARD)    
    数据库文件会被放在内部存储中，app卸载会被一起删除
#### setSavePathType(SavePathType.EXTERNAL_CARD)    
    数据库文件被放在外部存储中，app卸载不会被删除（使用这个需要申请存储权限）
#### setDatabaseName("king_db_demo")
    设置数据库的名字
#### setDatabasePath("aaa","bbb")
    设置当数据库文件放着外部存储的时候，存放的路径，如果不设置默认放在根目录下的kingdb文件夹中，上面设置的是放着根目录的aaa文件夹下bbb的文件夹中
	！！！如果使用的存储方式是内部存储，则不用设置这个
#### setVersion(1)
    设置版本号，当升级的时候（新增表、删除表、新增字段、删除字段），需要将版本号加1（其实只要大于当前版本就行）才能生效升级
#### complete()
    完成初始化（必须调用）
	
## 2、创建数据库表