package inter.flow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import inter.model.PixRecebido;
import inter.use.UsaListatPixPagamento;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;

import static inter.contants.Constants.CONTA_CORRENTE_HEADER;

public class BankingFlow {

    private final UsaListatPixPagamento usaBanking;

    public BankingFlow(UsaListatPixPagamento usaBanking) {
        this.usaBanking = usaBanking;
    }

    public void execute(Object msg, HttpRequest req) throws JsonProcessingException {

        PixRecebido body = getBody(msg);
        String contaCorrente = getContaCorrente(req);

        usaBanking.usaBanking(contaCorrente, body);

    }

    private PixRecebido getBody(Object msg) throws JsonProcessingException {

        HttpContent httpContent = (HttpContent) msg;
        ByteBuf content = httpContent.content();
        StringBuilder builderBody = new StringBuilder();

        if (content.isReadable()) {
            builderBody.append(content.toString(CharsetUtil.UTF_8));
        }

        return new ObjectMapper().readValue(builderBody.toString(),
                PixRecebido.class);
    }

    private String getContaCorrente(HttpRequest request) {
        return request.headers().get(CONTA_CORRENTE_HEADER);
    }

}
