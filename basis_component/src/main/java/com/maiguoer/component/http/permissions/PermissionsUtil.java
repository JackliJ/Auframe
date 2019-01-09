package com.maiguoer.component.http.permissions;

import android.Manifest;

/**
 * 全局的权限管理
 * Create by www.lijin@foxmail.com on 2019/1/8 0008.
 * <br/>
 */

public class PermissionsUtil {

    /**
     * 全局权限Code
     */
    public static final int REQUEST_CODE_PERMISSION = 0x1007;

    /**
     * 6.0所需权限
     */
    public static final String[] PERMISSION_STRS = {Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.USE_SIP, Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_WAP_PUSH, Manifest.permission.RECEIVE_MMS,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

}
