package com.everis.enums;

public enum CifradoMetodo {
    
    /*
     * AES/CBC/NoPadding (128)
     * AES/CBC/PKCS5Padding (128)
     * AES/ECB/NoPadding (128)
     * AES/ECB/PKCS5Padding (128)
     * DES/CBC/NoPadding (56)
     * DES/CBC/PKCS5Padding (56)
     * DES/ECB/NoPadding (56)
     * DES/ECB/PKCS5Padding (56)
     * DESede/CBC/NoPadding (168)
     * DESede/CBC/PKCS5Padding (168)
     * DESede/ECB/NoPadding (168)
     * DESede/ECB/PKCS5Padding (168)
     * RSA/ECB/PKCS1Padding (1024, 2048)
     * RSA/ECB/OAEPWithSHA-1AndMGF1Padding (1024, 2048)
     * RSA/ECB/OAEPWithSHA-256AndMGF1Padding (1024, 2048)
     */
    
    AES_128_ECB_NoPadding("AES", "AES/ECB/NoPadding", 16),
    AES_128_ECB_PKCS5Padding("AES", "AES/ECB/PKCS5Padding", 16),
    AES_128_ECB_PKCS7Padding("AES", "AES/ECB/PKCS7Padding", 16),
    AES_256_ECB_NoPadding("AES", "AES/ECB/NoPadding", 32),
    AES_256_ECB_PKCS5Padding("AES", "AES/ECB/PKCS5Padding", 32),
    AES_256_ECB_PKCS7Padding("AES", "AES/ECB/PKCS7Padding", 32),
    AES_128_CBC_NoPadding("AES", "AES/CBC/NoPadding", 16),
    AES_128_CBC_PKCS5Padding("AES", "AES/CBC/PKCS5Padding", 16),
    AES_256_CBC_NoPadding("AES", "AES/CBC/NoPadding", 32),
    AES_256_CBC_PKCS5Padding("AES", "AES/CBC/PKCS5Padding", 32),
    RSA_1024_ECB_PKCS1Padding("RSA", "RSA/ECB/PKCS1Padding", 64),
    RSA_2048_ECB_PKCS1Padding("RSA", "RSA/ECB/PKCS1Padding", 128);
    
    private String algoritmo;
    private String metodo;
    private int llave;
    
    CifradoMetodo(String algoritmo, String metodo, int llave) {
    	this.algoritmo = algoritmo;
    	this.metodo = metodo;
    	this.llave = llave;
    }
    
    public String getAlgoritmo() {
    	return algoritmo;
    }
    
    public String getMetodo() {
    	return metodo;
    }
    
    public int getLlave() {
    	return llave;
    }

}
