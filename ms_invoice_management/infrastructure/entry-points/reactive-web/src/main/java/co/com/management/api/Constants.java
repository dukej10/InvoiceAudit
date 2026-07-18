package co.com.management.api;

import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public class Constants {

    public static final String ID_PATH = "id";
    public static final String NUM_PARAM = "num";
    public static final String TYPE_PARAM = "type";
    public static final String PAGE_PARAM = "page";
    public static final String SIZE_PARAM = "size";
    public static final Set<String> PAGINATION_PARAMS = Set.of(Constants.PAGE_PARAM, Constants.SIZE_PARAM);

}
