package sample.jersey;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

@Provider
public class MyObjectMapperProvider implements ContextResolver<ObjectMapper> {

    private final ObjectMapper defaultObjectMapper;

    public MyObjectMapperProvider() {
        defaultObjectMapper = createDefaultMapper();
    }

    @Override
    public ObjectMapper getContext(final Class<?> type) {
        return defaultObjectMapper;
    }

    private static ObjectMapper createDefaultMapper() {
        final ObjectMapper result = new ObjectMapper();
        result.setAnnotationIntrospector(createJaxbJacksonAnnotationIntrospector());
        //result.enable(SerializationFeature.INDENT_OUTPUT);

        return result;
    }

    private static AnnotationIntrospector createJaxbJacksonAnnotationIntrospector() {

        final AnnotationIntrospector jaxbIntrospector = new JaxbAnnotationIntrospector(TypeFactory.defaultInstance());
        final AnnotationIntrospector jacksonIntrospector = new JacksonAnnotationIntrospector();

        return AnnotationIntrospector.pair(jacksonIntrospector, jaxbIntrospector);
    }
}
