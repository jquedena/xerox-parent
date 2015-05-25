package com.everis.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;

import com.everis.enums.CifradoMetodo;

/**
 * Metodos para cifrar y descifrar datos
 * 
 * @author jquedena
 *
 */
public class CifradoUtil {

    /**
     * Genera un valor para usarse como llave para el cifrado
     * 
     * @param length, cantidad de caracteres que deben generarse
     * @return String, cadena con valores aleatorios
     */
    private static String getUUID(int length) {
        return RandomStringUtils.random(length, true, true);
    }

    /**
     * Lee un certificado
     * 
     * @param fileName, ruta del archivo
     * @return X509Certificate, objeto certificado
     * @throws CifradoException
     */
    public static X509Certificate getCertificate(String fileName) throws CifradoException {
        X509Certificate cert = null;
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            InputStream in = new FileInputStream(fileName);
            cert = (X509Certificate) certFactory.generateCertificate(in);
        } catch(CertificateException e) {
            throw new CifradoException(e);
        } catch (FileNotFoundException e) {
            throw new CifradoException(e);
        }
        
        return cert;
    }

    /**
     * Lee la llave generada para el ceritficado .crt
     * 
     * @param fileName, ruta del archivo
     * @return PrivateKey, objeto qur contiene la informaci\u00F3n de la llave
     * @throws CifradoException
     */
    public static RSAPrivateKey getPrivateKeyRSA(String fileName) throws CifradoException {
        RSAPrivateKey privateKey = null;
        try {
            FileInputStream fis = new FileInputStream(fileName);
            String key = IOUtils.toString(fis);
            byte[] byteKey = Base64.decodeBase64(key);
    
            privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(byteKey));
        } catch(IOException e) {
            throw new CifradoException(e);
        } catch (InvalidKeySpecException e) {
            throw new CifradoException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new CifradoException(e);
        }
        
        return privateKey;
    }

    /**
     * Cifra un archivo
     * 
     * @param in InputStream, stream de origen
     * @param out OutputStream, stream de destino
     * @param key Key, llave a usar
     * @param cifrar CifradoMetodo, metodo a usar
     * @throws CifradoException 
     */
    public static void encrypt(int mode, byte[] in, OutputStream out, Key key, CifradoMetodo cifrar) throws CifradoException {
        ByteArrayInputStream bais;
        try {
            Cipher cipher = Cipher.getInstance(cifrar.getMetodo());
            cipher.init(mode, key);
            bais = new ByteArrayInputStream(cipher.doFinal(in));
            
            int read = 0;
            int i = 0;
            byte[] buffer = new byte[1024];
            while ((read = bais.read(buffer)) >= 0) {
                out.write(buffer, 0, read);
                i++;
                if ((i % 4) == 0) {
                    out.flush();
                }
            }

            out.flush();
            out.close();

            bais.close();
            
        } catch (NoSuchAlgorithmException e) {
            throw new CifradoException(e);
        } catch (NoSuchPaddingException e) {
            throw new CifradoException(e);
        } catch (InvalidKeyException e) {
            throw new CifradoException(e);
        } catch (IllegalBlockSizeException e) {
            throw new CifradoException(e);
        } catch (BadPaddingException e) {
            throw new CifradoException(e);
        } catch (IOException e) {
            throw new CifradoException(e);
        }
    }
    
    /**
     * Cifra un archivo
     * 
     * @param in InputStream, stream de origen
     * @param out OutputStream, stream de destino
     * @param key Key, llave a usar
     * @param cifrar CifradoMetodo, metodo a usar
     * @throws CifradoException 
     */
    public static void encrypt(InputStream in, OutputStream out, Key key, CifradoMetodo cifrar) throws CifradoException {
        try {
            Cipher cipher = Cipher.getInstance(cifrar.getMetodo());
            cipher.init(Cipher.ENCRYPT_MODE, key);

            CipherOutputStream cos = new CipherOutputStream(out, cipher);
            int read = 0;
            int i = 0;
            byte[] buffer = new byte[1024];
            while ((read = in.read(buffer)) >= 0) {
                cos.write(buffer, 0, read);
                i++;
                if ((i % 4) == 0) {
                    cos.flush();
                }
            }

            cos.flush();
            cos.close();

            in.close();
        } catch (NoSuchAlgorithmException e) {
            throw new CifradoException(e);
        } catch (NoSuchPaddingException e) {
            throw new CifradoException(e);
        } catch (InvalidKeyException e) {
            throw new CifradoException(e);
        } catch (IOException e) {
            throw new CifradoException(e);
        }
    }

    /**
     * Cifra un archivo
     * 
     * @param in InputStream, stream de origen
     * @param out OutputStream, stream de destino
     * @param password  byte[], llave a usar
     * @param cifrar CifradoMetodo, metodo a usar
     * @throws CifradoException 
     */
    public static void encrypt(InputStream in, OutputStream out, byte[] password, CifradoMetodo cifrar) throws CifradoException {
        SecretKeySpec key = new SecretKeySpec(password, cifrar.getAlgoritmo());
        encrypt(in, out, key, cifrar);
    }

    /**
     * Cifra un archivo
     * 
     * @param in String, ruta del archivo de origen
     * @param out String, ruta del archivo de destino
     * @param password String, llave a usar
     * @param cifrar CifradoMetodo, metodo a usar
     * @throws IOException
     * @throws CifradoException 
     */
    public static void encrypt(String in, String out, String password, CifradoMetodo cifrar) throws CifradoException {
        try {
            FileInputStream fis = new FileInputStream(in);
            BufferedInputStream bis = new BufferedInputStream(fis);

            FileOutputStream fos = new FileOutputStream(out);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            encrypt(bis, bos, password.getBytes(), cifrar);
        } catch (FileNotFoundException e) {
            throw new CifradoException(e);
        }
    }

    /**
     * Cifra un archivo zip
     * 
     * @param in String, ruta del archivo de origen
     * @param out String, ruta del archivo de destino
     * @param password String, llave a usar
     * @param cifrar CifradoMetodo, metodo a usar
     * @param mode int, modo
     * @throws IOException
     * @throws CifradoException 
     */
    public static void encryptZip(String in, String out, String password, CifradoMetodo cifrar, int mode) throws CifradoException {
        try {
            File f = new File(in);
            FileInputStream fis = new FileInputStream(in);
            byte[] bytes = new byte[(int) f.length()];
            fis.read(bytes);
            fis.close();
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            SecretKeySpec key = new SecretKeySpec(password.getBytes(), cifrar.getAlgoritmo());
            boolean eval = false;
            eval = eval || CifradoMetodo.AES_128_ECB_PKCS7Padding.compareTo(cifrar) == 0;
            eval = eval || CifradoMetodo.AES_256_ECB_PKCS7Padding.compareTo(cifrar) == 0;
            
            if(eval) {
                Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); 
                encrypt(mode, bytes, baos, key, cifrar);
                ArchivoUtil.escribirArchivo(out, baos.toByteArray());
            } else {
                throw new CifradoException("Algoritmo/Metodo/Relleno no soportado");
            }
        } catch (IOException e) {
            throw new CifradoException(e);
        }
    }

    /**
     * Cifra un archivo zip
     * 
     * @param in String, ruta del archivo de origen
     * @param out String, ruta del archivo de destino
     * @param password String, llave a usar
     * @param cifrar CifradoMetodo, metodo a usar
     * @throws CifradoException
     * @throws IOException 
     */
    public static String encryptZip(String in, String out, CifradoMetodo cifrar) throws CifradoException {
        String password = getUUID(cifrar.getLlave());
        encryptZip(in, out, password, cifrar, Cipher.ENCRYPT_MODE);

        return password;
    }

    /**
     * Cifra un texto
     * 
     * @param in String, texto a cifrar
     * @param out String, ruta del archivo de destino
     * @param key String, llave a usar
     * @param cifrar CifradoMetodo, metodo a usar
     * @throws IOException 
     * @throws CifradoException 
     */
    public static void encryptString(String in, String out, Key key, CifradoMetodo cifrar) throws CifradoException {
        InputStream fis = new ByteArrayInputStream(in.getBytes());

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(out);
        } catch (FileNotFoundException e) {
            throw new CifradoException(e);
        }
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        encrypt(fis, bos, key, cifrar);
    }

    /**
     * Cifra un archivo y retorna la llave usada para cifrar
     * 
     * @param in String, ruta del archivo de origen
     * @param out String, ruta del archivo de destino
     * @param cifrar CifradoMetodo, metodo a usar
     * @return String, retorna la llave usada para cifrar
     * @throws IOException 
     * @throws CifradoException 
     */
    public static String encrypt(String in, String out, CifradoMetodo cifrar) throws IOException, CifradoException {
        String password = getUUID(cifrar.getLlave());
        encrypt(in, out, password, cifrar);

        return password;
    }

    /**
     * Descifra un archivo
     * 
     * @param in InputStream, stream de origen
     * @param out OutputStream, stream de destino
     * @param key Key, llave a usar
     * @param cifrar CifradoMetodo, metodo a usar
     * @throws CifradoException 
     */
    public static void decrypt(InputStream in, OutputStream out, Key key, CifradoMetodo cifrar) throws CifradoException {
        try {
            Cipher cipher = Cipher.getInstance(cifrar.getMetodo());
            cipher.init(Cipher.DECRYPT_MODE, key);

            CipherInputStream cis = new CipherInputStream(in, cipher);
            int read = 0;
            int i = 0;
            byte[] buffer = new byte[1024];
            while ((read = cis.read(buffer)) >= 0) {
                out.write(buffer, 0, read);
                i++;
                if ((i % 4) == 0) {
                    out.flush();
                }
            }

            out.flush();
            out.close();

            cis.close();
        } catch (NoSuchAlgorithmException e) {
            throw new CifradoException(e);
        } catch (NoSuchPaddingException e) {
            throw new CifradoException(e);
        } catch (InvalidKeyException e) {
            throw new CifradoException(e);
        } catch (IOException e) {
            throw new CifradoException(e);
        }

    }

    /**
     * Descifra un archivo
     * 
     * @param in String, ruta del archivo de origen
     * @param out String, ruta del archivo de destino
     * @param key Key, llave a usar
     * @param cifrar CifradoMetodo, metodo a usar
     * @throws IOException 
     * @throws CifradoException 
     */
    public static void decrypt(String in, String out, String password, CifradoMetodo cifrar) throws IOException, CifradoException {
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), cifrar.getAlgoritmo());

        FileInputStream fis = new FileInputStream(in);
        BufferedInputStream bis = new BufferedInputStream(fis);

        FileOutputStream fos = new FileOutputStream(out);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        decrypt(bis, bos, key, cifrar);
    }

    /**
     * Descifra un archivo zip
     * 
     * @param in String, ruta del archivo de origen
     * @param out String, ruta del archivo de destino
     * @param key Key, llave a usar
     * @param cifrar CifradoMetodo, metodo a usar
     * @throws CifradoException 
     * @throws IOException 
     */
    public static void decryptZip(String in, String out, String password, CifradoMetodo cifrar) throws CifradoException {
        encryptZip(in, out, password, cifrar, Cipher.DECRYPT_MODE);
    }

    /**
     * Descifra un archivo
     * 
     * @param in String, ruta del archivo de origen
     * @param key Key, llave a usar
     * @param cifrar CifradoMetodo, metodo a usar
     * @return String, valor del archivo descifrado
     * @throws CifradoException 
     * @throws FileNotFoundException 
     */
    public static String decryptString(String in, Key key, CifradoMetodo cifrar) throws CifradoException {
        FileInputStream fis;
        try {
            fis = new FileInputStream(in);
        } catch (FileNotFoundException e) {
            throw new CifradoException(e);
        }
        
        BufferedInputStream bis = new BufferedInputStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        decrypt(bis, baos, key, cifrar);

        return new String(baos.toByteArray());
    }
}
