package inter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PixRecebido {
    @JsonProperty(value = "endToEnd")
    private String endToEnd;
    @JsonProperty(value = "valor")
    private BigDecimal valor;
    @JsonProperty(value = "status")
    private String status;
    @JsonProperty(value = "tipoMovimentacao")
    private String tipoMovimentacao;
    @JsonProperty(value = "dataHoraMovimento")
    private String dataHoraMovimento;
    @JsonProperty(value = "dataHoraSolicitacao")
    private String dataHoraSolicitacao;
    @JsonProperty(value = "chave")
    private String chave;
    @JsonProperty(value = "endToEndOriginal")
    private String endToEndOriginal;
    @JsonProperty(value = "recebedor")
    private Recebedor recebedor;
    @JsonProperty(value = "erros")
    private List<Erro> erros;
}
