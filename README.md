# contacts_plugin

## 支持Android和iOS端

[获取联系人和选择联系人使用示例](https://github.com/coolxinxin/ContactsPlugin/blob/main/example/lib/main.dart)

#### 选择联系人
```
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
```
#### 获取全部联系人
```
  ContactsPlugin.getAllContacts().then((value) {
    for (var contact in value) {
      var number = "";
      if (contact.phones != null &&
          contact.phones!.isNotEmpty) {
        number = contact.phones?.first.value ?? "";
      }
      var name = contact.otherName ?? "";
      debugPrint("phone:" + number);
      debugPrint("name:" + name);
    }
    setState(() {
      allContacts = "The number of all contacts is:" +
          value.length.toString();
    });
  });
```
