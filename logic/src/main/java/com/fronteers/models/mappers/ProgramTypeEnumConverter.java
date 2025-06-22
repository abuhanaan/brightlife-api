package com.fronteers.models.mappers;

import com.fronteers.brightlife.model.ProgramTypeEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProgramTypeEnumConverter implements Converter<String, ProgramTypeEnum> {

  @Override
  public ProgramTypeEnum convert(String source){
    return ProgramTypeEnum.fromValue(source);
  }
}
