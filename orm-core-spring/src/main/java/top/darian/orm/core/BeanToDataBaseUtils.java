package top.darian.orm.core;

import org.springframework.util.StringUtils;
import top.darian.orm.core.common.module.SFunction;
import top.darian.orm.core.common.utils.LambdaUtils;
import top.darian.orm.core.spring.TableInfoServiceBean;
import top.darian.orm.core.spring.beans.util.TableInfoBeanUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a>
 * @date 2021/1/24  下午1:32
 */
public class BeanToDataBaseUtils {

    /**
     * 传入 fieldName 和 clazz
     *
     * @param fieldName
     * @param clazz
     * @return
     */
    public static String getColumnByFieldName(String fieldName, Class<?> clazz) {
        TableInfoServiceBean<?> bean = TableInfoBeanUtils.getBean(clazz);

        return getTableInfoServiceBeanColumnName(bean, fieldName);
    }

    /**
     * 传入 function 和 clazz
     *
     * @param sFunction
     * @param clazz
     * @return
     */
    public static <T, R> String getColumnByFunctionName(SFunction<T, R> sFunction, Class<?> clazz) {
        TableInfoServiceBean<?> bean = TableInfoBeanUtils.getBean(clazz);
        ConcurrentMap<String, String> functionFieldMap = bean.getFunctionFieldMap();
        String functionName = sFunction.getClass().getName();
        String fieldName = functionFieldMap.get(functionName);
        if (StringUtils.isEmpty(fieldName)) {
            fieldName = LambdaUtils.functionToFieldName(sFunction);
            functionFieldMap.put(functionName, fieldName);
        }
        if (StringUtils.isEmpty(fieldName)) {
            throw new RuntimeException("convert fieldName is empty");
        }
        return getTableInfoServiceBeanColumnName(bean, fieldName);
    }

    private static String getTableInfoServiceBeanColumnName(TableInfoServiceBean tableInfoServiceBean, String fieldName) {
        if (StringUtils.isEmpty(fieldName)) {
            throw new RuntimeException("filedName convert to columnName filedName is empty!!!");
        }
        Map<String, String> fieldColumnMap = tableInfoServiceBean.getFieldColumnMap();
        String columnName = fieldColumnMap.get(fieldName);
        if (StringUtils.isEmpty(columnName)) {
            throw new RuntimeException(String.format("filedName convert to columnName , error, " +
                    "fieldName{[%s]} convert to columnName , " +
                    "result columnName is empty , " +
                    "please check the fieldName dose has the set or get method !!!", fieldName));
        }
        return columnName;
    }

}
