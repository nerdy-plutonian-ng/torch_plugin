import 'package:flutter/material.dart';
import 'package:torch_plugin/torch_plugin.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  var isTorchOn = false;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Container(
          color: isTorchOn ? Colors.white : Colors.black,
          child: Center(
            child: FilledButton(
              child: const Text('Toggle torch'),
              onPressed: () {
                TorchPlugin.toggleTorch().then((e) {
                  setState(() {
                    isTorchOn = e;
                  });
                });
              },
            ),
          ),
        ),
      ),
    );
  }
}
