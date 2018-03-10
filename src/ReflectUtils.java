import java.lang.reflect.Field;

public class ReflectUtils {

    private ReflectUtils (){ }

    @SuppressWarnings("rawtypes")
    public static Object getValueOf(Object clazz, String lookingForValue)
            throws Exception {
        Field field = clazz.getClass().getField(lookingForValue);
        Class clazzType = field.getType();
        if (clazzType.toString().equals("double"))
            return field.getDouble(clazz);
        else if (clazzType.toString().equals("int"))
            return field.getInt(clazz);
        // else other type ...
        // and finally
        return field.get(clazz);
    }

    public static void main(String[] args) throws Exception{

    }


}
