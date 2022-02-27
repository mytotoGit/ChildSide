package com.ishuinzu.childside.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldMapping {
    String columnName();

    PhysicalType physicalType();

    LogicalType logicalType() default LogicalType.Physical;

    boolean canUpdate() default false;

    String splitRegex() default "\\s+";

    enum PhysicalType {String, Long, Int, Double, Blob}

    enum LogicalType {String, Long, Int, Boolean, Array, EnumInt, Double, Physical}
}