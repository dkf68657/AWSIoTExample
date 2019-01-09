package com.haresh.demo.formatconversion;

import org.springframework.core.convert.converter.Converter;

import com.haresh.demo.device.BulbType;

public class BulbEnumConvertor implements Converter<String, BulbType> {
    
	@Override
    public BulbType convert(String source) {
       try {
          return BulbType.valueOf(source);
       } catch(Exception e) {
          return null; 
       }
    }
}