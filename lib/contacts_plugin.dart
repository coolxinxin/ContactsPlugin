import 'dart:async';

import 'package:flutter/services.dart';

class ContactsPlugin {
  static const MethodChannel _channel = MethodChannel('contacts_plugin');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<Map?> selectContact() async {
    return await _channel.invokeMapMethod("selectContact");
  }

  static Future<List<Contact>> getAllContacts() async {
    Iterable contacts =
        (await _channel.invokeListMethod("getAllContacts")) as Iterable;
    return contacts.map((m) => Contact.fromMap(m)).toList();
  }

}

class Contact {
  String? otherName, otherMobile, lastTime;

  Contact({this.otherName, this.otherMobile, this.lastTime});

  Contact.fromMap(Map m) {
    otherName = m["other_name"];
    otherMobile = m["other_mobile"];
    lastTime = m["last_time"];
  }
}
