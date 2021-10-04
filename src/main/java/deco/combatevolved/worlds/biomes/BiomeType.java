package deco.combatevolved.worlds.biomes;

/**
 * Special class to define accepted biome types
 */
public enum BiomeType {
    SNOW {
        @Override
        public String asLowerCase() {
            return SNOW.toString().toLowerCase();
        }
    },
    // This biome is the same as the LAKE biome except it can only appear at
    // a high altitude with a low moisture content
    ICE {
        @Override
        public String asLowerCase() {
            return ICE.toString().toLowerCase();
        }
    },
    TUNDRA {
        @Override
        public String asLowerCase() {
            return TUNDRA.toString().toLowerCase();
        }
    },
    MOUNTAIN_ROCKS {
        @Override
        public String asLowerCase() {
            return MOUNTAIN_ROCKS.toString().toLowerCase();
        }
    },
    SHRUBLAND {
        @Override
        public String asLowerCase() {
            return SHRUBLAND.toString().toLowerCase();
        }
    },
    // This biome does now require a height range but can be placed at any
    // altitude on the map where a high moisture content is available
    LAKE {
        @Override
        public String asLowerCase() {
            return LAKE.toString().toLowerCase();
        }
    },
    TEMPERATE_RAINFOREST {
        @Override
        public String asLowerCase() {
            return TEMPERATE_RAINFOREST.toString().toLowerCase();
        }
    },
    TEMPERATE_DESERT {
        @Override
        public String asLowerCase() {
            return TEMPERATE_DESERT.toString().toLowerCase();
        }
    },
    GRASSLAND {
        @Override
        public String asLowerCase() {
            return GRASSLAND.toString().toLowerCase();
        }
    },
    TROPICAL_RAINFOREST {
      @Override
      public String asLowerCase() {
          return TROPICAL_RAINFOREST.toString().toLowerCase();
      }
    },
    SUBTROPICAL_DESERT {
        @Override
        public String asLowerCase() {
            return SUBTROPICAL_DESERT.toString().toLowerCase();
        }
    },
    // This biome is technically the same as SUBTROPICAL_DESERT, it's more
    // used to distinguish between a Tile that is in land or one that is next
    // to an OCEAN Tile
    BEACH {
        @Override
        public String asLowerCase() {
            return BEACH.toString().toLowerCase();
        }
    },
    OCEAN {
        @Override
        public String asLowerCase() {
            return OCEAN.toString().toLowerCase();
        }
    };

    public abstract String asLowerCase();
}
