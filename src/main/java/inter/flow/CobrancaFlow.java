package inter.flow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import inter.model.BoletoConciliacao;
import inter.use.UsaBoletoConciliacao;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;

import static inter.contants.Constants.CONTA_CORRENTE_HEADER;

public class CobrancaFlow {

    private final UsaBoletoConciliacao usaBoletoConciliacao;

    public CobrancaFlow(UsaBoletoConciliacao usaBoletoConciliacao) {
        this.usaBoletoConciliacao = usaBoletoConciliacao;
    }

    public void execute(Object msg, HttpRequest req) throws JsonProcessingException {

        BoletoConciliacao body = getBody(msg);
        String contaCorrente = getContaCorrente(req);

        usaBoletoConciliacao.usaDadosBoleto(contaCorrente, body);

    }

    private BoletoConciliacao getBody(Object msg) throws JsonProcessingException {

        HttpContent httpContent = (HttpContent) msg;
        ByteBuf content = httpContent.content();
        StringBuilder builderBody = new StringBuilder();

        if (content.isReadable()) {
            builderBody.append(content.toString(CharsetUtil.UTF_8));
        }

        return new ObjectMapper().readValue(builderBody.toString(),
                BoletoConciliacao.class);
    }

    private String getContaCorrente(HttpRequest request) {
        return request.headers().get(CONTA_CORRENTE_HEADER);
    }

}
