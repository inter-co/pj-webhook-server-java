package inter.initializer;

import inter.controller.Controller;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.SslProtocols;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static inter.contants.Constants.MAX_CONTENT_LENGTH;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    private final TrustManagerFactory trustManagerFactory;
    private final KeyManagerFactory keyManagerFactory;

    public HttpServerInitializer(KeyManagerFactory keyManagerFactory, TrustManagerFactory trustManagerFactory) {
        this.trustManagerFactory = trustManagerFactory;
        this.keyManagerFactory = keyManagerFactory;
    }

    @Override
    public void initChannel(SocketChannel ch) throws NoSuchAlgorithmException, KeyManagementException {

        ChannelPipeline pipeline = ch.pipeline();

        SSLContext serverContext = SSLContext.getInstance(SslProtocols.TLS_v1_2);
        serverContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        SSLEngine sslEngine = serverContext.createSSLEngine();
        sslEngine.setUseClientMode(false);
        sslEngine.setNeedClientAuth(true);

        pipeline.addLast(new SslHandler(sslEngine));
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(MAX_CONTENT_LENGTH));
        pipeline.addLast(new HttpServerExpectContinueHandler());
        pipeline.addLast(new Controller());
    }
}
