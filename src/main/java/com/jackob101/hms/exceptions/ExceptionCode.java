package com.jackob101.hms.exceptions;

public enum ExceptionCode {

    PATIENT_CREATION_FAILED ("02_01", "Patient creation failed"),
    USER_DETAILS_CREATION_FAILED ("01_01", "User details creation failed"),
    USER_DETAILS_BINDING_ERROR ("01_02", "Error occurred while binding request data to model"),
    USER_DETAILS_ALREADY_EXISTS ("01_03", "This user details already exists"),
    USER_DETAILS_UPDATE_FAILED ("01_04", "Update of user details failed");

    public final String code;
    public final String message;


    ExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
