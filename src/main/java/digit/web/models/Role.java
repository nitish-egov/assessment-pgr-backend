package digit.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * minimal representation of the Roles in the system to be carried along in UserInfo with RequestHeader meta data. Actual authorization service to extend this to have more role related attributes 
 */
@Schema(description = "minimal representation of the Roles in the system to be carried along in UserInfo with RequestHeader meta data. Actual authorization service to extend this to have more role related attributes ")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-11-18T11:04:31.362244598+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role   {
        @JsonProperty("name")

        @Size(max=64)         private String name = null;

        @JsonProperty("code")
          @NotNull

        @Size(max=64)         private String code = null;

        @JsonProperty("tenantId")
          @NotNull

        @Size(max=64)         private String tenantId = null;

        @JsonProperty("description")

                private String description = null;


}
