import Flutter
import UIKit

public class SwiftContactsPlugin: NSObject, FlutterPlugin {

  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "contacts_plugin", binaryMessenger: registrar.messenger())
    let instance = SwiftContactsPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    switch call.method {
        case "getPlatformVersion":
        result("iOS " + UIDevice.current.systemVersion)
    case "selectContact":
        result(nil)
        case "getAllContacts":
        result(ContactService.shared.getContatList())
        default: result(FlutterMethodNotImplemented)
    }
  }

}
