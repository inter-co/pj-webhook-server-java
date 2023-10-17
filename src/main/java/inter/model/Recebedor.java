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
public class Recebedor {
    @JsonProperty(value = "codIspb")
    private String codIspb;
    @JsonProperty(value = "codAgencia")
    private String codAgencia;
    @JsonProperty(value = "digitoAgencia")
    private String digitoAgencia;
    @JsonProperty(value = "nroConta")
    private String nroConta;
    @JsonProperty(value = "tipoConta")
    private String tipoConta;
    @JsonProperty(value = "cpfCnpj")
    private String cpfCnpj;
    @JsonProperty(value = "nome")
    private String nome;
    @JsonProperty(value = "nomeInstituiacao")
    private String nomeInstituiacao;
}

