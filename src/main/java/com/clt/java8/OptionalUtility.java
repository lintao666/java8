package com.clt.java8;

import java.util.Optional;

/**
 * @date 2019/4/26 12:04
 */
public class OptionalUtility {
    public static Optional<Integer> stringToInt(String s){
        try{//如果String可以转换为对应的Integer
            return Optional.of(Integer.parseInt(s));
        }catch (NumberFormatException e){//不能转换返回一个空的Optional对象
            return Optional.empty();
        }
    }
}
