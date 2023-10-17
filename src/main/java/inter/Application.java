package inter;

import com.fasterxml.jackson.databind.ObjectMapper;
import inter.initializer.HttpServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Map;

import static inter.contants.Constants.CERT_PATH;
import static inter.contants.Constants.CONFIG_PATH;
import static inter.contants.Constants.KEYSTORE_PASSWORD;
import static inter.contants.Constants.KEYSTORE_TYPE;
import static inter.contants.Constants.PORT;
import static inter.contants.Constants.TRUSTSTORE_PASSWORD;
import static inter.contants.Constants.TRUSTSTORE_PATH;
import static inter.contants.Constants.TRUSTSTORE_TYPE;

public class Application {

    public static void main(String[] args) throws UnrecoverableKeyException, CertificateException, KeyStoreException,
            IOException, NoSuchAlgorithmException, InterruptedException {

        KeyManagerFactory keyManagerFactory = getKeyManagerFacotory();
        TrustManagerFactory trustManagerFactory = getTrustStoreManagerFactory();

        startServer(keyManagerFactory, trustManagerFactory);

    }

    private static String getPassword(String typePassword) throws IOException {

        Path path = Paths.get(CONFIG_PATH);
        byte[] byteArray = Files.readAllBytes(path);
        String json = new String(byteArray, StandardCharsets.UTF_8);

        ObjectMapper mapper = new ObjectMapper();
        Object obj = mapper.readValue(json, Object.class);

        Map<String, String> map = (Map<String, String>) obj;

        return map.get(typePassword);
    }

    private static KeyManagerFactory getKeyManagerFacotory() throws KeyStoreException, IOException,
            CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {

        KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
        try (InputStream readStream = Files.newInputStream(Paths.get(CERT_PATH))) {
            keyStore.load(readStream, getPassword(KEYSTORE_PASSWORD).toCharArray());
        }

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, getPassword(KEYSTORE_PASSWORD).toCharArray());

        return keyManagerFactory;

    }


    private static TrustManagerFactory getTrustStoreManagerFactory() throws KeyStoreException, IOException,
            CertificateException, NoSuchAlgorithmException {

        KeyStore keyStore = KeyStore.getInstance(TRUSTSTORE_TYPE);
        try (InputStream readStream = Files.newInputStream(Paths.get(TRUSTSTORE_PATH))) {
            keyStore.load(readStream, getPassword(TRUSTSTORE_PASSWORD).toCharArray());
        }

        TrustManagerFactory trustManagerFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        return trustManagerFactory;

    }

    private static void startServer(KeyManagerFactory keyManagerFactory, TrustManagerFactory trustManagerFactory)
            throws InterruptedException {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpServerInitializer(keyManagerFactory, trustManagerFactory));

            Channel channel = serverBootstrap.bind(PORT).sync().channel();

            channel.closeFuture().sync();

        } finally {

            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
