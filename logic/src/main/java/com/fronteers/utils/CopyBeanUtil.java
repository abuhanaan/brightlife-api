package com.fronteers.utils;

import java.beans.PropertyDescriptor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class CopyBeanUtil {
  public static void copyNonNullProperties(Object src, Object target) {
    BeanWrapper srcWrap = new BeanWrapperImpl(src);
    BeanWrapper targetWrap = new BeanWrapperImpl(target);

    for (PropertyDescriptor descriptor : srcWrap.getPropertyDescriptors()) {
      String propertyName = descriptor.getName();
      Object srcValue = srcWrap.getPropertyValue(propertyName);
      if (srcValue != null && targetWrap.isWritableProperty(propertyName)) {
        targetWrap.setPropertyValue(propertyName, srcValue);
      }
    }
  }
}
