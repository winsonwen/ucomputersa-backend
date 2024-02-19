package ucomputersa.domain.beans;

import com.ucomputersa.common.model.AddressModel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@ToString
public class CreatePcServiceRequest {

    @NotEmpty(message = "service type should be submitted")
    private String serviceType;

    @NotEmpty(message = "Service Category should be submitted")
    private String serviceCategory;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @NotEmpty(message = "last name should be submitted")
    @Length(max = 36, message = "last name should be not longer than 36")
    private String lastName;

    @NotEmpty(message = "first name should be submitted")
    @Length(max = 36, message = "first name should be not longer than 36")
    private String firstName;

    @NotEmpty(message = "phone number should be submitted")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "please enter a validated phone number")
    private String phone;

    @NotEmpty(message = "email should be submitted")
    @Email(message = "please enter a validate email")
    private String email;

    private String description;

    private AddressModel address;

}
