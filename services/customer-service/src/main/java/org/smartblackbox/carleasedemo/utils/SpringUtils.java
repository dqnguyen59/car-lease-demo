package org.smartblackbox.carleasedemo.utils;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

/**
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
public class SpringUtils {

  /**
   * 
   * @param <S>
   * @param <T>
   * @param modelMapper
   * @param source
   * @param targetClass
   * @return
   */
  public static <S, T> List<T> mapList(ModelMapper modelMapper, List<S> source, Class<T> targetClass) {
    return source
        .stream()
        .map(element -> modelMapper.map(element, targetClass))
        .collect(Collectors.toList());
  }

}
