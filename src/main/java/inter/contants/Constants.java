package inter.contants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static final String SUCCESS_MESSAGE = "Notificação recebida com sucesso";
    public static final String ERROR_MESSAGE = "Erro ao obter dados da request!";
    public static final String CONTA_CORRENTE_HEADER = "x-conta-corrente";

    public static final int MAX_CONTENT_LENGTH = 1048576;
    public static final int PORT = 8443;

    public static final String CERT_PATH = "certs/server_ricardo.p12";
    public static final String TRUSTSTORE_PATH = "certs/truststore_ricardo.jks";
    public static final String KEYSTORE_TYPE = "PKCS12";
    public static final String TRUSTSTORE_TYPE = "JKS";
    public static final String BOLETO_URI = "/cobranca";
    public static final String PIX_URI = "/pix";
    public static final String BANKING_URI = "/banking";

    public static final String KEYSTORE_PASSWORD = "KEYSTORE_PASSWORD";
    public static final String TRUSTSTORE_PASSWORD = "TRUSTSTORE_PASSWORD";
    public static final String CONFIG_PATH = "config/Config.json";

}
