package com.skintker.constants

object ResponseConstants {
    /** ERRORS */
    const val INVALID_INPUT_RESPONSE = "Invalid input. Verify that your input is not malformed."
    const val INVALID_TOKEN_RESPONSE = "Invalid token. Check that you have the correct authentication info"
    const val GENERIC_ERROR_RESPONSE = "Something went wrong. Please, contact with the support team"

    /* /REPORT */

    const val REPORT_STORED_RESPONSE = "Report stored correctly"
    const val REPORT_EDITED_RESPONSE = "Report edited correctly"
    const val REPORT_NOT_STORED_RESPONSE = "Report couldn't be stored"
    const val REPORT_NOT_EDITED_RESPONSE = "Report couldn't be edited"
    const val BAD_INPUT_DATA = "Invalid input data"

    /* /REPORTS */
    const val REPORTS_NOT_DELETED_RESPONSE = "Reports couldn't be removed correctly"

}