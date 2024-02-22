package sn.esmt.gesb.components;

import org.springframework.stereotype.Component;
import sn.esmt.gesb.soam.*;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@Component
public class CurrentStateComponent {

    public <T> EsbContent getEsbParameters(T object) {
        EsbContent esbContent = new EsbContent();
        List<EsbParameter> esbParameters = new ArrayList<>();
        EsbServices esbServices = new EsbServices();

        Class<?> clazz = object.getClass();

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {

            if (!Modifier.isPublic(field.getModifiers())) {
                try {
                    field.setAccessible(true);
                } catch (InaccessibleObjectException e) {
                    continue;
                }
            }

            if (!field.isEnumConstant()) {

                String name = field.getName();

                Object value = null;
                try {
                    value = field.get(object);

                    if (value == null) continue;

                    if (value instanceof String || value instanceof Boolean) {
                        EsbParameter esbParameter = buildEsbParameter(name, value.toString(), null);
                        esbParameters.add(esbParameter);
                    } else if (value instanceof Enum<?> ) {
                        Enum<?> enums = (Enum<?>) value;
                        EsbParameter esbParameter = buildEsbParameter(name, enums.name(), null);
                        esbParameters.add(esbParameter);
                    } else if (value instanceof List) {
                        ArrayList<?> list = (ArrayList<?>) value;
                        for (Object subObject : list) {
                            EsbContent esbContent2 = getEsbParameters(subObject);
                            EsbService esbService = buildEsbService(VerbType.ADD, esbContent2.getEsbParameter());
                            esbServices.getEsbService().add(esbService);
                        }
                    } else {
                        EsbContent esbContent2 = getEsbParameters(value);
                        esbServices.getEsbService().addAll(esbContent2.getEsbServices().getEsbService());
                        esbParameters.addAll(esbContent2.getEsbParameter());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    // todo : throw custom error here
                }
            }
        }

        esbContent.setEsbServices(esbServices);
        esbContent.getEsbParameter().addAll(esbParameters);
        return esbContent;
    }

    public EsbParameter buildEsbParameter(String name, String newValue, String oldValue) {
        EsbParameter esbParameter = new EsbParameter();
        esbParameter.setName(name);
        esbParameter.setNewValue(newValue);
        esbParameter.setOldValue(oldValue);
        return esbParameter;
    }

    public EsbService buildEsbService(VerbType verb, List<EsbParameter> esbParameter) {
        EsbService esbService = new EsbService();
        esbService.setVerb(verb);
        esbService.getEsbParameter().addAll(esbParameter);
        return esbService;
    }
}
