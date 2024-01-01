package com.example.cloudsystem.security;

import com.example.cloudsystem.data.entities.enums.PermissionType;
import com.example.cloudsystem.exceptions.ForbiddenException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Method;
import java.util.Collection;

@Aspect
@Configuration
public class SecurityAspect {

    @Around("@annotation(com.example.cloudsystem.security.CheckSecurity)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        CheckSecurity checkSecurity = method.getAnnotation(CheckSecurity.class);
        for(PermissionType permission: checkSecurity.permissions()){
            if(!authorities.contains(permission)){
                throw new ForbiddenException("You don't have " + permission + " permission.");
            }
        }
        return joinPoint.proceed();
    }
}
