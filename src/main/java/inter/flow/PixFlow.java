package inter.flow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import inter.model.PixResponse;
import inter.use.UsaListaPix;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;

import static inter.contants.Constants.CONTA_CORRENTE_HEADER;

public class PixFlow {

    private final UsaListaPix usaListaPix;

    public PixFlow(UsaListaPix usaListaPix) {
        this.usaListaPix = usaListaPix;
    }

    public void execute(Object msg, HttpRequest req) throws JsonProcessingException {

        PixResponse body = getBody(msg);
        String contaCorrente = getContaCorrente(req);

        usaListaPix.usaListaPix(contaCorrente, body.getPix());

    }

    private PixResponse getBody(Object msg) throws JsonProcessingException {

        HttpContent httpContent = (HttpContent) msg;
        ByteBuf content = httpContent.content();
        StringBuilder builderBody = new StringBuilder();

        if (content.isReadable()) {
            builderBody.append(content.toString(CharsetUtil.UTF_8));
        }

        return new ObjectMapper().readValue(builderBody.toString(),
                PixResponse.class);
    }

    private String getContaCorrente(HttpRequest request) {
        return request.headers().get(CONTA_CORRENTE_HEADER);
    }

}
