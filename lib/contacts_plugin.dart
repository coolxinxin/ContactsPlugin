import 'dart:async';

import 'package:flutter/services.dart';

class ContactsPlugin {
  static const MethodChannel _channel = MethodChannel('contacts_plugin');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<SelectContact> selectContact() async {
    return SelectContact.fromMap(await _channel.invokeMapMethod("selectContact") as Map);
  }

  static Future<List<Contact>> getAllContacts() async {
    Iterable contacts =
        (await _channel.invokeListMethod("getAllContacts")) as Iterable;
    return contacts.map((m) => Contact.fromMap(m)).toList();
  }

}

class SelectContact{
  String? name, number;

  SelectContact({this.name, this.number});

  SelectContact.fromMap(Map m){
    name = m["name"];
    number = m["number"];
  }
}

class Contact {
  String? otherName, lastTime;
  List<Phone>? phones = [];

  Contact({this.otherName, this.phones, this.lastTime});

  Contact.fromMap(Map m) {
    otherName = m["other_name"];
    phones = (m["phones"] as List?)?.map((e) => Phone.fromMap(e)).toList();
    lastTime = m["last_time"];
  }
}

class Phone{
  String? label , value;

  Phone({this.label, this.value,});

  Phone.fromMap(map){
    label = map["label"];
    value = map["value"];
  }
}
