package inter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Erro {
    @JsonProperty(value = "codigoErro")
    private String codigoErro;
    @JsonProperty(value = "descricaoErro")
    private String descricaoErro;
    @JsonProperty(value = "codigoErroComplementar")
    private String codigoErroComplementar;
}

