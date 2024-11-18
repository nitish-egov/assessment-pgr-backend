package digit.web.models.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.egov.common.contract.response.ResponseInfo;
import digit.web.models.User;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserDetailResponse {

  @JsonProperty("responseInfo")
  ResponseInfo responseInfo;

  @JsonProperty("user")
  List<User> user;
}
