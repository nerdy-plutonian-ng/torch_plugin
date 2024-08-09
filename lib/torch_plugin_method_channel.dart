import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'torch_plugin_platform_interface.dart';

/// An implementation of [TorchPluginPlatform] that uses method channels.
class MethodChannelTorchPlugin extends TorchPluginPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('torch_plugin');

  @override
  Future<bool?> turnOn() async {
    return await methodChannel.invokeMethod<bool?>('turnOn');
  }

  @override
  Future<bool?> turnOff() async {
    return await methodChannel.invokeMethod<bool?>('turnOff');
  }
}
