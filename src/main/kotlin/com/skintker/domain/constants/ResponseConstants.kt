package com.skintker.domain.constants

object ResponseConstants {
    /** ERRORS */
    const val INVALID_INPUT_RESPONSE = "Invalid input. Verify that your input is not malformed"
    const val INVALID_USER_ID_RESPONSE = "Invalid user id"
    const val INVALID_USER_TOKEN_RESPONSE = "Invalid user token"
    const val INVALID_USER_ID_OR_TOKEN_RESPONSE = "Invalid user id or token"
    const val GENERIC_ERROR_RESPONSE = "Something went wrong. Please, contact with the support team"
    const val DATABASE_ERROR = "Error in the database"

    const val INVALID_PARAM_RESPONSE = "Invalid query param"

    /* /REPORT */
    const val REPORT_STORED_RESPONSE = "Report stored correctly"
    const val REPORT_EDITED_RESPONSE = "Report edited correctly"
    const val REPORT_NOT_STORED_RESPONSE = "Report couldn't be stored"
    const val REPORT_NOT_EDITED_RESPONSE = "Report couldn't be edited"
    const val BAD_INPUT_DATA = "Invalid input data"
    const val REPORT_NOT_DELETED_RESPONSE = "Report couldn't be removed"
    const val REPORT_DELETED_RESPONSE = "Report removed correctly"

    /* /REPORTS */
    const val REPORTS_NOT_DELETED_RESPONSE = "Reports couldn't be removed"
    const val REPORTS_DELETED_RESPONSE = "Reports removed correctly"

    /*  /USER */
    const val USER_NOT_CREATED_RESPONSE = "User couldn't be created"
    const val USER_CREATED_RESPONSE = "User created"

}
