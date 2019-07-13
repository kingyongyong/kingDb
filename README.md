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
	
## 3、创建数据库表
### 创建一个实体类
```
@KingTable("t_student")
public class Student {
    //学号
    @KingField(value = "id", primaryKey = true, notNull = true)
    private Long studentId;
    private String name;
    //年龄
    @KingField(value = "age", notNull = true)
    private Integer age;
    //性别
    private Boolean gender;
    //分数
    private Integer score;
}
```
### 3.1 注解
#### @KingTable("t_student")
该注解表明该实体类映射的数据库表名叫 t_student，如果不加这个注解，表名默认是类名
#### @KingField(value = "id",primaryKey = true,notNull = true,unique = true)
该注解，value表明成员变量名在数据库表中映射的字段名
primaryKey = true,notNull = true,unique = true这三个约束都懂
然后如果不加注解，默认表字段名就是成员变量名字，然后primaryKe,notNull,unique默认都是false，就是没有这个约束

### 3.2 数据类型
！！！注意，kingDb只支持以下数据类型（足够用了）
```
Byte
Short
Integer
Long
Float
Double
Boolean
String
Date
Time
Timestamp
List<Byte>
List<Short>
List<Integer>
List<Long>
List<Float>
List<Double>
List<Boolean>
List<String>
```
不能用基础数据类型，要用包装数据类型，另外三个时间要用java.sql包下的
### 3.3 写好数据实体类要在 manifest 文件中注册
```
<application
		...
		>
		...

        <meta-data
            android:name="king_db"
            android:value="com.sqy.kingdbdemo.entity.Student" />

        ...
</application>

多张表用,(逗号)分割
<meta-data
      android:name="king_db"
      android:value="com.sqy.kingdbdemo.entity.Student,
	  com.sqy.kingdbdemo.entity.StudentTest" />
```
## 4、数据库操作
### 4.1、增
KingDbManager.newInsertBuilder().insert();
```
Student student = new Student();
student.setStudentId(1);
student.setName("学生");
student.setAge(21);
student.setGender(true);
student.setCScore(60);

KingDbManager.newInsertBuilder().insert(student);
```
### 4.2、删
删除可以直接传入实体类对象，但是要求是这个实体类对象必须要有主键，否则删除会不成功，然后也可以根据条件删除
#### 4.2.1直接根据实体类对象删除
```
KingDbManager.newDeleteBuilder().delete(student);
```
#### 4.2.2根据条件删除
例1、删除全部同学信息
```
KingDbManager.newDeleteBuilder().delete(Student.class);
```
例2、删除年龄为20岁的同学
```
KingDbManager.newDeleteBuilder().setCondition( KingDbManager.newQueryBuilder().addEqual("age",20).buildQueryCondition()
                                ).delete(Student.class);
```
例3、//删除年龄在19到21岁的同学
```
KingDbManager.newDeleteBuilder().setCondition( KingDbManager.newQueryBuilder().addBetween("age",19,21).buildQueryCondition()
                                ).delete(Student.class);
```
### 4.3、改
#### 4.3.1直接根据实体类对象进行数据更新（必须有主键）
```
KingDbManager.newDeleteBuilder().update(student);
```
#### 4.3.2根据条件更新
例1、学号为1的同学名字改成张三
```
KingDbManager.newUpdateBuilder().updateValue("name","张三").setCondition(
                                        KingDbManager.newQueryBuilder().addEqual("id",1).buildQueryCondition()
                                ).update(Student.class);
```
### 4.4、查
#### 4.4.1查询条件
 addEqual    等于
 addNotEqual    不等于
 addLessThan    少于
 addLessThanOrEqual     少于或等于
 addMoreThan    多于
 addMoreThanOrEqual    多于或等于
 addMatching    模糊查询
 addNotMatching    与模糊查询相反
 addValueContain    包含
 addValueNotContain    不包行
 addBetween    区间查询
 addLimit    限制查询条数
 addSkip    查询忽略前多少条
 orderBy    排序
 addOr    组合 或查询
addAnd    组合 与查询
#### 4.4.2实例
例1、查询所有同学信息
```
List<Student> data = KingDbManager.newQueryBuilder().query(Student.class)
```
例2、查询学号小于9并且成绩大于70的学生
```
List<Student> data = KingDbManager.newQueryBuilder().addLessThan("id",9).addMoreThan("score",70).query(Student.class));
```
例3、【组合或查询】查询是年龄在19到21岁，并且语文成绩大于70的女生或者数学成绩大于70的男生
```
List<Student> data = KingDbManager.newQueryBuilder()
                                        .addBetween("age",19,21)
                                        .addOr(
                                                KingDbManager.newQueryBuilder()
                                                        .addMoreThan("ChinesScore",70)
                                                        .addEqual("gender",false)
                                                        .buildQueryCondition(),
                                                KingDbManager.newQueryBuilder()
                                                        .addMoreThan("MathScore",70)
                                                        .addEqual("gender",true)
                                                        .buildQueryCondition()
                                        ).query(Student.class)
```
例4、成绩数字包含9的同学
```
List<Student> data = KingDbManager.newQueryBuilder()
                                                .addMatching("score","%9%").query(Student.class));
												
成绩数字9开头的同学:9%
成绩数字9结尾的同学:%9
查找成绩第二位和第三位为 00 的同学:_00%
查找成绩以 2 开头，且长度至少为 3 个字符的同学:2_%_%
查找成绩第二位为 2，且以 3 结尾的同学:_2%3
查找成绩长度为 5 位数，且以 2 开头以 3 结尾的同学:2___3
...
```
例5、成绩为60分、65分的同学
```
List<Student> data = KingDbManager.newQueryBuilder()
                                                .addValueContain("score",60,65).query(Student.class));
```
例6、分页查询，10条数据为一页，查询第3页
```
List<Student> data = KingDbManager.newQueryBuilder()
                                                .addLimit(10).addSkip(20).query(Student.class));
```
例7、排序，正序写表字段名，倒序加一个 - 号
```
按照分数正序
List<Student> data = KingDbManager.newQueryBuilder()
                                                .addLimit(10).addSkip(20).orderBy("score").query(Student.class));
按照分数倒序
List<Student> data = KingDbManager.newQueryBuilder()
                                                .addLimit(10).addSkip(20).orderBy("score").query(Student.class));
按照学号正序，分数倒序
List<Student> data = KingDbManager.newQueryBuilder()
                                                .addLimit(10).addSkip(20).orderBy("id","-score").query(Student.class));
```
### 4.5、升级
#### 4.5.1、新增表
新建一个实体类，然后在 AndroidManifest 文件中注册一下就好了,多个实体类用,（逗号）间隔
```
<meta-data
            android:name="king_db"
            android:value="com.sqy.kingdbdemo.entity.Student" />
```
#### 4.5.2、删除表
只需要在 AndroidManifest 文件中将注册的实体类删除就行
#### 4.5.3、表字段
新增字段、删除字段直接在实体类中进行就可以

### ！！！以上的任何一个升级操作，都必须前往初始化的地方更改一下版本号，只有这样升级才能生效
### 4.6、混淆
混淆打包的时候，需要与数据库映射的实体类不能混淆