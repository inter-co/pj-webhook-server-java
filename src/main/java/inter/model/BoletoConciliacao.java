package inter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoletoConciliacao {
    @JsonProperty(value = "idSolicitacao")
    private String idSolicitacao;
    @JsonProperty(value = "nossoNumero")
    private String nossoNumero;
    @JsonProperty(value = "seuNumero")
    private String seuNumero;
    @JsonProperty(value = "motivoCancelamento")
    private String motivoCancelamento;
    @JsonProperty(value = "situacao")
    private String situacao;
    @JsonProperty(value = "dataHoraSituacao")
    private String dataHoraSituacao;
    @JsonProperty(value = "valorNominal")
    private BigDecimal valorNominal;
    @JsonProperty(value = "valorTotalRecebimento")
    private BigDecimal valorTotalRecebimento;
    @JsonProperty(value = "codigoBarras")
    private String codigoBarras;
    @JsonProperty(value = "linhaDigitavel")
    private String linhaDigitavel;
}
