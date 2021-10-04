package deco.combatevolved.commands;

public enum ArgumentType {
    NUMBER {
        @Override
        public String toString() {
            return "number";
        }
    },
    TEXT {
        @Override
        public String toString() {
            return "text";
        }
    },
    USER {
        @Override
        public String toString() {
            return "username";
        }
    }
}
