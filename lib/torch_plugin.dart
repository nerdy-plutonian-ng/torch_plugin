import 'package:flutter/services.dart';

class TorchPlugin {
/*  Future<bool?> turnOn() {
    return TorchPluginPlatform.instance.turnOn();
  }

  Future<bool?> turnOff() {
    return TorchPluginPlatform.instance.turnOff();
  }
  */

  static const _channel = MethodChannel('torch_android_ios');

  static Future<bool> toggleTorch() async =>
      await _channel.invokeMethod('toggleTorch');
}
