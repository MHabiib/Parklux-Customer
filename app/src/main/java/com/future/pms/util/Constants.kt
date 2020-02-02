package com.future.pms.util

class Constants {
  companion object {
    const val BASE = "https://pms-future.herokuapp.com/"
    const val USERNAME = "pms-client"
    const val PASSWORD = "pms-secret"
    const val GRANT_TYPE = "password"
    const val GRANT_TYPE_REFRESH = "refresh_token"
    const val AUTHORIZATION = "Authorization"
    const val WRITE_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L

    const val AUTHENTICATION = "authentication"
    const val HOME_FRAGMENT = "Home Fragment"
    const val SCAN_FRAGMENT = "Scan Fragment"
    const val HISTORY_FRAGMENT = "History Fragment"
    const val PARKING_DETAIL_FRAGMENT = "Parking Detail Fragment"
    const val RECEIPT_FRAGMENT = "Receipt Fragment"
    const val ONGOING_FRAGMENT = "Ongoing Fragment"
    const val LIST_ACTIVITY_FRAGMENT = "List Activity Fragment"
    const val LIST_USER_FRAGMENT = "List User Fragment"
    const val BOOKING_DETAIL_FRAGMENT = "Booking Detail Fragment"
    const val HOME_FRAGMENT_SUPER_ADMIN = "Home Fragment Super Admin"
    const val TOKEN = "token"
    const val PROFILE_FRAGMENT = "Profile Fragment"
    const val ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN"
    const val ROLE_CUSTOMER = "ROLE_CUSTOMER"
    const val UPDATE_SUPER_ADMIN = "UPDATE_SUPER_ADMIN"
    const val UPDATE_ADMIN = "UPDATE_ADMIN"
    const val UPDATE_CUSTOMER = "UPDATE_CUSTOMER"
    const val ROLE_ADMIN = "ROLE_ADMIN"
    const val ROLE = "role"
    const val ID_USER = "idUser"
    const val ERROR = "Error"
    const val NULL = "null"
    const val ID_BOOKING = "idBooking"
    const val LEVEL_NAME = "levelName"
    const val PARKING_ZONE_NAME = "name"
    const val PARKING_ZONE_ADDRESS = "address"
    const val PARKING_ZONE_PHONE = "phone"
    const val PARKING_ZONE_PRICE = "price"
    const val PARKING_ZONE_OPEN_HOUR = "openHour"
    const val PARKING_ZONE_IMG = "img"
    const val PARKING_ZONE_LONGITUDE = "longitude"
    const val PARKING_ZONE_LATITUDE = "latitude"

    const val REQUEST_CAMERA_PERMISSION = 2020

    const val NOT_FOUND_CODE = "404"
    const val UNAUTHORIZED_CODE = "401"
    const val BAD_REQUEST_CODE = "400"
    const val NO_CONNECTION = "No address associated with hostname"

    const val FULL_DATE_TIME_FORMAT = "HH:mm - dd MMMM yyyy "
    const val SHORT_MONTH_DATE_TIME_FORMAT = "HH:mm - dd MMM yy"
    const val TIME_FORMAT = "HH:mm"
    const val SEC_IN_DAY = 3600000

    const val ALL = "all"
    const val ONGOING = "ongoing"
    const val PAST = "past"

    const val STATUS_ONGOING = "Ongoing"

    const val parkSize = 88
    const val SLOTS_IN_ROW = 16
    const val parkMargin = 0
    const val parkPadding = 0
    const val STATUS_AVAILABLE = 1
    const val STATUS_BOOKED = 2
    const val STATUS_RESERVED = 3
    const val STATUS_ROAD = 4
    const val STATUS_IN = 5
    const val STATUS_OUT = 6
    const val STATUS_BLOCK = 7
    const val SLOT_TAKEN = 'T'
    const val SLOT_EMPTY = 'E'
    const val SLOT_ROAD = 'R'
    const val SLOT_NULL = '_'
    const val SLOT_READY = 'O'
    const val SLOT_SCAN_ME = 'S'
    const val SLOT_IN = 'I'
    const val SLOT_OUT = 'Q'
    const val SLOT_BLOCK = 'B'
    const val MY_SLOT = 'V'
    const val DISABLED_SLOT = 'D'

    const val REQUEST_LOCATION_PERMISSION = 1
  }
}