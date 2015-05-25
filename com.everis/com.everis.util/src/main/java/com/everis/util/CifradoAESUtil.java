package com.everis.util;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;

public class CifradoAESUtil {
    
    private BlockCipher aesCipher = new AESEngine();
    private PaddedBufferedBlockCipher pbbc;
    private KeyParameter key;
 
    public void setPadding(BlockCipherPadding bcp) {
        this.pbbc = new PaddedBufferedBlockCipher(new CBCBlockCipher(aesCipher), bcp);
    }
 
    public void setKey(byte[] key) {
        this.key = new KeyParameter(key);
    }
 
    public byte[] encrypt(byte[] input) throws CifradoException {
        return processing(input, true);
    }
 
    public byte[] decrypt(byte[] input) throws CifradoException {
        return processing(input, false);
    }
 
    private byte[] processing(byte[] input, boolean encrypt) throws CifradoException {
        pbbc.init(encrypt, key);
        byte[] output = new byte[pbbc.getOutputSize(input.length)];
        int bytesWrittenOut = pbbc.processBytes(input, 0, input.length, output, 0);
        
        try {
            pbbc.doFinal(output, bytesWrittenOut);
        } catch(DataLengthException e) {
            throw new CifradoException(e);
        } catch (IllegalStateException e) {
            throw new CifradoException(e);
        } catch (InvalidCipherTextException e) {
            throw new CifradoException(e);
        }
        
        return output;
    }
}
