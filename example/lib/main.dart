import 'dart:async';

import 'package:contacts_plugin_plus/contacts_plugin.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:permission_handler/permission_handler.dart';

import 'button.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion =
          await ContactsPlugin.platformVersion ?? 'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  var selectText = "SelectContact";
  var allContacts = "getAllContacts";

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: [
            Container(
              margin: const EdgeInsets.only(top: 20),
              child: Text('Running on: $_platformVersion\n'),
            ),
            Button(
              text: selectText,
              margin: const EdgeInsets.fromLTRB(34.0, 35.0, 34.0, 0.0),
              click: () async {
                if (await Permission.contacts.request().isGranted) {
                  ContactsPlugin.selectContact().then((value) {
                    // if (value != null) {
                    //   var name = value["name"] ?? "";
                    //   var number = value["number"] ?? "";
                    var name = value.name ?? "";
                    var number = value.number ?? "";
                    setState(() {
                      selectText = name + "/" + number;
                    });
                    // }
                  });
                  debugPrint("Permission granted");
                } else {
                  //permission denied 使用是请自行处理权限
                  debugPrint("Permission denied");
                }
              },
            ),
            Button(
              text: allContacts,
              margin: const EdgeInsets.fromLTRB(34.0, 35.0, 34.0, 0.0),
              click: () async {
                if (await Permission.contacts.request().isGranted) {
                  ContactsPlugin.getAllContacts().then((value) {
                    for (var contact in value) {
                      var number = contact.phones?.first.value ?? "";
                      var name = contact.otherName ?? "";
                      debugPrint("phone:" + number);
                      debugPrint("name:" + name);
                    }
                    setState(() {
                      allContacts = "The number of all contacts is:" +
                          value.length.toString();
                    });
                  });
                  debugPrint("Permission granted");
                } else {
                  //permission denied 使用是请自行处理权限
                  debugPrint("Permission denied");
                }
              },
            )
          ],
        ),
      ),
    );
  }

  void selectContact(Map? value) {
    if (value != null) {
      var name = value["name"];
      var number = value["number"];
      setState(() {
        selectText = name + "/" + number;
      });
    }
  }
}
