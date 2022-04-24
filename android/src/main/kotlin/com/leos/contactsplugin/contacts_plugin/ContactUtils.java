package com.leos.contactsplugin.contacts_plugin;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.annimon.stream.Optional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Leo
 * @time: 2022/4/23
 * @desc:
 */
public class ContactUtils {

    private static ContentResolver resolver;

    /**
     * 获取联系人信息
     */
    public static List<ContactsData> getAllContacts(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }
        resolver = context.getContentResolver();
        List<ContactsData> logs = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = Nested.getCursor();
            if (cursor == null) {
                return logs;
            }
            Map<Long, List<ContactsData.Number>> contactIdNumbersMap = Nested.queryAllContactNumbers(context);
            Map<Long, ContactsData.ContactDetail> contactDetailMap = Nested.queryAllContactDetail();

            while (cursor.moveToNext()) {
                Optional<ContactsData> oneOpt = Nested.getOneContact(cursor, contactIdNumbersMap, contactDetailMap);
                if (oneOpt.isPresent()) {
                    logs.add(oneOpt.get());
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return logs;
    }

    private static class Nested {
        @SuppressLint("MissingPermission")
        private static Cursor getCursor() {

            if (resolver == null) {
                return null;
            }
            return resolver.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);
        }


        /**
         * 查询一条联系人信息
         */
        //do not close the cursor
        private static Optional<ContactsData> getOneContact(Cursor cursor,
                                                            Map<Long, List<ContactsData.Number>> contactIdNumbersMap,
                                                            Map<Long, ContactsData.ContactDetail> contactDetailMap) {
            try {
                final String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
                final long lastUpdate = readLastUpdateTime(cursor);

                long contactId = cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                final List<ContactsData.Number> numbers = contactIdNumbersMap.get(contactId);

                final ContactsData.ContactDetail detail = contactDetailMap.get(contactId);

                ContactsData response = new ContactsData() {{
                    setName(name);
                    setLastUpdate(lastUpdate);

                    setNumber(numbers);

                    if (detail != null) {
                        setContact_times(detail.getContact_times());
                        setLast_contact_time(detail.getLast_contact_time());
                        setNickname(detail.getNickname());
                        setRelation(detail.getRelation());
                        setStatus(detail.getStatus());
                    }
                }};

                return Optional.of(response);

            } catch (Exception e) {
                return Optional.empty();
            }
        }

        private static long readLastUpdateTime(Cursor cursor) {
            long lastUpdateTime = 0;

            int index = cursor.getColumnIndex("contact_last_updated_timestamp");

            if (index > -1) {
                return cursor.getLong(index);
            }

            index = cursor.getColumnIndex("contact_status_ts");
            if (index > -1) {
                return cursor.getLong(index);
            }

            return lastUpdateTime;
        }

        /**
         * 查询所有电话号码
         */
        private static Map<Long, List<ContactsData.Number>> queryAllContactNumbers(Context context) {

            Map<Long, List<ContactsData.Number>> idNumbersMap = new HashMap<>();
            Cursor cursor = null;
            try {
                cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);

                if (cursor == null) {
                    return idNumbersMap;
                }

                while (cursor.moveToNext()) {
                    final long contactId = cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));

                    Optional<ContactsData.Number> entityOpt = readOneNumber(cursor, context);

                    if (!entityOpt.isPresent()) {
                        continue;
                    }

                    List<ContactsData.Number> numbers = idNumbersMap.get(contactId);
                    if (numbers == null) {
                        List<ContactsData.Number> init = new ArrayList<>();
                        init.add(entityOpt.get());
                        idNumbersMap.put(contactId, init);
                    } else {
                        numbers.add(entityOpt.get());
                    }
                }

                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
            return idNumbersMap;
        }

        /**
         * 取出一条电话号码
         */
        private static Optional<ContactsData.Number> readOneNumber(Cursor cursor, Context context) {
            try {
                final String number = readNumber(cursor);
                final int contactTimes = readContactTimes(cursor);
                final long lastContactTime = readLastContactTime(cursor);

                final String customerData = readCustomerData(cursor, context);

                ContactsData.Number entity = new ContactsData.Number() {{
                    setNumber(number);
                    setLast_time_used("" + lastContactTime);
                    setTime_used(contactTimes);
                    setType_label(customerData);
                }};

                return Optional.of(entity);
            } catch (Exception e) {
//                UploadUtils.uploadException(e, "readOneNumber");
                return Optional.empty();
            }
        }


        private static long readLastContactTime(Cursor cursor) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                int index = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LAST_TIME_USED);
                if (index > -1) {
                    return cursor.getLong(index);
                }
            } else {
                int index = cursor.getColumnIndex("last_time_contacted");
                if (index > -1) {
                    return cursor.getLong(index);
                }
            }
            return 0;
        }

        private static String readCustomerData(Cursor cursor, Context context) {
            try {
                int data2Index = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DATA2));
                if (data2Index > -1) {
                    CharSequence typeLabel = ContactsContract.CommonDataKinds.Phone.getTypeLabel(context.getResources(), data2Index, "CUSTOME");
                    if (typeLabel != null) {
                        return typeLabel.toString();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }


        private static String readNumber(Cursor cursor) {
            try {
                String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if (!TextUtils.isEmpty(number)) {
                    number = number.replace("-", "");
                    number = number.replace(" ", "");
                }
                return number;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        private static int readContactTimes(Cursor cursor) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                int index = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TIMES_USED);
                if (index > -1) {
                    return cursor.getInt(index);
                }
            } else {
                int index = cursor.getColumnIndex("times_contacted");
                if (index > -1) {
                    return cursor.getInt(index);
                }
            }
            return 0;
        }

        private static Map<Long, ContactsData.ContactDetail> queryAllContactDetail() {
            Map<Long, ContactsData.ContactDetail> contactIdDetailMap = new HashMap<>();
            try {
                Cursor cursor = resolver.query(ContactsContract.Data.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
                if (cursor == null) {
                    return contactIdDetailMap;
                }
                while (cursor.moveToNext()) {
                    try {
                        ContactsData.ContactDetail detail = new ContactsData.ContactDetail();
                        final long contactId = cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.Data.CONTACT_ID));
                        int contactTimes = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Data.TIMES_CONTACTED));
                        String status = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Data.CONTACT_STATUS));
                        long lastContact = cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.Data.LAST_TIME_CONTACTED));
                        detail.setContact_times(contactTimes);
                        detail.setLast_contact_time(lastContact);
                        detail.setStatus(status);
                        int columnIndex = cursor.getColumnIndex("nickname");
                        if (columnIndex > -1) {
                            String nicky = cursor.getString(columnIndex);
                            detail.setNickname(nicky);
                        }
                        columnIndex = cursor.getColumnIndex("data2");
                        if (columnIndex > -1) {
                            String relationShip = cursor.getString(columnIndex);
                            detail.setRelation(relationShip);
                        }

                        contactIdDetailMap.put(contactId, detail);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return contactIdDetailMap;
        }
    }
}
