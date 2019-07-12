package com.sqy.kingdbdemo.entity;


import com.sqy.kingdb.annotion.KingField;

public class Student {
    //学号
    @KingField(value = "id",primaryKey = true,notNull = true)
    private Long studentId;
    private String name;
    //年龄
    @KingField(value = "age",notNull = true)
    private Integer age;
    //性别
    private Boolean gender;

    //分数
    private Integer ChinesScore;
    private Integer MathScore;
    private Integer EnglishScore;



    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public Integer getChinesScore() {
        return ChinesScore;
    }

    public void setChinesScore(Integer chinesScore) {
        ChinesScore = chinesScore;
    }

    public Integer getMathScore() {
        return MathScore;
    }

    public void setMathScore(Integer mathScore) {
        MathScore = mathScore;
    }

    public Integer getEnglishScore() {
        return EnglishScore;
    }

    public void setEnglishScore(Integer englishScore) {
        EnglishScore = englishScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
