package com.fronteers.models.mappers;

import com.fronteers.brightlife.model.ConsentTypeEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ConsentTypeEnumConverter implements Converter<String, ConsentTypeEnum> {
  @Override
  public ConsentTypeEnum convert(String source) {
    return ConsentTypeEnum.fromValue(source);
  }
}
