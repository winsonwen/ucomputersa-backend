package com.ucomputersa.common.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.util.HashMap;

@Data
@ToString
public class AddressModel {
    @NotEmpty(message = "street address should be submitted")
    @Length(max = 88, message = "street address should be not longer than 88")
    private String streetAddress;

    private String additionalInfo;

    @NotEmpty(message = "city should be submitted")
    @Length(max = 36, message = "last name should be not longer than 36")
    private String city;

    @NotEmpty(message = "state should be submitted")
    @Pattern(regexp = "^(?i)(AL|AK|AZ|AR|CA|CO|CT|DE|FL|GA|HI|ID|IL|IN|IA|KS|KY|LA|ME|MD|MA|MI|MN|MS|MO|MT|NE|NV|NH|NJ|NM|NY|NC|ND|OH|OK|OR|PA|RI|SC|SD|TN|TX|UT|VT|VA|WA|WV|WI|WY)$", message = "please enter a validate state")
    private String state;

    @NotEmpty(message = "zipcode should be submitted")
    @Pattern(regexp = "^\\d{5}$", message = "The format of zipcode isn't correct")
    private String zipcode;

    //TODO stateZipcode 校验
    public static HashMap<String, String> stateZipcode = new HashMap<>() {{
        put("AK", "99501-99950");
        put("AL", "35004-36925");
        put("AR", "71601-72959");
        put("AR1", "75502-75502");
        put("AZ", "85001-86556");
        put("CA", "90001-96162");
        put("CO", "80001-81658");
        put("CT", "6001-6389");
        put("CT1", "6401-6928");
        put("DC", "20001-20039");
        put("DC1", "20042-20599");
        put("DC2", "20799-20799");
        put("DE", "19701-19980");
        put("FL", "32004-34997");
        put("GA", "30001-31999");
        put("GA1", "39901-39901");
        put("HI", "96701-96898");
        put("IA", "50001-52809");
        put("IA1", "68119-68120");
        put("ID", "83201-83876");
        put("IL", "60001-62999");
        put("IN", "46001-47997");
        put("KS", "66002-67954");
        put("KY", "40003-42788");
        put("LA", "70001-71232");
        put("LA1", "71234-71497");
        put("MA", "1001-2791");
        put("MA1", "5501-5544");
        put("MD", "20331-20331");
        put("MD1", "20335-20797");
        put("MD2", "20812-21930");
        put("ME", "3901-4992");
        put("MI", "48001-49971");
        put("MN", "55001-56763");
        put("MO", "63001-65899");
        put("MS", "38601-39776");
        put("MS1", "71233-71233");
        put("MT", "59001-59937");
        put("NC", "27006-28909");
        put("ND", "58001-58856");
        put("NE", "68001-68118");
        put("NE1", "68122-69367");
        put("NH", "3031-3897");
        put("NJ", "7001-8989");
        put("NM", "87001-88441");
        put("NV", "88901-89883");
        put("NY", "6390-6390");
        put("NY1", "10001-14975");
        put("OH", "43001-45999");
        put("OK", "73001-73199");
        put("OK1", "73401-74966");
        put("OR", "97001-97920");
        put("PA", "15001-19640");
        put("PR", "0-0");
        put("RI", "2801-2940");
        put("SC", "29001-29948");
        put("SD", "57001-57799");
        put("TN", "37010-38589");
        put("TX", "73301-73301");
        put("TX1", "75001-75501");
        put("TX2", "75503-79999");
        put("TX3", "88510-88589");
        put("UT", "84001-84784");
        put("VA", "20040-20041");
        put("VA1", "20040-20167");
        put("VA2", "22001-24658");
        put("VT", "5001-5495");
        put("VT1", "5601-5907");
        put("WA", "98001-99403");
        put("WI", "53001-54990");
        put("WV", "24701-26886");
        put("WY", "82001-83128");
    }};


}
