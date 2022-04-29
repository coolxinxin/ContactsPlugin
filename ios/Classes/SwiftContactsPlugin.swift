import Flutter
import UIKit
import ContactsUI
import Foundation

public class SwiftContactsPlugin: NSObject, FlutterPlugin {

  private var pickerDelegate: CNContactPickerDelegate?
    
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "contacts_plugin", binaryMessenger: registrar.messenger())
    let instance = SwiftContactsPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

   private func requestPicker(result: @escaping FlutterResult) {
          let controller = CNContactPickerViewController()
          pickerDelegate = ContactPickerDelegate(result: result)
          controller.delegate = pickerDelegate
          controller.displayedPropertyKeys = [CNContactPhoneNumbersKey]
          var viewController = UIApplication.shared.delegate?.window??.rootViewController
          while ((viewController?.presentedViewController) != nil) {
              viewController = viewController?.presentedViewController
          }
          viewController?.present(controller, animated: true, completion: nil)
      }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    switch call.method {
        case "getPlatformVersion":
        result("iOS " + UIDevice.current.systemVersion)
    case "selectContact":
        requestPicker(result: result)
        case "getAllContacts":
        result(ContactService.shared.getContatList())
        default: result(FlutterMethodNotImplemented)
    }
  }

}

@available(iOS 9.0, *)
public class ContactPickerDelegate : NSObject, CNContactPickerDelegate {
    
    private let result: FlutterResult
    
    init(result: @escaping FlutterResult) {
        self.result = result
    }
    
    public func contactPicker(_ picker: CNContactPickerViewController, didSelect contactProperty: CNContactProperty) {
        let contact = contactProperty.contact
        let fullName = CNContactFormatter.string(from: contact, style: CNContactFormatterStyle.fullName)
        let itemRaw = contactProperty.value
        var item: Any?
        if(itemRaw is CNPhoneNumber) {
            item = (itemRaw as! CNPhoneNumber).stringValue
        } else {
            item = itemRaw
        }
        let label = stringifyLabel(label: contactProperty.label)
        let dict = [
            "name": fullName as Any,
            "number": item ?? "",
//            "number": [
//                "number": item,
//                "label": label
//            ],
            ] as [String : Any]
        result(dict)
    }
    
    private func stringifyLabel(label: String?) -> String {
        if(label == nil) {
            return ""
        }
        
        return CNLabeledValue<NSString>.localizedString(forLabel: label!)
    }
    
    public func contactPickerDidCancel(_ picker: CNContactPickerViewController) {
        result(nil)
    }
}
