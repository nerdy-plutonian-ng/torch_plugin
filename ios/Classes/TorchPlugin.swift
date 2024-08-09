import Flutter
import UIKit
import AVFoundation

public class TorchPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "torch_android_ios", binaryMessenger: registrar.messenger())
    let instance = TorchPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    switch call.method {
    case "toggleTorch":
        let permissionIsGranted = isCameraPErmissionGiven()
        if permissionIsGranted {
            result(toggleTorch())
        } else {
            requestCameraPermission()
        }
    default:
      result(FlutterMethodNotImplemented)
    }
  }
}

func isCameraPErmissionGiven() -> Bool {
    let status = AVCaptureDevice.authorizationStatus(for: .video)
    return status == .authorized
}

func requestCameraPermission(){
    AVCaptureDevice.requestAccess(for: .video) { Bool in
    }
}

func toggleTorch() -> Bool {
    let device1 = AVCaptureDevice.default(for: .video)
        guard let device = AVCaptureDevice.default(for: .video) else { return false}
    if device.hasTorch {
            do {
                try device.lockForConfiguration()
                let on = device.isTorchActive
                device.torchMode = on ? .off : .on
                device.unlockForConfiguration()
                return !on
            } catch{
                print("Torch could not be used")
                return device.isTorchActive
            }
    } else {
        print("Torch is not available")
        return false
       }
}
