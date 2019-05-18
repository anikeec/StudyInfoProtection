/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.studyinfoprotection.REST.api;

/**
 *
 * @author apu
 */
public class RestPacketType {
    
    public static String ERROR_PACKET = "ErrorPacket";
    public static String ENCRYPT_MESSAGE_REQUEST = "EncryptMessageRequest";
    public static String ENCRYPT_MESSAGE_RESPONSE = "EncryptMessageResponse";
    public static String DECRYPT_MESSAGE_REQUEST = "DecryptMessageRequest";
    public static String DECRYPT_MESSAGE_RESPONSE = "DecryptMessageResponse";
    public static String SIGN_MESSAGE_REQUEST = "SignMessageRequest";
    public static String SIGN_MESSAGE_RESPONSE = "SignMessageResponse";
    public static String CHECK_SIGNED_MESSAGE_REQUEST = "CheckSignedMessageRequest";
    public static String CHECK_SIGNED_MESSAGE_RESPONSE = "CheckSignedMessageResponse";
    
}
