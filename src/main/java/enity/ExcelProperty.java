package enity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jireh
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelProperty {

    // 指定 JavaBean 的属性对应 excel 的第几列
    int index();

    // 当 JavaBean 的属性为 Date 类型时，指定 Date 的格式化模式
    String format() default "";
}