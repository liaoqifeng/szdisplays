package com.koch.bean;

import org.apache.commons.beanutils.converters.AbstractConverter;

public class EnumConverter
  extends AbstractConverter
{
  private final Class<?> clszz;
  
  public EnumConverter(Class<?> enumClass)
  {
    this(enumClass, null);
  }
  
  public EnumConverter(Class<?> enumClass, Object defaultValue)
  {
    super(defaultValue);
    this.clszz = enumClass;
  }
  
  protected Class<?> getDefaultType()
  {
    return this.clszz;
  }
  
  protected Object convertToType(Class type, Object value)
  {
    String str = value.toString().trim();
    return Enum.valueOf(type, str);
  }
  
  protected String convertToString(Object value)
  {
    return value.toString();
  }
}

