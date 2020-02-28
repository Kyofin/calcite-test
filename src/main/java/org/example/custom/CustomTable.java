package org.example.custom;

import org.apache.calcite.DataContext;
import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.linq4j.AbstractEnumerable;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.ScannableTable;
import org.apache.calcite.schema.impl.AbstractTable;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.util.Pair;
import org.apache.calcite.util.Source;

import java.util.ArrayList;
import java.util.List;
/**
 * @describer:
 * 数据表的映射类，
 * ScannableTable 扫描表,实现scan方法，不用担心，框架会调用scan方法
 * AbstractTable 是一个抽象类，大多方法已经实现，在这里只需要实现 getRowType 设置列名和类型即可，框架会调用
 */
public class CustomTable extends AbstractTable implements ScannableTable {
    private Source source;

    public CustomTable(Source source) {
        this.source = source;
    }

    @Override
    public RelDataType getRowType(RelDataTypeFactory relDataTypeFactory) {
        JavaTypeFactory typeFactory = (JavaTypeFactory)relDataTypeFactory;

        List<String> names = new ArrayList<>();
        names.add("value");
        List<RelDataType> types = new ArrayList<>();
        types.add(typeFactory.createSqlType(SqlTypeName.VARCHAR));

        return typeFactory.createStructType(Pair.zip(names,types));
    }

    @Override
    public Enumerable<Object[]> scan(DataContext dataContext) {
        return new AbstractEnumerable<Object[]>() {
            @Override
            public Enumerator<Object[]> enumerator() {
                return new CustomEnumerator<>(source);
            }
        };
    }
}