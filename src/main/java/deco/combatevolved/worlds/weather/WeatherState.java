package deco.combatevolved.worlds.weather;

public enum WeatherState {
	WEATHER_STATE_0 {
        @Override
        public String asLowerCase() {
            return WEATHER_STATE_0.toString().toLowerCase();
        }
        
        @Override
        public Integer asInteger() {
            return 0;
        }
    },
    WEATHER_STATE_1 {
        @Override
        public String asLowerCase() {
            return WEATHER_STATE_1.toString().toLowerCase();
        }
        
        @Override
        public Integer asInteger() {
            return 1;
        }
    },
    WEATHER_STATE_2 {
        @Override
        public String asLowerCase() {
            return WEATHER_STATE_2.toString().toLowerCase();
        }
        
        @Override
        public Integer asInteger() {
            return 2;
        }
    };

    public abstract String asLowerCase();
    public abstract Integer asInteger();
}
