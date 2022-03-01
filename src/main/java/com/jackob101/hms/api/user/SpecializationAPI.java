package com.jackob101.hms.api.user;

import com.jackob101.hms.api.BaseController;
import com.jackob101.hms.model.user.Specialization;
import com.jackob101.hms.service.user.definition.ISpecializationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(SpecializationAPI.REQUEST_MAPPING)
@RestController
public class SpecializationAPI extends BaseController<Specialization, Specialization> {

    public final static String REQUEST_MAPPING = "specializations";

    public SpecializationAPI(ISpecializationService service) {
        super(service, REQUEST_MAPPING, null);
    }
}
