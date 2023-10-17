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
public class Devolucao {
    @JsonProperty(value = "id")
    private String id;
    @JsonProperty(value = "rtrId")
    private String rtrId;
    @JsonProperty(value = "valor")
    private String valor;
    @JsonProperty(value = "status")
    private String status;
    @JsonProperty(value = "motivo")
    private String motivo;
    @JsonProperty(value = "horario")
    private Horario horario;
}
