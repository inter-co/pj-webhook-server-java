package inter.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import inter.flow.BankingFlow;
import inter.flow.CobrancaFlow;
import inter.flow.PixFlow;
import inter.use.UsaBoletoConciliacao;
import inter.use.UsaListaPix;
import inter.use.UsaListatPixPagamento;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.CharsetUtil;

import static inter.contants.Constants.BANKING_URI;
import static inter.contants.Constants.BOLETO_URI;
import static inter.contants.Constants.ERROR_MESSAGE;
import static inter.contants.Constants.PIX_URI;
import static inter.contants.Constants.SUCCESS_MESSAGE;
import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

public class Controller extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws JsonProcessingException {

        if (isCallValid(msg)) {

            HttpRequest request = (HttpRequest) msg;

            selectFlow(request, msg);

            buildSuccessResponse(request, ctx);

        } else {
            buildErrorResponse(msg, ctx);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private boolean isCallValid(Object msg) {
        return msg instanceof HttpRequest && msg instanceof HttpContent && isPost(msg);
    }

    private boolean isPost(Object msg) {
        HttpRequest request = (HttpRequest) msg;
        return request.method() == HttpMethod.POST;
    }

    private void selectFlow(HttpRequest request, Object msg) throws JsonProcessingException {

        String uri = request.uri();

        switch (uri) {

            case BOLETO_URI:
                UsaBoletoConciliacao usaBoletoConciliacao = new UsaBoletoConciliacao();
                CobrancaFlow cobrancaFlow = new CobrancaFlow(usaBoletoConciliacao);
                cobrancaFlow.execute(msg, request);
                break;
            case PIX_URI:
                UsaListaPix usaListaPix = new UsaListaPix();
                PixFlow pixFlow = new PixFlow(usaListaPix);
                pixFlow.execute(msg, request);
                break;
            case BANKING_URI:
                UsaListatPixPagamento usaListatPixPagamento = new UsaListatPixPagamento();
                BankingFlow bankingFlow = new BankingFlow(usaListatPixPagamento);
                bankingFlow.execute(msg, request);
                break;
            default:

        }
    }

    private void buildSuccessResponse(HttpRequest request, ChannelHandlerContext ctx) {

        boolean keepAlive = HttpUtil.isKeepAlive(request);

        FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), OK,
                Unpooled.wrappedBuffer(SUCCESS_MESSAGE.getBytes()));
        response.headers()
                .set(CONTENT_TYPE, TEXT_PLAIN)
                .setInt(CONTENT_LENGTH, response.content().readableBytes());

        if (keepAlive) {
            if (!request.protocolVersion().isKeepAliveDefault()) {
                response.headers().set(CONNECTION, KEEP_ALIVE);
            }
        } else {
            response.headers().set(CONNECTION, CLOSE);
        }

        ChannelFuture f = ctx.write(response);

        if (!keepAlive) {
            f.addListener(ChannelFutureListener.CLOSE);
        }

    }

    private void buildErrorResponse(Object msg, ChannelHandlerContext ctx) {

        HttpRequest request = (HttpRequest) msg;

        FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(),
                HttpResponseStatus.BAD_REQUEST);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, TEXT_PLAIN);
        response.content().writeBytes(ERROR_MESSAGE.getBytes(CharsetUtil.UTF_8));

        response.headers().set(CONNECTION, CLOSE);

        ChannelFuture f = ctx.write(response);
        f.addListener(ChannelFutureListener.CLOSE);
    }
}
