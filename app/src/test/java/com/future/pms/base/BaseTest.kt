package com.future.pms.base

import com.future.pms.core.model.*
import com.future.pms.core.model.customerdetails.Body
import com.future.pms.history.model.History
import com.future.pms.maps.model.ParkingZoneLatLng
import com.future.pms.superadmin.activitydetails.model.Booking
import com.future.pms.superadmin.activitydetails.model.Content
import com.future.pms.superadmin.listuser.model.admin.Admin
import com.future.pms.superadmin.listuser.model.admin.ParkingZoneResponse
import com.future.pms.superadmin.listuser.model.admin.nonPage.AdminDetail
import com.future.pms.superadmin.listuser.model.admin.nonPage.AdminResponse
import com.future.pms.superadmin.listuser.model.customer.CustomerResponse
import com.future.pms.superadmin.listuser.model.superadmin.SuperAdminResponse
import com.future.pms.superadmin.userdetails.model.User
import com.future.pms.superadmin.userdetails.model.UserDetails
import org.junit.Before
import org.junit.Rule
import org.mockito.MockitoAnnotations

open class BaseTest {
  @get:Rule val rxSchedulerRule = RxSchedulerRule()

  @Before fun onSetup() {
    MockitoAnnotations.initMocks(this)
  }

  protected val ACCESS_TOKEN = "accessToken"
  protected val FCM_TOKEN = "fcmToken"
  protected val REFRESH_TOKEN = "refreshToken"
  protected val SUCCESS = "success"
  protected val ERROR = "error"
  protected val ID = "id"
  protected val PARKING_LAYOUT = "parkingLayout"
  protected val USERNAME = "username"
  protected val EMAIL = "email"
  protected val PASSWORD = "password"
  protected val NAME = "name"
  protected val PHONE_NUMBER = "phoneNumber"
  protected val ROLE = "role"
  protected val LEVEL_NAME = "levelName"
  protected val LAYOUT = "layout"
  protected val PAGE = 0
  protected val FILTER = "filter"
  protected val STR = "empty"

  protected fun customerBooking(): CustomerBooking {
    return CustomerBooking(0L, 0L, "idBooking", "idParkingZone", "idSlot", "idUser",
        "parkingZoneName", "address", 0.0, "slotName", "levelName", "totalTime", "imageUrl")
  }

  protected fun customer(): Body {
    return Body("email", "idCustomer", "name", "phoneNumber")
  }

  protected fun customerWithPassword(): Customer {
    return Customer("email", "name", "password", "phoneNumber")
  }

  protected fun user(): User {
    return User("email", "password", "role")
  }

  protected fun userDetails(): UserDetails {
    return UserDetails("email", "idUser", "password", "role")
  }

  protected fun admin(): Admin {
    return Admin(emptyList(), empty = false, first = false, last = false, number = 0,
        numberOfElements = 0, pageable = page(), size = 0, sort = sort(), totalElements = 0,
        totalPages = 0)
  }

  private fun adminDetail(): AdminDetail {
    return AdminDetail("address", "emailAdmin", "idParkingZone", "imageUrl", "name", "openHour",
        "phoneNumber", 0.0, 0.0, 0.0)
  }

  protected fun parkingZone(): ParkingZoneResponse {
    return ParkingZoneResponse("address", "emailAdmin", "name", "openHour", "password",
        "phoneNumber", 0.0, "imageUrl", 0.0, 0.0)
  }

  protected fun superAdmin(): SuperAdminResponse {
    return SuperAdminResponse(bodySuperAdmin(), "statusCode", 0)
  }

  protected fun history(): History {
    return History(emptyList(), empty = false, first = false, last = false, number = 0,
        numberOfElements = 0, pageable = page(), size = 0, sort = sort(), totalElements = 0,
        totalPages = 0)
  }

  protected fun receipt(): Receipt {
    return Receipt(0L, 0L, "idBooking", "customerName", "parkingZoneName", "address", 0.0,
        "slotName", 0, 0, "totalPrice", "status")
  }

  protected fun booking(): Booking {
    return Booking(emptyList(), empty = false, first = false, last = false, number = 0,
        numberOfElements = 0, pageable = page(), size = 0, sort = sort(), totalElements = 0,
        totalPages = 0)
  }

  protected fun content(): Content {
    return Content("address", "customerName", "customerEmail", "customerPhone", 0L, 0L, "idBooking",
        "idParkingZone", "idSlot", "idUser", "imageUrl", "levelName", "parkingZoneName", 0.0,
        "totalPrice", "slotName", "totalTime", null)
  }

  protected fun customerResponse(): CustomerResponse {
    return CustomerResponse(body(), "statusCode", 0)
  }

  protected fun customerUserDetails(): com.future.pms.core.model.customerdetails.Customer {
    return com.future.pms.core.model.customerdetails.Customer(customer(), "statusCode", 0)
  }

  protected fun adminUserDetails(): AdminResponse {
    return AdminResponse(adminDetail(), "statusCode", 0)
  }

  private fun body(): com.future.pms.superadmin.listuser.model.customer.Body {
    return com.future.pms.superadmin.listuser.model.customer.Body(emptyList(), empty = false,
        first = false, last = false, number = 0, numberOfElements = 0, pageable = page(), size = 0,
        sort = sort(), totalElements = 0, totalPages = 0)
  }

  private fun bodySuperAdmin(): com.future.pms.superadmin.listuser.model.superadmin.Body {
    return com.future.pms.superadmin.listuser.model.superadmin.Body(emptyList(), empty = false,
        first = false, last = false, number = 0, numberOfElements = 0, pageable = page(), size = 0,
        sort = sort(), totalElements = 0, totalPages = 0)
  }

  protected fun token(): Token {
    return Token("scope", "token_type", 0L, "refresh_token", "id_token", "access_token", "role")
  }

  private fun sort(): Sort {
    return Sort(empty = false, sorted = false, unsorted = false)
  }

  private fun page(): Pageable {
    return Pageable(0, 0, 0, false, sort(), false)
  }

  private fun parkingZoneLatLng(): ParkingZoneLatLng {
    return ParkingZoneLatLng("", 0.0, 0.0, "name", "openHours", "phoneNumber", "imageUrl", 0.0)
  }

  protected fun listParkingZoneLatLng(): List<ParkingZoneLatLng> {
    return listOf(parkingZoneLatLng())
  }
}