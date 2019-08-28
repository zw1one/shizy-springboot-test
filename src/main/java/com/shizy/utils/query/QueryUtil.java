package com.shizy.utils.query;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class QueryUtil<PO, DTO> {

    /**
     * 根据dto中的参数与注解，得到对应po的查询Wrapper
     * 注意：dto、po中的int(基本类型)要用(Integer)包装类型，因为它默认有值为0，即视为传入了参数
     *
     * @param dto 传入参数 dto中需要标明注解
     * @param po  指定Wrapper的entity
     */
    public static <PO, DTO> Wrapper<PO> getEntityCondition(DTO dto, PO po) {
        try {
            return getEntityCondition0(dto, po);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <PO, DTO> Wrapper<PO> getEntityCondition0(DTO dto, PO po) throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {

        if (dto == null) {
            return null;
        }

        Wrapper<PO> entityWrapper = new EntityWrapper<PO>(po);

        Map betweensMap = new HashMap();

        //迭代get方法
        Method[] mhs = dto.getClass().getMethods();
        for (Method m : mhs) {
            if (m.getName().indexOf("get") != 0 || m.getName().equals("getClass")) {
                continue;
            }
            Object value = m.invoke(dto);
            if (value == null) {
                continue;
            }
            //从dto的get方法中得到成员变量field字符串
            String field = new StringBuffer()
                    .append(m.getName().substring(3, 4).toLowerCase())
                    .append(m.getName().substring(4))
                    .toString();

            //读取dto上的QueryParam注解
            Field dtoFieldObj = dto.getClass().getDeclaredField(field);
            QueryParam qpAnno = dtoFieldObj.getAnnotation(QueryParam.class);
            //dtoFieldObj.setAccessible(true);//get注解好像不需要这个，而且这个不稳定，可以被java安全机制禁用掉

            if (qpAnno == null) {
                continue;
            }

            if (qpAnno.eq()) {
                entityWrapper.eq(getColumnByPo(field, po), value);

            } else if (qpAnno.like()) {
                entityWrapper.like(getColumnByPo(field, po), value.toString());

            } else if (qpAnno.betweenStart() || qpAnno.betweenEnd()) {
                if (betweensMap.containsKey(qpAnno.column())) {
                    betweensMap.put(qpAnno.column()
                            , new Object[]{betweensMap.get(qpAnno.column()), value});
                } else {
                    betweensMap.put(qpAnno.column(), value);
                }
            } else {
                continue;
            }

        }

        //处理between
        betweensMap.forEach((key, value) -> {
            try {
                entityWrapper.between(getColumnByPo(key.toString(), po), ((Object[]) value)[0], ((Object[]) value)[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return entityWrapper;
    }

    //根据field从po得到数据库列名
    private static <PO, DTO> String getColumnByPo(String field, PO po) throws NoSuchFieldException {
        TableField tableField = po.getClass().getDeclaredField(field)
                .getAnnotation(TableField.class);
        if (tableField != null) {
            return tableField.value();
        }

        TableId tableId = po.getClass().getDeclaredField(field)
                .getAnnotation(TableId.class);
        if (tableId != null) {
            return tableId.value();
        }

        return field;
    }

    @Test
    public void getEntityConditionTest() throws Exception {
        Object z = QueryUtil.getEntityCondition(
                new QueryDemoDTO("name", "address",
                        null, 15, 20,
                        null, "2018-01-20", "2018-03-20"),
                new QueryDemoPO()
        );
        System.out.println();
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class QueryDemoPO {
    private String name;
    private String address;
    private Integer age;
    private String brithday;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class QueryDemoDTO {

    @QueryParam(eq = true)
    private String name;

    @QueryParam(like = true)
    private String address;

    @QueryParam(eq = true)
    private Integer age;

    @QueryParam(column = "age", betweenStart = true)
    private Integer ageStart;

    @QueryParam(column = "age", betweenEnd = true)
    private Integer ageEnd;

    @QueryParam(eq = true)
    private String brithday;

    @QueryParam(column = "brithday", betweenStart = true)
    private String brithdayStart;

    @QueryParam(column = "brithday", betweenEnd = true)
    private String brithdayEnd;

}
















