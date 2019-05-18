package com.apu.studyinfoprotection.lab5;

import com.apu.studyinfoprotection.REST.api.RestBasePacket;
import com.apu.studyinfoprotection.REST.api.RestErrorPacket;
import com.apu.studyinfoprotection.lab1.REST.comtroller.lab1RestController;
import com.apu.studyinfoprotection.lab5.REST.api.Lab4RestCheckSignedMessageRequest;
import com.apu.studyinfoprotection.lab5.REST.api.Lab4RestCheckSignedMessageResponse;
import com.apu.studyinfoprotection.lab5.REST.api.Lab4RestSignMessageRequest;
import com.apu.studyinfoprotection.lab5.REST.api.Lab4RestSignMessageResponse;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author apu
 */
@RestController
public class Lab5RestController {
    
    private static final Logger LOGGER = 
                        LogManager.getLogger(lab1RestController.class);
    
    @RequestMapping(path="/rest/lab4/sign",  method=RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestBasePacket signMessage(@RequestBody Lab4RestSignMessageRequest request) {
        
        try {
            String message = request.getMessage().trim();
            if(message.length() == 0) {
                return new RestErrorPacket("Wrong message length");
            }

            int keylength = 0;
            String keylengthStr = request.getKeylength().trim();
            try {
                keylength = Integer.parseInt(keylengthStr);
                if((keylength != 512) && (keylength != 1024))
                    throw new NumberFormatException();
            } catch(NumberFormatException ex) {
                return new RestErrorPacket("Wrong keylength - " + keylengthStr);
            }        
        
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyPairGen.initialize(keylength, random);
            KeyPair    keyPair    = keyPairGen.genKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey  publicKey  = keyPair.getPublic();
            
            // Создание подписи
            Signature signature = Signature.getInstance("SHA1withDSA", "SUN");
            // Инициализация подписи закрытым ключом
            signature.initSign(privateKey);
            
            // Формирование цифровой подпись сообщения с закрытым ключом
            signature.update(message.getBytes(StandardCharsets.UTF_8));
            // Байтовый массив цифровой подписи
            byte[] signatureBytes = signature.sign();
            
            Lab4RestSignMessageResponse response = new Lab4RestSignMessageResponse();
            response.setMessage(message);

            String publicKeyStr = BytesToHexStr(publicKey.getEncoded());
            String privateKeyStr = BytesToHexStr(privateKey.getEncoded());           
            String signatureStr = BytesToHexStr(signatureBytes);
            
            response.setPrivateKey(privateKeyStr);
            response.setPublicKeyDetailed(publicKey.toString());
            response.setPublicKey(publicKeyStr);
            response.setSignedMessage(signatureStr);
            
            return response;
        } catch (NullPointerException ex) {
            return new RestErrorPacket("Generation keys pair error. NullPointerException.");
        } catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
            return new RestErrorPacket("Generation keys pair error.");
        } catch (InvalidKeyException ex) {
            return new RestErrorPacket("Generation keys pair error. InvalidKeyException.");
        } catch (SignatureException ex) {
            return new RestErrorPacket("Generation keys pair error. SignatureException.");
        }
    }
    
    @RequestMapping(path="/rest/lab4/checksigned",  method=RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestBasePacket checkSignedMessage(@RequestBody Lab4RestCheckSignedMessageRequest request) {
            
        try {
            String message = request.getMessage().trim();
            if(message.length() == 0) {
                return new RestErrorPacket("Wrong message length");
            }
            
            String sign = request.getSign().trim();
            if(sign.length() == 0) {
                return new RestErrorPacket("Wrong signedMessage length");
            }

            String publicKeyStr = request.getPublicKey().trim();
            if(publicKeyStr.length() == 0) {
                return new RestErrorPacket("Wrong publicKey length");
            } 
            
            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
            byte[] signBytes = HexStrToBytes(sign);
            byte[] publicKeyBytes = HexStrToBytes(publicKeyStr);
            
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);            

            Signature signature = Signature.getInstance("SHA1withDSA", "SUN");
            signature.initVerify(publicKey);
            signature.update(messageBytes);            
            
            Boolean result = signature.verify(signBytes);
            
            Lab4RestCheckSignedMessageResponse response = new Lab4RestCheckSignedMessageResponse();
            response.setResult(result.toString());
            
            return response;
        } catch (NullPointerException ex) {
            return new RestErrorPacket("Checking signed message error. NullPointerException.");
        } catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
            return new RestErrorPacket("Checking signed message error.");
        } catch (InvalidKeyException ex) {
            return new RestErrorPacket("Checking signed message error. InvalidKeyException.");
        } catch (SignatureException ex) {
            return new RestErrorPacket("Checking signed message error. SignatureException.");
        } catch (InvalidKeySpecException ex) {
            return new RestErrorPacket("Checking signed message error. InvalidKeySpecException.");
        }
    }
    
    private String BytesToHexStr(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
    
    private byte[] HexStrToBytes(String str) {
        byte[] strBytesTemp = str.getBytes();
        byte[] strBytes = new byte[str.length()/2];
        for (int i = 0, resIndex = 0; i < strBytesTemp.length; i += 2, resIndex++) {
            strBytes[resIndex] =
                    (byte)(Integer.parseInt(new String(strBytesTemp, i, 2), 16));
        }
        return strBytes;
    }
    
}
