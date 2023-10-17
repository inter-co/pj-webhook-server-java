package inter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Horario {
    @JsonProperty(value = "solicitacao")
    private Date solicitacao;
    @JsonProperty(value = "liquidacao")
    private Date liquidacao;
}

