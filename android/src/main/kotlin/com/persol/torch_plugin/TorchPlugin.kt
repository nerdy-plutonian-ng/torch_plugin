package com.persol.torch_plugin

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** TorchPlugin */
class TorchPlugin: FlutterPlugin, MethodCallHandler,ActivityAware {



  companion object {
    private const val CAMERA_PERMISSION_REQUEST_CODE = 123
    private var torchState = false
  }

  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var activity : Activity
  private lateinit var cameraManager : CameraManager
  private lateinit var cameraID : String

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "torch_android_ios")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    when(call.method){
      "getTorchState" -> result.success(false)
      "toggleTorch" -> {
        if (checkCameraPermission()) {
          toggleTorch()
          result.success(torchState)
        } else {
          requestCameraPermission()
          result.success(false)
        }
      }
      else -> result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
    cameraManager = activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    cameraID = cameraManager.cameraIdList[0]
  }

  override fun onDetachedFromActivityForConfigChanges() {
    TODO("Not yet implemented")
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    TODO("Not yet implemented")
  }

  override fun onDetachedFromActivity() {
    TODO("Not yet implemented")
  }

  private fun checkCameraPermission() : Boolean {
    return ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
  }

  private fun requestCameraPermission() {
    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
  }

  private fun isTorchOn() : Boolean {
    val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraID)
    val flashAvailable = cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
    ?: false
    val controlAeModeValues = cameraCharacteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES)
    return flashAvailable && controlAeModeValues?.contains(CameraMetadata.CONTROL_AE_MODE_ON_ALWAYS_FLASH) ?: false
  }

  private fun toggleTorch() {
    try {
         if(torchState) turnOff() else turnOn()
    } catch (e:Exception){
      println(e)
      e.printStackTrace()
    }
  }

  private fun turnOn() {
    try {
      cameraManager.setTorchMode(cameraID, true)
      torchState = true
    } catch (e: Exception) {
      // on below line we are handling exception.
      e.printStackTrace()
    }
  }

  // turn off torch
  private fun turnOff(){
    try {
      cameraManager.setTorchMode(cameraID, false)
      torchState =  false
    } catch (e: Exception) {
      // on below line we are handling exception.
      e.printStackTrace()
    }
  }
}