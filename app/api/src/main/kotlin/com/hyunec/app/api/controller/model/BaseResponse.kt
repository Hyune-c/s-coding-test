package com.hyunec.app.api.controller.model

abstract class BaseResponse {
    val resultCode: Int = 200
    val resultMsg: String = "OK"
}
