package com.jackob101.hms.exceptions;

public enum ExceptionCode {

    PATIENT_CREATION_FAILED ("02_01", "Patient creation failed"),
    PATIENT_UPDATE_FAILED ("02_02", "Patient update failed"),
    PATIENT_VALIDATION_ERROR ("02_03", "Patient validation error"),
    PATIENT_NOT_FOUND ("02_04", "Patient not found"),
    PATIENT_ID_NULL("02_05", "Patient id cannot be null"),
    USER_DETAILS_CREATION_FAILED ("01_01", "User details creation failed"),
    USER_DETAILS_BINDING_ERROR ("01_02", "Error occurred while binding request data to model"),
    USER_DETAILS_ALREADY_EXISTS ("01_03", "This user details already exists"),
    USER_DETAILS_UPDATE_FAILED ("01_04", "Update of user details failed"),
    USER_DETAILS_VALIDATION_FAILED ("01_05", "User details validation failed"),
    EMPLOYEE_VALIDATION_FAILED ("03_01", "Employee validation failed");

    public final String code;
    public final String message;


    ExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
