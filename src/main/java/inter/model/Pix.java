package inter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pix {
    @JsonProperty(value = "endToEndId")
    private String endToEndId;
    @JsonProperty(value = "txid")
    private String txid;
    @JsonProperty(value = "valor")
    private String valor;
    @JsonProperty(value = "chave")
    private String chave;
    @JsonProperty(value = "horario")
    private Date horario;
    @JsonProperty(value = "infoPagador")
    private String infoPagador;
    @JsonProperty(value = "devolucoes")
    private List<Devolucao> devolucoes;
}
