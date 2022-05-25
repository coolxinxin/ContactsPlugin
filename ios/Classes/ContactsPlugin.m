#import "ContactsPlugin.h"
#if __has_include(<contacts_plugin_plus/contacts_plugin_plus-Swift.h>)
#import <contacts_plugin_plus/contacts_plugin_plus-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "contacts_plugin_plus-Swift.h"
#endif

@implementation ContactsPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftContactsPlugin registerWithRegistrar:registrar];
}
@end
