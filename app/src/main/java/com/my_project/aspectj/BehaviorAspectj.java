package com.my_project.aspectj;

import android.util.Log;

import com.my_project.behaviortrace.BehaviorTrace;

/**
 * Created by com_c on 2018/1/26.
 */

//  @Aspect 需要导入 AOP jar包才能使用
public class BehaviorAspectj {
    /**
     * 定义切面的规则：
     * 1、切面长什么样子，哪些方法让这个切面去处理
     * 把代码中有注解BehaviorTrace的所有内容都加入到我们这个切面
     */
//    @Poinrcut("execution(@包名.要使用的注解类名)")
    public void method() {
    }


    /**
     * AOP核心使用，  在全局业务可以使用AOP框架
     * 2、切面上的内容使用规则
     * @Before  在切入点之前执行  使用场景：权限验证
     * @After  在切入点之后执行  使用场景：同一资源释放
     * @Around  在切入点前后都执行  使用场景：性能检测和用户行为统计
     * 在使用的时候在具体的方法上 标注当时我们create的注解   格式如下：@BehaviorTrace（“功能说明”）
     * */
   /* @Around("method()")
    public Object weaveJoinPoint(ProceedingJoinPoint point) throws Throwable{
//        当前执行的内容是哪个类里面的
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        //当前执行的是哪个方法
        String methodName = methodSignature.getName();
        //当前执行了什么样的功能
        BehaviorTrace behaviorTrace = methodSignature.getMethod().getAnnotation(BehaviorTrace);
        String funName = behaviorTrace.value();
        long begin = System.currentTimeMillis();
        //执行功能
        Object result = point.proceed();
        long duration = System.currentTimeMillis() - begin;
        Log.d("com_c",String.format("功能：%s,%s类的%s方法执行，耗时：%dms",funName,className,duration));
        return result;
    }*/
}
