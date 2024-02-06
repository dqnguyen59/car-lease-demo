package org.smartblackbox.carleasedemo;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Slf4j
@Component
public class InitServerContext {

  @PostConstruct
  public void init() {
    log.info("init()");

  }

}
