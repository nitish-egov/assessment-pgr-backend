package digit.web.models.Idgen;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.request.RequestInfo;

/**
 * <h1>IdGenerationRequest</h1>
 * 
 * @author VISHAL_GENIUS
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IdGenerationRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	private List<IdRequest> idRequests;

}
