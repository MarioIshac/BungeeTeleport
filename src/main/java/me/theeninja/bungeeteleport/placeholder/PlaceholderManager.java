package me.theeninja.bungeeteleport.placeholder;

import org.bukkit.Bukkit;

import java.util.logging.Level;

/**
 * Utility class for the BungeeTeleport plugin
 *
 * @author TheeNinja
 */
public class PlaceholderManager {

    private Placeholder[] registeredPlaceholders;

    /**
     * Constructor for case-specific placeholder managers.
     *
     * @param registeredPlaceholders - Represents the placeholders to check and manage for this
     *                               placeholder manager
     */
    public PlaceholderManager(Placeholder[] registeredPlaceholders) {

        this.registeredPlaceholders = registeredPlaceholders;
    }

    /**
     * @param string - String including raw placeholders
     * @return string with updated placeholders
     */
    public String replacePlaceholders(String string) {

        for (Placeholder placeholder : registeredPlaceholders) {

            if (placeholder.getPlaceholderAction() == null) {

                Bukkit.getLogger().log(Level.WARNING,
                        "Placeholder issue occurred, failed to find replacement function for " + placeholder.getConfigurationRepresentation() + " please contact developer. " +
                                "This is NOT a user-caused issue.");
                continue;
            }

            string = placeholder.getPlaceholderAction().replace(string);
        }

        return string;
    }

    /**
     * Represents the implementation to override for specific placeholders.
     *
     * @author TheeNinja
     */
    public interface PlaceholderReplaceAction {

        /**
         * Method to replace placeholders in string with relevant case-specific values.
         *
         * @param string - string to replace placeholders in
         * @return updated string with modified placeholders
         */
        String replace(String string);
    }

    /**
     * Represents individual placeholder, containing representation and
     * method of managing placeholder replacement.
     *
     * @author Theeninja
     */
    public static class Placeholder {

        private String representation;
        private PlaceholderReplaceAction replaceAction;
        private String configurationRepresentation;

        /**
         * Constructor for producing attributes specific to placeholder.
         *
         * @param representation - placeholder identifier (enclosed in % in configuration file)
         * @param replaceAction  - implementation containing method on how to go about the replacement of placeholder
         */
        public Placeholder(String representation, PlaceholderReplaceAction replaceAction) {

            this.representation = representation;
            this.configurationRepresentation = "%" + representation + "%";
            this.replaceAction = replaceAction;
        }

        public Placeholder(String representation) {

            this.representation = representation;
            this.configurationRepresentation = "%" + representation + "%";
        }

        public String getConfigurationRepresentation() {

            return configurationRepresentation;
        }

        /**
         * @return Placeholder representation
         */
        public String getRepresentation() {

            return representation;
        }

        public void setRepresentation(String representation) {

            this.representation = representation;
        }

        /**
         * @return Placeholder's implementation for replacing representation with another value.
         */
        PlaceholderReplaceAction getPlaceholderAction() {

            return replaceAction;
        }

        public void setPlaceholderAction(PlaceholderReplaceAction replaceAction) {

            this.replaceAction = replaceAction;
        }
    }
}


