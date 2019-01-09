package com.haresh.demo.formatconversion;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

public class ConversionConfig extends WebMvcConfigurationSupport {

	@Override
	   public FormattingConversionService mvcConversionService() {
	       FormattingConversionService f = super.mvcConversionService();
	       f.addConverter(new BulbEnumConvertor());
	       return f;
	   }
}
