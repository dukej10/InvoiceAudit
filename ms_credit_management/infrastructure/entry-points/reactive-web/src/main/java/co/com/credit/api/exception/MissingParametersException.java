package co.com.credit.api.exception;

import java.util.Set;

public class MissingParametersException extends RuntimeException {
    private final Set<String> missingParameters;

    public MissingParametersException(Set<String> missing) {
        super("Faltan parámetros obligatorios");
        this.missingParameters = Set.copyOf(missing);
    }

    public Set<String> getMissingParameters() {
        return missingParameters;
    }
}