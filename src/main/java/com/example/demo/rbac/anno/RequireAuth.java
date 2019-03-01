package com.example.demo.rbac.anno;

import java.lang.annotation.*;

/**
 * @Target
 *  表示该注解可以用于什么地方，可能的ElementType参数有：
 *       CONSTRUCTOR：构造器的声明
 *       FIELD：域声明（包括enum实例）
 *       LOCAL_VARIABLE：局部变量声明
 *       METHOD：方法声明
 *       PACKAGE：包声明
 *       PARAMETER：参数声明
 *       TYPE：类、接口（包括注解类型）或enum声明
 * @Retention
 *  表示需要在什么级别保存该注解信息。可选的RetentionPolicy参数包括：
 *      SOURCE：注解将被编译器丢弃
 *      CLASS：注解在class文件中可用，但会被VM丢弃
 * RUNTIME：VM将在运行期间保留注解，因此可以通过反射机制读取注解的信息
 * @Document  将注解包含在Javadoc中
 * @Inherited 允许子类继承父类中的注解
 */
@Documented //文档生成时，该注解将被包含在javadoc中，可去掉
@Target(ElementType.METHOD)//目标是方法
@Retention(RetentionPolicy.RUNTIME) //注解会在class中存在，运行时可通过反射获取
@Inherited
public @interface RequireAuth {
    boolean check() default false;
}