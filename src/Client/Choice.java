package Client;

public enum Choice {
    X, O;

    public static Choice from(String choice) {

        if ("X".equals(choice)) {
            return X;
        }

        if ("O".equals(choice)) {
            return O;
        }

        return null;
    }
}
