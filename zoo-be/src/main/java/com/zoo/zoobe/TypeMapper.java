package com.zoo.zoobe;

import java.util.HashMap;
import java.util.Map;

public abstract class TypeMapper
{

    public static Map<String, TypeMapper> typeMap = new HashMap();

    static
    {
        typeMap.put("float", new FloatMapper());
        typeMap.put("double", new DoubleMapper());
        typeMap.put("char", new CharMapper());
        typeMap.put("integer", new IntegerMapper());
        typeMap.put("boolean", new BooleanMapper());
    }

    public final static Object getValue(String type, String value)
    {
        if (type == null)
        {
            type = "char";
        }
        TypeMapper mapper = typeMap.get(type.toLowerCase());
        if (mapper == null)
        {
            throw new IllegalArgumentException("Type Not handled: " + type);
        }
        return mapper.mapValue(value);

    }

    public abstract Object mapValue(String s);

    private final static class FloatMapper extends TypeMapper
    {

        @Override
        public Object mapValue(String s)
        {
            return Float.valueOf(s);
        }
    }

    private final static class DoubleMapper extends TypeMapper
    {

        @Override
        public Object mapValue(String s)
        {
            return Double.valueOf(s);
        }
    }
    private final static class IntegerMapper extends TypeMapper
    {

        @Override
        public Object mapValue(String s)
        {
                return Integer.valueOf(s);
            
        }
    }

    private final static class CharMapper extends TypeMapper
    {

        @Override
        public Object mapValue(String s)
        {
            return s;
        }
    }

    private final static class BooleanMapper extends TypeMapper
    {

        @Override
        public Object mapValue(String s)
        {
            return Boolean.parseBoolean(s.toLowerCase());
        }
    }

}
