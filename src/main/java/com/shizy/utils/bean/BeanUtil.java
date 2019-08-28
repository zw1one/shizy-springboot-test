package com.shizy.utils.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanUtil {

    /**
     * 如果一个参数，他不是string，你要把get他然后toString，如果他不存在，get出来就是null，null.toString()就gg
     */
    public static String getString(Map map, String param) {
        return map.containsKey(param) ? map.get(param).toString() : null;
    }

    /***********************************************************/

    /**
     * 复制map中的参数到实体类中，以entity中成员变量是否存在为准
     * 注：两个类的成员变量(或get方法)的名字、类型需一致
     * 成员变量建议定义成包装类，基本类型没有值默认是0，null与0是不同的
     *
     * @param paramMap 键值对与Entity成员变量对应的map
     * @param entity   被填充内容的Entity
     * @return entity 被填充内容的Entity 可以不处理这个返回，参数中的引用类型entity，其值已经被改变
     */
    public static <T> T copyMapParam2Entity(Map paramMap, T entity) {

        if (paramMap == null) {
            return entity;
        }

        Class<?> aClass = entity.getClass();
        //迭代set
        for (Method method : aClass.getMethods()) {
            if (method.getName().indexOf("set") != 0) {
                continue;
            }
            String field = method.getName().substring(3, 4).toLowerCase() +
                    method.getName().substring(4);
            try {
                method.invoke(entity, paramMap.get(field));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return entity;
    }

    public static <S, T> T copyParam2Entity(S source, T target, String[] ignoreFields) {

        if (source == null || target == null) {
            return target;
        }

        Class<?> aClass = target.getClass();
        //迭代set
        for (Method method : aClass.getMethods()) {
            if (method.getName().indexOf("set") != 0) {
                continue;
            }

            String field = method.getName().substring(3, 4).toLowerCase() +
                    method.getName().substring(4);

            if (isIgnoredField(ignoreFields, field)) {
                continue;
            }

            Object sourceFieldValue = null;
            try {
                Method crtMethod = source.getClass()
                        .getMethod("get" + field.substring(0, 1).toUpperCase() + field.substring(1));
                crtMethod.setAccessible(true);

                sourceFieldValue = crtMethod.invoke(source);

                if (sourceFieldValue == null) {
                    continue;
                }

                if (!method.getParameterTypes()[0].isAssignableFrom(sourceFieldValue.getClass())) {
                    continue;
                }

                method.invoke(target, sourceFieldValue);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
            }
        }
        return target;
    }

    /**
     * 复制entity中的参数到other entity中，以target中成员属性是否存在为准
     *
     * @param source 提供数据的entity 参数命名需与target一致
     * @param target 被填充内容的entity
     * @return target 被填充内容的target 可以不处理这个返回，参数中的引用类型target，其值已经被改变
     */
    public static <S, T> T copyParam2Entity(S source, T target) {
        return copyParam2Entity(source, target, null);
    }

    private static boolean isIgnoredField(String[] ignoreFields, String field) {

        if (ignoreFields == null) {
            return false;
        }

        for (String ignoreField : ignoreFields) {
            if (ignoreField.equals(field)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 复制泛型S的List数据到泛型T的List中，以target中成员属性是否存在为准
     *
     * @param sourceList    泛型S的List
     * @param targetGeneric 用于确定结果List的泛型T
     * @param ignoreField   从target中忽略填值的字段
     * @return 泛型T的结果List
     */
    public static <S, T> List<T> copyParam2EntityList(List<S> sourceList, T targetGeneric, String... ignoreField) {
        Class targetClass = targetGeneric.getClass();
        List<T> targetList = new ArrayList<>();
        for (S s : sourceList) {
            try {
                targetList.add(copyParam2Entity(s, (T) targetClass.newInstance(), ignoreField));
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return targetList;
    }

    /***********************************************************/

    public static <T> Map<? extends String, ?> genMapFromEntity(T entity, Map<String, Object> existMap) {

        Class<?> aClass = entity.getClass();
        //迭代get
        for (Method method : aClass.getMethods()) {
            if (method.getName().indexOf("get") != 0 || method.getName().indexOf("getClass") == 0) {
                //若不为get或为getClass
                continue;
            }
            String field = method.getName().substring(3, 4).toLowerCase() +
                    method.getName().substring(4);
            try {
                existMap.put(field, method.invoke(entity));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return existMap;
    }

    /**
     * entity to map
     */
    public static <T> Map<? extends String, ?> genMapFromEntity(T entity) {
        return genMapFromEntity(entity, new HashMap<>());
    }


    /***********************************************************/

    public static <S, T> T get(S obj, String field, Class<T> fieldClass) {

        try {
            return (T) obj.getClass().getMethod(
                    "get" + field.substring(0, 1).toUpperCase() + field.substring(1)
            ).invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
        }
        return null;
    }

    public static <S, V> S set(S obj, String field, V value, Class fieldClass) {
        Method m = null;
        //get Method
        try {
            m = obj.getClass().getMethod(
                    "set" + field.substring(0, 1).toUpperCase() + field.substring(1),
                    fieldClass
            );
        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
        }
        //invoke
        try {
            if (m != null) {
                m.invoke(obj, value);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Test
    public void getSetTest() throws Exception {
        Archer entity = new Archer();

        BeanUtil.set(entity, "attack", 111, int.class);
        int z = BeanUtil.get(entity, "attack", int.class);

        System.out.println();
    }

    /***********************************************************/

    @Test
    public void copyMapParam2EntityTest() throws Exception {
        Map paramMap = new HashMap();
        paramMap.put("name", "ashe");
        paramMap.put("title", "ice archer");
        paramMap.put("attack", 67);

        Archer entity = new Archer();

        BeanUtil.copyMapParam2Entity(paramMap, entity);

        System.out.println();
    }

    @Test
    public void copyParam2EntityTest() throws Exception {
        Assassin source = new Assassin("zed", "Lord of Shadows", 80, "hehehe");
        Archer target = new Archer();

        BeanUtil.copyParam2Entity(source, target);

        System.out.println();
    }

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Archer {
    private String name;
    private String title;
    private int attack;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Assassin {
    private String name;
    private String title;
    private int attack;
    private String finalSkill;
}











