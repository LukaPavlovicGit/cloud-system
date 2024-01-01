package com.example.cloudsystem.security;

import com.example.cloudsystem.data.entities.enums.PermissionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckSecurity {
    PermissionType[] permissions() default {};
}
