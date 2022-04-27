//
//  SwiftContactsService.swift
//  Txl001
//
//  Created by iOS on 2022/4/24.
//

import Foundation
import Contacts

class ContactService : NSObject {
    
    /// 单例
    static let shared = ContactService.init()
    
    /*
     *作用：遍历通讯录
     */
    func getContatList() -> [[String:Any]] {
        //数据
        var result = [[String:Any]]()
        //获取授权状态
         let  status =  CNContactStore .authorizationStatus( for : .contacts)
         //判断当前授权状态
         guard status == .authorized  else  {
             return result
         }
         
         //创建通讯录对象
         let  store =  CNContactStore ()
         
         //获取Fetch,并且指定要获取联系人中的什么属性
         let  keys = [ CNContactFamilyNameKey ,  CNContactGivenNameKey ,  CNContactNicknameKey ,
                     CNContactOrganizationNameKey ,  CNContactJobTitleKey ,
                     CNContactDepartmentNameKey ,  CNContactPhoneNumbersKey ,
                     CNContactEmailAddressesKey ,  CNContactPostalAddressesKey ,
                     CNContactDatesKey ,  CNContactInstantMessageAddressesKey
         ]
         
         //创建请求对象，需要传入一个(keysToFetch: [CNKeyDescriptor]) 包含'CNKeyDescriptor'类型的数组
         let  request =  CNContactFetchRequest (keysToFetch: keys  as  [ CNKeyDescriptor ])
         
         //遍历所有联系人
         do {
             try store.enumerateContacts(with: request, usingBlock: {[weak self]
                 (contact :  CNContact , stop :  UnsafeMutablePointer < ObjCBool >) ->  Void  in
                 guard let self = self else { return }
                 //
                 result.append(self.contactToDictionary(contact: contact))
             })
         } catch {
             print (error,"报错咯😭")
         }
        //
        print("一共多少条数据=",result.count,"\n\n\n")
        return result
    }
    
    //
    func contactToDictionary(contact: CNContact) -> [String:Any] {
        //
        var result = [String:Any]()
        //获取姓名
        let  lastName = contact.familyName
        let  firstName = contact.givenName
//        let disName = CNContactFormatter.string(from: contact, style: CNContactFormatterStyle.fullName)
//         let mname = contact.middleName
        print ( "姓名：\(lastName)\(firstName)" )
        result["familyName"] = lastName
        result["givenName"] = firstName
        result["other_name"] = "\(firstName)\(lastName)"

        //获取昵称
        let  nikeName = contact.nickname
        print ( "昵称：\(nikeName)" )
        result["nickname"] = nikeName
        
        //获取公司（组织）
        let  organization = contact.organizationName
        print ( "公司（组织）：\(organization)" )
        result["organizationName"] = organization
        
        //获取职位
        let  jobTitle = contact.jobTitle
        print ( "职位：\(jobTitle)" )
        result["jobTitle"] = jobTitle
        
        //获取部门
        let  department = contact.departmentName
        print ( "部门：\(department)" )
        result["departmentName"] = department
        
        //获取电话号码
        print ( "电话：" )
        var phoneNumbers = [[String:String]]()
        for  phone  in  contact.phoneNumbers {
            //获得标签名（转为能看得懂的本地标签名，比如work、home）
            let  label =  CNLabeledValue < NSString >.localizedString(forLabel: phone.label ?? "")
            //获取号码
            let  value = phone.value.stringValue
            print ( "\t\(label)：\(value)" )
            var phoneDictionary = [String:String]()
            phoneDictionary["value"] = phone.value.stringValue
            phoneDictionary["label"] = label
            phoneNumbers.append(phoneDictionary)
        }
        result["phones"] = phoneNumbers
        
        //获取Email
        print ( "Email：" )
        var emailAddresses = [[String:String]]()
        for  email  in  contact.emailAddresses {
            //获得标签名（转为能看得懂的本地标签名）
            let  label =  CNLabeledValue < NSString >.localizedString(forLabel: email.label ?? "")
            //获取值
            let  value = email.value
            print ( "\t\(label)：\(value)" )
            //
            var emailDictionary = [String:String]()
            emailDictionary["value"] = String(email.value)
            emailDictionary["label"] = label
            emailAddresses.append(emailDictionary)
        }
        result["emails"] = emailAddresses
        
        //获取地址
        print ( "地址：" )
        var postalAddresses = [[String:String]]()
        for  address  in  contact.postalAddresses {
            //获得标签名（转为能看得懂的本地标签名）
            let  label =  CNLabeledValue < NSString >.localizedString(forLabel: address.label ?? "")
            //获取值
            let  detail = address.value
            let  contry = detail.value(forKey:  CNPostalAddressCountryKey ) ??  ""
            let  state = detail.value(forKey:  CNPostalAddressStateKey ) ??  ""
            let  city = detail.value(forKey:  CNPostalAddressCityKey ) ??  ""
            let  street = detail.value(forKey:  CNPostalAddressStreetKey ) ??  ""
            let  code = detail.value(forKey:  CNPostalAddressPostalCodeKey ) ??  ""
            let  str =  "国家:\(contry) 省:\(state) 城市:\(city) 街道:\(street) 邮编:\(code)"
            print ( "\t\(label)：\(str)" )
            var addressDictionary = [String:String]()
            addressDictionary["label"] = label
            addressDictionary["address"] = str
            postalAddresses.append(addressDictionary)
        }
        result["postalAddresses"] = postalAddresses
        
        //获取即时通讯(IM)
        print ( "即时通讯(IM)：" )
        var instantMessageAddresses = [[String:String]]()
        for  im  in  contact.instantMessageAddresses {
            //获得标签名（转为能看得懂的本地标签名）
            let  label =  CNLabeledValue < NSString >.localizedString(forLabel: im.label ?? "")
            //获取值
            let  detail = im.value
            let  username = detail.value(forKey:  CNInstantMessageAddressUsernameKey ) ??  ""
            let  service = detail.value(forKey:  CNInstantMessageAddressServiceKey ) ??  ""
            print ( "\t\(label)：\(username) 服务:\(service)" )
            var addressDictionary = [String:String]()
            addressDictionary["label"] = label
            addressDictionary["username"] = username as? String
            instantMessageAddresses.append(addressDictionary)
        }
        result["instantMessageAddresses"] = instantMessageAddresses
        
        print ( "----------------" )
        //
        return result
    }
}

