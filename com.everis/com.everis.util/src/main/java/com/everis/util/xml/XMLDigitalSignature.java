package com.everis.util.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.everis.util.CifradoException;
import com.everis.util.CifradoUtil;

public class XMLDigitalSignature {

    public static final String RSA_SHA256 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
    private static final Logger LOGGER = Logger.getLogger(XMLDigitalSignature.class);

    public XMLDigitalSignature() {
    }

    public static void createSignatureRSA2048(String in, String out, String certificate, String privateKeyFile) throws XMLDigitalSignatureException {

        XMLSignatureFactory xsf = XMLSignatureFactory.getInstance();
        try {
            Document document = DOMUtil.open(new File(in));

            DigestMethod dm = xsf.newDigestMethod(DigestMethod.SHA256, null);
            Transform trans = xsf.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null);
            Reference ref = xsf.newReference("", dm, Collections.singletonList(trans), null, null);
            SignatureMethod sm = xsf.newSignatureMethod(RSA_SHA256, null);
            CanonicalizationMethod cm = xsf.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE, (C14NMethodParameterSpec) null);
            SignedInfo si = xsf.newSignedInfo(cm, sm, Collections.singletonList(ref));

            Certificate x509 = CifradoUtil.getCertificate(certificate);
            PrivateKey privateKey = CifradoUtil.getPrivateKeyRSA(privateKeyFile);
            KeyInfoFactory kif = xsf.getKeyInfoFactory();
            KeyValue kv = kif.newKeyValue(x509.getPublicKey());

            LOGGER.info(x509.getPublicKey().toString());

            KeyInfo ki = kif.newKeyInfo(Collections.singletonList(kv));

            DOMSignContext dsc = new DOMSignContext(privateKey, document.getDocumentElement());
            dsc.setDefaultNamespacePrefix("");

            XMLSignature signature = xsf.newXMLSignature(si, ki);
            signature.sign(dsc);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transSignature = tf.newTransformer();

            FileOutputStream fos = new FileOutputStream(out);
            transSignature.transform(new DOMSource(document), new StreamResult(fos));
        } catch (NoSuchAlgorithmException e) {
            throw new XMLDigitalSignatureException("No se encuentra el algoritmo usado", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new XMLDigitalSignatureException("Parametro de algoritmo inv\u00E1lido", e);
        } catch (KeyException e) {
            throw new XMLDigitalSignatureException("Llave inv\u00E1lida", e);
        } catch (MarshalException e) {
            throw new XMLDigitalSignatureException("Error de en el proceso de transformaci\u00F3n del archivo", e);
        } catch (javax.xml.crypto.dsig.XMLSignatureException e) {
            throw new XMLDigitalSignatureException("Error al firmar el archivo", e);
        } catch (TransformerConfigurationException e) {
            throw new XMLDigitalSignatureException("Error en la transformaci\u00F3n", e);
        } catch (FileNotFoundException e) {
            throw new XMLDigitalSignatureException("No se pudo encontrar el archivo", e);
        } catch (TransformerException e) {
            throw new XMLDigitalSignatureException("Llave inv\u00E1lida", e);
        } catch (ParserConfigurationException e) {
            throw new XMLDigitalSignatureException("Error de transformaci\u00F3n al leer el archivo", e);
        } catch (SAXException e) {
            throw new XMLDigitalSignatureException("Error de lectura de archivo", e);
        } catch (IOException e) {
            throw new XMLDigitalSignatureException("No se puede leer el archivo", e);
        } catch (CifradoException e) {
            throw new XMLDigitalSignatureException("No se puede leer el la llave privada del certificado", e);
        }
    }

    @SuppressWarnings("rawtypes")
    public static boolean verifySignature(String in) throws XMLDigitalSignatureException {
        boolean valid = false;

        try {
            // Instantiate the document to be validated
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            Document doc = dbf.newDocumentBuilder().parse(new FileInputStream(in));

            // Find Signature element
            NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
            if (nl.getLength() == 0) {
                throw new NullPointerException("Cannot find Signature element");
            }

            // Create a DOM XMLSignatureFactory that will be used to unmarshal
            // the
            // document containing the XMLSignature
            XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

            // Create a DOMValidateContext and specify a KeyValue KeySelector
            // and document context
            DOMValidateContext valContext = new DOMValidateContext(new KeyValueKeySelector(), nl.item(0));

            // unmarshal the XMLSignature
            XMLSignature signature = fac.unmarshalXMLSignature(valContext);

            // Validate the XMLSignature (generated above)
            boolean coreValidity = signature.validate(valContext);

            // Check core validation status
            if (!coreValidity) {
                LOGGER.error("Signature failed core validation");
                boolean sv = signature.getSignatureValue().validate(valContext);
                LOGGER.error("signature validation status: " + sv);
                // check the validation status of each Reference
                Iterator i = signature.getSignedInfo().getReferences().iterator();
                int j = 0; 
                while (i.hasNext()) {
                    boolean refValid = ((Reference) i.next()).validate(valContext);
                    LOGGER.error("ref[" + j + "] validity status: " + refValid);
                    j++;
                }
            } else {
                valid = true;
                LOGGER.info("Signature passed core validation");
            }
        } catch (NullPointerException e) {
            throw new XMLDigitalSignatureException("Error al validar la firma del archivo, no se encuentra el elemento de la firma", e);
        } catch (Exception e) {
            throw new XMLDigitalSignatureException("Error al validar la firma del archivo", e);
        }

        return valid;
    }

    /**
     * KeySelector which retrieves the public key out of the KeyValue element
     * and returns it. NOTE: If the key algorithm doesn't match signature
     * algorithm, then the public key will be ignored.
     */
    @SuppressWarnings("rawtypes")
    private static class KeyValueKeySelector extends KeySelector {
        
        @Override
        public KeySelectorResult select(KeyInfo keyInfo, KeySelector.Purpose purpose, AlgorithmMethod method, XMLCryptoContext context) throws KeySelectorException {
            if (keyInfo == null) {
                throw new KeySelectorException("Null KeyInfo object!");
            }
            SignatureMethod sm = (SignatureMethod) method;
            List list = keyInfo.getContent();

            for (int i = 0; i < list.size(); i++) {
                XMLStructure xmlStructure = (XMLStructure) list.get(i);
                if (xmlStructure instanceof KeyValue) {
                    PublicKey pk = null;
                    try {
                        pk = ((KeyValue) xmlStructure).getPublicKey();
                    } catch (KeyException ke) {
                        throw new KeySelectorException(ke);
                    }
                    // make sure algorithm is compatible with method
                    if (algEquals(sm.getAlgorithm(), pk.getAlgorithm())) {
                        return new SimpleKeySelectorResult(pk);
                    }
                }
            }
            throw new KeySelectorException("No KeyValue element found!");
        }

        // This should also work for key types other than DSA/RSA
        static boolean algEquals(String algURI, String algName) {
            boolean equalsAlg = false;
            if ("DSA".equalsIgnoreCase(algName) && SignatureMethod.DSA_SHA1.equalsIgnoreCase(algURI)) {
                equalsAlg = true;
            } else if ("RSA".equalsIgnoreCase(algName) && RSA_SHA256.equalsIgnoreCase(algURI)) {
                equalsAlg = true;
            }
            
            return equalsAlg;
        }
    }

    private static class SimpleKeySelectorResult implements KeySelectorResult {
        private PublicKey pk;

        SimpleKeySelectorResult(PublicKey pk) {
            this.pk = pk;
        }

        @Override
        public Key getKey() {
            return pk;
        }
    }

}
