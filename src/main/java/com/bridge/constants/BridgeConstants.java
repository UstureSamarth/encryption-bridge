package com.bridge.constants;


import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class BridgeConstants {
    public static final String BAV = "200.32.OFFUS";
    public static final String CDA = "220.32.OFFUS";
    public static final String BFD = "BFD";
    public static final String AEPS_AUTH = "AEPS_AUTH";
    public static final String DEMO_AUTH = "DEMO_AUTH";
    public static final String SEEDING_GEN_OTP = "SEEDING_GEN_OTP";
    public static final String AEPS_GEN_OTP = "AEPS_GEN_OTP";
    public static final String SEEDING_VAL_OTP = "SEEDING_VAL_OTP";
    public static final String SEEDING_BIO = "SEEDING_BIO";
    public static final String EKYC_GEN_OTP = "EKYC_GEN_OTP";
    public static final String EKYC_VAL_OTP = "EKYC_VAL_OTP";
    public static final String EKYC_BIO = "EKYC_BIO";
    public static final String GENERATE_OTP = "100.94.SL";
    public static final String VALIDATE_OTP = "100.95.SL";
    public static final String VALIDATE_OTP_2FA = "100.95.SL.2FA";
    public static final String GENERATE_OTP_API = "generate-login-otp";
    public static final String VALIDATE_OTP_API = "validate-login-otp";
    public static final Set<String> STAN_REQUIRED_TXN = Set.of(CDA);
    public static String BANK_LIST = "BANK_LIST";
    public static Set<String> NO_PARSE_TXN = Set.of(GENERATE_OTP, VALIDATE_OTP, BANK_LIST, AEPS_GEN_OTP);
    public static Set<String> XML_TXN = Set.of(BFD, AEPS_AUTH, DEMO_AUTH, EKYC_GEN_OTP, EKYC_VAL_OTP, EKYC_BIO, SEEDING_BIO, SEEDING_GEN_OTP, SEEDING_VAL_OTP);
    public static final Set<String> NO_PARSING_TXN = Stream.concat(NO_PARSE_TXN.stream(), XML_TXN.stream())
            .collect(Collectors.toUnmodifiableSet());
    public static Set<String> NO_BODY_TXN = Set.of(BANK_LIST);
}
