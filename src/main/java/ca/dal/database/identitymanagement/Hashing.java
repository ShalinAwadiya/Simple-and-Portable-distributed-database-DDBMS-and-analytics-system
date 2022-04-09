package ca.dal.database.identitymanagement;

import ca.dal.database.query.QueryParser;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hashing {
    public  String getHash(String string){
        MessageDigest digest = null;
         final Logger logger = Logger.getLogger(QueryParser.class.getName());
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return bytesToHex(digest.digest(string.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * @param hash
     * @return
     */
    private  String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
