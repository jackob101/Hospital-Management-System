package com.jackob101.hms.deserializers.user;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.jackob101.hms.model.user.Employee;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.service.user.definition.UserDetailsService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashSet;

@Slf4j
public class EmployeeDeserializer extends StdDeserializer<Employee> {

    private UserDetailsService userDetailsService;

    public EmployeeDeserializer() {
        this(null);
        log.info("Employee Deserializer initialized");
    }

    protected EmployeeDeserializer(Class<?> vc) {
        super(vc);
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Employee deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {

        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);

        Long userDetailsId = null;
        JsonNode userDetailsIdNode = jsonNode.get("userDetailsId");
        if (userDetailsIdNode != null && !userDetailsIdNode.isNull())
            userDetailsId = userDetailsIdNode.longValue();

        UserDetails userDetails = userDetailsService.find(userDetailsId);

        JsonNode idNode = jsonNode.get("id");
        Long id = null;
        if (idNode != null && !idNode.isNull())
            id = idNode.longValue();

        //TODO implement after implementing specializations
//        HashSet<Specialization> specializations = new HashSet<>();
//
//        jsonNode.get("specializations")
//                .elements()
//                .forEachRemaining(entry -> {
//
//        });


        return new Employee(id, userDetails, new HashSet<>());
    }
}
