package br.com.mikaioyamada.todolist.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public class Utils {
    //foi atualizado para static para n ser instanciado essa classe em outras classes.
    public static void copyNonNullProperties(Object source , Object target){
        BeanUtils.copyProperties(source,target,getNullPropertyNames(source));
    }
    public static String[] getNullPropertyNames(Object source){ // essa propriedade sera usada pra fazermos um update parcial a onde for necessário
        final BeanWrapper src = new BeanWrapperImpl(source); // interface que fornece para acesso de propriedades de objetos no java

        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();

        for(PropertyDescriptor  pd: pds){
            Object srcValue = src.getPropertyValue(pd.getName());

            if(srcValue == null){
                emptyNames.add(pd.getName());
            }
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
